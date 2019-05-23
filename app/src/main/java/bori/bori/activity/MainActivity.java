package bori.bori.activity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.widget.*;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.appcompat.app.AlertDialog;
import android.view.View;
import bori.bori.fragment.*;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.google.android.material.navigation.NavigationView;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import bori.bori.R;

import bori.bori.adapter.HeadNewsAdapter;
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
import bori.bori.user.MyUser;
import io.realm.Realm;
import io.realm.RealmConfiguration;
import org.json.JSONObject;


public class MainActivity extends AppCompatActivity
        implements  RecommendFragment.OnRecommendFragmentListener
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
    private ImageView mSortImageView;



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
            mRecommendFragment = new RecommendFragment();
            transaction.add(R.id.fragment_container,mRecommendFragment, RecommendFragment.TAG);
            transaction.commit();
        }

        sessionCheck();
        setUser();

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        setToolbarTitle(toolbar);
        setSortPopup(toolbar);

        mBottomNavigationView = findViewById(R.id.bottom_navigation);
        mBottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener()
        {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item)
            {

                switch (item.getItemId())
                {
                    case R.id.btm_nav_rmcd_news:
                        checkFragment(new RecommendFragment(),RecommendFragment.TAG);
                        return true;

                    case R.id.btm_nav_head_news:
                        checkFragment(new HeadNewsFragment(),HeadNewsFragment.TAG);
                        return true;

                    case R.id.btm_nav_fav:
                        checkFragment(new FavNewsFragment(),FavNewsFragment.TAG);
                        return true;

                    case R.id.btm_nav_stat:
                        checkFragment(new StatFragment(),StatFragment.TAG);
                        return true;
                }
                return false;
            }
        });


        setProfilePic(mMyUser.getProfileUrl());

        //requestRcmdNews();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        mClient = new GoogleApiClient.Builder(this).addApi(AppIndex.API).build();

    }

    private void setSortPopup(Toolbar toolbar)
    {
        mSortImageView = toolbar.findViewById(R.id.sort_img);

        mSortImageView.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                showSortBottomSheet();

            }
        });

    }

    private void showSortBottomSheet()
    {

        SortNewsBottomSheetFragment sortNewsBottomSheetFragment = new SortNewsBottomSheetFragment();

        sortNewsBottomSheetFragment.setOnSortListener(mRecommendFragment);
        sortNewsBottomSheetFragment.setRetainInstance(true);

        sortNewsBottomSheetFragment.show(getSupportFragmentManager(),
                SortNewsBottomSheetFragment.TAG);

    }

    private void checkFragment(Fragment fragment, String tag)
    {

        Fragment nextFragment = getSupportFragmentManager()
                .findFragmentByTag(tag);

        if(nextFragment == null)
        {
            Bundle bundle = new Bundle();
            bundle.putInt(FontUtils.KEY_FONT_SIZE, mWebViewFontSize);
            fragment.setArguments(bundle);

        }
        else
        {
            if(!nextFragment.isVisible())
            {
                fragment = nextFragment;
            }

        }

        replaceFragment(fragment,tag);
    }

    private void replaceFragment(Fragment fragment, String tag)
    {
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.fragment_container,fragment, tag);
        ft.addToBackStack(null);
        ft.commit();
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
        Realm.init(this);

        RealmConfiguration realmConfiguration = new RealmConfiguration.Builder()
                .name(Realm.DEFAULT_REALM_NAME)
                .schemaVersion(0)
                .deleteRealmIfMigrationNeeded()
                .build();
        Realm.setDefaultConfiguration(realmConfiguration);
    }

    public void setProfilePic (String url)
    {
        Toolbar toolbar = findViewById(R.id.toolbar);
        mUserImageView =  toolbar.findViewById(R.id.user_profile_pic);

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


    private void startFontSizeActivity()
    {
        Intent intent = new Intent(this, FontSizeAcitivity.class);
        intent.putExtra(FontUtils.KEY_FONT_SIZE,mWebViewFontSize);

        startActivityForResult(intent, FontUtils.REQUEST_FONT_SIZE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == FontUtils.REQUEST_FONT_SIZE)
        {
            if (resultCode == RESULT_OK)
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
