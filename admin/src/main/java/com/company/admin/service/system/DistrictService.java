package com.company.admin.service.system;

import java.util.List;

import com.company.framework.globalresponse.ExceptionUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import com.company.admin.entity.base.XSPageModel;
import com.company.admin.entity.system.District;
import com.company.admin.mapper.system.DistrictDao;

/**
 * 区县ServiceImpl
 * Created by JQ棣 on 2018/05/30.
 */
@Service
@RequiredArgsConstructor
public class DistrictService {

    private final DistrictDao districtDao;

    public void save(District district) {
        if (null == district.getCity() || null == district.getCity().getId()) {
            ExceptionUtil.throwException("请选择区县所属城市");
        }
        District dist = new District(district.getId());
        Long count = districtDao.count(dist);
        if (count.compareTo(0L) > 0) {
            ExceptionUtil.throwException("区县已存在");
        }
        dist = new District();
        dist.setName(district.getName());
        dist.setCity(district.getCity());
        count = districtDao.count(dist);
        if (count.compareTo(0L) > 0) {
            ExceptionUtil.throwException("区县已存在");
        }
        dist.setStatus(0);
        districtDao.save(district);
    }

    public void remove(District district) {
        District existent = get(district);
        districtDao.remove(existent);
    }

    public void update(District district) {
        if (null == district.getCity() || null == district.getCity().getId()) {
            ExceptionUtil.throwException("请选择区县所属城市");
        }
        District existent = get(district);
        District dist = new District();
        dist.setName(district.getName());
        dist.setCity(district.getCity());
        List<District> existents = districtDao.list(dist);
        if (existents.size() > 0) {
            boolean isSelf = existents.get(0).getId().equals(existent.getId());
            if (!isSelf) {
                ExceptionUtil.throwException("区县已存在");
            }
        }
        districtDao.update(district);
    }

    public void updateStatus(District district) {
        districtDao.updateStatus(district);
    }

    public District get(District district) {
        District existent = districtDao.get(district);
        if (existent == null) {
            ExceptionUtil.throwException("区县不存在");
        }
        return existent;
    }

    public XSPageModel<District> listAndCount(District district) {
        district.setDefaultSort(new String[]{"city_id", "seq", "id"}, new String[]{"ASC", "DESC", "ASC"});
        List<District> districtList = districtDao.list(district);
        return XSPageModel.build(districtList, districtDao.count(district));
    }

    public List<District> listCombo(District district) {
        district.setDefaultSort("seq", "DESC");
        return districtDao.listCombo(district);
    }
}
