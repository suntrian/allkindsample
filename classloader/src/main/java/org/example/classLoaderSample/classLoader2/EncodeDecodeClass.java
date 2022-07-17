package org.example.classLoaderSample.classLoader2;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;

public class EncodeDecodeClass {

    public static void encode(File file)throws Exception
    {
        FileInputStream fis = new FileInputStream(file);

        String name = file.getName();
        file.getPath();
        String[] strs = name.split("\\.");

        System.out.println(file.toString()+" :: "+name+" :: "+strs.length);
        name = strs[0]+"_1."+strs[1];
        file.renameTo(new File(file.getParent(),file.getName()+"_1"));

        File desFile = new File(file.getParent(),name);

        FileOutputStream fos = new FileOutputStream(desFile);
        byte[] buf = new byte[1024];
        int len = 0;
        while( (len = fis.read(buf))!=-1)
        {
            for(int i=0; i<len ;i++)
            {
                buf[i] = (byte)(buf[i]^0xff);//取反加密
            }
            fos.write(buf,0,len);
        }

        fis.close();
        fos.close();

    }
    public static byte[] decode(File file) throws Exception
    {
        FileInputStream fis = new FileInputStream(file);
        ByteArrayOutputStream dest = new ByteArrayOutputStream();

        byte[] buf = new byte[1024];
        int len = 0;
        while((len = fis.read(buf))!=-1)
        {
            for(int i=0; i<len ;i++)
            {
                buf[i] = (byte)(buf[i]^0xff); //取反解密
            }
            dest.write(buf, 0, len);
        }
        fis.close();
        System.out.println("ok");
        return dest.toByteArray();
    }
    public static void main(String[] args)
    {
        File file = new File("C:\\Users\\suntri\\Projects\\allkinksample\\out\\production\\classes\\com\\suntr" +
                "\\classLoaderSample\\classLoader2\\ClassLoaderTest.class");
        if(file.exists())
        {
            try {
                EncodeDecodeClass.encode(file);
                System.out.println("加密成功！");
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

}
