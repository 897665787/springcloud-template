package com.company.tool.api.request;

import lombok.Data;
import lombok.experimental.Accessors;

import java.util.List;

@Data
@Accessors(chain = true)
public class CommonI18nReq {

    private List<BusinessTypeId> businessTypeIdList;
    private String locale;

    @Data
    @Accessors(chain = true)
    public static class BusinessTypeId {

        /**
         * 业务类型
         */
        private String businessType;

        /**
         * 业务ID
         */
        private Integer businessId;
    }

}
