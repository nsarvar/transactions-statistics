package com.n26.payload;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.n26.config.BigDecimalSerializer;
import lombok.Data;

import java.math.BigDecimal;
import java.math.RoundingMode;

@Data
public class Statistics {
    @JsonSerialize(using = BigDecimalSerializer.class)
    private BigDecimal sum;
    @JsonSerialize(using = BigDecimalSerializer.class)
    private BigDecimal avg;
    @JsonSerialize(using = BigDecimalSerializer.class)
    private BigDecimal max;
    @JsonSerialize(using = BigDecimalSerializer.class)
    private BigDecimal min;
    private long count;

    public Statistics() {
        this.sum = BigDecimal.ZERO;
        this.min = BigDecimal.ZERO;
        this.max = BigDecimal.ZERO;
        this.avg = BigDecimal.ZERO;
    }

    public void accept(BigDecimal value) {
        ++this.count;
        this.sum = this.sum.add(value);
        if (this.min.equals(BigDecimal.ZERO))
            this.min = value;
        else
            this.min = this.min.min(value);
        if (this.max.equals(BigDecimal.ZERO))
            this.max = value;
        else
            this.max = this.max.max(value);
    }

    public void combine(Statistics other) {
        this.count += other.count;
        this.sum = this.sum.add(other.sum);

        if (this.min.equals(BigDecimal.ZERO))
            this.min = other.min;
        else
            this.min = this.min.min(other.min);
        if (this.max.equals(BigDecimal.ZERO))
            this.max = other.max;
        else
            this.max = this.max.max(other.max);
    }

    public BigDecimal getAvg() {
        return (this.count > 0) ? sum.divide(BigDecimal.valueOf(count), 2, RoundingMode.HALF_UP) : BigDecimal.ZERO;
    }
}
