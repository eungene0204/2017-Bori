package bori.bori.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import bori.bori.R;
import bori.bori.news.News;
import bori.bori.realm.FavNews;
import bori.bori.realm.RealmHelper;
import bori.bori.utility.MenuUtils;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

public class FavNewsBottomSheetFragment extends BottomSheetDialogFragment
{
    public static final String TAG = "FavNewsBttmFrgmt";
    private News mNews;
    private RealmHelper mHelper;

    public static FavNewsBottomSheetFragment newInstance()
    {
        return new FavNewsBottomSheetFragment();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setStyle(BottomSheetDialogFragment.STYLE_NO_TITLE, R.style.BottomSheetDialogTheme);

    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState)
    {

        View view = inflater.inflate(R.layout.fav_news_bottom_sheet, container, false);

        readNewsInfo();

        mHelper = new RealmHelper(getContext());

        LinearLayout shareLayout = view.findViewById(R.id.share_layout);

        shareLayout.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                Intent intent = MenuUtils.createShareIntent(mNews,getActivity());
                String share = getString(R.string.news_share);
                Intent chooser = Intent.createChooser(intent,share);

                startActivity(chooser);

                dismiss();

            }
        });

        LinearLayout removelayout = view.findViewById(R.id.news_remove);
        removelayout.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                removeNews(mNews);
                dismiss();
            }

        });


        return view;

    }

    private void removeNews(final News news)
    {
        mHelper.delete(news.getId(),news.getCategory());
    }

    private void readNewsInfo()
    {
        Bundle bundle = getArguments();

        if(null != bundle)
        {
            News news = new News();

            String id = bundle.getString(FavNews.KEY_ID);
            String title = bundle.getString(FavNews.KEY_TITLE);
            String url =  bundle.getString(FavNews.KEY_URL);
            String category = bundle.getString(FavNews.KEY_CATEGORY);

            news.setId(id);
            news.setTitle(title);
            news.setUrl(url);
            news.setCategory(category);

            mNews = news;

        }
    }
}
