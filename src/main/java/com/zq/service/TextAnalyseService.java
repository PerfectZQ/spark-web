package com.zq.service;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

/**
 * Created by zhangqiang on 2017/3/13.
 */

public interface TextAnalyseService {
    JSONArray getTextAnalyseResult();

    JSONArray getDetailVisitInformation(String userId);

    JSONObject getKeywordStatistics(String category);


    /**
     * 获取每个分群的统计总数
     *
     * @return
     */
    JSONArray getSumOfEveryCluster();

    /**
     * 获取分群信息，包含群粗中心点，群名，生成蛛网图
     *
     * @return
     */
    JSONArray getClusterResult();

    /**
     * 为当前分类命名/重新命名
     *
     * @param clusterId 群类ID
     * @param name      类群名称
     */
    void renameClusterId(int clusterId, String name);

    /**
     * 获取词云种类
     *
     * @return
     */
    JSONArray getCategories();

    /**
     * 根据教师的userId获取其差异性向量，和所属群clusterId
     *
     * @param userId
     * @return
     */
    JSONObject getOneVisitVectorById(String userId);
}
