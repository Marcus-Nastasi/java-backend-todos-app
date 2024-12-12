package com.app.todos.domain.metrics;

import java.io.Serial;
import java.io.Serializable;
import java.math.BigDecimal;

public class MetricsResponse implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    private final Long total;
    private final Long high;
    private final Long medium;
    private final Long low;
    private final Long pending;
    private final Long in_progress;
    private final Long done;
    private final Long overdue;
    private final Long future;
    private final BigDecimal completion_rate;

    public MetricsResponse(Long total, Long high, Long medium, Long low, Long pending, Long in_progress, Long done, Long overdue, Long future, BigDecimal completion_rate) {
        this.total = total;
        this.high = high;
        this.medium = medium;
        this.low = low;
        this.pending = pending;
        this.in_progress = in_progress;
        this.done = done;
        this.overdue = overdue;
        this.future = future;
        this.completion_rate = completion_rate;
    }

    public Long getTotal() {
        return total;
    }

    public Long getHigh() {
        return high;
    }

    public Long getMedium() {
        return medium;
    }

    public Long getLow() {
        return low;
    }

    public Long getPending() {
        return pending;
    }

    public Long getIn_progress() {
        return in_progress;
    }

    public Long getDone() {
        return done;
    }

    public Long getOverdue() {
        return overdue;
    }

    public Long getFuture() {
        return future;
    }

    public BigDecimal getCompletion_rate() {
        return completion_rate;
    }
}
