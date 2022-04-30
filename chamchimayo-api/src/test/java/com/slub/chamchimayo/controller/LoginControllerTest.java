package com.slub.chamchimayo.controller;

import com.slub.chamchimayo.CommonApiTest;
import com.slub.chamchimayo.repository.UserRefreshTokenRepository;
import com.slub.chamchimayo.repository.UserRepository;
import com.slub.chamchimayo.service.UserService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.restdocs.RestDocumentationExtension;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

//@ExtendWith({RestDocumentationExtension.class, SpringExtension.class})
//@WebMvcTest(LoginController.class)
//@AutoConfigureRestDocs
public class LoginControllerTest extends CommonApiTest{
//    @Autowired
//    private MockMvc mockMvc;
//
//    @MockBean
//    UserRepository userRepository;
//
//    @MockBean
//    UserRefreshTokenRepository userRefreshTokenRepository;
//
//    @MockBean
//    UserService userService;
//
//    @Test
//    void helloRestdoc() throws Exception {
//        mockMvc.perform(get("/hello"))
//            .andExpect(status().isOk())
//            .andDo(print());
//    }
}