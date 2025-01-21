package com.company.admin.entity.marketing;

import javax.validation.constraints.Pattern;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import org.hibernate.validator.constraints.Length;

import com.company.admin.entity.base.BaseModel;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

/**
 * Created by gustinlau on 11/1/17.
 */
@Accessors(chain = true)
@Getter
@Setter
@TableName("sc_feedback")
public class Feedback extends BaseModel {

    @TableId("id")
    private Long id;
    /**
     * 用户名
     */
    @Length(max = 20, message = "名称长度为1-20个字符", groups = Save.class)
    private String name;

    /**
     * 手机号
     */
    @Pattern(regexp = "^1[0-9]{10}$", message = "手机号格式错误", groups = Save.class)
    private String mobile;

//    /**
//     * 标题
//     */
//    @NotNull(message = "标题不能为空", groups = Save.class)
//    @Length(min = 1, max = 255, message = "名称长度为1-255个字符", groups = Save.class)
//    private String title;
    /**
     * 内容
     */
    @Length( max = 500, message = "内容长度最多500个字符", groups = Save.class)
    private String content;

    /**
     * 是否解决 1 是 0：否
     */
    private Integer status;

    public interface Save {}

}
