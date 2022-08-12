package nithra.tamil.word.game.giftsuggestions.Retrofit;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Occasion {

    @SerializedName("id")
    @Expose
    public String id;
    @SerializedName("category")
    @Expose
    public String category;
    @SerializedName("category_logo")
    @Expose
    public String categoryLogo;
    @SerializedName("status")
    @Expose
    public String status;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getCategoryLogo() {
        return categoryLogo;
    }

    public void setCategoryLogo(String categoryLogo) {
        this.categoryLogo = categoryLogo;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
