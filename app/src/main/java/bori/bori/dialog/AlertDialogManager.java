package bori.bori.dialog;

import android.content.Context;
import android.content.DialogInterface;
import androidx.appcompat.app.AlertDialog;

/**
 * Created by Eugene on 2017-02-23.
 */

public class AlertDialogManager
{
    public void showAlertDialog(Context context, String title,String message,Boolean status)
    {
        AlertDialog alertDialog = new AlertDialog.Builder(context).create();

        alertDialog.setTitle(title);
        alertDialog.setMessage(message);

        if(status != null)
        {
            alertDialog.setButton(DialogInterface.BUTTON_POSITIVE,
                    "OK", new DialogInterface.OnClickListener()
                    {
                        @Override
                        public void onClick(DialogInterface dialog, int which)
                        {

                        }
                    });

            alertDialog.show();
        }
    }
}
