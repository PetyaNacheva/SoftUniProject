package MyProjectGradle.web;

import MyProjectGradle.models.entities.Role;
import MyProjectGradle.models.entities.UserEntity;
import MyProjectGradle.models.enums.RolesEnum;
import MyProjectGradle.repository.RoleRepository;
import MyProjectGradle.repository.UserRepository;
import MyProjectGradle.service.UserService;
import MyProjectGradle.service.impl.UserServiceImpl;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;

import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.crypto.password.Pbkdf2PasswordEncoder;

import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.*;

import java.util.List;


import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc

class UserControllerTest {

    private UserEntity testUser;
    private Role userRole;

    @Autowired
    private UserServiceImpl userService;

   @Autowired
    MockMvc mockMvc;
    @MockBean
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
        testUser.setId(1L);
        mockUserRepository.save(testUser);
    }

    @AfterEach
    void tearDown() {
        mockUserRepository.deleteAll();
        mockRoleRepository.deleteAll();
    }

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
    @WithMockUser()
    void testProfileUpdateReturnStatusOK() throws Exception{
        mockMvc.perform(get("/users/profile/update").with(csrf()))
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


   @Test
    void test_PostLoginCorrectCredentials_LoginUserCorrectly() throws Exception {
       PasswordEncoder passwordEncoder = new Pbkdf2PasswordEncoder();
        mockMvc
                .perform(post("/users/login")
                        .param("username", "testUser")
                        .param("password", passwordEncoder.encode("test"))
                        .secure(true).with(csrf())
                )
                .andExpect(status().isOk());
    }

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
       }

   /* @Test
    @WithMockUser
    void testProfilePageLoadsCorrectly() throws Exception {
        mockMvc.perform(get("/users/profile").with(csrf())).
                andExpect(status().isOk()).
                andExpect(view().name("my-profile"));

     /* mockMvc
                .perform(get("/users/profile").with(csrf()).secure(true))
                .andExpect(status().isOk()).
                andExpect(view().name("my-profile"));
    }*/



}