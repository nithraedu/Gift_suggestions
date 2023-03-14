package nithra.gift.suggestion.shop.birthday.marriage.retrofit

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class AddSeller {
    @SerializedName("id")
    @Expose
    var id: String? = null

    @SerializedName("name")
    @Expose
    var name: String? = null

    @SerializedName("gmail")
    @Expose
    var gmail: String? = null

    @SerializedName("shop_name")
    @Expose
    var shopName: String? = null

    @SerializedName("seller_mobile")
    @Expose
    var sellerMobile: String? = null

    @SerializedName("state")
    @Expose
    var state: String? = null

    @SerializedName("address")
    @Expose
    var address: String? = null

    @SerializedName("pincode")
    @Expose
    var pincode: String? = null

    @SerializedName("latitude")
    @Expose
    var latitude: String? = null

    @SerializedName("logo")
    @Expose
    var logo: String? = null

    @SerializedName("longitude")
    @Expose
    var longitude: String? = null

    @SerializedName("district")
    @Expose
    var district: String? = null

    @SerializedName("city")
    @Expose
    var city: String? = null

    @SerializedName("status")
    @Expose
    var status: String? = null
}