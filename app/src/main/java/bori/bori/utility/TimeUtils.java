package bori.bori.utility;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.v4.app.INotificationSideChannel;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class TimeUtils
{
    public static final String TAG = "TimeUtils";

    public static final String KEY_TODAY = "today";

    static public int getToday()
    {
        Date date = Calendar.getInstance().getTime();
        SimpleDateFormat fomatter = new SimpleDateFormat("yyyyMMdd");
        String today = fomatter.format(date);

        return Integer.valueOf(today);

    }

    static public Integer[] getTodayArray()
    {
        Date date = Calendar.getInstance().getTime();
        SimpleDateFormat fomatter = new SimpleDateFormat("yyyyMMdd");
        String today = fomatter.format(date);

        Integer[] todayArr = new Integer[1];
        todayArr[0] = Integer.valueOf(today);

        return todayArr;

    }

    static public int getSevenDaysAgo()
    {
        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.DATE, -6);
        Date date = cal.getTime();
        SimpleDateFormat fomatter = new SimpleDateFormat("yyyyMMdd");
        String senvenDays = fomatter.format(date);

        return Integer.valueOf(senvenDays);

    }


    static public void saveToday(Context context)
    {
        SharedPreferences preferences = context.getSharedPreferences(TAG,0);
        SharedPreferences.Editor editor = preferences.edit();

        editor.putInt(KEY_TODAY,getToday());

        editor.apply();

    }

    static public String getLastSavedDay(Context context)
    {

        SharedPreferences preferences = context.getSharedPreferences(TAG,0);

        String savedToday = preferences.getString(KEY_TODAY,"");

        return savedToday;

    }
}
