package payloads;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public class RespScheduleGbk {
    @JsonProperty("Code")
    private String code;
    @JsonProperty("Status")
    private String status;
    @JsonProperty("Message")
    private String message;
    @JsonProperty("Data")
    private List<ListScheduleGbk> data;

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

    public List<ListScheduleGbk> getData() {
        return data;
    }

    public void setData(List<ListScheduleGbk> data) {
        this.data = data;
    }
}
