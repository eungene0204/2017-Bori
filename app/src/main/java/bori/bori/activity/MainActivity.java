package bori.bori.activity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.core.content.ContextCompat;
import androidx.appcompat.app.AlertDialog;
import android.view.View;
import bori.bori.fragment.UserBottomSheetDialogFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationView;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;

import bori.bori.R;

import bori.bori.adapter.HeadNewsAdapter;
import bori.bori.fragment.HeadNewsFragment;
import bori.bori.utility.FontUtils;
import bori.bori.utility.JsonUtils;
import bori.bori.volley.VolleyHelper;
import bori.bori.volley.VolleySingleton;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonObjectRequest;
import com.google.android.gms.appindexing.Action;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.appindexing.Thing;
import com.google.android.gms.common.api.GoogleApiClient;
import com.koushikdutta.urlimageviewhelper.UrlImageViewHelper;


import bori.bori.adapter.RecommendListAdapter;
import bori.bori.auth.SessionManager;
import bori.bori.fragment.FavNewsFragment;
import bori.bori.fragment.RecommendFragment;
import bori.bori.user.MyUser;
import io.realm.Realm;
import io.realm.RealmConfiguration;
import org.json.JSONObject;


public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, RecommendFragment.OnRecommendFragmentListener
    ,RecommendListAdapter.OnNewsClickListener,HeadNewsFragment.OnHeadNewsFragmentInteractionListener,HeadNewsAdapter.OnHeadNewsClickListener
{

    private static final String TAG = "MainActivity";
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

    private BottomNavigationView mBottomNavigationView;

    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    private GoogleApiClient mClient;
    private VolleyHelper mVolleyHelper;

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
        setToolbarTitle(toolbar);

        mBottomNavigationView = findViewById(R.id.bottom_navigation);
        mBottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener()
        {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item)
            {
                Fragment fragment = null;
                Fragment tempFragment = null;
                String fragmentTag = null;

                switch (item.getItemId())
                {
                    case R.id.btm_nav_rmcd_news:
                        tempFragment = getSupportFragmentManager()
                                .findFragmentByTag(RecommendFragment.TAG);

                        if(tempFragment == null)
                        {
                            fragment = new RecommendFragment();
                            Bundle bundle = new Bundle();
                            bundle.putInt(FontUtils.KEY_FONT_SIZE, mWebViewFontSize);
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

                        if(null != fragment)
                        {
                            replaceFragment(fragment,fragmentTag);
                        }

                        return true;

                    case R.id.btm_nav_head_news:
                        tempFragment = getSupportFragmentManager()
                                .findFragmentByTag(HeadNewsFragment.TAG);

                        if(tempFragment == null)
                        {
                            fragment = new HeadNewsFragment();
                            Bundle bundle = new Bundle();
                            bundle.putInt(FontUtils.KEY_FONT_SIZE, mWebViewFontSize);
                            fragment.setArguments(bundle);
                            fragmentTag = HeadNewsFragment.TAG;
                        }
                        else
                        {
                            if(!tempFragment.isVisible())
                            {
                                fragment = tempFragment;
                            }
                        }

                        if(null != fragment)
                        {
                            replaceFragment(fragment,fragmentTag);
                        }

                        return true;

                    case R.id.btm_nav_fav:
                        return true;

                    case R.id.btm_nav_stat:
                        return true;
                }
                return false;
            }
        });



        /*
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
        */

        setProfilePic(mMyUser.getProfileUrl());

        //requestRcmdNews();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        mClient = new GoogleApiClient.Builder(this).addApi(AppIndex.API).build();

    }

    private void setToolbarTitle(Toolbar toolbar)
    {
        TextView titleView = toolbar.findViewById(R.id.toolbar_main_tile);
        titleView.setText(getResources().getString(R.string.bori_news));
    }


    private void requestRcmdNews()
    {
        initVolley();

        JSONObject jsonObject = JsonUtils.writeJSON(mMyUser);
        JsonObjectRequest jsonObjectRequest = mVolleyHelper.rcmdRequest(jsonObject,
                VolleyHelper.RCMD_NEWS_URL);

        jsonObjectRequest.setShouldCache(false);

        VolleySingleton.getInstance(this).addToRequestQueue(jsonObjectRequest);
    }

    private void initVolley()
    {

        RequestQueue requestQueue;
        requestQueue = VolleySingleton.getInstance
                 (this.getApplicationContext()).getRequestQueue();
        mVolleyHelper = new VolleyHelper(requestQueue,
                this,null);

    }

    private void setNavigationView()
    {
        /*
        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        View header = navigationView.getHeaderView(0);
        header.setBackgroundColor(ContextCompat.getColor(this,R.color.nav_head));

        mUserNameTextView = (TextView) header.findViewById(R.id.user_name);
        mUserEmailTextView = (TextView) header.findViewById(R.id.user_email);
        mUserImageView = (ImageView) header.findViewById(R.id.user_profile_pic);

        mUserNameTextView.setText(mMyUser.getScreenName());
        mUserEmailTextView.setText(mMyUser.getEmail());
        */

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
        Toolbar toolbar = findViewById(R.id.toolbar);
        mUserImageView = (ImageView) toolbar.findViewById(R.id.user_profile_pic);

        mUserImageView.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                showUserBottomSheet();

            }
        });

        UrlImageViewHelper.setUrlDrawable(mUserImageView,url);
    }

    private void showUserBottomSheet()
    {
        UserBottomSheetDialogFragment userBottomSheetDialogFragment =
                UserBottomSheetDialogFragment.newInstance();

        Bundle bundle = new Bundle();
        bundle.putParcelable(MyUser.KEY_MYUSER, mMyUser);

        userBottomSheetDialogFragment.setArguments(bundle);

        userBottomSheetDialogFragment.show(getSupportFragmentManager(),
                UserBottomSheetDialogFragment.TAG);

    }


    @Override
    public void onBackPressed()
    {

        /*
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START))
        {
            drawer.closeDrawer(GravityCompat.START);
        } else
        {
            super.onBackPressed();
        } */

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
        if ( id == R.id.nav_head_news)
        {

        }
        else if (id == R.id.nav_rcmd_news)
        {
            Fragment tempFragment = getSupportFragmentManager()
                    .findFragmentByTag(RecommendFragment.TAG);

            if(tempFragment == null)
            {
                fragment = new RecommendFragment();
                Bundle bundle = new Bundle();
                bundle.putInt(FontUtils.KEY_FONT_SIZE, mWebViewFontSize);
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
            bundle.putInt(FontUtils.KEY_FONT_SIZE, mWebViewFontSize);
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

        //DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        //drawer.closeDrawer(GravityCompat.START);

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
        intent.putExtra(FontUtils.KEY_FONT_SIZE,mWebViewFontSize);

        startActivityForResult(intent, FontUtils.REQUEST_FONT_SIZE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        if(requestCode == FontUtils.REQUEST_FONT_SIZE)
        {
            if(resultCode == RESULT_OK)
            {
                int result = data.getIntExtra(FontUtils.KEY_FONT_SIZE, (int)
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
    public int onSetFontSize()
    {
        return mWebViewFontSize;
    }

    @Override
    public MyUser onHeadNewsFragmentCall()
    {
        return mMyUser;
    }
}
