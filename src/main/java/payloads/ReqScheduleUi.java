package payloads;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

public class ReqScheduleUi {
    @NotNull
    private int venueId;
    @NotNull
    @NotBlank
    private String dateFrom;
    @NotNull
    @NotBlank
    private String dateTo;

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
