package com.pramod.apartmentrental.Renter.my_listings;

public class RenterApartmentObject {

    private String listingId;
    private String listingName;
    private String listingDescription;
    private String listingPrice;
    private String listingImageUrl;

    public RenterApartmentObject(String listingId, String listingName, String listingDescription, String listingPrice, String listingImageUrl) {
        this.listingId = listingId;
        this.listingName = listingName;
        this.listingDescription = listingDescription;
        this.listingPrice = listingPrice;
        this.listingImageUrl = listingImageUrl;
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
