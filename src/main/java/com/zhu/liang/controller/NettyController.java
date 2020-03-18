package com.zhu.liang.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

@RequestMapping("/api/v1.0/netty")
@Api(value = "/api/v1.0/netty", description = "netty 测试接口")
@Controller
public class NettyController {

    private static final Logger logger = LoggerFactory.getLogger(NettyController.class);




    @ApiOperation(value = "接口", notes = "接口", nickname = "test")
    @RequestMapping(value = "/test", method = RequestMethod.POST)
    @ResponseBody
    public String batterySend(@RequestBody String msg) {
        logger.info(msg);
        return msg;
    }

}
