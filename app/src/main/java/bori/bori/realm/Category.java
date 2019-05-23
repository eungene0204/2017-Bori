package bori.bori.realm;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Category extends RealmObject
{
    @PrimaryKey
    private String mCategory;

    private float mValue;

    public Category()
    {
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
