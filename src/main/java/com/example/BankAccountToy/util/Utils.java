package com.example.BankAccountToy.util;

import java.math.BigDecimal;

public class Utils {

    public static final int MINUS_ONE = -1;
    public static final int ONE = 1;

    public static boolean equalsZero(BigDecimal x) {
        return (0 == x.compareTo(BigDecimal.ZERO));
    }
    public static boolean equals(BigDecimal x, BigDecimal y) {
        return (0 == x.compareTo(y));
    }
    public static boolean lessThan(BigDecimal x, BigDecimal y) {
        return (MINUS_ONE == x.compareTo(y));
    }
    public static boolean lessThanOrEquals(BigDecimal x, BigDecimal y) {
        return (x.compareTo(y) <= 0);
    }
    public static boolean greaterThan(BigDecimal x, BigDecimal y) {
        return (ONE == x.compareTo(y));
    }
    public static boolean greaterThanOrEquals(BigDecimal x, BigDecimal y) {
        return (x.compareTo(y) >= 0);
    }
}
