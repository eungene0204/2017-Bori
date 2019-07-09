package bori.bori.activity;

import android.app.Activity;
import android.content.Intent;
import androidx.annotation.IdRes;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import bori.bori.R;
import bori.bori.utility.FontUtils;

public class FontSizeAcitivity extends AppCompatActivity
{
    private RadioButton mMiddleBtn;
    private RadioButton mLargeBtn;
    private RadioButton mXlargeBtn;

    private int mFontSize;
    private float mMiddle;
    private float mLarge;
    private float mXlarge;


    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_font_size);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setTitle(R.string.nav_font_size);

        mMiddle = getResources().getDimension(R.dimen.webview_text_size_middle);
        mLarge = getResources().getDimension(R.dimen.webview_text_size_large);
        mXlarge = getResources().getDimension(R.dimen.webview_text_size_xlarge);

        mMiddleBtn= (RadioButton) findViewById(R.id.radioBtn_middle);
        mMiddleBtn.setTextSize(mMiddle/ 6);

        mLargeBtn= (RadioButton) findViewById(R.id.radioBtn_large);
        mLargeBtn.setTextSize(mLarge/ 6);

        mXlargeBtn = (RadioButton) findViewById(R.id.radioBtn_xlarge);
        mXlargeBtn.setTextSize(mXlarge / 6);

        Intent intent = getIntent();
        if(null != intent)
            mFontSize = intent.getIntExtra(FontUtils.KEY_FONT_SIZE, (int) mMiddle);

        setCheckedButton(mFontSize);

        RadioGroup radioGroup = (RadioGroup) findViewById(R.id.radioGrp);
        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener()
        {
            @Override
            public void onCheckedChanged(RadioGroup group, @IdRes int checkedId)
            {
                int fontSize = (int)
                        getResources().getDimension(R.dimen.webview_text_size_middle);

                switch (checkedId)
                {
                    case R.id.radioBtn_middle:

                        fontSize = (int)
                                getResources().getDimension(R.dimen.webview_text_size_middle);
                        break;

                    case R.id.radioBtn_large:
                        fontSize = (int)
                                getResources().getDimension(R.dimen.webview_text_size_large);
                        break;

                    case R.id.radioBtn_xlarge:
                         fontSize = (int)
                                getResources().getDimension(R.dimen.webview_text_size_xlarge);
                        break;

                    default:
                        break;

                }
                Intent intent = new Intent();
                intent.putExtra(FontUtils.KEY_FONT_SIZE, fontSize);
                setResult(Activity.RESULT_OK, intent);
            }
        });

    }

    @Override
    public boolean onSupportNavigateUp()
    {
        onBackPressed();
        return true;
    }

    public void setCheckedButton(int fontSize)
    {
        if(fontSize == (int)mMiddle)
            mMiddleBtn.setChecked(true);
        else if(fontSize == (int) mLarge)
            mLargeBtn.setChecked(true);
        else if(fontSize == (int) mXlarge)
            mXlargeBtn.setChecked(true);
        else
            mMiddleBtn.setChecked(true);

    }
}
