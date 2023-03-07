package nithra.gift.suggestion.shop.birthday.marriage.Retrofit;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class DeleteGift {

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    @SerializedName("status")
    @Expose
    public String status;
}
