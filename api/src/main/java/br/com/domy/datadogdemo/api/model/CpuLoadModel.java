package br.com.domy.datadogdemo.api.model;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;

import org.hibernate.validator.constraints.Range;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CpuLoadModel {
    @NotNull
    @Positive
    private int numCore;
    @NotNull
    @Positive
    private int numThreadsPerCore;
    @NotNull
    @Positive
    @Range(min = 0, max = 1)
    private double load;
    @NotNull
    @Positive
    private long duration;
    
}
