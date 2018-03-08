package org.lordstar.learning.zookeeper;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.apache.zookeeper.*;
import org.junit.Test;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;
import java.util.concurrent.CountDownLatch;

public class ZookeeperTest {
    private static Logger logger = Logger.getLogger(ZookeeperTest.class);

    private ZooKeeper zk;

    private CountDownLatch connectLatch = new CountDownLatch(1);

    private String path = "/node";

    public void initZookeeper() throws IOException, InterruptedException, KeeperException {

        String hostPort = getZookeeperHost();
        if(StringUtils.isEmpty(hostPort)) {
            logger.error("zookeeper hostPort is empty");
            return;
        }
        zk = new ZooKeeper(hostPort, 15000, new Watcher() {
            public void process(WatchedEvent event) {
                if (event.getState() == Event.KeeperState.SyncConnected) {
                    connectLatch.countDown();
                    logger.info("客户端已经连接上了");
                    if(event.getType() == Event.EventType.NodeCreated) {
                        logger.info("创建了节点");
                    }
                }
            }
        });
        connectLatch.await();
        zk.exists(path, true);
        zk.create(path, new byte[0], ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.EPHEMERAL);
    }

    public String getZookeeperHost() {
        Properties pro = new Properties();
        InputStream in = null;
        in = ZookeeperTest.class.getClassLoader().getResourceAsStream("config.properties");
        try {
            pro.load(in);
        } catch (IOException e) {
            logger.error("读取配置文件错误: " + e);
        }
        String hostPort = pro.getProperty("zookeeperList");
        if (StringUtils.isEmpty(hostPort)) {
            logger.error("zookeeper hostPort is empty");
            return "";
        }
        return hostPort;
    }

    @Test
    public void hostTest() {
        String host = getZookeeperHost();
        System.out.println(host);
    }

    @Test
    public void initZookeeperTest() throws IOException, InterruptedException, KeeperException {
        initZookeeper();
        Thread.sleep(50000);
    }
}

class NodeWatcher implements Watcher {

    public void process(WatchedEvent event) {

    }
}
