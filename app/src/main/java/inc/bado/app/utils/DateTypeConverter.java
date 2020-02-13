package inc.bado.app.utils;

import androidx.room.TypeConverter;


import com.google.android.gms.common.util.NumberUtils;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

public class DateTypeConverter {
    @TypeConverter
    public static Date toDate(Long value){
        if(value == null){
            return null;
        }

        return new Date(value);
    }

    @TypeConverter
    public static Long toLong(Date date){
        if(date == null){
            return null;
        }
        return date.getTime();
    }
}
