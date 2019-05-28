package bori.bori.realm;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

import java.util.Date;

public class SavedCategory extends RealmObject
{

    public SavedCategory()
    {
    }

    @PrimaryKey
    private String mCategory;

    private float mValue;
    private int mToday;
    private Date mDate;


    public int getToday()
    {
        return mToday;
    }

    public void setToday(int today)
    {
        mToday = today;
    }

    public Date getDate()
    {
        return mDate;
    }

    public void setDate(Date date)
    {
        mDate = date;
    }

    public void increaseVal()
    {
        mValue += 1;
    }

    public void decreaseVal()
    {
        mValue -= 1;
    }

    public String getCategory()
    {
        return mCategory;
    }

    public void setCategory(String category)
    {
        mCategory = category;
    }

    public float getValue()
    {
        return mValue;
    }

    public void setValue(int value)
    {
        mValue = value;
    }

}
