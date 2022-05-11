package com.redscarf.process.crawl.component;

import com.alibaba.fastjson.JSONObject;
import com.redscarf.process.crawl.properties.ProcessOnUserInfoProperties;
import com.redscarf.process.crawl.util.OkHttpUtil;
import lombok.extern.slf4j.Slf4j;
import okhttp3.Headers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

/**
 * @author: leejohn
 * @date: 2022/2/16 11:34
 * @since:
 */
@Slf4j
@Service
public class ProcessOnService {


    /**
     * 获取脑力的json数据
     *
     * @param id : 脑图uid
     */
    public JSONObject getMindMapJson(String id) {
        String url = DIAGRAMING_DEF + "?id=" + id;
        String jsonStr = OkHttpUtil.get(url);
        //log.debug("脑图json : \n{}", jsonStr);
        JSONObject jsonObj = JSONObject.parseObject(jsonStr);
        return jsonObj.getJSONObject("def");
    }

    /**
     * 动作
     */
    public void actionMindMap(String id, String msgJsonStr,
                              Headers headers) {
        String actionUrl = ACTION;
        Map<String, String> postForm = new HashMap<>();
        postForm.put("ignore", "msgStr");
        postForm.put("chartId", id);
        postForm.put("uk", properties.getUk());
        postForm.put("fullName", properties.getFullName());
        postForm.put("msgStr", msgJsonStr);
        String result = OkHttpUtil.postForm(actionUrl, postForm, headers);
        log.info("保存结果 : \n{}", result);
    }

    /**
     * 创建脑图文件
     */
    public void createFile(Headers headers, String fileType) {
        String url = CREATE_FILE + "?category=" + fileType;
        String jsonStr = OkHttpUtil.get(url, headers);
        log.info("创建文件结果 : \n{}", jsonStr);
    }

    @Autowired
    private ProcessOnUserInfoProperties properties;

    private final static String ACTION_CREATE = "create";

    public final static String HOST = "https://processon.com";
    public final static String DIAGRAMING_DEF = HOST + "/diagraming/getdef";
    public final static String ACTION = HOST + "/mindmap/msg";
    public final static String CREATE_FILE = HOST + "/mindmap/new";

    /**
     * 思维导图
     */
    public final static String MIND_TYPE = "mind_free";
    /**
     * 流程图
     */
    public final static String FLOW_TYPE = "flow";

    public static String createActionStr = "[{\"action\":\"create\",\"content\":{\"content\":[],\"index\":{\"78250f96d687\":0},\"parts\":{\"78250f96d687\":\"right\"},\"updates\":{},\"original\":{},\"updateTps\":{}}}]";
}
