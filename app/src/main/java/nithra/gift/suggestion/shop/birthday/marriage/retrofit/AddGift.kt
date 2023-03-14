package nithra.gift.suggestion.shop.birthday.marriage.retrofit

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class AddGift {
    @SerializedName("id")
    @Expose
    var id: String? = null

    @SerializedName("user_id")
    @Expose
    var userId: String? = null

    @SerializedName("gift_category")
    @Expose
    var giftCategory: String? = null

    @SerializedName("gift_for")
    @Expose
    var giftFor: String? = null

    @SerializedName("gift_name")
    @Expose
    var giftName: String? = null

    @SerializedName("gift_image")
    @Expose
    var giftImage: String? = null

    @SerializedName("gift_amount")
    @Expose
    var giftAmount: String? = null

    @SerializedName("discount")
    @Expose
    var discount: String? = null

    @SerializedName("total_amount")
    @Expose
    var totalAmount: String? = null

    @SerializedName("gift_description")
    @Expose
    var giftDescription: String? = null

    @SerializedName("gift_status")
    @Expose
    var giftStatus: String? = null

    @SerializedName("status")
    @Expose
    var status: String? = null
}