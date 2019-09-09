package bori.bori.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import bori.bori.R;

public class EmptyFragment extends Fragment
{
    static public final String TAG = "EmptyFragment";

    private EditText mEditText;
    private String mMessage;

    public EmptyFragment()
    {
    }

    public EmptyFragment(String msg)
    {
        this.mMessage = msg;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState)
    {
        View view = inflater.inflate(R.layout.fragment_empty,container,false);

        mEditText = view.findViewById(R.id.empty_text);
        mEditText.setText(mMessage);

        return view;

    }

    public void setMessage(String msg)
    {
        mMessage = msg;
        mEditText.setText(mMessage);
    }
}
