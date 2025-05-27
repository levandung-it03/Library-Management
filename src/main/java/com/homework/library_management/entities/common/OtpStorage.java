package com.homework.library_management.entities.common;

import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class OtpStorage {
    private final Map<String, String> otpMap = new ConcurrentHashMap<>();

    public void put(String key, String value) {
        otpMap.put(key, value);
    }

    public String get(String key) {
        return otpMap.get(key);
    }

    public void remove(String key) {
        otpMap.remove(key);
    }

    public boolean contains(String key) {
        return otpMap.containsKey(key);
    }
}
