package org.example.classLoaderSample.classLoader3;

import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import java.security.SecureRandom;

public class GenerateKey {


    static public void main(String args[]) throws Exception {
        String keyFilename;
        if (args==null || args.length == 0){
            keyFilename = "C:\\Users\\suntri\\Projects\\allkinksample\\out\\production\\classes\\com\\suntr\\classLoaderSample" +
                    "\\classLoader3\\desKey";
        } else {
            keyFilename = args[0];
        }
        String algorithm = "DES";

        // 生成密匙
        SecureRandom sr = new SecureRandom();
        KeyGenerator kg = KeyGenerator.getInstance(algorithm);
        kg.init(sr);
        SecretKey key = kg.generateKey();

        // 把密匙数据保存到文件
        Util.writeFile(keyFilename, key.getEncoded());
    }

}
