package com.spring.controller;

import com.alibaba.fastjson.JSONObject;
import com.spring.service.ReportService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/report/")
public class ReportController {

    @Autowired
    private ReportService reportService;

    /**
     * 景点
     */
    @RequestMapping("state/data")
    public List<JSONObject> getStateReport () {
        return reportService.getAttractionsReport();
    }

    /**
     * 路线
     */
    @RequestMapping("number/data")
    public JSONObject getNumberReport () {
        List<JSONObject> reports = reportService.getRouteReport();
        List<String> nameList = new ArrayList<>();
        List<String> valueList = new ArrayList<>();
        for (JSONObject result : reports) {
            nameList.add(result.getString("name"));
            valueList.add(result.getString("value"));
        }
        JSONObject result = new JSONObject();
        result.put("name", nameList);
        result.put("value", valueList);
        return result;
    }
}
