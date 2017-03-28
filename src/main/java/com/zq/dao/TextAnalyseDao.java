package com.zq.dao;


import com.mongodb.MongoClient;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

/**
 * Created by zhangqiang on 2017/3/13.
 */
public interface TextAnalyseDao {
    JSONArray getTextAnalyseVectorInfo();
    JSONArray getDetailVisitInformation(String userId);
    JSONObject getKeywordStatistics(String category);
    void renameCluster(int clusterId , String name);
    JSONObject getClusterPredictResult(String userId);
    String getClusterNameById(int clusterId);
    JSONArray getSumOfEveryCluster();

    /**
     * 获取分群信息，群簇中心点等。
     * @return
     */
    JSONArray getClusterResult();

    JSONArray getCategories();

    /**
     * 根据教师的userId获取其差异性向量，和所属群clusterId
     *
     * @param userId
     * @return
     */
    JSONObject getOneVisitVectorById(String userId);
}
