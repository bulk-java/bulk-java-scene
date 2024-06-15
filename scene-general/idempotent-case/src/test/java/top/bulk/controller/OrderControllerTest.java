package top.bulk.controller;

import lombok.SneakyThrows;
import org.junit.jupiter.api.RepeatedTest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * @author 散装java
 * @date 2024-06-14
 */
// @ExtendWith(SpringExtension.class)
// @WebMvcTest(OrderController.class)

@SpringBootTest
@AutoConfigureMockMvc
class OrderControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @SneakyThrows
    @Test
    void testGet() {
        mockMvc.perform(MockMvcRequestBuilders.get("/get?key=1")).andExpect(status().isOk()).andReturn();
    }

    @Test
    void noKey() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/noKey?id=11233")).andExpect(status().isOk()).andReturn();
    }

    @Test
    void order() throws Exception {
        String str = "{\"id\":null,\"userId\":123,\"productId\":456}";
        MockHttpServletRequestBuilder builder = MockMvcRequestBuilders.post("/order").contentType(MediaType.APPLICATION_JSON).content(str);
        mockMvc.perform(builder).andExpect(status().isOk()).andReturn();
    }

    // 执行10 次
    @RepeatedTest(10)
    void orderRepeatedly() throws Exception {
        System.out.println(Thread.currentThread().getName());
        String str = "{\"id\":null,\"userId\":123,\"productId\":456}";
        MockHttpServletRequestBuilder builder = MockMvcRequestBuilders.post("/order").contentType(MediaType.APPLICATION_JSON).content(str);
        mockMvc.perform(builder).andExpect(status().isOk()).andReturn();
    }

    @Test
    void orderWithToken() throws Exception {
        // 先获取 token
        String token = mockMvc.perform(MockMvcRequestBuilders.get("/getToken")).andReturn().getResponse().getContentAsString();

        // 在请求下单接口
        String str = "{\"id\":null,\"userId\":123,\"productId\":456}";
        MockHttpServletRequestBuilder builder = MockMvcRequestBuilders.post("/orderWithToken")
                // 请求头中添加token
                .header("token", token).contentType(MediaType.APPLICATION_JSON).content(str);
        mockMvc.perform(builder).andExpect(status().isOk()).andReturn();

        // 再次调用 这时候会 抛出 "请勿重复下单"，因为 token 已经用过了
        // mockMvc.perform(builder).andExpect(status().isOk()).andReturn();

    }
}