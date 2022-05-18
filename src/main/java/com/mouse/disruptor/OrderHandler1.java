package com.mouse.disruptor;

import com.lmax.disruptor.EventHandler;
import com.lmax.disruptor.WorkHandler;

/**
 * @author gongchangyou
 * @version 1.0
 * @date 2022/5/18 14:08
 */
public class OrderHandler1 implements EventHandler<Order>, WorkHandler<Order> {
    private String consumerId;
    public OrderHandler1(String consumerId){
        this.consumerId = consumerId;
    }

    //EventHandler的方法
    @Override
    public void onEvent(Order order, long sequence, boolean endOfBatch) throws Exception {
        System.out.println("OrderHandler1 " + this.consumerId + "，消费信息：" + order.getId());
    }

    //WorkHandler的方法
    @Override
    public void onEvent(Order order) throws Exception {
        System.out.println("OrderHandler1 " + this.consumerId + "，消费信息：" + order.getId());
    }
}
