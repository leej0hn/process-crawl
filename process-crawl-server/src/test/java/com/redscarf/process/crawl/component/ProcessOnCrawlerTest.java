package com.redscarf.process.crawl.component;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.redscarf.process.crawl.BaseTest;
import com.redscarf.process.crawl.properties.ProcessOnUserInfoProperties;
import lombok.extern.slf4j.Slf4j;
import okhttp3.Headers;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static com.redscarf.process.crawl.component.ProcessOnService.*;

/**
 * @author: leejohn
 * @date: 2022/2/10 11:42
 * @since:
 */
@Slf4j
public class ProcessOnCrawlerTest extends BaseTest {
    @Autowired
    private ProcessOnService processOnService;

    /**
     * 获取图的json信息
     */
    @Test
    public void testGetMindMap() {
        // 分享的url上有id
        String id = "60617095f346fb6d9ef3d42e";
        JSONObject mindMapJson = processOnService.getMindMapJson(id);
        log.info("mindMapJson : \n{}", mindMapJson);
    }

    /**
     * 获取未分享脑图的json数据
     */
    @Test
    public void testGetPrivateMindMap() {
        // 分享的url上有id
        String id = "617b81ebf346fb01b918d499";
        String tempId = "62d7c6577d9c085ba14421a4";
        String time = "1658308184543";
        JSONObject mindMapJson = processOnService.getPrivateMindMapJson(id, tempId, time);
        log.info("mindMapJson : \n{}", mindMapJson);
    }

    /**
     * 拷贝未分享指定的图到自己的文件中
     */
    @Test
    public void testPrivateAction() {
        // 获取需要拷贝的脑图json
        String targetId = "617b81ebf346fb01b918d499";
        String tempId = "62d7c72407912923e88864ad";
        String time = "1658308388901";
        JSONObject mindMapJson = processOnService.getPrivateMindMapJson(targetId, tempId, time);
        log.info("需要拷贝的Json : \n{}", mindMapJson);
        // 自己用来保存的 id
        String actionId = "620cec841efad406e72e6b04";
        JSONArray jsonArray = createAction.getJSONObject(0).getJSONObject("content").getJSONArray("content");
        JSONArray children = mindMapJson.getJSONArray("children");
        JSONArray leftChildren = mindMapJson.getJSONArray("leftChildren");
        if (children != null && children.size() > 0) {
            jsonArray.addAll(children);
        }
        if (leftChildren != null && leftChildren.size() > 0) {
            jsonArray.addAll(leftChildren);
        }
        String msgStr = createAction.toJSONString();
        // 保存到自己的脑图
        processOnService.actionMindMap(actionId, msgStr, headers);
    }

    /**
     * 拷贝指定的图到自己的文件中
     */
    @Test
    public void testAction() {
        // 获取需要拷贝的脑图json
        String targetId = "608824a6079129753c5910db";
        JSONObject mindMapJson = processOnService.getMindMapJson(targetId);
        log.info("需要拷贝的Json : \n{}", mindMapJson);
        // 自己用来保存的 id
        String actionId = "61dbc6125653bb069f535585";
        JSONArray jsonArray = createAction.getJSONObject(0).getJSONObject("content").getJSONArray("content");
        JSONArray children = mindMapJson.getJSONArray("children");
        JSONArray leftChildren = mindMapJson.getJSONArray("leftChildren");
        if (children != null && children.size() > 0) {
            jsonArray.addAll(children);
        }
        if (leftChildren != null && leftChildren.size() > 0) {
            jsonArray.addAll(leftChildren);
        }
        String msgStr = createAction.toJSONString();
        // 保存到自己的脑图
        processOnService.actionMindMap(actionId, msgStr, headers);
    }

    /**
     * 创建思维导图文件
     */
    @Test
    public void testCreateMindFile() throws InterruptedException {
        for (int i = 0; i < 500; i++) {
            processOnService.createFile(headers, MIND_TYPE);
            Thread.sleep(100L);
        }
    }

    /**
     * 创建流程图文件
     */
    @Test
    public void testCreateFlowFile() throws InterruptedException {
        for (int i = 0; i < 100; i++) {
            processOnService.createFile(headers, FLOW_TYPE);
            Thread.sleep(100L);
        }
    }

    @Before
    public void before() {
        headers = new Headers.Builder()
                .add("Cookie", properties.getCookie())
                .build();
        createAction = JSONObject.parseArray(createActionStr);
    }

    @Autowired
    private ProcessOnUserInfoProperties properties;

    private JSONArray createAction;
    private Headers headers;

}
/*
添加脑图
 [{
	"action": "create",
	"content": {
		"content": [],
		"index": {
			"78250f96d687": 0
		},
		"parts": {
			"78250f96d687": "right"
		},
		"updates": {},
		"original": {},
		"updateTps": {}
	}
}]
 */