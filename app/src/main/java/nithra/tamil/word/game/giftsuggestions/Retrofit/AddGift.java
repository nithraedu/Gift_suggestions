package nithra.tamil.word.game.giftsuggestions.Retrofit;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class AddGift {
    @SerializedName("id")
    @Expose
    public String id;
    @SerializedName("user_id")
    @Expose
    public String userId;
    @SerializedName("gift_category")
    @Expose
    public String giftCategory;
    @SerializedName("gift_for")
    @Expose
    public String giftFor;
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
    @SerializedName("gift_status")
    @Expose
    public String giftStatus;
    @SerializedName("status")
    @Expose
    public String status;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
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

    public String getGiftStatus() {
        return giftStatus;
    }

    public void setGiftStatus(String giftStatus) {
        this.giftStatus = giftStatus;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
