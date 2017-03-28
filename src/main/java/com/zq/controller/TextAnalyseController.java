package com.zq.controller;

import com.zq.service.TextAnalyseService;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;

/**
 * Created by zhangqiang on 2017/3/13.
 */
@Controller
@RequestMapping(value = "textAnalyse")
public class TextAnalyseController {
    @Autowired
    @Qualifier("TextAnalyseService") // 注入指定的bean
    private TextAnalyseService textAnalyseService;

    @RequestMapping(value = "classificationDisplay")
    public ModelAndView displayClassification(ModelAndView model) {
        model.setViewName("classificationDisplay");
        return model;
    }

    //TODO: 重构任务：将displayDetailInformation和getDetailVisitInformation合并，或者改成超链接的方式
    @RequestMapping(value = "visitInformationDetailsDisplay", method = RequestMethod.POST)
    public ModelAndView displayDetailInformation(HttpServletRequest request) {
        ModelAndView model = new ModelAndView();
        model.setViewName("visitInformationDetailsDisplay");
        model.addObject("userId", request.getParameter("userId"));
        return model;
    }

    @RequestMapping(value = "keywordStatisticDisplay")
    public ModelAndView displayKeywordStatistic(ModelAndView model) {
        model.setViewName("keywordStatisticDisplay");
        return model;
    }

    @RequestMapping(value = "textAnalyseResultDisplay")
    public ModelAndView displayTextAnalyseResult(ModelAndView model) {
        model.setViewName("textAnalyseResultDisplay");
        return model;
    }

    /**
     * 从hbase获取每个教师的分析结果-差异性向量
     *
     * @return
     */
    @RequestMapping(value = "getAnalyseResult")
    @ResponseBody
    public JSONArray getTextAnalyseResult() {
        return textAnalyseService.getTextAnalyseResult();
    }

    /**
     * 获取关键词统计结果
     *
     * @param category
     * @return
     */
    @RequestMapping(value = "getKeywordStatistics")
    @ResponseBody
    public JSONObject getKeywordStatistics(String category) {
        return textAnalyseService.getKeywordStatistics(category);
    }

    /**
     * 从mongoDB获取每个教师的每个学生的详细文字信息
     *
     * @param request
     * @return
     */
    @RequestMapping(value = "getDetailVisitInformation", method = RequestMethod.POST)
    @ResponseBody
    public JSONArray getDetailVisitInformation(@RequestBody JSONObject request) {
        return textAnalyseService.getDetailVisitInformation(request.getString("userId"));
    }

    /**
     * 获取分群的统计总数
     *
     * @return
     */
    @RequestMapping(value = "getSumOfEveryCluster", method = RequestMethod.POST)
    @ResponseBody
    public JSONArray getSumOfEveryCluster() {
        return textAnalyseService.getSumOfEveryCluster();
    }

    /**
     * 获取分群信息，包含群粗中心点，群名，生成蛛网图
     *
     * @return
     */
    @RequestMapping(value = "getClusterResult", method = RequestMethod.POST)
    @ResponseBody
    public JSONArray getClusterResult() {
        return textAnalyseService.getClusterResult();
    }

    /**
     * 给分类群重命名
     *
     * @param clusterId
     * @param name
     */
    @RequestMapping(value = "renameClusterId", method = RequestMethod.POST)
    @ResponseBody
    public void renameCluster(int clusterId, String name) {
        textAnalyseService.renameClusterId(clusterId, name);
    }

    /**
     * 获取词云种类
     *
     * @return
     */
    @RequestMapping(value = "getCategories", method = RequestMethod.POST)
    @ResponseBody
    public JSONArray getCategories() {
        return textAnalyseService.getCategories();
    }

    /**
     * 根据教师的userId获取其差异性向量，和所属群clusterId
     *
     * @param userId
     * @return
     */
    @RequestMapping(value = "getOneVisitVectorById", method = RequestMethod.POST)
    @ResponseBody
    public JSONObject getOneVisitVectorById(String userId) {
        return textAnalyseService.getOneVisitVectorById(userId);
    }
}
