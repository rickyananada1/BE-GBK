package payloads;

import com.fasterxml.jackson.annotation.JsonProperty;

public class ReqVenueInfoGbk {
    @JsonProperty("PartnerID")
    private int partnerId;
    @JsonProperty("Name")
    private String name;
    @JsonProperty("IsActive")
    private int isActive;
    @JsonProperty("Status")
    private int status;

    public ReqVenueInfoGbk(int partnerId, ReqVenueInfoUi reqVenueInfoUi) {
        this.partnerId = partnerId;
        this.name = reqVenueInfoUi.getName();
        this.isActive = reqVenueInfoUi.getIsActive();
        this.status = reqVenueInfoUi.getStatus();
    }

    public int getPartnerId() {
        return partnerId;
    }

    public void setPartnerId(int partnerId) {
        this.partnerId = partnerId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getIsActive() {
        return isActive;
    }

    public void setIsActive(int isActive) {
        this.isActive = isActive;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

}
