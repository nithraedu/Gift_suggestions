package nithra.gift.suggestion.shop.birthday.marriage.retrofit

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class Fav_view {
    @SerializedName("id")
    @Expose
    var id: String? = null

    @SerializedName("gift_id")
    @Expose
    var giftId: String? = null

    @SerializedName("user_id")
    @Expose
    var userId: String? = null

    @SerializedName("name")
    @Expose
    var name: String? = null

    @SerializedName("shop_name")
    @Expose
    var shopName: String? = null

    @SerializedName("seller_mobile")
    @Expose
    var sellerMobile: String? = null

    @SerializedName("state")
    @Expose
    var state: String? = null

    @SerializedName("district")
    @Expose
    var district: String? = null

    @SerializedName("country")
    @Expose
    var country: String? = null

    @SerializedName("city")
    @Expose
    var city: String? = null

    @SerializedName("address")
    @Expose
    var address: String? = null

    @SerializedName("pincode")
    @Expose
    var pincode: String? = null

    @SerializedName("latitude")
    @Expose
    var latitude: String? = null

    @SerializedName("longitude")
    @Expose
    var longitude: String? = null

    @SerializedName("logo")
    @Expose
    var logo: String? = null

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

    @SerializedName("gift_category")
    @Expose
    var giftCategory: String? = null

    @SerializedName("gift_for")
    @Expose
    var giftFor: String? = null

    @SerializedName("status")
    @Expose
    var status: String? = null

    @SerializedName("fav")
    @Expose
    var fav: Int? = null
}