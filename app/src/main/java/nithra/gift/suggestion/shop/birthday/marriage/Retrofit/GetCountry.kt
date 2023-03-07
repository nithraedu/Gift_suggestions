package nithra.gift.suggestion.shop.birthday.marriage.Retrofit;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class GetCountry {
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getIso() {
        return iso;
    }

    public void setIso(String iso) {
        this.iso = iso;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getNicename() {
        return nicename;
    }

    public void setNicename(String nicename) {
        this.nicename = nicename;
    }

    public String getIso3() {
        return iso3;
    }

    public void setIso3(String iso3) {
        this.iso3 = iso3;
    }

    public String getNumcode() {
        return numcode;
    }

    public void setNumcode(String numcode) {
        this.numcode = numcode;
    }

    public String getPhonecode() {
        return phonecode;
    }

    public void setPhonecode(String phonecode) {
        this.phonecode = phonecode;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    @SerializedName("id")
    @Expose
    public String id;
    @SerializedName("iso")
    @Expose
    public String iso;
    @SerializedName("name")
    @Expose
    public String name;
    @SerializedName("nicename")
    @Expose
    public String nicename;
    @SerializedName("iso3")
    @Expose
    public String iso3;
    @SerializedName("numcode")
    @Expose
    public String numcode;
    @SerializedName("phonecode")
    @Expose
    public String phonecode;
    @SerializedName("status")
    @Expose
    public String status;
}
