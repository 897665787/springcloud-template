package com.company.system.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.experimental.Accessors;

import java.time.LocalDateTime;

@TableName("bu_system_inner_callback")
@Data
@Accessors(chain = true)
public class InnerCallback {
    /**
     * ID
     */
    @TableId(type = IdType.AUTO)
    private Integer id;

    /**
     * 回调地址
     */
    private String url;
    /**
     * JSON参数
     */
    private String jsonParams;
    /**
     * JSON结果
     */
    private String jsonResult;
    /**
     * 处理器bean名称
     */
    private String processorBeanName;
    /**
     * 状态(1:待回调,2:回调成功(END),21.回调失败,22.放弃回调(END))
     */
    private Integer status;
    /**
     * 递增秒数策略(increment:增量,fix:固定)
     */
    private String secondsStrategy;
    /**
     * 递增秒数
     */
    private Integer increaseSeconds;
    /**
     * 下次处理时间
     */
    private LocalDateTime nextDisposeTime;
    /**
     * 最大允许失败次数
     */
    private Integer maxFailure;
    /**
     * 当前失败次数
     */
    private Integer failure;

    /**
     * 追踪ID
     */
    private String traceId;

    /**
     * 备注信息
     */
    private String remark;
    /**
     * 创建时间
     */
    private LocalDateTime createTime;

    /**
     * 更新时间
     */
    private LocalDateTime updateTime;
}