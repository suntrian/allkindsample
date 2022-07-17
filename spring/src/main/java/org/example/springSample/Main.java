package org.example.springSample;

import com.suntr.springSample.model.Car;
import com.suntr.springSample.model.Person;
import org.example.springSample.aop.Arithmetic;
import org.example.springSample.model.FakeDatasource;
import org.example.springSample.model.House;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class Main {

    private HelloSpringWorld springWorld;

    public static void main(String[] args) {
        ApplicationContext context = new ClassPathXmlApplicationContext("com/suntr/springSample/beans.xml");
        HelloSpringWorld world  = (HelloSpringWorld) context.getBean("HelloSpringWorld");
        world.sayHello();

        Car car1 = (Car) context.getBean("car1");
        Car car2 = (Car) context.getBean("car2");
        Car car3 = (Car) context.getBean("car3");
        Car car4 = (Car) context.getBean("car4");
        Car car5 = (Car) context.getBean("car5");
        System.out.println(car1.toString());
        System.out.println(car2.toString());
        System.out.println(car3.toString());
        System.out.println(car4.toString());
        System.out.println(car5.toString());

        Person person1 = (Person) context.getBean("person1");
        Person person2 = (Person) context.getBean("person2");
        Person person3 = (Person) context.getBean("person3");
        Person person4 = (Person) context.getBean("person4");
        Person person5 = (Person) context.getBean("person5");
        Person person6 = (Person) context.getBean("person6");
        Person person7 = (Person) context.getBean("person7");
        Person person8 = (Person) context.getBean("person8");
        Person person9 = (Person) context.getBean("person9");
        Person person10 = (Person) context.getBean("person10");
        Person person11 = (Person) context.getBean("person11");
        System.out.println(person1);
        System.out.println(person2);
        System.out.println(person3);
        System.out.println(person4);
        System.out.println(person5);
        System.out.println(person6);
        System.out.println(person7);
        System.out.println(person8);
        System.out.println(person9);
        System.out.println(person10);
        System.out.println(person11);

        FakeDatasource datasource= (FakeDatasource) context.getBean("fakeDatasource");
        System.out.println(datasource);

        House house = (House) context.getBean("house");
        house.live();
        ((ClassPathXmlApplicationContext) context).close();

        context = new ClassPathXmlApplicationContext("com/suntr/springSample/spring-aspect.xml");
        Arithmetic arithmetic = (Arithmetic) context.getBean("arithmetic");
        System.out.println(arithmetic.add(3, 7));
        arithmetic.div(10,2);
    }
}
