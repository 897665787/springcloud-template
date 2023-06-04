package com.company.admin.service.marketing;

import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.service.IService;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.company.admin.entity.marketing.HotWord;
import com.company.admin.mapper.marketing.HotWordDao;

@Service
public class HotWordService extends ServiceImpl<HotWordDao, HotWord>
		implements IService<HotWord> {

}