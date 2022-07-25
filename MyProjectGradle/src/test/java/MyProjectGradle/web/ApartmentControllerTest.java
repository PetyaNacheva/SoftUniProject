package MyProjectGradle.web;

import MyProjectGradle.models.entities.*;
import MyProjectGradle.models.enums.RolesEnum;
import MyProjectGradle.models.enums.TypeEnum;
import MyProjectGradle.repository.ApartmentRepository;
import MyProjectGradle.repository.RoleRepository;
import MyProjectGradle.repository.TownRepository;
import MyProjectGradle.repository.UserRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.Pbkdf2PasswordEncoder;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

@SpringBootTest
@AutoConfigureMockMvc

class ApartmentControllerTest {

    private UserEntity testUser;
    private Role userRole, adminRole;
    private Apartment apartment;
    private Town testTown;
    private Type studio;
    private Picture testPicture;


    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private RoleRepository mockRoleRepository;
    @Autowired
    private UserRepository mockUserRepository;
    @Autowired
    private ApartmentRepository apartmentRepository;
    @Autowired
    private TownRepository townRepository;

    @BeforeEach
    void setUp() {

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
        townRepository.save(testTown);
        studio = new Type();
        studio.setType(TypeEnum.STUDIO);
        studio.setCapacity(3);
        studio.setDescription("studio");

        testPicture=new Picture();
        testPicture.setTitle("test");
        testPicture.setUrl("testUrl");
        testPicture.setUserName("test");
        testPicture.setPublicId("publicId");


        apartment=new Apartment();
        apartment.setOwner(testUser);
        apartment.setType(studio);
        apartment.setAddress("any address");
        apartment.setPrice(BigDecimal.valueOf(50));
        apartment.setTown(testTown);
        apartment.setName("firstApartment");
        apartment.setPictures(List.of(testPicture));
        apartment.setId(1L);
        apartmentRepository.save(apartment);
    }

    @AfterEach
    void tearDown() {
        mockUserRepository.deleteAll();
        mockRoleRepository.deleteAll();
        apartmentRepository.deleteAll();

    }


   /* @Test
    @WithMockUser(value = "testUser", username = "testUser", roles = "ADMIN")
    void testAddApartment() throws Exception {
        mockMvc.perform(get("/apartments/add")).
                andExpect(status().isOk()).
                andExpect(view().name("add-apartment"));
    }


    @Test
    @WithMockUser(value = "testUser", username = "testUser", roles = "ADMIN")
    void testGetMyApartments() throws Exception {
        mockMvc.perform(get("/apartments/getMy")).
                andExpect(status().isOk()).
                andExpect(view().name("my-apartments"));
    }*/
}