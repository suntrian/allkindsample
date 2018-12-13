package com.suntr.webfluxSample;

import org.springframework.context.annotation.AnnotationConfigApplicationContext;

public class SpringAnnotationStartup {

    public static void main(String[] args) {
        AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext(BasicController.class);
        BasicController controller = (BasicController) context.getBean("basicController");
        System.out.println(controller.sayHello());
    }
}
