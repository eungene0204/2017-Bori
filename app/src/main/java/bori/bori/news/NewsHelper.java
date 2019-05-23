package bori.bori.news;

import android.app.Activity;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Parcelable;
import android.util.Log;
import androidx.core.content.ContextCompat;
import bori.bori.R;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class NewsHelper
{
    public static final String TAG = "NewsHelper";

    private Activity mActivity;
    private Map<String, Drawable> mSourceLogo;

    public NewsHelper(Activity activity)
    {
        this.mActivity = activity;
        this.mSourceLogo = new HashMap<String, Drawable>();

        setSourceLogo();

    }

    public Map<String,Drawable> getSourceLogo()
    {
        return mSourceLogo;
    }

    private void setSourceLogo()
    {
        mSourceLogo.put("default",ContextCompat.getDrawable(mActivity,R.drawable.ic_rss_grey));

        mSourceLogo.put("아시아경제", ContextCompat.getDrawable(mActivity, R.drawable.asia_econ));
        mSourceLogo.put("부산일보", ContextCompat.getDrawable(mActivity, R.drawable.busan));
        mSourceLogo.put("조선비즈", ContextCompat.getDrawable(mActivity, R.drawable.chosun_biz));
        mSourceLogo.put("조선일보", ContextCompat.getDrawable(mActivity, R.drawable.chosun_ilbo));
        mSourceLogo.put("컨슈머치", ContextCompat.getDrawable(mActivity, R.drawable.consu_much));
        mSourceLogo.put("데일지비즈온", ContextCompat.getDrawable(mActivity, R.drawable.daily_bizon));
        mSourceLogo.put("이투데이", ContextCompat.getDrawable(mActivity, R.drawable.etoday_logo));
        mSourceLogo.put("한국경제", ContextCompat.getDrawable(mActivity, R.drawable.han_kook_econ));
        mSourceLogo.put("한국일보", ContextCompat.getDrawable(mActivity, R.drawable.asia_econ));
        mSourceLogo.put("한겨레", ContextCompat.getDrawable(mActivity, R.drawable.han_kyu_re));
        mSourceLogo.put("허프포스트코리아", ContextCompat.getDrawable(mActivity, R.drawable.huffpost));
        mSourceLogo.put("허핑턴포스트", ContextCompat.getDrawable(mActivity, R.drawable.huffpost));
        mSourceLogo.put("환경미디어", ContextCompat.getDrawable(mActivity, R.drawable.hwankyung));
        mSourceLogo.put("중앙일보", ContextCompat.getDrawable(mActivity, R.drawable.joongang));
        mSourceLogo.put("경향신문", ContextCompat.getDrawable(mActivity, R.drawable.kyung_hyang));
        mSourceLogo.put("이데일리", ContextCompat.getDrawable(mActivity, R.drawable.logo_edaily));
        mSourceLogo.put("매일경제", ContextCompat.getDrawable(mActivity, R.drawable.mail_econ));
        mSourceLogo.put("동아일보", ContextCompat.getDrawable(mActivity, R.drawable.dong_ah));
        mSourceLogo.put("미디어오늘", ContextCompat.getDrawable(mActivity, R.drawable.media_today));
        mSourceLogo.put("머니투데이", ContextCompat.getDrawable(mActivity, R.drawable.money_today));
        mSourceLogo.put("위키트리", ContextCompat.getDrawable(mActivity, R.drawable.wiki_tree));
        mSourceLogo.put("위키트리 WIKITREE", ContextCompat.getDrawable(mActivity, R.drawable.wiki_tree));
        mSourceLogo.put("노컷뉴스", ContextCompat.getDrawable(mActivity, R.drawable.no_cut));
        mSourceLogo.put("세계일보", ContextCompat.getDrawable(mActivity, R.drawable.sege));
        mSourceLogo.put("YTN", ContextCompat.getDrawable(mActivity, R.drawable.ytn));
        mSourceLogo.put("뉴시스", ContextCompat.getDrawable(mActivity, R.drawable.new_sis));
        mSourceLogo.put("오마이뉴스", ContextCompat.getDrawable(mActivity, R.drawable.oh_my));
        mSourceLogo.put("KBS뉴스", ContextCompat.getDrawable(mActivity, R.drawable.kbs_news));
        mSourceLogo.put("광주in", ContextCompat.getDrawable(mActivity, R.drawable.kwangju_in));
        mSourceLogo.put("뉴스플러스", ContextCompat.getDrawable(mActivity, R.drawable.news_plus));
        mSourceLogo.put("프레시안", ContextCompat.getDrawable(mActivity, R.drawable.pressian));
        mSourceLogo.put("월간조선", ContextCompat.getDrawable(mActivity, R.drawable.chosun_month));
        mSourceLogo.put("한국농어민신문", ContextCompat.getDrawable(mActivity, R.drawable.hankook_nong_min));
        mSourceLogo.put("뉴스1", ContextCompat.getDrawable(mActivity, R.drawable.news1));
        mSourceLogo.put("SBS연예뉴스", ContextCompat.getDrawable(mActivity, R.drawable.sbs_entertain));
        mSourceLogo.put("더팩트 뉴스", ContextCompat.getDrawable(mActivity, R.drawable.the_fact));
        mSourceLogo.put("헤럴드경제", ContextCompat.getDrawable(mActivity, R.drawable.heraldecon));
        mSourceLogo.put("MBC News", ContextCompat.getDrawable(mActivity, R.drawable.mbc_news));
        mSourceLogo.put("MBC뉴스", ContextCompat.getDrawable(mActivity, R.drawable.mbc_news));
        mSourceLogo.put("자유아시아방송(RFA)", ContextCompat.getDrawable(mActivity, R.drawable.rfa));
        mSourceLogo.put("뉴비씨", ContextCompat.getDrawable(mActivity, R.drawable.newbc));
        mSourceLogo.put("오토데일리", ContextCompat.getDrawable(mActivity, R.drawable.auto_daily));
        mSourceLogo.put("연합뉴스TV", ContextCompat.getDrawable(mActivity, R.drawable.yeon_hap));
        mSourceLogo.put("매일노동뉴스", ContextCompat.getDrawable(mActivity, R.drawable.mail_daily));
        mSourceLogo.put("포커스데일리", ContextCompat.getDrawable(mActivity, R.drawable.focus_daily));
        mSourceLogo.put("JTBC", ContextCompat.getDrawable(mActivity, R.drawable.jtbc_logo));
        mSourceLogo.put("서울경제", ContextCompat.getDrawable(mActivity, R.drawable.seoul_econ));
        mSourceLogo.put("국민일보", ContextCompat.getDrawable(mActivity, R.drawable.kookmin));
        mSourceLogo.put("법률신문", ContextCompat.getDrawable(mActivity, R.drawable.law_paper));
        mSourceLogo.put("미주 한국일보", ContextCompat.getDrawable(mActivity, R.drawable.hankook_usa));
        mSourceLogo.put("매일신문", ContextCompat.getDrawable(mActivity, R.drawable.mail_paper));
        mSourceLogo.put("서울신문", ContextCompat.getDrawable(mActivity, R.drawable.seoul_paper));
        mSourceLogo.put("베리타스", ContextCompat.getDrawable(mActivity, R.drawable.veritas));
        mSourceLogo.put("파이낸스 투데이", ContextCompat.getDrawable(mActivity, R.drawable.finace_today));
        mSourceLogo.put("울산매일", ContextCompat.getDrawable(mActivity, R.drawable.woolsan_daily));
        mSourceLogo.put("경남일보", ContextCompat.getDrawable(mActivity, R.drawable.kyunnam_daily));
        mSourceLogo.put("아주경제", ContextCompat.getDrawable(mActivity, R.drawable.ah_joo));
        mSourceLogo.put("동양일보", ContextCompat.getDrawable(mActivity, R.drawable.dong_yang));
        mSourceLogo.put("뉴데일리", ContextCompat.getDrawable(mActivity, R.drawable.newdaily));
        mSourceLogo.put("더프리뷰", ContextCompat.getDrawable(mActivity, R.drawable.the_preview));
        mSourceLogo.put("동아사이언스", ContextCompat.getDrawable(mActivity, R.drawable.donga_science));
        mSourceLogo.put("문화일보", ContextCompat.getDrawable(mActivity, R.drawable.munhwa_logo_2014));
        mSourceLogo.put("디스패치", ContextCompat.getDrawable(mActivity, R.drawable.dispatch));
        mSourceLogo.put("전자신문", ContextCompat.getDrawable(mActivity, R.drawable.jeonja_paper));
        mSourceLogo.put("농민신문", ContextCompat.getDrawable(mActivity, R.drawable.nongmin));
        mSourceLogo.put("Science Times", ContextCompat.getDrawable(mActivity, R.drawable.scinecetimes));
        mSourceLogo.put("경북일보", ContextCompat.getDrawable(mActivity, R.drawable.kyungbook_ilbo));
        mSourceLogo.put("뉴스핌", ContextCompat.getDrawable(mActivity, R.drawable.newsfim));
        mSourceLogo.put("뉴시안", ContextCompat.getDrawable(mActivity, R.drawable.newsian));
        mSourceLogo.put("시사저널", ContextCompat.getDrawable(mActivity, R.drawable.sisa_press));
        mSourceLogo.put("뉴스에이", ContextCompat.getDrawable(mActivity, R.drawable.news_a));
        mSourceLogo.put("교수신문", ContextCompat.getDrawable(mActivity, R.drawable.professor_paper));
        mSourceLogo.put("국제신문", ContextCompat.getDrawable(mActivity, R.drawable.kook_je));
        mSourceLogo.put("파이낸셜뉴스", ContextCompat.getDrawable(mActivity, R.drawable.finantial));
        mSourceLogo.put("경기일보", ContextCompat.getDrawable(mActivity, R.drawable.kyeongki));
        mSourceLogo.put("대전일보", ContextCompat.getDrawable(mActivity, R.drawable.daejonilbo_logo));
        mSourceLogo.put("스타뉴스", ContextCompat.getDrawable(mActivity, R.drawable.star_news));
        mSourceLogo.put("경남신문", ContextCompat.getDrawable(mActivity, R.drawable.kyeng_nam));
        mSourceLogo.put("서울일보", ContextCompat.getDrawable(mActivity, R.drawable.seoul_ilbo));
        mSourceLogo.put("로이슈", ContextCompat.getDrawable(mActivity, R.drawable.law_issue));
        mSourceLogo.put("경인방송", ContextCompat.getDrawable(mActivity, R.drawable.kyeong_in));
        mSourceLogo.put("ZD넷 코리아", ContextCompat.getDrawable(mActivity, R.drawable.zd_net));
        mSourceLogo.put("스포츠동아", ContextCompat.getDrawable(mActivity, R.drawable.donga_spots));
        mSourceLogo.put("스포츠경향", ContextCompat.getDrawable(mActivity, R.drawable.kyeong_hyang_sports));
        mSourceLogo.put("KNN", ContextCompat.getDrawable(mActivity, R.drawable.knn));
        mSourceLogo.put("디지털타임스", ContextCompat.getDrawable(mActivity, R.drawable.digital_times));
        mSourceLogo.put("전민일보", ContextCompat.getDrawable(mActivity, R.drawable.jeon_min));
        mSourceLogo.put("뉴스큐브", ContextCompat.getDrawable(mActivity, R.drawable.news_cube));
        mSourceLogo.put("텐아시아", ContextCompat.getDrawable(mActivity, R.drawable.ten_asia));
        mSourceLogo.put("전라일보", ContextCompat.getDrawable(mActivity, R.drawable.jeon_la_ilbo));
        mSourceLogo.put("보안뉴스", ContextCompat.getDrawable(mActivity, R.drawable.boan_news));
        mSourceLogo.put("ITWorld Korea", ContextCompat.getDrawable(mActivity, R.drawable.it_world_korea));
        mSourceLogo.put("돌직구뉴스", ContextCompat.getDrawable(mActivity, R.drawable.straight_news));
        mSourceLogo.put("뉴데일리경제", ContextCompat.getDrawable(mActivity, R.drawable.new_daily_econ));
        mSourceLogo.put("스포츠조선", ContextCompat.getDrawable(mActivity, R.drawable.chosun_sports));
        mSourceLogo.put("IT조선", ContextCompat.getDrawable(mActivity, R.drawable.chosun_it));
        mSourceLogo.put("한국어 방송(VOA)", ContextCompat.getDrawable(mActivity, R.drawable.voa));
        mSourceLogo.put("한국어 방송 (VOA)", ContextCompat.getDrawable(mActivity, R.drawable.voa));
        mSourceLogo.put("VOA Korea", ContextCompat.getDrawable(mActivity, R.drawable.voa));
        mSourceLogo.put("HYPEBEAST", ContextCompat.getDrawable(mActivity, R.drawable.hypebeast));
        mSourceLogo.put("아시아투데이", ContextCompat.getDrawable(mActivity, R.drawable.asia_today));
        mSourceLogo.put("데일리시큐", ContextCompat.getDrawable(mActivity, R.drawable.daily_seeq));
        mSourceLogo.put("채널A", ContextCompat.getDrawable(mActivity, R.drawable.channel_a_logo_transparent));
        mSourceLogo.put("더셀럽", ContextCompat.getDrawable(mActivity, R.drawable.the_celeb));
        mSourceLogo.put("Goal.com", ContextCompat.getDrawable(mActivity, R.drawable.goalcom));
        mSourceLogo.put("RNX뉴스", ContextCompat.getDrawable(mActivity, R.drawable.rnx));
        mSourceLogo.put("민중의소리", ContextCompat.getDrawable(mActivity, R.drawable.min_joong));
        mSourceLogo.put("폴리뉴스", ContextCompat.getDrawable(mActivity, R.drawable.polly_news));
        mSourceLogo.put("세계타임즈", ContextCompat.getDrawable(mActivity, R.drawable.segey_times));
        mSourceLogo.put("인벤", ContextCompat.getDrawable(mActivity, R.drawable.inven));
        mSourceLogo.put("트래블바이크뉴스", ContextCompat.getDrawable(mActivity, R.drawable.travel_bike));
        mSourceLogo.put("뉴스티앤티", ContextCompat.getDrawable(mActivity, R.drawable.new_tnt));
        mSourceLogo.put("미디어펜", ContextCompat.getDrawable(mActivity, R.drawable.media_fan));
        mSourceLogo.put("경제투데이", ContextCompat.getDrawable(mActivity, R.drawable.econ_today));
        mSourceLogo.put("중도일보", ContextCompat.getDrawable(mActivity, R.drawable.joong_do));
        mSourceLogo.put("강원신문", ContextCompat.getDrawable(mActivity, R.drawable.kangwon_paper));
        mSourceLogo.put("법률저널", ContextCompat.getDrawable(mActivity, R.drawable.law_journal));
        mSourceLogo.put("신아일보", ContextCompat.getDrawable(mActivity, R.drawable.shin_ah));
        mSourceLogo.put("베프리포트", ContextCompat.getDrawable(mActivity, R.drawable.beff));
        mSourceLogo.put("톱스타뉴스", ContextCompat.getDrawable(mActivity, R.drawable.topstar_news));
        mSourceLogo.put("베리타스알파", ContextCompat.getDrawable(mActivity, R.drawable.veritas_alpha));
        mSourceLogo.put("조세일보", ContextCompat.getDrawable(mActivity, R.drawable.jose_ilbo));
        mSourceLogo.put("식품저널", ContextCompat.getDrawable(mActivity, R.drawable.food_news));
        mSourceLogo.put("에너지경제신문", ContextCompat.getDrawable(mActivity, R.drawable.energy_econ));
        mSourceLogo.put("Bitter Winter (한국어)", ContextCompat.getDrawable(mActivity, R.drawable.bitter_winter));
        mSourceLogo.put("스페셜경제", ContextCompat.getDrawable(mActivity, R.drawable.special_econ));
        mSourceLogo.put("한라일보", ContextCompat.getDrawable(mActivity, R.drawable.hanla_ilbo));
        mSourceLogo.put("옥천신문", ContextCompat.getDrawable(mActivity, R.drawable.ohk_cheon));
        mSourceLogo.put("KBS WORLD Radio News", ContextCompat.getDrawable(mActivity, R.drawable.kbs_world_radio_news));
        mSourceLogo.put("키즈맘", ContextCompat.getDrawable(mActivity, R.drawable.kizmom));
        mSourceLogo.put("Korea Daily", ContextCompat.getDrawable(mActivity, R.drawable.college_inside));
        mSourceLogo.put("치과신문", ContextCompat.getDrawable(mActivity, R.drawable.dental_paper));
        mSourceLogo.put("엠스플뉴스", ContextCompat.getDrawable(mActivity, R.drawable.emspl_news));
        mSourceLogo.put("뉴스인사이드", ContextCompat.getDrawable(mActivity, R.drawable.news_inside));
        mSourceLogo.put("미디어SR", ContextCompat.getDrawable(mActivity, R.drawable.media_sr));
        mSourceLogo.put("비주얼다이브", ContextCompat.getDrawable(mActivity, R.drawable.visual_dive));
        mSourceLogo.put("리얼뉴스", ContextCompat.getDrawable(mActivity, R.drawable.real_news));
        mSourceLogo.put("tiptipnews", ContextCompat.getDrawable(mActivity, R.drawable.tiptip_news));
        mSourceLogo.put("이코노미톡뉴스", ContextCompat.getDrawable(mActivity, R.drawable.econ_talk_news));
        mSourceLogo.put("뉴스티앤티", ContextCompat.getDrawable(mActivity, R.drawable.news_tnt));
        mSourceLogo.put("IT DAILY", ContextCompat.getDrawable(mActivity, R.drawable.it_daily));
        mSourceLogo.put("디트뉴스24", ContextCompat.getDrawable(mActivity, R.drawable.dee_te_news));
        mSourceLogo.put("로봇신문사", ContextCompat.getDrawable(mActivity, R.drawable.robot_paper));
        mSourceLogo.put("KINEWS(Korea Industry News)", ContextCompat.getDrawable(mActivity, R.drawable.robot_paper));
        mSourceLogo.put("독서신문", ContextCompat.getDrawable(mActivity, R.drawable.readers_paper));
        mSourceLogo.put("미래 한국 신문", ContextCompat.getDrawable(mActivity, R.drawable.future_korea));
        mSourceLogo.put("싱글리스트", ContextCompat.getDrawable(mActivity, R.drawable.single_list));
        mSourceLogo.put("헤럴드경제 미주판", ContextCompat.getDrawable(mActivity, R.drawable.heraldk_new_logo_lnn));
        mSourceLogo.put("일요시사", ContextCompat.getDrawable(mActivity, R.drawable.ilyo_sisa));
        mSourceLogo.put("클래시안 (Classian)", ContextCompat.getDrawable(mActivity, R.drawable.classian));
        mSourceLogo.put("백제뉴스", ContextCompat.getDrawable(mActivity, R.drawable.baek_jae));
        mSourceLogo.put("신문고", ContextCompat.getDrawable(mActivity, R.drawable.sinmoon_go));
        mSourceLogo.put("강릉뉴스", ContextCompat.getDrawable(mActivity, R.drawable.kang_rung));
        mSourceLogo.put("한국연예스포츠신문", ContextCompat.getDrawable(mActivity, R.drawable.korea_en_news));
        mSourceLogo.put("충청투데이", ContextCompat.getDrawable(mActivity, R.drawable.choong_cheong_today));
        mSourceLogo.put("미디어팜", ContextCompat.getDrawable(mActivity, R.drawable.media_farm));
        mSourceLogo.put("세종의소리", ContextCompat.getDrawable(mActivity, R.drawable.sejong_sound));
        mSourceLogo.put("제주의소리", ContextCompat.getDrawable(mActivity, R.drawable.jejoo_sori));
        mSourceLogo.put("케이벤치", ContextCompat.getDrawable(mActivity, R.drawable.k_bench));
        mSourceLogo.put("헬스조선", ContextCompat.getDrawable(mActivity, R.drawable.chosun_health));
        mSourceLogo.put("한겨례21", ContextCompat.getDrawable(mActivity, R.drawable.han21));
        mSourceLogo.put("연합뉴스", ContextCompat.getDrawable(mActivity, R.drawable.yeonhap));
        mSourceLogo.put("시사IN", ContextCompat.getDrawable(mActivity, R.drawable.sisain));
        mSourceLogo.put("더기어", ContextCompat.getDrawable(mActivity, R.drawable.thegear));
        mSourceLogo.put("뉴스엔", ContextCompat.getDrawable(mActivity, R.drawable.newsn));
        mSourceLogo.put("영남일보", ContextCompat.getDrawable(mActivity, R.drawable.yeongnamilbo));
        mSourceLogo.put("금강일보", ContextCompat.getDrawable(mActivity, R.drawable.kumkangilbo));
        mSourceLogo.put("참여일보", ContextCompat.getDrawable(mActivity, R.drawable.chamyeo));

    }

    public ArrayList getNewsInfo(JSONArray jsonArray, String newsType )
    {

        ArrayList<News> newsArrayList = new ArrayList<News>();

        for(int i=0; i < jsonArray.length(); i++)
        {
            News news = new News();

            String id = "";
            String title = "";
            String link = "";
            String imgSrc = "";
            String source = "";
            String sourceUrl = "";
            String date = "";
            String category = "";
            String original = "";
            String simLelvel = "";

            JSONObject jsonObject  = null;
            try
            {
                jsonObject = jsonArray.getJSONObject(i);

                id = jsonObject.getString("id");
                title = getTitle(jsonObject.getString("title"));
                //title = jsonObject.getString("title");

                if(newsType.equals(News.KEY_RECOMMEND_NEWS))
                {
                    original = jsonObject.getString("original");
                }

                link = jsonObject.getString("link");
                //imgSrc = jsonObject.getString("img src");
                imgSrc = getImgSrc(jsonObject);
                source = getSource(jsonObject.getString("source"));
                sourceUrl = getSourceUrl(jsonObject.getString("source"));
                date = getDate(jsonObject.getString("published"));
                category = jsonObject.getString(News.KEY_CATEGORY);
                simLelvel = jsonObject.getString("sim_level");

            }
            catch (JSONException e)
            {
                e.printStackTrace();
                continue;
            }
            finally
            {
                news.setNewsType(newsType);
                news.setId(id);
                news.setTitle(title);
                news.setUrl(link);
                news.setImgUrl(imgSrc);
                news.setDate(date);
                news.setSource(source);
                news.setSourceUrl(sourceUrl);
                news.setCategory(category);
                news.setSourceLogo(getSourceLogo(source));
                news.setSimilarityLevel(simLelvel);
                news.setOriginal(original);

                newsArrayList.add(news);
            }

        }

        return newsArrayList;
    }

    private Drawable getSourceLogo(String source)
    {
        Drawable logo =  mSourceLogo.get(source);

        return logo;
    }

    private String getDate(String publichsed_parsed)
    {
        String published = publichsed_parsed;
        String totalDate = "";

        try
        {
            String[] dateArray = published.split(",");
            String day = dateArray[0];
            String restDate = dateArray[1];

            String[] restArray =  restDate.split(" ");
            String date = restArray[1];
            String month = restArray[2];
            String year = restArray[3];

            String _month = parseMonth(month);

            totalDate = year + "년" + _month + "월" + date + "일"; //+ hour + "시" + min + "분";

        }
        catch (ArrayIndexOutOfBoundsException e)
        {
            Log.e(TAG, String.valueOf(e));

        }

        /*
        Pattern p = Pattern.compile("\\[(.*?)\\]");
        Matcher m = p.matcher(publichsed_parsed);

        if(m.find())
        {
            date = m.group(1);
        }

        String[] dateArr = date.split(",");
        String totalDate = "";

        try
        {
            String year = dateArr[0];
            String month = dateArr[1];
            String day = dateArr[2];
            String hour = dateArr[3];
            String min = dateArr[4];

            totalDate = year + "년" + month + "월" + day + "일"; //+ hour + "시" + min + "분";

        }
        catch (IndexOutOfBoundsException e)
        {
            e.getStackTrace();
        }

        return totalDate; */

        return totalDate;

    }

    private String parseMonth(String month)
    {
        String result = "";
        String[] months = {"Jan","Feb","Mar","Apr","May","Jun","Jul","Aug","Sep","Oct",
        "Nov","Dec"};

        for(int i=0; i < months.length; i++)
        {
            if(months[i].equals(month))
            {
                int intMonth = i+1;
                result = String.valueOf(intMonth);
            }

        }

        return result;
    }

    private String getSource(String source) throws JSONException
    {
        JSONObject jsonObject = new JSONObject(source);

        String src = jsonObject.getString("title");

        return src;

    }

    private String getSourceUrl(String source) throws JSONException
    {
        JSONObject jsonObject = new JSONObject(source);

        String url = jsonObject.getString("href");

        return url;
    }

    private String getTitle(String title)
    {
        String[] titleOnly = title.split("-");

        return titleOnly[0];

    }

    private String getImgSrc(JSONObject jsonObject)
    {
        String img_src = "";

        try
        {

            String json = jsonObject.getString("media_content");

            JSONArray jsonArray = new JSONArray(json);

            for(int i=0; i < jsonArray.length(); i++)
            {
                JSONObject jobject = jsonArray.optJSONObject(i);
                img_src = jobject.getString("url");
            }


        }
        catch (JSONException e)
        {
            e.printStackTrace();
        }

        if( !img_src.isEmpty())
            return img_src;


        try
        {
            img_src = jsonObject.getString("img_src");

        }
        catch (JSONException e)
        {
            e.printStackTrace();
        }

        if( !img_src.isEmpty())
            return img_src;

        try
        {

            String  html = "";
            html = jsonObject.getString("summary");

            Document doc = Jsoup.parse(html);
            Element link = doc.selectFirst("img[src]");
            img_src = link.attr("src");
        }
        catch (NullPointerException e)
        {
            return img_src;
        }
        catch (JSONException e)
        {
            e.printStackTrace();
        }

        return img_src;

    }

    public interface OnSortListener
    {
        void onSort(String sortType);
    }




}
