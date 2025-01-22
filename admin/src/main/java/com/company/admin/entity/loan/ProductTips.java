package com.company.admin.entity.loan;

import com.company.admin.entity.base.BaseModel;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;
import org.hibernate.validator.constraints.Length;
import javax.validation.constraints.NotBlank;
import org.hibernate.validator.constraints.URL;

/**
 * 产品提示
 * Created by JQ棣 on 2018/11/20.
 */
@Accessors(chain = true)
@Getter
@Setter
public class ProductTips extends BaseModel {

    /**
     * id
     */
    private Long id;

    /**
     * 名称
     */
    @NotBlank(message = "名称不能为空", groups = Save.class)
    @Length(min = 1, max = 255, message = "名称长度为1-255个字符", groups = {Save.class, Update.class})
    private String name;

    /**
     * 图标
     */
    @URL(message = "图标链接错误", groups = {Save.class, Update.class})
    private String icon;

    /**
     * 提示内容
     */
    @Length(max = 65535, message = "提示内容长度最多为65535个字符", groups = {Save.class, Update.class})
    private String tips;

    public interface Save {
    }

    public interface Update {
    }
}
