package com.company.{module}.service;

import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.company.{module}.entity.{ModelName};
import com.company.{module}.mapper.{ModelName}Mapper;

@Service
public class {ModelName}Service extends ServiceImpl<{ModelName}Mapper, {ModelName}>
		implements IService<{ModelName}> {

}