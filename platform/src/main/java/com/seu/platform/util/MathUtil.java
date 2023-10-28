package com.seu.platform.util;

import lombok.experimental.UtilityClass;
import org.apache.commons.math3.fitting.PolynomialCurveFitter;
import org.apache.commons.math3.fitting.WeightedObservedPoints;

import java.util.List;

/**
 * @author chenjiale
 * @version 1.0
 * @date 2023-10-04 13:53
 */
@UtilityClass
public class MathUtil {

    public double[] fitting(List<Integer> nums, int degree) {
        WeightedObservedPoints points = new WeightedObservedPoints();
        for (int i = 0; i < nums.size(); i++) {
            Integer num = nums.get(i);
            points.add(i, num);
        }
        return PolynomialCurveFitter.create(degree).fit(points.toList());
    }
}
