package com.doomibox.pricealarm.dao;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class JobInfo {
    private String jobKey;
    private String merchantUrl;
    private String merchantName;
}
