package com.zq.service;

import com.mongodb.MongoClient;
import com.zq.dao.TextAnalyseDao;
import com.zq.mongo.Mongo;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

/**
 * Created by zhangqiang on 2@017/3/13.
 */
@Service("TextAnalyseService")
public class TextAnalyseServiceImpl implements TextAnalyseService {
    @Autowired
    @Qualifier("TextAnalyseDao")
    TextAnalyseDao textAnalyseDao;

    @Override
    public JSONArray getTextAnalyseResult() {
        return textAnalyseDao.getTextAnalyseVectorInfo();
    }

    @Override
    public JSONArray getDetailVisitInformation(String userId) {
        return textAnalyseDao.getDetailVisitInformation(userId);
    }

    @Override
    public JSONObject getKeywordStatistics(String category) {
        return textAnalyseDao.getKeywordStatistics(category);
    }


    /**
     * 获取每个分群的统计总数
     *
     * @return
     */
    @Override
    public JSONArray getSumOfEveryCluster() {
        JSONArray arr = textAnalyseDao.getSumOfEveryCluster();
        JSONArray newArr = new JSONArray();
        for (Object o : arr) {
            JSONObject json = JSONObject.fromObject(o);
            String name = textAnalyseDao.getClusterNameById(json.getInt("clusterId"));
            json.put("name", name);
            newArr.add(json);
        }
        return newArr;
    }

    /**
     * 获取分群信息，包含群粗中心点，群名，生成蛛网图
     *
     * @return
     */
    @Override
    public JSONArray getClusterResult() {
        return textAnalyseDao.getClusterResult();
    }

    @Override
    public void renameClusterId(int clusterId, String name) {
        textAnalyseDao.renameCluster(clusterId, name);
    }

    /**
     * 获取词云种类
     *
     * @return
     */
    @Override
    public JSONArray getCategories() {
        JSONArray arr = textAnalyseDao.getCategories();
        JSONArray newArr = new JSONArray();
        arr.forEach(o -> {
            JSONObject json = JSONObject.fromObject(o);
            json.remove("result");
            newArr.add(json);
        });
        return newArr;
    }

    /**
     * 根据教师的userId获取其差异性向量，和所属群clusterId
     *
     * @param userId
     * @return
     */
    @Override
    public JSONObject getOneVisitVectorById(String userId) {
        return textAnalyseDao.getOneVisitVectorById(userId);
    }

}
