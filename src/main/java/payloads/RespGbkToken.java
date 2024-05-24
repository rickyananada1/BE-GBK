package payloads;

import com.fasterxml.jackson.annotation.JsonProperty;

public class RespGbkToken {
    @JsonProperty("Code")
    private String code;
    @JsonProperty("Status")
    private String status;
    @JsonProperty("Message")
    private String message;

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

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    @JsonProperty("Token")
    private String token;
}
