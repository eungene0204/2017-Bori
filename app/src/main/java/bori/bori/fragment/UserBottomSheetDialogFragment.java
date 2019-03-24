package bori.bori.fragment;

import android.content.Context;
import android.os.Bundle;
import android.view.ContextThemeWrapper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import bori.bori.R;
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

    public UserBottomSheetDialogFragment()
    {
        mMyUser = new MyUser(getContext());
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setStyle(BottomSheetDialogFragment.STYLE_NO_TITLE, R.style.BottomSheetDialogTheme);
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


        findView(view);

        readUserInfo();
        setUserInfo();

        return view;
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
