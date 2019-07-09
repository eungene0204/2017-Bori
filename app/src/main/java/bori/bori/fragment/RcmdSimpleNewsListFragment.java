package bori.bori.fragment;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import bori.bori.R;
import bori.bori.activity.WebViewActivity;
import bori.bori.news.News;
import bori.bori.realm.RealmHelper;
import bori.bori.utility.MenuUtils;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

public class RcmdSimpleNewsListFragment extends BottomSheetDialogFragment
{
     public static final String TAG = "RcmdSimpleNewsFragment";

    private News mNews = new News();
    private RealmHelper mHelper;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setStyle(BottomSheetDialogFragment.STYLE_NO_TITLE, R.style.BottomSheetDialogTheme);

    }

    public static RcmdSimpleNewsListFragment newInstance()
    {
        return new RcmdSimpleNewsListFragment();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState)
    {
        View view = inflater.inflate(R.layout.rcmd_simple_news_bottom, container, false);

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

            }
        });

        LinearLayout bookmarkLayout = view.findViewById(R.id.news_bookmark);
        bookmarkLayout.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                mHelper.bookMarkNews(mNews);
                dismiss();

            }

        });


        LinearLayout sourceLayout = view.findViewById(R.id.news_source);
        sourceLayout.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                startWebView();
                dismiss();
            }
        });

        return view;

    }

    private void startWebView()
    {

        Context context = getContext();
        Intent intent = new Intent(context, WebViewActivity.class);

        intent.putExtra(News.TAG, mNews);
        intent.putExtra(News.KEY_URL_TYPE,WebViewActivity.TYPE_SOURCE_URL);
        //intent.putExtra(News.KEY_FONT_SIZE, getFontSize());

        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);

    }

    private void readNewsInfo()
    {
        Bundle bundle = getArguments();

        if(null != bundle)
        {
            mNews = bundle.getParcelable(News.TAG);

        }
    }
}
