package bori.bori.news;

import androidx.annotation.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Category
{
    private String mCategory;
    private List<News> mNewsList;
    private String mLevel;

    public Category(String category)
    {
        this.mCategory = category;
        mNewsList = new ArrayList<>();
    }

    public String getCategory()
    {
        return mCategory;
    }

    public void setCategory(String category)
    {
        mCategory = category;
    }

    public List<News> getNewsList()
    {
        return mNewsList;
    }

    public void setNewsList(List<News> newsList)
    {
        mNewsList = newsList;
    }

    public void addNews(News news)
    {
        mNewsList.add(news);
    }


    @Override
    public int hashCode()
    {
        return Objects.hashCode(getCategory());
    }

    @Override
    public boolean equals(@Nullable Object obj)
    {
        if(this == obj) return true;
        if(!(obj instanceof Category)) return false;
        Category category= (Category) obj;

        return getCategory().equals(category.getCategory());
    }

    public String getLevel()
    {
        return mLevel;
    }

    public void setLevel(String level)
    {
        mLevel = level;
    }
}
