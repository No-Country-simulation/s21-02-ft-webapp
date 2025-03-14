package com.wallex.financial_platform.dtos.officialBank;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Data
@Getter
@Setter
@JsonIgnoreProperties(ignoreUnknown = true)
public class StadisticResponseDto {
    @JsonProperty(value="status")
    private Integer status;
    @JsonProperty(value="results")
    private List<StadisticVariableResponseDto> results;
}
