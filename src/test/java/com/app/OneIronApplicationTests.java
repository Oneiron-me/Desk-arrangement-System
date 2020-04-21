package com.app;

import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;

@SpringBootTest
@RunWith(MockitoJUnitRunner.class)
@TestPropertySource(locations="classpath:/security.properties")
class OneIronApplicationTests {

//	@InjectMocks
//	LoginController loginController;
	
//	private MockMvc mockMvc;
	
	@Test
	void contextLoads() {
	}
	
//	@Before
//	public void setUp() throws Exception{
//		mockMvc = MockMvcBuilders.standaloneSetup(loginController).build();
//	}
//	
//	@Test
//	public void myTest() throws Exception {
//		mockMvc.perform(MockMvcRequestBuilders.get("/login")) ;
//			
//	}
	
}
