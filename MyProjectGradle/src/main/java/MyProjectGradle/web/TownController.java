package MyProjectGradle.web;

import MyProjectGradle.models.binding.TownAddBindingModel;
import MyProjectGradle.models.binding.TownUpdateBindingModel;
import MyProjectGradle.models.entities.Apartment;
import MyProjectGradle.models.service.ApartmentServiceModel;
import MyProjectGradle.models.service.TownServiceModel;
import MyProjectGradle.models.views.ApartmentDetailsViewModel;
import MyProjectGradle.models.views.TownViewModel;
import MyProjectGradle.models.views.TownDetailsViewModel;
import MyProjectGradle.service.TownService;
import MyProjectGradle.service.UserService;
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
import java.util.List;

@Controller
@RequestMapping("/towns")
public class TownController {
    private final TownService townService;
    private final ModelMapper modelMapper;
    private final UserService userService;

    public TownController(TownService townService, ModelMapper modelMapper, UserService userService) {
        this.townService = townService;
        this.modelMapper = modelMapper;
        this.userService = userService;
    }

    @GetMapping("/add")
    public String addTown(Model model){
            model.addAttribute("towns", townService.getTowns());
            if (!model.containsAttribute("townAddBindingModel")) {
                model.addAttribute("townAddBindingModel", new TownAddBindingModel());
                model.addAttribute("isExist", false);
            }
            return "town-add";

    }

    @PostMapping("/add")
    public String addTownConfirmed(@Valid TownAddBindingModel townAddBindingModel, BindingResult bindingResult, RedirectAttributes redirectAttributes, @AuthenticationPrincipal MySecurityUser principal){

            if (bindingResult.hasErrors()) {
                redirectAttributes.addFlashAttribute("townAddBindingModel", townAddBindingModel);
                redirectAttributes.addFlashAttribute("org.springframework.validation.BindingResult.townAddBindingModel", bindingResult);
                return "redirect:add";
            }
            boolean isSaved = townService.saveTown(modelMapper.map(townAddBindingModel, TownServiceModel.class), principal.getUserIdentifier());
            if (!isSaved) {
                redirectAttributes.addFlashAttribute("townAddBindingModel", townAddBindingModel);
                redirectAttributes.addFlashAttribute("isExist", true);
                return "redirect:add";
            }
            return "redirect:/";
    }

    @GetMapping("/all")
    public String allTowns(Model model){
            List<TownViewModel> towns = townService.getAllTowns();
            if(!model.containsAttribute("towns")){
                model.addAttribute("towns", towns);
            }
        return  "allTowns";
    }

    @GetMapping("/{id}/details")
    public String details(@PathVariable Long id, Model model, @AuthenticationPrincipal MySecurityUser principal) {
        TownDetailsViewModel townsDetailsViewModel = townService.findTownDetailsViewModelBy(id);
        if (userService.isAdmin(principal.getUserIdentifier())){
            townsDetailsViewModel.setCanUpdate(true);
        }
        if(townService.canDelete(id, principal.getUserIdentifier())){
            townsDetailsViewModel.setCanDelete(true);
        }
            if (!model.containsAttribute("town")) {
                model.addAttribute("town", townsDetailsViewModel);
                return "town-details";
            }
        return "errors/error403";
    }

    @Transactional
    @GetMapping("/{id}/update")
    public String getUpdate(@PathVariable Long id, Model model, @AuthenticationPrincipal MySecurityUser principal) {
        if (!model.containsAttribute("town")&& userService.isAdmin(principal.getUserIdentifier())) {
            TownDetailsViewModel townsDetailsViewModel = townService.findTownDetailsViewModelBy(id);
            townsDetailsViewModel.setCanUpdate(true);
            model.addAttribute("town", townsDetailsViewModel);
            return "town-update";
        }
        return "errors/error403";
    }

    @Transactional
    @PatchMapping("/{id}/update")
    public String townUpdate(@PathVariable Long id, @Valid TownUpdateBindingModel townUpdateBindingModel,
                                   @AuthenticationPrincipal MySecurityUser principal,
                                   BindingResult bindingResult,
                                   RedirectAttributes redirectAttributes) throws IOException {

        if(bindingResult.hasErrors()){
            redirectAttributes.addFlashAttribute("townUpdateBindingModel", townUpdateBindingModel);
            redirectAttributes.addFlashAttribute("org.springframework.validation.BindingResult.townUpdateBindingModel", townUpdateBindingModel);
            return "redirect:town-update";
        }
         townService.updateTown(id, townUpdateBindingModel, principal.getUserIdentifier());
        return "redirect:/towns/all";
    }

    @Transactional
    @DeleteMapping("/{id}/delete")
    public String townDelete(@PathVariable Long id, @AuthenticationPrincipal MySecurityUser principal){
        if (townService.canDelete(id, principal.getUserIdentifier())) {
            townService.deleteTown(id);
        }
        return "redirect:/towns/all";
    }


}
