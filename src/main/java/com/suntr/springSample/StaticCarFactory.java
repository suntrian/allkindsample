package com.suntr.springSample;


import com.suntr.springSample.model.Car;

import java.util.HashMap;
import java.util.Map;

public class StaticCarFactory {


    private static Map<String,Car> cars=new HashMap<String, Car>();

    static {
        cars.put("audi",new Car("Audi",300000));
        cars.put("ford",new Car("Ford",200000));
    }


    //静态工厂方法
    public static Car getCar(String name){
        return cars.get(name);
    }

}
