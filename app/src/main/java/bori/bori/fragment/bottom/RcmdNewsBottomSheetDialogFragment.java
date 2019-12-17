package bori.bori.fragment.bottom;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import bori.bori.R;
import bori.bori.news.News;
import bori.bori.realm.RealmHelper;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

public class RcmdNewsBottomSheetDialogFragment extends BottomSheetDialogFragment
{

    public static final String TAG = "NewsBtmSheetDialogFragment";

    private News mNews = new News();
    private RealmHelper mHelper;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setStyle(BottomSheetDialogFragment.STYLE_NO_TITLE, R.style.BottomSheetDialogTheme);

    }

   public static RcmdNewsBottomSheetDialogFragment newInstance()
    {
        return new RcmdNewsBottomSheetDialogFragment();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState)
    {
        View view = inflater.inflate(R.layout.rcmd_news_bottom_sheet, container, false);

        readNewsInfo();

        mHelper = new RealmHelper(getContext());


        LinearLayout snsLayout = view.findViewById(R.id.sns_original_layout);
        snsLayout.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                showOriginalSnsDialog(mNews.getOriginal());
                dismiss();

            }
        });


        return view;

    }


    private void showOriginalSnsDialog(String original)
    {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setTitle("SNS 원본");
        builder.setMessage(original);
        builder.setPositiveButton("확인",null);

        AlertDialog dialog = builder.create();
        dialog.show();

        Button positiveButton = dialog.getButton(AlertDialog.BUTTON_POSITIVE);
        positiveButton.setTextColor(getResources().getColor(R.color.colorAccent, null));
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
