package MyProjectGradle.service.impl;

import MyProjectGradle.models.binding.TownUpdateBindingModel;
import MyProjectGradle.models.entities.*;
import MyProjectGradle.models.service.TownServiceModel;
import MyProjectGradle.models.views.TownViewModel;
import MyProjectGradle.models.views.TownDetailsViewModel;
import MyProjectGradle.repository.TownRepository;
import MyProjectGradle.service.PictureService;
import MyProjectGradle.service.TownService;
import MyProjectGradle.service.UserService;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.persistence.EntityNotFoundException;
import javax.transaction.Transactional;
import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class TownServiceImpl implements TownService {
    private final TownRepository townRepository;
    private final PictureService pictureService;
    private final ModelMapper modelMapper;
    private final UserService userService;

    public TownServiceImpl(TownRepository townRepository, PictureService pictureService, ModelMapper modelMapper, UserService userService) {
        this.townRepository = townRepository;
        this.pictureService = pictureService;
        this.modelMapper = modelMapper;
        this.userService = userService;
    }
    

    @Override
    public boolean saveTown(TownServiceModel townServiceModel, String userIdentifier) throws IOException {
           Town town = modelMapper.map(townServiceModel, Town.class);
            UserEntity user =userService.findByUsername(userIdentifier);
            MultipartFile picture = townServiceModel.getPicture();
            Picture pictureFile = pictureService.createPicture(picture, picture.getOriginalFilename(), user.getUsername(), town.getName());
            town.setPictureUrl(pictureFile);
            townRepository.save(town);
       if(town.getId()==null){
            return false;
        }
        return true;
    }

    @Override
    public Town findByName(String town) {
        return townRepository.findByName(town).orElseThrow(() -> new EntityNotFoundException("Town"));
    }


    @Override
    public List<TownViewModel> getAllTowns() {
       return townRepository.findAll().stream().map(t-> modelMapper.map(t, TownViewModel.class)).collect(Collectors.toList());
    }

    @Override
    public List<String> getTowns(){
        return townRepository.findAll().stream().map(Town::getName).collect(Collectors.toList());
    }

    @Override
    public Town findById(Long id) {
        return townRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("Town"));
       }

       @Transactional
    @Override
    public List<TownViewModel> getTop3TownsWithMostApartments() {
        // TODO: 7/1/2022 to implement cash function for better functionality
      return townRepository.findByAndApartments().stream()/*.sorted(Comparator.comparing(town -> town.getApartments().size()))*/.limit(3).map(t->{
         TownViewModel townViewModel = modelMapper.map(t, TownViewModel.class);
         townViewModel.setPictureUrl(t.getPictureUrl().getUrl());
          return townViewModel;
      }).collect(Collectors.toList());
    }

    @Override
    public TownDetailsViewModel findTownDetailsViewModelBy(Long id) {
        Town town = townRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("Apartment"));
        TownDetailsViewModel townsDetailsViewModel = modelMapper.map(town, TownDetailsViewModel.class);
        townsDetailsViewModel.setPicture(town.getPictureUrl());
        return townsDetailsViewModel;
    }

    @Override
    public TownServiceModel findByTownName(String name) {
       return townRepository.findByName(name).map(t ->modelMapper.map(t,TownServiceModel.class)).orElse(null);
    }
    @Transactional
    @Override
    public void updateTown(Long id, TownUpdateBindingModel townUpdateBindingModel, String userIdentifier) throws IOException {
        Town town = townRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("Town"));
        town.setName(townUpdateBindingModel.getName());
        town.setDescription(townUpdateBindingModel.getDescription());
        if(!townUpdateBindingModel.getPicture().isEmpty()) {
            if(!town.getPictureUrl().getTitle().isEmpty()){
                Picture oldPicture = pictureService.findPictureByPublicId(town.getPictureUrl().getPublicId());
                pictureService.delete(oldPicture.getPublicId());
            }
            MultipartFile picture = townUpdateBindingModel.getPicture();
            Picture pictureFile = pictureService.createPicture(picture, picture.getOriginalFilename(), userIdentifier, townUpdateBindingModel.getName());
           town.setPictureUrl(pictureFile);
        }
         townRepository.save(town);
    }

    @Transactional
    @Override
    public boolean canDelete(Long id, String username) {
        Town town = townRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("Town"));
        return userService.isAdmin(username) && town != null&&town.getApartments().size()==0;
    }

    @Transactional
    @Override
    public boolean canUpdate(Long id, String username) {
        Town town = townRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("Town"));
        return userService.isAdmin(username) && town != null&&town.getApartments().size()==0;
    }
    @Transactional
    @Override
    public void deleteTown(Long id) {
        Town town = townRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("Town"));
        Picture picture = pictureService.findPictureByPublicId(town.getPictureUrl().getPublicId());
            /*pictureService.delete(picture.getPublicId());*/
            townRepository.delete(town);
    }
}
