package bori.bori.fragment;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.ContextThemeWrapper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import bori.bori.R;
import bori.bori.activity.UserSettingsActivity;
import bori.bori.application.BoriApplication;
import bori.bori.user.MyUser;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.koushikdutta.urlimageviewhelper.UrlImageViewHelper;

public class UserBottomSheetDialogFragment extends BottomSheetDialogFragment
{
    static public final String TAG = "UserBottomSheetDialogFragment";

    private MyUser mMyUser;

    private ImageView mUserProfile;
    private TextView mUserName;
    private TextView mUserEmail;
    private LinearLayout mLinearLayout;
    private Boolean mIsDark = false;

    public UserBottomSheetDialogFragment()
    {
        mMyUser = new MyUser(getContext());
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
         if(BoriApplication.getInstance().isNightModeEnabled())
         {
             setStyle(BottomSheetDialogFragment.STYLE_NO_TITLE, R.style.DarkBottomSheetDialogTheme);
             mIsDark = true;
         }
         else
         {
             setStyle(BottomSheetDialogFragment.STYLE_NO_TITLE, R.style.BottomSheetDialogTheme);
             mIsDark = false;
         }

    }

    public static UserBottomSheetDialogFragment newInstance()
    {
        return new UserBottomSheetDialogFragment();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState)
    {

        //final Context contextThemeWrapper = new ContextThemeWrapper(getActivity(), R.style.BottomSheetDialogTheme);
        //LayoutInflater locaInflater = inflater.cloneInContext(contextThemeWrapper);

        View view = inflater.inflate(R.layout.user_bottom_sheet, container, false);

        if(mIsDark)
            setDarkTheme(view);


        findView(view);

        readUserInfo();
        setUserInfo();

        return view;
    }

    private void setDarkTheme(View view)
    {

        view.setBackgroundColor(ContextCompat.getColor(getContext(),R.color.darkColorPrimary));

        View dividerTop  = view.findViewById(R.id.dividerTop);
        dividerTop.setBackgroundColor(ContextCompat.getColor(getContext(),R.color.darkColorPrimaryLight));

        View dividerBttom = view.findViewById(R.id.divider_bttm);
        dividerBttom.setBackgroundColor(ContextCompat.getColor(getContext(),R.color.darkColorPrimaryLight));

    }

    private void setUserInfo()
    {
         UrlImageViewHelper.setUrlDrawable(mUserProfile, mMyUser.getProfileUrl());
         mUserName.setText(mMyUser.getName());
         mUserEmail.setText(mMyUser.getEmail());
    }

    private void findView(View view)
    {
        mUserProfile = view.findViewById(R.id.user_profile_pic) ;
        mUserName = view.findViewById(R.id.bottom_sheet_user_name);
        mUserEmail = view.findViewById(R.id.bottom_sheet_email);
        mLinearLayout = view.findViewById(R.id.setting_layout);

        mLinearLayout.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                showSettingActivity();
                dismiss();

            }
        });
    }

    private void showSettingActivity()
    {
        Intent intent = new Intent(getActivity(), UserSettingsActivity.class);
        startActivity(intent);
    }

    private void readUserInfo()
    {
        Bundle bundle = getArguments();

        if(bundle != null)
        {
            mMyUser = bundle.getParcelable(MyUser.KEY_MYUSER);
        }

    }

}
