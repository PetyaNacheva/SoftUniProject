package MyProjectGradle.service;

import MyProjectGradle.models.entities.Apartment;
import MyProjectGradle.models.entities.Picture;
import MyProjectGradle.models.entities.UserEntity;
import MyProjectGradle.models.service.PictureServiceModel;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

public interface PictureService {

    Picture createPicture(MultipartFile file, String title, String user, String apartment) throws IOException;
    Picture createProfilePicture(MultipartFile file, String title, String user) throws IOException;

    void delete(String publicId);

   List<Picture> findPictureByApartmentName(String apartmentName);

    Picture findPictureByPublicId(String id);

    void UpdateApartmentName(Picture p, String name);

    Picture findDefaultProfilePicture(String s);


    void createDefaultProfilePicture();

    String findPictureByUsername(String username);
}
