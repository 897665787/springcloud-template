package com.company.user.dto;

import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class DistributeAttach {
	String specContent;
	String userRemark;
	
	String shopCode;
	String shopName;
	String shopLogo;
}
