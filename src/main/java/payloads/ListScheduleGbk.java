package payloads;

import com.fasterxml.jackson.annotation.JsonProperty;

public class ListScheduleGbk {
    @JsonProperty("BookingNumber")
    private String bookingNumber;
    @JsonProperty("VenueID")
    private int venueId;
    @JsonProperty("PackageID")
    private String packageId;
    @JsonProperty("Type")
    private String type;
    @JsonProperty("ProfileEvent")
    private String profileEvent;
    @JsonProperty("DescriptionEvent")
    private String DescriptionEvent;
    @JsonProperty("ScheduleDate")
    private String scheduleDate;
    @JsonProperty("ScheduleTimeFrom")
    private String ScheduleDateFrom;
    @JsonProperty("ScheduleTimeTo")
    private String ScheduleDateTo;

    public String getBookingNumber() {
        return bookingNumber;
    }

    public void setBookingNumber(String bookingNumber) {
        this.bookingNumber = bookingNumber;
    }

    public int getVenueId() {
        return venueId;
    }

    public void setVenueId(int venueId) {
        this.venueId = venueId;
    }

    public String getPackageId() {
        return packageId;
    }

    public void setPackageId(String packageId) {
        this.packageId = packageId;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getProfileEvent() {
        return profileEvent;
    }

    public void setProfileEvent(String profileEvent) {
        this.profileEvent = profileEvent;
    }

    public String getDescriptionEvent() {
        return DescriptionEvent;
    }

    public void setDescriptionEvent(String descriptionEvent) {
        DescriptionEvent = descriptionEvent;
    }

    public String getScheduleDate() {
        return scheduleDate;
    }

    public void setScheduleDate(String scheduleDate) {
        this.scheduleDate = scheduleDate;
    }

    public String getScheduleDateFrom() {
        return ScheduleDateFrom;
    }

    public void setScheduleDateFrom(String scheduleDateFrom) {
        ScheduleDateFrom = scheduleDateFrom;
    }

    public String getScheduleDateTo() {
        return ScheduleDateTo;
    }

    public void setScheduleDateTo(String scheduleDateTo) {
        ScheduleDateTo = scheduleDateTo;
    }
}
