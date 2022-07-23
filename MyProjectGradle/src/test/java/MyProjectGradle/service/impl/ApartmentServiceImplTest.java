package MyProjectGradle.service.impl;

import MyProjectGradle.models.binding.TownUpdateBindingModel;
import MyProjectGradle.models.entities.*;
import MyProjectGradle.models.enums.RolesEnum;
import MyProjectGradle.models.enums.TypeEnum;
import MyProjectGradle.models.service.ApartmentServiceModel;
import MyProjectGradle.models.views.ApartmentViewModel;
import MyProjectGradle.repository.*;
import MyProjectGradle.service.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.stubbing.OngoingStubbing;
import org.modelmapper.ModelMapper;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.crypto.password.Pbkdf2PasswordEncoder;
import org.springframework.web.multipart.MultipartFile;

import javax.persistence.EntityNotFoundException;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ApartmentServiceImplTest {
    private Apartment apartment1, apartment2;
    private UserEntity testUser;
    private Town testTown;
    private Picture pictureTest, pictureFirst, pictureSecond;
    private ApartmentService apartmentService;
    private PasswordEncoder passwordEncoder;
    private List<GrantedAuthority> authorities;
    private Role userRole, adminRole, hostRole;
    private Type studio, oneBed;
    private ApartmentServiceModel apartmentServiceModel;
    private MultipartFile multipartFile;



    @Mock
    UserRepository userRepository;

    @Mock
    ApartmentRepository apartmentRepository;
    @Mock
    TownService townService;
    @Mock
    TownRepository townRepository;
    @Mock
    TypeRepository typeRepository;

    @Mock
    TypeService typeService;
    @Mock
    PictureService pictureService;

    @Mock
    PictureRepository pictureRepository;
    @Mock
    ReservationService reservationService;
    @Mock
    MultipartFile mockMultipartFile;
    @Mock
    UserService userService;
    @Mock
    ModelMapper modelMapper = new ModelMapper();


    @BeforeEach
    public void setUp(){
        passwordEncoder = new Pbkdf2PasswordEncoder();
        apartmentService = new ApartmentServiceImpl(apartmentRepository, modelMapper,userService, townService, typeService, pictureService, reservationService);
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

        testTown = new Town();
        testTown.setName("testTown");
        testTown.setDescription("testTown");

        pictureTest=new Picture();
        pictureTest.setTitle("test");
        pictureTest.setUrl("testUrl");
        pictureTest.setUserName("test");
        pictureTest.setPublicId("publicId");
        testTown.setPictureUrl(pictureTest);

        townRepository.save(testTown);

        pictureFirst=new Picture();
        pictureFirst.setTitle("first");
        pictureFirst.setUrl("first");
        pictureFirst.setUserName("first");
        pictureFirst.setPublicId("firstPublicId");

        pictureSecond=new Picture();
        pictureSecond.setTitle("second");
        pictureSecond.setUrl("second");
        pictureSecond.setUserName("second");
        pictureSecond.setPublicId("secondPublicId");

        pictureRepository.save(pictureTest);
        pictureRepository.save(pictureFirst);
        pictureRepository.save(pictureSecond);

        studio = new Type();
        studio.setType(TypeEnum.STUDIO);
        studio.setCapacity(3);
        studio.setDescription("studio");

        oneBed = new Type();
        oneBed.setType(TypeEnum.ONE_BEDROOM);
        oneBed.setCapacity(4);
        oneBed.setDescription("oneBed");

        typeRepository.save(studio);
        typeRepository.save(oneBed);

        apartment1=new Apartment();
        apartment1.setOwner(testUser);
        apartment1.setType(studio);
        apartment1.setAddress("any address");
        apartment1.setPrice(BigDecimal.valueOf(50));
        apartment1.setTown(testTown);
        apartment1.setName("firstApartment");
        apartment1.setPictures(List.of(pictureFirst));


        apartment2=new Apartment();
        apartment2.setOwner(testUser);
        apartment2.setType(oneBed);
        apartment2.setAddress("second adress");
        apartment2.setPrice(BigDecimal.valueOf(80));
        apartment2.setTown(testTown);
        apartment2.setName("secondApartment");
        apartment2.setPictures(List.of(pictureSecond));
        testUser.setHostedApartments(List.of(apartment1, apartment2));
        apartmentRepository.save(apartment1);
        apartmentRepository.save(apartment2);

        multipartFile = new MockMultipartFile("test", "testFileName", "testContentName",new byte[1]);

    }

   /* @Test
    public void testFindAllApartmentsByUsername(){
        when(userRepository.findByUsername(testUser.getUsername())).thenReturn(Optional.of(testUser));
        when(apartmentRepository.findApartmentByName(apartment1.getName()));
        when(apartmentRepository.findApartmentByName(apartment2.getName()));
        List<ApartmentViewModel> allApartmentsByUsername = apartmentService.findAllApartmentsByUsername(testUser);
        allApartmentsByUsername.size();
        assertEquals(apartmentService.findAllApartmentsByUsername(testUser).size(),2);

    }*/

    @Test
    public void testFindById(){

        when(apartmentRepository.findById(apartment1.getId())).thenReturn(Optional.of(apartment1));
        assertEquals("firstApartment", apartmentService.findApartmentById(apartment1.getId()).getName());
    }

    @Test
    public void testFindByIdThrow(){
        assertThrows(EntityNotFoundException.class, () -> apartmentService.findById(5L));
    }


    @Test
    public void testFindByApartmentName(){
        when(apartmentRepository.findApartmentByName(apartment1.getName())).thenReturn(Optional.of(apartment1));
        assertEquals("any address", apartmentService.findByApartmentByApartmentName(apartment1.getName()).getAddress());
    }

    @Test
    public void testFindApartmentByNameShouldThrow(){
        String fakeApartment = "FakeApartment";
        when(apartmentRepository.findApartmentByName(fakeApartment)).
                thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () ->apartmentService.findByApartmentByApartmentName(fakeApartment));
    }

    @Test
    public void testGetAllApartments(){
        when(apartmentRepository.findAll()).thenReturn(List.of(apartment1, apartment2));

        assertEquals(2, apartmentService.getAllApartments().size());
    }

   /* @Test
    public  void testCanDeleteApartment(){
        lenient().when(apartmentRepository.findById(apartment1.getId())).thenReturn(Optional.of(apartment1));
        lenient().when(userService.isAdmin(testUser.getUsername())).thenReturn(true);
       /* List<Reservation> reservation = new ArrayList<>();
        apartment1.setReservations(reservation);

        OngoingStubbing<ApartmentServiceModel> apService = when(modelMapper.map(apartmentService.findById(apartment1.getId()), ApartmentServiceModel.class));

        assertTrue(apartmentService.canDelete(apartment1.getId(), testUser.getUsername()));
    }


    @Test
    public void testCanDeleteApartmentFalse(){
        String fakeUser = "fakeUser";
        lenient().when(apartmentRepository.findById(apartment1.getId())).thenReturn(Optional.of(apartment1));
        lenient().when(userService.isAdmin(fakeUser)).thenReturn(false);
        assertFalse(apartmentService.canDelete(apartment1.getId(), fakeUser));
    }*/


    @Test
    public void testUpdateApartment() throws IOException {

        apartment1.setPictures(List.of(pictureTest));

        apartmentServiceModel = new ApartmentServiceModel();
        apartmentServiceModel.setPicture(mockMultipartFile);
        apartmentServiceModel.setName("TestingTesting");
        apartmentServiceModel.setReservationList(apartment1.getReservations());
        apartmentServiceModel.setAddress(apartment1.getAddress());
        apartmentServiceModel.setOwner(apartment1.getOwner());
        apartmentServiceModel.setTown(apartment1.getTown().getName());
        apartmentServiceModel.setPrice(apartment1.getPrice());

        apartmentServiceModel.setType(apartment1.getType().getType());


        pictureService.findPictureByPublicId(apartment1.getPictures().get(0).getPublicId());
        apartment1.getPictures().get(0).setTitle("");
        apartmentServiceModel.setId(apartment1.getId());

        lenient().when(apartmentRepository.findById(apartment1.getId())).thenReturn(Optional.of(apartment1));

        Long aLong = apartmentService.updateApartment(apartmentServiceModel);
        assertEquals(apartment1.getId(), aLong);
    }

    @Test
    public void testUpdateApartmentShouldThrow() throws IOException {
        apartmentServiceModel = new ApartmentServiceModel();
        apartmentServiceModel.setId(555L);
        when(apartmentRepository.findById(555L)).
                thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () ->apartmentService.updateApartment(apartmentServiceModel));
    }
}