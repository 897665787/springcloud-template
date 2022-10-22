package com.company.admin.mapper.system;


import com.company.admin.entity.system.Version;

import java.util.List;

/**
 * Created by gustinlau on 11/1/17.
 */
public interface VersionDao {
    
    int save(Version version);

    int remove(Version version);

    int update(Version version);

    Version get(Version version);

    List<Version> list(Version version);

    Long count(Version version);
}
