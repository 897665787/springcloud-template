package com.company.admin.service.{module};

import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.service.IService;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.company.admin.entity.{module}.{ModelName};
import com.company.admin.mapper.{module}.{ModelName}Dao;

@Service
public class {ModelName}Service extends ServiceImpl<{ModelName}Dao, {ModelName}>
		implements IService<{ModelName}> {

}