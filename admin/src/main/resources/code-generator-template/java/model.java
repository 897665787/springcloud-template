package com.company.admin.entity.{module};

import java.math.BigDecimal;
import java.util.Date;

import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableName;
import com.baomidou.mybatisplus.enums.IdType;
import com.company.admin.jackson.annotation.AutoDesc;

import lombok.Data;

/**
 * {module_name}
 * 
 * @author CodeGenerator
 */
@Data
@TableName("{table}")
public class {ModelName} {
{column_field}
	public interface Save {
	}
	
	public interface Update {
	}
}