package top.bulk.controller;

import lombok.SneakyThrows;
import org.junit.jupiter.api.RepeatedTest;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.parallel.Execution;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import top.bulk.entity.OrderEntity;

import static org.junit.jupiter.api.parallel.ExecutionMode.CONCURRENT;
import static org.junit.jupiter.api.parallel.ExecutionMode.SAME_THREAD;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * @author 散装java
 * @date 2024-06-14
 */
@SpringBootTest
@AutoConfigureMockMvc
class OrderControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @SneakyThrows
    @Test
    void testGet() {
        mockMvc.perform(MockMvcRequestBuilders.get("/get?key=1"))
                .andExpect(status().isOk()).andReturn();
    }

    @Test
    void noKey() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/noKey?id=11233"))
                .andExpect(status().isOk()).andReturn();
    }

    @Test
    void order() throws Exception {
        String str = "{\"id\":null,\"userId\":123,\"productId\":456}";
        MockHttpServletRequestBuilder builder = MockMvcRequestBuilders
                .post("/order")
                .contentType(MediaType.APPLICATION_JSON)
                .content(str);
        mockMvc.perform(builder).andExpect(status().isOk()).andReturn();
    }

    // 并发执行 在有些版本中并发有问题
    @Execution(CONCURRENT)
    // 执行10 次
    @RepeatedTest(10)
    void orderThread() throws Exception {
        String str = "{\"id\":null,\"userId\":123,\"productId\":456}";
        MockHttpServletRequestBuilder builder = MockMvcRequestBuilders
                .post("/order")
                .contentType(MediaType.APPLICATION_JSON)
                .content(str);
        mockMvc.perform(builder).andExpect(status().isOk()).andReturn();
    }
}