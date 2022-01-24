package com.slub.chamchimayo.controller;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.restdocs.RestDocumentationExtension;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith({RestDocumentationExtension.class, SpringExtension.class})
@WebMvcTest(LoginController.class)
@AutoConfigureRestDocs
public class HelloWorldControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @Test
    void helloRestdoc() throws Exception {
        mockMvc.perform(get("/hello"))
                .andExpect(status().isOk())
                .andDo(document("helloRestdoc"));
    }
}
