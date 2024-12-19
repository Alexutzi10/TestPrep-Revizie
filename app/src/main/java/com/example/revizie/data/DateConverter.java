package com.example.revizie.data;

import androidx.room.TypeConverter;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class DateConverter {
    public static SimpleDateFormat formatter = new SimpleDateFormat("mm.DD.yyyy", Locale.US);

    @TypeConverter
    public static String fromDate(Date value) {
        if (value == null) {
            return null;
        }

        return formatter.format(value);
    }

    @TypeConverter
    public static Date toDate(String value) {
        try {
            return formatter.parse(value);
        } catch (ParseException ex) {
            return null;
        }
    }
}
