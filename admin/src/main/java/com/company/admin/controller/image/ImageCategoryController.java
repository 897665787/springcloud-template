package com.company.admin.controller.image;



import com.company.admin.service.image.ImageCategoryService;
import com.company.admin.service.system.DictionaryService;
import com.company.common.api.Result;
import com.company.admin.entity.image.ImageCategory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * 图片分类Controller
 * Created by JQ棣 on 2017/10/23.
 */
@Controller
public class ImageCategoryController {

    @Autowired
    private ImageCategoryService imageCategoryService;

    @Autowired
    private DictionaryService dictionaryService;

    @RequestMapping(value = "/admin/content/imageCategory", method = RequestMethod.GET)
    public String index(Model model) {
        model.addAttribute("imageJumpTypeList", dictionaryService.mapByCategory("imageJumpType"));
        return "content/imageCategory";
    }

    @RequestMapping(value = "/admin/content/imageCategory/tree", method = RequestMethod.POST)
    @ResponseBody
    public Result<?> adminTree(ImageCategory imageCategory) {
        imageCategory = imageCategory == null ? new ImageCategory() : imageCategory;
        return Result.success(imageCategoryService.tree(imageCategory));
    }

    @RequestMapping(value = "/admin/content/imageCategory/get", method = RequestMethod.POST)
    @ResponseBody
    public Result<?> adminGet(ImageCategory imageCategory) {
        ImageCategory i = imageCategoryService.get(imageCategory);
        return Result.success(i);
    }

    @RequestMapping(value = "/admin/content/imageCategory/save", method = RequestMethod.POST)
    @ResponseBody
    public Result<?> adminSave(@Validated(ImageCategory.Save.class) @RequestBody ImageCategory imageCategory) {
        imageCategory.setLock(0);
        imageCategoryService.save(imageCategory);
        return Result.success();
    }

    @RequestMapping(value = "/admin/content/imageCategory/remove", method = RequestMethod.POST)
    @ResponseBody
    public Result<?> adminRemove(ImageCategory imageCategory) {
        imageCategoryService.remove(imageCategory);
        return Result.success();
    }

    @RequestMapping(value = "/admin/content/imageCategory/update", method = RequestMethod.POST)
    @ResponseBody
    public Result<?> adminUpdate(@Validated(ImageCategory.Update.class) @RequestBody ImageCategory imageCategory)
            {
        imageCategoryService.update(imageCategory);
        return Result.success();
    }

    @RequestMapping(value = "/admin/content/imageCategory/lock", method = RequestMethod.POST)
    @ResponseBody
    public Result<?> adminLock(ImageCategory imageCategory)
            {
        imageCategoryService.updateLock(imageCategory);
        return Result.success();
    }

    @RequestMapping(value = "/admin/content/imageCategory/jumpType/list", method = RequestMethod.POST)
    @ResponseBody
    public Result<?> listJumptype(ImageCategory imageCategory) {
        return Result.success(imageCategoryService.listJumpType(imageCategory));
    }
}
