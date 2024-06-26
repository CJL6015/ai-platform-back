package com.seu.platform.model.vo;

import lombok.*;

/**
 * @author 陈小黑
 */
@Data
@Builder
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class WarnConfigVO {
    /**
     * 制药工序人员上限
     */
    private Integer pharmaceuticalProcessLimit;

    /**
     * 包装工序人员上限
     */
    private Integer packagingProcessLimit;

    /**
     * 装药工序人员上限
     */
    private Integer fillingProcessLimit;

    /**
     * 装车工序人员上限
     */
    private Integer loadingProcessLimit;

    /**
     * 生产线总限值
     */
    private Integer totalLimit;

    /**
     * 超限/停机扣分
     */
    private Double score;

    /**
     * 超高高限/低低限扣分
     */
    private Double highScore;

    private Double peopleScore;
}
