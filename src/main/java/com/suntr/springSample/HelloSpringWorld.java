package com.suntr.springSample;

public class HelloSpringWorld {

    private String name;

    public HelloSpringWorld() {
        System.out.println("constructor without args");
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        System.out.println("name setted here:" + name);
        this.name = name;
    }

    public void sayHello() {
        System.out.println("Hello:" + getName());
    }
}
