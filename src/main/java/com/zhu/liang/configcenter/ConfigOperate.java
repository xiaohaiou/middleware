package com.zhu.liang.configcenter;

import com.google.common.collect.Maps;
import com.zhu.liang.util.SpringContextHolder;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.util.Arrays;
import java.util.Map;

/**
 * @创建人 ZHULIANG
 * @创建人时间 2020/3/13
 * @描述
 *      操作流程，按照配置信息依次加载本地配置信息和zk配置信息
 *   对比俩数据源，以本地数据为准，添加到缓存。如果本地配置多于zk， 添加本地信息到zk.
 *   本地信息缺失（少于zk），则读取zk配置信息到缓存.
 */
@Component
public class ConfigOperate {

    private static final Logger logger = LoggerFactory.getLogger(ConfigOperate.class);

    @Autowired
    private ConfigProperties zookeeperProperties;

    @Autowired
    private ZKOperate zkOperate;

    @Autowired
    private ZKWatcherListener zkWatcherListener;

    @Autowired
    private SpringContextHolder springContextHolder;

    @Cacheable(value = "conf")
    public Map getConfig() {
        Map localPropertiesMap = zookeeperProperties.parseToMap();
        final Map resultMap = Maps.newHashMap();

        if(CollectionUtils.isEmpty(localPropertiesMap)){
            logger.info("ConfigOperate 读取配置信息，本地配置文件为空，采用zk 配置信息，conf:{}",
                    Arrays.toString(resultMap.values().toArray()));
            return resultMap;
        }

        if(StringUtils.isNotEmpty(zookeeperProperties.getName())){
            resultMap.putAll(zkOperate.getAllRootData(zookeeperProperties.getName(),zkWatcherListener));
        }

        localPropertiesMap.forEach((key,value)->{
            if(!resultMap.containsKey(key)){
                zkOperate.createNode(zookeeperProperties.getName()+"/"+key,String.valueOf(value));
            }
            if( value!=null && !value.equals(resultMap.get(key))){
                resultMap.put(key,value);
            }
        });
        logger.info("ConfigOperate 读取配置信息成功！conf:{}",Arrays.toString(resultMap.values().toArray()));
        return resultMap;
    }

    /**
     * 新增缓存
     * @param key
     * @param value
     * @return
     */
    @CachePut("conf")
    public Map addConfig(String key,Object value){
        ConfigOperate configOperate = (ConfigOperate)springContextHolder.getBean(ConfigOperate.class);
        Map confMap =  configOperate.getConfig();
        logger.info("ConfigOperate 新增配置zk配置。key:{},value:{}",key,value);
        zkOperate.updateStat(zookeeperProperties.getName()+"/"+key,String.valueOf(value));
        confMap.put(key,value);
        return confMap;
    }

    /**
     * 替换缓存配置
     * @param map
     * @return
     */
    @CachePut("conf")
    public Map replaceConfig(Map map){
        if(CollectionUtils.isEmpty(map)){
            logger.info("ConfigOperate 替换缓存配置内容失败！传入参数为空。");
        }
        ConfigOperate configOperate = (ConfigOperate)springContextHolder.getBean(ConfigOperate.class);
        Map confMap =  configOperate.getConfig();
        logger.info("ConfigOperate 开始替换缓存配置内容，原配置信息 conf:{}",Arrays.toString(confMap.values().toArray()));
        confMap.forEach((key,value)->{
            zkOperate.deleteNode(zookeeperProperties.getName()+"/"+key);
        });
        map.forEach((key,value)->{
            zkOperate.createNode(zookeeperProperties.getName()+"/"+key,String.valueOf(value));
        });
        logger.info("ConfigOperate 替换缓存配置内容成功，配置信息 conf:{}",Arrays.toString(map.values().toArray()));
        return map;
    }

    /**
     * 删除缓存中的某一个配置项
     * @param key
     * @return
     */
    @CachePut("conf")
    public Map reduceConfig(String key){
        ConfigOperate configOperate = (ConfigOperate)springContextHolder.getBean(ConfigOperate.class);
        Map confMap =  configOperate.getConfig();
        if(StringUtils.isEmpty(key)){
            logger.info("ConfigOperate  删除配置内容失败！传入参数为空。");
            return confMap;
        }
        confMap.remove(key);
        zkOperate.deleteNode(zookeeperProperties.getName()+"/"+key);
        return confMap;
    }

    /**
     * 删除所有的缓存
     *  （方法执行成功）
     */
    @CacheEvict(value="conf", beforeInvocation=true)
    public void deleteConfig(){
        ConfigOperate configOperate = (ConfigOperate)springContextHolder.getBean(ConfigOperate.class);
        Map confMap =  configOperate.getConfig();
        confMap.forEach((key,value)->{
            zkOperate.deleteNode(zookeeperProperties.getName()+"/"+key);
        });
        logger.info("ConfigOperate 删除spring cache 配置信息，conf:[{}]", Arrays.toString(confMap.values().toArray()));
    }

    public void addRoot(){
        zkOperate.createNode(zookeeperProperties.getName(),"");
        logger.info("ConfigOperate 创建根节点，root:[{}]",zookeeperProperties.getName());
    }

}
