package com.pramod.apartmentrental.Admin.listings;

public class AdminListingsObject {

    private String listingId;
    private String listingName;
    private String listingDescription;
    private String listingPrice;
    private String listingImageUrl;
    private String listingRenter;

    public AdminListingsObject(String listingId, String listingName, String listingDescription, String listingPrice, String listingImageUrl, String listingRenter) {
        this.listingId = listingId;
        this.listingName = listingName;
        this.listingDescription = listingDescription;
        this.listingPrice = listingPrice;
        this.listingImageUrl = listingImageUrl;
        this.listingRenter = listingRenter;
    }

    public AdminListingsObject(String listingName, String listingDescription, String listingPrice, String listingImageUrl) {
        this.listingName = listingName;
        this.listingDescription = listingDescription;
        this.listingPrice = listingPrice;
        this.listingImageUrl = listingImageUrl;
    }

    public String getListingRenter() {
        return listingRenter;
    }

    public void setListingRenter(String listingRenter) {
        this.listingRenter = listingRenter;
    }

    public String getListingId() {
        return listingId;
    }

    public void setListingId(String listingId) {
        this.listingId = listingId;
    }

    public String getListingName() {
        return listingName;
    }

    public void setListingName(String listingName) {
        this.listingName = listingName;
    }

    public String getListingDescription() {
        return listingDescription;
    }

    public void setListingDescription(String listingDescription) {
        this.listingDescription = listingDescription;
    }

    public String getListingPrice() {
        return listingPrice;
    }

    public void setListingPrice(String listingPrice) {
        this.listingPrice = listingPrice;
    }

    public String getListingImageUrl() {
        return listingImageUrl;
    }

    public void setListingImageUrl(String listingImageUrl) {
        this.listingImageUrl = listingImageUrl;
    }


}
