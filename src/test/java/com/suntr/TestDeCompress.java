package com.suntr;

import com.suntr.compressSample.DeCompressUtil;
import org.junit.Test;

import java.io.IOException;

public class TestDeCompress {

    @Test
    public void testDecompress() throws IOException {
        String filePath = getClass().getResource("/archive/ab.rar").getPath();
        DeCompressUtil.deCompress(filePath);
    }

}
