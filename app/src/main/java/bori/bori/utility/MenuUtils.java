package bori.bori.utility;

import android.app.Activity;
import android.content.Intent;
import bori.bori.R;
import bori.bori.news.News;

public class MenuUtils
{
    public static Intent createShareIntent(News news, Activity activity)
    {
        String bori = activity.getResources().getString(R.string.bori_news);
        String newText = "[" + bori + "]" + news.getTitle()+ " " + news.getUrl();

        Intent shareIntent = new Intent(Intent.ACTION_SEND);
        shareIntent.setType("text/plain");
        shareIntent.putExtra(Intent.EXTRA_SUBJECT, news.getTitle());
        shareIntent.putExtra(Intent.EXTRA_TEXT,newText);

        return shareIntent;

    }
}
