package MyProjectGradle.web;

import MyProjectGradle.models.binding.ApartmentAddBindingModel;
import MyProjectGradle.models.binding.ApartmentUpdateBindingModel;
import MyProjectGradle.models.binding.ReservationCreateBindingModel;
import MyProjectGradle.models.entities.*;
import MyProjectGradle.models.service.ApartmentServiceModel;
import MyProjectGradle.models.service.ReservationServiceModel;
import MyProjectGradle.models.views.ApartmentDetailsViewModel;
import MyProjectGradle.models.views.ApartmentViewModel;
import MyProjectGradle.service.*;
import MyProjectGradle.service.impl.MySecurityUser;
import org.modelmapper.ModelMapper;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.transaction.Transactional;
import javax.validation.Valid;
import java.io.IOException;
import java.time.LocalDate;
import java.util.List;

@Controller
@RequestMapping("/apartments")
public class ApartmentController {
private final ApartmentService apartmentService;
private final ModelMapper modelMapper;
private final TownService townService;
private final UserService userService;
private final PictureService pictureService;
private final ReservationService reservationService;


    public ApartmentController(ApartmentService apartmentService, ModelMapper modelMapper, TownService townService, UserService userService, PictureService pictureService, ReservationService reservationService) {
        this.apartmentService = apartmentService;
        this.modelMapper = modelMapper;
        this.townService = townService;
        this.userService = userService;
        this.pictureService = pictureService;
        this.reservationService = reservationService;
    }

    @ModelAttribute("reservationCreateBindingModel")
    public ReservationCreateBindingModel reservationCreateBindingModel() {
        return new ReservationCreateBindingModel();
    }


    @GetMapping("/add")
    public String add(Model model){
        model.addAttribute("towns", townService.getTowns());
        if(!model.containsAttribute("apartmentAddBindingModel")){
            model.addAttribute("apartmentAddBindingModel", new ApartmentAddBindingModel());
            model.addAttribute("isExist", false);
        }

        return "add-apartment";
    }

    @PostMapping("/add")
    public String addApartment(@Valid ApartmentAddBindingModel apartmentAddBindingModel, BindingResult bindingResult, RedirectAttributes redirectAttributes, @AuthenticationPrincipal MySecurityUser principal) throws IOException {

        if(bindingResult.hasErrors()){
            redirectAttributes.addFlashAttribute("apartmentAddBindingModel", apartmentAddBindingModel);
            redirectAttributes.addFlashAttribute("org.springframework.validation.BindingResult.apartmentAddBindingModel", bindingResult);
            return "redirect:add";
        }
        boolean isSaved = apartmentService.saveApartment(modelMapper.map(apartmentAddBindingModel, ApartmentServiceModel.class), principal.getUserIdentifier());
        if (!isSaved){
            redirectAttributes.addFlashAttribute("apartmentAddBindingModel", apartmentAddBindingModel);
            redirectAttributes.addFlashAttribute("isExist", true);
            return "redirect:add";
        }

        return "redirect:getMy";
    }



    @GetMapping("/getMy")
    public String getMyApartments(@AuthenticationPrincipal MySecurityUser principal, Model model){

        UserEntity user = userService.findByUsername(principal.getUserIdentifier());
        List<ApartmentViewModel> myApartments = apartmentService.findAllApartmentsByUsername(user);
        model.addAttribute("myApartments", myApartments);
        return "my-apartments";
    }

    @GetMapping("/{id}/details")
    public String showApartment(@PathVariable Long id, Model model, @AuthenticationPrincipal MySecurityUser principal){
        ApartmentDetailsViewModel apartment = apartmentService.findById(id);
      if (apartment.getOwner().equals(principal.getUserIdentifier())||userService.isAdmin(principal.getUserIdentifier())) {
            apartment.setCanDelete(true);
            apartment.setCanUpdate(true);
            apartment.setCanReserve(false);
      }
          if(!model.containsAttribute("apartment")){

              model.addAttribute("apartment", apartment);
              model.addAttribute("canDelete", apartmentService.canDelete(apartment.getId(), principal.getUserIdentifier()));
          return "apartment-details";
        }
          if(!model.containsAttribute("isAvailable")){
              model.addAttribute("isAvailable", true);
          }
        return "errors/error401";
    }
    @Transactional
    @PostMapping("/reserve/{id}")
    public String makeReservation(@Valid ReservationCreateBindingModel reservationCreateBindingModel,
                              BindingResult bindingResult,
                              RedirectAttributes redirectAttributes,
                              @PathVariable Long id,
                              @AuthenticationPrincipal MySecurityUser principal) {
        ApartmentDetailsViewModel apartmentDetailsViewModel = apartmentService.findApartmentDetailsViewModelById(id);
        if (bindingResult.hasErrors()|| reservationCreateBindingModel.getNumberOfGuests()>apartmentDetailsViewModel.getCapacity()) {
            redirectAttributes.addFlashAttribute("reservationCreateBindingModel", reservationCreateBindingModel);
            redirectAttributes.addFlashAttribute("org.springframework.validation.BindingResult.reservationCreateBindingModel", bindingResult);
            return "redirect:/apartments/"+id+"/details";
        }
        String isAvailable=apartmentService.isAvailable(apartmentDetailsViewModel.getName(), reservationCreateBindingModel.getArrivalDate(), reservationCreateBindingModel.getDepartureDate());
        if(!isAvailable.equals("available")){
            redirectAttributes.addFlashAttribute("reservationCreateBindingModel", reservationCreateBindingModel);
            redirectAttributes.addFlashAttribute("isReserved", true);
            redirectAttributes.addFlashAttribute("nextAvailableDates", isAvailable);
            return "redirect:/apartments/"+id+"/details";
        }

        ReservationServiceModel reservationServiceModel = modelMapper.map(reservationCreateBindingModel, ReservationServiceModel.class);
        reservationServiceModel.setApartment(apartmentService.findApartmentById(id));
        reservationServiceModel.setReservedOn(LocalDate.now());
        reservationServiceModel.setUsername(userService.findByUsername(principal.getUserIdentifier()));

        boolean isSaved= reservationService.addReservation(reservationServiceModel);
        if (!isSaved) {
            redirectAttributes.addFlashAttribute("reservationCreateBindingModel", reservationCreateBindingModel);
            return "redirect:/apartments/"+id+"/details";
        }
        return "redirect:/reservations/getMy";
    }

    @DeleteMapping("/{id}/delete")
    public String deleteApartment(@PathVariable Long id, @AuthenticationPrincipal MySecurityUser principal) {
        if (apartmentService.canDelete(id, principal.getUserIdentifier())) {
            apartmentService.deleteApartment(id);
            }
        return "redirect:/apartments/getMy";
    }

    @Transactional
    @GetMapping("/{id}/update")
    public String getUpdate(@PathVariable Long id, Model model, @AuthenticationPrincipal MySecurityUser principal) {
        ApartmentServiceModel apartment = modelMapper.map(apartmentService.findApartmentById(id), ApartmentServiceModel.class);
        if (!model.containsAttribute("apartmentViewModel")&& apartmentService.canUpdate(apartment.getId(), principal.getUserIdentifier())) {
           ApartmentDetailsViewModel apartmentViewModel = apartmentService.findApartmentDetailsViewModelById(id);
            apartmentViewModel.setCanUpdate(true);
            model.addAttribute("apartmentViewModel", apartmentViewModel);
    }
        return "apartment-update";
    }
    @Transactional
    @PatchMapping("/{id}/update")
    public String updateApartment(@PathVariable Long id, @Valid ApartmentUpdateBindingModel apartmentUpdateBindingModel,
                                  @AuthenticationPrincipal MySecurityUser principal,
                                  BindingResult bindingResult,
                                  RedirectAttributes redirectAttributes){
        if(bindingResult.hasErrors()){
            redirectAttributes.addFlashAttribute("apartmentUpdateBindingModel", apartmentUpdateBindingModel);
            redirectAttributes.addFlashAttribute("org.springframework.validation.BindingResult.apartmentUpdateBindingModel", bindingResult);
            return "redirect:/" + id+"/update";
        }

        Apartment byApartmentName =  apartmentService.findApartmentById(id);;
        if(byApartmentName!=null){
            redirectAttributes.addFlashAttribute("apartmentUpdateBindingModel", apartmentUpdateBindingModel);
            redirectAttributes.addFlashAttribute("isExist", true);
        }
        ApartmentDetailsViewModel apartmentView = apartmentService.findById(id);
        if (apartmentService.canUpdate(apartmentView.getId(), principal.getUserIdentifier())) {
            ApartmentServiceModel apartment = modelMapper.map(apartmentUpdateBindingModel, ApartmentServiceModel.class);
            apartmentService.updateApartment(apartment);
        }
        return "redirect:/apartments/"+id+"/details";
    }


}
