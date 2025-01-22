package com.company.admin.service.image;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.company.admin.entity.base.XSPageModel;
import com.company.admin.entity.image.Image;
import com.company.admin.entity.image.ImageCategory;
import com.company.admin.entity.system.Dictionary;
import com.company.admin.mapper.image.ImageCategoryDao;
import com.company.admin.mapper.image.ImageDao;
import com.company.admin.service.system.DictionaryService;
import com.company.admin.util.XSUuidUtil;
import com.company.common.exception.BusinessException;

/**
 * 图片ServiceImpl
 * Created by JQ棣 on 2017/10/26.
 */
@Service
public class ImageService {

    private static final Logger logger = LoggerFactory.getLogger(ImageService.class);

    @Autowired
    private ImageDao imageDao;

    @Autowired
    private ImageCategoryService imageCategoryService;

    @Autowired
    private ImageCategoryDao imageCategoryDao;

    @Autowired
    private DictionaryService dictionaryService;

    public void save(Image image) {
        image.setId(XSUuidUtil.generate());
        if (image.getJumpType().equals(0)) {//网页链接
            if (StringUtils.isBlank(image.getWebLink())) {
                throw new BusinessException("网页链接不能为空");
            }
        } else if (image.getJumpType().equals(1)){//APP内页
			if (image.getAppInnerPageDict() == null) {
				throw new BusinessException("App内页字典不能为空");
			}
        }
        imageDao.save(image);
    }

    public void remove(Image image) {
        get(image);
        imageDao.remove(image);
    }

    public void update(Image image) {
        get(image);
        if(image.getJumpType()!=null){
            if (image.getJumpType().equals(0)) {//网页链接
            	if (StringUtils.isBlank(image.getWebLink())) {
                    throw new BusinessException("网页链接不能为空");
                }
            } else if (image.getJumpType().equals(1)){//APP内页
				if (image.getAppInnerPageDict() == null) {
					throw new BusinessException("App内页字典不能为空");
				}
            }
        }
        imageDao.update(image);
    }

    public Image get(Image image) {
        Image existent = imageDao.get(image);
        if (existent == null) {
            throw new BusinessException("图片不存在");
        }

        List<Dictionary> ownedJumpType = imageCategoryDao.listJumpType(existent.getCategory());
        Map<String, String> jumpTypeDict = dictionaryService.mapByCategory("imageJumpType");
        List<Dictionary> dictDataList = new ArrayList<>();
        for (Map.Entry<String, String> entry : jumpTypeDict.entrySet()) {
            for (Dictionary item : ownedJumpType) {
                if (entry.getKey().equals(item.getKey())) {
                    Dictionary dictData = new Dictionary();
                    dictData.setKey(entry.getKey());
                    dictData.setValue(entry.getValue());
                    dictData.setChecked(0);
                    if (dictData.getValue().equals(existent.getJumpType())) {
                        dictData.setChecked(1);
                    }
                    dictDataList.add(dictData);
                }
            }
        }
        existent.setJumpTypeDictDataList(dictDataList);
        return existent;
    }

    public Image getByCategory(Image image) {
        return imageDao.getByCategory(image);
    }

    public XSPageModel<Image> listAndCount(Image image) {
        image.setDefaultSort(new String[]{"i.seq", "i.create_time"},new String[]{"DESC", "DESC"});
        if (image.getCategory() != null && image.getCategory().getId() != null) {
            List<String> categoryList = imageCategoryService.listSubTree(image.getCategory().getId());
            image.setCategoryList(categoryList);
        }

        if (image.getCategory() != null && image.getCategory().getKey() != null) {
            try {
                ImageCategory c = imageCategoryService.getByKey(image.getCategory().getKey());
                List<String> categoryList = imageCategoryService.listSubTree(c.getId());
                image.setCategoryList(categoryList);
            } catch (BusinessException e) {
                logger.error("error : ", e);
            }
        }

        return XSPageModel.build(imageDao.list(image), imageDao.count(image));
    }
}
