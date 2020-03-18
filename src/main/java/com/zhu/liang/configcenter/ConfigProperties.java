package com.zhu.liang.configcenter;

import com.google.common.collect.Maps;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.Map;

/**
 * @创建人 ZHULIANG
 * @创建人时间 2020/3/13
 * @描述 zk 配置信息
 */
@Component
@ConfigurationProperties(prefix="config")
public class ConfigProperties {

    private String address;

    private Integer port;

    private String test;

    private String name;

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public Integer getPort() {
        return port;
    }

    public void setPort(Integer port) {
        this.port = port;
    }

    public String getTest() {
        return test;
    }

    public void setTest(String test) {
        this.test = test;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return "ConfigProperties{" +
                "address='" + address + '\'' +
                ", port=" + port +
                ", test='" + test + '\'' +
                ", name='" + name + '\'' +
                '}';
    }

    public Map parseToMap(){
        Map resultMap = Maps.newHashMap();
        Field[] fields = this.getClass().getDeclaredFields();
        Arrays.stream(fields).forEach(field -> {
            String varName = field.getName();
            boolean accessFlag = field.isAccessible();
            field.setAccessible(true);
            Object value = null;
            try {
                value = field.get(this);
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
            resultMap.put(varName,value);
            field.setAccessible(accessFlag);
        });
        System.out.println("ConfigProperties 转换成Map,data:"+Arrays.toString(resultMap.values().toArray()));
        return resultMap;
    }

}
