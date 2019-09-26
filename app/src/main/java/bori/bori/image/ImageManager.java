package bori.bori.image;

import android.content.Context;
import android.graphics.drawable.GradientDrawable;
import android.widget.ImageView;

import bori.bori.R;

public class ImageManager
{
    private ImageManager()
    {
    }

    static public void setRoundedImg(ImageView imgview, Context context)
    {
        GradientDrawable drawable = (GradientDrawable)context.getDrawable(R.drawable.round_background);
        imgview.setBackground(drawable);
        imgview.setClipToOutline(true);

    }

}
