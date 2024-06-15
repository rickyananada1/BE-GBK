package payloads;

import com.fasterxml.jackson.annotation.JsonProperty;

public class ReqGbkToken {
    @JsonProperty("PartnerID")
    private int PartnerID;
    @JsonProperty("PartnerKey")
    private String PartnerKey;

    public ReqGbkToken(int partnerID, String partnerKey) {
        this.PartnerID = partnerID;
        this.PartnerKey = partnerKey;
    }

    public int getPartnerID() {
        return PartnerID;
    }

    private void setPartnerID(int partnerID) {
        this.PartnerID = partnerID;
    }

    public String getPartnerKey() {
        return PartnerKey;
    }

    private void setPartnerKey(String partnerKey) {
        this.PartnerKey = partnerKey;
    }
}
