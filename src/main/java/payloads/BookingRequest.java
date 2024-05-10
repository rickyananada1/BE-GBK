package payloads;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.util.Date;

public class BookingRequest {

    @NotBlank
    @Size(min=3,max = 40)
    private String venue;

    @NotBlank
    @Size(min=5,max = 15)
    private Integer rangeDate;

    @NotBlank
    @Size(min=10,max = 40)
    private Date waktuMulai;

    @NotBlank
    @Size(min=10,max = 100)
    private Date waktuSelesai;

    @NotBlank
    @Size(max = 40)
    private Long user_id;


    public String getVenue() {
        return venue;
    }

    public void setVenue(String venue) {
        this.venue = venue;
    }

    public Integer getRangeDate() {
        return rangeDate;
    }

    public void setRangeDate(Integer rangeDate) {
        this.rangeDate = rangeDate;
    }

    public Date getWaktuMulai() {
        return waktuMulai;
    }

    public void setWaktuMulai(Date waktuMulai) {
        this.waktuMulai = waktuMulai;
    }

    public Date getWaktuSelesai() {
        return waktuSelesai;
    }

    public void setWaktuSelesai(Date waktuSelesai) {
        this.waktuSelesai = waktuSelesai;
    }

    public Long getUser_id() {
        return user_id;
    }

    public void setUser_id(Long user_id) {
        this.user_id = user_id;
    }
}
