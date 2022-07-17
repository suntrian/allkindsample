package org.example.springSample.model;

import org.springframework.stereotype.Component;

@Component("house")
public class House {

    public void live(){
        System.out.println("house is used to live");
    }
}
