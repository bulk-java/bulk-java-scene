package top.bulk.controller;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpHeaders;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static org.junit.jupiter.api.Assertions.*;

/**
 * @author 散装java
 * @date 2024-06-13
 */
@SpringBootTest
@AutoConfigureMockMvc
class TestControllerTest {
    /**
     * 配合 @AutoConfigureMockMvc 调用后端 API 接口
     */
    @Autowired
    private MockMvc mvc;

    @Test
    void get() throws Exception {
        MockHttpServletRequestBuilder builder = MockMvcRequestBuilders.get("/get");
        ResultActions resultActions = mvc.perform(builder);
    }
}