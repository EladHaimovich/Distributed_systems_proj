package CryptoSystem.gRPCwrapper;

import CryptoSystem.SystemServer.ServerApp;
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
import java.text.DecimalFormat;
import java.util.*;
import java.util.concurrent.*;
import java.util.stream.Collectors;


public class SystemServerImpl extends SystemServerGrpc.SystemServerImplBase {

    static private Integer shard_id;
    static private Integer server_id;
    static private Integer serverGrpcPort;
    static private Integer zkPort;
    static private ZKManager myZK;

//    Map<uint128, List<UTxO>> spent_utxos;
//    Map<uint128, List<UTxO>> unspent_utxos;

    List<UTxO> spent_utxos;
    List<UTxO> unspent_utxos;

    List<TX> txList;




    static Semaphore atomic_transaction_Mutex = new Semaphore(1);

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

    private static Integer getLeader(Integer shard) {
        try {
            List<String> servers = myZK.getChildren("/Shards/" + shard);
            System.out.println("[getLeaderPort]: Shard: "+ shard + " ,List: " + servers.toString());
            servers.sort(Comparator.naturalOrder());
            Integer server = Integer.parseInt(servers.get(0));
            return server;

        } catch (InterruptedException | KeeperException | NullPointerException e) {
            e.printStackTrace();
            return -1;
        }
    }

    private static String getHostName(Integer shard, Integer server) {
        return "server" + new DecimalFormat("00").format(shard).toString() +
                new DecimalFormat("00").format(server).toString() + ".local";
    }

    private static Integer getServerPort(Integer shard, Integer server) {
        try {
            Integer server_port = Integer.parseInt(myZK.getZNodeData("/Shards/" + shard.toString() + "/" + server.toString(),false));
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

        if (getLeader(shard_id) == server_id) {
            try {
                List<String> servers = myZK.getChildren("/Shards/" + shard_id.toString());
                servers.remove(server_id.toString());
                for (String server:servers) {
                    Integer server_port = Integer.parseInt(myZK.getZNodeData("/Shards/" + shard_id.toString() + "/" + server,false));
                    String hostname = getHostName(shard_id, Integer.parseInt(server));
                    System.out.println("sending to " + hostname + ":" + server_port.toString());
                    ManagedChannel channel = ManagedChannelBuilder.forTarget(hostname + ":" + server_port.toString())
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

        for (int i = 0; i < 10; i++) {
            try {
                System.out.println("i = " +Integer.toString(i) + ", " + Long.toUnsignedString(myZK.generate_timestamp()));
            } catch (Exception e) {
                e.printStackTrace();
            }

        }



//        spent_utxos = new HashMap<uint128, List<UTxO>>();
//        unspent_utxos = new HashMap<uint128, List<UTxO>>();
        spent_utxos = new ArrayList<UTxO>();
        unspent_utxos = new ArrayList<UTxO>();
        txList = new ArrayList<TX>();

        if (shard_id == 0) {
//            uint128 zero = new uint128(0,0);
//            List<UTxO> gen_list = new ArrayList<UTxO>();
//            gen_list.add(UTxO.genUTxO());
//            unspent_utxos.put(zero, gen_list);
            unspent_utxos.add(UTxO.genUTxO());
            txList.add(TX.gen_TX());
        }
        Response_status response = Response_status.newBuilder().setResponse("OK").build();
        responseObserver.onNext(response);
        responseObserver.onCompleted();
    }

    @Override
    public void submitTransaction(notsystemserver.grpc.TX_m request,
                                  io.grpc.stub.StreamObserver<notsystemserver.grpc.Response_status> responseObserver) {

        atomic_transaction_Mutex.acquireUninterruptibly();
        boolean transaction_failed = false;
        TX transaction = new TX(request);
        uint128 sender = transaction.getUtxos().get(0).getAddress();
        assert (sender.hashCode()%ServerApp.NUM_OF_SHARDS == shard_id);
        List<UTxO> processed_utxos = new ArrayList<UTxO>();
        String response_string = null;
        for (UTxO utxo: transaction.getUtxos()) {
            if (processed_utxos.contains(utxo)) {
                response_string = "UTxO " +utxo.toString() + " appeared twice in transaction: " + transaction.getTx_id();
                transaction_failed = true;
                break;
            } else if (spent_utxos.contains(utxo)) {
                response_string = "UTxO " + utxo.toString() + " was already spent";
                transaction_failed = true;
                break;
            } else if (!utxo.getAddress().equals(sender)) {
                response_string = "UTxO " + utxo.toString() + " doesn't belong to sender " + sender.toString();
                transaction_failed = true;
                break;
            }
            processed_utxos.add(utxo);
        }
        List<uint128> prev_transtactions_ids = processed_utxos.stream()
                        .map(UTxO::getTx_id)
                        .collect(Collectors.toList());

        List<TX> prev_transactions = txList.stream()
                        .filter(tx -> prev_transtactions_ids.contains(tx.getTx_id()))
                        .collect(Collectors.toList());

        long utxo_sum = prev_transactions.stream()
                        .map(x -> x.getTrs().stream()
                                .filter(tr -> tr.getAddress().equals(sender))
                                .map(TR::getAmount)
                                .reduce(0L, Long::sum))
                        .reduce(0L, Long::sum);

        long trs_sum = transaction.getTrs().stream().map(TR::getAmount).reduce(0L, Long::sum);
        if (!transaction_failed && utxo_sum != trs_sum) {
            response_string = "value of UTxOs doesn't match value of TRs";
            transaction_failed = true;
        }
        if(response_string == null) {
            /* update database */
            spent_utxos.addAll(transaction.getUtxos());
            unspent_utxos.removeAll(transaction.getUtxos());
            List<UTxO> shards_utxos = transaction.getTrs().stream()
                        .filter(tr -> shard_id.equals(tr.getAddress().hashCode()%ServerApp.NUM_OF_SHARDS))
                        .map(tr -> new UTxO(transaction.getTx_id(), tr.getAddress())).collect(Collectors.toList());
            unspent_utxos.addAll(shards_utxos);
            txList.add(transaction);

            /* broadcast to all the servers */

            Transaction_list request_list = Transaction_list.newBuilder().addTransactions(request).build();
            send_publishTransactions(request_list);
            response_string = "OK";
        }
        atomic_transaction_Mutex.release();
        Response_status response = Response_status.newBuilder().setResponse(response_string).build();
        responseObserver.onNext(response);
        responseObserver.onCompleted();
        return;

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
    public void publishTransaction(notsystemserver.grpc.Transaction_list request,
                                   io.grpc.stub.StreamObserver<notsystemserver.grpc.Response_status> responseObserver) {
        String response_string = null;

        List<TX> transactions = request.getTransactionsList().stream().map(TX::new).collect(Collectors.toList());
        List<TX> relevant_transactions = new ArrayList<TX>();
        for (TX tx:transactions) {
            List<TR> trs = tx.getTrs().stream()
                        .filter(tr -> shard_id.equals(tr.getAddress().hashCode()%ServerApp.NUM_OF_SHARDS))
                        .collect(Collectors.toList());
            if (!trs.isEmpty()) {
                relevant_transactions.add(tx);
            }
        }

        /* if the first transaction is new (all other should be as well), add them to database and broadcast to other nodes */
        if (!txList.contains(relevant_transactions.get(0))) {
            atomic_transaction_Mutex.acquireUninterruptibly();
            for (TX tx : relevant_transactions) {
                List<TR> trs = tx.getTrs().stream()
                        .filter(tr -> shard_id.equals(tr.getAddress().hashCode() % ServerApp.NUM_OF_SHARDS))
                        .collect(Collectors.toList());
                List<UTxO> relevant_utxos = trs.stream().map(tr -> new UTxO(tx.getTx_id(), tr.getAddress())).collect(Collectors.toList());
                assert (relevant_utxos.size() != 0);
                unspent_utxos.addAll(relevant_utxos);
                txList.add(tx);
            }
            atomic_transaction_Mutex.release();
            Response_status response = Response_status.newBuilder().setResponse("OK").build();
            responseObserver.onNext(response);
            responseObserver.onCompleted();

            send_publishTransactions(request);
            return;
        }
        Response_status response = Response_status.newBuilder().setResponse("OK").build();
        responseObserver.onNext(response);
        responseObserver.onCompleted();
        return;
    }


    /*
    * * * * * * * * * * *
    * Client functions  *
    * * * * * * * * * * *
    */

    public void send_publishTransactions(notsystemserver.grpc.Transaction_list request) {

        List<TX> transactions = request.getTransactionsList().stream().map(TX::new).collect(Collectors.toList());
        System.out.println("Entered send_publishTransactions " + shard_id.toString() + " " + server_id.toString()
                + "\nTX_id:" + transactions.get(0).getTx_id());
        Set<Integer> shards = new HashSet<Integer>();
        for (TX tx: transactions) {
            shards.addAll(tx.getTrs().stream().map(tr -> tr.getAddress().hashCode()%ServerApp.NUM_OF_SHARDS).collect(Collectors.toList()));
        }

        try {
            List<ManagedChannel> channels = new ArrayList<ManagedChannel>();
            List<Future<Response_status>> futures = new ArrayList<Future<Response_status>>();

            for (Integer shard:shards) {
                List<String> servers = myZK.getChildren("/Shards/"+shard);
                for (String server:servers) {
                    if (server.equals(server_id.toString()) && shard.equals(shard_id))
                        continue;
                    System.out.println("in send_initServer: sending init to shard: " + shard);
                    Integer server_port = getServerPort(shard, Integer.parseInt(server));
                    String hostname = getHostName(shard, Integer.parseInt(server));
                    System.out.println("sending to " + hostname + ":" + server_port.toString());


                    /* send to all servers */
                    ManagedChannel channel = ManagedChannelBuilder.forTarget(hostname + ":" + server_port.toString())
                            .usePlaintext()
                            .build();
                    SystemServerGrpc.SystemServerFutureStub stub = SystemServerGrpc.newFutureStub(channel);
                    List<TX_m> transaction_messages = transactions.stream().map(TX::to_grpc).collect(Collectors.toList());
                    Future<Response_status> future = stub.publishTransaction(request);
                    futures.add(future);
                    channels.add(channel);

                }
            }

            /* wait any response */
            int number_of_responses;
            do {
                number_of_responses = 0;
                for (Future<Response_status> future: futures) {
                    if (future.isDone())
                        number_of_responses++;
                }
            } while (number_of_responses != 0);

            for (ManagedChannel channel: channels) {
                channel.shutdown();
            }

        } catch (InterruptedException | KeeperException | NullPointerException e) {
            e.printStackTrace();
            return;
        }
        return;

    }

    public boolean send_initServer() {
        System.out.println("Entered send_initServer " + shard_id.toString() + " " + server_id.toString());
        try {
            List<String> shards = myZK.getChildren("/Shards");
//            shards.remove(shard_id.toString());
            for (String shard:shards) {
                Integer server  = getLeader(Integer.parseInt(shard));
                Integer server_port = getServerPort(Integer.parseInt(shard), getLeader(Integer.parseInt(shard)));
                System.out.println("in send_initServer: sending init to shard: " + shard + ", server: " + server.toString() +  ", port: " + server_port.toString());
                ManagedChannel channel;
                try {
                    String hostname = getHostName(Integer.parseInt(shard), server);
                    System.out.println("sending to " + hostname + ":" + server_port.toString());

                    channel = ManagedChannelBuilder.forTarget(hostname + ":" + server_port.toString())
                            .usePlaintext()
                            .build();
//                    channel = ManagedChannelBuilder.forAddress(hostname, server_port)
//                            .usePlaintext()
//                            .build();
                } catch (Exception e) {
                    System.out.println("[send_initServer] got wierd exception when creating channel " + e.getMessage());
//                    e.printStackTrace();
                    return false;
                }
                SystemServerGrpc.SystemServerBlockingStub stub;
                try {
                    stub = SystemServerGrpc.newBlockingStub(channel);
                } catch (Exception e) {
                    System.out.println("[send_initServer] got wierd exception when creating stub " + e.getMessage());
                    System.out.println(e.getCause().getMessage());
                    e.getCause().printStackTrace();
//                    e.printStackTrace();
                    return false;
                }

                Init_Server_Args args = Init_Server_Args.newBuilder().build();

                Response_status res_grpc;
                try {
                    res_grpc = stub.initServer(args);
                } catch (Exception e) {
                    System.out.println("[send_initServer] got wierd exception when creating stub " + e.getMessage());
//                    e.printStackTrace();
                    return false;
                }

                try {
                    if (!(res_grpc.getResponse().equals("OK"))) {
                        channel.shutdown();
                        return false;
                    }
                    channel.shutdown();
                }catch (Exception e) {
                    System.out.println("[send_initServer] got wierd exception when shutting down channel " + e.getMessage());
                    e.printStackTrace();
                    return false;

                }

            }

        } catch (InterruptedException | KeeperException | NullPointerException e) {
            System.out.println("[send_initServer] got exception " + e.getMessage() + "\n" + Arrays.toString(e.getStackTrace()));

            e.printStackTrace();
            return false;
        } catch (Exception e) {
            System.out.println("[send_initServer] got wierd exception " + e.getMessage() + "\n" + Arrays.toString(e.getStackTrace()));

            e.printStackTrace();
            return false;

        }


        return true;
    }

}
