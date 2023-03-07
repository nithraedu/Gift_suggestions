package nithra.gift.suggestion.shop.birthday.marriage.Retrofit

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class GiftEdit {
    @SerializedName("total_gifts")
    @Expose
    var totalGifts: String? = null

    @SerializedName("id")
    @Expose
    var id: String? = null

    @SerializedName("name")
    @Expose
    var name: String? = null

    @SerializedName("seller_mobile")
    @Expose
    var sellerMobile: String? = null

    @SerializedName("seller_mobile2")
    @Expose
    var sellerMobile2: String? = null

    @SerializedName("shop_name")
    @Expose
    var shopName: String? = null

    @SerializedName("address")
    @Expose
    var address: String? = null

    @SerializedName("logo")
    @Expose
    var logo: String? = null

    @SerializedName("state")
    @Expose
    var state: String? = null

    @SerializedName("city")
    @Expose
    var city: String? = null

    @SerializedName("district")
    @Expose
    var district: String? = null

    @SerializedName("latitude")
    @Expose
    var latitude: String? = null

    @SerializedName("shop_website")
    @Expose
    var shopWebsite: String? = null

    @SerializedName("shop_email")
    @Expose
    var shopEmail: String? = null

    @SerializedName("longitude")
    @Expose
    var longitude: String? = null

    @SerializedName("pincode")
    @Expose
    var pincode: String? = null

    @SerializedName("country")
    @Expose
    var country: String? = null

    @SerializedName("country_name")
    @Expose
    var country_name: String? = null

    @SerializedName("status")
    @Expose
    var status: String? = null
}