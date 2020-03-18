package com.zhu.liang.loadfile;

import io.netty.util.internal.SystemPropertyUtil;
import org.hibernate.validator.constraints.Range;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;
import org.springframework.validation.annotation.Validated;
import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

/**
 * @创建人 ZHULIANG
 * @创建人时间 2020/3/12
 * @描述
 */
@Component
@ConfigurationProperties(prefix="netty.file")
@Validated
public class NettyFileProperties {

    private static final Logger logger = LoggerFactory.getLogger(NettyFileProperties.class);

    @NotNull(message = "端口不能为空")
    @Range(min=1000, max=60000)
    private Integer port;

    @NotNull(message = "文件路径不能为空")
    private String path;

    @Pattern(regexp="((25[0-5]|2[0-4]\\d|((1\\d{2})|([1-9]?\\d)))\\.){3}(25[0-5]|2[0-4]\\d|((1\\d{2})|([1-9]?\\d)))",message="ip地址格式不正确")
    private String bindIp;

    //必须大于1 ,老板线程，即认为是分配工作的线程
    @DecimalMin("1")
    private Integer bossThreads = Math.max(1, SystemPropertyUtil.getInt(
            "io.netty.eventLoopThreads", Runtime.getRuntime().availableProcessors() * 2));

    //必须大于1，实际工作线程数量，这个数量最好根据JVM的系统信息进行配置，这里直接动态获取
    @DecimalMin("1")
    private Integer workThreads = Math.max(1, SystemPropertyUtil.getInt(
            "io.netty.eventLoopThreads", Runtime.getRuntime().availableProcessors() * 2));

    public Integer getPort() {
        return port;
    }

    public void setPort(Integer port) {
        this.port = port;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getBindIp() {
        return bindIp;
    }

    public void setBindIp(String bindIp) {
        this.bindIp = bindIp;
    }

    public Integer getBossThreads() {
        return bossThreads;
    }

    public void setBossThreads(Integer bossThreads) {
        this.bossThreads = bossThreads;
    }

    public Integer getWorkThreads() {
        return workThreads;
    }

    public void setWorkThreads(Integer workThreads) {
        this.workThreads = workThreads;
    }

}
