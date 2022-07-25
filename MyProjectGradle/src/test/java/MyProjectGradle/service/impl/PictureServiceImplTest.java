package MyProjectGradle.service.impl;

import MyProjectGradle.models.entities.Picture;
import MyProjectGradle.models.entities.Role;
import MyProjectGradle.models.entities.UserEntity;
import MyProjectGradle.models.enums.RolesEnum;
import MyProjectGradle.repository.PictureRepository;
import MyProjectGradle.service.CloudinaryImage;
import MyProjectGradle.service.CloudinaryService;
import com.cloudinary.Cloudinary;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;
import org.modelmapper.internal.bytebuddy.pool.TypePool;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.lenient;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class PictureServiceImplTest {
    private PictureServiceImpl pictureService;
    private final ModelMapper modelMapper = new ModelMapper();
    private CloudinaryServiceImpl testCloudinaryService;
    private Picture testPicture;
    private final Cloudinary cloudinary = new Cloudinary();
    private MultipartFile multipartFile;
    private final CloudinaryImage cloudinaryImage = new CloudinaryImage();
    private UserEntity testUser;
    private Role userRole, adminRole;

    @Mock
    PictureRepository pictureRepository;
    @Mock
    CloudinaryService cloudinaryService;

    @BeforeEach
    void setUp(){

        pictureService = new PictureServiceImpl(pictureRepository, cloudinaryService, new ModelMapper());
        testPicture = new Picture();
        testPicture.setTitle("testPicture");
        testPicture.setApartmentName("testApartment");
        testPicture.setUrl("anyUrl");
        testPicture.setPublicId("publicId");
        testPicture.setUserName("admin");
        testPicture.setId(1L);

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


        pictureRepository.save(testPicture);


        multipartFile = new MockMultipartFile("test", "testFileName", "testContentName",new byte[1]);

    }

    @Test
    public void testFindPictureByApartmentName(){
        when(pictureRepository.findAllByApartmentName("test")).thenReturn(List.of(testPicture));

        assertEquals(1, pictureService.findPictureByApartmentName("test").size());
    }

    @Test
    public void testFindPictureByPublicId(){
        when(pictureRepository.findByPublicId("publicId")).thenReturn(Optional.of(testPicture));
       //cloudinaryService = new CloudinaryServiceImpl(new Cloudinary());
        assertEquals("testPicture", pictureService.findPictureByPublicId("publicId").getTitle());
    }

    @Test
    public void testUpdateApartmentName(){
        when(pictureRepository.findById(1L)).thenReturn(Optional.of(testPicture));
         pictureService.UpdateApartmentName(testPicture, "differentName");
        assertEquals("differentName",testPicture.getApartmentName());
    }
    @Test
    public void testCreateProfilePicture() throws IOException {
        //lenient().when(pictureRepository.findAll()).thenReturn(List.of(testPicture));
        when(cloudinaryService.upload(multipartFile)).thenReturn(cloudinaryImage);
       Picture profilePicture = new Picture();
        profilePicture.setId(2L);
        profilePicture.setUserName(testUser.getUsername());
        profilePicture.setUrl("profileUrl");
        profilePicture.setTitle("profilePicture");
        profilePicture.setPublicId("profilePublicId");
        profilePicture.setApartmentName("testApartment");
        profilePicture.setUserName(testUser.getUsername());
        pictureService.createProfilePicture(multipartFile, "profilePicture", "admin");
        assertEquals("profilePicture", profilePicture.getTitle());
    }

    @Test
    public void testFindDefaultProfilePicture(){
        when(pictureRepository.findByTitle(testPicture.getTitle())).thenReturn(Optional.of(testPicture));
        assertEquals("testPicture",pictureService.findDefaultProfilePicture(testPicture.getTitle()).getTitle());
    }

    @Test
    public void testFindPictureByUsername(){
        when(pictureRepository.findByUserName(testUser.getUsername())).thenReturn(Optional.of(testPicture));

        assertEquals("anyUrl", pictureService.findPictureByUsername(testUser.getUsername()));
    }


}