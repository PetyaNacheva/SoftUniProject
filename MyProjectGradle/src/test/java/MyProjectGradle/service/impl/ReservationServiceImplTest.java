package MyProjectGradle.service.impl;

import MyProjectGradle.models.entities.*;
import MyProjectGradle.models.enums.RolesEnum;
import MyProjectGradle.models.enums.TypeEnum;
import MyProjectGradle.models.service.ReservationServiceModel;
import MyProjectGradle.models.views.ReservationDetailsViewModel;
import MyProjectGradle.models.views.ReservationViewModel;
import MyProjectGradle.repository.*;
import MyProjectGradle.service.UserService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;

import javax.persistence.EntityNotFoundException;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ReservationServiceImplTest {
    private UserEntity testUser, secondUser;
    private final ModelMapper modelMapper= new ModelMapper();
    private ReservationServiceImpl reservationService;
    private Role userRole, adminRole, hostRole;
    private Reservation testReservation, pastReservation;
    private Apartment testApartment;
    private Town testTown;
    private Type studio, oneBed;
    private Picture pictureFirst;
    private ReservationViewModel reservationViewModel;
    private ReservationServiceModel reservationServiceModel;

    @Mock
    ReservationRepository reservationRepository;
    @Mock
    TownRepository townRepository;

    @Mock
    ApartmentRepository apartmentRepository;

    @Mock
    UserRepository userRepository;
    @Mock
    UserService userService;
    @Mock
    TypeRepository typeRepository;
    @Mock
    PictureRepository pictureRepository;


    @BeforeEach
    void setUp(){
        reservationService = new ReservationServiceImpl(reservationRepository, new ModelMapper(), userService);

        testUser = new UserEntity();
        userRole = new Role();
        userRole.setName(RolesEnum.USER);
        adminRole = new Role();
        adminRole.setName(RolesEnum.ADMIN);
        testUser.setRole(List.of(adminRole));
        testUser.setUsername("testUser");
        testUser.setFirstName("test");
        testUser.setLastName("test");
        testUser.setPassword("test");
        testUser.setEmail("test@test.com");
        testUser.setPhone("+3598935467");

        secondUser = new UserEntity();
        secondUser.setRole(List.of(userRole));
        secondUser.setUsername("secondUser");
        secondUser.setFirstName("secondUser");
        secondUser.setLastName("secondUser");
        secondUser.setPassword("secondUser");
        secondUser.setEmail("test@test.com");
        secondUser.setPhone("+3598935467");

        testTown = new Town();
        testTown.setName("TestTown");
        testTown.setDescription("TestTown");
        testTown.setApartments(new ArrayList<>());
        townRepository.save(testTown);

        studio = new Type();
        studio.setType(TypeEnum.STUDIO);
        studio.setCapacity(3);
        studio.setDescription("studio");
        typeRepository.save(studio);

        pictureFirst=new Picture();
        pictureFirst.setTitle("first");
        pictureFirst.setUrl("first");
        pictureFirst.setUserName("first");
        pictureFirst.setPublicId("firstPublicId");
        pictureRepository.save(pictureFirst);

        testApartment = new Apartment();
        testApartment.setName("test");
        testApartment.setOwner(testUser);
        testApartment.setTown(testTown);
        testApartment.setAddress("Test adress");
        testApartment.setType(studio);
        testApartment.setPrice(BigDecimal.valueOf(50));
        testApartment.setPictures(List.of(pictureFirst));
        apartmentRepository.save(testApartment);


        testReservation = new Reservation();
        testReservation.setUsername(testUser);
        testReservation.setApartment(testApartment);
        testReservation.setGuestName(testUser.getFirstName());
        testReservation.setArrivalDate(LocalDate.of(2022, 10, 1));
        testReservation.setDepartureDate(LocalDate.of(2022, 10, 5));
        testReservation.setReservedOn(LocalDate.of(2022, 7, 1));
        testReservation.setNumberOfGuests(2);
        testReservation.setPrice(BigDecimal.valueOf(200));
        testReservation.setId(1L);
        reservationRepository.save(testReservation);

        pastReservation = new Reservation();
        pastReservation.setUsername(testUser);
        pastReservation.setApartment(testApartment);
        pastReservation.setGuestName(testUser.getFirstName());
        pastReservation.setArrivalDate(LocalDate.of(2021, 1, 1));
        pastReservation.setDepartureDate(LocalDate.of(2021, 1, 5));
        pastReservation.setReservedOn(LocalDate.of(2020, 7, 1));
        pastReservation.setNumberOfGuests(2);
        pastReservation.setPrice(BigDecimal.valueOf(200));
        pastReservation.setId(2L);
        reservationRepository.save(pastReservation);


    }


    @AfterEach
    void tearDown(){
        reservationRepository.deleteAll();
    }

    @Test
    public void testFindByIdAndUsername(){
        when(reservationRepository.findById(testReservation.getId())).thenReturn(Optional.of(testReservation));

    ReservationDetailsViewModel byIdAndUsername = reservationService.findByIdAndUsername(testReservation.getId(), testUser.getUsername());
    assertEquals(testUser.getFirstName(), byIdAndUsername.getGuestName());
}

    @Test
    public void testFindByIdAndUsernameShouldThrow(){
        when(reservationRepository.findById(55L)).thenReturn(Optional.empty());;
        assertThrows(EntityNotFoundException.class,()->reservationService.findByIdAndUsername(55L, testUser.getUsername()));
    }

    @Test
    public void testCanDelete(){
        when(reservationRepository.findById(testReservation.getId())).thenReturn(Optional.of(testReservation));

        assertTrue(reservationService.canDelete(testReservation.getId(), testUser.getUsername()));
    }

    @Test
    public void testCanDeleteShoulThrow(){
        when(reservationRepository.findById(testReservation.getId())).thenReturn(Optional.of(testReservation));
        assertFalse(reservationService.canDelete(testReservation.getId(), secondUser.getUsername()));
    }
    
    @Test
    public void testDelete(){

        reservationService.deleteReservation(testReservation.getId());
        assertEquals(0, reservationRepository.findAll().size());
        
    }

    @Test
    public  void testDeletePastReservations(){
        when(reservationRepository.findAll()).thenReturn(List.of(testReservation));
        reservationRepository.save(pastReservation);
        reservationService.deletePastReservations();
       assertEquals(1, reservationRepository.findAll().size());
    }

    @Test
    public void testFindAllReservationsWithTotalProfit(){
        when(reservationRepository.findAll()).thenReturn(List.of(testReservation, pastReservation));
        when(reservationRepository.findTotalPrice()).thenReturn(BigDecimal.valueOf(400));
         assertEquals("Reservation count 2 - with total profit 400.00", reservationService.findAllReservationsWithTotalProfit());
    }

    @Test
    public void testFindById(){
        when(reservationRepository.findById(pastReservation.getId())).thenReturn(Optional.of(pastReservation));

        assertEquals("test", reservationService.findById(pastReservation.getId()).getGuestName());
    }

    @Test
    public void testFindByIdThrow(){
        assertThrows(EntityNotFoundException.class, () -> reservationService.findById(5L));
    }

    @Test
    public void testFindAllReservationsByUsernameFilterByDate(){
        when(reservationRepository.findAllByUsername_Username(testUser.getUsername())).thenReturn(List.of(testReservation, pastReservation));
        List<ReservationViewModel> past = reservationService.findAllReservationsByUsernameFilterByDate(testUser.getUsername(), "past");

        assertEquals(1, past.size());

    }
    @Test
    public void testFindAllReservationsByUsernameFilterByDateWithToday(){
        when(reservationRepository.findAllByUsername_Username(testUser.getUsername())).thenReturn(List.of(testReservation, pastReservation));
        List<ReservationViewModel> past = reservationService.findAllReservationsByUsernameFilterByDate(testUser.getUsername(), "today");

        assertEquals(0, past.size());

    }

    @Test
    public void testFindAllReservationsByUsernameFilterByDateWithFuture(){
        when(reservationRepository.findAllByUsername_Username(testUser.getUsername())).thenReturn(List.of(testReservation, pastReservation));
        List<ReservationViewModel> past = reservationService.findAllReservationsByUsernameFilterByDate(testUser.getUsername(), "future");

        assertEquals(1, past.size());

    }

    @Test
    public void testAddReservation(){

        reservationServiceModel = new ReservationServiceModel();
        reservationServiceModel.setUsername(testUser);

        reservationServiceModel.setReservedOn(LocalDate.now());
        reservationServiceModel.setApartment(testApartment);
        reservationServiceModel.setArrivalDate(LocalDate.now().plusDays(5));
        reservationServiceModel.setDepartureDate(LocalDate.now().plusDays(8));
        reservationServiceModel.setGuestName(testUser.getFirstName());
        reservationServiceModel.setNumberOfGuests(2);
        reservationServiceModel.setPrice(BigDecimal.valueOf(150));
        reservationServiceModel.setId(3L);
       assertTrue(reservationService.addReservation(reservationServiceModel));
    }

    @Test
    public void testAddReservationFalse(){

        reservationServiceModel = new ReservationServiceModel();
        reservationServiceModel.setUsername(testUser);

        reservationServiceModel.setReservedOn(LocalDate.now());
        reservationServiceModel.setApartment(testApartment);
        reservationServiceModel.setArrivalDate(LocalDate.now().plusDays(5));
        reservationServiceModel.setDepartureDate(LocalDate.now().plusDays(8));
        reservationServiceModel.setGuestName(testUser.getFirstName());
        reservationServiceModel.setNumberOfGuests(2);
        reservationServiceModel.setPrice(BigDecimal.valueOf(150));

        assertFalse(reservationService.addReservation(reservationServiceModel));
    }
    
    @Test
    public void testFindAllByApartmentsByName(){
        when(reservationRepository.findAllByApartment_Name(testApartment.getName())).thenReturn(List.of(testReservation, pastReservation));

        assertEquals(2, reservationService.findAllByApartmentsByName(testApartment.getName()).size());
    }
}