package com.javaproject.storeapp.entity;

public enum CarCategory {
    SPORT, FAST, OFFROAD, UTILITY, VINTAGE, CONVERTIBLE;

    public static boolean contains(String test) {
        for (CarCategory c : CarCategory.values()) {
            if (c.name().equals(test)) {
                return true;
            }
        }
        return false;
    }
}
