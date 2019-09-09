package bori.bori.news;

import android.util.Log;

public class NewsDateManager
{
    static public final String TAG = "NewsDateManager";

    private NewsDateManager()
    {
    }

    public static String getDate(String publichsed_parsed)
    {
        String published = publichsed_parsed;
        String totalDate = "";

        try
        {
            String[] dateArray = published.split(",");
            String day = dateArray[0];
            String restDate = dateArray[1];

            String[] restArray =  restDate.split(" ");
            String date = restArray[1];
            String month = restArray[2];
            String year = restArray[3];

            String _month = parseMonth(month);

            totalDate = year + "년" + _month + "월" + date + "일"; //+ hour + "시" + min + "분";

        }
        catch (ArrayIndexOutOfBoundsException e)
        {
            Log.e(TAG, String.valueOf(e));

        }

        /*
        Pattern p = Pattern.compile("\\[(.*?)\\]");
        Matcher m = p.matcher(publichsed_parsed);

        if(m.find())
        {
            date = m.group(1);
        }

        String[] dateArr = date.split(",");
        String totalDate = "";

        try
        {
            String year = dateArr[0];
            String month = dateArr[1];
            String day = dateArr[2];
            String hour = dateArr[3];
            String min = dateArr[4];

            totalDate = year + "년" + month + "월" + day + "일"; //+ hour + "시" + min + "분";

        }
        catch (IndexOutOfBoundsException e)
        {
            e.getStackTrace();
        }

        return totalDate; */

        return totalDate;

    }

    static private String parseMonth(String month)
    {
        String result = "";
        String[] months = {"Jan","Feb","Mar","Apr","May","Jun","Jul","Aug","Sep","Oct",
                "Nov","Dec"};

        for(int i=0; i < months.length; i++)
        {
            if(months[i].equals(month))
            {
                int intMonth = i+1;
                result = String.valueOf(intMonth);
            }

        }

        return result;
    }

}
