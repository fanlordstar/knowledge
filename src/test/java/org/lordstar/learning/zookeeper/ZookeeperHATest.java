package org.lordstar.learning.zookeeper;

import org.apache.log4j.Logger;
import org.apache.zookeeper.*;
import org.junit.Test;
import org.lordstar.learning.util.ConfigUtil;

import java.io.IOException;
import java.util.concurrent.CountDownLatch;

public class ZookeeperHATest {
    private static Logger logger = Logger.getLogger(ZookeeperHATest.class);

    private ZooKeeper zk;

    private CountDownLatch connectLatch = new CountDownLatch(1);

    private String masterNode = "/master";

    private boolean isMaster = false;

    private void initZookeeperClient() throws IOException, InterruptedException {
        String host = ConfigUtil.getInstance().getConfigValue("zookeeperList");

        zk = new ZooKeeper(host, 15000, new ConnectWatch());
        connectLatch.await();

        // 开始尝试创建父节点，以此来竞选主节点，这里可以先判断是否存在主节点，然后再创建
        // 即使在刚开始所有的客户端都判断没有主节点，但是在zookeeper客户端上创建的时候总是只有一个节点才可以创建OK
        electionMaster();
    }

    public class ConnectWatch implements Watcher {

        @Override
        public void process(WatchedEvent event) {
            if (event.getState() == Event.KeeperState.SyncConnected) {
                logger.info("客户端连接上了");
                connectLatch.countDown();
            }
        }
    }

    public class zkMasterWatcher implements Watcher {

        @Override
        public void process(WatchedEvent event) {
            if (event.getType() == Event.EventType.NodeDeleted) {
                // 尝试创建/master节点，竞选master
                electionMaster();
            }
        }
    }

    private void electionMaster() {
        try {
            zk.create(masterNode, new byte[0], ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.EPHEMERAL);
        } catch (KeeperException e) {
            logger.info("master节点已经存在，该客户端不是主节点，而是作为备用节点");
            try {
                zk.exists(masterNode, new zkMasterWatcher());
            } catch (KeeperException e1) {
                e1.printStackTrace();
            } catch (InterruptedException e1) {
                e1.printStackTrace();
            }
            return;
        } catch (InterruptedException e) {
            logger.info(e);
            return;
        }
        logger.info("该节点是主节点");
    }

    @Test
    public void zookeeperHaTest() throws IOException, InterruptedException {
        CountDownLatch count = new CountDownLatch(1);
        initZookeeperClient();
        count.await();
    }
}
