package bori.bori.fragment;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import bori.bori.R;
import bori.bori.news.NewsHelper;
import bori.bori.utility.SortUtils;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

public class SortNewsBottomSheetFragment extends BottomSheetDialogFragment
{
    public static final String TAG ="sortNewsBttmFrgmt" ;

    private ImageView mCheckSim;
    private ImageView mCheckSns;
    private NewsHelper.OnSortListener mOnSortListener;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        setStyle(BottomSheetDialogFragment.STYLE_NO_TITLE, R.style.BottomSheetDialogTheme);

    }

    public SortNewsBottomSheetFragment()
    {
    }

    public void setOnSortListener(NewsHelper.OnSortListener onSortListener)
    {
        mOnSortListener = onSortListener;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState)
    {
        View view = inflater.inflate(R.layout.sort_bottom_sheet, container, false);


        mCheckSim = view.findViewById(R.id.check_sim);
        mCheckSns = view.findViewById(R.id.check_sns);

        RelativeLayout relativeLayoutSim = view.findViewById(R.id.sort_sim_layout);
        relativeLayoutSim.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                sortBySim();
                dismiss();
            }
        });

        RelativeLayout relativeLayoutSns = view.findViewById(R.id.sort_sns_layout);
        relativeLayoutSns.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                sortBySns();
                dismiss();
            }
        });


        return view;

    }


    private void sortBySim()
    {

        mCheckSns.setVisibility(View.INVISIBLE);
        mCheckSim.setVisibility(View.VISIBLE);

        mOnSortListener.onSort(SortUtils.TYPE_SIM);
    }

    private void sortBySns()
    {
        mCheckSim.setVisibility(View.INVISIBLE);
        mCheckSns.setVisibility(View.VISIBLE);

        mOnSortListener.onSort(SortUtils.TYPE_SNS);
    }

}
