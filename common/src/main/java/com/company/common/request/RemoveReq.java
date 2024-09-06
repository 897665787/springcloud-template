package com.company.common.request;

import java.util.List;

import lombok.Data;

@Data
public class RemoveReq<T> {
    private List<T> idList;
}
