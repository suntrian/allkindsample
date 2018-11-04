package com.suntr.springSample.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;

import java.util.List;
import java.util.Map;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class Person {

    private String name;
    private int age;
    private double account;
    private boolean gender;
    private Car car;
    private List<Car> cars;
    private Map<String, Car> carmap;

    private Address address;

    public Person() {}

    public Person(String name, int age, Car car) {
        this.name = name;
        this.age = age;
        this.car = car;
    }

    public Person(String name, int age, Car car, List<Car> cars) {
        this.name = name;
        this.age = age;
        this.car = car;
        this.cars = cars;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public Car getCar() {
        return car;
    }

    public void setCar(Car car) {
        this.car = car;
    }

    public List<Car> getCars() {
        return cars;
    }

    public void setCars(List<Car> cars) {
        this.cars = cars;
    }

    public Map<String, Car> getCarmap() {
        return carmap;
    }

    public void setCarmap(Map<String, Car> carmap) {
        this.carmap = carmap;
    }

    public Address getAddress() {
        return address;
    }

    public void setAddress(Address address) {
        this.address = address;
    }

    public double getAccount() {
        return account;
    }

    public void setAccount(double account) {
        this.account = account;
    }

    public boolean isGender() {
        return gender;
    }

    public void setGender(boolean gender) {
        this.gender = gender;
    }

    @Override
    public String toString() {
        return "Person{" +
                "name='" + name + '\'' +
                ", age=" + age +
                ", account=" + account +
                ", gender=" + gender +
                ", car=" + car +
                ", cars=" + cars +
                ", carmap=" + carmap +
                ", address=" + address +
                '}';
    }
}
