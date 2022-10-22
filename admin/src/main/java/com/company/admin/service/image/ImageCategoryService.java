package com.company.admin.service.image;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.company.admin.annotation.XSTransactional;
import com.company.admin.entity.image.Image;
import com.company.admin.entity.image.ImageCategory;
import com.company.admin.entity.system.Dictionary;
import com.company.admin.exception.ExceptionConsts;
import com.company.admin.mapper.image.ImageCategoryDao;
import com.company.admin.mapper.image.ImageDao;
import com.company.admin.service.system.DictionaryService;
import com.company.admin.util.XSTreeUtil;
import com.company.admin.util.XSUuidUtil;

/**
 * 图片分类ServiceImpl
 * Created by xuxiaowei on 2017/10/23.
 */
@Service
public class ImageCategoryService {

    @Autowired
    private ImageCategoryDao imageCategoryDao;

    @Autowired
    private ImageDao imageDao;

    @Autowired
    private DictionaryService dictionaryService;

    @XSTransactional
    public void save(ImageCategory imageCategory) {
        ImageCategory criteria = new ImageCategory();
        criteria.setKey(imageCategory.getKey());
        Long count = imageCategoryDao.count(criteria);
        if (count.compareTo(0L) > 0) {
            throw ExceptionConsts.IMAGE_CATEGORY_EXIST;
        }
        //父级隐藏则子级必须隐藏，父级锁定则子级必须锁定
        if (imageCategory.getParent() != null) {
            ImageCategory parent = imageCategoryDao.get(imageCategory.getParent());
            if (parent != null) {
                if (parent.getStatus().equals(0)) {
                    imageCategory.setStatus(0);
                }
                if (parent.getLock().equals(1)) {
                    imageCategory.setLock(1);
                }
            }
        }
        imageCategory.setId(XSUuidUtil.generate());
        if (imageCategory.getParent() == null || imageCategory.getParent().getId() == null) {
            ImageCategory parent = new ImageCategory();
            parent.setId("");
            imageCategory.setParent(parent);
        }
        imageCategoryDao.save(imageCategory);
        if (!CollectionUtils.isEmpty(imageCategory.getJumpTypeDictDataList())) {
            imageCategoryDao.saveImageCategoryJumpType(imageCategory);
        }
    }

    @XSTransactional
    public void remove(ImageCategory imageCategory) {
        ImageCategory existent = get(imageCategory);
        if (existent.getLock().equals(1)) {
            throw ExceptionConsts.IMAGE_CATEGORY_LOCKED;
        }
        //校验该分类下是否存在子级
        ImageCategory children = new ImageCategory();
        children.setParent(existent);
        Long childrenCount = imageCategoryDao.count(children);
        if (childrenCount.compareTo(0L) > 0) {
            throw ExceptionConsts.IMAGE_CATEGORY_USED;
        }
        //校验该分类下是否存在图片
        Image image = new Image();
        List<String> cateList = listSubTree(imageCategory.getId());
        image.setCategoryList(cateList);
        Long count1 = imageDao.count(image);
        if (count1.compareTo(0L) > 0) {
            throw ExceptionConsts.IMAGE_CATEGORY_USED;
        }
        imageCategoryDao.remove(existent);
        imageCategoryDao.removeImageCategoryJumpType(imageCategory);
    }

    @XSTransactional
    public void update(ImageCategory imageCategory) {
        ImageCategory existent = get(imageCategory);
        if (imageCategory.getKey() != null) {
            ImageCategory criteria = new ImageCategory();
            criteria.setKey(imageCategory.getKey());
            List<ImageCategory> existents = imageCategoryDao.list(criteria);
            if (existents.size() > 0) {
                boolean isSelf = existents.get(0).getId().equals(existent.getId());
                if (!isSelf) {
                    throw ExceptionConsts.IMAGE_CATEGORY_EXIST;
                }
            }
        }
        //不能选择自己或自己的下级作为父级
        List<ImageCategory> list = imageCategoryDao.listCombo(new ImageCategory());
        Map<String, ImageCategory> map = XSTreeUtil.buildTree(list);
        List<ImageCategory> subList = XSTreeUtil.listSubTree(map.get(imageCategory.getId()));
        if(imageCategory.getParent().getId() != null){
            boolean isSelfSub = subList.stream().anyMatch(s -> s.getId().equals(imageCategory.getParent().getId()));
            if(isSelfSub){
                throw ExceptionConsts.IMAGE_CATEGORY_NOT_SELF_OR_SUB;
            }
        }
        if (imageCategory.getParent() == null || imageCategory.getParent().getId() == null) {
            ImageCategory parent = new ImageCategory();
            parent.setId("");
            imageCategory.setParent(parent);
        }
        imageCategoryDao.update(imageCategory);
        if (imageCategory.getStatus() != null) {
            ImageCategory latest = new ImageCategory();
            latest.setStatus(imageCategory.getStatus());
            //取消展示则所有子级也取消，开启展示则直属父级和所有子级也开启
            if (imageCategory.getStatus().equals(0)) {
                List<ImageCategory> subTreeList = XSTreeUtil.listSubTree(map.get(imageCategory.getId()));
                latest.setList(subTreeList);
            }
            else {
                List<ImageCategory> treePath = XSTreeUtil.getTreePath(map, map.get(imageCategory.getId()));
                latest.setList(treePath);
                List<ImageCategory> subTreeList = XSTreeUtil.listSubTree(map.get(imageCategory.getId()));
                latest.getList().addAll(subTreeList);
            }
            imageCategoryDao.batchUpdate(latest);
        }
        if (imageCategory.getJumpTypeDictDataList() != null) {
            imageCategoryDao.removeImageCategoryJumpType(existent);
            if (!CollectionUtils.isEmpty(imageCategory.getJumpTypeDictDataList())) {
                imageCategoryDao.saveImageCategoryJumpType(imageCategory);
            }
        }
    }

    public void updateLock(ImageCategory imageCategory) {
        ImageCategory existent = get(imageCategory);
        List<ImageCategory> list = imageCategoryDao.listCombo(new ImageCategory());
        Map<String, ImageCategory> map = XSTreeUtil.buildTree(list);
        //解锁则所有子级也解锁，锁定则直属父级和所有子级也锁定
        ImageCategory latest = new ImageCategory();
        if (existent.getLock().equals(0)) {
            latest.setLock(1);
            List<ImageCategory> treePath = XSTreeUtil.getTreePath(map, map.get(imageCategory.getId()));
            latest.setList(treePath);
            List<ImageCategory> subTreeList = XSTreeUtil.listSubTree(map.get(imageCategory.getId()));
            latest.getList().addAll(subTreeList);
        }
        else {
            latest.setLock(0);
            List<ImageCategory> subTreeList = XSTreeUtil.listSubTree(map.get(imageCategory.getId()));
            latest.setList(subTreeList);
        }
        imageCategoryDao.batchUpdateLock(latest);
    }

    public ImageCategory get(ImageCategory imageCategory) {
        ImageCategory existent = imageCategoryDao.get(imageCategory);
        if (existent == null) {
            throw ExceptionConsts.IMAGE_CATEGORY_NOT_EXIST;
        }
        List<Dictionary> ownedJumpType = imageCategoryDao.listJumpType(imageCategory);
        Map<String, String> jumpTypeDict = dictionaryService.mapByCategory("imageJumpType");
        List<Dictionary> dictDataList = new ArrayList<>();
        for (Map.Entry<String, String> entry : jumpTypeDict.entrySet()) {
            Dictionary dictData = new Dictionary();
            dictData.setKey(entry.getKey());
            dictData.setValue(entry.getValue());
            dictData.setChecked(0);
            for (Dictionary item : ownedJumpType) {
                if (entry.getKey().equals(item.getKey())) {
                    dictData.setChecked(1);
                    break;
                }
            }
            dictDataList.add(dictData);
        }
        existent.setJumpTypeDictDataList(dictDataList);
        return existent;
    }

    public List<?> tree(ImageCategory imageCategory) {
        imageCategory.setDefaultSort("seq", "DESC");
        List<ImageCategory> list = imageCategoryDao.listCombo(imageCategory);
        XSTreeUtil.buildTree(list);
        return XSTreeUtil.getSubTrees(list, new ImageCategory(""));
    }

    public List<String> listSubTree(String id) {
        List<ImageCategory> list = imageCategoryDao.listCombo(new ImageCategory());
        Map<String, ImageCategory> map = XSTreeUtil.buildTree(list);
        List<ImageCategory> cates = XSTreeUtil.listSubTree(map.get(id));
        List<String> orgList = cates.stream().map(a -> a.getId()).collect(Collectors.toList());
        return orgList;
    }

    public ImageCategory getByKey(String key) {
        ImageCategory existent = imageCategoryDao.getByKey(key);
        if (existent == null) {
            throw ExceptionConsts.IMAGE_CATEGORY_NOT_EXIST;
        }
        return existent;
    }

    public List<Dictionary> listJumpType(ImageCategory imageCategory) {
        List<Dictionary> ownedJumpType = imageCategoryDao.listJumpType(imageCategory);
        Map<String, String> jumpTypeDict = dictionaryService.mapByCategory("imageJumpType");
        List<Dictionary> dictDataList = new ArrayList<>();
        for (Map.Entry<String, String> entry : jumpTypeDict.entrySet()) {
            for (Dictionary item : ownedJumpType) {
                if (entry.getKey().equals(item.getKey())) {
                    Dictionary dictData = new Dictionary();
                    dictData.setKey(entry.getKey());
                    dictData.setValue(entry.getValue());
                    dictDataList.add(dictData);
                }
            }
        }
        return dictDataList;
    }
}
