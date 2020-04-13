package com.demo;

import javax.servlet.ServletContext;

import org.junit.Before;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import com.oneiron.login.controller.LoginController;

@SpringBootTest
@RunWith(MockitoJUnitRunner.class)
class OneIronApplicationTests {

	@InjectMocks
	LoginController loginController;
	
	private MockMvc mockMvc;
	
	@Test
	void contextLoads() {
	}
	
	@Before
	public void setUp() throws Exception{
		mockMvc = MockMvcBuilders.standaloneSetup(loginController).build();
	}
//	
//	@Test
//	public void myTest() throws Exception {
//		mockMvc.perform(MockMvcRequestBuilders.get("/login")) ;
//			
//	}
	
}
