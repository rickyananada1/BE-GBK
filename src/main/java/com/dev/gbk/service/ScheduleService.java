package com.dev.gbk.service;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import com.dev.gbk.dto.ScheduleRequest;

import com.dev.gbk.model.Unit;
import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.JoinType;
import jakarta.persistence.criteria.Predicate;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import com.dev.gbk.exception.ResourceNotFoundException;
import com.dev.gbk.model.Schedule;
import com.dev.gbk.model.Venue;
import com.dev.gbk.payloads.ListScheduleGbk;
import com.dev.gbk.repository.ScheduleRepository;
import com.dev.gbk.repository.VenueRepository;
import com.dev.gbk.spesification.SpecificationBuilderImpl;
import com.dev.gbk.utils.Utils;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;

@Service
public class ScheduleService {

    private final ScheduleRepository scheduleRepository;
    private final VenueRepository venueRepository;

    // logger
    private final Logger log = LoggerFactory.getLogger(ScheduleService.class);
    private final SpecificationBuilderImpl<Schedule> specificationBuilder = new SpecificationBuilderImpl<>(
            new ObjectMapper(), Schedule.class);

    public ScheduleService(
            ScheduleRepository scheduleRepository, VenueRepository venueRepository) {
        this.scheduleRepository = scheduleRepository;
        this.venueRepository = venueRepository;
    }

    public Page<Schedule> findAll(String search, int page, int size, String unit, String event, LocalDate tanggal, LocalDate end, String status) {
        Sort sort = Sort.by(Sort.Direction.DESC, "updatedAt").and(Sort.by(Sort.Direction.DESC, "createdAt"));
        Pageable pageable = PageRequest.of(page, size, sort);

        Specification<Schedule> scheduleSpecification = (root, query, criteriaBuilder) -> {
            query.distinct(true);

            List<Predicate> predicates = new ArrayList<>();

            if (unit != null && !unit.isEmpty()) {
                predicates.add(criteriaBuilder.equal(root.get("venues").get("unit").get("name"), unit));
            }

            if (event != null && !event.isEmpty()) {
                predicates.add(criteriaBuilder.equal(root.get("category"), event));
            }

            if (tanggal != null && end != null) {
                predicates.add(criteriaBuilder.between(root.get("scheduleStartDate"), tanggal, end));
            } else if (tanggal != null) {
                predicates.add(criteriaBuilder.greaterThanOrEqualTo(root.get("scheduleStartDate"), tanggal));
            } else if (end != null) {
                predicates.add(criteriaBuilder.lessThanOrEqualTo(root.get("scheduleEndDate"), end));
            }

            if (status != null && !status.isEmpty()) {
                predicates.add(criteriaBuilder.equal(root.get("statusPayment"), status));
            }

            return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
        };

        Optional<Specification<Schedule>> specification = specificationBuilder.parseAndBuild(search);

        return specification
                .map(spec -> scheduleRepository.findAll(spec.and(scheduleSpecification), pageable))
                .orElseGet(() -> scheduleRepository.findAll(scheduleSpecification, pageable));
    }

    public List<Schedule> findAll(String search, String unit, String event, LocalDate tanggal, LocalDate end, String status) {
        Sort sort = Sort.by(Sort.Direction.DESC, "updatedAt").and(Sort.by(Sort.Direction.DESC, "createdAt"));

        Specification<Schedule> scheduleSpecification = (root, query, criteriaBuilder) -> {
            query.distinct(true);

            List<Predicate> predicates = new ArrayList<>();

            if (unit != null && !unit.isEmpty()) {
                predicates.add(criteriaBuilder.equal(root.get("venues").get("unit").get("name"), unit));
            }

            if (event != null && !event.isEmpty()) {
                predicates.add(criteriaBuilder.equal(root.get("category"), event));
            }

            if (tanggal != null && end != null) {
                predicates.add(criteriaBuilder.between(root.get("scheduleStartDate"), tanggal, end));
            } else if (tanggal != null) {
                predicates.add(criteriaBuilder.greaterThanOrEqualTo(root.get("scheduleStartDate"), tanggal));
            } else if (end != null) {
                predicates.add(criteriaBuilder.lessThanOrEqualTo(root.get("scheduleEndDate"), end));
            }

            if (status != null && !status.isEmpty()) {
                predicates.add(criteriaBuilder.equal(root.get("statusPayment"), status));
            }

            return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
        };

        Optional<Specification<Schedule>> specification = specificationBuilder.parseAndBuild(search);

        return specification
                .map(spec -> scheduleRepository.findAll(spec.and(scheduleSpecification), sort))
                .orElseGet(() -> scheduleRepository.findAll(scheduleSpecification, sort));
    }


    public List<Schedule> findPendingSchedulesCreatedBefore(Long venue) {
        LocalDateTime currentDateTime = LocalDateTime.now();
        if (venue != null) {
            return scheduleRepository.findByStatusPaymentAndVenuesIdAndCreatedAtBefore("Soft Booking", venue);
        } else {
            return scheduleRepository.findByStatusPaymentAndCreatedAtBefore("Soft Booking");
        }
    }

    public Schedule findById(Long id) {
        return scheduleRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Schedule not found"));
    }

    public Schedule store(ScheduleRequest scheduleRequest) {
        // get all venue in schedulerequest by venue id
        List<Venue> venues = new ArrayList<>();
        for (Long id : scheduleRequest.getVenueID()) {
            Venue venue = venueRepository.findById(id)
                    .orElseThrow(() -> new ResourceNotFoundException("Venue not found"));
            venues.add(venue);
        }
    
        // get last id from schedule
        Schedule latestSchedule = scheduleRepository.findTopByOrderByIdDesc();
        Long lastId = latestSchedule == null ? 0L : latestSchedule.getId();
    
        Schedule schedule = Schedule.builder()
                .bookingNumber(Utils.generateBookingNumber(lastId, scheduleRequest.getStatusPayment()))
                .type(scheduleRequest.getType())
                .profileEvent(scheduleRequest.getProfileEvent())
                .descriptionEvent(scheduleRequest.getDescriptionEvent())
                .games(scheduleRequest.getGames())
                .category(scheduleRequest.getCategory())
                .session(scheduleRequest.getSession())
                .statusBooking(scheduleRequest.getStatusBooking())
                .statusPayment(scheduleRequest.getStatusPayment())
                .totalPaid(Utils.convertStringToBigDecimal(scheduleRequest.getTotalPaid()))
                .totalSF(Utils.convertStringToBigDecimal(scheduleRequest.getTotalSF()))
                .customerName(scheduleRequest.getCustomerName())
                .customerEmail(scheduleRequest.getCustomerEmail())
                .customerPhone(scheduleRequest.getCustomerPhone())
                .scheduleTime(scheduleRequest.getScheduleTime())
                .venues(venues)
                .sizeOfField(scheduleRequest.getSizeOfField())
                .build();

        if (scheduleRequest.getScheduleStartInLoad() != null && scheduleRequest.getScheduleEndInLoad() != null) {
            LocalDate requestStartInLoad = Utils.convertStringToLocalDate(scheduleRequest.getScheduleStartInLoad());
            LocalDate requestEndInLoad = Utils.convertStringToLocalDate(scheduleRequest.getScheduleEndInLoad());

            schedule.setScheduleStartInLoad(requestStartInLoad);
            schedule.setScheduleEndInLoad(requestEndInLoad);

            if (checkScheduleExists(requestStartInLoad, requestEndInLoad, venues, schedule.getSession())) {
                throw new ResourceNotFoundException("Schedule already exists");
            }
        }


        if (scheduleRequest.getScheduleStartOutLoad() != null && scheduleRequest.getScheduleEndOutLoad() != null) {
            schedule.setScheduleStartOutLoad(Utils.convertStringToLocalDate(scheduleRequest.getScheduleStartOutLoad()));
            schedule.setScheduleEndOutLoad(Utils.convertStringToLocalDate(scheduleRequest.getScheduleEndOutLoad()));
        }
    
        if (scheduleRequest.getScheduleStartDate() != null && scheduleRequest.getScheduleEndDate() != null) {
            schedule.setScheduleStartDate(Utils.convertStringToLocalDate(scheduleRequest.getScheduleStartDate()));
            schedule.setScheduleEndDate(Utils.convertStringToLocalDate(scheduleRequest.getScheduleEndDate()));
        }
    
        return scheduleRepository.save(schedule);
    }

    public boolean checkScheduleExists(LocalDate scheduleStartInLoad, LocalDate scheduleEndInLoad, List<Venue> venues,List<String> sessionSchedule) {
        List<Long> venueIds = venues.stream()
                .map(Venue::getId)
                .collect(Collectors.toList());

        return scheduleRepository.existsByScheduleStartInLoadAndScheduleEndInLoadAndVenues(
                scheduleStartInLoad, scheduleEndInLoad,sessionSchedule, venueIds) == 1;
    }



    public Schedule update(Long id, ScheduleRequest scheduleRequest) {
        Schedule schedule = findById(id);

        // get all venue in schedulerequest by venue id
        List<Venue> venues = new ArrayList<>();
        for (Long id2 : scheduleRequest.getVenueID()) {
            Venue venue = venueRepository.findById(id2)
                    .orElseThrow(() -> new ResourceNotFoundException("Venue not found"));
            venues.add(venue);
        }

        schedule.setType(scheduleRequest.getType());
        schedule.setProfileEvent(scheduleRequest.getProfileEvent());
        schedule.setDescriptionEvent(scheduleRequest.getDescriptionEvent());
        schedule.setGames(scheduleRequest.getGames());
        schedule.setCategory(scheduleRequest.getCategory());
        schedule.setSession(scheduleRequest.getSession());
        schedule.setStatusBooking(scheduleRequest.getStatusBooking());
        schedule.setStatusPayment(scheduleRequest.getStatusPayment());
        schedule.setTotalPaid(Utils.convertStringToBigDecimal(scheduleRequest.getTotalPaid()));
        schedule.setTotalSF(Utils.convertStringToBigDecimal(scheduleRequest.getTotalSF()));
        schedule.setScheduleTime(scheduleRequest.getScheduleTime());
        schedule.setCustomerName(scheduleRequest.getCustomerName());
        schedule.setCustomerEmail(scheduleRequest.getCustomerEmail());
        schedule.setCustomerPhone(scheduleRequest.getCustomerPhone());
        schedule.setVenues(venues);
        schedule.setSizeOfField(scheduleRequest.getSizeOfField());

        if (scheduleRequest.getScheduleStartDate() != null && scheduleRequest.getScheduleEndDate() != null) {
            schedule.setScheduleStartDate(Utils.convertStringToLocalDate(scheduleRequest.getScheduleStartDate()));
            schedule.setScheduleEndDate(Utils.convertStringToLocalDate(scheduleRequest.getScheduleEndDate()));
        }

        if (scheduleRequest.getScheduleStartInLoad() != null && scheduleRequest.getScheduleEndInLoad() != null) {
            schedule.setScheduleStartInLoad(Utils.convertStringToLocalDate(scheduleRequest.getScheduleStartInLoad()));
            schedule.setScheduleEndInLoad(Utils.convertStringToLocalDate(scheduleRequest.getScheduleEndInLoad()));
        }

        if (scheduleRequest.getScheduleStartOutLoad() != null && scheduleRequest.getScheduleEndOutLoad() != null) {
            schedule.setScheduleStartOutLoad(Utils.convertStringToLocalDate(scheduleRequest.getScheduleStartOutLoad()));
            schedule.setScheduleEndOutLoad(Utils.convertStringToLocalDate(scheduleRequest.getScheduleEndOutLoad()));
        }

        return scheduleRepository.save(schedule);
    }

    public void delete(Long id) {
        findById(id);
        scheduleRepository.deleteById(id);
    }

    public void synchronizeSchedules(List<ListScheduleGbk> schedules, Venue venue) {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        log.info("Synchronizing schedules");
        for (ListScheduleGbk schedule : schedules) {
            ScheduleRequest scheduleRequest = objectMapper.convertValue(schedule, ScheduleRequest.class);
            List<Long> venues = new ArrayList<>();
            venues.add(venue.getId());
            scheduleRequest.setVenueID(venues);
            store(scheduleRequest);
        }
    }

    public byte[] generateExcelFile(LocalDate start, LocalDate end, String unitName) throws IOException {
        try (Workbook workbook = new XSSFWorkbook()) {
            // Sheet 1
            createSheet(workbook, "List Transaction", scheduleRepository.findAllByDateAndUnit(start, end, unitName));

        // Sheet 2
            createSheetByKlasifikasi(workbook, "LT According Klasfikasi", scheduleRepository.findAllByProfileEventAndUnit(start, end, unitName));

            // Sheet 3
            createSheetByCategory(workbook, "LT According Kategori", scheduleRepository.findAllByCategoryAndUnit(start, end, unitName));

            // Write to byte array
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            workbook.write(outputStream);
            return outputStream.toByteArray();
        }
    }

    private void createSheet(Workbook workbook, String sheetName, List<Schedule> data) {
        Sheet sheet = workbook.createSheet(sheetName);
        int rowIndex = 0;
        Row headerRow = sheet.createRow(rowIndex++);
        headerRow.createCell(0).setCellValue("No");
        headerRow.createCell(1).setCellValue("Nama Penyewa");
        headerRow.createCell(2).setCellValue("Email Penyewa");
        headerRow.createCell(3).setCellValue("No. HP Penyewa");
        headerRow.createCell(4).setCellValue("Klasifikasi");
        headerRow.createCell(5).setCellValue("Deskripsi");
        headerRow.createCell(6).setCellValue("Tipe");
        headerRow.createCell(7).setCellValue("Kategori");
        headerRow.createCell(8).setCellValue("Tanggal In Loading");
        headerRow.createCell(9).setCellValue("Tanggal Event");
        headerRow.createCell(10).setCellValue("Tanggal Out Loading");
        headerRow.createCell(11).setCellValue("Unit");
        headerRow.createCell(12).setCellValue("Venue");
        headerRow.createCell(13).setCellValue("Sesi");
        headerRow.createCell(14).setCellValue("Jam Per Sesi");
        headerRow.createCell(15).setCellValue("Status Payment");
        headerRow.createCell(16).setCellValue("Status Booking");
        headerRow.createCell(17).setCellValue("Total Soft Booking");
        headerRow.createCell(18).setCellValue("Total Paid");

        for (Schedule rowData : data) {
            Row row = sheet.createRow(rowIndex++);
            row.createCell(0).setCellValue(rowIndex);

            row.createCell(1).setCellValue(rowData.getCustomerName() != null ? rowData.getCustomerName() : "");
            row.createCell(2).setCellValue(rowData.getCustomerEmail() != null ? rowData.getCustomerEmail() : "");
            row.createCell(3).setCellValue(rowData.getCustomerPhone() != null ? rowData.getCustomerPhone() : "");
            row.createCell(4).setCellValue(rowData.getProfileEvent() != null ? rowData.getProfileEvent() : "");
            row.createCell(5).setCellValue(rowData.getDescriptionEvent() != null ? rowData.getDescriptionEvent() : "");
            row.createCell(6).setCellValue(rowData.getType() != null ? rowData.getType() : "");
            row.createCell(7).setCellValue(rowData.getCategory() != null ? rowData.getCategory() : "");
            row.createCell(8).setCellValue(rowData.getScheduleStartInLoad() != null ? rowData.getScheduleStartInLoad().toString() : "");
            row.createCell(9).setCellValue(rowData.getScheduleStartDate() != null ? rowData.getScheduleStartDate().toString() : "");
            row.createCell(10).setCellValue(rowData.getScheduleEndOutLoad() != null ? rowData.getScheduleEndOutLoad().toString() : "");
            row.createCell(11).setCellValue(rowData.getVenues() != null && !rowData.getVenues().isEmpty()
                    ? rowData.getVenues().get(0).getUnit().getName()
                    : "");
            row.createCell(12).setCellValue(rowData.getVenues() != null && !rowData.getVenues().isEmpty()
                    ? rowData.getVenues().get(0).getVenue()
                    : "");
            row.createCell(13).setCellValue(rowData.getScheduleTime() != null ? rowData.getScheduleTime().toString() : "");
            row.createCell(14).setCellValue(rowData.getSession() != null ? rowData.getSession().toString() : "");
            row.createCell(15).setCellValue(rowData.getStatusPayment() != null ? rowData.getStatusPayment() : "");
            row.createCell(16).setCellValue(rowData.getStatusBooking() != null ? rowData.getStatusBooking() : "");
            row.createCell(17).setCellValue(rowData.getTotalSF() != null ? rowData.getTotalSF().toString() : "");
            row.createCell(18).setCellValue(rowData.getTotalPaid() != null ? rowData.getTotalPaid().toString() : "");
        }

    }

    private void createSheetByKlasifikasi(Workbook workbook, String sheetName, List<Schedule> data) {
        Sheet sheet = workbook.createSheet(sheetName);
        int rowIndex = 0;
        Row headerRow = sheet.createRow(rowIndex++);
        headerRow.createCell(0).setCellValue("No");
        headerRow.createCell(1).setCellValue("Nama Penyewa");
        headerRow.createCell(2).setCellValue("Nama Kegiatan");
        headerRow.createCell(3).setCellValue("Venue");
        headerRow.createCell(4).setCellValue("Tanggal");
        headerRow.createCell(5).setCellValue("Jenis");
        headerRow.createCell(6).setCellValue("Harga");
        headerRow.createCell(7).setCellValue("Klasifikasi");

        for (Schedule rowData : data) {
            Row row = sheet.createRow(rowIndex++);
            row.createCell(0).setCellValue(rowIndex);

            row.createCell(1).setCellValue(rowData.getCustomerName() != null ? rowData.getCustomerName() : "");
            row.createCell(2).setCellValue(rowData.getDescriptionEvent() != null ? rowData.getDescriptionEvent() : "");
            row.createCell(3).setCellValue(rowData.getVenues() != null && !rowData.getVenues().isEmpty()
                    ? rowData.getVenues().get(0).getVenue()
                    : "");
            row.createCell(4).setCellValue(rowData.getScheduleStartDate() != null ? rowData.getScheduleStartDate().toString() : "");
            row.createCell(5).setCellValue(rowData.getType() != null ? rowData.getType() : "");
            row.createCell(6).setCellValue(rowData.getTotalPaid() != null ? rowData.getTotalPaid().toString() : "");
            row.createCell(7).setCellValue(rowData.getProfileEvent() != null ? rowData.getProfileEvent() : "");
        }

    }

    private void createSheetByCategory(Workbook workbook, String sheetName, List<Schedule> data) {
        Sheet sheet = workbook.createSheet(sheetName);
        int rowIndex = 0;
        Row headerRow = sheet.createRow(rowIndex++);
        headerRow.createCell(0).setCellValue("No");
        headerRow.createCell(1).setCellValue("Nama Penyewa");
        headerRow.createCell(2).setCellValue("Nama Kegiatan");
        headerRow.createCell(3).setCellValue("Venue");
        headerRow.createCell(4).setCellValue("Tanggal");
        headerRow.createCell(5).setCellValue("Jenis");
        headerRow.createCell(6).setCellValue("Harga");
        headerRow.createCell(7).setCellValue("Kategori");

        for (Schedule rowData : data) {
            Row row = sheet.createRow(rowIndex++);
            row.createCell(0).setCellValue(rowIndex);

            row.createCell(1).setCellValue(rowData.getCustomerName() != null ? rowData.getCustomerName() : "");
            row.createCell(2).setCellValue(rowData.getDescriptionEvent() != null ? rowData.getDescriptionEvent() : "");
            row.createCell(3).setCellValue(rowData.getVenues() != null && !rowData.getVenues().isEmpty()
                    ? rowData.getVenues().get(0).getVenue()
                    : "");
            row.createCell(4).setCellValue(rowData.getScheduleStartDate() != null ? rowData.getScheduleStartDate().toString() : "");
            row.createCell(5).setCellValue(rowData.getType() != null ? rowData.getType() : "");
            row.createCell(6).setCellValue(rowData.getTotalPaid() != null ? rowData.getTotalPaid().toString() : "");
            row.createCell(7).setCellValue(rowData.getCategory() != null ? rowData.getCategory() : "");
        }

    }

}
