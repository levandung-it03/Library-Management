package com.homework.library_management.exception;

import static com.homework.library_management.enums.StatusMsgEnum.*;

import com.homework.library_management.config.GlobalLogger;
import com.homework.library_management.enums.Loggers;
import com.homework.library_management.utils.APIHelper;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class AppExcHandler {
    GlobalLogger logger;

    @ExceptionHandler(AppException.class)
    public String applicationExceptionHandler(HttpServletRequest request, HttpServletResponse response, AppException ex) {
        var extractedMsg = ex.getMessage().split("_");
        if (extractedMsg[1].contains("login")) {
            //--Something is wrong that we need to redirect to /login, so clear current session.
            request.getSession().removeAttribute("librarianId");
            request.getSession().removeAttribute("expiredAt");
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        } else {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
        }
        logger.error(request, "ApplicationException: %s", ex.getMessage());
        logger.markRequestToContinueLoggingWhenRedirect(request);
        return "redirect:" + extractedMsg[1] + "?" + ERROR.getMsg() + "=" + APIHelper.encodeUrlMsg(extractedMsg[0]);
    }

    @ExceptionHandler(value = MethodArgumentNotValidException.class)
    public String handleHibernateValidatorException(
        HttpServletRequest request,
        HttpServletResponse response,
        MethodArgumentNotValidException ex) {
        response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
        String[] extractedExc = ex.getMessage().split(";");
        if (extractedExc[5].contains("_/") || extractedExc.length == 6) {
            var plainMsg = extractedExc[5];
            var extractedMsg = plainMsg.substring(
                plainMsg.indexOf("[") + 1,
                plainMsg.indexOf("]")
            ).split("_");
            logger.error(request, "ValidatorException: %s", extractedMsg[0]);

            if (extractedMsg[1].isEmpty())  //--ErrMsg does not contain "/redirect-url", so we return JSON err.
                return "{\"" + ERROR.getMsg() + "\": \"" + APIHelper.encodeUrlMsg(extractedMsg[0]) + "\"}";
            else {
                logger.markRequestToContinueLoggingWhenRedirect(request);
                return "redirect:" + extractedMsg[1] + "?" + ERROR.getMsg() + "=" + APIHelper.encodeUrlMsg(extractedMsg[0]);
            }
        } else {
            var plainMsg = extractedExc[4];
            var errorMsg = "Không thể đọc được kiểu dữ liệu: " + plainMsg.substring(
                plainMsg.indexOf("[") + 1, plainMsg.indexOf("]"));

            logger.error(request, "ValidatorException: %s", errorMsg);
            logger.markRequestToContinueLoggingWhenRedirect(request);
            if (request.getSession().getAttribute("librarianId") == null)
                return "redirect:/login?" + ERROR.getMsg() + "=" + APIHelper.encodeUrlMsg(errorMsg);
            else return "redirect:/home?" + ERROR.getMsg() + "=" + APIHelper.encodeUrlMsg(errorMsg);
        }
    }

    @ExceptionHandler(Exception.class)
    public String exceptionHandler(HttpServletRequest request, HttpServletResponse response, Exception ex) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
        logger.weird(request, "UnawareException: %s", ex.getMessage());
        return "error/500";
    }
}
