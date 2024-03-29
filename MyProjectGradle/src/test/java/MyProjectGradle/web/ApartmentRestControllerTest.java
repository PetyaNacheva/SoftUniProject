package MyProjectGradle.web;

import MyProjectGradle.service.ApartmentService;
import MyProjectGradle.service.UserService;
import org.junit.jupiter.api.Test;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class ApartmentRestControllerTest {
    @Autowired
    MockMvc mockMvc;
    @MockBean
    ApartmentService apartmentService;
    @Autowired
    ModelMapper modelMapper;
    @MockBean
    UserService userService;

    @Test
    @WithMockUser
    public void  getAll() throws Exception {
        mockMvc.perform(get("/apartments/api/all")).
                andExpect(status().isOk());
    }

}