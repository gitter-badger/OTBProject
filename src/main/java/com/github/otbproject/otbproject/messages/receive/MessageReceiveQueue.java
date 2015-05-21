package com.github.otbproject.otbproject.messages.receive;

import java.util.concurrent.LinkedBlockingQueue;

public class MessageReceiveQueue {
    private final LinkedBlockingQueue<PackagedMessage> queue = new LinkedBlockingQueue<>();

    public PackagedMessage take() throws InterruptedException {
        return queue.take();
    }

    public boolean add(PackagedMessage message) {
        return queue.add(message);
    }

    public void clear() {
        queue.clear();
    }

    public int size() {
        return queue.size();
    }
}
