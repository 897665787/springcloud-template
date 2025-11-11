package com.company.web.req;

import lombok.Data;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.List;

@Data
public class ValidNestingReq {
    @NotBlank
    String p1;

    @NotEmpty
    String p2;

    @Valid // 嵌套校验需要在实例上声明 @Valid
    @NotNull
    AmeReq ame;

    @Valid // 嵌套校验需要在实例上声明 @Valid
    @NotEmpty
    List<AmeReq> ameList;

    @Data
    public static class AmeReq {
        @NotBlank
        String p1;

        @NotEmpty
        String p2;

        @NotNull
        Integer p3;
    }
}
