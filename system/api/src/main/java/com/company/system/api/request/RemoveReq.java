package com.company.system.api.request;

import java.util.List;

import lombok.Data;

@Data
public class RemoveReq<T> {
    private List<T> idList;
}
