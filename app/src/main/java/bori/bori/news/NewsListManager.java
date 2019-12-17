package bori.bori.news;

import java.util.List;

public class NewsListManager
{
    private List<News> mNewsList;

    public NewsListManager(List<News> newsList)
    {
        mNewsList = newsList;
    }

    public List<News> getNewsList()
    {
        return mNewsList;
    }

    public List<News> getSubNewsList(final int size)
    {

        if(mNewsList.size() < size)
            return mNewsList;
        else
            return mNewsList.subList(0,size);

    }
}
