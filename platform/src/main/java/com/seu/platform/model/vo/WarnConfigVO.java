package com.seu.platform.model.vo;

import lombok.*;

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
}
