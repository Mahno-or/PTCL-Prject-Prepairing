package com.evampsaanga.FilterService.dto;

import lombok.Data;

@Data
public class ResponseDto {
    private String responseCode;
    private String responseDesc;
    private String execTime;
    private String timeStamp;
    private String responseConfig;
    private String responseString;
}
