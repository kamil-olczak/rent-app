package com.rentapp.util;


public class Calculate {
    public static final double TAX_RATE = 0.23;
    public static double calculateGross(double amountNett){
        return Math.round((amountNett * TAX_RATE + amountNett) * 100.0) / 100.0;
    }
    public static String calculateGrossString(String amountNett) {
        double amountNettDouble = Double.parseDouble(amountNett);
        String res = String.valueOf(Math.round((amountNettDouble * TAX_RATE + amountNettDouble) * 100.0) / 100.0);
        return addZeroIfTenthsEqual(res);
    }
    public static String calculateGrossString(double amountNett) {
        String res = String.valueOf(Math.round((amountNett * TAX_RATE + amountNett) * 100.0) / 100.0);
        return addZeroIfTenthsEqual(res);
    }
    public static double calculateNett(double amountGross){
        return Math.round((amountGross / (1+ TAX_RATE)) * 100.0) / 100.0;
    }

    public static String calculateNettString(double amountGross){
        String res = String.valueOf(Math.round((amountGross / (1+ TAX_RATE)) * 100.0) / 100.0);
        return addZeroIfTenthsEqual(res);
    }

    public static String addZeroIfTenthsEqual(String s){
        if(s.charAt(s.length()-2) == '.'){
            return s + "0";
        } else {
            return s;
        }
    }

    public static String addZeroIfTenthsEqual(double d){
        String s = String.valueOf(d);
        if(s.charAt(s.length()-2) == '.'){
            return s + "0";
        } else {
            return s;
        }
    }

    public static double parseDoubleRound2Tenths(String s){
        return Math.round(Double.parseDouble(s) * 100.0) / 100.0;
    }

}
