package MyProjectGradle.web;

import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.RequestDispatcher;
import javax.servlet.http.HttpServletRequest;

@Controller
public class ExceptionController implements ErrorController {

    @RequestMapping("/error")
    public String handleError(HttpServletRequest request) {
        Object status =
                request.getAttribute(RequestDispatcher.ERROR_STATUS_CODE);

        if (status != null) {
            int statusCode = Integer.parseInt(status.toString());

            if (statusCode == HttpStatus.NOT_FOUND.value()) {
                return "errors/error404";
            } else if (statusCode == HttpStatus.INTERNAL_SERVER_ERROR.value()) {
                return "errors/error500";
            }else if (statusCode == HttpStatus.BAD_REQUEST.value()){
                return "errors/error400";
            }else if (statusCode == HttpStatus.UNAUTHORIZED.value()){
                return "errors/error401";
            }else if (statusCode == HttpStatus.FORBIDDEN.value()){
                return "errors/error403";
            }
        }
        return "errors/error";
    }

}
