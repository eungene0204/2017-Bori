package bori.bori.activity;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;

import bori.bori.R;

import com.google.android.gms.appindexing.Action;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.appindexing.Thing;
import com.google.android.gms.common.api.GoogleApiClient;
import com.koushikdutta.urlimageviewhelper.UrlImageViewHelper;
import com.twitter.sdk.android.tweetui.TweetTimelineListAdapter;
import com.twitter.sdk.android.tweetui.UserTimeline;


import java.util.ArrayList;

import bori.bori.adapter.RecommendListAdapter;
import bori.bori.auth.SessionManager;
import bori.bori.fragment.FavNewsFragment;
import bori.bori.fragment.RecommendFragment;
import bori.bori.news.News;
import bori.bori.user.MyUser;
import bori.bori.volley.VolleyHelper;
import io.realm.Realm;
import io.realm.RealmConfiguration;


public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, RecommendFragment.OnRecommendFragmentListener
    ,RecommendListAdapter.OnNewsClickListener
{

    private static final String TAG = "MainActivity";

    public static final String KEY_FONT_SIZE = "fontSIze";
    public static final int REQUEST_FONT_SIZE = 1;

    // Note: Your consumer key and secret should be obfuscated in your source code before shipping.
    private static final String TWITTER_KEY = "mYh1sti2PMGKmiU0C8pPLbGGl";
    private static final String TWITTER_SECRET = "0Kf8DNw8HklsHnINLGOPRryZMo3EvSCJvIL0Edh9JKS6ShCfx1";

    private TextView mUserNameTextView;
    private TextView mUserEmailTextView;
    private ImageView mUserImageView;
    private AlertDialog mAlertDialog;

    private SessionManager session;
    private MyUser mMyUser;
    private RecommendFragment mRecommendFragment;

    private int mWebViewFontSize;

    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    private GoogleApiClient mClient;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        //TwitterAuthConfig authConfig = new TwitterAuthConfig(TWITTER_KEY, TWITTER_SECRET);
        //Fabric.with(this, new Twitter(authConfig));
        setContentView(R.layout.activity_main);

        setRealmConfiguration();

        //Fragment
        if(savedInstanceState == null)
        {
            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
            RecommendFragment fragment = new RecommendFragment();
            transaction.add(R.id.fragment_container,fragment, RecommendFragment.TAG);
            transaction.commit();
        }

        sessionCheck();
        setUser();

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle(getString(R.string.bori_news));

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close)
        {
            @Override
            public void onDrawerClosed(View drawerView)
            {
                super.onDrawerClosed(drawerView);

                NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
                for (int i = 0; i <navigationView.getMenu().size() ; i++)
                {
                    navigationView.getMenu().getItem(i).setChecked(false);
                }
            }
        };

        drawer.addDrawerListener(toggle);
        toggle.syncState();

        setNavigationView();
        setProfilePic(mMyUser.getProfileUrl());

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        mClient = new GoogleApiClient.Builder(this).addApi(AppIndex.API).build();

    }

    private void setNavigationView()
    {
        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        View header = navigationView.getHeaderView(0);
        header.setBackgroundColor(ContextCompat.getColor(this,R.color.nav_head));

        mUserNameTextView = (TextView) header.findViewById(R.id.user_name);
        mUserEmailTextView = (TextView) header.findViewById(R.id.user_email);
        mUserImageView = (ImageView) header.findViewById(R.id.user_profile_pic);

        mUserNameTextView.setText(mMyUser.getScreenName());
        mUserEmailTextView.setText(mMyUser.getEmail());

    }

    private void setUser()
    {
        mMyUser = new MyUser(getApplicationContext());
        mMyUser.setName(session.getUserName());
        mMyUser.setEmail(session.getUserEmail());
        mMyUser.setScreenName(session.getScreenName());
        mMyUser.setProfileUrl(session.getProfilePicUrl());
    }

    private void sessionCheck()
    {
        session = new SessionManager(getApplicationContext());
        session.checkLogin();
    }

    private void setRealmConfiguration()
    {
        RealmConfiguration realmConfiguration = new RealmConfiguration.Builder(this)
                .name(Realm.DEFAULT_REALM_NAME)
                .schemaVersion(0)
                .deleteRealmIfMigrationNeeded()
                .build();
        Realm.setDefaultConfiguration(realmConfiguration);
    }

    public void setProfilePic (String url)
    {
        UrlImageViewHelper.setUrlDrawable(mUserImageView,url);
    }


    @Override
    public void onBackPressed()
    {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START))
        {
            drawer.closeDrawer(GravityCompat.START);
        } else
        {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings)
        {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item)
    {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        Fragment fragment = null;
        String fragmentTag= null;
        if (id == R.id.nav_news)
        {
            Fragment tempFragment = getSupportFragmentManager()
                    .findFragmentByTag(RecommendFragment.TAG);

            if(tempFragment == null)
            {
                fragment = new RecommendFragment();
                Bundle bundle = new Bundle();
                bundle.putInt(KEY_FONT_SIZE, mWebViewFontSize);
                fragment.setArguments(bundle);
                fragmentTag = RecommendFragment.TAG;
            }
            else
            {
                if(!tempFragment.isVisible())
                {
                    fragment = tempFragment;
                }
            }
        }
        else if (id == R.id.nav_fav_news)
        {
            fragment = new FavNewsFragment();
            Bundle bundle = new Bundle();
            bundle.putInt(KEY_FONT_SIZE, mWebViewFontSize);
            fragment.setArguments(bundle);
        }
        else if (id == R.id.nav_font_size)
        {
            startFontSizeActivity();
        }
        else if (id == R.id.nav_logout)
        {
            session.logoutUser();
            finish();
        }

        if(null != fragment)
        {
            replaceFragment(fragment,fragmentTag);
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);

        return true;
    }


    private void replaceFragment(Fragment fragment, String tag)
    {
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.fragment_container,fragment, tag);
        ft.addToBackStack(null);
        ft.commit();
    }

    private void startFontSizeActivity()
    {
        Intent intent = new Intent(this, FontSizeAcitivity.class);
        intent.putExtra(KEY_FONT_SIZE,mWebViewFontSize);

        startActivityForResult(intent, REQUEST_FONT_SIZE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        if(requestCode == REQUEST_FONT_SIZE)
        {
            if(resultCode == RESULT_OK)
            {
                int result = data.getIntExtra(KEY_FONT_SIZE, (int)
                        getResources().getDimension(R.dimen.webview_text_size_middle));

                mWebViewFontSize = result;

            }
        }

    }


    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    public Action getIndexApiAction()
    {
        Thing object = new Thing.Builder()
                .setName("Main Page") // TODO: Define a title for the content shown.
                // TODO: Make sure this auto-generated URL is correct.
                .setUrl(Uri.parse("http://[ENTER-YOUR-URL-HERE]"))
                .build();
        return new Action.Builder(Action.TYPE_VIEW)
                .setObject(object)
                .setActionStatus(Action.STATUS_TYPE_COMPLETED)
                .build();
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu)
    {
        return false;
    }

    @Override
    public void onStart()
    {
        super.onStart();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        mClient.connect();
        AppIndex.AppIndexApi.start(mClient, getIndexApiAction());
    }

    @Override
    public void onStop()
    {
        super.onStop();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        AppIndex.AppIndexApi.end(mClient, getIndexApiAction());
        mClient.disconnect();
    }

    @Override
    protected void onResume()
    {
        super.onResume();

    }

    @Override
    public MyUser onRecommendFragmentCall()
    {
        return mMyUser;
    }

    @Override
    public int onNewsClicked()
    {
        return mWebViewFontSize;
    }

}
