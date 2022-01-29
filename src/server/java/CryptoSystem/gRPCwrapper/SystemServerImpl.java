package CryptoSystem.gRPCwrapper;

import CryptoSystem.SystemServer.ServerApp;
import CryptoSystem.SystemServer.Spring.RESTresponse;
import CryptoSystem.ZooKeeper.ZKManager;
import CryptoSystem.ZooKeeper.ZKManagerImpl;
import CryptoSystem.types.TR;
import CryptoSystem.types.TX;
import CryptoSystem.types.TX.CompareByTimestamp;
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
import com.google.gson.Gson;


public class SystemServerImpl extends SystemServerGrpc.SystemServerImplBase {

    static private Integer shard_id;
    static private Integer server_id;
    static private Integer serverGrpcPort;
    static private ZKManager myZK;

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

    public static void setMyZK(ZKManager myZK) {
        SystemServerImpl.myZK = myZK;
    }

    public static Integer getGrpcPort() {
        return serverGrpcPort;
    }

    private static Integer getLeader(Integer shard) {
        try {
            List<String> servers = myZK.getChildren("/Shards/" + shard);
            servers.sort(Comparator.naturalOrder());
            return Integer.parseInt(servers.get(0));

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

    private static Integer get_shard_of_address(uint128 address) {
        return address.hashCode()%ServerApp.NUM_OF_SHARDS;
    }

    /*
     * * * * * * * * * * *
     * Server functions  *
     * * * * * * * * * * *
     */

    @Override
    public void initServer(Init_Server_Args request,
                           StreamObserver<Response_status> responseObserver) {
//        System.out.println("Entered init server " + shard_id.toString() + " " + server_id.toString());

        if (getLeader(shard_id).equals(server_id)) {
            try {
                List<String> servers = myZK.getChildren("/Shards/" + shard_id.toString());
                servers.remove(server_id.toString());
                for (String server:servers) {
                    Integer server_port = Integer.parseInt(myZK.getZNodeData("/Shards/" + shard_id.toString() + "/" + server,false));
                    String hostname = getHostName(shard_id, Integer.parseInt(server));
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
                Response_status response = Response_status.newBuilder().setSuccess(false).setResponse("Catched Exception").build();
                responseObserver.onNext(response);
                responseObserver.onCompleted();
            }
        }
        spent_utxos = new ArrayList<UTxO>();
        unspent_utxos = new ArrayList<UTxO>();
        txList = new ArrayList<TX>();
        uint128 zero = new uint128(0,0);
        if (shard_id.equals(get_shard_of_address(zero))) {
            unspent_utxos.add(UTxO.genUTxO());
            txList.add(TX.gen_TX());

//            System.out.println("[init server] database initialized." +
//                                "\n spent utxos = " + spent_utxos.toString() +
//                                "\n unspent utxos = " + unspent_utxos.toString() +
//                                "\n transaction list = " + txList.toString());
        }



        Response_status response = Response_status.newBuilder().setSuccess(true).setResponse("OK").build();
        responseObserver.onNext(response);
        responseObserver.onCompleted();
    }

    @Override
    public void submitTransaction(notsystemserver.grpc.TX_m request,
                                  io.grpc.stub.StreamObserver<notsystemserver.grpc.Response_status> responseObserver) {

        atomic_transaction_Mutex.acquireUninterruptibly();
        boolean transaction_failed = false;

        /* generate timestamp */
        long timestamp;
        try {
            timestamp = myZK.generate_timestamp();
        } catch (Exception e) {
            e.printStackTrace();
            atomic_transaction_Mutex.release();
            Response_status response = Response_status.newBuilder().setSuccess(false).setResponse("Error: zookeeper failed").build();
            responseObserver.onNext(response);
            responseObserver.onCompleted();
            return;
        }

        /* validate transaction */
        TX transaction = new TX(request);
        transaction.assign_timestamp(timestamp);
        uint128 sender = transaction.getUtxos().get(0).getAddress();
        assert (shard_id.equals(get_shard_of_address(sender)));
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
        if (!transaction_failed && Long.compareUnsigned(utxo_sum, trs_sum) != 0) {
            response_string = "value of UTxOs (" + Long.toUnsignedString(utxo_sum) + ") doesn't match value of TRs(" + Long.toUnsignedString(trs_sum) +")";
            transaction_failed = true;
        }
        if(!transaction_failed) { /* if transaction is valid */
            /* update database */
            spent_utxos.addAll(transaction.getUtxos());
            unspent_utxos.removeAll(transaction.getUtxos());
            List<UTxO> shards_utxos = transaction.getTrs().stream()
                        .filter(tr -> shard_id.equals(get_shard_of_address(tr.getAddress())))
                        .map(tr -> new UTxO(transaction.getTx_id(), tr.getAddress())).collect(Collectors.toList());
            unspent_utxos.addAll(shards_utxos);
            txList.add(transaction);

            /* broadcast to all the servers */
            TX_m updated_request = TX_m.newBuilder().mergeFrom(transaction.to_grpc()).build();
            send_publishTransaction(updated_request);
            Gson gson = new Gson();
            String json = gson.toJson(transaction.getTx_id());
            response_string = "transaction ID = " + json;
        }
        atomic_transaction_Mutex.release();
        Response_status response = Response_status.newBuilder().setSuccess(!transaction_failed).setResponse(response_string).build();
        responseObserver.onNext(response);
        responseObserver.onCompleted();
        return;
    }

    @Override
    public void publishTransaction(notsystemserver.grpc.TX_m request,
                                   io.grpc.stub.StreamObserver<notsystemserver.grpc.Response_status> responseObserver) {
        TX transaction = new TX(request);
        /* if the transaction is new, add it to database and broadcast to other nodes */
        if (!txList.contains(transaction)) {
            if (get_shard_of_address(transaction.getUtxos().get(0).getAddress()).equals(shard_id) ||
                    transaction.getTrs().stream().anyMatch(tr -> get_shard_of_address(tr.getAddress()).equals(shard_id))) {
                atomic_transaction_Mutex.acquireUninterruptibly();
                List<TR> trs = transaction.getTrs().stream()
                        .filter(tr -> shard_id.equals(get_shard_of_address(tr.getAddress())))
                        .collect(Collectors.toList());
                List<UTxO> relevant_utxos = trs.stream().map(tr -> new UTxO(transaction.getTx_id(), tr.getAddress())).collect(Collectors.toList());
                assert (relevant_utxos.size() != 0);
                unspent_utxos.addAll(relevant_utxos);
                txList.add(transaction);
                atomic_transaction_Mutex.release();
                Response_status response = Response_status.newBuilder().setSuccess(true).setResponse("OK").build();
                responseObserver.onNext(response);
                responseObserver.onCompleted();
                send_publishTransaction(request);
                return;
            }
        }
        Response_status response = Response_status.newBuilder().setSuccess(true).setResponse("OK").build();
        responseObserver.onNext(response);
        responseObserver.onCompleted();
        return;
    }

    public void getTransactions(notsystemserver.grpc.uint128_m request,
                                io.grpc.stub.StreamObserver<notsystemserver.grpc.Transaction_list> responseObserver) {
        if (request.isInitialized()) {
            System.out.println("[getTransactions] got initialized request");
            uint128 address = new uint128(request);
            assert (get_shard_of_address(address).equals(shard_id));

            atomic_transaction_Mutex.acquireUninterruptibly();
            Set<TX_m> transactions = txList.stream()
                    .filter(tx -> (!tx.equals(TX.gen_TX()))) // filter gen transaction since it doesnt have any utxos
                    .filter(tx -> tx.getUtxos()
                            .get(0)
                            .getAddress()
                            .equals(address))
                    .map(TX::to_grpc)
                    .collect(Collectors.toSet());

            transactions.addAll(txList.stream()
                    .filter(tx -> tx.getTrs()
                            .stream()
                            .anyMatch(tr -> tr.getAddress().equals(address)))
                    .map(TX::to_grpc)
                    .collect(Collectors.toList()));
            atomic_transaction_Mutex.release();

            Transaction_list result = Transaction_list.newBuilder().addAllTransactions(transactions).build();
            responseObserver.onNext(result);
            responseObserver.onCompleted();
            return;
        } else {
            System.out.println("[getTransactions] got uninitialized request");
            // TODO: add implementation for get all history
            atomic_transaction_Mutex.acquireUninterruptibly();
            Set<TX_m> transactions = txList.stream()
                    .map(TX::to_grpc)
                    .collect(Collectors.toSet());

            Transaction_list result = Transaction_list.newBuilder().addAllTransactions(transactions).build();
            responseObserver.onNext(result);
            responseObserver.onCompleted();
            try {
                String lock = myZK.acquire_lock(ZKManagerImpl.HISTORY_LOCK_PATH); // as long as the client side keep this lock, the server wont continue to process requests
                myZK.release_lock(lock);
            } catch (InterruptedException | KeeperException e) {
                e.printStackTrace();
            }
            atomic_transaction_Mutex.release();

            return;

        }
    }

    public void getUtxos(notsystemserver.grpc.uint128_m request,
                         io.grpc.stub.StreamObserver<notsystemserver.grpc.UTxO_list> responseObserver) {
        assert request.isInitialized();
        uint128 address = new uint128(request);
        assert (get_shard_of_address(address).equals(shard_id));

        atomic_transaction_Mutex.acquireUninterruptibly();
        Set<UTxO_m> utxos = unspent_utxos.stream()
                .filter(utxo -> utxo.getAddress().equals(address))
                .map(UTxO::to_grpc)
                .collect(Collectors.toSet());

        atomic_transaction_Mutex.release();

        UTxO_list result = UTxO_list.newBuilder().addAllUxtos(utxos).build();
        responseObserver.onNext(result);
        responseObserver.onCompleted();
        return;
    }

    public void transferCoins(notsystemserver.grpc.Send_Coins_req request,
                              io.grpc.stub.StreamObserver<notsystemserver.grpc.Response_status> responseObserver) {
        uint128 sender = new uint128(request.getSender());
        uint128 receiver = new uint128(request.getReceiver());
        Long amount = request.getCoins();
        assert get_shard_of_address(sender).equals(shard_id);

        boolean transfer_failed = false;
        String return_status = "OK";

        atomic_transaction_Mutex.acquireUninterruptibly();
        List<UTxO> available_utxos = unspent_utxos.stream().filter(uTxO -> uTxO.getAddress().equals(sender)).collect(Collectors.toList());
        List<UTxO> used_utxos = new ArrayList<UTxO>();
        Long sum = 0L;
        for (UTxO utxo:available_utxos) {
            used_utxos.add(utxo);
            List<TX> matched_transactions = txList.stream().filter(tx -> tx.getTx_id().equals(utxo.getTx_id())).collect(Collectors.toList());
            assert matched_transactions.size() == 1; // shouldn't have two copies of a transaction
            TX transaction = matched_transactions.get(0);
            sum += transaction.getTrs().stream().filter(tr -> tr.getAddress().equals(sender)).map(TR::getAmount).reduce(0L, Long::sum);
            if (!(Long.compareUnsigned(sum, amount) < 0))
                break;
        }
        if (Long.compareUnsigned(sum, amount) < 0) {
            transfer_failed = true;
            return_status = "Error: not enough coins";
        }
        if (!transfer_failed) {
            List<TR> trs = new ArrayList<>();
            trs.add(new TR(receiver, amount));
            if (Long.compareUnsigned(sum, amount) > 0)
                trs.add(new TR(sender, sum - amount));

            try {
                TX transaction = new TX(myZK.generate_tx_id(shard_id),myZK.generate_timestamp(),used_utxos, trs);
                /* update database */
                spent_utxos.addAll(transaction.getUtxos());
                unspent_utxos.removeAll(transaction.getUtxos());

                List<UTxO> shards_utxos = transaction.getTrs().stream()
                        .filter(tr -> shard_id.equals(get_shard_of_address(tr.getAddress())))
                        .map(tr -> new UTxO(transaction.getTx_id(), tr.getAddress())).collect(Collectors.toList());
                unspent_utxos.addAll(shards_utxos);
                txList.add(transaction);

                /* broadcast to all the servers */
                TX_m updated_request = TX_m.newBuilder().mergeFrom(transaction.to_grpc()).build();
                send_publishTransaction(updated_request);
                Gson gson = new Gson();
                String json = gson.toJson(transaction.getTx_id());

                return_status = "transaction ID = " + json;
            } catch (KeeperException | InterruptedException e) {
                transfer_failed = true;
                return_status = "Error: zookeeper error";
                e.printStackTrace();
            }
        }
        atomic_transaction_Mutex.release();
        Response_status response = Response_status.newBuilder().setSuccess(!transfer_failed).setResponse(return_status).build();
        responseObserver.onNext(response);
        responseObserver.onCompleted();
        return;
    }

    /*
    * * * * * * * * * * *
    * Client functions  *
    * * * * * * * * * * *
    */



    public static Response_status send_submitTransaction(TX transaction) {
        Response_status response;
        try {
            /* send transaction to leader of relevant shard */
            Integer shard = get_shard_of_address(transaction.getUtxos().get(0).getAddress());
            Integer server  = getLeader(shard);
            Integer server_port = getServerPort(shard, server);
            String hostname = getHostName(shard, server);
//            System.out.println("in send_submitTransaction: sending TX " + transaction + " to hostname " + hostname);
            ManagedChannel channel;

//            System.out.println("sending to " + hostname + ":" + server_port.toString());

            channel = ManagedChannelBuilder.forTarget(hostname + ":" + server_port.toString())
                    .usePlaintext()
                    .build();
            SystemServerGrpc.SystemServerBlockingStub stub;
            stub = SystemServerGrpc.newBlockingStub(channel);

            /* generate TX_id */
            transaction.assign_TXid(myZK.generate_tx_id(shard));

            TX_m request = transaction.to_grpc();


            response = stub.submitTransaction(request);
            channel.shutdown();

        } catch (Exception e) {
            e.printStackTrace();
            response = Response_status.newBuilder().setSuccess(false).setResponse("Error: transaction failed").build();
        }
        return response;


    }

    public void send_publishTransaction(notsystemserver.grpc.TX_m request) {

        TX transaction = new TX(request);
        System.out.println("Entered send_publishTransactions " + shard_id.toString() + " " + server_id.toString()
                + "\nTX_id:" + transaction.getTx_id());
        Set<Integer> shards = transaction.getTrs().stream()
                                                    .map(tr -> get_shard_of_address(tr.getAddress()))
                                                    .collect(Collectors.toSet());

        try {
            List<ManagedChannel> channels = new ArrayList<ManagedChannel>();
            List<Future<Response_status>> futures = new ArrayList<Future<Response_status>>();

            for (Integer shard:shards) {
                List<Integer> servers = myZK.getChildren("/Shards/"+shard).stream().map(Integer::parseInt).collect(Collectors.toList());
                for (Integer server:servers) {
                    if (server.equals(server_id) && shard.equals(shard_id))
                        continue;
                    System.out.println("in send_publishTransaction: sending transaction to shard: " + shard);
                    Integer server_port = getServerPort(shard, server);
                    String hostname = getHostName(shard, server);
                    System.out.println("sending to " + hostname + ":" + server_port.toString());


                    /* send to all servers */
                    ManagedChannel channel = ManagedChannelBuilder.forTarget(hostname + ":" + server_port.toString())
                            .usePlaintext()
                            .build();
                    SystemServerGrpc.SystemServerFutureStub stub = SystemServerGrpc.newFutureStub(channel);
                    Future<Response_status> future = stub.publishTransaction(request);
                    futures.add(future);
                    channels.add(channel);

                }
            }
            /* wait all responses (including fails) */
            int number_of_responses;
            do {
                number_of_responses = 0;
                for (Future<Response_status> future: futures) {
                    if (future.isDone())
                        number_of_responses++;
                }
            } while (number_of_responses != channels.size());

            for (ManagedChannel channel: channels) {
                channel.shutdown();
            }

        } catch (InterruptedException | KeeperException | NullPointerException e) {
            e.printStackTrace();
            return;
        }
        return;
    }

    public static boolean send_initServer() {
//        System.out.println("Entered send_initServer " + shard_id.toString() + " " + server_id.toString());
        try {
            List<String> shards = myZK.getChildren("/Shards");
            for (String shard:shards) {
                Integer server  = getLeader(Integer.parseInt(shard));
                Integer server_port = getServerPort(Integer.parseInt(shard), getLeader(Integer.parseInt(shard)));
//                System.out.println("in send_initServer: sending init to shard: " + shard + ", server: " + server.toString() +  ", port: " + server_port.toString());
                ManagedChannel channel;
                try {
                    String hostname = getHostName(Integer.parseInt(shard), server);
//                    System.out.println("sending to " + hostname + ":" + server_port.toString());

                    channel = ManagedChannelBuilder.forTarget(hostname + ":" + server_port.toString())
                            .usePlaintext()
                            .build();
//                    channel = ManagedChannelBuilder.forAddress(hostname, server_port)
//                            .usePlaintext()
//                            .build();
                } catch (Exception e) {
//                    System.out.println("[send_initServer] got wierd exception when creating channel " + e.getMessage());
                    e.printStackTrace();
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

    public static List<TX> send_getTransactions(uint128 address, int amount) {
        List<TX> result = null;
        if (address != null) {
            try {
                /* send transaction to leader of relevant shard */
                Integer shard = get_shard_of_address(address);
                Integer server = getLeader(shard);
                Integer server_port = getServerPort(shard, server);
                String hostname = getHostName(shard, server);
                ManagedChannel channel;
                channel = ManagedChannelBuilder.forTarget(hostname + ":" + server_port.toString())
                        .usePlaintext()
                        .build();
                SystemServerGrpc.SystemServerBlockingStub stub;
                stub = SystemServerGrpc.newBlockingStub(channel);

                uint128_m request_address = address.to_grpc();
//                get_transactions_m request = get_transactions_m.newBuilder().setSpecificAddress(true).setAddress(request_address).build();

                Transaction_list transactions = stub.getTransactions(request_address);
                channel.shutdown();

                if (transactions.isInitialized()) {
                    result = transactions.getTransactionsList().stream().map(TX::new).collect(Collectors.toList());
                    result.sort(new CompareByTimestamp());
                    if (amount > 0 && amount < result.size())
                        result = result.subList(result.size() - amount, result.size());
                }
            } catch (Exception e) {
                e.printStackTrace();
                return null;
            }
        } else {
            //TODO: implement get transaction from all shards.

            List<Future<Transaction_list>> futures = new ArrayList<Future<Transaction_list>>();
            List<ManagedChannel> channels = new ArrayList<ManagedChannel>();
            String zookeeper_lock = null;
            Set<TX> transactions = new HashSet<TX>();
            try {
                /* create zookeeper lock to prevent shards from processing any request */
                zookeeper_lock = myZK.history_create_lock(); /* continues only if no other locks */
                if (zookeeper_lock == null)
                    return null;
                List<Integer> shards = myZK.getChildren("/Shards").stream().map(Integer::parseUnsignedInt).collect(Collectors.toList());
                for (Integer shard:shards) {
                    Integer leader = getLeader(shard);
                    Integer server_port = getServerPort(shard, leader);
                    String hostname = getHostName(shard, leader);
                    ManagedChannel channel;
                    channel = ManagedChannelBuilder.forTarget(hostname + ":" + server_port.toString())
                            .usePlaintext()
                            .build();
                    SystemServerGrpc.SystemServerFutureStub stub;
                    stub = SystemServerGrpc.newFutureStub(channel);
                    uint128_m request_address = uint128_m.newBuilder().build();
                    get_transactions_m request = get_transactions_m.newBuilder().setSpecificAddress(false).build();
                    Future<Transaction_list> transaction_list = stub.getTransactions(request_address);
                    futures.add(transaction_list);
                    channels.add(channel);
                }
                int number_of_responses;
                do {
                    number_of_responses = 0;
                    for (Future<Transaction_list> future: futures) {
                        if (future.isDone())
                            number_of_responses++;
                    }
                } while (number_of_responses != channels.size());

                for (Future<Transaction_list> future: futures) {
                    Transaction_list list = future.get();
                    if (list.isInitialized()) {
                        transactions.addAll(list.getTransactionsList().stream().map(TX::new).collect(Collectors.toList()));
                    }
                }

                for (ManagedChannel channel: channels) {
                    channel.shutdown();
                }
                result = new ArrayList<TX>(transactions);
                result.sort(new CompareByTimestamp());
                if (amount > 0 && amount < result.size())
                    result = result.subList(result.size() - amount, result.size());
            } catch (InterruptedException | KeeperException | ExecutionException e) {
                e.printStackTrace();
                if (zookeeper_lock != null) {
                    try {
                        myZK.release_lock(zookeeper_lock);
                    } catch (KeeperException | InterruptedException e2) {
                        e2.printStackTrace();
                    }
                }
                return null;
            }
            try {
                myZK.release_lock(zookeeper_lock);
            } catch (KeeperException | InterruptedException e2) {
                e2.printStackTrace();
            }
        }

        return result;
    }

    public static List<UTxO> send_getUtxos(uint128 address, int amount) {
        List<UTxO> result = null;
        assert address != null;
        try {
            /* send transaction to leader of relevant shard */
            Integer shard = get_shard_of_address(address);
            Integer server = getLeader(shard);
            Integer server_port = getServerPort(shard, server);
            String hostname = getHostName(shard, server);
//            System.out.println("in send_getTransactions: sending address " + address.toString() + " to hostname " + hostname);
            ManagedChannel channel;

//            System.out.println("sending to " + hostname + ":" + server_port.toString());

            channel = ManagedChannelBuilder.forTarget(hostname + ":" + server_port.toString())
                    .usePlaintext()
                    .build();
            SystemServerGrpc.SystemServerBlockingStub stub;
            stub = SystemServerGrpc.newBlockingStub(channel);

            uint128_m request_address = address.to_grpc();

            UTxO_list uTxOList = stub.getUtxos(request_address);
            channel.shutdown();

            if (uTxOList.isInitialized()) {
                result = uTxOList.getUxtosList().stream().map(UTxO::new).collect(Collectors.toList());
                result.sort(new UTxO.CompareByTXid());
                if (amount != 0 && amount < result.size())
                    result = result.subList(result.size() - amount, result.size());
            }
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
        return result;
    }

    public static Response_status send_transferCoins(uint128 sender, uint128 receiver, long coins) {
        Response_status response;
        try {
            /* send transaction to leader of relevant shard */
            Integer shard = get_shard_of_address(sender);
            Integer server  = getLeader(shard);
            Integer server_port = getServerPort(shard, server);
            String hostname = getHostName(shard, server);
            ManagedChannel channel;

            channel = ManagedChannelBuilder.forTarget(hostname + ":" + server_port.toString())
                    .usePlaintext()
                    .build();
            SystemServerGrpc.SystemServerBlockingStub stub;
            stub = SystemServerGrpc.newBlockingStub(channel);
            Send_Coins_req request = Send_Coins_req.newBuilder().setSender(sender.to_grpc()).setReceiver(receiver.to_grpc()).setCoins(coins).build();
            response = stub.transferCoins(request);
            channel.shutdown();
        } catch (Exception e) {
            e.printStackTrace();
            response = Response_status.newBuilder().setSuccess(false).setResponse("Error: coin transfer failed").build();
        }
        return response;
    }

}
