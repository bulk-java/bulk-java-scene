package top.bulk.util;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

/**
 * @author 散装java
 * @date 2024-06-13
 */
@SpringBootTest
class IpAddressUtilTest {

    @Test
    void getIp() {
    }

    @Test
    void getCityInfo() {
        String info = IpAddressUtil.getCityInfo("127.0.0.1");
        System.out.println(info);
    }
}