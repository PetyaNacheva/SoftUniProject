package MyProjectGradle.config.repository;


import MyProjectGradle.models.entities.Reservation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Repository
public interface ReservationRepository extends JpaRepository<Reservation, Long> {

    List<Reservation> findAllByApartment_Name(String name);

    void deleteReservationsByDepartureDateBefore(LocalDate dateInPast);
    List<Reservation> findAllByUsername_Username(String username);
    void deleteById(Long id);
    @Query(value = "select sum(r.price) from Reservation as r")
    BigDecimal findTotalPrice();


}
