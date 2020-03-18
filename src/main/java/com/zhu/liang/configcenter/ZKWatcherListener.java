package com.zhu.liang.configcenter;

import com.zhu.liang.util.SpringContextHolder;
import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.Watcher;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.stereotype.Component;

/**
 * @创建人 ZHULIANG
 * @创建人时间 2020/3/18
 * @描述
 */
@Component
public class ZKWatcherListener implements Watcher{

    private static final Logger logger = LoggerFactory.getLogger(ZKWatcherListener.class);

    @Autowired
    private SpringContextHolder springContextHolder;

    @Override
    public void process(WatchedEvent watchedEvent) {
        logger.info("ConfigOperateListener 监听到 zk watcherEvent 事件。");
        ZKWatcherListener zkWatcherListener = (ZKWatcherListener)springContextHolder.getBean(ZKWatcherListener.class);

        // 事件类型
        Event.EventType eventType = watchedEvent.getType();
        if (watchedEvent.getState() == Event.KeeperState.SyncConnected) {
            if (Event.EventType.NodeCreated == eventType) {
                logger.info("节点创建");
                zkWatcherListener.deleteConfigCache();
//                configOperate.getConfig();
            }
            //更新节点
            else if (Event.EventType.NodeDataChanged == eventType) {
                logger.info("节点数据更新");
                zkWatcherListener.deleteConfigCache();
//                configOperate.getConfig();
            }
            //更新子节点
            else if (Event.EventType.NodeChildrenChanged == eventType) {
                logger.info("子节点变更");
                zkWatcherListener.deleteConfigCache();
//                configOperate.getConfig();
            }
            //删除节点
            else if (Event.EventType.NodeDeleted == eventType) {
                logger.info( "节点被删除");
                zkWatcherListener.deleteConfigCache();
//                configOperate.getConfig();
            }
        }
    }

    /**
     * 删除缓存信息
     */
    @CacheEvict(value="conf", beforeInvocation=true)
    public void deleteConfigCache(){return;}

}
