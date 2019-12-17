package bori.bori.adapter;

import android.app.Activity;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;

import bori.bori.R;
import bori.bori.bottomSheet.BottomSheetManager;
import bori.bori.databinding.RcmdNewsSubItemBinding;
import bori.bori.fragment.bottom.RcmdNewsBottomSheetDialogFragment;
import bori.bori.news.Image.NewsImgManager;
import bori.bori.news.News;

import java.util.List;

public class RecommendSubNewsAdapter extends RecyclerView.Adapter
{
    private List<News> mNewsList;
    private RcmdNewsSubItemBinding mRcmdNewsSubItemBinding;
    private final Activity mActivity;
    private final FragmentManager mFragmentManager;

    public RecommendSubNewsAdapter(final List<News> newsList, Activity activity,
                                   FragmentManager fragmentManager)
    {
        mNewsList = newsList;
        mActivity = activity;
        mFragmentManager = fragmentManager;
    }


    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType)
    {
        RcmdNewsSubItemBinding rcmdNewsSubItemBinding=
                RcmdNewsSubItemBinding.inflate(LayoutInflater.from(parent.getContext()),
                        parent,false);

        return new ViewHolder(rcmdNewsSubItemBinding);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position)
    {
        ViewHolder viewHolder = (ViewHolder) holder;
        News news = mNewsList.get(position);

        setSourceLogo(news);

        viewHolder.bind(news);
    }

    private void setSourceLogo(News news)
    {
        if(news.getSourceLogo() != null)
            news.setStringSrc("");
    }



    @Override
    public int getItemCount()
    {
        return mNewsList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder
    {
        public RcmdNewsSubItemBinding mBinding;
        private ImageView mNewsImg;
        private ImageView mDownArrow;

        public ViewHolder(RcmdNewsSubItemBinding binding)
        {
            super(binding.getRoot());
            this.mBinding = binding;

            mNewsImg = mBinding.getRoot().findViewById(R.id.news_img);

            NewsImgManager.setRoundedImg(mNewsImg, mActivity);
        }

        public void bind(News news)
        {
            mBinding.setNews(news);
            setImageSrc(news);
            mDownArrow = mBinding.getRoot().findViewById(R.id.down_arrow);
            mDownArrow.setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View view)
                {
                    BottomSheetManager.showBottomSheet(news, RcmdNewsBottomSheetDialogFragment.newInstance(),
                            mFragmentManager);

                }
            });

            mBinding.executePendingBindings();
        }

        private void setImageSrc(News news)
        {
            String url = news.getImgUrl();

            Glide.with(mActivity).load(url)
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
                            int a =10 ;
                            return false;
                        }
                    })
                    .placeholder(R.drawable.ic_rss_grey)
                    .into(mNewsImg);
        }


    }

}
