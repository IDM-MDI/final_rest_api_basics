package com.epam.esm.task.controller;

import com.epam.esm.task.config.MyWebApplicationInitializer;
import com.epam.esm.task.config.SpringConfig;
import com.epam.esm.task.exception.DaoException;
import com.epam.esm.task.exception.ServiceException;
import com.epam.esm.task.service.impl.GiftCertificateService;
import com.epam.esm.task.service.impl.TagService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.HttpMediaTypeException;
import org.springframework.web.server.MethodNotAllowedException;
import org.springframework.web.servlet.NoHandlerFoundException;

import javax.validation.ConstraintViolationException;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
@WebAppConfiguration
@ContextConfiguration(classes = {MyWebApplicationInitializer.class,SpringConfig.class})
class ExceptionControllerTest {

    private MockMvc mockMvc;

    @InjectMocks
    private ExceptionController exceptionController;

    @Mock
    private GiftCertificateService giftCertificateService;
    @Mock
    private TagService tagService;


    @BeforeEach
    public void setup() {
        mockMvc = MockMvcBuilders.standaloneSetup(exceptionController)
                .build();
    }


    @Test
    void handleConstraintViolationException() throws Exception {

    }

    @Test
    void handleDaoExceptions() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.patch("/gifts", (Object) null)).
                andExpect(result -> assertTrue(result.getResolvedException() instanceof DaoException));
    }

    @Test
    void handleBadRequestExceptions() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.patch("/tags/0")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(result -> assertTrue(result.getResolvedException() instanceof ConstraintViolationException));
    }

    @Test
    void handleNotFoundException() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.patch("/test")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(result -> assertTrue(result.getResolvedException() instanceof NoHandlerFoundException));
    }

    @Test
    void methodNotAllowedExceptionException() throws Exception {
        long id = 10;
        mockMvc.perform(MockMvcRequestBuilders.patch("/tags/{id}", id)
                        .contextPath("/tags")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isMethodNotAllowed())
                .andExpect(result -> assertTrue(result.getResolvedException() instanceof MethodNotAllowedException));
              //  .andExpect(result -> assertEquals("resource not found", result.getResolvedException().getMessage()));
    }

    @Test
    void handleBadMediaTypeException() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.post("/tags")
                        .contentType(MediaType.APPLICATION_XML))
                .andExpect(status().isUnsupportedMediaType())
                .andExpect(result -> assertTrue(result.getResolvedException() instanceof HttpMediaTypeException));
    }
}