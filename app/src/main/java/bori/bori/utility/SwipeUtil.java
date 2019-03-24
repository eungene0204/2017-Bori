package bori.bori.utility;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.ItemTouchHelper;
import android.view.View;

import bori.bori.R;

/**
 * Created by Eugene on 2017-09-18.
 */

public abstract class SwipeUtil extends ItemTouchHelper.SimpleCallback
{


    private Drawable mBackground;
    private Drawable mDeleteIcon;

    private int mXMarkMargin;

    private boolean mInitiated;
    private Context mContext;

    private String mLeftSwipeLable;
    private int mLeftcolorCode;


    public SwipeUtil(int dragDirs, int swipeDirs, Context context)
    {
        super(dragDirs, swipeDirs);
        mContext = context;
    }

    private void init()
    {
        mBackground = new ColorDrawable();
        mXMarkMargin = (int)  mContext.getResources().getDimension(R.dimen.ic_clear_margin);
        mDeleteIcon = ContextCompat.getDrawable(mContext, android.R.drawable.ic_menu_delete);
        mDeleteIcon.setColorFilter(Color.WHITE, PorterDuff.Mode.SRC_ATOP);
        mInitiated = true;
    }

    @Override
    public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target)
    {
        return false;
    }

    @Override
    public abstract void onSwiped(RecyclerView.ViewHolder viewHolder, int direction);

    @Override
    public int getSwipeDirs(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder)
    {
        return super.getSwipeDirs(recyclerView, viewHolder);
    }

    @Override
    public void onChildDraw(Canvas c, RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, float dX, float dY, int actionState, boolean isCurrentlyActive)
    {
        View itemView = viewHolder.itemView;
        if (!mInitiated)
        {
            init();
        }

        int itemHeight = itemView.getBottom() - itemView.getTop();

        //Setting Swipe Background
        ((ColorDrawable) mBackground).setColor(getLeftcolorCode());
        mBackground.setBounds(itemView.getRight() + (int) dX, itemView.getTop(), itemView.getRight(), itemView.getBottom());
        mBackground.draw(c);

        int intrinsicWidth = mDeleteIcon.getIntrinsicWidth();
        int intrinsicHeight = mDeleteIcon.getIntrinsicWidth();

        int xMarkLeft = itemView.getRight() - mXMarkMargin- intrinsicWidth;
        int xMarkRight = itemView.getRight() - mXMarkMargin;
        int xMarkTop = itemView.getTop() + (itemHeight - intrinsicHeight) / 2;
        int xMarkBottom = xMarkTop + intrinsicHeight;


        //Setting Swipe Icon
        mDeleteIcon.setBounds(xMarkLeft, xMarkTop + 16, xMarkRight, xMarkBottom);
        //mDeleteIcon.draw(c);

        //Setting Swipe Text
        Paint paint = new Paint();
        paint.setColor(Color.WHITE);
        paint.setTextSize(48);
        paint.setTextAlign(Paint.Align.CENTER);
        c.drawText(getLeftSwipeLable(), xMarkLeft + 80, xMarkTop + 60, paint);
        super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive);
    }

    public String getLeftSwipeLable()
    {
        return mLeftSwipeLable;
    }

    public void setLeftSwipeLable(String leftSwipeLable) {
        this.mLeftSwipeLable = leftSwipeLable;
    }

    public int getLeftcolorCode()
    {
        return mLeftcolorCode;
    }

    public void setLeftcolorCode(int leftcolorCode)
    {
        this.mLeftcolorCode = leftcolorCode;
    }
}
