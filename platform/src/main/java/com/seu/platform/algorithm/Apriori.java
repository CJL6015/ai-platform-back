package com.seu.platform.algorithm;

import lombok.extern.slf4j.Slf4j;

import java.text.DecimalFormat;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @author chenjiale
 * @version 1.0
 * @date 2023-12-09 14:17
 */
@Slf4j
public class Apriori {
    List<Set<String>> allFrequentSet = new ArrayList<>();
    List<Set<String>> data;
    List<Set<String>> sc;
    double minSupport;
    double confidence;

    public Apriori(List<Set<String>> data, double minSupport, double confidence) {
        this.data = data;
        this.minSupport = minSupport;
        this.confidence = confidence;
        List<Set<String>> itemSet = createItemSet(data);
        int k = 0;
        while (!itemSet.isEmpty()) {
            Map<Set<String>, Integer> frequent = frequentSet(itemSet, this.data);
            sc = scan(frequent, this.minSupport);
            itemSet = candidateSet(sc);
        }
    }


    //关联规则
    public List<List<Object>> associationRules() {
        List<List<Object>> result = new ArrayList<>();
        DecimalFormat df = new DecimalFormat("0.00");
        sc.forEach(set -> {
            List<Set<String>> subset = nonEmptySubset(set);
            int y = support(set, data);
            double support = 1.0 * y / data.size();
            subset.forEach(sub -> {
                int x = support(sub, data);
                HashSet<String> target = new HashSet<>(set);
                target.removeAll(sub);
                double conf = 1.0 * y / x;
                List<Object> d = new ArrayList<>();
                d.add(sub);
                d.add(target);
                d.add(df.format(conf));
                d.add(df.format(support));
                d.add(conf > this.confidence ? "是" : "否");
                result.add(d);
            });
        });
        return result;
    }

    public List<Object[]> getGroup() {
        DecimalFormat df = new DecimalFormat("0.00");
        List<Object[]> group = new ArrayList<>();
        sc.forEach(set -> {
            int y = support(set, data);
            double support = 1.0 * y / data.size();
            group.add(new Object[]{set, df.format(support)});
        });
        return group.stream().sorted((o1, o2) -> (Double.parseDouble(o2[1].toString()) >= Double.parseDouble(o1[1].toString())) ? 0 : 1).collect(Collectors.toList());
    }

    public Integer support(Set<String> set, List<Set<String>> data) {
        return (int) data.stream()
                .filter(d -> d.containsAll(set))
                .count();
    }

    //生成候选项集
    //安算法来讲应该是将候选项集按一定规则排序 将两个集合只有最后一个元素不同的合并
    //个人理解：
    //对任意频繁集A B属于S  若  C = A 并 B 是频繁的  则  D=C- （A 交  B） 也是频繁的
    public List<Set<String>> candidateSet(List<Set<String>> list) {//候选项集
        List<Set<String>> candidate = new ArrayList<Set<String>>();
        for (int i = 0; i < list.size(); i++) {
            for (int j = i + 1; j < list.size(); j++) {
                Set<String> item = list.get(i);
                Set<String> compare = list.get(j);
                if (item.size() == 1) {
                    Set<String> unionn = new HashSet<String>(item);
                    unionn.addAll(compare);
                    candidate.add(unionn);
                } else {
                    Set<String> intersection = new HashSet<String>(item);
                    intersection.retainAll(compare);
                    if (!intersection.isEmpty()) {
                        Set<String> unionn = new HashSet<String>(item);
                        unionn.addAll(compare);
                        Set<String> difference = new HashSet<String>(unionn);
                        difference.removeAll(intersection);
                        if (allFrequentSet.contains(difference)) {
                            candidate.add(unionn);
                        }
                    }
                }
            }
        }
        candidate = candidate.stream().distinct().collect(Collectors.toList());
        return candidate;
    }

    //扫描满足最小支持度的频繁项目集
    public List<Set<String>> scan(Map<Set<String>, Integer> frequent, double minSupport) {
        List<Set<String>> list = new ArrayList<Set<String>>();
        for (Set<String> key : frequent.keySet()) {
            if (frequent.get(key) >= minSupport * data.size()) {
                list.add(key);
                allFrequentSet.add(key);
            }
        }
        return list;
    }

    //建立频繁项目集与支持度的映射关系
    public Map<Set<String>, Integer> frequentSet(List<Set<String>> candidate, List<Set<String>> data) {//频繁项集
        Map<Set<String>, Integer> frequent = new HashMap();
        candidate.forEach(can -> {
            Integer sup = support(can, data);
            frequent.put(can, sup);
        });
        return frequent;
    }

    //创建事务集
    public List<Set<String>> createItemSet(List<Set<String>> data) {
        List<Set<String>> itemSet = new ArrayList<Set<String>>();
        Set<String> set = new HashSet<String>();
        data.forEach(set::addAll);
        set.forEach(e -> itemSet.add(new HashSet<String>() {{
            add(e);
        }}));
        return itemSet;
    }

    //获取给定集合的所有非空真子集
    public List<Set<String>> nonEmptySubset(Set<String> set) {
        List<Set<String>> list = new ArrayList<>();
        List<HashSet<String>> arr = set.stream()
                .map(s -> new HashSet<String>() {{
                    add(s);
                }})
                .collect(Collectors.toList());
        //子集个数位2^n
        //遍历二进制序列的每一位，若为“1”则保留
        int max = (int) Math.pow(2, arr.size());
        for (int i = 0; i < max; i++) {
            Set<String> tmp = new HashSet<String>();
            String bt = Integer.toBinaryString(i);
            while (arr.size() > bt.length()) {
                bt = "0" + bt;
            }
            for (int j = 0; j < arr.size() && j < bt.length(); j++) {
                if (bt.charAt(j) == '1') {
                    tmp.addAll(arr.get(j));
                }
            }
            if (!tmp.isEmpty() && !tmp.containsAll(set)) {
                list.add(tmp);
            }
        }
        return list;
    }
}
