package com.suntr.misc;

import lombok.Data;

import java.io.Serializable;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

@Data
public class GenericClassSample{

    public static void main(String[] args) {
        GenericClass<String, Integer> c = new GenericClass<String, Integer>(){};
        Type t = c.getClass().getGenericSuperclass();
        System.out.println(t);
        Type type = c.getT();
        System.out.println(type);
        System.out.println(c.getClass());

        Type type1 = c.getPK();
        System.out.println(type1);
    }


}

abstract class GenericClass<T, PK extends Serializable> {
    private T obj;

    @SuppressWarnings("unchecked")
    public Class<T> getT() {
        return (Class<T>)((ParameterizedType)getClass().getGenericSuperclass()).getActualTypeArguments()[0];
    }

    @SuppressWarnings("unchecked")
    public Class<PK> getPK() {
        return (Class<PK>)((ParameterizedType)getClass().getGenericSuperclass()).getActualTypeArguments()[1];
    }

    public Class getGenericType() {
        Class c = this.getClass();
        return c;
    }
}

@Data
class User {
    Integer id;
    String name;
}