package CryptoSystem.gRPCwrapper;

import CryptoSystem.ZooKeeper.ZKManager;
import CryptoSystem.types.TR;
import CryptoSystem.types.TX;
import CryptoSystem.types.UTxO;
import io.grpc.stub.StreamObserver;
import notsystemserver.grpc.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Semaphore;


public class SystemServerImpl extends SystemServerGrpc.SystemServerImplBase {

    static private Integer shard_id;
    static private Integer server_id;
    static private Integer serverGrpcPort;
    static private Integer zkPort;
    static private ZKManager myZK;

    Map<byte[], List<UTxO>> spent_utxos;
    Map<byte[], List<UTxO>> unspent_utxos;

    static Semaphore atomic_vote_Mutex = new Semaphore(1);



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

    @Override
    public void initServer(Init_Server_Args request,
                           StreamObserver<Response_status> responseObserver) {
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
}
