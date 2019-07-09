package bori.bori.adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.RecyclerView;
import bori.bori.R;
import bori.bori.activity.RcmdNewsListActivity;
import bori.bori.activity.WebViewActivity;
import bori.bori.fragment.RcmdNewsBottomSheetDialogFragment;
import bori.bori.fragment.RcmdSimpleNewsListFragment;
import bori.bori.news.Category;
import bori.bori.news.News;
import com.koushikdutta.urlimageviewhelper.UrlImageViewHelper;

import java.util.ArrayList;
import java.util.List;

public class RecommendCardAdapter extends RecyclerView.Adapter
{
    static final String TAG = "RemdCardAdapter";

    private FragmentManager mFragmentManager;
    private Context mContext;
    private List<Category> mCategoryList;
    private FragmentActivity mActivity;
    private LayoutInflater mLayoutInflater;
    private List<SubViewHolder> mSubViewHolders;

    public RecommendCardAdapter(FragmentManager fragmentManager, Context context, List<Category> categoryList, FragmentActivity activity)
    {
        mCategoryList = categoryList;
        mFragmentManager = fragmentManager;
        mContext = context;
        mActivity = activity;

        mLayoutInflater= (LayoutInflater)
                mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);


    }

    public void initSubViewHolder(int newsSize)
    {
        mSubViewHolders = new ArrayList<>();

        int size = newsSize * 5;

        for(int i=0; i < size; i++)
        {
            View view = mLayoutInflater.inflate(R.layout.rcmd_news_simple_item,null);

            SubViewHolder subViewHolder = new SubViewHolder(view);
            subViewHolder.setView(view);
            mSubViewHolders.add(subViewHolder);
        }

    }

    private SubViewHolder getUnusedHolder()
    {
        SubViewHolder subViewHolder = null;
        if( mSubViewHolders == null) initSubViewHolder(mCategoryList.size());

        for(SubViewHolder holder : mSubViewHolders)
        {
            if(!holder.hasItParent())
            {
                subViewHolder = holder;
                mSubViewHolders.remove(holder);
                return subViewHolder;
            }

        }

        return subViewHolder;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType)
    {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.rcmd_news_card,
                parent,false);


        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position)
    {
        ViewHolder viewHolder = (ViewHolder) holder;
        Category category = mCategoryList.get(position);

        String categoryTitle = category.getCategory();
        viewHolder.mCategory.setText(categoryTitle);

        setLevelBar(category.getLevel(),viewHolder);

        addNewsView(viewHolder, category.getNewsList());
        //addShowMoreView(category.getNewsList(),viewHolder.mLinearLayout );

        viewHolder.mNews = category.getNewsList().get(0);


    }

    private void addNewsView(ViewHolder viewHolder,List<News> newsList)
    {
        //viewHolder.mLinearLayout.removeAllViews();

        int size = 0;

        if(newsList.size() < 5)
            size = newsList.size();
        else
            size = 5;

        for(int i=0; i < size; i++)
        {
            News news = newsList.get(i);
            SubViewHolder subHolder = viewHolder.mSubViewHolderList.get(i);
            subHolder.getView().setVisibility(View.VISIBLE);

            subHolder.setNews(news);
            //viewHolder.setSubViewHolder(holder);
            setNewsValue(news,subHolder);

        }

    }



    private void addShowMoreView(List<News> newsList, LinearLayout linearLayout)
    {
        if(5 < newsList.size())
        {
            View view = mLayoutInflater.inflate(R.layout.rcmd_show_more,null);
            view.setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View v)
                {
                   startNewsActivity(newsList);
                    
                }
            });

            linearLayout.addView(view);

        }
    }

    private void startNewsActivity(List<News> newsList)
    {
        Intent intent = new Intent(mActivity, RcmdNewsListActivity.class);

        intent.putParcelableArrayListExtra(News.KEY_NEWS_LIST,(ArrayList<News>)newsList);
        mContext.startActivity(intent);

    }

    private void setNewsValue(News news, SubViewHolder viewHolder)
    {
        String title = news.getTitle();
        viewHolder.mTitile.setText(title);

        String imgUrl = news.getImgUrl();
        setImageSrc(imgUrl,viewHolder.mNewsImg,news);

        String source = news.getSource();
        viewHolder.mSourceText.setText(source);

        String date = news.getDate();
        viewHolder.mDate.setText(date);

        Drawable logo = news.getSourceLogo();

        ImageView source_img = viewHolder.mSourceImg;
        setSourceLogo(logo,source_img, viewHolder.mSourceText);

        viewHolder.setNews(news);

        /*
        if(viewHolder.mView.getParent() != null)
        {
            ((ViewGroup)viewHolder.mView.getParent()).removeView(viewHolder.mView);
        }


        linearLayout.addView(viewHolder.mView); */
    }

    private void setSourceLogo(Drawable logo, ImageView source_img, TextView source_text)
    {
        if(null != logo)
        {
            source_img.setImageDrawable(logo);
            source_text.setVisibility(View.INVISIBLE);
            source_img.setVisibility(View.VISIBLE);
        }
        else
        {
            source_img.setVisibility(View.INVISIBLE);
            source_text.setVisibility(View.VISIBLE);
        }

    }


    private void setImageSrc(String url, ImageView imageView, News news)
    {

        if(url.isEmpty())
        {
            imageView.setImageDrawable(news.getSourceLogo());
        }
        else
        {
            UrlImageViewHelper.setUrlDrawable(imageView,url, news.getSourceLogo(),6000);
        }
    }



    @Override
    public int getItemCount()
    {
        return mCategoryList == null ? 0 : mCategoryList.size();
    }

    private void startWebViewActivity(News news)
    {
        Intent intent = new Intent(mContext, WebViewActivity.class);

        intent.putExtra(News.TAG, news);
        intent.putExtra(News.KEY_URL_TYPE, WebViewActivity.TYPE_NEWS_URL);
        //intent.putExtra(News.KEY_FONT_SIZE, getFontSize());
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

        mContext.startActivity(intent);

    }

    @Override
    public long getItemId(int position)
    {
        return mCategoryList.get(position).hashCode();
    }

    public class ViewHolder extends RecyclerView.ViewHolder
    {

        public View mLevelBar;
        public ImageView mCardOverFlow;
        public TextView mCategory;
        public LinearLayout mLinearLayout;
        //public SubViewHolder mSubViewHolder;
        public List<SubViewHolder> mSubViewHolderList;
        public View[] mItemArr = new View[5];
        public News mNews;

        public ViewHolder(@NonNull View itemView)
        {
            super(itemView);

            mLevelBar = itemView.findViewById(R.id.level_bar);
            mCategory = itemView.findViewById(R.id.card_category);
            mCardOverFlow = itemView.findViewById(R.id.card_overflow);
            mLinearLayout = itemView.findViewById(R.id.news_layout);

            mItemArr[0] = itemView.findViewById(R.id.item1);
            mItemArr[1] = itemView.findViewById(R.id.item2);
            mItemArr[2] = itemView.findViewById(R.id.item3);
            mItemArr[3] = itemView.findViewById(R.id.item4);
            mItemArr[4] = itemView.findViewById(R.id.item5);

            mSubViewHolderList = new ArrayList<>();
            setSubViewHolderList();

            mCardOverFlow.setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View v)
                {
                    showNewsBottomSheet(mNews);
                }

            });

        }

        private void setSubViewHolderList()
        {
            for(View view: mItemArr)
            {
                SubViewHolder subViewHolder = new SubViewHolder(view);
                mSubViewHolderList.add(subViewHolder);
            }
        }

        public void setSubViewHolder(SubViewHolder subViewHolder)
        {
            //mSubViewHolder = subViewHolder;
        }


        private void showNewsBottomSheet(News news)
        {

            RcmdNewsBottomSheetDialogFragment rcmdNewsBottomSheetDialogFragment =
                    RcmdNewsBottomSheetDialogFragment.newInstance();

            Bundle bundle = new Bundle();
            bundle.putParcelable(News.TAG, news);

            rcmdNewsBottomSheetDialogFragment.setArguments(bundle);

            rcmdNewsBottomSheetDialogFragment.show(mFragmentManager,
                    RcmdNewsBottomSheetDialogFragment.TAG);

        }


    }


     private void setLevelBar(String level, ViewHolder holder)
    {
        View bar = holder.mLevelBar;

        if(level.equals("1"))
        {
            bar.setBackgroundTintList(ContextCompat.
                    getColorStateList(mContext,R.color.sim_level_1));

        }
        else if(level.equals("2"))
        {
            bar.setBackgroundTintList(ContextCompat.
                    getColorStateList(mContext,R.color.sim_level_2));

        }
        else if(level.equals("3"))
        {
            bar.setBackgroundTintList(ContextCompat.
                    getColorStateList(mContext,R.color.sim_level_3));

        }
        else if(level.equals("4"))
        {
            bar.setBackgroundTintList(ContextCompat.
                    getColorStateList(mContext,R.color.sim_level_4));

        }
        else if(level.equals("5"))
        {
            bar.setBackgroundTintList(ContextCompat.
                    getColorStateList(mContext,R.color.sim_level_5));

        }

    }

    public class SubViewHolder extends RecyclerView.ViewHolder
    {
        public TextView mTitile;
        public ImageView mNewsImg;
        public TextView mSourceText;
        public ImageView mSourceImg;
        public ImageView mDownArrow;
        public TextView mDate;
        public View mView;

        public TextView getTitile()
        {
            return mTitile;
        }

        public void setTitile(TextView titile)
        {
            mTitile = titile;
        }

        public ImageView getNewsImg()
        {
            return mNewsImg;
        }

        public void setNewsImg(ImageView newsImg)
        {
            mNewsImg = newsImg;
        }

        public TextView getSourceText()
        {
            return mSourceText;
        }

        public void setSourceText(TextView sourceText)
        {
            mSourceText = sourceText;
        }

        public ImageView getSourceImg()
        {
            return mSourceImg;
        }

        public void setSourceImg(ImageView sourceImg)
        {
            mSourceImg = sourceImg;
        }

        public ImageView getDownArrow()
        {
            return mDownArrow;
        }

        public void setDownArrow(ImageView downArrow)
        {
            mDownArrow = downArrow;
        }

        public TextView getDate()
        {
            return mDate;
        }

        public void setDate(TextView date)
        {
            mDate = date;
        }

        public View getView()
        {
            return mView;
        }

        public void setView(View view)
        {
            mView = view;
        }

        private News mNews;

        public SubViewHolder(@NonNull View itemView)
        {
            super(itemView);

            mTitile = itemView.findViewById(R.id.title);
            mNewsImg= itemView.findViewById(R.id.news_img);
            mSourceText= itemView.findViewById(R.id.source_text);
            mDate = itemView.findViewById(R.id.date);
            mSourceImg= itemView.findViewById(R.id.source_img);
            mDownArrow = itemView.findViewById(R.id.down_arrow);

            mView = itemView;

            mView.setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View v)
                {

                    startWebViewActivity(mNews);

                }
            });

            mDownArrow.setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View v)
                {
                    showSimpleBottomSheet(mNews);

                }
            });


        }

        public void SetNews(News news)
        {
            mNews = news;
        }


        private void showSimpleBottomSheet(News news)
        {

             RcmdSimpleNewsListFragment rcmdSimpleNewsListFragment =
                    RcmdSimpleNewsListFragment.newInstance();

            Bundle bundle = new Bundle();
            bundle.putParcelable(News.TAG, news);

            rcmdSimpleNewsListFragment.setArguments(bundle);

            rcmdSimpleNewsListFragment.show(mFragmentManager,
                    RcmdSimpleNewsListFragment.TAG);

        }

        public boolean hasItParent()
        {
            return mView.getParent()  == null ? false : true ;

        }

        public News getNews()
        {
            return mNews;
        }

        public void setNews(News news)
        {
            mNews = news;
        }
    }




}
