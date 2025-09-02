package com.company.tool.smartretry;

import com.company.tool.entity.AppInfo;
import com.github.smartretry.core.RetryFunction;
import org.springframework.stereotype.Service;

@Service
public class CgService implements ICgService {

    @RetryFunction(maxRetryCount = 3, interval = 3)
    @Override
    public AppInfo get(AppInfo info) {
        if (true) {
            throw new RuntimeException("error");
        }
        return new AppInfo();
    }
}
