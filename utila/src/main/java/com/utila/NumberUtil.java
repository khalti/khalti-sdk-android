package com.utila;


public class NumberUtil {
    public static double round(double value, int places) {
        if (places < 0) throw new IllegalArgumentException();

        long factor = (long) Math.pow(10, places);
        value = value * factor;
        long tmp = Math.round(value);
        return (double) tmp / factor;
    }

    public static Double convertToRupees(Long amount) {
        Double paisa = Double.parseDouble(amount.toString());
        LogUtil.log("paisa", paisa / 100);
        return round((paisa / 100), 3);
    }

    public static Integer addNumber(Object number1, Object number2) {
        Integer num = 0;
        if (number1.toString().length() > 0) {
            num = Integer.parseInt(number1.toString());
        }
        return num + Integer.parseInt(number2.toString());
    }

    public static Integer greaterNumber(Integer first, Integer second) {
        return first > second ? first : second;
    }

    public static Integer smallerNumber(Integer first, Integer second) {
        return first < second ? first : second;
    }

    public static Long convertToPaisa(Double amount) {
        Double newAmount = round(amount, 2);
        return ((Double) (newAmount * 100)).longValue();
    }
}
