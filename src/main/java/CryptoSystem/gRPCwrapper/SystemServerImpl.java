package CryptoSystem.gRPCwrapper;

import CryptoSystem.SystemServer.Spring.RESTresponse;
import CryptoSystem.ZooKeeper.ZKManager;
import CryptoSystem.types.TR;
import CryptoSystem.types.TX;
import CryptoSystem.types.UTxO;
import com.google.common.util.concurrent.ListenableFuture;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import io.grpc.stub.StreamObserver;
import notsystemserver.grpc.*;
import org.apache.zookeeper.KeeperException;

import java.io.UnsupportedEncodingException;
import java.util.*;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Semaphore;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;


public class SystemServerImpl extends SystemServerGrpc.SystemServerImplBase {

    static private Integer shard_id;
    static private Integer server_id;
    static private Integer serverGrpcPort;
    static private Integer zkPort;
    static private ZKManager myZK;

    Map<byte[], List<UTxO>> spent_utxos;
    Map<byte[], List<UTxO>> unspent_utxos;

    static Semaphore atomic_vote_Mutex = new Semaphore(1);

    /*
     * * * * * * * * * * * *
     * Internal functions  *
     * * * * * * * * * * * *
     */

    public static void setShard_id(Integer shard_id) {
        SystemServerImpl.shard_id = shard_id;
    }

    public static void setServer_id(Integer server_id) {
        SystemServerImpl.server_id = server_id;
    }

    public static void setServerGrpcPort(Integer serverGrpcPort) {
        SystemServerImpl.serverGrpcPort = serverGrpcPort;
    }

    public static void setZkPort(Integer zkPort) {
        SystemServerImpl.zkPort = zkPort;
    }

    public static void setMyZK(ZKManager myZK) {
        SystemServerImpl.myZK = myZK;
    }

    public static Integer getGrpcPort() {
        return serverGrpcPort;
    }

    private static Integer getLeaderPort(String shard) {
        try {
            List<String> servers = myZK.getChildren("/" + shard);
            servers.sort(Comparator.naturalOrder());
            Integer server_port = Integer.parseInt(myZK.getZNodeData("/" + shard + "/" + servers.get(0),false));
            return server_port;

        } catch (InterruptedException | KeeperException | NullPointerException | UnsupportedEncodingException e) {
            e.printStackTrace();
            return -1;
        }
    }

    /*
     * * * * * * * * * * *
     * Server functions  *
     * * * * * * * * * * *
     */

    @Override
    public void initServer(Init_Server_Args request,
                           StreamObserver<Response_status> responseObserver) {
        System.out.println("Entered init server " + shard_id.toString() + " " + server_id.toString());

        if (getLeaderPort(shard_id.toString()) == serverGrpcPort) {
            try {
                List<String> servers = myZK.getChildren("/" + shard_id.toString());
                servers.remove(server_id.toString());
                for (String server:servers) {
                    Integer server_port = Integer.parseInt(myZK.getZNodeData("/" + shard_id.toString() + "/" + server,false));
                    ManagedChannel channel = ManagedChannelBuilder.forAddress("localhost", server_port)
                            .usePlaintext()
                            .build();
                    SystemServerGrpc.SystemServerBlockingStub stub = SystemServerGrpc.newBlockingStub(channel);
                    Init_Server_Args args = Init_Server_Args.newBuilder().build();
                    Response_status res_grpc = stub.initServer(args);
                    channel.shutdown();
                    if (!(res_grpc.getResponse().equals("OK"))) {
                        responseObserver.onNext(res_grpc);
                        responseObserver.onCompleted();
                    }
                }

            } catch (InterruptedException | KeeperException | NullPointerException | UnsupportedEncodingException e) {
                e.printStackTrace();
                Response_status response = Response_status.newBuilder().setResponse("Catched Exception").build();
                responseObserver.onNext(response);
                responseObserver.onCompleted();
            }
        }

        spent_utxos = new HashMap<byte[], List<UTxO>>();
        unspent_utxos = new HashMap<byte[], List<UTxO>>();

        if (shard_id == 0) {
            TR gen_tr = new TR(new byte[UTxO.SIZE_OF_ADDESSS], -1);
            UTxO gen_utxo = new UTxO(new byte[UTxO.SIZE_OF_TXID], gen_tr);
            List<UTxO> gen_list = new ArrayList<UTxO>();
            gen_list.add(gen_utxo);
            unspent_utxos.put(new byte[UTxO.SIZE_OF_ADDESSS],gen_list);
        }
        Response_status response = Response_status.newBuilder().setResponse("OK").build();

        responseObserver.onNext(response);
        responseObserver.onCompleted();
    }

    @Override
    public void submitTransactionList(notsystemserver.grpc.Submit_Transaction_list_Req request,
                                      io.grpc.stub.StreamObserver<notsystemserver.grpc.Response_status> responseObserver) {
        Response_status response;
        List<TX> txes = new ArrayList<TX>();
        List<TX_m> requests = request.getRequestsList();
        List<UTxO> utxos = new ArrayList<UTxO>();
        byte[] sender = requests.get(0).getSender().toByteArray();
        for (TX_m tx_request:requests) {
            TX tx = new TX(tx_request);
            if (!tx.getSender().equals(sender)) {
                String response_string = "Transaction list contains multiple senders";
                response = Response_status.newBuilder().setResponse(response_string).build();
                responseObserver.onNext(response);
                responseObserver.onCompleted();
                return;
            }
            for (UTxO utxo: tx.getUtxos()) {
                if (utxos.contains(utxo)) {
                    String response_string = "UTxO with TX_id: " + utxo.getTx_id().toString() + " and address: "
                            + utxo.getAddress().toString() + " appeared more than once";
                    response = Response_status.newBuilder().setResponse(response_string).build();
                    responseObserver.onNext(response);
                    responseObserver.onCompleted();
                    return;
                } else if (spent_utxos.get(tx.getSender()).contains(utxo)) {
                    String response_string = "UTxO with TX_id: " + utxo.getTx_id().toString() + " and address: "
                            + utxo.getAddress().toString() + " was already spent";
                    response = Response_status.newBuilder().setResponse(response_string).build();
                    responseObserver.onNext(response);
                    responseObserver.onCompleted();
                    return;
                } else if (!utxo.getAddress().equals(tx.getSender())) {
                    String response_string = "UTxO with TX_id: " + utxo.getTx_id().toString() + " and address: "
                            + utxo.getAddress().toString() + " doesn't belong to sender:" + tx.getSender().toString();
                    response = Response_status.newBuilder().setResponse(response_string).build();
                    responseObserver.onNext(response);
                    responseObserver.onCompleted();
                    return;
                }
                utxos.add(utxo);
            }
            if (!tx.validate(tx_request.getAmount())) {
                String response_string = "TX with TX_id: " + tx.getTx_id().toString() + " doesn't have sufficient amount of coins";
                response = Response_status.newBuilder().setResponse(response_string).build();
                responseObserver.onNext(response);
                responseObserver.onCompleted();
                return;
            }
        }
        String response_string = "OK";
        response = Response_status.newBuilder().setResponse(response_string).build();
        responseObserver.onNext(response);
        responseObserver.onCompleted();


    }


    /*
    * * * * * * * * * * *
    * Client functions  *
    * * * * * * * * * * *
    */

    public static boolean send_initServer() {
        System.out.println("Entered send_initServer " + shard_id.toString() + " " + server_id.toString());
        try {
            List<String> shards = myZK.getChildren("/");
            for (String shard:shards) {
                System.out.println("in send_initServer: sending init to shard: " + shard);
                Integer server_port = getLeaderPort(shard);
                ManagedChannel channel = ManagedChannelBuilder.forAddress("localhost", server_port)
                        .usePlaintext()
                        .build();
                SystemServerGrpc.SystemServerBlockingStub stub = SystemServerGrpc.newBlockingStub(channel);
                Init_Server_Args args = Init_Server_Args.newBuilder().build();
                Response_status res_grpc = stub.initServer(args);
                if (!(res_grpc.getResponse().equals("OK"))) {
                    channel.shutdown();
                    return false;
                }
                channel.shutdown();
            }

        } catch (InterruptedException | KeeperException | NullPointerException e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

}
