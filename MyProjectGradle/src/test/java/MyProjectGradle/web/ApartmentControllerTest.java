package MyProjectGradle.web;

import MyProjectGradle.models.entities.*;
import MyProjectGradle.models.enums.RolesEnum;
import MyProjectGradle.models.enums.TypeEnum;
import MyProjectGradle.repository.*;
import MyProjectGradle.service.ApartmentService;
import MyProjectGradle.service.TypeService;
import MyProjectGradle.service.impl.ApartmentServiceImpl;
import MyProjectGradle.service.impl.TownServiceImpl;
import MyProjectGradle.service.impl.TypeServiceImpl;
import MyProjectGradle.service.impl.UserServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.util.List;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
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
    @Autowired
    private TypeRepository typeRepository;
    @Autowired
    private PictureRepository pictureRepository;
    @MockBean
    private ApartmentServiceImpl apartmentService;
    @MockBean
    private TownServiceImpl townService;
    @MockBean
    private TypeServiceImpl typeService;
    @MockBean
    private UserServiceImpl userService;

    @BeforeEach
    void setUp() {
        mockUserRepository.deleteAll();
        mockRoleRepository.deleteAll();
        apartmentRepository.deleteAll();
        typeRepository.deleteAll();
        pictureRepository.deleteAll();
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
        typeRepository.save(studio);

        testPicture=new Picture();
        testPicture.setTitle("test");
        testPicture.setUrl("testUrl");
        testPicture.setUserName("test");
        testPicture.setPublicId("publicId");
        pictureRepository.save(testPicture);

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


   /*@Test
    @WithMockUser(authorities="ROLE_USER")
    void testAddApartment() throws Exception {
        mockMvc.perform(get("/apartments/add").with(csrf())).
                andExpect(status().isOk()).
                andExpect(view().name("add-apartment"));
    }
    @Test
    void testAddApartmentNotAuthorized() throws Exception {
        mockMvc.perform(post("/apartments/add"))
                .andExpect(status().is4xxClientError());
    }


    @Test
    @WithMockUser(authorities="ROLE_USER")
    void testGetMyApartments() throws Exception {
        mockMvc.perform(get("/apartments/getMy").with(csrf())).
                andExpect(status().isOk()).
                andExpect(view().name("my-apartments"));
    }

    @Test
    void testGetMyApartmentsNotAuthorized() throws Exception {
        mockMvc.perform(post("/apartments/getMy"))
                .andExpect(status().is4xxClientError());
    }*/
}