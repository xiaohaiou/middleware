package com.zhu.liang.controller;

import com.zhu.liang.bean.config.ConfigAddRequest;
import com.zhu.liang.configcenter.ConfigOperate;
import com.zhu.liang.configcenter.ConfigProperties;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RequestMapping("/api/v1.0/configCenter")
@Api(value = "/api/v1.0/configCenter", description = "configCenter 测试接口")
@Controller
public class ConfigCenterController {

    private static final Logger logger = LoggerFactory.getLogger(ConfigCenterController.class);

    @Autowired
    private ConfigOperate configOperate;

    @Autowired
    private ConfigProperties configProperties;

    @ApiOperation(value = "接口", notes = "接口", nickname = "test")
    @RequestMapping(value = "/test", method = RequestMethod.GET)
    @ResponseBody
    public String test() {
        return configProperties.toString();
    }

    @ApiOperation(value = "创建根节点", notes = "创建根节点", nickname = "addRoot")
    @RequestMapping(value = "/addRoot", method = RequestMethod.POST)
    @ResponseBody
    public String addRoot() {
        configOperate.addRoot();
        return configProperties.getName();
    }

    @ApiOperation(value = "查询配置信息", notes = "配置中心 查找配置信息", nickname = "getConfig")
    @RequestMapping(value = "/getConfig", method = RequestMethod.GET)
    @ResponseBody
    public Map getConfig() {
        return configOperate.getConfig();
    }

    @ApiOperation(value = "新增配置信息", notes = "配置中心 新增配置信息", nickname = "addConfig")
    @RequestMapping(value = "/addConfig", method = RequestMethod.POST)
    @ResponseBody
    public Map addConfig(@RequestBody ConfigAddRequest configAddRequest) {
        return configOperate.addConfig(configAddRequest.getKey(),configAddRequest.getValue());
    }

    @ApiOperation(value = "替换配置信息", notes = "配置中心 替换配置信息", nickname = "replaceConfig")
    @RequestMapping(value = "/replaceConfig", method = RequestMethod.POST)
    @ResponseBody
    public Map replaceConfig(@RequestBody Map map) {
        return configOperate.replaceConfig(map);
    }

    @ApiOperation(value = "删除配置信息", notes = "配置中心 删除配置信息", nickname = "reduceConfig")
    @RequestMapping(value = "/reduceConfig", method = RequestMethod.DELETE)
    @ResponseBody
    public Map reduceConfig(@RequestParam String key) {
        return configOperate.reduceConfig(key);
    }

    @ApiOperation(value = "删除配置信息", notes = "配置中心 删除配置信息", nickname = "deleteConfig")
    @RequestMapping(value = "/deleteConfig", method = RequestMethod.DELETE)
    @ResponseBody
    public String deleteConfig() {
        configOperate.deleteConfig();
        return "success";
    }

}
