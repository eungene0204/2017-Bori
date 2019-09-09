package bori.bori.adapter;

import android.app.Activity;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.asynclayoutinflater.view.AsyncLayoutInflater;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.koushikdutta.urlimageviewhelper.UrlImageViewHelper;

import bori.bori.R;
import bori.bori.databinding.RcmdNewsSubItemBinding;
import bori.bori.news.News;
import bori.bori.news.SrcLogoManager;

import java.util.List;

public class RecommendSubNewsAdapter extends RecyclerView.Adapter
{
    private List<News> mNewsList;
    private RcmdNewsSubItemBinding mRcmdNewsSubItemBinding;
    private final Activity mActivity;

    public RecommendSubNewsAdapter(final List<News> newsList, Activity activity)
    {
        mNewsList = newsList;
        mActivity = activity;
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


        viewHolder.bind(news);
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
        return mNewsList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder
    {
        public RcmdNewsSubItemBinding mBinding;

        public ViewHolder(RcmdNewsSubItemBinding binding)
        {
            super(binding.getRoot());
            this.mBinding = binding;
        }

        public void bind(News news)
        {
            mBinding.setNews(news);
            mBinding.executePendingBindings();
        }

        private void showNewsBottomSheet(News news)
        {
            /*
            RcmdNewsBottomSheetDialogFragment rcmdNewsBottomSheetDialogFragment =
                    RcmdNewsBottomSheetDialogFragment.newInstance();

            Bundle bundle = new Bundle();
            bundle.putParcelable(News.TAG, news);

            rcmdNewsBottomSheetDialogFragment.setArguments(bundle);

            rcmdNewsBottomSheetDialogFragment.show(mFragmentManager,
                    RcmdNewsBottomSheetDialogFragment.TAG); */

        }


    }

}
