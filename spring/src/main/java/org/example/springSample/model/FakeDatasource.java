package org.example.springSample.model;

import java.util.Properties;

public class FakeDatasource {

    private Properties properties;

    public void initMethod(){
        System.out.println("FakeDatasource　InitMethod");
    }

    public void destroyMethod(){
        System.out.println("FakeDatasource Destroyed");
    }

    public Properties getProperties() {
        return properties;
    }

    public void setProperties(Properties properties) {
        this.properties = properties;
    }

    @Override
    public String toString() {
        return "FakeDatasource{" +
                "properties=" + properties +
                '}';
    }
}
