package com.spring.dao;

import com.alibaba.fastjson.JSONObject;

import java.util.List;

public interface ReportDao {

	/**
	 * 景点数据分析
	 */
	List<JSONObject> selectAttractionsReport();

	/**
	 * 路线数据分析
	 */
	List<JSONObject> selectRouteReport();

}
