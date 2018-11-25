package com.suntr.webfluxSample.entity;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Data
public class User {
  @Setter @Getter
  private String id;
  private String name;
  private String email;
}
