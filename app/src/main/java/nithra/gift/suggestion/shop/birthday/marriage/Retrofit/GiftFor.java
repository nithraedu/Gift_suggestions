package nithra.gift.suggestion.shop.birthday.marriage.Retrofit;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class GiftFor {
    @SerializedName("id")
    @Expose
    public String id;
    @SerializedName("people")
    @Expose
    public String people;
    @SerializedName("people_logo")
    @Expose
    public String peopleLogo;
    @SerializedName("status")
    @Expose
    public String status;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getPeople() {
        return people;
    }

    public void setPeople(String people) {
        this.people = people;
    }

    public String getPeopleLogo() {
        return peopleLogo;
    }

    public void setPeopleLogo(String peopleLogo) {
        this.peopleLogo = peopleLogo;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
