package payloads;

import javax.validation.constraints.NotNull;

public class ReqVenueInfoUi {
    private String name;
    @NotNull
    private int isActive;
    @NotNull
    private int status;

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
