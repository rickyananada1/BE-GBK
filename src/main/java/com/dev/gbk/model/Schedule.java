package com.dev.gbk.model;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.LocalDate;
import java.util.List;

import com.dev.gbk.dto.CardSewaLahanDTO;
import jakarta.persistence.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "schedules", indexes = {
        @Index(name = "idx_booking_number", columnList = "booking_number"),
        @Index(name = "idx_type", columnList = "type"),
        @Index(name = "idx_profile_event", columnList = "profile_event"),
        @Index(name = "idx_games", columnList = "games"),
        @Index(name = "idx_category", columnList = "category"),
        @Index(name = "idx_schedule_start_in_load", columnList = "schedule_start_in_load"),
        @Index(name = "idx_schedule_end_in_load", columnList = "schedule_end_in_load"),
        @Index(name = "idx_start_date", columnList = "start_date"),
        @Index(name = "idx_end_date", columnList = "end_date"),
        @Index(name = "idx_status_payment", columnList = "status_payment"),
        @Index(name = "idx_status_booking", columnList = "status_booking")
})
@EntityListeners(AuditingEntityListener.class)
@SqlResultSetMapping(
        name = "CardSewaLahanDTOMapping",
        classes = @ConstructorResult(
                targetClass = CardSewaLahanDTO.class,
                columns = {
                        @ColumnResult(name = "tenant_name", type = String.class),
                        @ColumnResult(name = "tanggal", type = String.class)
                }
        )
)
@NamedNativeQueries({
        @NamedNativeQuery(
                name = "getSewaLahanCardData",
                query = "SELECT v2.venue AS tenant_name, " +
                        "s.start_date AS tanggal " +
                        "FROM schedules s " +
                        "INNER JOIN schedules_venues sv ON s.id = sv.schedule_id " +
                        "JOIN schedule_times st ON s.id = st.schedule_id " +
                        "JOIN venues v2 ON sv.venue_id = v2.id " +
                        "WHERE s.category = 'Sewa Lahan' " +
                        "AND s.status_payment = 'Paid' " +
                        "AND s.status_booking = 'Processing' " +
                        "AND v2.venue in :unit " +
                        "AND s.start_date >= :startDate " +
                        "AND s.end_date <= :endDate " +
                        "GROUP BY s.start_date",
                resultSetMapping = "CardSewaLahanDTOMapping"
        )
})
public class Schedule implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "booking_number")
    private String bookingNumber;

    @ManyToMany
    @JoinTable(name = "schedules_venues", joinColumns = @JoinColumn(name = "schedule_id", referencedColumnName = "id"), inverseJoinColumns = @JoinColumn(name = "venue_id", referencedColumnName = "id"))
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

    @Column(name = "size_of_field")
    private Long sizeOfField;
}
