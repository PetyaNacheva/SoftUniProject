package MyProjectGradle.models.views;

import MyProjectGradle.models.entities.Reservation;

import java.math.BigDecimal;
import java.util.List;

public class ApartmentStatisticViewModel {
    private Long id;
    private String name;
    private BigDecimal profitFromPastMonth;
    private BigDecimal profitForFutureMonth;
    private List<ReservationStatViewModel> comingReservations;
    private List<ReservationStatViewModel> past30DaysReservations;

    public ApartmentStatisticViewModel() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public BigDecimal getProfitFromPastMonth() {
        return profitFromPastMonth;
    }

    public void setProfitFromPastMonth(BigDecimal profitFromPastMonth) {
        this.profitFromPastMonth = profitFromPastMonth;
    }

    public BigDecimal getProfitForFutureMonth() {
        return profitForFutureMonth;
    }

    public void setProfitForFutureMonth(BigDecimal profitForFutureMonth) {
        this.profitForFutureMonth = profitForFutureMonth;
    }

    public List<ReservationStatViewModel> getComingReservations() {
        return comingReservations;
    }

    public void setComingReservations(List<ReservationStatViewModel> comingReservations) {
        this.comingReservations = comingReservations;
    }

    public List<ReservationStatViewModel> getPast30DaysReservations() {
        return past30DaysReservations;
    }

    public void setPast30DaysReservations(List<ReservationStatViewModel> past30DaysReservations) {
        this.past30DaysReservations = past30DaysReservations;
    }
}