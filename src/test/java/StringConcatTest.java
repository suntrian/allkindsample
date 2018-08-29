import junit.framework.TestCase;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;


/**
 * Unit test for simple App.
 */
public class StringConcatTest extends TestCase {
    private final Logger logger = LoggerFactory.getLogger(this.getClass());
    private int TIMES = 10000;

    public void testPlus() {
        String s = "";
        long ts = System.currentTimeMillis();
        for (int i = 0; i < TIMES; i++) {
            s = s + String.valueOf(i);
        }
        long te = System.currentTimeMillis();
        logger.info("+ cost {} ms", te - ts);
    }

    public void testConcat() {
        String s = "";
        long ts = System.currentTimeMillis();
        for (int i = 0; i < TIMES; i++) {
            s = s.concat(String.valueOf(i));
        }
        long te = System.currentTimeMillis();
        logger.info("concat cost {} ms", te - ts);
    }

    public void testJoin() {
        List<String> list = new ArrayList<String>();
        long ts = System.currentTimeMillis();
        for (int i = 0; i < TIMES; i++) {
            list.add(String.valueOf(i));
        }
        StringUtils.join(list, "");
        long te = System.currentTimeMillis();
        logger.info("StringUtils.join cost {} ms", te - ts);
    }

    public void testStringBuffer() {
        StringBuffer sb = new StringBuffer();
        long ts = System.currentTimeMillis();
        for (int i = 0; i < TIMES; i++) {
            sb.append(String.valueOf(i));
        }
        sb.toString();
        long te = System.currentTimeMillis();
        logger.info("StringBuffer cost {} ms", te - ts);
    }

    public void testStringBuilder() {
        StringBuilder sb = new StringBuilder();
        long ts = System.currentTimeMillis();
        for (int i = 0; i < TIMES; i++) {
            sb.append(String.valueOf(i));
        }
        sb.toString();
        long te = System.currentTimeMillis();
        logger.info("StringBuilder cost {} ms", te - ts);
    }

    public void testStringFormat() {
        String str="";
        long ts = System.currentTimeMillis();
        for (int i = 0; i < TIMES; i++) {

            str=String.format("%s%s",str,String.valueOf(i));

        }
        str.toString();
        long te = System.currentTimeMillis();
        logger.info("StringFormat cost {} ms", te - ts);
    }
}