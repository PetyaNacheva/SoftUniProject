package MyProjectGradle.service.impl;

import MyProjectGradle.models.entities.Picture;
import MyProjectGradle.models.service.PictureServiceModel;
import MyProjectGradle.config.repository.PictureRepository;
import MyProjectGradle.service.CloudinaryImage;
import MyProjectGradle.service.CloudinaryService;
import MyProjectGradle.service.PictureService;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.persistence.EntityNotFoundException;
import javax.transaction.Transactional;
import java.io.IOException;
import java.util.List;

@Service
public class PictureServiceImpl implements PictureService {

    private final PictureRepository pictureRepository;
    private final CloudinaryService cloudinaryService;
    private final ModelMapper modelMapper;

    public PictureServiceImpl(PictureRepository pictureRepository, CloudinaryService cloudinaryService, ModelMapper modelMapper) {
        this.pictureRepository = pictureRepository;
        this.cloudinaryService = cloudinaryService;
        this.modelMapper = modelMapper;
    }


    @Override
    public Picture createPicture(MultipartFile file, String title, String userName, String apartmentName) throws IOException {
        final CloudinaryImage upload = this.cloudinaryService.upload(file);
        PictureServiceModel pictureServiceModel = new PictureServiceModel();
        pictureServiceModel.setUser(userName);
        pictureServiceModel.setTitle(title);
        pictureServiceModel.setApartment(apartmentName);
        pictureServiceModel.setUrl(upload.getUrl());
        pictureServiceModel.setPublicId(upload.getPublicId());
        Picture picture = modelMapper.map(pictureServiceModel, Picture.class);
        picture.setUserName(userName);
        picture.setApartmentName(apartmentName);
       return pictureRepository.save(picture);

    }
    @Transactional
    @Override
    public void delete(String publicId) {
        cloudinaryService.delete(publicId);
        this.pictureRepository.deleteByPublicId(publicId);
    }

    @Override
    public List<Picture> findPictureByApartmentName(String apartmentName) {
       return pictureRepository.findAllByApartmentName(apartmentName);
    }

    @Transactional
    @Override
    public Picture findPictureByPublicId(String id) {
      //this is added here because of user profile picture
        cloudinaryService.delete(id);
       return pictureRepository.findByPublicId(id).orElse(null);
    }

    @Override
    public void UpdateApartmentName(Picture p, String name) {
        Picture pictureById = pictureRepository.findById(p.getId()).orElse(null);
        pictureById.setApartmentName(name);
        pictureRepository.save(pictureById);
    }

    @Override
    public Picture createProfilePicture(MultipartFile file, String title, String user) throws IOException {
        final CloudinaryImage upload = this.cloudinaryService.upload(file);
        PictureServiceModel pictureServiceModel = new PictureServiceModel();
        pictureServiceModel.setUser(user);
        pictureServiceModel.setTitle(title);
        pictureServiceModel.setUrl(upload.getUrl());
        pictureServiceModel.setPublicId(upload.getPublicId());
        Picture picture = modelMapper.map(pictureServiceModel, Picture.class);
        picture.setUserName(user);
        return pictureRepository.save(picture);
    }


    @Override
    public Picture findDefaultProfilePicture(String s) {
        return pictureRepository.findByTitle(s).orElseThrow(() -> new EntityNotFoundException("Picture"));
    }


    @Override
    public String findPictureByUsername(String username) {
        Picture picture = pictureRepository.findByUserName(username).orElseThrow(() -> new EntityNotFoundException("Picture"));
        return picture.getUrl();
    }

    @Override
    public void createDefaultProfilePicture(){
        if(pictureRepository.count()==0){
            Picture picture = new Picture("https://res.cloudinary.com/petia/image/upload/v1656522441/blank-profile-picture-973460_640_1_fqfwuw.png","blank-profile-picture-973460_640_1_fqfwuw", "defaultPicture", "admin");
           picture.setPublicId("blank-profile-picture-973460_640_1_fqfwuw");
            pictureRepository.save(picture);
        }

    }
}

