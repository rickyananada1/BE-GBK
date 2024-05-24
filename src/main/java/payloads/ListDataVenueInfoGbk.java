package payloads;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public class ListDataVenueInfoGbk {
    @JsonProperty("ID")
    private int id;
    @JsonProperty("ParentID")
    private int parentId;
    @JsonProperty("MaxClubMember")
    private int maxClubMember;
    @JsonProperty("Name")
    private String name;
    @JsonProperty("UnitNumber")
    private String unitNumber;
    @JsonProperty("Large")
    private String large;
    @JsonProperty("Capacity")
    private String capacity;
    @JsonProperty("CapacityVisitor")
    private String capacityVisitor;
    @JsonProperty("PhoneVenue")
    private String phoneVenue;
    @JsonProperty("Description")
    private String description;
    @JsonProperty("PrimaryImage")
    private String primaryImage;
    @JsonProperty("IsActive")
    private int isActive;
    @JsonProperty("Status")
    private int status;
    @JsonProperty("UnitName")
    private String unitName;
    @JsonProperty("UnitSimpleName")
    private String unitSimpleName;
    @JsonProperty("UnitSheetName")
    private String unitSheetName;
    @JsonProperty("CategoryName")
    private String categoryName;
    @JsonProperty("Gallery")
    private List<String> gallery;
    @JsonProperty("Ratting")
    private String rating;


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getParentId() {
        return parentId;
    }

    public void setParentId(int parentId) {
        this.parentId = parentId;
    }

    public int getMaxClubMember() {
        return maxClubMember;
    }

    public void setMaxClubMember(int maxClubMember) {
        this.maxClubMember = maxClubMember;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUnitNumber() {
        return unitNumber;
    }

    public void setUnitNumber(String unitNumber) {
        this.unitNumber = unitNumber;
    }

    public String getLarge() {
        return large;
    }

    public void setLarge(String large) {
        this.large = large;
    }

    public String getCapacity() {
        return capacity;
    }

    public void setCapacity(String capacity) {
        this.capacity = capacity;
    }

    public String getCapacityVisitor() {
        return capacityVisitor;
    }

    public void setCapacityVisitor(String capacityVisitor) {
        this.capacityVisitor = capacityVisitor;
    }

    public String getPhoneVenue() {
        return phoneVenue;
    }

    public void setPhoneVenue(String phoneVenue) {
        this.phoneVenue = phoneVenue;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getPrimaryImage() {
        return primaryImage;
    }

    public void setPrimaryImage(String primaryImage) {
        this.primaryImage = primaryImage;
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

    public String getUnitName() {
        return unitName;
    }

    public void setUnitName(String unitName) {
        this.unitName = unitName;
    }

    public String getUnitSimpleName() {
        return unitSimpleName;
    }

    public void setUnitSimpleName(String unitSimpleName) {
        this.unitSimpleName = unitSimpleName;
    }

    public String getUnitSheetName() {
        return unitSheetName;
    }

    public void setUnitSheetName(String unitSheetName) {
        this.unitSheetName = unitSheetName;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }

    public List<String> getGallery() {
        return gallery;
    }

    public void setGallery(List<String> gallery) {
        this.gallery = gallery;
    }

    public String getRating() {
        return rating;
    }

    public void setRating(String rating) {
        this.rating = rating;
    }
}
