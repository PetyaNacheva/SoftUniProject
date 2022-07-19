package MyProjectGradle.web;

import MyProjectGradle.repository.UserRepository;
import MyProjectGradle.models.entities.Role;
import MyProjectGradle.models.entities.UserEntity;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

@SpringBootTest
@AutoConfigureMockMvc
@AutoConfigureTestDatabase

public class HomeControllerTest {

    @Autowired
    MockMvc mockMvc;

    private UserEntity testUser;
    private Role adminRole, userRole;

    @Autowired
    private UserRepository userRepository;


   /* @BeforeEach
    void setup(){
        testUser = new UserEntity();
        userRole = new Role();
        userRole.setName(RolesEnum.USER);
        testUser.setRole(List.of(userRole));
        testUser.setUsername("testUser");
        testUser.setFirstName("test");
        testUser.setLastName("test");
        testUser.setPassword("test");
        testUser.setEmail("test@test.com");
        testUser.setPhone("+3598935467");
        userRepository.save(testUser);
    }

    @AfterEach
    void tearDown(){
        userRepository.deleteAll();
    }*/

    @Test
    @WithMockUser(username = "admin@admin.bg")
    public void testGetHomeLogged() throws Exception {
        mockMvc.perform(get("/")).
                andExpect(status().isOk()).
                andExpect(view().name("/index"));
    }

    @Test
    public void testGetHomeNotLogged() throws Exception {
        mockMvc.perform(get("/")).
                andExpect(status().isOk()).
                andExpect(view().name("/index"));
    }
}
