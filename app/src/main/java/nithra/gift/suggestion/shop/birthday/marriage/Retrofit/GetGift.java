package nithra.gift.suggestion.shop.birthday.marriage.Retrofit;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class GetGift {
    @SerializedName("id")
    @Expose
    public String id;
    @SerializedName("seller_id")
    @Expose
    public String sellerId;
    @SerializedName("seller_mobile")
    @Expose
    public String sellerMobile;
    @SerializedName("shop_name")
    @Expose
    public String shopName;
    @SerializedName("address")
    @Expose
    public String address;
    @SerializedName("country_id")
    @Expose
    public String countryId;
    @SerializedName("country_name")
    @Expose
    public String countryName;
    @SerializedName("state")
    @Expose
    public String state;
    @SerializedName("city")
    @Expose
    public String city;
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
    @SerializedName("gift_name")
    @Expose
    public String giftName;
    @SerializedName("gift_image")
    @Expose
    public String giftImage;
    @SerializedName("gift_amount")
    @Expose
    public String giftAmount;
    @SerializedName("discount")
    @Expose
    public String discount;
    @SerializedName("total_amount")
    @Expose
    public String totalAmount;
    @SerializedName("gift_description")
    @Expose
    public String giftDescription;
    @SerializedName("gift_category")
    @Expose
    public String giftCategory;
    @SerializedName("gift_for")
    @Expose
    public String giftFor;
    @SerializedName("status")
    @Expose
    public String status;
    @SerializedName("fav")
    @Expose
    public Integer fav;
    @SerializedName("gift_cat")
    @Expose
    public String giftCat;
    @SerializedName("gift_for_people")
    @Expose
    public String giftForPeople;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getSellerId() {
        return sellerId;
    }

    public void setSellerId(String sellerId) {
        this.sellerId = sellerId;
    }

    public String getSellerMobile() {
        return sellerMobile;
    }

    public void setSellerMobile(String sellerMobile) {
        this.sellerMobile = sellerMobile;
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

    public String getCountryId() {
        return countryId;
    }

    public void setCountryId(String countryId) {
        this.countryId = countryId;
    }

    public String getCountryName() {
        return countryName;
    }

    public void setCountryName(String countryName) {
        this.countryName = countryName;
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

    public String getGiftName() {
        return giftName;
    }

    public void setGiftName(String giftName) {
        this.giftName = giftName;
    }

    public String getGiftImage() {
        return giftImage;
    }

    public void setGiftImage(String giftImage) {
        this.giftImage = giftImage;
    }

    public String getGiftAmount() {
        return giftAmount;
    }

    public void setGiftAmount(String giftAmount) {
        this.giftAmount = giftAmount;
    }

    public String getDiscount() {
        return discount;
    }

    public void setDiscount(String discount) {
        this.discount = discount;
    }

    public String getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(String totalAmount) {
        this.totalAmount = totalAmount;
    }

    public String getGiftDescription() {
        return giftDescription;
    }

    public void setGiftDescription(String giftDescription) {
        this.giftDescription = giftDescription;
    }

    public String getGiftCategory() {
        return giftCategory;
    }

    public void setGiftCategory(String giftCategory) {
        this.giftCategory = giftCategory;
    }

    public String getGiftFor() {
        return giftFor;
    }

    public void setGiftFor(String giftFor) {
        this.giftFor = giftFor;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Integer getFav() {
        return fav;
    }

    public void setFav(Integer fav) {
        this.fav = fav;
    }

    public String getGiftCat() {
        return giftCat;
    }

    public void setGiftCat(String giftCat) {
        this.giftCat = giftCat;
    }

    public String getGiftForPeople() {
        return giftForPeople;
    }

    public void setGiftForPeople(String giftForPeople) {
        this.giftForPeople = giftForPeople;
    }
}
