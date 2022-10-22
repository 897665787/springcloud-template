package com.company.admin.entity.user;

import org.hibernate.validator.constraints.Length;
import javax.validation.constraints.NotBlank;

import com.company.admin.entity.base.BaseModel;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

/**
 * 定位
 * Created by wjc on 2018/11/20.
 */
@Accessors(chain = true)
@Getter
@Setter
public class UserMapPosition extends BaseModel {

    /**
     * id
     */
    private Long id;

    /**
     * 用户id
     */
    private String userId;

    /**
     * 位置地址
     */
    @NotBlank(message = "位置地址不能为空", groups = Save.class)
    @Length(min = 1, max = 255, message = "位置地址长度为1-255个字符", groups = {Save.class, Update.class})
    private String address;

    /**
     * 经度
     */
    @Length(max = 255, message = "经度长度最多为255个字符", groups = {Save.class, Update.class})
    private String lng;

    /**
     * 纬度
     */
    @NotBlank(message = "纬度不能为空", groups = Save.class)
    @Length(min = 1, max = 255, message = "纬度长度为1-255个字符", groups = {Save.class, Update.class})
    private String lat;


    /**
     * 备注
     */
    @Length(max = 65535, message = "备注长度最多为65535个字符", groups = {Save.class, Update.class})
    private String remark;

    public UserMapPosition() {
    }

    public UserMapPosition(String userId) {
        this.userId = userId;
    }

    public interface Save {
    }

    public interface Update {
    }

    public String position() {
        return lng.concat(",").concat(lat);
    }
}
