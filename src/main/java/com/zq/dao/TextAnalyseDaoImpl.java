package com.zq.dao;

import com.mongodb.Block;
import com.mongodb.MongoClient;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.zq.hbase.HBaseOperation;
import com.zq.mongo.Mongo;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import org.apache.hadoop.hbase.Cell;
import org.apache.hadoop.hbase.CellUtil;
import org.apache.hadoop.hbase.client.Result;
import org.apache.hadoop.hbase.util.Bytes;
import org.bson.Document;
import org.springframework.stereotype.Repository;

import java.util.*;

import static com.mongodb.client.model.Updates.set;

/**
 * Created by zhangqiang on 2017/3/13.
 */
@Repository("TextAnalyseDao")
public class TextAnalyseDaoImpl implements TextAnalyseDao {
    @Override
    public JSONArray getTextAnalyseVectorInfo() {
        return HBaseOperation.scan("home_visit_reality_prediction", null, null);
    }

    @Override
    public JSONArray getDetailVisitInformation(String userId) {
        MongoCollection<Document> mc = Mongo.getCollection("HomeVisitInformation");
        FindIterable<Document> iterable = mc.find(new Document("userId", userId));
        final List list = new ArrayList<String>();
        iterable.forEach(new Block<Document>() {
            @Override
            public void apply(Document document) {
                list.add(document.toJson());
            }
        });
        Mongo.close();
        return JSONArray.fromObject(list);
    }

    @Override
    public JSONObject getKeywordStatistics(String category) {
        MongoCollection<Document> mc = Mongo.getCollection("WordFrequencyStatistics");
        FindIterable<Document> iterable = mc.find(new Document("category", category));
        final List results = new ArrayList<String>();
        iterable.forEach(new Block<Document>() {
            @Override
            public void apply(Document document) {
                results.add(document.toJson());
            }
        });
        Mongo.close();
        return JSONObject.fromObject(results.get(0));
    }

    @Override
    public void renameCluster(int clusterId, String name) {
        MongoCollection<Document> mc = Mongo.getCollection("ClusterResult");
        mc.updateOne(new Document("clusterId", clusterId), set("name", name));
        Mongo.close();
    }

    @Override
    public JSONObject getClusterPredictResult(String userId) {
        MongoCollection<Document> mc = Mongo.getCollection("ClusterPredict");
        FindIterable<Document> iterable = mc.find(new Document("userId", userId));
        final List results = new ArrayList<String>();
        iterable.forEach(new Block<Document>() {
            @Override
            public void apply(Document document) {
                results.add(document.toJson());
            }
        });
        Mongo.close();
        return JSONObject.fromObject(results.get(0));
    }

    @Override
    public String getClusterNameById(int clusterId) {
        MongoCollection<Document> mc = Mongo.getCollection("ClusterResult");
        FindIterable<Document> iterable = mc.find(new Document("clusterId", clusterId));
        final List results = new ArrayList<String>();
        iterable.forEach(new Block<Document>() {
            @Override
            public void apply(Document document) {
                String name = document.getString("name");
                if ("".equals(name) || name == null) name = clusterId + "";
                results.add(name);
            }
        });
        Mongo.close();
        return results.get(0).toString();
    }

    @Override
    public JSONArray getSumOfEveryCluster() {
        MongoCollection<Document> mc = Mongo.getCollection("sumOfOneCluster");
        FindIterable<Document> iterable = mc.find();
        final List results = new ArrayList<String>();
        iterable.forEach(new Block<Document>() {
            @Override
            public void apply(Document document) {
                results.add(document.toJson());
            }
        });
        Mongo.close();
        return JSONArray.fromObject(results);
    }

    /**
     * 获取分群信息，群簇中心点等。
     *
     * @return
     */
    @Override
    public JSONArray getClusterResult() {
        MongoCollection<Document> mc = Mongo.getCollection("ClusterResult");
        FindIterable<Document> iterable = mc.find();
        final List results = new ArrayList<String>();
        iterable.forEach(new Block<Document>() {
            @Override
            public void apply(Document document) {
                results.add(document.toJson());
            }
        });
        Mongo.close();
        return JSONArray.fromObject(results);
    }

    @Override
    public JSONArray getCategories() {
        MongoCollection<Document> mc = Mongo.getCollection("WordFrequencyStatistics");
        FindIterable<Document> iterable = mc.find();
        final List results = new ArrayList<String>();
        iterable.forEach(new Block<Document>() {
            @Override
            public void apply(Document document) {
                results.add(document.toJson());
            }
        });
        Mongo.close();
        return JSONArray.fromObject(results);
    }

    /**
     * 根据教师的userId获取其差异性向量，和所属群clusterId
     *
     * @param userId
     * @return
     */
    @Override
    public JSONObject getOneVisitVectorById(String userId) {

        JSONObject json = new JSONObject();
        Result result = HBaseOperation.getData("home_visit_reality_prediction", userId, null, null);
        if (result != null) {
            StringBuffer points = new StringBuffer();
            StringBuffer columnNames = new StringBuffer();
            for (Cell cell : result.rawCells()) {
                byte[] family = CellUtil.cloneFamily(cell);
                byte[] qualifier = CellUtil.cloneQualifier(cell);
                if (!"predictIndex".equals(Bytes.toString(qualifier))) {
                    points.append(Bytes.toString(result.getValue(family, qualifier))).append(",");
                    columnNames.append(Bytes.toString(qualifier)).append(",");
                }
            }
            String pointsStr = points.substring(0, points.length() - 1);
            String columnNamesStr = columnNames.substring(0, points.length() - 1);
            String clusterId = Bytes.toString(result.getValue(Bytes.toBytes("result"), Bytes.toBytes("predictIndex")));
            json.put("points", pointsStr);
            json.put("columnNames", columnNamesStr);
            json.put("clusterId", clusterId);
        }
        return json;
    }

}
