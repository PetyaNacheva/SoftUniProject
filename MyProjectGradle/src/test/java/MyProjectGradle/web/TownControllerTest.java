package MyProjectGradle.web;

import MyProjectGradle.models.entities.Role;
import MyProjectGradle.models.entities.Town;
import MyProjectGradle.models.entities.UserEntity;
import MyProjectGradle.models.enums.RolesEnum;
import MyProjectGradle.repository.RoleRepository;
import MyProjectGradle.repository.TownRepository;
import MyProjectGradle.repository.UserRepository;
import MyProjectGradle.service.TownService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.crypto.password.Pbkdf2PasswordEncoder;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
class TownControllerTest {
    private UserEntity testUser;
    private Role userRole, adminRole;
    private Town testTown;

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private RoleRepository mockRoleRepository;

    @Autowired
    private TownService mockTownService;
    @Autowired
    private UserRepository mockUserRepository;

    @Autowired
    private TownRepository  mockTownRepository;

    @BeforeEach
    void setUp() {
        passwordEncoder = new Pbkdf2PasswordEncoder();
        testUser = new UserEntity();
        userRole = new Role();
        userRole.setName(RolesEnum.USER);
        adminRole = new Role();
        adminRole.setName(RolesEnum.ADMIN);
        testUser.setRole(List.of(userRole, adminRole));
        testUser.setUsername("testUser");
        testUser.setFirstName("test");
        testUser.setLastName("test");
        testUser.setPassword("test");
        testUser.setEmail("test@test.com");
        testUser.setPhone("+3598935467");


        mockUserRepository.save(testUser);

        testTown = new Town();
        testTown.setId(1L);
        testTown.setName("Sofia");
        testTown.setDescription("Sofia is the capital of Bulgaria");
        mockTownRepository.save(testTown);
    }

    @AfterEach
    void tearDown() {
        mockUserRepository.deleteAll();
        mockRoleRepository.deleteAll();
        mockTownRepository.deleteAll();
    }

    @Test
    @WithMockUser(value = "testUser", username = "testUser", roles = "ADMIN")
    void testAddTown() throws Exception {
        mockMvc.perform(get("/towns/add")).
                andExpect(status().isOk()).
                andExpect(view().name("town-add"));
    }


    @Test
    @WithMockUser(value = "testUser", username = "testUser", roles = "ADMIN")
    void testAllTowns() throws Exception {
        mockMvc.perform(get("/towns/all")).
                andExpect(status().isOk()).
                andExpect(view().name("allTowns"));
    }

    /*@Test
    @WithMockUser(value = "testUser", username = "testUser", roles = "ADMIN")
    void testUpdate() throws Exception {
        mockMvc.perform(get("/towns/1/update")).
                andExpect(status().isOk()).
                andExpect(view().name("town-update"));
    }


    @Test
    @WithMockUser(value = "testUser", username = "testUser")
    void testDetails() throws Exception {
        mockMvc.perform(get("/towns/1/details")).
                andExpect(status().isOk()).
                andExpect(view().name("town-details"));
    }*/

   /* @Test
    @WithMockUser(value = "testUser", username = "testUser", roles = "ADMIN")
    void testAddTownCorrect() throws Exception {

        mockMvc
                .perform(post("/towns/add")
                        .param("name", "Varna")
                        .param("description", "any text")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                )
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/"));

        Optional<Town> town = mockTownRepository.findByName("Varna");
        assertTrue(town.isPresent());
        assertTrue(town.get().getId() > 0);

        mockTownRepository.delete(town.get());
    }
*/
    @Test
    @WithMockUser(value = "test", username = "test", roles = "USER")
    void testAddTownNotAuthorized() throws Exception {

        mockMvc
                .perform(post("/towns/add"))
                        .andExpect(status().is4xxClientError());
    }

    /*@Test
    @WithMockUser(value = "testUser", username = "testUser", roles = "ADMIN")
    void testUpdateTownCorrect() throws Exception {
        mockMvc
                .perform(patch("/towns/"+testTown.getId()+"/update" )
                        .param("name", "Ploviv")
                        .param("description", "Old town of Plovdiv is very attractive")
                        .with(csrf()).contentType(MediaType.APPLICATION_FORM_URLENCODED)
                )
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/towns/all"));

      /*  Optional<Town> plovdiv = mockTownRepository.findByName("Plovdiv");
        assertTrue(plovdiv.isPresent());
        assertTrue(mockTownRepository.findByName("Sofia").isEmpty());
    }*/

   @Test
    @WithMockUser(value = "test", username = "test", roles = "USER")
    void testEditTownNotAuthorized() throws Exception {

        mockMvc
                .perform(patch("/towns/"+testTown.getId()+"/update" ))
                .andExpect(status().is4xxClientError());

    }
  /* @Test
   void test_GetTownDetails_ReturnsCorrectly() throws Exception {
       mockMvc
               .perform(get("/towns/" + testTown.getId()+"/details"))
               .andExpect(status().isOk())
               .andExpect(view().name("town-details"))
               .andExpect(model().attributeExists("town"));
   }
*/
    @Test
    void test_GetTownDetailsInvalidId_Throws() throws Exception {
        mockMvc
                .perform(get("/towns/"+100+"/details"))
                .andExpect(status().is3xxRedirection());
    }

   /* @Test
    @WithMockUser(username = "testUser", roles = "ADMIN")
    void test_Delete_WorksCorrectly() throws Exception {
        mockMvc
                .perform(delete("/towns/" + testTown.getId()+"/delete")
                        .with(csrf()))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/towns/all"));

        Town deletedTown = mockTownRepository.findById(testTown.getId()).get();
        assertTrue(deletedTown.getName().isEmpty());
    }*/
}