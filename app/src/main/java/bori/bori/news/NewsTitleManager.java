package bori.bori.news;

public class NewsTitleManager
{
    private NewsTitleManager()
    {
    }

    static public String getTitle(String title, String source)
    {
        int index = title.lastIndexOf("-");
        String sub = title.substring(0,index);

        return sub;

    }

}
