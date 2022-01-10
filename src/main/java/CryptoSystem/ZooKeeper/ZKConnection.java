package CryptoSystem.ZooKeeper;

import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.Watcher;
import org.apache.zookeeper.ZooKeeper;

import java.io.IOException;
import java.util.concurrent.CountDownLatch;

public class ZKConnection {
    private ZooKeeper zoo;
    CountDownLatch connectionLatch = new CountDownLatch(1);

    public ZooKeeper connect(String host) throws IOException,  InterruptedException {
        System.out.println("\n***\nEntered ZKConnection.connect. host:\n***" + host);
        zoo = new ZooKeeper(host, 2000, new Watcher() {
            public void process(WatchedEvent we) {
                if (we.getState() == Event.KeeperState.SyncConnected) {
                    System.out.println("\n\n\nthis is the middle of ZKConnection.connect\n\n");
                    connectionLatch.countDown();
                }
            }
        });

        connectionLatch.await();
        System.out.println("finished ZKConnection.connect. host:" + host);
        return zoo;
    }

    public void close() throws InterruptedException {
        zoo.close();
    }
}
