package bori.bori.connection;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

/**
 * Created by Eugene on 2017-02-23.
 */

public class ConnectionDetector
{
    final private Context context;

    public ConnectionDetector(Context context)
    {
        this.context = context;
    }

    public boolean isConnectingTOInternet()
    {
        ConnectivityManager connectivityManager = (ConnectivityManager)
                this.context.getSystemService(Context.CONNECTIVITY_SERVICE);
        if(connectivityManager != null)
        {
            NetworkInfo info = connectivityManager.getActiveNetworkInfo();
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

            return false;
        }
    }


}
