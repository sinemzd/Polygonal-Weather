package com.example.sinemdalak.weatherforecasting;

import java.text.DateFormat;
import java.util.zip.DataFormatException;

public class Common {

    public static String API_KEY = "4509d4e4fe84d0523805a73785201aae";
    public static String API_LINK = "http://api.openweathermap.org/data/2.5/forecast?id=745044&APPID=";

    public static String apiRequest (){
        StringBuilder sb = new StringBuilder(API_LINK);
        sb.append(String.format(API_KEY));
        return sb.toString();
    }


}
