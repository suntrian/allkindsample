package com.suntr.guava;

import com.google.common.eventbus.DeadEvent;
import com.google.common.eventbus.EventBus;
import com.google.common.eventbus.Subscribe;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class GuavaEventBus {

    public static void main(String[] args) {
        EventBus eventBus = new EventBus("test");
        EventListener listener1 = new EventListener();
        EventListener listener2 = new EventListener();
        DeadEventListener deadEventListener = new DeadEventListener();
        eventBus.register(listener1);
        eventBus.register(listener2);
        eventBus.register(deadEventListener);

        eventBus.post(new Event("hello World"));
        eventBus.post(new LongEvent(5555L));
        eventBus.post("hello world");
        /**
         * output:
         * 391447681 receive message:hello World
         * 152005629 receive message:hello World
         * 391447681 receive Long Message:5555
         * 152005629 receive Long Message:5555
         * 391447681 receive message:5555
         * 152005629 receive message:5555
         *
         */

    }


    public static class Event {
        private final String message;

        public Event(String message) {
            this.message = message;
        }

        public String getMessage() {
            return message;
        }
    }

    public static class LongEvent extends Event {

        public LongEvent(Long message1) {
            super(String.valueOf(message1));
        }

    }

    @Slf4j
    public static class EventListener {
        public String last;

        @Subscribe
        public void listen(Event event) {
            last = event.getMessage();
            log.info("{} receive message:{}", System.identityHashCode(this), last);
        }

        @Subscribe
        public void listen(LongEvent event) {
            last = event.getMessage();
            log.info("{} receive Long Message:{}", System.identityHashCode(this), last);
        }

    }

    @Slf4j
    public static class DeadEventListener {

        @Subscribe
        public void listen(DeadEvent event) {
            log.info("{} receive deadevent: {}", System.identityHashCode(this), event);
        }

    }

}
