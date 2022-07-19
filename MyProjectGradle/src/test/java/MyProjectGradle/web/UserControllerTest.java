package MyProjectGradle.web;

import MyProjectGradle.models.entities.Role;
import MyProjectGradle.models.entities.UserEntity;
import MyProjectGradle.models.enums.RolesEnum;
import MyProjectGradle.repository.RoleRepository;
import MyProjectGradle.repository.UserRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;

import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.crypto.password.Pbkdf2PasswordEncoder;
import org.springframework.security.test.context.support.WithMockUser;

import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import org.springframework.web.context.WebApplicationContext;

import java.util.List;


import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.security.test.web.servlet.response.SecurityMockMvcResultMatchers.authenticated;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@AutoConfigureTestDatabase
class UserControllerTest {

    private UserEntity testUser;
    private Role userRole;


   @Autowired
    MockMvc mockMvc;
    @Autowired
    PasswordEncoder passwordEncoder;

    @Autowired
    RoleRepository mockRoleRepository;

    @Autowired
    UserRepository mockUserRepository;

    @BeforeEach
    void setUp() {
        mockUserRepository.deleteAll();
        mockRoleRepository.deleteAll();

        passwordEncoder = new Pbkdf2PasswordEncoder();
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
        mockUserRepository.save(testUser);
    }

   /* @AfterEach
    void tearDown() {
        mockUserRepository.deleteAll();
        mockRoleRepository.deleteAll();
    }
*/
    @Test
    void testLoginPageReturnValidStatusOK() throws Exception {
        mockMvc.perform(get("/users/login")).
                andExpect(status().isOk()).
                andExpect(view().name("login"));
    }

    @Test
    void testRegisterPageReturnValidStatusOK() throws Exception {
        mockMvc.perform(get("/users/register")).
                andExpect(status().isOk()).
                andExpect(view().name("register"));
    }


   /* @Test
    @WithMockUser(username = "testUser")
    void testProfileUpdateReturnStatusOK() throws Exception{
        mockMvc.perform(get("/users/profile/update"))
                .andExpect(status().isOk())
                .andExpect(view().name("my-profile-update"));
    }*/

    @Test
    void testPostLoginWrongCredentialsLoginError() throws Exception {
        mockMvc
                .perform(post("/users/login")
                        .param("username", "wrong_username")
                        .param("password", "12345")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                )
                .andExpect(forwardedUrl("/users/login-error"));
    }


    /*@Test
    void test_PostLoginCorrectCredentials_LoginUserCorrectly() throws Exception {
        PasswordEncoder passwordEncoder = new Pbkdf2PasswordEncoder();
       mockMvc
                .perform(post("/users/login")
                        .param("username", "testUser")
                        .param("password", passwordEncoder.encode("test"))
                        .with(csrf())
                )

                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/"));
    }*/

    @Test
    void testRegisterPageReturnRedirectStatus() throws Exception {
        mockMvc.perform(post("/users/register").
                        param("username", "test").
                        param("firstName", "Test").
                        param("lastName", "Test").
                        param("email", "test@gmail.com").
                        param("password", "123456").
                        param("confirmPassword", "1234567").
                        param("phone", "+35986785236").
                        with(csrf())).
                andExpect(status().is3xxRedirection()).
                andExpect(flash().attributeExists("userRegisterBindingModel")).
                andExpect(redirectedUrl("register"));
    }

    @Test
    public void testPostUserRegisterPage() throws Exception {
        mockMvc.perform(post("/users/register")
                        .param("username", "user")
                        .param("firstName", "user")
                        .param("lastName", "user")
                        .param("email", "user@gmail.com")
                        .param("password", "123456")
                        .param("confirmPassword", "123456")
                        .param("phone", "+35986785236")
                        .with(csrf()))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/"));
        assertEquals(1, mockUserRepository.count());
        /*Optional<UserEntity> user = mockUserRepository.findByUsername("user");
        assertTrue(user.isPresent());
        assertTrue(user.get().getId()>0);
        assertEquals("user@gmail.com", user.get().getEmail());
        mockUserRepository.delete(user.get());*/
       }

  /*  @Test
    @WithMockUser(username = "testUser")
    void testProfilePageLoadsCorrectly() throws Exception {
        mockMvc.perform(get("/users/profile")).
                andExpect(status().isOk()).
                andExpect(view().name("my-profile"));

    }
*/


}