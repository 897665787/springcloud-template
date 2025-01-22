package com.company.admin.mapper.image;

import com.company.admin.entity.image.Image;

import java.util.List;

/**
 * 图片Dao
 * Created by JQ棣 on 2017/10/25.
 */
public interface ImageDao {

    int save(Image image);

    int remove(Image image);

    int update(Image image);

    Image get(Image image);

    List<Image> list(Image image);

    Long count(Image image);

    Image getByCategory(Image image);
}
