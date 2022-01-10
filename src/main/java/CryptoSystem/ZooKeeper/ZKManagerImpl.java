package CryptoSystem.ZooKeeper;

import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.KeeperException;
import org.apache.zookeeper.ZooDefs;
import org.apache.zookeeper.ZooKeeper;
import org.apache.zookeeper.data.Stat;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.LinkedList;
import java.util.List;



public class ZKManagerImpl implements ZKManager {
    private static ZooKeeper zkeeper;
    private static ZKConnection zkConnection;
    private static final String zooKeeperHostName = "zoo1.zk.local";



    public ZKManagerImpl(Integer port) throws IOException, InterruptedException {
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
}