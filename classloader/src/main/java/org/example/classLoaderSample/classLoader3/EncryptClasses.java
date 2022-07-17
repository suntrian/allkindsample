package org.example.classLoaderSample.classLoader3;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESKeySpec;
import java.security.SecureRandom;

public class EncryptClasses {

    static public void main(String args[]) throws Exception {
        String keyFilename;
        String algorithm = "DES";
        if (args==null || args.length == 0){
            keyFilename = "C:\\Users\\suntri\\Projects\\allkinksample\\out\\production\\classes\\com\\suntr\\classLoaderSample" +
                    "\\classLoader3\\desKey";
            args = new String[2];
            args[1] = "C:\\Users\\suntri\\Projects\\allkinksample\\out\\production\\classes\\com\\suntr" +
                    "\\classLoaderSample\\classLoader3\\ClassLoaderTest.class";
        } else {
            keyFilename = args[0];
        }

        // 生成密匙
        SecureRandom sr = new SecureRandom();
        byte rawKey[] = Util.readFile(keyFilename);
        DESKeySpec dks = new DESKeySpec(rawKey);
        SecretKeyFactory keyFactory = SecretKeyFactory.getInstance( algorithm );
        SecretKey key = keyFactory.generateSecret(dks);

        // 创建用于实际加密操作的Cipher对象
        Cipher ecipher = Cipher.getInstance(algorithm);
        ecipher.init(Cipher.ENCRYPT_MODE, key, sr);

        // 加密命令行中指定的每一个类
        for (int i=1; i<args.length; ++i) {
            String filename = args[i];
            byte classData[] = Util.readFile(filename);  //读入类文件
            byte encryptedClassData[] = ecipher.doFinal(classData);  //加密
            Util.writeFile(filename, encryptedClassData);  // 保存加密后的内容
            System.out.println("Encrypted "+filename);
        }
    }

}
