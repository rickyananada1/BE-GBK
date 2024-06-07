package com.dev.gbk.model;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import java.time.LocalTime;

@Entity
public class Time {
    @Id
    private Long id;

    @Basic(optional = false)
    @Column(name = "start_time")
    private LocalTime startTime;

    @Basic(optional = false)
    @Column(name = "end_time")
    private LocalTime endTime;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public LocalTime getStartTime() {
        return startTime;
    }

    void setStartTime(LocalTime startTime) {
        this.startTime = startTime;
    }

    public LocalTime getEndTime() {
        return endTime;
    }

    void setEndTime(LocalTime endTime) {
        this.endTime = endTime;
    }
}
