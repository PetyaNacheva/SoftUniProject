package MyProjectGradle.web;

import MyProjectGradle.models.binding.ReservationCreateBindingModel;
import MyProjectGradle.models.entities.UserEntity;
import MyProjectGradle.models.views.ApartmentDetailsViewModel;
import MyProjectGradle.models.views.ReservationDetailsViewModel;
import MyProjectGradle.models.views.ReservationViewModel;
import MyProjectGradle.service.ReservationService;
import MyProjectGradle.service.UserService;
import MyProjectGradle.service.impl.MySecurityUser;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.transaction.Transactional;
import java.time.LocalDate;
import java.util.List;

@Controller
@RequestMapping("/reservations")
public class ReservationController {
private final ReservationService reservationService;
private final UserService userService;

    public ReservationController(ReservationService reservationService, UserService userService) {
        this.reservationService = reservationService;
        this.userService = userService;
    }

    @Transactional
    @GetMapping("/getMy")
    public String getAll(Model model, @AuthenticationPrincipal MySecurityUser principal){

        if(!model.containsAttribute("reservations")){
            model.addAttribute("futureReservations", reservationService.findAllReservationsByUsernameFilterByDate(principal.getUserIdentifier(), "future"));
            model.addAttribute("reservationArrivedToday", reservationService.findAllReservationsByUsernameFilterByDate(principal.getUserIdentifier(), "today"));
            model.addAttribute("oldReservations", reservationService.findAllReservationsByUsernameFilterByDate(principal.getUserIdentifier(), "past"));
        }
        return "my-reservations";
    }

    @Transactional
    @GetMapping("/{id}/details")
    public String showReservation(@PathVariable Long id, Model model, @AuthenticationPrincipal MySecurityUser principal){
        ReservationDetailsViewModel reservation = reservationService.findByIdAndUsername(id, principal.getUserIdentifier());
           if (reservation.getUsername().equals(principal.getUserIdentifier()) || userService.isAdmin(principal.getUserIdentifier())) {
               reservation.setCanUpdate(true);

           if (reservationService.canDelete(reservation.getId(), principal.getUserIdentifier())) {
               reservation.setCanDelete(true);
           }
           if (!model.containsAttribute("reservation")) {
               model.addAttribute("reservation", reservation);
               model.addAttribute("canDelete", reservation.getCanDelete());
               return "reservation-details";
           }
       }
        return "errors/error401";
    }

    @Transactional
    @DeleteMapping("/{id}/delete")
    public String deleteReservation(@PathVariable Long id, @AuthenticationPrincipal MySecurityUser principal){
       if(reservationService.canDelete(id, principal.getUserIdentifier())) {
           reservationService.deleteReservation(id);
       }
        return "redirect:/reservations/getMy";
    }
}
