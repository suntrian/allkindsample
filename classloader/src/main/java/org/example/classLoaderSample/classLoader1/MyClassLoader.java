package org.example.classLoaderSample.classLoader1;

import java.io.*;

/**
 * reference
 * https://blog.csdn.net/u014782692/article/details/50620659
 */
public class MyClassLoader extends ClassLoader {

    public static void main(String[] args) throws IOException {
        //未加密class文件路径
        String srcPath = args[0];
        //加密class文件保存路径
        String destDir = args[1];

        FileInputStream fis = new FileInputStream(srcPath);
        String destFileName = srcPath.substring(srcPath.lastIndexOf('\\') + 1);
        String destPath = destDir + "\\" + destFileName;
        FileOutputStream fos = new FileOutputStream(destPath);
        cypher(fis, fos);
        fis.close();
        fos.close();

    }

    private static void cypher(InputStream ips, OutputStream ops) throws IOException {
        int b= -1;
        while ((b=ips.read())!=-1){
            ops.write(b^0xff);
        }
    }

    private String classDir;

    @Override
    protected Class<?> findClass(String name) throws ClassNotFoundException {
        String classFileName = classDir + "\\" + name.substring(name.lastIndexOf('.') + 1)+".class";
        try {
            FileInputStream fis = new FileInputStream(classFileName);
            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            cypher(fis, bos);
            byte[] bytes = bos.toByteArray();
            return defineClass(null, bytes, 0, bytes.length);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public MyClassLoader(){}

    public MyClassLoader(String classDir) {
        this.classDir = classDir;
    }
}
