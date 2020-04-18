package com.doomibox.pricealarm.dao;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Data
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class AddJobRequest {
    private String merchantName;
    private String merchantUrl;
}
