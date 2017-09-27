package bori.bori.activity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Build;
import android.os.Bundle;
import android.os.Vibrator;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.ShareActionProvider;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;

import java.util.UUID;

import bori.bori.R;
import bori.bori.news.News;
import bori.bori.realm.FavNews;
import bori.bori.realm.RealmController;
import io.realm.Realm;
import io.realm.RealmResults;

public class WebViewActivity extends AppCompatActivity
{

    private final String TAG = "WebViewActivity";

    private WebView mWebView;
    private ProgressDialog mProgressDialog;
    private ProgressBar mProgressBar;
    private ShareActionProvider mShareActionProvider;

    private String mId;
    private String mNewsLinkUrl;
    private String mNewsTitle;
    private String mImgUrl;
    private int mFontSize;

    private Realm mRealm;
    private Menu mMenu;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_web_view);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        this.mRealm = RealmController.with(this).getRealm();

        mProgressDialog = new ProgressDialog(this);
        mProgressDialog.setIndeterminate(false);
        mProgressDialog.setCancelable(true);
        mProgressDialog.getWindow().setLayout(50,50);

        mProgressBar = (ProgressBar) findViewById(R.id.progressBar);

        Intent intent  = getIntent();
        mId = intent.getStringExtra(News.KEY_ID);
        mNewsLinkUrl = intent.getStringExtra(News.KEY_URL);
        mNewsTitle = intent.getStringExtra(News.KEY_TITLE);
        mImgUrl = intent.getStringExtra(News.KEY_IMG_URL);
        mFontSize = intent.getIntExtra(News.KEY_FONT_SIZE, (int)getResources().
                getDimension(R.dimen.webview_text_size_middle));

        setWebview();

   }

    private void setWebview()
    {
        mWebView = (WebView) findViewById(R.id.webView);
        mWebView.setWebViewClient(new WebViewClient()
                                  {
                                      @Override
                                      public void onPageStarted(WebView view, String url, Bitmap favicon)
                                      {
                                          super.onPageStarted(view, url, favicon);
                                          mProgressBar.setVisibility(View.VISIBLE);
                                      }

                                      @Override
                                      public void onPageFinished(WebView view, String url)
                                      {
                                          super.onPageFinished(view, url);
                                          mProgressBar.setVisibility(View.GONE);

                                      }
                                  }
        );

        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT)
        {
            mWebView.setLayerType(View.LAYER_TYPE_HARDWARE,null);
        }
        else
        {
            mWebView.setLayerType(View.LAYER_TYPE_SOFTWARE,null);
        }

        mWebView.getSettings().setLoadWithOverviewMode(true);
        mWebView.getSettings().setUseWideViewPort(true);
        mWebView.getSettings().setBuiltInZoomControls(true);
        mWebView.getSettings().setJavaScriptEnabled(true);
        mWebView.getSettings().setJavaScriptCanOpenWindowsAutomatically(false);
        mWebView.getSettings().setLoadsImagesAutomatically(true);
        mWebView.getSettings().setGeolocationEnabled(false);
        mWebView.getSettings().setNeedInitialFocus(false);
        mWebView.getSettings().setSaveFormData(false);

        if(mFontSize == 0 )
            mFontSize = (int) getResources().getDimension(R.dimen.webview_text_size_middle);
        mWebView.getSettings().setTextZoom(mFontSize);

        mWebView.loadUrl(mNewsLinkUrl);

    }

    @Override
    public boolean onSupportNavigateUp()
    {
        onBackPressed();
        return true;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        mMenu =  menu;

        getMenuInflater().inflate(R.menu.webview, menu);
        MenuItem item = menu.findItem(R.id.action_share);
        MenuItem favItem = menu.findItem(R.id.action_fav);

        FavNews favNews = new FavNews();
        favNews.setId(mId);
        if(isDuplicateNews(favNews))
        {
            favItem.setIcon(R.drawable.ic_star_white_24dp);
        }
        else
        {
            favItem.setIcon(R.drawable.ic_star_border_white_24dp);
        }


        mShareActionProvider = (ShareActionProvider)
                MenuItemCompat.getActionProvider(item);

        setShareIntent(createShareIntent());

        return super.onCreateOptionsMenu(menu);

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        int id = item.getItemId();
        if(id == R.id.action_refresh)
        {
            Log.i(TAG, "action_refresh");
            mWebView.reload();
        }
        else if(id == R.id.action_fav)
        {
            Log.i(TAG, "fav_news");
            saveFavNews();
        }

        return super.onOptionsItemSelected(item);
    }

    private void saveFavNews()
    {
        FavNews favNews = new FavNews();

        if(null == mNewsLinkUrl)
            mNewsLinkUrl = " ";

        if(null == mNewsTitle)
            mNewsTitle = " ";

        favNews.setId(mId);
        favNews.setTitle(mNewsTitle);
        favNews.setUrl(mNewsLinkUrl);
        favNews.setImgUrl(mImgUrl);

        if( false == isDuplicateNews(favNews) )
        {
            mRealm.beginTransaction();
            mRealm.copyToRealm(favNews);
            mRealm.commitTransaction();

            mMenu.findItem(R.id.action_fav).setIcon(R.drawable.ic_star_white_24dp);
            vibrate();
        }

    }

    private void cancleSave(FavNews favNews)
    {
        int position = RealmController.with(this).getIndexOf(favNews);
        RealmResults<FavNews> results = mRealm.where(FavNews.class)
                .findAll();

        mRealm.beginTransaction();
        results.remove(position);
        mRealm.commitTransaction();

        mMenu.findItem(R.id.action_fav).setIcon(R.drawable.ic_star_border_white_24dp);
        vibrate();

    }

    private void vibrate()
    {
        Vibrator vibrator = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
        vibrator.vibrate(500);
    }



    private boolean isDuplicateNews(FavNews favNews)
    {
         FavNews tempNews = RealmController.with(this).
                getFavNews(favNews.getId());

        if(tempNews == null)
            return false;

        return true;
    }

    private void setShareIntent(Intent shareIntent)
    {
        if(null != mShareActionProvider)
        {
            mShareActionProvider.setShareIntent(shareIntent);
        }

    }

    private Intent createShareIntent()
    {
        String bori = getResources().getString(R.string.bori);
        String newText = "[" + bori + "]" + mNewsTitle + " " + mNewsLinkUrl;

        Intent shareIntent = new Intent(Intent.ACTION_SEND);
        shareIntent.setType("text/plain");
        shareIntent.putExtra(Intent.EXTRA_SUBJECT, mNewsTitle);
        shareIntent.putExtra(Intent.EXTRA_TEXT,newText);

        return shareIntent;

    }

}
