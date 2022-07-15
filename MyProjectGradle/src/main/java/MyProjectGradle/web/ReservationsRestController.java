package MyProjectGradle.web;


import MyProjectGradle.models.views.ApartmentSearchViewModel;
import MyProjectGradle.models.views.ReservationViewModel;
import MyProjectGradle.service.ReservationService;
import MyProjectGradle.service.impl.MySecurityUser;
import org.modelmapper.ModelMapper;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.transaction.Transactional;
import java.util.List;

@RestController
@RequestMapping("/reservations/api")
public class ReservationsRestController {
    private final ReservationService reservationService;
    private final ModelMapper modelMapper;

    public ReservationsRestController(ReservationService reservationService, ModelMapper modelMapper) {
        this.reservationService = reservationService;
        this.modelMapper = modelMapper;
    }

   /* @Transactional
    @GetMapping("/all")
    public ResponseEntity<List<ReservationViewModel>> getAllReservations(@AuthenticationPrincipal MySecurityUser principal){
       // List<ReservationViewModel> reservationsViewModel = reservationService.findAllNewReservationsByUsername(principal.getUserIdentifier());
        return null;// ResponseEntity.ok().body(reservationsViewModel);

    }*/
}
