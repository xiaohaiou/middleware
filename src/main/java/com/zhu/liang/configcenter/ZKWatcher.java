package com.zhu.liang.configcenter;

import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.Watcher;
import org.apache.zookeeper.ZooKeeper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.util.concurrent.CountDownLatch;

/**
 * @创建人 ZHULIANG
 * @创建人时间 2020/3/16
 * @描述
 */
@Component
@ConfigurationProperties(prefix="zookeeper")
public class ZKWatcher implements Watcher {

    private static final Logger logger = LoggerFactory.getLogger(ZKWatcher.class);

    public String address;

    public int timeout;

    /**
     * 同步锁，等待监听启动信息
     */
    private CountDownLatch latch = new CountDownLatch(1);

    private ZooKeeper zooKeeper;

//    ZKWatcher(){
//        this.conn();
//    }

    @Override
    public void process(WatchedEvent event) {
        if (event.getState() == Event.KeeperState.SyncConnected) {
            latch.countDown();
        }
    }

    @PostConstruct
    public void conn(){
        try {
            zooKeeper = new ZooKeeper(address, timeout, this);
        } catch (IOException e) {
            logger.error("zk 链接异常！");
            e.printStackTrace();
        }
        try {
            latch.await();
        } catch (InterruptedException e) {
            logger.error("zk 同步异常！");
            e.printStackTrace();
        }
        setZooKeeper(zooKeeper);
        logger.info("Zookeeper已连接成功：" + address);
    }

    public ZooKeeper getZooKeeper() {
        return zooKeeper;
    }

    public void setZooKeeper(ZooKeeper zooKeeper) {
        this.zooKeeper = zooKeeper;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public int getTimeout() {
        return timeout;
    }

    public void setTimeout(int timeout) {
        this.timeout = timeout;
    }
}
