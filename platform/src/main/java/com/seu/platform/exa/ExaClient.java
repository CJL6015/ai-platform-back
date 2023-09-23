package com.seu.platform.exa;

import cn.hutool.http.HttpUtil;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.seu.platform.common.constant.Numbers;
import com.seu.platform.exa.model.RecordsFloat;
import com.seu.platform.exa.model.ValueFloat;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author chenjiale
 * @version 1.0
 * @date 2023-09-23 19:27
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class ExaClient {
    @Value("${exa.get-value}")
    private String getValueUrl;

    @Value("${exa.get-values}")
    private String getValuesUrl;

    @Value("${exa.get-history}")
    private String getHistoryUrl;

    public Float getValue(String point) {
        Float value = null;
        try {
            Map<String, Object> body = new HashMap<>(Numbers.FOUR);
            body.put("Name", point);
            String post = HttpUtil.post(getValueUrl, body);
            ValueFloat valueFloat = JSON.parseObject(post, ValueFloat.class);
            value = valueFloat.getValue();
        } catch (Exception e) {
            log.error("exa获取时实值异常,point:{}", point, e);
        }

        return value;
    }

    /**
     * 批量获取点号时实值
     *
     * @param points 点号
     * @return 时实值
     */
    public Float[] getValues(List<String> points) {
        Float[] res = new Float[points.size()];
        try {
            Map<String, Object> body = new HashMap<>(Numbers.FOUR);
            body.put("Names", points);
            String post = HttpUtil.post(getValuesUrl, body);
            JSONObject jsonObject = JSON.parseObject(post);
            JSONArray values = jsonObject.getJSONArray("values");
            if (values.size() != points.size()) {
                log.error("exa返回结果异常,points:{},返回结果:{}", points, post);
                return res;
            }
            for (int i = 0; i < values.size(); i++) {
                res[i] = values.getFloatValue(i);
            }
        } catch (Exception e) {
            log.error("获取exa点号时实值异常,points:{}", points, e);
        }
        return res;
    }

    /**
     * 获取测点历史值
     *
     * @param point 测点
     * @param st    开始时间
     * @param et    结束时间
     * @return 历史值
     */
    public RecordsFloat getHistory(String point, Long st, Long et) {
        RecordsFloat result = null;
        try {
            Map<String, Object> body = new HashMap<>(Numbers.FOUR);
            body.put("Name", point);
            body.put("Start", st);
            body.put("End", et);
            String post = HttpUtil.post(getHistoryUrl, body);
            result = JSON.parseObject(post, RecordsFloat.class);
        } catch (Exception e) {
            log.error("exa获取历史异常,point:{},st:{},et:{}", point, st, et);
        }
        return result;
    }
}
