package com.suntr.springSample;

import com.suntr.springSample.model.Car;

import java.util.HashMap;
import java.util.Map;

public class InstanceCarFactory {

    private Map<String, Car> cars = null;

    public InstanceCarFactory(){
        cars = new HashMap<>();
        cars.put("audi", new Car("audi", 30000));
        cars.put("ford", new Car("ford", 20001));
    }

    public Car getCar(String name){
        return cars.get(name);
    }
}
