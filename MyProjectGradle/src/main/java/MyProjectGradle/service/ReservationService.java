package MyProjectGradle.service;

import MyProjectGradle.models.entities.Reservation;
import MyProjectGradle.models.service.ReservationServiceModel;
import MyProjectGradle.models.views.ReservationDetailsViewModel;
import MyProjectGradle.models.views.ReservationViewModel;

import java.time.Period;
import java.util.List;

public interface ReservationService {
    List<Reservation> findAllApartmentsByName(String name);


    boolean addReservation(ReservationServiceModel reservationServiceModel);

    void deletePastReservations();

    List<ReservationViewModel> findAllReservationsByUsernameFilterByDate(String userIdentifier, String periodIdentifier);

    ReservationDetailsViewModel findByIdAndUsername(Long id, String userIdentifier);

    boolean canDelete(Long id, String userIdentifier);

    void deleteReservation(Long id);

    String findAllReservationsWithTotalProfit();

    Reservation findById(Long reservationId);
}
