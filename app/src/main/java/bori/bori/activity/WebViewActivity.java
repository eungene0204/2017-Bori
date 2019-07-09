package bori.bori.activity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Vibrator;
import androidx.core.content.ContextCompat;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.ShareActionProvider;
import androidx.appcompat.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;

import bori.bori.R;
import bori.bori.news.News;
import bori.bori.realm.RealmHelper;
import bori.bori.utility.MenuUtils;
import io.realm.Realm;

public class WebViewActivity extends AppCompatActivity
{

    private final String TAG = "WebViewActivity";

    public static final String TYPE_NEWS_URL = "NEWS_URL";
    public static final String TYPE_SOURCE_URL = "SOURCE_RUL";

    private WebView mWebView;
    private ProgressDialog mProgressDialog;
    private ProgressBar mProgressBar;
    private ShareActionProvider mShareActionProvider;

    private int mFontSize;

    private Realm mRealm;
    private Menu mMenu;
    private News mNews;
    private String mUrlType;
    private RealmHelper mRealmHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_web_view);

        Toolbar toolbar =  findViewById(R.id.toolbar);
        toolbar.setTitleTextColor(ContextCompat.getColor(getBaseContext(),R.color.GreyPrimary));
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayShowTitleEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setTitle(getResources().getString(R.string.nav_rcmd_news));

        mProgressDialog = new ProgressDialog(this);
        mProgressDialog.setIndeterminate(false);
        mProgressDialog.setCancelable(true);
        mProgressDialog.getWindow().setLayout(50,50);

        mProgressBar =  findViewById(R.id.progressBar);

        Intent intent  = getIntent();

        mNews = intent.getExtras().getParcelable(News.TAG);
        getSupportActionBar().setTitle(mNews.getSource());
        mUrlType = intent.getStringExtra(News.KEY_URL_TYPE);

        mFontSize = intent.getIntExtra(News.KEY_FONT_SIZE, (int)getResources().
                getDimension(R.dimen.webview_text_size_middle));

        mRealmHelper = new RealmHelper(getApplicationContext());

        saveCount(mNews);

        setWebview();

   }

    private void saveCount(News news)
    {
        mRealmHelper.addTodayCategory(news.getCategory());

    }

    private void setWebview()
    {
        mWebView =  findViewById(R.id.webView);
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

        if(mUrlType.equals(TYPE_NEWS_URL))
        {
            mWebView.loadUrl(mNews.getUrl());
        }
        else if(mUrlType.equals(TYPE_SOURCE_URL))
        {
            mWebView.loadUrl(mNews.getSourceUrl());
        }

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

        getMenuInflater().inflate(R.menu.webview_overflow, menu);

        return true;

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        int id = item.getItemId();

        if(id == R.id.action_share)
        {

            Intent intent = MenuUtils.createShareIntent(mNews, this);
            String share = getString(R.string.news_share);
            Intent chooser = Intent.createChooser(intent,share);

            startActivity(chooser);

        }
        else if(id == R.id.action_bookmark)
        {
            RealmHelper helper = new RealmHelper(getBaseContext());
            helper.bookMarkNews(mNews);

        }
        else if( id == R.id.action_source)
        {
            showBrowser();
        }


        return super.onOptionsItemSelected(item);
    }

    private void showBrowser()
    {
        String url = mNews.getUrl();
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setData(Uri.parse(url));
        startActivity(intent);
    }

    private void vibrate()
    {
        Vibrator vibrator = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
        vibrator.vibrate(500);
    }


    private void setShareIntent(Intent shareIntent)
    {
        if(null != mShareActionProvider)
        {
            mShareActionProvider.setShareIntent(shareIntent);
        }

    }

}
