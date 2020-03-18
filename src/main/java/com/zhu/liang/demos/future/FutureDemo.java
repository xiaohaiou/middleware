package com.zhu.liang.demos.future;

import java.util.concurrent.*;

/**
 * @创建人 ZHULIANG
 * @创建人时间 2020/3/12
 * @描述
 */
public class FutureDemo {

    public static void main(String[] args){
        Long sleepTime = 1000L; //ms
        Callable<Long> sleepTask = new LongTimeTask(sleepTime);
        ExecutorService executorService = Executors.newSingleThreadScheduledExecutor();

        Long startTime = System.currentTimeMillis();
        Future<Long> future = executorService.submit(sleepTask);

        try {
            Thread.sleep(100);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        System.out.println("Main UsedTime:"+(System.currentTimeMillis()-startTime));
        try {
            System.out.println("Future UsedTime:"+future.get());
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
        System.out.println("Main UsedTime:"+(System.currentTimeMillis()-startTime));
        executorService.shutdown();
    }

}
