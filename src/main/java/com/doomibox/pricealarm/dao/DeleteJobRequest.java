package com.doomibox.pricealarm.dao;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;

@Data
@Getter
@AllArgsConstructor
public class DeleteJobRequest {
    private String jobKey;
}
