package MyProjectGradle.web;

import MyProjectGradle.models.entities.Apartment;
import MyProjectGradle.models.entities.Reservation;
import MyProjectGradle.models.entities.UserEntity;
import MyProjectGradle.models.views.*;
import MyProjectGradle.service.ApartmentService;
import MyProjectGradle.service.UserService;
import MyProjectGradle.service.impl.MySecurityUser;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.transaction.Transactional;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/apartments/api")
public class ApartmentRestController {
    private final ApartmentService apartmentService;
    private final ModelMapper modelMapper;
    private final UserService userService;


    public ApartmentRestController(ApartmentService apartmentService, ModelMapper modelMapper, UserService userService) {
        this.apartmentService = apartmentService;
        this.modelMapper = modelMapper;
        this.userService = userService;
    }
    @Transactional
    @GetMapping("/all")
    public ResponseEntity<List<ApartmentSearchViewModel>> getAllApartments(){
        List<ApartmentSearchViewModel> apartmentViewModels = apartmentService.getAllApartments()
                .stream().map(a ->{
                    ApartmentSearchViewModel apartmentSearchViewModel = modelMapper.map(a, ApartmentSearchViewModel.class);
                    apartmentSearchViewModel.setPicture(a.getPictures().get(0).getUrl());
                    apartmentSearchViewModel.setTown(a.getTown().getName());
                    apartmentSearchViewModel.setType(a.getType().getType().name());
                    return apartmentSearchViewModel;
                }).toList();
       return ResponseEntity.ok().body(apartmentViewModels);
    }

    @Transactional
    @GetMapping("/statistic/{id}")
    public ResponseEntity<ApartmentStatisticViewModel>getStatistic(@PathVariable Long id, @AuthenticationPrincipal MySecurityUser principal){
        Apartment apartment = apartmentService.findApartmentById(id);
        UserEntity user = userService.findByUsername(principal.getUserIdentifier());
        if(user.getUsername().equals(apartment.getOwner().getUsername())|| userService.isAdmin(principal.getUserIdentifier())) {
            BigDecimal futureProfit = BigDecimal.valueOf(0);
            BigDecimal pastProfit = BigDecimal.valueOf(0);
            List<ReservationStatViewModel> pastReservations = new ArrayList<>();
            List<ReservationStatViewModel> futureReservations = new ArrayList<>();
            for (Reservation reservation : apartment.getReservations()) {
                if (reservation.getArrivalDate().isAfter(LocalDate.now().minusDays(1)) && reservation.getArrivalDate().isBefore(LocalDate.now().plusDays(31))) {
                    futureReservations.add(modelMapper.map(reservation, ReservationStatViewModel.class));
                    futureProfit = futureProfit.add(reservation.getPrice());
                } else if (reservation.getArrivalDate().isBefore(LocalDate.now()) && reservation.getArrivalDate().isAfter(LocalDate.now().minusDays(31))) {
                    pastProfit.add(reservation.getPrice());
                    pastReservations.add(modelMapper.map(reservation, ReservationStatViewModel.class));
                }
            }
            ApartmentStatisticViewModel apartmentStatisticViewModel = modelMapper.map(apartment, ApartmentStatisticViewModel.class);
            apartmentStatisticViewModel.setName(apartment.getName());

            apartmentStatisticViewModel.setComingReservations(futureReservations);
            apartmentStatisticViewModel.setPast30DaysReservations(pastReservations);
            apartmentStatisticViewModel.setProfitForFutureMonth(futureProfit);
            apartmentStatisticViewModel.setProfitFromPastMonth(pastProfit);

            return ResponseEntity.ok().body(apartmentStatisticViewModel);
        }else{
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
    }

}
