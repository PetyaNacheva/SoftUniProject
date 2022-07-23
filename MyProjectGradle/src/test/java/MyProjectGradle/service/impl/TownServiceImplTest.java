package MyProjectGradle.service.impl;

import MyProjectGradle.models.binding.TownUpdateBindingModel;
import MyProjectGradle.models.entities.*;
import MyProjectGradle.models.enums.RolesEnum;
import MyProjectGradle.models.service.TownServiceModel;
import MyProjectGradle.models.views.TownDetailsViewModel;
import MyProjectGradle.repository.TownRepository;
import MyProjectGradle.repository.UserRepository;
import MyProjectGradle.service.PictureService;
import MyProjectGradle.service.RoleService;
import MyProjectGradle.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.stubbing.OngoingStubbing;
import org.modelmapper.ModelMapper;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.web.multipart.MultipartFile;

import javax.persistence.EntityNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class TownServiceImplTest {
    private UserEntity testUser;
    private UserEntity secondUser;
    private TownServiceModel townServiceModel;
    private TownUpdateBindingModel townUpdateBindingModel;
    private Town townTest;
    private Role userRole, adminRole, hostRole;
    private TownServiceImpl townService;
    private Picture pictureTest;
    private MultipartFile multipartFile;
    private final ModelMapper modelMapper= new ModelMapper();

    @Mock
    UserRepository mockUserRepository;
    @Mock
    TownRepository mockTownRepository;
    @Mock
    RoleService mockRoleService;
   @Mock
   MultipartFile mockMultipartFile;
    @Mock
    PictureService pictureService;
    @Mock
    UserService userService;


    @BeforeEach
    void setUp(){
        townService= new TownServiceImpl(mockTownRepository,pictureService,new ModelMapper(),userService);
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
        mockUserRepository.save(testUser);
        secondUser = new UserEntity();
        secondUser.setRole(List.of(userRole));
        secondUser.setUsername("user");
        secondUser.setFirstName("user");
        secondUser.setLastName("user");
        secondUser.setPassword("user");
        secondUser.setEmail("user@user.com");
        secondUser.setPhone("+3598935467");
        mockUserRepository.save(secondUser);
        townTest = new Town();
        townTest.setName("TestTown");
        townTest.setDescription("TestTown");
        townTest.setApartments(new ArrayList<>());
        pictureTest = new Picture();
        pictureTest.setTitle("test");
        pictureTest.setUserName("TestTown");
        pictureTest.setUrl("testUrl");
        pictureTest.setPublicId("testId");
        townTest.setPictureUrl(pictureTest);
        mockTownRepository.save(townTest);

        multipartFile = new MockMultipartFile("test", "testFileName", "testContentName",new byte[1]);

    }


    @Test
    public void testFindById(){
        when(mockTownRepository.findById(townTest.getId())).thenReturn(Optional.of(townTest));

        assertEquals("TestTown", townService.findById(townTest.getId()).getName());
    }

    @Test
    public void testFindByIdThrow(){
        assertThrows(EntityNotFoundException.class, () -> townService.findById(5L));
    }

    @Test
    public void testFindByName(){
        when(mockTownRepository.findByName(townTest.getName())).thenReturn(Optional.of(townTest));

        assertEquals("TestTown", townService.findByName(townTest.getName()).getName());
    }


    @Test
    public void testFindByNameShouldThrow(){
        String fakeTown = "FakeTown";
        when(mockTownRepository.findByName(fakeTown)).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () ->townService.findByName(fakeTown));
    }

    @Test
    public void testDeleteTown(){
        when(mockTownRepository.findById(townTest.getId())).thenReturn(Optional.of(townTest));
        townService.deleteTown(townTest.getId());
        Mockito.verify(mockTownRepository).delete(townTest);
    }


    @Test
    public void testDeleteTownShouldThrow(){
        when(mockTownRepository.findById(55L)).thenReturn(Optional.empty());;
        assertThrows(EntityNotFoundException.class,()->townService.deleteTown(55L));
    }

    @Test
    public void testUpdate() throws IOException {
        when(mockTownRepository.findById(townTest.getId())).thenReturn(Optional.of(townTest));
       // when(pictureService.findPictureByPublicId(townTest.getPictureUrl().getPublicId()));

        //when(townUpdateBindingModel).thenReturn(new TownUpdateBindingModel());
        townTest.setPictureUrl(pictureTest);
        townUpdateBindingModel = new TownUpdateBindingModel();
        townUpdateBindingModel.setName("TestingTesting");
        townUpdateBindingModel.setDescription("TestingTesting");
        townUpdateBindingModel.setPicture(mockMultipartFile);

        String user= testUser.getUsername().toString();
        pictureService.findPictureByPublicId(townTest.getPictureUrl().getPublicId());
        townTest.getPictureUrl().setTitle("");
        townService.updateTown(townTest.getId(),townUpdateBindingModel,user);
        assertEquals(townUpdateBindingModel.getName(), townTest.getName());
    }


    @Test
    public  void testCanDelete(){
        when(mockTownRepository.findById(townTest.getId())).thenReturn(Optional.of(townTest));
        when(userService.isAdmin(testUser.getUsername())).thenReturn(true);
        assertTrue(townService.canDelete(townTest.getId(), testUser.getUsername()));
    }

    @Test
    public void testCanDeleteFalse(){
        String fakeUser = "fakeUser";
        when(mockTownRepository.findById(townTest.getId())).thenReturn(Optional.of(townTest));
        when(userService.isAdmin(fakeUser)).thenReturn(false);
        assertFalse(townService.canDelete(townTest.getId(), fakeUser));
    }

     @Test
    public void testFindByTownName(){

        when(mockTownRepository.findByName(townTest.getName())).thenReturn(Optional.of(townTest));

         assertEquals(townService.findByTownName(townTest.getName()).getName(), townTest.getName());
    }

    @Test
    public void testFindTownDetailsViewModelBy(){
        when(mockTownRepository.findById(townTest.getId())).thenReturn(Optional.of(townTest));

        TownDetailsViewModel townDetailsViewModelBy = townService.findTownDetailsViewModelBy(townTest.getId());

        assertEquals(townDetailsViewModelBy.getDescription(), townTest.getDescription());
    }

/*    @Test
    public void testSave(){
        when(mockUserRepository.findByUsername(testUser.getUsername())).thenReturn(Optional.of(testUser));
        when(mockTownRepository.findAll()).thenReturn(List.of(townTest));

        townServiceModel = new TownServiceModel();
        townServiceModel.setPicture(mockMultipartFile);
        townServiceModel.setName("NewTown");
        townServiceModel.setDescription("New best Town");
        townServiceModel.setId(2L);
      //  Town newTown = new Town("NewTown", "New best Town");
        townService.saveTown(townServiceModel, testUser.getUsername());



        assertEquals("NewTown",townService.findById(2L).getName());

    }*/

}