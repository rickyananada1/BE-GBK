package com.dev.gbk.payloads;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Setter
@Getter
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

}