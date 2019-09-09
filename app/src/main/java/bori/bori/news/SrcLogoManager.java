package bori.bori.news;

import android.app.Activity;
import android.graphics.drawable.Drawable;
import androidx.core.content.ContextCompat;
import bori.bori.R;

import java.util.HashMap;
import java.util.Map;

public class SrcLogoManager
{
    static private final SrcLogoManager mInstnace = new SrcLogoManager();
    private Map<String, Drawable> mSourceMap;
    private Activity mActivity;

    public SrcLogoManager()
    {
        mSourceMap = new HashMap<>();
    }

    public void init(Activity activity)
    {
        mActivity = activity;
        setSourceLogo(activity);
    }

    public static SrcLogoManager getInstance()
    {
        return mInstnace;
    }

    public Drawable findSourceLogo(String source)
    {
        checkInit();

        Drawable logo = null;
        if(mSourceMap.containsKey(source))
        {
            logo = mSourceMap.get(source);
        }

        return logo;
    }

    private void checkInit()
    {
        if(null == mActivity)
            throw new NullPointerException("have to call init method first");
    }


    private void setSourceLogo(Activity activity)
    {

        mSourceMap.put("default", ContextCompat.getDrawable(activity, R.drawable.ic_rss_grey));

        mSourceMap.put("아시아경제", ContextCompat.getDrawable(activity, R.drawable.asia_econ));
        mSourceMap.put("부산일보", ContextCompat.getDrawable(activity, R.drawable.busan));
        mSourceMap.put("조선비즈", ContextCompat.getDrawable(activity, R.drawable.chosun_biz));
        mSourceMap.put("조선일보", ContextCompat.getDrawable(activity, R.drawable.chosun_ilbo));
        mSourceMap.put("컨슈머치", ContextCompat.getDrawable(activity, R.drawable.consu_much));
        mSourceMap.put("데일지비즈온", ContextCompat.getDrawable(activity, R.drawable.daily_bizon));
        mSourceMap.put("이투데이", ContextCompat.getDrawable(activity, R.drawable.etoday_logo));
        mSourceMap.put("한국경제", ContextCompat.getDrawable(activity, R.drawable.han_kook_econ));
        mSourceMap.put("한국일보", ContextCompat.getDrawable(activity, R.drawable.asia_econ));
        mSourceMap.put("한겨레", ContextCompat.getDrawable(activity, R.drawable.han_kyu_re));
        mSourceMap.put("허프포스트코리아", ContextCompat.getDrawable(activity, R.drawable.huffpost));
        mSourceMap.put("허핑턴포스트", ContextCompat.getDrawable(activity, R.drawable.huffpost));
        mSourceMap.put("환경미디어", ContextCompat.getDrawable(activity, R.drawable.hwankyung));
        mSourceMap.put("중앙일보", ContextCompat.getDrawable(activity, R.drawable.joongang));
        mSourceMap.put("경향신문", ContextCompat.getDrawable(activity, R.drawable.kyung_hyang));
        mSourceMap.put("이데일리", ContextCompat.getDrawable(activity, R.drawable.logo_edaily));
        mSourceMap.put("매일경제", ContextCompat.getDrawable(activity, R.drawable.mail_econ));
        mSourceMap.put("동아일보", ContextCompat.getDrawable(activity, R.drawable.dong_ah));
        mSourceMap.put("미디어오늘", ContextCompat.getDrawable(activity, R.drawable.media_today));
        mSourceMap.put("머니투데이", ContextCompat.getDrawable(activity, R.drawable.money_today));
        mSourceMap.put("위키트리", ContextCompat.getDrawable(activity, R.drawable.wiki_tree));
        mSourceMap.put("위키트리 WIKITREE", ContextCompat.getDrawable(activity, R.drawable.wiki_tree));
        mSourceMap.put("노컷뉴스", ContextCompat.getDrawable(activity, R.drawable.no_cut));
        mSourceMap.put("세계일보", ContextCompat.getDrawable(activity, R.drawable.sege));
        mSourceMap.put("YTN", ContextCompat.getDrawable(activity, R.drawable.ytn));
        mSourceMap.put("뉴시스", ContextCompat.getDrawable(activity, R.drawable.new_sis));
        mSourceMap.put("오마이뉴스", ContextCompat.getDrawable(activity, R.drawable.oh_my));
        mSourceMap.put("KBS뉴스", ContextCompat.getDrawable(activity, R.drawable.kbs_news));
        mSourceMap.put("광주in", ContextCompat.getDrawable(activity, R.drawable.kwangju_in));
        mSourceMap.put("뉴스플러스", ContextCompat.getDrawable(activity, R.drawable.news_plus));
        mSourceMap.put("프레시안", ContextCompat.getDrawable(activity, R.drawable.pressian));
        mSourceMap.put("월간조선", ContextCompat.getDrawable(activity, R.drawable.chosun_month));
        mSourceMap.put("한국농어민신문", ContextCompat.getDrawable(activity, R.drawable.hankook_nong_min));
        mSourceMap.put("뉴스1", ContextCompat.getDrawable(activity, R.drawable.news1));
        mSourceMap.put("SBS연예뉴스", ContextCompat.getDrawable(activity, R.drawable.sbs_entertain));
        mSourceMap.put("더팩트 뉴스", ContextCompat.getDrawable(activity, R.drawable.the_fact));
        mSourceMap.put("헤럴드경제", ContextCompat.getDrawable(activity, R.drawable.heraldecon));
        mSourceMap.put("MBC News", ContextCompat.getDrawable(activity, R.drawable.mbc_news));
        mSourceMap.put("MBC뉴스", ContextCompat.getDrawable(activity, R.drawable.mbc_news));
        mSourceMap.put("자유아시아방송(RFA)", ContextCompat.getDrawable(activity, R.drawable.rfa));
        mSourceMap.put("뉴비씨", ContextCompat.getDrawable(activity, R.drawable.newbc));
        mSourceMap.put("오토데일리", ContextCompat.getDrawable(activity, R.drawable.auto_daily));
        mSourceMap.put("연합뉴스TV", ContextCompat.getDrawable(activity, R.drawable.yeon_hap));
        mSourceMap.put("매일노동뉴스", ContextCompat.getDrawable(activity, R.drawable.mail_daily));
        mSourceMap.put("포커스데일리", ContextCompat.getDrawable(activity, R.drawable.focus_daily));
        mSourceMap.put("JTBC", ContextCompat.getDrawable(activity, R.drawable.jtbc_logo));
        mSourceMap.put("서울경제", ContextCompat.getDrawable(activity, R.drawable.seoul_econ));
        mSourceMap.put("국민일보", ContextCompat.getDrawable(activity, R.drawable.kookmin));
        mSourceMap.put("법률신문", ContextCompat.getDrawable(activity, R.drawable.law_paper));
        mSourceMap.put("미주 한국일보", ContextCompat.getDrawable(activity, R.drawable.hankook_usa));
        mSourceMap.put("매일신문", ContextCompat.getDrawable(activity, R.drawable.mail_paper));
        mSourceMap.put("서울신문", ContextCompat.getDrawable(activity, R.drawable.seoul_paper));
        mSourceMap.put("베리타스", ContextCompat.getDrawable(activity, R.drawable.veritas));
        mSourceMap.put("파이낸스 투데이", ContextCompat.getDrawable(activity, R.drawable.finace_today));
        mSourceMap.put("울산매일", ContextCompat.getDrawable(activity, R.drawable.woolsan_daily));
        mSourceMap.put("경남일보", ContextCompat.getDrawable(activity, R.drawable.kyunnam_daily));
        mSourceMap.put("아주경제", ContextCompat.getDrawable(activity, R.drawable.ah_joo));
        mSourceMap.put("동양일보", ContextCompat.getDrawable(activity, R.drawable.dong_yang));
        mSourceMap.put("뉴데일리", ContextCompat.getDrawable(activity, R.drawable.newdaily));
        mSourceMap.put("더프리뷰", ContextCompat.getDrawable(activity, R.drawable.the_preview));
        mSourceMap.put("동아사이언스", ContextCompat.getDrawable(activity, R.drawable.donga_science));
        mSourceMap.put("문화일보", ContextCompat.getDrawable(activity, R.drawable.munhwa_logo_2014));
        mSourceMap.put("디스패치", ContextCompat.getDrawable(activity, R.drawable.dispatch));
        mSourceMap.put("전자신문", ContextCompat.getDrawable(activity, R.drawable.jeonja_paper));
        mSourceMap.put("농민신문", ContextCompat.getDrawable(activity, R.drawable.nongmin));
        mSourceMap.put("Science Times", ContextCompat.getDrawable(activity, R.drawable.scinecetimes));
        mSourceMap.put("경북일보", ContextCompat.getDrawable(activity, R.drawable.kyungbook_ilbo));
        mSourceMap.put("뉴스핌", ContextCompat.getDrawable(activity, R.drawable.newsfim));
        mSourceMap.put("뉴시안", ContextCompat.getDrawable(activity, R.drawable.newsian));
        mSourceMap.put("시사저널", ContextCompat.getDrawable(activity, R.drawable.sisa_press));
        mSourceMap.put("뉴스에이", ContextCompat.getDrawable(activity, R.drawable.news_a));
        mSourceMap.put("교수신문", ContextCompat.getDrawable(activity, R.drawable.professor_paper));
        mSourceMap.put("국제신문", ContextCompat.getDrawable(activity, R.drawable.kook_je));
        mSourceMap.put("파이낸셜뉴스", ContextCompat.getDrawable(activity, R.drawable.finantial));
        mSourceMap.put("경기일보", ContextCompat.getDrawable(activity, R.drawable.kyeongki));
        mSourceMap.put("대전일보", ContextCompat.getDrawable(activity, R.drawable.daejonilbo_logo));
        mSourceMap.put("스타뉴스", ContextCompat.getDrawable(activity, R.drawable.star_news));
        mSourceMap.put("경남신문", ContextCompat.getDrawable(activity, R.drawable.kyeng_nam));
        mSourceMap.put("서울일보", ContextCompat.getDrawable(activity, R.drawable.seoul_ilbo));
        mSourceMap.put("로이슈", ContextCompat.getDrawable(activity, R.drawable.law_issue));
        mSourceMap.put("경인방송", ContextCompat.getDrawable(activity, R.drawable.kyeong_in));
        mSourceMap.put("ZD넷 코리아", ContextCompat.getDrawable(activity, R.drawable.zd_net));
        mSourceMap.put("스포츠동아", ContextCompat.getDrawable(activity, R.drawable.donga_spots));
        mSourceMap.put("스포츠경향", ContextCompat.getDrawable(activity, R.drawable.kyeong_hyang_sports));
        mSourceMap.put("KNN", ContextCompat.getDrawable(activity, R.drawable.knn));
        mSourceMap.put("디지털타임스", ContextCompat.getDrawable(activity, R.drawable.digital_times));
        mSourceMap.put("전민일보", ContextCompat.getDrawable(activity, R.drawable.jeon_min));
        mSourceMap.put("뉴스큐브", ContextCompat.getDrawable(activity, R.drawable.news_cube));
        mSourceMap.put("텐아시아", ContextCompat.getDrawable(activity, R.drawable.ten_asia));
        mSourceMap.put("전라일보", ContextCompat.getDrawable(activity, R.drawable.jeon_la_ilbo));
        mSourceMap.put("보안뉴스", ContextCompat.getDrawable(activity, R.drawable.boan_news));
        mSourceMap.put("ITWorld Korea", ContextCompat.getDrawable(activity, R.drawable.it_world_korea));
        mSourceMap.put("돌직구뉴스", ContextCompat.getDrawable(activity, R.drawable.straight_news));
        mSourceMap.put("뉴데일리경제", ContextCompat.getDrawable(activity, R.drawable.new_daily_econ));
        mSourceMap.put("스포츠조선", ContextCompat.getDrawable(activity, R.drawable.chosun_sports));
        mSourceMap.put("IT조선", ContextCompat.getDrawable(activity, R.drawable.chosun_it));
        mSourceMap.put("한국어 방송(VOA)", ContextCompat.getDrawable(activity, R.drawable.voa));
        mSourceMap.put("한국어 방송 (VOA)", ContextCompat.getDrawable(activity, R.drawable.voa));
        mSourceMap.put("VOA Korea", ContextCompat.getDrawable(activity, R.drawable.voa));
        mSourceMap.put("HYPEBEAST", ContextCompat.getDrawable(activity, R.drawable.hypebeast));
        mSourceMap.put("아시아투데이", ContextCompat.getDrawable(activity, R.drawable.asia_today));
        mSourceMap.put("데일리시큐", ContextCompat.getDrawable(activity, R.drawable.daily_seeq));
        mSourceMap.put("채널A", ContextCompat.getDrawable(activity, R.drawable.channel_a_logo_transparent));
        mSourceMap.put("더셀럽", ContextCompat.getDrawable(activity, R.drawable.the_celeb));
        mSourceMap.put("Goal.com", ContextCompat.getDrawable(activity, R.drawable.goalcom));
        mSourceMap.put("RNX뉴스", ContextCompat.getDrawable(activity, R.drawable.rnx));
        mSourceMap.put("민중의소리", ContextCompat.getDrawable(activity, R.drawable.min_joong));
        mSourceMap.put("폴리뉴스", ContextCompat.getDrawable(activity, R.drawable.polly_news));
        mSourceMap.put("세계타임즈", ContextCompat.getDrawable(activity, R.drawable.segey_times));
        mSourceMap.put("인벤", ContextCompat.getDrawable(activity, R.drawable.inven));
        mSourceMap.put("트래블바이크뉴스", ContextCompat.getDrawable(activity, R.drawable.travel_bike));
        mSourceMap.put("뉴스티앤티", ContextCompat.getDrawable(activity, R.drawable.new_tnt));
        mSourceMap.put("미디어펜", ContextCompat.getDrawable(activity, R.drawable.media_fan));
        mSourceMap.put("경제투데이", ContextCompat.getDrawable(activity, R.drawable.econ_today));
        mSourceMap.put("중도일보", ContextCompat.getDrawable(activity, R.drawable.joong_do));
        mSourceMap.put("강원신문", ContextCompat.getDrawable(activity, R.drawable.kangwon_paper));
        mSourceMap.put("법률저널", ContextCompat.getDrawable(activity, R.drawable.law_journal));
        mSourceMap.put("신아일보", ContextCompat.getDrawable(activity, R.drawable.shin_ah));
        mSourceMap.put("베프리포트", ContextCompat.getDrawable(activity, R.drawable.beff));
        mSourceMap.put("톱스타뉴스", ContextCompat.getDrawable(activity, R.drawable.topstar_news));
        mSourceMap.put("베리타스알파", ContextCompat.getDrawable(activity, R.drawable.veritas_alpha));
        mSourceMap.put("조세일보", ContextCompat.getDrawable(activity, R.drawable.jose_ilbo));
        mSourceMap.put("식품저널", ContextCompat.getDrawable(activity, R.drawable.food_news));
        mSourceMap.put("에너지경제신문", ContextCompat.getDrawable(activity, R.drawable.energy_econ));
        mSourceMap.put("Bitter Winter (한국어)", ContextCompat.getDrawable(activity, R.drawable.bitter_winter));
        mSourceMap.put("스페셜경제", ContextCompat.getDrawable(activity, R.drawable.special_econ));
        mSourceMap.put("한라일보", ContextCompat.getDrawable(activity, R.drawable.hanla_ilbo));
        mSourceMap.put("옥천신문", ContextCompat.getDrawable(activity, R.drawable.ohk_cheon));
        mSourceMap.put("KBS WORLD Radio News", ContextCompat.getDrawable(activity, R.drawable.kbs_world_radio_news));
        mSourceMap.put("키즈맘", ContextCompat.getDrawable(activity, R.drawable.kizmom));
        mSourceMap.put("Korea Daily", ContextCompat.getDrawable(activity, R.drawable.college_inside));
        mSourceMap.put("치과신문", ContextCompat.getDrawable(activity, R.drawable.dental_paper));
        mSourceMap.put("엠스플뉴스", ContextCompat.getDrawable(activity, R.drawable.emspl_news));
        mSourceMap.put("뉴스인사이드", ContextCompat.getDrawable(activity, R.drawable.news_inside));
        mSourceMap.put("미디어SR", ContextCompat.getDrawable(activity, R.drawable.media_sr));
        mSourceMap.put("비주얼다이브", ContextCompat.getDrawable(activity, R.drawable.visual_dive));
        mSourceMap.put("리얼뉴스", ContextCompat.getDrawable(activity, R.drawable.real_news));
        mSourceMap.put("tiptipnews", ContextCompat.getDrawable(activity, R.drawable.tiptip_news));
        mSourceMap.put("이코노미톡뉴스", ContextCompat.getDrawable(activity, R.drawable.econ_talk_news));
        mSourceMap.put("뉴스티앤티", ContextCompat.getDrawable(activity, R.drawable.news_tnt));
        mSourceMap.put("IT DAILY", ContextCompat.getDrawable(activity, R.drawable.it_daily));
        mSourceMap.put("디트뉴스24", ContextCompat.getDrawable(activity, R.drawable.dee_te_news));
        mSourceMap.put("로봇신문사", ContextCompat.getDrawable(activity, R.drawable.robot_paper));
        mSourceMap.put("KINEWS(Korea Industry News)", ContextCompat.getDrawable(activity, R.drawable.robot_paper));
        mSourceMap.put("독서신문", ContextCompat.getDrawable(activity, R.drawable.readers_paper));
        mSourceMap.put("미래 한국 신문", ContextCompat.getDrawable(activity, R.drawable.future_korea));
        mSourceMap.put("싱글리스트", ContextCompat.getDrawable(activity, R.drawable.single_list));
        mSourceMap.put("헤럴드경제 미주판", ContextCompat.getDrawable(activity, R.drawable.heraldk_new_logo_lnn));
        mSourceMap.put("일요시사", ContextCompat.getDrawable(activity, R.drawable.ilyo_sisa));
        mSourceMap.put("클래시안 (Classian)", ContextCompat.getDrawable(activity, R.drawable.classian));
        mSourceMap.put("백제뉴스", ContextCompat.getDrawable(activity, R.drawable.baek_jae));
        mSourceMap.put("신문고", ContextCompat.getDrawable(activity, R.drawable.sinmoon_go));
        mSourceMap.put("강릉뉴스", ContextCompat.getDrawable(activity, R.drawable.kang_rung));
        mSourceMap.put("한국연예스포츠신문", ContextCompat.getDrawable(activity, R.drawable.korea_en_news));
        mSourceMap.put("충청투데이", ContextCompat.getDrawable(activity, R.drawable.choong_cheong_today));
        mSourceMap.put("미디어팜", ContextCompat.getDrawable(activity, R.drawable.media_farm));
        mSourceMap.put("세종의소리", ContextCompat.getDrawable(activity, R.drawable.sejong_sound));
        mSourceMap.put("제주의소리", ContextCompat.getDrawable(activity, R.drawable.jejoo_sori));
        mSourceMap.put("케이벤치", ContextCompat.getDrawable(activity, R.drawable.k_bench));
        mSourceMap.put("헬스조선", ContextCompat.getDrawable(activity, R.drawable.chosun_health));
        mSourceMap.put("한겨례21", ContextCompat.getDrawable(activity, R.drawable.han21));
        mSourceMap.put("연합뉴스", ContextCompat.getDrawable(activity, R.drawable.yeonhap));
        mSourceMap.put("시사IN", ContextCompat.getDrawable(activity, R.drawable.sisain));
        mSourceMap.put("더기어", ContextCompat.getDrawable(activity, R.drawable.thegear));
        mSourceMap.put("뉴스엔", ContextCompat.getDrawable(activity, R.drawable.newsn));
        mSourceMap.put("영남일보", ContextCompat.getDrawable(activity, R.drawable.yeongnamilbo));
        mSourceMap.put("금강일보", ContextCompat.getDrawable(activity, R.drawable.kumkangilbo));
        mSourceMap.put("참여일보", ContextCompat.getDrawable(activity, R.drawable.chamyeo));
        mSourceMap.put("씨네21", ContextCompat.getDrawable(activity, R.drawable.cine21));


    }

}
