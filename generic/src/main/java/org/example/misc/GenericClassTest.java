package org.example.misc;

import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.List;
import java.util.Map;
public class GenericClassTest {

    public static void main(String[] args) {
        Class tc = T.class;
        Field[] fields = tc.getDeclaredFields();
        for (Field f : fields) {
            Class fc = f.getType();
            if(fc.isPrimitive()){
                System.out.println("基本数据类型： " + f.getName() + "  " + fc.getName());
            }else{
                if(fc.isAssignableFrom(List.class)){ //判断是否为List
                    System.out.println("List类型：" + f.getName());
                    Type gt = f.getGenericType();	//得到泛型类型
                    ParameterizedType pt = (ParameterizedType)gt;
                    Class lll = (Class)pt.getActualTypeArguments()[0];
                    System.out.println("\t\t" + lll.getName());
                }
            }
        }
    }

}




class T{
    List<A>  a;
    List<B>  b;
    //	List l ;
    Map<Integer, String> map ;
    int c;
}

class A {}
class B{}