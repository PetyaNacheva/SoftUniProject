package MyProjectGradle.service.impl;

import MyProjectGradle.models.entities.*;
import MyProjectGradle.models.enums.RolesEnum;
import MyProjectGradle.models.enums.TypeEnum;
import MyProjectGradle.models.views.ReservationDetailsViewModel;
import MyProjectGradle.models.views.ReservationViewModel;
import MyProjectGradle.repository.*;
import MyProjectGradle.service.UserService;
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

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ReservationServiceImplTest {
    private UserEntity testUser, secondUser;
    private final ModelMapper modelMapper= new ModelMapper();
    private ReservationServiceImpl reservationService;
    private Role userRole, adminRole, hostRole;
    private Reservation testReservation;
    private Apartment testApartment;
    private Town testTown;
    private Type studio, oneBed;
    private Picture pictureFirst;
    private ReservationViewModel reservationViewModel;

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
        reservationRepository.save(testReservation);

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
}