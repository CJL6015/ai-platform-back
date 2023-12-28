package com.seu.platform.exa;

import cn.hutool.http.HttpResponse;
import cn.hutool.http.HttpUtil;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.seu.platform.common.constant.Numbers;
import com.seu.platform.exa.model.ExaPoint;
import com.seu.platform.exa.model.ExaPointResponse;
import com.seu.platform.exa.model.RecordsFloat;
import com.seu.platform.exa.model.ValueFloat;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author chenjiale
 * @version 1.0
 * @date 2023-09-23 19:27
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class ExaClient {
    private static final long TIME_DELTA = 8 * 60 * 60 * 1000;
    private static final String SELECT_STR_FORMATTER = "Name like '%%%s%%'";

    @Value("${exa.get-value}")
    private String getValueUrl;

    @Value("${exa.get-values}")
    private String getValuesUrl;

    @Value("${exa.get-values-boolean}")
    private String getValuesBooleanUrl;

    @Value("${exa.get-history}")
    private String getHistoryUrl;

    @Value("${exa.get-points}")
    private String getPoints;

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
        Map<String, Object> body = new HashMap<>(Numbers.FOUR);
        body.put("Names", points);
        try (HttpResponse response = HttpUtil.createPost(getValuesUrl)
                .body(JSON.toJSONString(body))
                .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .execute()) {
            String post = response.body();
            JSONObject jsonObject = JSON.parseObject(post);
            JSONArray values = jsonObject.getJSONArray("Values");
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
     * 批量获取点号时实值
     *
     * @param points 点号
     * @return 时实值
     */
    public Boolean[] getValuesBoolean(List<String> points) {
        Boolean[] res = new Boolean[points.size()];
        Map<String, Object> body = new HashMap<>(Numbers.FOUR);
        body.put("Names", points);
        try (HttpResponse response = HttpUtil.createPost(getValuesBooleanUrl)
                .body(JSON.toJSONString(body))
                .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .execute()) {
            String post = response.body();
            JSONObject jsonObject = JSON.parseObject(post);
            JSONArray values = jsonObject.getJSONArray("Values");
            if (values.size() != points.size()) {
                log.error("exa返回结果异常,points:{},返回结果:{}", points, post);
                return res;
            }
            for (int i = 0; i < values.size(); i++) {
                res[i] = values.getBoolean(i);
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
        Map<String, Object> body = new HashMap<>(Numbers.FOUR);
        body.put("Name", point);
        body.put("Aggregator", "avg");
        body.put("Start", st - TIME_DELTA);
        body.put("End", et - TIME_DELTA);
        body.put("Interval", 300);

        try (HttpResponse response = HttpUtil.createPost(getHistoryUrl)
                .body(JSON.toJSONString(body))
                .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .execute()) {
            String post = response.body();
            result = JSON.parseObject(post, RecordsFloat.class);
            List<Long> timestamps = result.getTimestamps();
            result.setTimestamps(timestamps.stream().map(t -> t + TIME_DELTA).collect(Collectors.toList()));
        } catch (Exception e) {
            log.error("exa获取历史异常,point:{},st:{},et:{}", point, st, et);
        }
        return result;
    }

    /**
     * 获取测点历史值
     *
     * @param point 测点
     * @param st    开始时间
     * @param et    结束时间
     * @return 历史值
     */
    public RecordsFloat getHistoryBoolean(String point, Long st, Long et) {
        RecordsFloat result = null;
        Map<String, Object> body = new HashMap<>(Numbers.FOUR);
        body.put("Name", point);
        body.put("Aggregator", "avg");
        body.put("Start", st - TIME_DELTA);
        body.put("End", et - TIME_DELTA);
        body.put("Interval", 300);

        try (HttpResponse response = HttpUtil.createPost(getHistoryUrl)
                .body(JSON.toJSONString(body))
                .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .execute()) {
            String post = response.body();
            result = JSON.parseObject(post, RecordsFloat.class);
            List<Long> timestamps = result.getTimestamps();
            result.setTimestamps(timestamps.stream().map(t -> t + TIME_DELTA).collect(Collectors.toList()));
        } catch (Exception e) {
            log.error("exa获取历史异常,point:{},st:{},et:{}", point, st, et);
        }
        return result;
    }

    public List<ExaPoint> getExaPoints(String name) {
        List<ExaPoint> list = new ArrayList<>();
        String filter = String.format(SELECT_STR_FORMATTER, name);
        Map<String, Object> body = new HashMap<>(Numbers.FOUR);
        body.put("Filter", filter);
        try (HttpResponse response = HttpUtil.createPost(getPoints)
                .body(JSON.toJSONString(body))
                .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .execute()) {
            String post = response.body();
            ExaPointResponse exaPointResponse = JSON.parseObject(post, ExaPointResponse.class);
            String object = JSON.parseObject(exaPointResponse.getVariablesJson(), String.class);
            list = JSON.parseArray(object, ExaPoint.class);
        } catch (Exception e) {
            log.error("exa获取测点异常,name:{}", name);
        }
        return list;
    }
}
