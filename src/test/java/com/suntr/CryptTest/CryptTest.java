package com.suntr.CryptTest;

import com.suntr.CryptSample.CryptoUtil;
import org.junit.Test;

import java.io.File;

public class CryptTest {



    @Test
    public void testMd5File() {
        File file = new File("D:\\Sublime Text3 Stable Build 3170 x64 Chs.7z");
        System.out.println(System.nanoTime());
        System.out.println(CryptoUtil.MD5(file));
        System.out.println(System.nanoTime());
    }

    @Test
    public void testMd5String() {
        System.out.println(System.nanoTime());
        System.out.println(CryptoUtil.MD5("我是一个鬼"));
        System.out.println(System.nanoTime());
    }
}


