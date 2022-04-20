package com.epam.esm.task.controller;

import com.epam.esm.task.config.MyWebApplicationInitializer;
import com.epam.esm.task.config.SpringConfig;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import javax.servlet.ServletContext;

@ExtendWith(MockitoExtension.class)
@WebAppConfiguration
@ContextConfiguration(classes = {MyWebApplicationInitializer.class, SpringConfig.class})
class ExceptionControllerTest {

    private static MockMvc mockMvc;

    @InjectMocks
    private static ExceptionController exceptionController;
//    @Mock
//    private GiftCertificateService giftCertificateService;
//    @Mock
//    private TagService tagService;


    @BeforeAll
    public static void setup() {
        mockMvc = MockMvcBuilders.standaloneSetup(exceptionController)
                .setControllerAdvice(new ExceptionController())
                .build();
    }


    @Test
    void handleConstraintViolationException() {
    }

    @Test
    void handleDaoExceptions() {
    }

    @Test
    void handleBadRequestExceptions() {
    }

    @Test
    void handleBadRequestException() {
    }

    @Test
    void methodNotAllowedExceptionException() {
    }

    @Test
    void handleBadMediaTypeException() {
    }
}