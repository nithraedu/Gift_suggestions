package nithra.gift.suggestion.shop.birthday.marriage.Retrofit;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Androidid {

    @SerializedName("status")
    @Expose
    public String status;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    @SerializedName("user_id")
    @Expose
    public String userId;
}
