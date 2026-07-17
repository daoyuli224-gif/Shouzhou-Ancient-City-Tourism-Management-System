package com.spring.service;

import com.alibaba.fastjson.JSONObject;
import com.spring.dao.ReportDao;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

@Service
public class ReportService {

    @Resource
    private ReportDao reportDao;

    /**
     * 景点分析
     */
    public List<JSONObject> getAttractionsReport () {
        return reportDao.selectAttractionsReport();
    }

    /**
     * 路线分析
     */
    public List<JSONObject> getRouteReport () {
        return reportDao.selectRouteReport();
    }

}
