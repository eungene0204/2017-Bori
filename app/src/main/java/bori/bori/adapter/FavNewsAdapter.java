package bori.bori.adapter;

import android.content.Context;
import android.content.Intent;
import androidx.recyclerview.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.koushikdutta.urlimageviewhelper.UrlImageViewHelper;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import bori.bori.R;
import bori.bori.activity.WebViewActivity;
import bori.bori.news.News;
import bori.bori.realm.FavNews;
import bori.bori.realm.RealmController;
import io.realm.Realm;
import io.realm.RealmResults;

/**
 * Created by Eugene on 2017-09-01.
 */


public class FavNewsAdapter extends RealmRecyclerViewAdapter<FavNews>
{
    private final String TAG = "FavNewsAdapter";

    private Context mContext;
    private Realm mRealm;
    private RecommendListAdapter.OnNewsClickListener mOnNewsClickListener;
    private List<FavNews> mItemsPendingRemoval;

    private static final int PENDING_REMOVAL_TIMEOUT = 3000;
    private android.os.Handler mHandler = new android.os.Handler();
    HashMap<String, Runnable> mPendingRunnables = new HashMap<>();

    private int mFontSize;

    public int getFontSize()
    {
        return mFontSize;
    }

    public void setFontSize(int fontSize)
    {
        mFontSize = fontSize;
    }

    public FavNewsAdapter(Context context)
    {
        this.mContext = context;
        mItemsPendingRemoval = new ArrayList<>();

    }


    public void setNewsClickListener(RecommendListAdapter.OnNewsClickListener listener)
    {
        mOnNewsClickListener = listener;
    }


    @Override
    public FavNewsViewHolder onCreateViewHolder(ViewGroup parent, int viewType)
    {
        View view = LayoutInflater.from(parent.getContext()).
                inflate(R.layout.fav_row_item,parent,false);

        return new FavNewsViewHolder(view,mContext);

    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position)
    {
        FavNewsViewHolder viewHolder = (FavNewsViewHolder) holder;
        mRealm = RealmController.getInstance().getRealm();

        final FavNews favNews = getItem(position);
        viewHolder.setFavNews(favNews);

        viewHolder.getTextView().setText(favNews.getTitle());
        ImageView imageView = viewHolder.getImageView();
        String imgUrl = favNews.getImgUrl();
        setImageSrc(imgUrl, position, imageView);

        View v = viewHolder.getView();

        v.setOnLongClickListener(new View.OnLongClickListener()
        {
            @Override
            public boolean onLongClick(View v)
            {

                RealmResults<FavNews> results = mRealm.where(FavNews.class)
                        .findAll();

                // All changes to data must happen in a transaction
                mRealm.beginTransaction();

                // remove single match
                results.remove(position);
                mRealm.commitTransaction();
                notifyDataSetChanged();

                return false;
            }
        });

        if(mItemsPendingRemoval.contains(favNews))
        {
            viewHolder.mRegularLayout.setVisibility(View.GONE);
            viewHolder.mSwipeLayout.setVisibility(View.VISIBLE);
            viewHolder.mUndo.setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View v)
                {
                   undoOpt(favNews);
                }
            });
        }
        else
        {
            viewHolder.mRegularLayout.setVisibility(View.VISIBLE);
            viewHolder.mSwipeLayout.setVisibility(View.GONE);
            viewHolder.getTextView().setText(favNews.getTitle());
        }


    }

    private RealmResults<FavNews> getRealmResults()
    {

        RealmResults<FavNews> results = mRealm.where(FavNews.class)
                .findAll();

        return results;
    }

    private void undoOpt(FavNews favNews)
    {
        Runnable pendingRemovalRunnable = mPendingRunnables.get(favNews.getId());
        mPendingRunnables.remove(favNews.getId());

        if(pendingRemovalRunnable != null)
        {
            mHandler.removeCallbacks(pendingRemovalRunnable);
        }
        mItemsPendingRemoval.remove(favNews);
        RealmResults<FavNews> results = getRealmResults();
        notifyItemChanged(getIndex(results,favNews));
    }

    private int getIndex(RealmResults<FavNews> results, FavNews favNews)
    {
        for(int i=0; i < results.size(); i++)
        {
            if(results.get(i).equals(favNews))
            {
                return i;
            }
        }
        return 0;
    }

    private void setImageSrc(String url, int position, ImageView imageView)
    {
        Log.i(TAG,url);
        UrlImageViewHelper.setUrlDrawable(imageView,url, R.drawable.tw__ic_logo_default,6000);

    }

    @Override
    public int getItemCount()
    {
        if(getRealmAdapter() != null)
        {
            return getRealmAdapter().getCount();
        }
        return 0;
    }

   public void pendingRemoval(final int position)
   {
       final FavNews favNews = getItem(position);
       if(!mItemsPendingRemoval.contains(favNews))
       {
           mItemsPendingRemoval.add(favNews);
           // this will redraw row in "undo" state
           notifyItemChanged(position);
           //let's create, store and post a runnable to remove the data
           Runnable pendingRemovalRunnable = new Runnable()
           {
               @Override
               public void run()
               {
                   remove(position);
               }
           };

           mHandler.postDelayed(pendingRemovalRunnable, PENDING_REMOVAL_TIMEOUT);
           mPendingRunnables.put(favNews.getId(),pendingRemovalRunnable);

       }

   }

   public void remove(int position)
   {
       FavNews favNews = getItem(position);
       RealmResults<FavNews> results = getRealmResults();

       if(mItemsPendingRemoval.contains(favNews))
       {
           mItemsPendingRemoval.remove(favNews);
       }

       if(results.contains(favNews))
       {
           removeRealm(position,results);
       }

   }

   private void removeRealm(int position,RealmResults<FavNews> results)
   {
       // All changes to data must happen in a transaction
       mRealm.beginTransaction();

       // remove single match
       results.remove(position);
       mRealm.commitTransaction();
       notifyDataSetChanged();

   }

   public boolean isPendingRemoval(int position)
   {
       FavNews favNews = getItem(position);
       return mItemsPendingRemoval.contains(favNews);

   }


    public class FavNewsViewHolder extends RecyclerView.ViewHolder
    {
        private final String TAG = "FavNewsViewHolder";
        private final TextView mTextView;
        private final ImageView mImageView;
        private View mView = null;
        private FavNews mFavNews;

        private LinearLayout mRegularLayout;
        private LinearLayout mSwipeLayout;
        private TextView mUndo;

        public FavNewsViewHolder(View v, Context context)
        {
            super(v);
            mView = v;

            mTextView = (TextView) v.findViewById(R.id.row_textview);
            mImageView = (ImageView) v.findViewById(R.id.row_imageview);

            mRegularLayout = (LinearLayout) v.findViewById(R.id.regularItem);
            mSwipeLayout = (LinearLayout) v.findViewById(R.id.swipeItem);
            mUndo = (TextView) v.findViewById(R.id.undo);

            mRegularLayout.setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View v)
                {
                     setFontSize(mOnNewsClickListener.onSetFontSize());
                        startWebViewActivity();

                }
            });


        }

        private void startWebViewActivity()
        {
            Intent intent = new Intent(mContext, WebViewActivity.class);

            intent.putExtra(News.KEY_ID,mFavNews.getId());
            intent.putExtra(News.KEY_URL,mFavNews.getUrl());
            intent.putExtra(News.KEY_TITLE, mFavNews.getTitle());
            intent.putExtra(News.KEY_IMG_URL, mFavNews.getImgUrl());
            intent.putExtra(News.KEY_FONT_SIZE, getFontSize());

            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

            mContext.startActivity(intent);
        }

        public View getView()
        {
            return mView;
        }

        public void setView(View view)
        {
            mView = view;
        }

        public FavNews getFavNews()
        {
            return mFavNews;
        }

        public void setFavNews(FavNews favNews)
        {
            mFavNews = favNews;
        }

        public TextView getTextView()
        {
            return mTextView;
        }

        public ImageView getImageView()
        {
            return mImageView;
        }
    } //end of viewholder
}

