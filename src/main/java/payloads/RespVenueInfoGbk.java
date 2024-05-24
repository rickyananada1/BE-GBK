package payloads;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public class RespVenueInfoGbk {
    @JsonProperty("Code")
    private String code;
    @JsonProperty("Status")
    private String status;
    @JsonProperty("Message")
    private String message;
    @JsonProperty("Data")
    private List<ListDataVenueInfoGbk> data;

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public List<ListDataVenueInfoGbk> getData() {
        return data;
    }

    public void setData(List<ListDataVenueInfoGbk> data) {
        this.data = data;
    }
}
