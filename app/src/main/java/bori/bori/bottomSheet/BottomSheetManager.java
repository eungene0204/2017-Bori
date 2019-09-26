package bori.bori.bottomSheet;

import android.os.Bundle;

import androidx.fragment.app.FragmentManager;

import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

import bori.bori.fragment.RcmdNewsBottomSheetDialogFragment;
import bori.bori.news.News;

public class BottomSheetManager
{
    private BottomSheetManager()
    {
    }

    static public void showBottomSheet(News news, BottomSheetDialogFragment bottomSheetDialogFragment,
                                     FragmentManager fragmentManager)
    {
        BottomSheetDialogFragment fragment = new BottomSheetDialogFragment();
        String fragmentTag = "";

        if(bottomSheetDialogFragment instanceof RcmdNewsBottomSheetDialogFragment)
        {
            fragment = RcmdNewsBottomSheetDialogFragment.newInstance();
            fragmentTag = RcmdNewsBottomSheetDialogFragment.TAG;

        }


        Bundle bundle = new Bundle();
        bundle.putParcelable(News.TAG, news);

        fragment.setArguments(bundle);

        fragment.show(fragmentManager, fragmentTag);

    }


}
