package com.revolut.integration;

import com.revolut.event.command.IntraBankTransferCommandEvent;
import com.revolut.executor.ce.IntraBankTransferCommandExecutor;

import javax.ws.rs.container.AsyncResponse;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingDeque;

public class MessageBroker {
    private static final BlockingQueue<AsyncResponse> suspended = new ArrayBlockingQueue<>(5);
    private BlockingQueue<IntraBankTransferCommandEvent> transferTopic = new ArrayBlockingQueue<IntraBankTransferCommandEvent>(100);

    private static final MessageBroker BROKER=new MessageBroker();

    private MessageBroker(){}

    public static MessageBroker getInstance() {
        return BROKER;
    }

    public BlockingQueue<IntraBankTransferCommandEvent> getTransferTopic() {
        return transferTopic;
    }

    public boolean publish(IntraBankTransferCommandEvent commandEvent) {
        return transferTopic.offer(commandEvent);
    }

    public IntraBankTransferCommandEvent consume() throws InterruptedException{
        return transferTopic.take();
    }
}
