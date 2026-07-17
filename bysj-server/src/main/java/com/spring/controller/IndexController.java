package com.spring.controller;


import com.spring.util.CacheUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import dao.CommDAO;

import java.util.*;
import net.jntoo.db.Query;

/**
 * 首页控制器
 * 使用 Redis 缓存热门数据，10 分钟内不重复查询数据库
 */
@Controller
public class IndexController extends BaseController{

    @Autowired
    private CacheUtil cacheUtil;

    // 首页
    @RequestMapping(value = {"/" , "index"})
    public String Index()
    {
        // 优先查 Redis 缓存，未命中则查数据库并回写缓存
        // 如果 Redis 不可用，CacheUtil 会自动降级返回 null → 走 DB 查询
        // ========== 轮播图 ==========
        List bhtList = cacheUtil.getHomeCacheList("banners");
        if (bhtList == null) {
            bhtList = Query.make("lunbotu").order("id desc").limit(5).select();
            cacheUtil.setHomeCache("banners", bhtList);
        }
        assign("bhtList" , bhtList);

        // ========== 热门景点 ==========
        List jingdianxinxilist1 = cacheUtil.getHomeCacheList("hotspots");
        if (jingdianxinxilist1 == null) {
            jingdianxinxilist1 = Query.make("jingdianxinxi").limit(4).order("liulanliang desc").select();
            cacheUtil.setHomeCache("hotspots", jingdianxinxilist1);
        }
        assign("jingdianxinxilist1" , jingdianxinxilist1);

        // ========== 推荐美食 ==========
        List difangmeishilist2 = cacheUtil.getHomeCacheList("hotfoods");
        if (difangmeishilist2 == null) {
            difangmeishilist2 = Query.make("difangmeishi").limit(4).order("id desc").select();
            cacheUtil.setHomeCache("hotfoods", difangmeishilist2);
        }
        assign("difangmeishilist2" , difangmeishilist2);

        // ========== 推荐线路 ==========
        List lvyouxianlulist3 = cacheUtil.getHomeCacheList("hotroutes");
        if (lvyouxianlulist3 == null) {
            lvyouxianlulist3 = Query.make("lvyouxianlu").limit(4).order("id desc").select();
            cacheUtil.setHomeCache("hotroutes", lvyouxianlulist3);
        }
        assign("lvyouxianlulist3" , lvyouxianlulist3);

        // ========== 最新新闻 ==========
        List xinwenxinxilist4 = cacheUtil.getHomeCacheList("latestnews");
        if (xinwenxinxilist4 == null) {
            xinwenxinxilist4 = Query.make("xinwenxinxi").limit(4).order("id desc").select();
            cacheUtil.setHomeCache("latestnews", xinwenxinxilist4);
        }
        assign("xinwenxinxilist4" , xinwenxinxilist4);

        if(isAjax())
        {
            return json();
        }
        return "index";
    }

    /**
     * 手动清除所有首页缓存（浏览器直接访问 /clearcache.do 即可）
     */
    @RequestMapping("/clearcache")
    public void clearCache()
    {
        cacheUtil.clearAllHomeCache();
        if (isAjax()) {
            jsonResult("首页缓存已清除");
            return;
        }
        try {
            response.sendRedirect("./");
        } catch (Exception e) {}
    }
}
