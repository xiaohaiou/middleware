package com.zhu.liang.configcenter;

import com.google.common.collect.Maps;
import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.KeeperException;
import org.apache.zookeeper.Watcher;
import org.apache.zookeeper.ZooDefs;
import org.apache.zookeeper.data.ACL;
import org.apache.zookeeper.data.Stat;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

/**
 * @创建人 ZHULIANG
 * @创建人时间 2020/3/17
 * @描述
 */
@Component
public class ZKOperate {

    private static final Logger logger = LoggerFactory.getLogger(ZKOperate.class);

    @Autowired
    private ZKWatcher zkWatcher;

    /**
     *  创建节点信息
     * @param nodePath
     * @param nodeData
     */
    public void createNode(String nodePath,String nodeData){
        List<ACL> acl = ZooDefs.Ids.OPEN_ACL_UNSAFE;
        CreateMode createMode = CreateMode.PERSISTENT;
        String result = null;
        try {
            result = zkWatcher.getZooKeeper().create(nodePath, nodeData.getBytes(), acl, createMode);
        } catch (KeeperException e) {
            logger.error("ZKOperate 创建节点失败！创建路径:[{}]，创建数据:[{}]",nodePath,nodeData);
            e.printStackTrace();
            return;
        } catch (InterruptedException e) {
            logger.error("ZKOperate 创建节点失败！创建路径:[{}]，创建数据:[{}]",nodePath,nodeData);
            e.printStackTrace();
            return;
        }
        logger.info("ZKOperate 创建节点返回结果：{}",result);
        logger.info("ZKOperate 完成创建节点：{}， 数据：{}",nodePath,nodeData);
    }

    /**
     * 查询节点结构信息
     * @param nodePath
     * @return
     */
    public Stat queryStat(String nodePath){
        Stat stat = null;
        try {
            stat = zkWatcher.getZooKeeper().exists(nodePath, false);
        } catch (KeeperException e) {
            logger.error("ZKOperate 查询节点Stat结构异常！path：{}，version：{}", nodePath, stat==null?"":stat.getVersion());
            e.printStackTrace();
            return stat;
        } catch (InterruptedException e) {
            logger.error("ZKOperate 查询节点Stat结构异常！path：{}，version：{}", nodePath, stat==null?"":stat.getVersion());
            e.printStackTrace();
            return stat;
        }
        logger.info("ZKOperate 结束查询节点Stat，path：{}，version：{}", nodePath, stat==null?"":stat.getVersion());
        return stat;
    }

    /**
     * 查询节点数据信息
     * @param nodePath
     * @return
     */
    public String queryData(String nodePath){
        String data = null;
        try {
            Stat stat = queryStat(nodePath);
            if(null == stat){
                return null;
            }
            data = new String(zkWatcher.getZooKeeper().getData(nodePath, false, queryStat(nodePath)));
        } catch (KeeperException e) {
            logger.error("ZKOperate 查询节点Data异常！path：{}，Data：{}", nodePath, data);
            e.printStackTrace();
            return data;
        } catch (InterruptedException e) {
            logger.error("ZKOperate 查询节点Data异常！path：{}，Data：{}", nodePath, data);
            e.printStackTrace();
            return data;
        }
        logger.info("ZKOperate 结束查询节点Data,path：{}，Data：{}", nodePath, data);
        return data;
    }

    /**
     * 递归查找根路径下所有的节点数据
     * @param rootPath
     * @return
     */
    public Map getAllRootData(final String rootPath, Watcher watcher){
        final Map resultMap = Maps.newHashMap();
        try {
            List<String> paths = zkWatcher.getZooKeeper().getChildren(rootPath,watcher);
            if(!CollectionUtils.isEmpty(paths)){
                paths.stream().forEach(path->{
                    String value= this.queryData(rootPath+"/"+path);
                    resultMap.put(path,value);
                });
            }
        } catch (KeeperException e) {
            logger.error("ZKOperate 遍历查找配置异常，rootPath:{},data:{}",rootPath,Arrays.toString(resultMap.values().toArray()));
            e.printStackTrace();
            return resultMap;
        } catch (InterruptedException e) {
            logger.error("ZKOperate 遍历查找配置异常，rootPath:{},data:{}",rootPath,Arrays.toString(resultMap.values().toArray()));
            e.printStackTrace();
            return resultMap;
        }
        logger.info("ZKOperate 结束查找配置信息，path:{},data:{}",rootPath, Arrays.toString(resultMap.values().toArray()));
        return resultMap;
    }

    /**
     * 修改节点信息，返回原节点信息。
     * @param nodePath
     * @param nodeData
     * @return
     */
    public Stat updateStat(String nodePath,String nodeData){
        Stat stat = queryStat(nodePath);
        logger.info("ZKOperate 准备修改节点，path：{}，data：{}，原version：{}", nodePath, nodeData, stat==null?"":stat.getVersion());
        Stat newStat = null;
        try {
            newStat = zkWatcher.getZooKeeper().setData(nodePath, nodeData.getBytes(), stat==null?null:stat.getVersion());
        } catch (KeeperException e) {
            logger.info("ZKOperate 修改节点异常！path：{}，data：{}，原version：{}", nodePath, nodeData, stat.getVersion());
            e.printStackTrace();
            return stat;
        } catch (InterruptedException e) {
            logger.info("ZKOperate 修改节点异常！path：{}，data：{}，原version：{}", nodePath, nodeData, stat.getVersion());
            e.printStackTrace();
            return stat;
        }
        //修改节点值有两种方法，上面是第一种，还有一种可以使用回调函数及参数传递，与上面方法名称相同。
        //zk.setData(path, data, version, cb, ctx);
        logger.info("ZKOperate 完成修改节点，path：{}，data：{}，现version：{}", nodePath, nodeData, newStat==null?null:stat.getVersion());
        return stat;
    }

    /**
     * 删除节点信息
     * @param nodePath
     */
    public void deleteNode(String nodePath){
        Stat stat = queryStat(nodePath);
        logger.info("ZKOperate 准备删除节点，path：{}，原version：{}", nodePath, stat==null?null:stat.getVersion());
        try {
            zkWatcher.getZooKeeper().delete(nodePath, stat==null?null:stat.getVersion());
        } catch (InterruptedException e) {
            logger.error("ZKOperate 删除节点异常！path：{}", nodePath);
            e.printStackTrace();
            return;
        } catch (KeeperException e) {
            logger.error("ZKOperate 删除节点异常！path：{}", nodePath);
            e.printStackTrace();
            return;
        }
        //修改节点值有两种方法，上面是第一种，还有一种可以使用回调函数及参数传递，与上面方法名称相同。
        //zk.delete(path, version, cb, ctx);
        logger.info("ZKOperate 完成删除节点，path：{}", nodePath);
    }

}
