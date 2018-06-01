package com.suntr.AnnotationSample;

import com.suntr.AnnotationSample.annotation.Column;
import com.suntr.AnnotationSample.annotation.Table;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

public class Main {
    public static void main(String[] args) {

        Filter f1 = new Filter();
        f1.setId(10);

        Filter f2 = new Filter();
        f2.setUsername("lucy");

        Filter f3 = new Filter();
        f3.setEmail("liu@sina.com,zh@163.com,1235@qq.com");


        Filter f4 = new Filter();
        f4.setEmail("liu@sina.com");

        Filter f5 = new Filter();
        f5.setUsername("Tom");
        f5.setAge(20);

        String sq1 = query(f1);
        String sq2 = query(f2);
        String sq3 = query(f3);
        String sq4 = query(f4);
        String sq5 = query(f5);
        System.out.println(sq1);
        System.out.println(sq2);
        System.out.println(sq3);
        System.out.println(sq4);
        System.out.println(sq5);

    }

    public static String query(Filter filter) {

        StringBuilder sb = new StringBuilder();
        //1.获取到Class
        Class c = filter.getClass();
        //获取到Table的名字
        boolean exist = c.isAnnotationPresent(Table.class);
        if (!exist) {
            return null;
        }

        Table table = (Table) c.getAnnotation(Table.class);
        String tbName = table.value();
        sb.append("SELECT * FROM ").append(tbName).append(" WHERE 1=1 ");
        //遍历所有字段
        Field[] fArray = c.getDeclaredFields();
        for (Field field : fArray) {
            boolean fExists = field.isAnnotationPresent(Column.class);
            if (!fExists) {
                continue;
            }

            Column column = field.getAnnotation(Column.class);
            String columnName = column.value();
            //拿到字段的值
            String fieldName = field.getName();
            Object fieldValue = null;

            String getMethodName = "get" + fieldName.substring(0, 1).toUpperCase() + fieldName.substring(1);
            try {
                Method getMethod = c.getMethod(getMethodName);
                fieldValue = getMethod.invoke(filter, null);
            } catch (Exception e) {
                e.printStackTrace();
            }

            //拼装SQL

            if (fieldValue == null || (fieldValue instanceof Integer && (Integer) fieldValue == 0)) {
                continue;
            }
            sb.append(" AND ").append(fieldName);
            if (fieldValue instanceof String) {
                if (((String) fieldValue).contains(",")) {
                    String[] values = ((String) fieldValue).split(",");
                    sb.append(" IN (");
                    for (String v : values) {
                        sb.append("\'").append(v).append("\'").append(",");
                    }
                    sb.deleteCharAt(sb.length() - 1);
                    sb.append(")");
                } else {
                    fieldValue = "\'" + fieldValue + "\'";
                    sb.append(" = ").append(fieldValue);
                }

            }else{
                sb.append(" = ").append(fieldValue);
            }

        }
        return sb.toString();
    }
}
