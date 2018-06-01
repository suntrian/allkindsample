package com.suntr.springSample.model;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanPostProcessor;

import java.util.Properties;

public class MyFakeDatasourcePostProcessor implements BeanPostProcessor {
    @Override
    public Object postProcessBeforeInitialization(Object bean, String beanName) throws BeansException {
        System.out.println("postProcessBeforeInitialization:    " + bean + "," + beanName);
        if ("fakeDatasource".equals(beanName)) {
            FakeDatasource datasource = new FakeDatasource();
            if (datasource.getProperties() == null){
                datasource.setProperties(new Properties());
            };
            datasource.getProperties().setProperty("added","another");
            return datasource;
        }
        return bean;
    }

    @Override
    public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
        System.out.println("postProcessAfterInitialization: " + bean + "," + beanName);
        return bean;
    }
}
