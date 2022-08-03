package MyProjectGradle.web;

import MyProjectGradle.models.entities.Picture;
import MyProjectGradle.models.entities.Role;
import MyProjectGradle.models.entities.Town;
import MyProjectGradle.models.entities.UserEntity;
import MyProjectGradle.models.enums.RolesEnum;
import MyProjectGradle.repository.PictureRepository;
import MyProjectGradle.repository.RoleRepository;
import MyProjectGradle.repository.TownRepository;
import MyProjectGradle.repository.UserRepository;
import MyProjectGradle.service.CloudinaryService;
import MyProjectGradle.service.TownService;
import MyProjectGradle.service.UserService;
import MyProjectGradle.service.impl.PictureServiceImpl;
import MyProjectGradle.service.impl.TownServiceImpl;
import MyProjectGradle.service.impl.UserServiceImpl;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.crypto.password.Pbkdf2PasswordEncoder;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;


import java.util.List;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc

class TownControllerTest {
    private UserEntity testUser;
    private Role userRole, adminRole;
    private Town testTown;
    private Picture testPicture;

    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private PasswordEncoder passwordEncoder;
    @MockBean
    private RoleRepository mockRoleRepository;

    @MockBean
    private UserServiceImpl userService;
    @MockBean
    private TownServiceImpl mockTownService;
    @MockBean
    private UserRepository mockUserRepository;
    @MockBean
    private PictureServiceImpl pictureService;
    @MockBean
    private PictureRepository pictureRepository;

    @MockBean
    private TownRepository  mockTownRepository;
    @MockBean
    private CloudinaryService cloudinaryService;

    @BeforeEach
    void setUp() {
        mockUserRepository.deleteAll();
        mockRoleRepository.deleteAll();
        mockTownRepository.deleteAll();
        passwordEncoder = new Pbkdf2PasswordEncoder();
        testUser = new UserEntity();
        adminRole = new Role();
        adminRole.setName(RolesEnum.ADMIN);
        testUser.setRole(List.of( adminRole));
        testUser.setUsername("testUser");
        testUser.setFirstName("test");
        testUser.setLastName("test");
        testUser.setPassword("test");
        testUser.setEmail("test@test.com");
        testUser.setPhone("+3598935467");

        testPicture=new Picture();
        testPicture.setTitle("test");
        testPicture.setUrl("testUrl");
        testPicture.setUserName("test");
        testPicture.setPublicId("publicId");
        pictureRepository.save(testPicture);

        mockUserRepository.save(testUser);

        testTown = new Town();

        testTown.setName("Sofia");
        testTown.setDescription("Sofia is the capital of Bulgaria");
        mockTownRepository.save(testTown);
        testTown.setId(1L);
    }

    @AfterEach
    void tearDown() {
        mockUserRepository.deleteAll();
        mockRoleRepository.deleteAll();
        mockTownRepository.deleteAll();
    }

    @Test
    @WithMockUser(authorities="ROLE_ADMIN")
    void testAddTown() throws Exception {
        mockMvc.perform(get("/towns/add").secure(true)).
                andExpect(status().isOk()).
                andExpect(view().name("town-add"));
    }


    @Test
    @WithMockUser(authorities="ROLE_ADMIN")
    void testAllTowns() throws Exception {
        mockMvc.perform(get("/towns/all")).
                andExpect(status().isOk()).
                andExpect(view().name("allTowns"));
    }

  /* @Test
    @WithMockUser(authorities="ROLE_ADMIN")
    void testUpdate() throws Exception {
        mockMvc.perform(get("/towns/{id}/update", 1).with(csrf())).
                andExpect(status().isOk()).
                andExpect(view().name("town-update"));
    }


    @Test
    @WithMockUser(authorities="ROLE_ADMIN")
    void testDetails() throws Exception {
        mockMvc.perform(get("/towns/"+1+"/details").with(csrf())).
                andExpect(status().isOk()).
                andExpect(view().name("town-details"));
    }*/

    @Test
    @WithMockUser(authorities="ROLE_ADMIN")
    void testAddTownCorrect() throws Exception {
        MockMultipartFile multipartFile = new MockMultipartFile("file", "test.txt",
                "text/plain", "Spring Framework".getBytes());
        mockMvc
                .perform(post("/towns/add")
                        .param("name", "Varna")
                        .param("description", "any text")
                        .param("picture", multipartFile.getName())
                        .with(csrf())
                )
                .andExpect(status().is3xxRedirection())
                .andExpect(view().name("redirect:add"));
    }

    @Test
    void testAddTownNotAuthorized() throws Exception {

        mockMvc
                .perform(post("/towns/add"))
                        .andExpect(status().is4xxClientError());
    }


   @Test
    void testEditTownNotAuthorized() throws Exception {

        mockMvc
                .perform(patch("/towns/"+testTown.getId()+"/update" ))
                .andExpect(status().is4xxClientError());

    }


    @Test
    void test_GetTownDetailsInvalidId_Throws() throws Exception {
        mockMvc
                .perform(get("/towns/"+100+"/details"))
                .andExpect(status().is3xxRedirection());
    }


}