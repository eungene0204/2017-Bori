package bori.bori.activity;

import android.app.Activity;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.view.*;
import android.widget.*;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import bori.bori.application.BoriApplication;
import bori.bori.connection.ConnectionDetector;
import bori.bori.fragment.*;
import bori.bori.news.source.SrcLogoManager;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import bori.bori.R;

import bori.bori.adapter.HeadNewsAdapter;
import bori.bori.user.UserManager;
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


import bori.bori.adapter.RecommendListAdapter;
import bori.bori.user.MyUser;
import io.realm.Realm;
import io.realm.RealmConfiguration;
import org.json.JSONObject;


public class MainActivity extends AppCompatActivity
        implements  RecommendCardFragment.OnRecommendFragmentListener
    ,RecommendListAdapter.OnNewsClickListener,HeadNewsFragment.OnHeadNewsFragmentInteractionListener,HeadNewsAdapter.OnHeadNewsClickListener
{

    private static final String TAG = "MainActivity";
    // Note: Your consumer key and secret should be obfuscated in your source code before shipping.
    private static final String TWITTER_KEY = "mYh1sti2PMGKmiU0C8pPLbGGl";
    private static final String TWITTER_SECRET = "0Kf8DNw8HklsHnINLGOPRryZMo3EvSCJvIL0Edh9JKS6ShCfx1";

    private ImageView mUserImageView;

    private MyUser mMyUser;
    private RecommendCardFragment mRecommendCardFragment;

    private int mWebViewFontSize;
    private boolean mIsDark = false;

    private BottomNavigationView mBottomNavigationView;
    private ImageView mSortImageView;


        /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    private GoogleApiClient mClient;
    private VolleyHelper mVolleyHelper;
    private Activity mActivity = this;
    private boolean mIsNetworkAvailable;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        BoriApplication.getInstance().checkDarkTheme(this);

        setContentView(R.layout.activity_main);

        mIsNetworkAvailable = ConnectionDetector.isNetworkAvailable(this);

        setRealmConfiguration();

        //Fragment
        if(savedInstanceState == null)
        {
            if(mIsNetworkAvailable)
            {
                mRecommendCardFragment = new RecommendCardFragment();
                FragmentHelper.checkFragment(this, mRecommendCardFragment,
                        RecommendCardFragment.TAG, null);

            }
            else
            {
                String msg = getResources().getString(R.string.no_connection);
                FragmentHelper.setEmptyFragment(this, msg);
            }

        }

        mMyUser = new MyUser(getApplicationContext());
        UserManager.sessionCheck(getApplicationContext());
        UserManager.setUser(mMyUser, getApplicationContext());

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        setToolbarTitle(toolbar);
        //setSortPopup(toolbar);


        mBottomNavigationView = findViewById(R.id.bottom_navigation);
        mBottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener()
        {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item)
            {

                switch (item.getItemId())
                {
                    case R.id.btm_nav_rmcd_news:
                        FragmentHelper.checkFragment(mActivity, new RecommendCardFragment(),
                                RecommendCardFragment.TAG, null);
                        return true;

                    case R.id.btm_nav_head_news:
                        FragmentHelper.checkFragment(mActivity, new HeadNewsFragment(),
                                HeadNewsFragment.TAG, null);
                        return true;

                    case R.id.btm_nav_fav:
                        FragmentHelper.checkFragment(mActivity, new FavNewsFragment(),
                                FavNewsFragment.TAG, null);
                        return true;

                    case R.id.btm_nav_stat:
                        FragmentHelper.checkFragment(mActivity, new StatFragment(),
                                StatFragment.TAG, null);
                        return true;
                }
                return false;
            }
        });


        setProfilePic(mMyUser.getProfileUrl());
        initSrcLogoManager();

        //set theme

        //requestRcmdNews();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        mClient = new GoogleApiClient.Builder(this).addApi(AppIndex.API).build();

    }

    private void initSrcLogoManager()
    {
        SrcLogoManager.getInstance().init(this);
    }


    /*
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

    }*/

    private void showSortBottomSheet()
    {

        SortNewsBottomSheetFragment sortNewsBottomSheetFragment = new SortNewsBottomSheetFragment();

        sortNewsBottomSheetFragment.setOnSortListener(mRecommendCardFragment);
        sortNewsBottomSheetFragment.setRetainInstance(true);

        sortNewsBottomSheetFragment.show(getSupportFragmentManager(),
                SortNewsBottomSheetFragment.TAG);

    }


    private void setToolbarTitle(Toolbar toolbar)
    {
        TextView titleView = toolbar.findViewById(R.id.toolbar_main_tile);

        titleView.setText(getResources().getString(R.string.bori_news));
    }


    //For test. need to be removed
    private void requestRcmdNews()
    {
        initVolley();

        JSONObject jsonObject = JsonUtils.writeJSON(mMyUser);
        JsonObjectRequest jsonObjectRequest = mVolleyHelper.rcmdNewsRequest(jsonObject,
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



    private void setRealmConfiguration()
    {
        Realm.init(this);

        RealmConfiguration realmConfiguration = new RealmConfiguration.Builder()
                .name(Realm.DEFAULT_REALM_NAME)
                .schemaVersion(0)
                .build();

        Realm.setDefaultConfiguration(realmConfiguration);
    }


    /*
    RequestListener<String, GlideDrawable> glideCallback = new RequestListener<String, GlideDrawable>(){
        @Override
        public boolean onException(Exception e, String model, Target<GlideDrawable> target, boolean isFirstResource)
        {
            return false;

        }
        @Override
        public boolean onResourceReady(GlideDrawable resource, String model, Target<GlideDrawable> target, boolean isFromMemoryCache, boolean isFirstResource) {
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {

                }
            },500);

            return false;

        }
    }; */

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

        //UrlImageViewHelper.setUrlDrawable(mUserImageView,url);
        Glide.with(this)
                .load(url)
                .listener(new RequestListener<Drawable>()
                {
                    @Override
                    public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource)
                    {
                        return false;
                    }

                    @Override
                    public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource)
                    {
                        return false;
                    }
                })
                .error(R.drawable.ic_user_profile_grey)
                .into(mUserImageView);


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
