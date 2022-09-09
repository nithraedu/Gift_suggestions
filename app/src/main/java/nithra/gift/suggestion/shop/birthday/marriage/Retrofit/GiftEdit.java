package nithra.gift.suggestion.shop.birthday.marriage.Retrofit;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class GiftEdit {
    @SerializedName("total_gifts")
    @Expose
    public String totalGifts;
    @SerializedName("id")
    @Expose
    public String id;
    @SerializedName("name")
    @Expose
    public String name;
    @SerializedName("seller_mobile")
    @Expose
    public String sellerMobile;
    @SerializedName("seller_mobile2")
    @Expose
    public String sellerMobile2;
    @SerializedName("shop_name")
    @Expose
    public String shopName;
    @SerializedName("address")
    @Expose
    public String address;
    @SerializedName("logo")
    @Expose
    public String logo;
    @SerializedName("state")
    @Expose
    public String state;
    @SerializedName("city")
    @Expose
    public String city;
    @SerializedName("district")
    @Expose
    public String district;
    @SerializedName("latitude")
    @Expose
    public String latitude;
    @SerializedName("shop_website")
    @Expose
    public String shopWebsite;
    @SerializedName("shop_email")
    @Expose
    public String shopEmail;
    @SerializedName("longitude")
    @Expose
    public String longitude;
    @SerializedName("pincode")
    @Expose
    public String pincode;
    @SerializedName("country")
    @Expose
    public String country;
    @SerializedName("status")
    @Expose
    public String status;

    public String getTotalGifts() {
        return totalGifts;
    }

    public void setTotalGifts(String totalGifts) {
        this.totalGifts = totalGifts;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSellerMobile() {
        return sellerMobile;
    }

    public void setSellerMobile(String sellerMobile) {
        this.sellerMobile = sellerMobile;
    }

    public String getSellerMobile2() {
        return sellerMobile2;
    }

    public void setSellerMobile2(String sellerMobile2) {
        this.sellerMobile2 = sellerMobile2;
    }

    public String getShopName() {
        return shopName;
    }

    public void setShopName(String shopName) {
        this.shopName = shopName;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getLogo() {
        return logo;
    }

    public void setLogo(String logo) {
        this.logo = logo;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getDistrict() {
        return district;
    }

    public void setDistrict(String district) {
        this.district = district;
    }

    public String getLatitude() {
        return latitude;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    public String getShopWebsite() {
        return shopWebsite;
    }

    public void setShopWebsite(String shopWebsite) {
        this.shopWebsite = shopWebsite;
    }

    public String getShopEmail() {
        return shopEmail;
    }

    public void setShopEmail(String shopEmail) {
        this.shopEmail = shopEmail;
    }

    public String getLongitude() {
        return longitude;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }

    public String getPincode() {
        return pincode;
    }

    public void setPincode(String pincode) {
        this.pincode = pincode;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
