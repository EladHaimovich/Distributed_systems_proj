package CryptoSystem.ZooKeeper;

import org.apache.zookeeper.*;
import org.apache.zookeeper.data.*;

import java.io.UnsupportedEncodingException;
import java.util.List;

public interface ZKManager {
    public void create(String path, byte[] data) throws KeeperException, InterruptedException;
    public void createEphemeral(String path, byte[] data) throws KeeperException, InterruptedException;
    public String getZNodeData(String path, boolean watchFlag) throws KeeperException, InterruptedException, UnsupportedEncodingException;
    public void update(String path, byte[] data) throws KeeperException, InterruptedException;

    public void closeConnection() throws InterruptedException;
    public List<String> getChildren(String path) throws KeeperException, InterruptedException;
    public String getData(String path) throws KeeperException, InterruptedException;
    public Stat getStat(String path) throws KeeperException, InterruptedException;
}