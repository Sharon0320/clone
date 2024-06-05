package com.gachon.ReAction_bank_server;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.gachon.ReAction_bank_server.controller.AccountController;
import com.gachon.ReAction_bank_server.controller.StatementController;
import com.gachon.ReAction_bank_server.controller.UserController;
import com.gachon.ReAction_bank_server.service.AccountService;
import com.gachon.ReAction_bank_server.service.StatementService;
import com.gachon.ReAction_bank_server.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

@ActiveProfiles("test")
@WebMvcTest(controllers = {
        UserController.class,
        AccountController.class,
        StatementController.class
})
public abstract class ControllerTestSupport {

    @Autowired
    protected MockMvc mockMvc;

    @Autowired
    protected ObjectMapper objectMapper;

    @MockBean
    protected UserService userService;

    @MockBean
    protected AccountService accountService;

    @MockBean
    protected StatementService statementService;
}
