package nithra.gift.suggestion.shop.birthday.marriage

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.view.Window
import android.view.WindowManager
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.google.gson.Gson
import nithra.gift.suggestion.shop.birthday.marriage.Retrofit.RetrofitAPI
import nithra.gift.suggestion.shop.birthday.marriage.Retrofit.RetrofitApiClient.retrofit
import nithra.gift.suggestion.shop.birthday.marriage.Retrofit.SellerProfilePojo
import nithra.gift.suggestion.shop.birthday.marriage.Utils_Class.android_id
import nithra.gift.suggestion.shop.birthday.marriage.Utils_Class.mProgress
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class SellerProfile : AppCompatActivity() {
    var gift: ArrayList<SellerProfilePojo>? = null
    var IVPreviewImage: ImageView? = null
    var seller_name: TextView? = null
    var shop_name: TextView? = null
    var mobile: TextView? = null
    var address: TextView? = null
    var pincode: TextView? = null
    var state: TextView? = null
    var district: TextView? = null
    var city: TextView? = null
    var country: TextView? = null
    var latitude: TextView? = null
    var longitude: TextView? = null
    var total_gifts: TextView? = null
    var mail: TextView? = null
    var web: TextView? = null
    var sharedPreference = SharedPreference()
    var back: ImageView? = null
    var profile_edit: ImageView? = null
    var web_gone: LinearLayout? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requestWindowFeature(Window.FEATURE_NO_TITLE)
        this.window.setFlags(
            WindowManager.LayoutParams.FLAG_FULLSCREEN,
            WindowManager.LayoutParams.FLAG_FULLSCREEN
        )
        setContentView(R.layout.activity_seller_profile)
        val pullToRefresh = findViewById<SwipeRefreshLayout>(R.id.pullToRefresh)
        gift = ArrayList()
        IVPreviewImage = findViewById(R.id.IVPreviewImage)
        seller_name = findViewById(R.id.seller_name)
        shop_name = findViewById(R.id.shop_name)
        mobile = findViewById(R.id.mobile)
        mail = findViewById(R.id.mail)
        web = findViewById(R.id.web)
        address = findViewById(R.id.address)
        pincode = findViewById(R.id.pincode)
        state = findViewById(R.id.state)
        district = findViewById(R.id.district)
        IVPreviewImage = findViewById(R.id.IVPreviewImage)
        profile_edit = findViewById(R.id.profile_edit)
        country = findViewById(R.id.country)
        city = findViewById(R.id.city)
        back = findViewById(R.id.back)
        longitude = findViewById(R.id.longitude)
        latitude = findViewById(R.id.latitude)
        total_gifts = findViewById(R.id.total_gifts)
        web_gone = findViewById(R.id.web_gone)
        val android_id = findViewById<TextView>(R.id.android_id)
        android_id.text = android_id(this)
        back!!.setOnClickListener(View.OnClickListener { finish() })
        profile_edit!!.setOnClickListener(View.OnClickListener {
            val i = Intent(applicationContext, ShopEdit::class.java)
            startActivity(i)
        })
        mProgress(this, "Loading please wait...", false)!!.show()
        category()
        pullToRefresh.setOnRefreshListener {
            gift!!.clear()
            category()
            pullToRefresh.isRefreshing = false
        }
    }

    override fun onResume() {
        super.onResume()
        if (sharedPreference.getInt(applicationContext, "finish") == 1) {
            category()
            sharedPreference.putInt(this@SellerProfile, "finish", 0)
        }
    }

    fun category() {
        val map = HashMap<String, String?>()
        map["action"] = "get_id"
        map["id"] = sharedPreference.getString(this, "user_id")
        val retrofitAPI = retrofit!!.create(RetrofitAPI::class.java)
        val call = retrofitAPI.profile(map)
        call.enqueue(object : Callback<ArrayList<SellerProfilePojo>?> {
            override fun onResponse(
                call: Call<ArrayList<SellerProfilePojo>?>,
                response: Response<ArrayList<SellerProfilePojo>?>
            ) {
                if (response.isSuccessful) {
                    val result = Gson().toJson(response.body())
                    println("======response result:$result")
                    gift!!.clear()
                    gift!!.addAll(response.body()!!)
                    Glide.with(applicationContext).load(gift!![0].logo)
                        .error(R.drawable.ic_gift_default_img)
                        .placeholder(R.drawable.ic_gift_default_img)
                        .diskCacheStrategy(DiskCacheStrategy.ALL)
                        .into(IVPreviewImage!!)
                    seller_name!!.text = gift!![0].name
                    shop_name!!.text = gift!![0].shopName
                    mobile!!.text = gift!![0].sellerMobile
                    mail!!.text = gift!![0].shopEmail
                    web!!.text = gift!![0].shopWebsite
                    address!!.text = gift!![0].address
                    pincode!!.text = gift!![0].pincode
                    state!!.text = gift!![0].state
                    district!!.text = gift!![0].district
                    city!!.text = gift!![0].city
                    country!!.tag = gift!![0].country
                    latitude!!.text = gift!![0].latitude
                    longitude!!.text = gift!![0].longitude
                    total_gifts!!.text = "My total gifts : " + gift!![0].totalGifts
                    mProgress!!.dismiss()
                    if (gift!![0].shopWebsite == "") {
                        web_gone!!.visibility = View.GONE
                    }
                }
                println("======response :$response")
            }

            override fun onFailure(call: Call<ArrayList<SellerProfilePojo>?>, t: Throwable) {
                println("======response t:$t")
            }
        })
    }
}