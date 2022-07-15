package MyProjectGradle.web;

import MyProjectGradle.models.binding.PictureAddBindingModel;
import MyProjectGradle.models.entities.Apartment;
import MyProjectGradle.models.entities.Picture;
import MyProjectGradle.models.entities.UserEntity;
import MyProjectGradle.service.ApartmentService;
import MyProjectGradle.service.PictureService;
import MyProjectGradle.service.UserService;
import MyProjectGradle.service.impl.MySecurityUser;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.transaction.Transactional;
import java.io.IOException;
import java.util.List;

@Controller
@RequestMapping("/pictures")
public class PictureController {
    private final ApartmentService apartmentService;
    private final UserService userService;
    private final PictureService pictureService;

    public PictureController(ApartmentService apartmentService, UserService userService, PictureService pictureService) {
        this.apartmentService = apartmentService;
        this.userService = userService;
        this.pictureService = pictureService;
    }


    @GetMapping("/add/{id}")
    public String pictureAdd(@PathVariable Long id, @AuthenticationPrincipal MySecurityUser principal, Model model){
        Apartment apartment = apartmentService.findApartmentById(id);
        UserEntity byUsername = userService.findByUsername(principal.getUserIdentifier());
        if(!model.containsAttribute("pictureAddBindingModel") &&(userService.isAdmin(principal.getUserIdentifier())||apartment.getOwner().getUsername().equals(principal.getUserIdentifier()))){
            PictureAddBindingModel pictureAddBindingModel = new PictureAddBindingModel();
            pictureAddBindingModel.setUsername(byUsername.getUsername());
            pictureAddBindingModel.setApartmentName(apartment.getName());
            model.addAttribute("pictureAddBindingModel", pictureAddBindingModel);
            model.addAttribute("apartmentId", apartment.getId());

        return "picture-add-form";
    }else {
            return "errors/error403";
        }
    }
    @Transactional
    @PostMapping("/add/{id}")
    public String pictureAddConfirmed(@PathVariable Long id, @AuthenticationPrincipal MySecurityUser principal,
                                      PictureAddBindingModel pictureAddBindingModel, BindingResult bindingResult,
                                      RedirectAttributes redirectAttributes) throws IOException {
        Apartment apartment = apartmentService.findApartmentById(id);
        if(bindingResult.hasErrors()){
        redirectAttributes.addFlashAttribute("pictureAddBindingModel", pictureAddBindingModel);
        redirectAttributes.addFlashAttribute("org.springframework.validation.BindingResult.pictureAddBindingModel", bindingResult);
        return "redirect:/apartments/" + id +"/details";
    }
    MultipartFile picture = pictureAddBindingModel.getPicture();
        Picture pictureFile = pictureService.createPicture(picture, picture.getOriginalFilename(), principal.getUserIdentifier(), apartment.getName());
        List<Picture> pictures = apartment.getPictures();
        pictures.add(pictureFile);
        apartment.setPictures(pictures);
        apartmentService.updateApartmentPictures(apartment);
        
    return  "redirect:/apartments/getMy";
    }

    @Transactional
    @GetMapping("/delete/{id}")
    public String delete(@PathVariable String id, @AuthenticationPrincipal MySecurityUser principal) {
        Picture picture = pictureService.findPictureByPublicId(id);
        if(userService.isAdmin(principal.getUserIdentifier())||picture.getUserName().equals(principal.getUserIdentifier())) {
            Apartment apartment = apartmentService.findByApartmentByApartmentName(picture.getApartmentName());
            List<Picture> pictures = apartment.getPictures();
            if(pictures.size()>1) {
                pictures.removeIf(picture1 -> picture1.getUrl().equals(picture.getUrl()));
                apartment.setPictures(pictures);
                apartmentService.updateApartmentPictures(apartment);
                pictureService.delete(id);
            }
            return "redirect:/apartments/getMy";
        }else {
            return "errors/error403";
        }
    }
}
