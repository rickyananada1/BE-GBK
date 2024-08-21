package com.dev.gbk.model;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.LocalDate;
import java.util.List;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonManagedReference;

import jakarta.persistence.CollectionTable;
import jakarta.persistence.Column;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "schedules")
@EntityListeners(AuditingEntityListener.class)
public class Schedule implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "booking_number")
    private String bookingNumber;

    @ManyToMany
    @CollectionTable(name = "schedules_venues", joinColumns = @JoinColumn(name = "schedule_id", referencedColumnName = "id"))
    private List<Venue> venues;

    @Column(name = "type")
    private String type;

    @Column(name = "profile_event")
    private String profileEvent;

    @Column(name = "description_event")
    private String descriptionEvent;

    @Column(name = "games")
    private String games;

    @Column(name = "category")
    private String category;

    @Column(name = "schedule_start_in_load")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    private LocalDate scheduleStartInLoad;

    @Column(name = "schedule_end_in_load")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    private LocalDate scheduleEndInLoad;

    @Column(name = "start_date")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    private LocalDate scheduleStartDate;

    @Column(name = "end_date")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    private LocalDate scheduleEndDate;

    @Column(name = "schedule_start_out_load")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    private LocalDate scheduleStartOutLoad;

    @Column(name = "schedule_end_out_load")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    private LocalDate scheduleEndOutLoad;

    @ElementCollection // To store multiple scheduleTimes
    @CollectionTable(name = "schedule_times", joinColumns = @JoinColumn(name = "schedule_id"))
    private List<String> scheduleTime;

    @ElementCollection
    @CollectionTable(name = "schedule_sessions", joinColumns = @JoinColumn(name = "schedule_id"))
    private List<String> session;

    @Column(name = "status_payment")
    private String statusPayment; // Renamed for clarity

    @Column(name = "status_booking")
    private String statusBooking;

    @Column(name = "total_sf")
    private BigDecimal totalSF;

    @Column(name = "total_paid")
    private BigDecimal totalPaid;

    @Column(name = "customer_name")
    private String customerName;

    @Column(name = "customer_email")
    private String customerEmail;

    @Column(name = "customer_phone")
    private String customerPhone;

    @CreationTimestamp
    @Column(name = "created_at")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime updatedAt;

    @CreatedBy
    @Column(name = "created_by")
    private Long createdBy;

    @LastModifiedDate
    @Column(name = "updated_by")
    private Long updatedBy;
}
