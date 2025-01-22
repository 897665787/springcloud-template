package com.company.admin.mapper.image;

import com.company.admin.entity.image.ImageCategory;
import com.company.admin.entity.system.Dictionary;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 图片分类Dao
 * Created by JQ棣 on 2017/10/23.
 */
public interface ImageCategoryDao {

    int save(ImageCategory imageCategory);

    int remove(ImageCategory imageCategory);

    int update(ImageCategory imageCategory);

    int batchUpdate(ImageCategory imageCategory);

    int batchUpdateLock(ImageCategory imageCategory);

    ImageCategory get(ImageCategory imageCategory);

    List<ImageCategory> list(ImageCategory imageCategory);

    List<ImageCategory> listCombo(ImageCategory imageCategory);

    Long count(ImageCategory imageCategory);

    ImageCategory getByKey(@Param("key") String key);

    int saveImageCategoryJumpType(ImageCategory imageCategory);

    int removeImageCategoryJumpType(ImageCategory imageCategory);

    List<Dictionary> listJumpType(ImageCategory imageCategory);
}
