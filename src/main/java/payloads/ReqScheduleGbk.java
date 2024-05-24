package payloads;

import com.fasterxml.jackson.annotation.JsonProperty;

public class ReqScheduleGbk {
    @JsonProperty("PartnerID")
    private int partnerId;
    @JsonProperty("VenueID")
    private int venueId;
    @JsonProperty("DateFrom")
    private String dateFrom;
    @JsonProperty("DateTo")
    private String dateTo;

    public ReqScheduleGbk(int partnerId, ReqScheduleUi reqScheduleUi) {
        this.partnerId = partnerId;
        this.venueId = reqScheduleUi.getVenueId();
        this.dateFrom = reqScheduleUi.getDateFrom();
        this.dateTo = reqScheduleUi.getDateTo();
    }

    public int getPartnerId() {
        return partnerId;
    }

    public void setPartnerId(int partnerId) {
        this.partnerId = partnerId;
    }

    public int getVenueId() {
        return venueId;
    }

    public void setVenueId(int venueId) {
        this.venueId = venueId;
    }

    public String getDateFrom() {
        return dateFrom;
    }

    public void setDateFrom(String dateFrom) {
        this.dateFrom = dateFrom;
    }

    public String getDateTo() {
        return dateTo;
    }

    public void setDateTo(String dateTo) {
        this.dateTo = dateTo;
    }
}
