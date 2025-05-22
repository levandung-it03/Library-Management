package com.homework.library_management.exception;

import static com.homework.library_management.enums.StatusMsgEnum.*;

import com.homework.library_management.utils.APIHelper;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@Slf4j
@ControllerAdvice
public class AppExcHandler {

    @ExceptionHandler(AppException.class)
    public String applicationExceptionHandler(HttpServletRequest request, HttpServletResponse response, AppException ex) {
        var extractedMsg = ex.getMessage().split("_");
        log.info("[HANDLER]_ApplicationException: {}", ex.getMessage());
        if (extractedMsg[1].contains("login")) {
            //--Something is wrong that we need to redirect to /login, so clear current session.
            request.getSession().removeAttribute("librarianId");
            request.getSession().removeAttribute("expiredAt");
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        } else {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
        }
        return "redirect:" + extractedMsg[1] + "?" + ERROR.getMsg() + "=" + APIHelper.encodeUrlMsg(extractedMsg[0]);
    }

    @ExceptionHandler(value = MethodArgumentNotValidException.class)
    public String handleHibernateValidatorException(HttpServletResponse response, MethodArgumentNotValidException ex) {
        response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
        String[] extractedExc = ex.getMessage().split(";");
        if (extractedExc.length == 6) {
            var plainMsg = extractedExc[5];
            var extractedMsg = plainMsg.substring(
                plainMsg.indexOf("[") + 1,
                plainMsg.indexOf("]")
            ).split("_");

            log.info("[HANDLER]_ValidatorException: {}", extractedMsg[0]);
            return "redirect:" + extractedMsg[1] + "?" + ERROR.getMsg() + "=" + APIHelper.encodeUrlMsg(extractedMsg[0]);
        } else {
            var plainMsg = extractedExc[4];
            var errorMsg = "Can not parse invalid value's type of: " + plainMsg.substring(
                plainMsg.indexOf("[") + 1, plainMsg.indexOf("]"));

            log.info("[HANDLER]_ValidatorException: '{}'", errorMsg);
            return "redirect:/home?" + ERROR.getMsg() + "=" + errorMsg;
        }
    }

    @ExceptionHandler(Exception.class)
    public String exceptionHandler(HttpServletResponse response, Exception ex) {
        response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
        log.info("[HANDLER]_UnawareException: {}", ex.getMessage());
        return "error/500";
    }
}
