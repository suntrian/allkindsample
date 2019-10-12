package com.suntr.guava;

import com.google.common.primitives.UnsignedLong;

import java.math.BigInteger;

public class Data {

  public static void main(String[] args) {
    System.out.println(UnsignedLong.MAX_VALUE);
    System.out.println(Long.MAX_VALUE);
    System.out.println( UnsignedLong.fromLongBits(Long.MAX_VALUE).plus(UnsignedLong.ONE));
    System.out.println(UnsignedLong.valueOf(BigInteger.valueOf(Long.MAX_VALUE)));
    System.out.println(UnsignedLong.valueOf(new BigInteger("9223372036854775808")));
    System.out.println(new BigInteger("19223372036854775808"));
  }
}
