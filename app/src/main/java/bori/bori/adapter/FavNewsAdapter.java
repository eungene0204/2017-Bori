package bori.bori.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.RecyclerView;
import bori.bori.R;
import bori.bori.activity.WebViewActivity;
import bori.bori.fragment.FavNewsBottomSheetFragment;
import bori.bori.fragment.RcmdNewsBottomSheetDialogFragment;
import bori.bori.news.News;
import bori.bori.news.NewsHelper;
import bori.bori.realm.FavNews;
import com.koushikdutta.urlimageviewhelper.UrlImageViewHelper;
import io.realm.OrderedRealmCollection;
import io.realm.Realm;
import io.realm.RealmRecyclerViewAdapter;
import io.realm.RealmResults;

import java.util.Map;

public class FavNewsAdapter extends RealmRecyclerViewAdapter
{
    private final String TAG = "FavNewsAdapter";
    private FragmentManager mFragmentManager;
    private int mFontSize;
    private Activity mContext;
    private Realm mRealm;
    private NewsHelper mNewsHelper;
    private Drawable mSourceLogo = null;

    public FavNewsAdapter(@Nullable OrderedRealmCollection data, boolean autoUpdate)
    {
        super(data, autoUpdate);
    }

    public FavNewsAdapter(RealmResults<FavNews> list, Activity activity,FragmentManager fragmentManager)
    {
        super(list,true,true);

        this.mContext = activity;

        mRealm = Realm.getDefaultInstance();
        mNewsHelper = new NewsHelper(mContext);
        this.mFragmentManager = fragmentManager;

    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType)
    {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.fav_news_card,parent,false);

        ViewHolder viewHolder = new ViewHolder(v,mContext);

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position)
    {
        ViewHolder _holder = (ViewHolder) holder;
        FavNews news = (FavNews) getItem(position);

        String title = news.getTitle();
        _holder.mTitleView.setText(title);

        ImageView imageView = _holder.mNewsImgView;
        String imgUrl = news.getImgUrl();

        String source = news.getSource();
        _holder.mSourceView.setText(source);

        mSourceLogo = findSourceLogo(source);

        setImageSrc(imgUrl,imageView,mNewsHelper.getSourceLogo());

        setSourceLogo(source,_holder);

        _holder.setNews(news);


    }

    private Drawable findSourceLogo(String source)
    {

        Map<String, Drawable> logoMap = mNewsHelper.getSourceLogo();
        Drawable logo = null;


        if(logoMap.containsKey(source))
        {
            logo = logoMap.get(source);
        }

        return logo;

    }

    private void setImageSrc(String url, ImageView imageView, Map<String,Drawable> logoMap)
    {

        if(url.isEmpty())
        {
            if(null != mSourceLogo)
            {
                imageView.setImageDrawable(mSourceLogo);
            }
            else
            {
                imageView.setVisibility(View.GONE);
            }

        }
        else
        {
            UrlImageViewHelper.setUrlDrawable(imageView,url, null,6000);
        }
    }



    public class ViewHolder extends RecyclerView.ViewHolder
    {
        private Context mContext;

        private FavNews mNews;

        public TextView mTitleView;
        public ImageView mNewsImgView;
        public TextView mSourceView;
        public ImageView mCardOverFlow;

        public ImageView mSourceLogo;
        public CardView mCardView;

        public ViewHolder(View v, Context context)
        {
            super(v);
            mContext = context;

            mCardView = v.findViewById(R.id.card_view);

            mTitleView =  v.findViewById(R.id.card_title);
            mNewsImgView =  v.findViewById(R.id.news_img);
            mSourceView = v.findViewById(R.id.card_source);
            mCardOverFlow = v.findViewById(R.id.card_overflow);
            mSourceLogo = v.findViewById(R.id.source_img);

            setRoundedImg();

            mCardView.setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View v)
                {
                     //setFontSize(mOnNewsClickListener.onSetFontSize());
                    startWebViewActivity(getNews());

                }
            });


            mCardOverFlow.setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View v)
                {
                    showNewsBottomSheet(getNews());
                }
            });

        }

        private void showNewsBottomSheet(FavNews news)
        {

            FavNewsBottomSheetFragment favNewsBottomSheetFragment=
                    FavNewsBottomSheetFragment.newInstance();

            Bundle bundle = new Bundle();
            bundle.putString(FavNews.KEY_ID, news.getId());
            bundle.putString(FavNews.KEY_URL, news.getUrl());
            bundle.putString(FavNews.KEY_TITLE, news.getTitle());
            bundle.putString(FavNews.KEY_CATEGORY, news.getCategory());

            favNewsBottomSheetFragment.setArguments(bundle);

            favNewsBottomSheetFragment.show(mFragmentManager,
                    RcmdNewsBottomSheetDialogFragment.TAG);

        }


        private void setRoundedImg()
        {
            GradientDrawable drawable = (GradientDrawable) mContext.getDrawable(R.drawable.round_background);
            mNewsImgView.setBackground(drawable);
            mNewsImgView.setClipToOutline(true);

        }


        private void startWebViewActivity(FavNews favNews)
        {
            Intent intent = new Intent(mContext, WebViewActivity.class);

            News news = new News();
            news.setId(favNews.getId());
            news.setUrl(favNews.getUrl());
            news.setTitle(favNews.getTitle());

            intent.putExtra(News.TAG, news);
            intent.putExtra(News.KEY_URL_TYPE, WebViewActivity.TYPE_NEWS_URL);

            intent.putExtra(News.KEY_FONT_SIZE, getFontSize());
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

            mContext.startActivity(intent);
        }

        public FavNews getNews()
        {
            return mNews;
        }

        public void setNews(FavNews news)
        {
            mNews = news;
        }
    }

     public int getFontSize()
    {
        return mFontSize;
    }

    public void setFontSize(int fontSize)
    {
        mFontSize = fontSize;
    }

    public interface OnNewsClickListener
    {
        int onSetFontSize();
    }

     private void setSourceLogo(String source, ViewHolder holder)
    {

        if(null != mSourceLogo)
        {
            holder.mSourceLogo.setImageDrawable(mSourceLogo);
            holder.mSourceView.setVisibility(View.INVISIBLE);
            holder.mSourceLogo.setVisibility(View.VISIBLE);
        }
        else
        {
            holder.mSourceLogo.setVisibility(View.INVISIBLE);
            holder.mSourceView.setVisibility(View.VISIBLE);
        }

    }



}
