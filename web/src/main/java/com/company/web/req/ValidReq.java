package com.company.web.req;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@Data
public class ValidReq {
    @NotBlank
	String p1;

    @NotEmpty
	String p2;

    @NotNull
	Integer p3;
}
