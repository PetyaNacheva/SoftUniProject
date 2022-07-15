package MyProjectGradle.aop;

import MyProjectGradle.service.LogService;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;

import javax.transaction.Transactional;

@Aspect
@Component
public class LogAspect {
    private final LogService logService;

    public LogAspect(LogService logService) {
        this.logService = logService;
    }

    @Pointcut("execution(* MyProjectGradle.service.ReservationService.addReservation(..))")
    public void detailsPointcut(){};

  /*@After("detailsPointcut()")
    public void afterAdvice(JoinPoint joinPoint){
        Object[] args = joinPoint.getArgs();
        Long reservationId = (Long) args[0];
        String action = joinPoint.getSignature().getName();
        logService.createLog(action, reservationId);
       // TODO: 7/9/2022 to fix the logic for the logger when making a reservation

    }*/
}
