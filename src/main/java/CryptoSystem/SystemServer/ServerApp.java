package CryptoSystem.SystemServer;



import CryptoSystem.SystemServer.Spring.SystemServerApplication;
import CryptoSystem.ZooKeeper.ZKManager;
import CryptoSystem.ZooKeeper.ZKManagerImpl;
import CryptoSystem.gRPCwrapper.SystemServerImpl;
import io.grpc.BindableService;
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
    private static Integer gRPCPort;
    private static  Integer restPort;
    private static Integer serverID;
    private static Integer shard;
    private static SystemServerImpl grpc_service;
    private static ZKManager myZK;

    public static void main(String[] args) throws InterruptedException {

        if (args.length > 0) {
            for (int i = 0; i < args.length; i++) {
                System.out.println("arg "+ Integer.toString(i) + ": " +args[i]);
            }
            shard = Integer.parseInt(args[0]);
            serverID = Integer.parseInt(args[1]);
            zkPort = Integer.parseInt(args[2]);
        } else {
            try (Scanner scanner = new Scanner(new File("./parameters"))) {
                shard = scanner.nextInt();
                serverID = scanner.nextInt();
                zkPort = scanner.nextInt();
            } catch (Exception e) {//Catch exception if any
                System.err.println("Error: " + e.getMessage());
            }
        }

        gRPCPort = 30000 + 100 * shard + serverID;
        restPort = 40000 + 100 * shard + serverID;


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

            try{
                System.err.println("*** running myZK.create_base_znodes ***");
                myZK.create_base_znodes();
            } catch (KeeperException e)  {
                System.err.println("*** myZK.create_base_znodes failed ***\n" + e.getMessage());
                System.err.println("ERROR ERROR ERROR!");
                assert false;
            }

        } catch (IOException | InterruptedException | KeeperException e) {
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
                .addService((BindableService) new SystemServerImpl()).build();

        System.out.println("Starting server...");
        try {
            server.start();
        } catch (IOException e) {
            e.printStackTrace();
        }
        System.out.println("Server started! listening on port " + port);
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