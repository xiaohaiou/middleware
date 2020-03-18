package com.zhu.liang.demos.future;

import java.util.concurrent.Callable;

/**
 * @创建人 ZHULIANG
 * @创建人时间 2020/3/12
 * @描述
 */
public class LongTimeTask implements Callable<Long> {

    private Long longTime;

    LongTimeTask(Long longTime){
        this.longTime = longTime;
    }

    @Override
    public Long call() throws Exception {
        Thread.sleep(longTime);
        return longTime;
    }

}
