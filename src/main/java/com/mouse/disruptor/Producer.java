package com.mouse.disruptor;

import com.lmax.disruptor.RingBuffer;

/**
 * @author gongchangyou
 * @version 1.0
 * @date 2022/5/18 14:07
 */

public class Producer {

    private final RingBuffer<Order> ringBuffer;
    public Producer(RingBuffer<Order> ringBuffer){
        this.ringBuffer = ringBuffer;
    }
    public void onData(String data){
        long sequence = ringBuffer.next();
        try {
            Order order = ringBuffer.get(sequence);
            order.setId(data);
        } finally {
            ringBuffer.publish(sequence);
        }
    }
}
