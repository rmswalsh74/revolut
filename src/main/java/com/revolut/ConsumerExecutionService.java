package com.revolut;

import com.revolut.event.command.IntraBankTransferCommandEvent;
import com.revolut.executor.Invoker;
import com.revolut.executor.Result;
import com.revolut.executor.ce.IntraBankTransferCommandExecutor;
import com.revolut.integration.MessageBroker;
import io.dropwizard.lifecycle.Managed;
import jersey.repackaged.com.google.common.util.concurrent.ThreadFactoryBuilder;
import org.glassfish.jersey.process.JerseyProcessingUncaughtExceptionHandler;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ConsumerExecutionService implements Managed {

    IntraBankTransferCommandExecutor executor;

    private static final ExecutorService QUEUE_EXECUTOR = Executors.newCachedThreadPool(new ThreadFactoryBuilder()
            .setNameFormat("fire&forget-transaction-executor-%d")
            .setUncaughtExceptionHandler(new JerseyProcessingUncaughtExceptionHandler())
            .build());
    public ConsumerExecutionService(IntraBankTransferCommandExecutor executor) {
        this.executor=executor;
    }

    @Override
    public void start() throws Exception {
        QUEUE_EXECUTOR.submit(new Runnable() {

            @Override
            public void run() {
                while (true) {
                    try {
                        System.out.println("Process Consumer Waiting for Message");
                        process(MessageBroker.getInstance().consume());
                        System.out.println("Process Consumer Processed Message");
                    } catch (InterruptedException e) {
                        System.out.println("Process Consumer InterruptedException:"+e.getMessage());
                        e.printStackTrace();
                    }
                }
            }
        });
    }

    protected void process(IntraBankTransferCommandEvent event) {
        if (event==null) return;
        System.out.println("Process event::::"+event);
        Invoker invoker=new Invoker(executor, event);
        Result result=invoker.invokeCommandUnmanaged();
    }

    @Override
    public void stop() throws Exception {
        try {
            QUEUE_EXECUTOR.shutdown();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
        }
    }
}
