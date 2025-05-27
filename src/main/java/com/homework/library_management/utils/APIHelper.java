package com.homework.library_management.utils;

import jakarta.servlet.http.HttpServletRequest;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.Objects;

public class APIHelper {

    public static String encodeUrlMsg(String msg) {
        return URLEncoder.encode(msg, StandardCharsets.UTF_8);
    }

    public static long toValidNum(Long num) {
        return Objects.isNull(num) ? 0 : num;
    }

    public static void clearSession(HttpServletRequest request) {
        request.getSession().removeAttribute("librarianId");
        request.getSession().removeAttribute("expiredAt");
        request.getSession().removeAttribute("clientIp");
    }
}
