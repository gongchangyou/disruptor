package com.mouse.disruptor;

import com.lmax.disruptor.EventFactory;

/**
 * @author gongchangyou
 * @version 1.0
 * @date 2022/5/18 14:07
 */
public class OrderFactory implements EventFactory<Order> {
    @Override
    public Order newInstance() {
        return new Order();
    }
}
