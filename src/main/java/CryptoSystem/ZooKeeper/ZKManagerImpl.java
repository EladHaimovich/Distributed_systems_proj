package CryptoSystem.ZooKeeper;

import CryptoSystem.types.uint128;
import org.apache.zookeeper.*;
import org.apache.zookeeper.data.Stat;

import java.io.Closeable;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.Semaphore;


public class ZKManagerImpl implements ZKManager {
    private static ZooKeeper zkeeper;
    private static ZKConnection zkConnection;
    private static final String zooKeeperHostName = "zoo1.zk.local";

    private static final String TIMESTAMP_BASE_PATH = "/Timestamp";
    private static final String UINT128_BASE_PATH = "/uint128";
    private static final String UINT128_HIGH_PATH = "/uint128/high";
    private static final String UINT128_LOW_PATH = "/uint128/low";
    private static final String UINT128_LOCK_PATH = "/uint128/lock";

    public ZKManagerImpl(Integer port) throws IOException, InterruptedException, KeeperException {
        initialize(port);
    }


    private void initialize(Integer port) throws IOException, InterruptedException {
        System.out.println("Entered ZKManagerImpl initialize with port: " + port);
        zkConnection = new ZKConnection();
        zkeeper = zkConnection.connect(zooKeeperHostName+ ":" + port.toString());
    }

    public void closeConnection() throws InterruptedException {
        zkConnection.close();
    }

    public void create(String path, byte[] data) throws KeeperException, InterruptedException {

        zkeeper.create(
                path,
                data,
                ZooDefs.Ids.OPEN_ACL_UNSAFE,
                CreateMode.PERSISTENT);
    }

    public void createEphemeral(String path, byte[] data) throws KeeperException, InterruptedException {
        zkeeper.create(
                path,
                data,
                ZooDefs.Ids.OPEN_ACL_UNSAFE,
                CreateMode.EPHEMERAL);
    }

    public String createSequentialEphemeral(String path, byte[] data) throws KeeperException, InterruptedException {
        return zkeeper.create(
                    path,
                    data,
                    ZooDefs.Ids.OPEN_ACL_UNSAFE,
                    CreateMode.EPHEMERAL_SEQUENTIAL);
    }

    public String getZNodeData(String path, boolean watchFlag) throws KeeperException, InterruptedException, UnsupportedEncodingException {

        byte[] b = null;
        b = zkeeper.getData(path, false, null);
        return new String(b);
    }

    public void update(String path, byte[] data) throws KeeperException,
            InterruptedException {
        int version = zkeeper.exists(path, true).getVersion();
        zkeeper.setData(path, data, version);
    }

    public List<String> getChildren(String path) throws KeeperException, InterruptedException {
        if (zkeeper.exists(path, false) == null) {
            System.out.println("[ZKManagerImpl] PATH NOT EXIST: " + path);
            return new LinkedList<>();
        }
        return zkeeper.getChildren(path, false);
    }

    public String getData(String path) throws KeeperException, InterruptedException {
        return zkeeper.getData(path, false, null).toString();
    }

    public Stat getStat(String path) throws KeeperException, InterruptedException {
        Stat _stat = new Stat();
        zkeeper.getData(path, false, _stat);
        return _stat;
    }

    public void create_base_znodes() throws InterruptedException, KeeperException {
        this.create(TIMESTAMP_BASE_PATH, Long.toString(0).getBytes());
        this.create(UINT128_BASE_PATH, Long.toString(0).getBytes());
        this.create(UINT128_HIGH_PATH, Long.toString(0).getBytes());
        this.create(UINT128_LOW_PATH, Long.toString(0).getBytes());
        this.create(UINT128_LOCK_PATH, Long.toString(0).getBytes());
    }

    private String acquire_lock(String path) throws InterruptedException, KeeperException {

        String lock_string = createSequentialEphemeral(path+"/lock-", Long.toString(0).getBytes());
        String znode_name = lock_string.substring(lock_string.lastIndexOf('/') + 1);

        System.out.println("[acquire_lock]: lock_string: " + lock_string);
        System.out.println("[acquire_lock]: znode_name: " + znode_name);

        do {
            Semaphore watcher_semaphore = new Semaphore(0);
            List<String> children = zkeeper.getChildren(path, false);
            children.sort(Comparator.naturalOrder());

            children.indexOf(znode_name);

            System.out.println("[acquire_lock]: children.get(0): " + children.get(0));
            if (lock_string.endsWith(children.get(0))) {
                System.out.println("[acquire_lock]: lock acquired: " + lock_string);
                return lock_string;
            }
            System.out.println("[acquire_lock]: watching znode:" + path + "/" + children.get(0));
            if (zkeeper.exists(path + "/" + children.get(0), new Watcher() {
                @Override
                public void process(WatchedEvent event) {
                    watcher_semaphore.release();
                }
            }) == null) {
                continue;
            }
            watcher_semaphore.acquire();
        } while (true);

    }

    private void release_lock(String node) throws InterruptedException, KeeperException {
        System.out.println("[release_lock]: node: " + node);
        zkeeper.delete(node, -1);
    }


    public long generate_timestamp() throws InterruptedException, KeeperException {
        Long timestamp = 0L;
        String lock_node = acquire_lock(TIMESTAMP_BASE_PATH);
        System.out.println("[generate_timestamp]: lock acquired " + lock_node);
        Stat stat = zkeeper.exists(TIMESTAMP_BASE_PATH, false);
        int version = stat.getVersion();
        timestamp = stat.getMzxid();
        System.out.println("[generate_uint128]: generated high " + timestamp.toString());
        zkeeper.setData(TIMESTAMP_BASE_PATH, Long.toString(timestamp).getBytes(), version);
        release_lock(lock_node);
        System.out.println("[generate_timestamp]: lock released " + lock_node);
        return timestamp;
    }

    public uint128 generate_uint128() throws InterruptedException, KeeperException {
        Long high = 0L;
        Long low = 0L;
        String lock_node = acquire_lock(UINT128_LOCK_PATH);
        System.out.println("[generate_uint128]: lock acquired " + lock_node);
        Stat stat = zkeeper.exists(UINT128_HIGH_PATH, false);
        int version = stat.getVersion();
        high = stat.getMzxid();
        System.out.println("[generate_uint128]: generated high " + high.toString());
        zkeeper.setData(UINT128_HIGH_PATH, Long.toString(high).getBytes(), version);
        stat = zkeeper.exists(UINT128_LOW_PATH, false);
        version = stat.getVersion();
        low = stat.getMzxid();
        System.out.println("[generate_uint128]: generated low " + low.toString());
        zkeeper.setData(UINT128_LOW_PATH, Long.toString(low).getBytes(), version);
        release_lock(lock_node);
        System.out.println("[generate_uint128]: lock released " + lock_node);
        return new uint128(high, low);
    }

}