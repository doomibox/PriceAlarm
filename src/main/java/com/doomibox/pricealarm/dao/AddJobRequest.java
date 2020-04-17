package com.doomibox.pricealarm.dao;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;

@Data
@Getter
@AllArgsConstructor
public class AddJobRequest {
    private String merchantName;
    private String merchantUrl;
}
