package MyProjectGradle.service.impl;

import MyProjectGradle.models.entities.Reservation;
import MyProjectGradle.models.service.ReservationServiceModel;
import MyProjectGradle.models.views.ApartmentDetailsViewModel;
import MyProjectGradle.models.views.ReservationDetailsViewModel;
import MyProjectGradle.models.views.ReservationViewModel;
import MyProjectGradle.config.repository.ReservationRepository;

import MyProjectGradle.service.ReservationService;
import MyProjectGradle.service.UserService;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import javax.persistence.EntityNotFoundException;
import javax.transaction.Transactional;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Service
public class ReservationServiceImpl implements ReservationService {

    private final ReservationRepository reservationRepository;
    private final ModelMapper modelMapper;
    private final UserService userService;


    public ReservationServiceImpl(ReservationRepository reservationRepository, ModelMapper modelMapper, UserService userService) {
        this.reservationRepository = reservationRepository;
        this.modelMapper = modelMapper;
        this.userService = userService;

    }

    @Override
    public List<Reservation> findAllApartmentsByName(String name) {
        return reservationRepository.findAllByApartment_Name(name);
    }

    @Override
    public boolean addReservation(ReservationServiceModel reservationServiceModel) {
      try{  Reservation reservation = modelMapper.map(reservationServiceModel, Reservation.class);
          int days = reservation.getArrivalDate().until(reservation.getDepartureDate()).getDays();
          BigDecimal totalPrice = reservation.getApartment().getPrice().multiply(BigDecimal.valueOf(days));
          reservation.setPrice(totalPrice);
          reservationRepository.save(reservation);
       if(reservation.getId()==null){
           return false;
       }}catch (Exception e){
          return false;
      }
        return true;

    }

    @Override
    public void deletePastReservations() {
        LocalDate dateInPast=LocalDate.now().minusYears(1);
        reservationRepository.deleteReservationsByDepartureDateBefore(dateInPast);
    }

    @Transactional
    @Override
    public List<ReservationViewModel> findAllReservationsByUsernameFilterByDate(String userIdentifier, String period) {
        List<ReservationViewModel> reservationViewModelList = reservationRepository.findAllByUsername_Username(userIdentifier)
                .stream()
                .map(r ->{
                    ReservationViewModel reservationViewModel = modelMapper.map(r, ReservationViewModel.class);
                    ApartmentDetailsViewModel apartmentDetailsViewModel = modelMapper.map(r.getApartment(), ApartmentDetailsViewModel.class);
                    reservationViewModel.setApartment(apartmentDetailsViewModel);
                    return reservationViewModel;
                }).toList();
        if(period.equals("past")){
           return reservationViewModelList.stream().filter(r->r.getArrivalDate().isBefore(LocalDate.now())).toList();
        }else if(period.equals("today")){
            return reservationViewModelList.stream().filter(r->r.getArrivalDate().isEqual(LocalDate.now())).toList();
        }else{
            return reservationViewModelList.stream().filter(r->r.getArrivalDate().isAfter(LocalDate.now())).toList();
        }
    }

    @Transactional
    @Override
    public ReservationDetailsViewModel findByIdAndUsername(Long id, String userIdentifier) throws EntityNotFoundException{

            Reservation reservation = reservationRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("Reservation"));
                ReservationDetailsViewModel reservationView = modelMapper.map(reservation, ReservationDetailsViewModel.class);
                reservationView.setApartment(reservation.getApartment());
                reservationView.setPictureUrl(reservation.getApartment().getPictures().get(0).getUrl());
                return reservationView;

    }

    @Override
    public boolean canDelete(Long id, String username) {
        Reservation reservation = reservationRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("Reservation"));
        if((userService.isAdmin(username) || reservation.getUsername().getUsername().equals(username)) &&(reservation.getArrivalDate().isAfter(LocalDate.now().plusDays(1)))){
            return true;
        }else {return false;}
    }

    @Transactional
    @Override
    public void deleteReservation(Long id) {
            reservationRepository.deleteById(id);
    }

    @Override
    public String findAllReservationsWithTotalProfit() {
        int reservationsCount = reservationRepository.findAll().size();
        BigDecimal totalPrice = reservationRepository.findTotalPrice();
        return String.format("Reservation count %d - with total profit %.2f", reservationsCount, totalPrice);
    }

    @Override
    public Reservation findById(Long reservationId) {
        return reservationRepository.findById(reservationId).orElseThrow(()->new EntityNotFoundException("Reservation"));
    }
}
