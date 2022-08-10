package MyProjectGradle.service.impl;

import MyProjectGradle.models.entities.*;
import MyProjectGradle.models.enums.RolesEnum;
import MyProjectGradle.models.enums.TypeEnum;
import MyProjectGradle.models.service.ApartmentServiceModel;
import MyProjectGradle.models.views.ApartmentDetailsViewModel;
import MyProjectGradle.models.views.ApartmentStatisticViewModel;
import MyProjectGradle.models.views.ApartmentViewModel;
import MyProjectGradle.models.views.ReservationStatViewModel;
import MyProjectGradle.repository.*;
import MyProjectGradle.service.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.crypto.password.Pbkdf2PasswordEncoder;
import org.springframework.test.context.TestExecutionListeners;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.web.multipart.MultipartFile;

import javax.persistence.EntityNotFoundException;
import java.io.IOException;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
@RunWith(SpringJUnit4ClassRunner.class)
@TestExecutionListeners({})
@ExtendWith(MockitoExtension.class)
class ApartmentServiceImplTest {
    private Apartment apartment1, apartment2;
    private UserEntity testUser, otherUser;
    private Town testTown;
    private Picture pictureTest, pictureFirst, pictureSecond;
    private ApartmentServiceImpl apartmentService;
    private PasswordEncoder passwordEncoder;
    private Role userRole, adminRole, hostRole;
    private Type studio, oneBed;
    private ApartmentServiceModel apartmentServiceModel;
    private MultipartFile multipartFile;
    private ApartmentDetailsViewModel apartmentDetailsViewModel;
    private final ModelMapper modelMapper= new ModelMapper();
    private Reservation testReservation;

    @Mock
    UserRepository userRepository;

    @Mock
    ApartmentRepository apartmentRepository= Mockito.mock(ApartmentRepository.class);
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
    ReservationRepository reservationRepository;
    @Mock
    ReservationService reservationService;
    @Mock
    MultipartFile mockMultipartFile;
    @Mock
    UserService userService;



    @BeforeEach
    public void setUp(){
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

        otherUser = new UserEntity();

        otherUser.setRole(List.of(userRole));
        otherUser.setUsername("otherUser");
        otherUser.setFirstName("otherUser");
        otherUser.setLastName("otherUser");
        otherUser.setPassword("otherUser");
        otherUser.setEmail("test@test.com");
        otherUser.setPhone("+3598935467");
        otherUser.setId(2L);
        userRepository.save(otherUser);

        testTown = new Town();
        testTown.setName("testTown");
        testTown.setDescription("testTown");

        pictureTest=new Picture();
        pictureTest.setTitle("test");
        pictureTest.setUrl("testUrl");
        pictureTest.setUserName("test");
        pictureTest.setPublicId("publicId");
        testTown.setPictureUrl(pictureTest);
        testTown.setId(1L);

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
        pictureFirst.setApartmentName(apartment1.getName());
        pictureRepository.save(pictureFirst);
        apartment1.setPictures(List.of(pictureFirst));
        apartment1.setId(1L);


        apartment2=new Apartment();
        apartment2.setOwner(testUser);
        apartment2.setType(oneBed);
        apartment2.setAddress("second adress");
        apartment2.setPrice(BigDecimal.valueOf(80));
        apartment2.setTown(testTown);
        apartment2.setName("secondApartment");
        pictureSecond.setApartmentName(apartment2.getName());
        pictureRepository.save(pictureSecond);
        apartment2.setPictures(List.of(pictureSecond));
         apartment1.setId(1L);
         apartment2.setId(2L);
        testUser.setHostedApartments(List.of(apartment1, apartment2));
        apartmentRepository.save(apartment1);
        apartmentRepository.save(apartment2);

        testReservation = new Reservation();
        testReservation.setUsername(testUser);
        testReservation.setApartment(apartment1);
        testReservation.setGuestName(testUser.getFirstName());
        testReservation.setArrivalDate(LocalDate.of(2022, 10, 1));
        testReservation.setDepartureDate(LocalDate.of(2022, 10, 5));
        testReservation.setReservedOn(LocalDate.of(2022, 7, 1));
        testReservation.setNumberOfGuests(2);
        testReservation.setPrice(BigDecimal.valueOf(200));
        testReservation.setId(1L);
        reservationRepository.save(testReservation);

        multipartFile = new MockMultipartFile("test", "testFileName", "testContentName",new byte[1]);
        apartmentService = new ApartmentServiceImpl(apartmentRepository, new ModelMapper(),userService, townService, typeService, pictureService, reservationService);
    }


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

    @Test
    public  void testCanDeleteApartment(){
       when(apartmentRepository.findById(apartment1.getId())).thenReturn(Optional.of(apartment1));
        when(userService.findByUsername(testUser.getUsername())).thenReturn(testUser);
        assertTrue(apartmentService.canDelete(apartment1.getId(), testUser.getUsername()));
    }


    @Test
    public void testCanDeleteApartmentFalse(){

        when(apartmentRepository.findById(apartment1.getId())).thenReturn(Optional.of(apartment1));
        when(userService.findByUsername(otherUser.getUsername())).thenReturn(otherUser);
        assertFalse(apartmentService.canDelete(apartment1.getId(), otherUser.getUsername()));
    }


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

    @Test
    public void testDeleteApartment(){
       lenient().when(apartmentRepository.findById(apartment2.getId())).thenReturn(Optional.of(apartment2));
        lenient().when(apartmentRepository.findApartmentByName(apartment2.getName())).thenReturn(Optional.of(apartment2));

        assertEquals("secondApartment",apartmentRepository.findApartmentByName(apartment2.getName()).get().getName());
        apartmentService.deleteApartment(apartment2.getId());
        assertEquals(0, apartmentRepository.findAll().size());
    }


    @Test
    public void testDeleteApartmentShouldThrow(){
        when(apartmentRepository.findById(555L)).thenReturn(Optional.empty());;
        assertThrows(EntityNotFoundException.class,()->apartmentService.deleteApartment(555L));
    }

    @Test
    public void testFindAllApartmentsByTownAndUser(){
       when(apartmentRepository.findAllByTown_Id(testTown.getId())).thenReturn(List.of(apartment1, apartment2));
        List<ApartmentViewModel> allApartmentsByTownAndUser = apartmentService.findAllApartmentsByTownAndUser(testTown.getId(), testUser.getId());

        assertEquals(2, allApartmentsByTownAndUser.size());
    }

    @Test
    public void testFindAllApartments(){
        when(apartmentRepository.findAll()).thenReturn(List.of(apartment1, apartment2));

        assertEquals("Total count of all apartments: 2", apartmentService.findAllApartments());
    }


    @Test
    public void testFindApartmentDetailsViewModelById(){
        when(apartmentRepository.findById(apartment1.getId())).thenReturn(Optional.of(apartment1));

        assertEquals(apartment1.getAddress(),apartmentService.findApartmentDetailsViewModelById(apartment1.getId()).getAddress());
    }

    @Test
    public void testFindApartmentDetailsViewModelByIdShouldThrow(){
        when(apartmentRepository.findById(55L)).
                thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () ->apartmentService.findApartmentDetailsViewModelById(55L));
    }


    @Test
    public void testCanUpdate(){
        when(apartmentRepository.findById(apartment1.getId())).thenReturn(Optional.of(apartment1));

        assertTrue(apartmentService.canUpdate(apartment1.getId(), testUser.getUsername()));
    }


    @Test
    public void testFindByIdDetailsViewModel(){
        when(apartmentRepository.findById(1L)).thenReturn(Optional.of(apartment1));

        apartmentDetailsViewModel = new ApartmentDetailsViewModel();
        apartmentDetailsViewModel.setId(apartment1.getId());
        apartmentDetailsViewModel.setAddress(apartment1.getAddress());
        apartmentDetailsViewModel.setName(apartment1.getName());
        apartmentDetailsViewModel.setCapacity(apartment1.getType().getCapacity());
        apartmentDetailsViewModel.setType(apartment1.getType());
        apartmentDetailsViewModel.setPictures(apartment1.getPictures());
        apartmentDetailsViewModel.setTown(apartment1.getTown().getName());
        apartmentDetailsViewModel.setPrice(apartment1.getPrice());
        apartmentDetailsViewModel.setOwner(testUser.getUsername());


        assertEquals(apartmentDetailsViewModel.getName(), apartmentService.findById(apartment1.getId()).getName());
    }

    @Test
    public void testIsAvailable(){
        when(reservationService.findAllByApartmentsByName(apartment1.getName())).thenReturn(List.of(testReservation));

        LocalDate arrivalTest= LocalDate.of(2022,10,1);
        LocalDate departureTest= LocalDate.of(2022, 10,5);
        assertEquals("Next available days are after 2022-10-05", apartmentService.isAvailable(apartment1.getName(), arrivalTest, departureTest));
    }
    @Test
    public void testFindNextAvailableDays(){
        when(reservationService.findAllByApartmentsByName(apartment1.getName())).thenReturn(List.of(testReservation));

        assertEquals("Next available days are after 2022-10-05", apartmentService.isAvailable(apartment1.getName(),testReservation.getArrivalDate(), testReservation.getDepartureDate()));
    }

    @Test
    public void testFindAllAvailableApartmentsInPeriod(){
        when(apartmentRepository.findAllByTown_Name(testTown.getName())).thenReturn(List.of(apartment1, apartment2));
        assertEquals(2, apartmentService.findAllAvailableApartmentsInPeriod(testTown.getName(),LocalDate.now().plusDays(50), LocalDate.now().plusDays(60)).size());
    }

    @Test
    public void testGetStatistics(){
        when(apartmentRepository.findById(apartment1.getId())).thenReturn(Optional.of(apartment1));

        assertEquals(0,apartmentService.getStatistic(apartment1.getId()).getComingReservations().size());
        assertEquals(0, apartmentService.getStatistic(apartment1.getId()).getPast30DaysReservations().size());
        assertEquals(BigDecimal.ZERO, apartmentService.getStatistic(apartment1.getId()).getProfitFromPastMonth());
    }


}