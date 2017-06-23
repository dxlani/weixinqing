package me.mymilkbottles.weixinqing.async;

import java.util.concurrent.*;

/**
 * Created by Administrator on 2017/06/23 23:32.
 */
public class HandlerSolveThreadPool {

    private static ExecutorService executors = Executors.newFixedThreadPool(8);

    public void handlerSolve() {
        Future<Event> future = executors.submit(new Callable<Event>() {
            @Override
            public Event call() throws Exception {

                return null;
            }
        });


    }

}
