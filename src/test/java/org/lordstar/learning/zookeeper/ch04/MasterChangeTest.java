package org.lordstar.learning.zookeeper.ch04;

import org.apache.log4j.Logger;
import org.apache.zookeeper.*;
import org.apache.zookeeper.data.Stat;
import org.junit.Test;
import org.lordstar.learning.util.ConfigUtil;

import java.io.IOException;
import java.util.UUID;
import java.util.concurrent.CountDownLatch;

/**
 * 当前master down机之后，监听器重新选举master的过程
 */
public class MasterChangeTest {

    private static Logger logger = Logger.getLogger(MasterChangeTest.class);

     // 一个master进程有下面三种状态，running，elected是作为主节点的，noteleted是作为备用节点的
    enum MasterStates {RUNNING, ELECTED, NOTELECTED};

    private MasterStates states = MasterStates.NOTELECTED;

    private ZooKeeper zk;

    private CountDownLatch connectLatch = new CountDownLatch(1);

    private String masterNode = "/master";

    private String serverId = UUID.randomUUID().toString().replace("-", "");

    private void initZookeeper() throws IOException, InterruptedException {
        String hosts = ConfigUtil.getInstance().getConfigValue("zookeeperList");

        zk = new ZooKeeper(hosts, 15000, (WatchedEvent event) -> {
           if (event.getState() == Watcher.Event.KeeperState.SyncConnected) {
               connectLatch.countDown();
           }
        });

        connectLatch.await();
    }

    // 尝试创建/master节点
    private void runForMaster() {
        zk.create(masterNode, serverId.getBytes(), ZooDefs.Ids.OPEN_ACL_UNSAFE,
                CreateMode.EPHEMERAL,
                (int rc, String path, Object ctx, String name) -> {
                    switch (KeeperException.Code.get(rc)) {
                        case CONNECTIONLOSS:
                            // 连接丢失的时候，机器自己也不知道master是否创建，因此调用方法判断一下
                            checkMaster();
                            break;

                        case OK:
                            states = MasterStates.ELECTED;
                            // 开始行使master的动作
                            logger.info("我是主节点，开始干活");
                            CountDownLatch count = new CountDownLatch(1);
                            try {
                                count.await();
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                            break;

                        case NODEEXISTS:
                            // 主节点已经存在的时候，监听该节点
                            masterExists();
                            break;

                        default:
                            states = MasterStates.NOTELECTED;
                            logger.error("创建master节点的时候发生了未知错误: "
                                    + KeeperException.create(KeeperException.Code.get(rc), path));

                    }
                }, null);
    }

    private void checkMaster() {
        zk.getData(masterNode, false,
                (int rc, String path, Object ctx, byte[] data, Stat stat) -> {
                    switch (KeeperException.Code.get(rc)) {
                        case CONNECTIONLOSS:
                            checkMaster();
                            break;
                        case NONODE:
                            runForMaster();
                            break;
                        case OK:
                            if (serverId.equals(new String(data))) {
                                states = MasterStates.ELECTED;
                            } else {
                                logger.info("我是备用节点");
                                logger.info("主节点中的serverId是：" + new String(data));
                                logger.info("本机的serverId是：" + serverId);
                                states = MasterStates.NOTELECTED;
                            }
                    }
                }, null);
    }

    private void masterExists() {
        zk.exists(masterNode, (WatchedEvent event) -> {
            // 判断event的事件，如果是node被删除的话，证明master掉线了，开始竞选master
            if (event.getType() == Watcher.Event.EventType.NodeDeleted) {
                runForMaster();
            }

        }, (int rc, String path, Object ctx, Stat stat) -> {
            switch (KeeperException.Code.get(rc)) {
                case CONNECTIONLOSS:
                    masterExists();
                    break;

                case OK:
                    if (stat == null) {
                        runForMaster();
                        break;
                    }
                default:
                    checkMaster();
                    break;
            }
        }, null);
    }

    @Test
    public void masterChangeTest() throws IOException, InterruptedException {
        CountDownLatch count = new CountDownLatch(1);
        initZookeeper();
        runForMaster();
        count.await();
    }
}
