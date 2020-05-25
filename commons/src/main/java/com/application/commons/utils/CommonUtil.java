package com.application.commons.utils;

public class CommonUtil {


    public static String generateUniqueId(String prefix){
        return prefix + new RandomUtil().nextString()
                + pad(baseNString((long) (System.currentTimeMillis() % (36 * 36)), 36), 6);
    }

    public static final String EMPTY_STRING = "";


    private static int base10Value(String str, int base) {
        int ret = 0;
        for (int i = 0; i < str.length(); i++) {
            char c = str.charAt(i);
            ret = (ret * base) + base10Value(c);
        }
        return ret;
    }

    private static int base10Value(char c) {
        if (c >= '0' && c <= '9') {
            return c - '0';
        }
        return c - 'A' + 10;
    }

    private static String pad(String str, int len) {
        if (str == null) {
            str = EMPTY_STRING;
        }
        return pad(str, "0", len, true);
    }

    private static String pad(String str, String padWith, int len, boolean padLeftNotRight) {
        while (str.length() < len) {
            if (padLeftNotRight) {
                str = padWith + str;
            } else {
                str = str + padWith;
            }
        }
        return str;
    }

    private static String baseNString(long Id, int base) {
        String baseNString = "";
        while (Id > 0) {
            long rem = Id % base;
            Id = Id / base;
            baseNString = baseNChar(rem) + baseNString;
        }
        return baseNString;
    }

    private static char baseNChar(long value) {
        if (value < 10) {
            return (char) ('0' + value);
        }
        return ((char) ('A' + value - 10));
    }

}
