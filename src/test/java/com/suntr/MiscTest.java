package com.suntr;

import org.apache.commons.lang3.ArrayUtils;
import org.junit.Test;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.lang.reflect.Field;
import java.util.Date;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

public class MiscTest {

    @Test
    public void testSwapInteger() {
        Integer a = 5;
        Integer b = 8;
        swap(a, b);
        System.out.println("a:"+a + ", b:" + b);
        swap2(a, b);
        System.out.println("a:"+a + ", b:" + b);

        Integer c = 128;
        Integer d = -129;
        swap(c, d);
        System.out.println("c:"+c + ", d:" + d);
        swap2(c,d);
        System.out.println("c:"+c + ", d:" + d);


        int e = 5;
        int f = 7;
        swap(e,f);
        System.out.println("e:" + e + ",f:" +f );
        swap2(e,f);
        System.out.println("e:" + e + ",f:" +f );

        String g = "GGGGGG";
        String h = "HHHHHH";
        swap(g,h);
        System.out.println("g:" + g + ", h:" + h);
        swap2(g,h);
        System.out.println("g:" + g + ", h:" + h);

    }

    /**
     * 对象按值传递，无法实际交换两个对象的傎
     * @param a
     * @param b
     * @param <T>
     */
    private <T> void swap(T a, T b) {
        T temp = b;
        b = a;
        a = temp;
    }

    @SuppressWarnings("unchecked")
    private <T> void swap2(T a, T b) {
        if (a == null || b == null) return;
        Class<T> clazz = (Class<T>) a.getClass();
        try {
            if (clazz.isPrimitive()) {      //基本数据类型

            } else if (clazz.isAssignableFrom(Integer.class)) {
                Field value = clazz.getDeclaredField("value");
                value.setAccessible(true);
                Object temp =  value.get(a);
                value.set(a, b);
                value.set(b, temp);
            } else if (clazz.isAssignableFrom(String.class)) {
                Field value = clazz.getDeclaredField("value");
                value.setAccessible(true);
                char[] temp = (char[]) value.get(a);
                value.set(a, value.get(b));
                value.set(b, temp);
            }

        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
    }

    public void swap(AtomicInteger a, AtomicInteger b){
        // look mom, no tmp variables needed
        a.set(b.getAndSet(a.get()));
    }



    public void swap(int a , int b){
        a = a ^ b;
        b = a ^ b;
        a = a ^ b;
    }

    public  void swap2(int a, int b) {
        a += (b - (b = a));
    }

    @Test
    public void testArrayType() {
        Object[] a = new Object[5];
        Object[] b = new Object[7];
        System.out.println(a.getClass().isArray());
        if (a.getClass().equals(b.getClass())) {
            System.out.println(a.getClass().getCanonicalName());
            System.out.println(a.getClass().getSuperclass().getCanonicalName());
        }

        int c = 6;
        char d = '7';
    }

    @Test
    public void getCurrentMethodName() {
        String threadName = Thread.currentThread().getName();
        String methodName = Thread.currentThread().getStackTrace()[1].getMethodName();
        System.out.println(threadName);
        System.out.println(methodName);
    }


    /**
     * 屏幕截图
     * @throws AWTException
     * @throws IOException
     */
    @Test
    public void captureCurrentScreen() throws AWTException, IOException {
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        Rectangle rectangle = new Rectangle(screenSize);
        Robot robot = new Robot();
        BufferedImage image = robot.createScreenCapture(rectangle);
        ImageIO.write(image, "png", new File("D:\\temp\\nn.png"));
    }

    @Test
    public void convertArray2Map() {
        String[][] countries = { { "United States", "New York" }, { "United Kingdom", "London" },
                { "Netherland", "Amsterdam" }, { "Japan", "Tokyo" }, { "France", "Paris" } };
        Map countryCapitals = ArrayUtils.toMap(countries);
        System.out.println("Capital of Japan is " + countryCapitals.get("Japan"));
        System.out.println("Capital of France is " + countryCapitals.get("France"));
    }

}
