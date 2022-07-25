package MyProjectGradle.service.impl;

import MyProjectGradle.models.entities.*;
import MyProjectGradle.models.enums.RolesEnum;
import MyProjectGradle.models.enums.TypeEnum;
import MyProjectGradle.models.service.LogServiceModel;
import MyProjectGradle.repository.LogRepository;
import MyProjectGradle.service.ApartmentService;
import MyProjectGradle.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class LogServiceImplTest {

    private final ModelMapper modelMapper = new ModelMapper();
    private LogServiceImpl logService;
    private Log testLog;
    private Type studio;
    private Apartment testApartment;
    private UserEntity testUser;
    private Town testTown;
    private Picture testPicture;
    private Role adminRole;

    @Mock
    private LogRepository logRepository;

    @Mock
    private UserService userService;

    @Mock
    private ApartmentService apartmentService;

    @BeforeEach
    void setUp(){

        logService = new LogServiceImpl(logRepository, apartmentService, userService, new ModelMapper());
        studio = new Type();
        studio.setType(TypeEnum.STUDIO);
        studio.setCapacity(3);
        studio.setDescription("studio");

        testUser = new UserEntity();
        adminRole = new Role();
        adminRole.setName(RolesEnum.ADMIN);
        testUser.setRole(List.of(adminRole));
        testUser.setUsername("testUser");
        testUser.setFirstName("test");
        testUser.setLastName("test");
        testUser.setPassword("test");
        testUser.setEmail("test@test.com");
        testUser.setPhone("+3598935467");

        testTown = new Town();
        testTown.setName("TestTown");
        testTown.setDescription("TestTown");
        testTown.setApartments(new ArrayList<>());

        testPicture=new Picture();
        testPicture.setTitle("first");
        testPicture.setUrl("first");
        testPicture.setUserName("first");
        testPicture.setPublicId("firstPublicId");

        testApartment = new Apartment();
        testApartment.setName("test");
        testApartment.setOwner(testUser);
        testApartment.setTown(testTown);
        testApartment.setAddress("Test adress");
        testApartment.setType(studio);
        testApartment.setPrice(BigDecimal.valueOf(50));
        testApartment.setPictures(List.of(testPicture));

        testLog = new Log();
        testLog.setApartment(testApartment);
        testLog.setUser(testUser);
        testLog.setDateTime(LocalDateTime.now());
        testLog.setAction("anyAction");
        logRepository.save(testLog);

    }

    @Test
    public  void testFindAll(){
        when(logRepository.findAll()).thenReturn(List.of(testLog));
        assertEquals("anyAction", logService.findAll().get(0).getAction());
    }
    
    @Test
    public void testDeleteAll(){
        logService.logClear();
        assertEquals(0, logService.findAll().size());
    }

}