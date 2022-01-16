package CryptoSystem.gRPCwrapper;

import CryptoSystem.SystemServer.Spring.RESTresponse;
import CryptoSystem.ZooKeeper.ZKManager;
import CryptoSystem.types.TR;
import CryptoSystem.types.TX;
import CryptoSystem.types.UTxO;
import CryptoSystem.types.uint128;
import com.google.common.io.ByteStreams;
import com.google.common.util.concurrent.ListenableFuture;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import io.grpc.stub.StreamObserver;
import notsystemserver.grpc.*;
import org.apache.zookeeper.KeeperException;

import java.io.UnsupportedEncodingException;
import java.math.BigInteger;
import java.nio.ByteBuffer;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
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

    Map<uint128, List<UTxO>> spent_utxos;
    Map<uint128, List<UTxO>> unspent_utxos;

    List<TX> txList;


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
            List<String> servers = myZK.getChildren("/Shards/" + shard);
            System.out.println("[getLeaderPort]: Shard: "+ shard + " ,List: " + servers.toString());
            servers.sort(Comparator.naturalOrder());
            return getServerPort(shard, servers.get(0));

        } catch (InterruptedException | KeeperException | NullPointerException e) {
            e.printStackTrace();
            return -1;
        }
    }

    private static Integer getServerPort(String shard, String server) {
        try {
            Integer server_port = Integer.parseInt(myZK.getZNodeData("/Shards/" + shard + "/" + server,false));
            return server_port;

        } catch (InterruptedException | KeeperException | NullPointerException | UnsupportedEncodingException e) {
            e.printStackTrace();
            return -1;
        }
    }


//    private byte[] hash128bit(String input) throws NoSuchAlgorithmException {
//        String hash = "35454B055CC325EA1AF2126E27707052";
//        String password = "ILoveJava";
//
//        MessageDigest md = MessageDigest.getInstance("MD5");
//        md.update(input.getBytes());
//        byte[] digest = md.digest();
//
//        byte[] res = new byte[8];
//        for (int i = 0; i < 8; i++)
//            res[i]=digest[i];
//        return res;
//    }


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
                List<String> servers = myZK.getChildren("/Shards/" + shard_id.toString());
                servers.remove(server_id.toString());
                for (String server:servers) {
                    Integer server_port = Integer.parseInt(myZK.getZNodeData("/Shards/" + shard_id.toString() + "/" + server,false));
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

        spent_utxos = new HashMap<uint128, List<UTxO>>();
        unspent_utxos = new HashMap<uint128, List<UTxO>>();
        txList = new ArrayList<TX>();

        if (shard_id == 0) {
            uint128 zero = new uint128(0,0);
            TR gen_tr = new TR(zero, -1);
            List<TR> tr_list = new ArrayList<TR>();
            tr_list.add(gen_tr);
            TX gen_tx = new TX(zero, 0,null, tr_list);

            UTxO gen_utxo = new UTxO(zero, zero);
            List<UTxO> gen_list = new ArrayList<UTxO>();
            gen_list.add(gen_utxo);
            unspent_utxos.put(zero,gen_list);
        }
        Response_status response = Response_status.newBuilder().setResponse("OK").build();
        responseObserver.onNext(response);
        responseObserver.onCompleted();
    }

//    @Override
//    public void submitTransactionList(notsystemserver.grpc.Transaction_list request,
//                                      io.grpc.stub.StreamObserver<notsystemserver.grpc.Response_status> responseObserver) {
//        Response_status response;
//        List<TX> txes = new ArrayList<TX>();
//        List<TX_m> requests = request.getRequestsList();
//        List<UTxO> utxos = new ArrayList<UTxO>();
//        List<TX_m> requests_processed = new ArrayList<TX_m>();
//        byte[] sender = requests.get(0).getSender().toByteArray();
//        for (TX_m tx_request:requests) {
//            TX tx = new TX(tx_request);
//            if (!tx.getSender().equals(sender)) {
//                String response_string = "Transaction list contains multiple senders";
//                response = Response_status.newBuilder().setResponse(response_string).build();
//                responseObserver.onNext(response);
//                responseObserver.onCompleted();
//                return;
//            }
//            for (UTxO utxo: tx.getUtxos()) {
//                if (utxos.contains(utxo)) {
//                    String response_string = "UTxO with TX_id: " + utxo.getTx_id().toString() + " and address: "
//                            + utxo.getAddress().toString() + " appeared more than once";
//                    response = Response_status.newBuilder().setResponse(response_string).build();
//                    responseObserver.onNext(response);
//                    responseObserver.onCompleted();
//                    return;
//                } else if (spent_utxos.get(tx.getSender()).contains(utxo)) {
//                    String response_string = "UTxO with TX_id: " + utxo.getTx_id().toString() + " and address: "
//                            + utxo.getAddress().toString() + " was already spent";
//                    response = Response_status.newBuilder().setResponse(response_string).build();
//                    responseObserver.onNext(response);
//                    responseObserver.onCompleted();
//                    return;
//                } else if (!utxo.getAddress().equals(tx.getSender())) {
//                    String response_string = "UTxO with TX_id: " + utxo.getTx_id().toString() + " and address: "
//                            + utxo.getAddress().toString() + " doesn't belong to sender:" + tx.getSender().toString();
//                    response = Response_status.newBuilder().setResponse(response_string).build();
//                    responseObserver.onNext(response);
//                    responseObserver.onCompleted();
//                    return;
//                }
//                utxos.add(utxo);
//            }
//            if (!tx.validate(tx_request.getAmount())) {
//                String response_string = "TX with TX_id: " + tx.getTx_id().toString() + " doesn't have sufficient amount of coins";
//                response = Response_status.newBuilder().setResponse(response_string).build();
//                responseObserver.onNext(response);
//                responseObserver.onCompleted();
//                return;
//            }
//
//            System.currentTimeMillis();
//
//
//            tx.process();
//
//        }
//
//
//
//
//
//
//        String response_string = "OK";
//        response = Response_status.newBuilder().setResponse(response_string).build();
//        responseObserver.onNext(response);
//        responseObserver.onCompleted();
//    }


    @Override
    public void publishTransaction(notsystemserver.grpc.Submit_Transaction_list_Req request,
                                   io.grpc.stub.StreamObserver<notsystemserver.grpc.Response_status> responseObserver) {
        boolean republish = false;
        List<TX_m> requestsList = request.getRequestsList();
        for (TX_m req: requestsList) {
            TX tx = new TX(req);
            if (txList.contains(tx)) {
                Response_status response = Response_status.newBuilder().setResponse("OK").build();
                responseObserver.onNext(response);
                responseObserver.onCompleted();
                return;
            }
            txList.add(tx);
            if (Arrays.hashCode(tx.getSender()) == shard_id) {
                assert (tx.isProcessed());
                // remove old utxo from unspent list and add to spent list
                List<UTxO> current_spent_tx = tx.getUtxos();
                for (UTxO utxo:current_spent_tx) {
                    List<UTxO> current_spent;
                    if (spent_utxos.containsKey(tx.getSender())) {
                        current_spent = spent_utxos.get(tx.getSender());
                        assert (!current_spent.contains(utxo));
                    } else
                        current_spent = new ArrayList<>();
                    current_spent.add(utxo);
                    spent_utxos.put(tx.getSender(),current_spent);

                    List<UTxO> current_unspent;
                    if (unspent_utxos.containsKey(tx.getSender())) {
                        current_unspent = unspent_utxos.get(tx.getSender());
                    } else
                        current_unspent = new ArrayList<>();
                    current_unspent.remove(utxo);
                    unspent_utxos.put(tx.getSender(),current_unspent);
                }

                // add new utxo to unspentlist
                UTxO uTxO_sender = new UTxO(tx, tx.getSender());
                if (uTxO_sender.getAmount() > 0) {
                    List<UTxO> current_unspent;
                    if (unspent_utxos.containsKey(tx.getSender()))
                        current_unspent = unspent_utxos.get(tx.getSender());
                    else
                        current_unspent = new ArrayList<UTxO>();
                    if (current_unspent.contains(uTxO_sender))
                        System.out.println("utxo already exists: tx_id" + Arrays.toString(tx.getTx_id()) + " , receiver:" + Arrays.toString(tx.getSender()));
                    current_unspent.add(uTxO_sender);
                    unspent_utxos.put(tx.getSender(), current_unspent);
                }
            }
            if (Arrays.hashCode(tx.getReceiver()) == shard_id) {
                assert (tx.isProcessed());
                // add new utxo to unspentlist
                UTxO uTxO_receiver = new UTxO(tx, tx.getReceiver());
                if (uTxO_receiver.getAmount() > 0) {
                    List<UTxO> current_unspent;
                    if (unspent_utxos.containsKey(tx.getReceiver()))
                        current_unspent = unspent_utxos.get(tx.getReceiver());
                    else
                        current_unspent = new ArrayList<UTxO>();
                    if (current_unspent.contains(uTxO_receiver))
                        System.out.println("utxo already exists: tx_id" + Arrays.toString(tx.getTx_id()) + " , receiver:" + Arrays.toString(tx.getReceiver()));
                    current_unspent.add(uTxO_receiver);
                    unspent_utxos.put(tx.getReceiver(), current_unspent);
                }
            }

            if (Arrays.hashCode(tx.getSender()) == shard_id)
                republish =  true;
        }


        if (republish)
            send_publishTransaction(request);
    }


    /*
    * * * * * * * * * * *
    * Client functions  *
    * * * * * * * * * * *
    */

    public void send_publishTransaction(Submit_Transaction_list_Req request) {
        System.out.println("Entered send_publishTransaction " + shard_id.toString() + " " + server_id.toString()
                            + "\nTX_id:" + request.getRequests(0).getTxId().toString());
        try {
            List<String> shards = myZK.getChildren("/Shards");
            for (String shard:shards) {
                List<String> servers = myZK.getChildren("/Shards/"+shard);
                for (String server:servers) {
                    if (server.equals(server_id.toString()) && shard.equals(shard_id.toString()))
                        continue;
                    System.out.println("in send_initServer: sending init to shard: " + shard);
                    Integer server_port = getServerPort(shard, server);
                    ManagedChannel channel = ManagedChannelBuilder.forAddress("localhost", server_port)
                            .usePlaintext()
                            .build();
                    SystemServerGrpc.SystemServerBlockingStub stub = SystemServerGrpc.newBlockingStub(channel);
                    stub.publishTransaction(request);
                    channel.shutdown();
                }
            }

        } catch (InterruptedException | KeeperException | NullPointerException e) {
            e.printStackTrace();
            return;
        }
        return;

    }

    public static boolean send_initServer() {
        System.out.println("Entered send_initServer " + shard_id.toString() + " " + server_id.toString());
        try {
            List<String> shards = myZK.getChildren("/Shards");
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
