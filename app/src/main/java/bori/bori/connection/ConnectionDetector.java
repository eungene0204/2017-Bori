package bori.bori.connection;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.widget.Toast;

import bori.bori.R;

/**
 * Created by Eugene on 2017-02-23.
 */

public class ConnectionDetector
{

    private ConnectionDetector()
    {
    }

    static public boolean isNetworkAvailable(Context context)
    {
        ConnectivityManager connectivityManager = (ConnectivityManager)
                context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo info = connectivityManager.getActiveNetworkInfo();

        if(info != null)
        {
            if(info.getType() == ConnectivityManager.TYPE_WIFI)
            {
                //WIFI
            }
            else if(info.getType() == ConnectivityManager.TYPE_MOBILE)
            {
                //Data plan
            }

            return true;
        }
        else
        {
            //not connected
            String msg = context.getResources().getString(R.string.no_connection);
            Toast.makeText(context, msg, Toast.LENGTH_LONG).show();

            return false;
        }
    }


}
