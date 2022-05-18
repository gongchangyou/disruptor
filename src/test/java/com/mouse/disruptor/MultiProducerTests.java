package com.mouse.disruptor;

import com.lmax.disruptor.EventFactory;
import com.lmax.disruptor.RingBuffer;
import com.lmax.disruptor.YieldingWaitStrategy;
import com.lmax.disruptor.dsl.Disruptor;
import com.lmax.disruptor.dsl.ProducerType;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.Executors;

@SpringBootTest
@Slf4j
class MultiProducerTests {

    /**
     * 多生产者 单消费者
     */
    @Test
    void contextLoads() {
        EventFactory<Order> factory = new OrderFactory();
        int ringBufferSize = 1024 * 1024;
        //ProducerType要设置为MULTI，后面才可以使用多生产者模式
        Disruptor<Order> disruptor =
                new Disruptor<Order>(factory, ringBufferSize, Executors.defaultThreadFactory(), ProducerType.MULTI, new YieldingWaitStrategy());
        //简化问题，设置为单消费者模式，也可以设置为多消费者及消费者间多重依赖。
        disruptor.handleEventsWith(new OrderHandler1("1"));
        disruptor.start();
        final RingBuffer<Order> ringBuffer = disruptor.getRingBuffer();
        //判断生产者是否已经生产完毕
        final CountDownLatch countDownLatch = new CountDownLatch(3);
        //单生产者，生产3条数据
        for (int l = 0; l < 3; l++) {
            Thread thread = new Thread() {
                @Override
                public void run() {
                    for(int i = 0; i < 3; i++) {
                        new Producer(ringBuffer).onData(Thread.currentThread().getName() + "'s " + i + "th message");
                    }
                    countDownLatch.countDown();
                }
            };
            thread.setName("producer thread " + l);
            thread.start();
        }
        try {
            countDownLatch.await();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        //为了保证消费者线程已经启动，留足足够的时间。具体原因详见另一篇博客：disruptor的shutdown失效问题
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        disruptor.shutdown();
    }
}
