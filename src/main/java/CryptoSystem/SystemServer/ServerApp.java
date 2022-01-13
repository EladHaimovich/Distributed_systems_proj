package CryptoSystem.SystemServer;



import CryptoSystem.SystemServer.Spring.SystemServerApplication;
import CryptoSystem.ZooKeeper.ZKManager;
import CryptoSystem.ZooKeeper.ZKManagerImpl;
import CryptoSystem.gRPCwrapper.SystemServerImpl;
import io.grpc.Server;
import io.grpc.ServerBuilder;
import org.apache.zookeeper.KeeperException;

import java.io.File;
import java.io.IOException;
import java.math.BigInteger;
import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.List;
import java.util.Scanner;
import java.util.concurrent.TimeUnit;

public class ServerApp {
    public static final Integer NUM_OF_SHARDS = 2;



    private static Integer zkPort = 2181;
    private static final Integer gRPCPort = 30000;
    private static final Integer restPort = 40000;
    private static Integer serverID;
    private static Integer shard;
    private static SystemServerImpl grpc_service;
    private static ZKManager myZK;

    public static void main(String[] args) throws InterruptedException {

        Scanner scanner = null;
        try {
            scanner = new Scanner(new File("./parameters"));
            shard = scanner.nextInt();
            serverID = scanner.nextInt();
            zkPort = scanner.nextInt();

        } catch (Exception e) {//Catch exception if any
            System.err.println("Error: " + e.getMessage());
        } finally {
            scanner.close();
        }

        assert (Integer.compareUnsigned(NUM_OF_SHARDS, shard) > 0);

        System.out.println("started server");
        System.out.println("shardID = " + shard.toString());
        System.out.println("serverID = " + serverID.toString());
        System.out.println("zkPort = " + zkPort.toString());

        HashMap<String, Integer> server_args = new HashMap<String, Integer>();
        server_args.put("serverNo", serverID);
        server_args.put("shard", shard);
        server_args.put("zkPort", zkPort);
        server_args.put("gRPCPort", gRPCPort);
        server_args.put("restPort", restPort);

        TimeUnit.SECONDS.sleep(10);
        initServer(server_args);
    }

    static void initServer(HashMap<String, Integer> server_args) {
        System.out.println("initializing server");
        initZooKeeper(server_args);
        initRestSpring(server_args);
        initGRpc(server_args);
    }

    static void initZooKeeper(HashMap<String, Integer> server_args) {
        System.out.println("Init CryptoSystem.SystemServer.ZooKeeper service");

        // CryptoSystem.ZooKeeper Client
        String shard_string = server_args.get("shard").toString();
        String serverNo_String = server_args.get("serverNo").toString();
        try {
            System.err.println("*** running new ZKManagerImpl ***");
            myZK = new ZKManagerImpl(zkPort);
            try {
                System.err.println("*** running myZK.create ***");
                myZK.create("/Shards", shard_string.getBytes());
            } catch (KeeperException e)  {
                System.err.println("*** myZK.create failed ***\n" + e.getMessage());
            }
            try {
                System.err.println("*** running myZK.create ***");
                myZK.create("/Shards/" + shard_string, shard_string.getBytes());
            } catch (KeeperException e)  {
                System.err.println("*** myZK.create failed ***\n" + e.getMessage());
            }
            try {
                System.err.println("*** running myZK.createEphemeral ***");
                myZK.createEphemeral("/Shards/" + shard_string + "/" + serverNo_String, gRPCPort.toString().getBytes() );
            } catch (KeeperException e)  {
                System.err.println("*** myZK.createEphemeral failed ***\n" + e.getMessage());
                System.err.println("ERROR ERROR ERROR!");
                assert false;
            }
        } catch (IOException e) {
            System.err.println("*** IOException during myZK init ***\n" + e.getMessage());
            e.printStackTrace();
        } catch (InterruptedException e) {
            System.err.println("*** IOException during myZK init ***\n" + e.getMessage());
            e.printStackTrace();
        }
    }

    static void initGRpc(HashMap<String, Integer> server_args) {
        System.out.println("Init CryptoSystem.SystemServer.gRPC service");
        int port = server_args.get("gRPCPort");
        grpc_service = new SystemServerImpl();
        grpc_service.setServer_id(server_args.get("serverNo"));
        grpc_service.setShard_id(server_args.get("shard"));
        grpc_service.setMyZK(myZK);
        grpc_service.setZkPort(server_args.get("zkPort"));
        grpc_service.setServerGrpcPort(server_args.get("gRPCPort"));
        Server server = ServerBuilder.forPort(port)
                .addService(new SystemServerImpl()).build();

        System.out.println("Starting server...");
        try {
            server.start();
        } catch (IOException e) {
            e.printStackTrace();
        }
        System.out.println("Server started!");
        try {
            server.awaitTermination();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    static void initRestSpring(HashMap<String, Integer> server_args) {
        System.out.println("Init REST CryptoSystem.SystemServer.Spring service");

        SystemServerApplication springServer = new SystemServerApplication();
        String[] args = new String[2];
        args[0] = server_args.get("restPort").toString();
        args[1] = server_args.get("gRPCPort").toString();
        SystemServerApplication.main_spring(args);
    }

    public static SystemServerImpl getGrpc_service() {
        return grpc_service;
    }
}