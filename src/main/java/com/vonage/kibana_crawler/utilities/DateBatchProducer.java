package com.vonage.kibana_crawler.utilities;

import org.apache.commons.lang3.tuple.Pair;

import java.time.LocalDateTime;
import java.util.Iterator;
import java.util.concurrent.TimeUnit;

public final class DateBatchProducer implements Iterator<Pair<LocalDateTime, LocalDateTime>> {

    private LocalDateTime start;

    private final LocalDateTime end;

    private final TimeUnit timeUnit;

    private final int multiplier;

    public DateBatchProducer(LocalDateTime start, LocalDateTime end, TimeUnit timeUnit, int multiplier) {
        if(start.isAfter(end)){
            throw new IllegalArgumentException("Start date cannot be after end date");
        }
        if(multiplier <= 0){
            throw new IllegalArgumentException("Multiplier must be greater than zero");
        }
        this.start = start;
        this.end = end;
        this.timeUnit = timeUnit;
        this.multiplier = multiplier;
    }

    private LocalDateTime updateStart(){
        start = incrementDate(start, timeUnit, multiplier);
        return start;
    }

    private LocalDateTime incrementDate(LocalDateTime start, TimeUnit timeUnit, int multiple) {
        switch (timeUnit) {
            case MINUTES:
                start = start.plusMinutes(multiple);
                break;
            case HOURS:
                start = start.plusHours(multiple);
                break;
            case DAYS:
                start = start.plusDays(multiple);
                break;
            default:
                start = incrementDate(start, TimeUnit.HOURS, multiple);

        }
        return start;
    }

    @Override
    public boolean hasNext() {
        return start.isBefore(end);
    }

    @Override
    public Pair<LocalDateTime, LocalDateTime> next() {
        LocalDateTime _start = start;
        LocalDateTime _end = updateStart();
        return Pair.of(_start, _end);
    }

    public static DateBatchProducer defaultDateBatchProducer(LocalDateTime start, LocalDateTime end) {
        return new DateBatchProducer(start, end, TimeUnit.HOURS, 1);
    }
}
