package MyProjectGradle.web;

import MyProjectGradle.models.entities.*;
import MyProjectGradle.models.enums.RolesEnum;
import MyProjectGradle.models.enums.TypeEnum;
import MyProjectGradle.repository.*;
import MyProjectGradle.service.ApartmentService;
import MyProjectGradle.service.CloudinaryService;
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
import org.springframework.mock.web.MockMultipartFile;
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
    @MockBean
    private RoleRepository mockRoleRepository;
    @MockBean
    private UserRepository mockUserRepository;
    @MockBean
    private ApartmentRepository apartmentRepository;
    @MockBean
    private TownRepository townRepository;
    @MockBean
    private TypeRepository typeRepository;
    @MockBean
    private PictureRepository pictureRepository;
    @MockBean
    private ApartmentServiceImpl apartmentService;
    @MockBean
    private TownServiceImpl townService;
    @MockBean
    private TypeServiceImpl typeService;
    @MockBean
    private UserServiceImpl userService;
    @MockBean
    private CloudinaryService cloudinaryService;

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


   @Test
    @WithMockUser()
    void testAddApartmentGet() throws Exception {
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
    void testGetMyApartmentsNotAuthorized() throws Exception {
        mockMvc.perform(post("/apartments/getMy"))
                .andExpect(status().is4xxClientError());
    }

    @Test
    @WithMockUser
    void testApartmentUpdate() throws Exception {
        mockMvc.perform(get("/apartments/"+100+"/update").with(csrf()))
               .andExpect(view().name("errors/error404"));
    }

    @Test
    @WithMockUser
    void testAddApartmentPost() throws Exception {
        MockMultipartFile multipartFile = new MockMultipartFile("file", "test.txt",
                "text/plain", "Spring Framework".getBytes());

        mockMvc
                .perform(post("/apartments/add")
                        .param("name", "MyApartment")
                        .param("address", "any text")
                        .param("price", "50")
                        .param("town", testTown.getName())
                        .param("type", studio.getType().name())
                        .param("picture", multipartFile.getName())
                        .with(csrf())
                )
                .andExpect(status().is3xxRedirection())
                .andExpect(view().name("redirect:add"));
    }
}