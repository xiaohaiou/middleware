package com.zhu.liang.bean.config;

import io.swagger.annotations.ApiModelProperty;

/**
 * @创建人 ZHULIANG
 * @创建人时间 2020/3/17
 * @描述
 */
public class ConfigAddRequest {

    @ApiModelProperty(name = "配置参数名称",required = true)
    private String key;
    @ApiModelProperty(name = "配置参数值",required = true)
    private String value;

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }
}
