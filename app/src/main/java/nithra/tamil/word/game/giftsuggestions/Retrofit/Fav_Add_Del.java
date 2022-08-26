package nithra.tamil.word.game.giftsuggestions.Retrofit;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Fav_Add_Del {
    @SerializedName("status")
    @Expose
    public String status;
    @SerializedName("fv_action")
    @Expose
    public Integer fvAction;
    @SerializedName("msg")
    @Expose
    public String msg;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Integer getFvAction() {
        return fvAction;
    }

    public void setFvAction(Integer fvAction) {
        this.fvAction = fvAction;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }
}
