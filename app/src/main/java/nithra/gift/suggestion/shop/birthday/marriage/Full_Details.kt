package nithra.gift.suggestion.shop.birthday.marriage

import android.app.Dialog
import android.content.Intent
import android.graphics.Paint
import android.net.Uri
import android.os.Bundle
import android.view.View
import android.view.Window
import android.view.WindowManager
import android.webkit.URLUtil
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.browser.customtabs.CustomTabsIntent
import androidx.cardview.widget.CardView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import nithra.gift.suggestion.shop.birthday.marriage.fragment.Sellerproducts
import nithra.gift.suggestion.shop.birthday.marriage.retrofit.*
import nithra.gift.suggestion.shop.birthday.marriage.support.SharedPreference
import nithra.gift.suggestion.shop.birthday.marriage.support.Utils_Class
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class Full_Details : AppCompatActivity() {
    var gift_show: ArrayList<Gift_Cat>? = null
    var intent1: Intent? = null
    var extra: Bundle? = null
    var id_gift: String? = null
    var IVPreviewImage: ImageView? = null
    var fav: ImageView? = null
    var back: LinearLayout? = null
    var giftname: TextView? = null
    var giftprize: TextView? = null
    var offerprize: TextView? = null
    var description: TextView? = null
    var detail_shop_name: TextView? = null
    var detail_add: TextView? = null
    var head: TextView? = null
    var phone: LinearLayout? = null
    var card_mail: CardView? = null
    var card_web: CardView? = null
    var sharedPreference = SharedPreference()
    var fav_show: ArrayList<Fav_view>? = null
    var pullToRefresh: SwipeRefreshLayout? = null
    var pos_id = 0
    var btShowmore: TextView? = null
    var btShowmore1: TextView? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requestWindowFeature(Window.FEATURE_NO_TITLE)
        this.window.setFlags(
            WindowManager.LayoutParams.FLAG_FULLSCREEN,
            WindowManager.LayoutParams.FLAG_FULLSCREEN
        )
        setContentView(R.layout.buyer_view)
        gift_show = ArrayList()
        back = findViewById(R.id.back)
        giftname = findViewById(R.id.giftname)
        giftname = findViewById(R.id.giftname)
        giftprize = findViewById(R.id.giftprize)
        offerprize = findViewById(R.id.offerprize)
        description = findViewById(R.id.description)
        detail_shop_name = findViewById(R.id.detail_shop_name)
        detail_add = findViewById(R.id.detail_add)
        IVPreviewImage = findViewById(R.id.IVPreviewImage)
        phone = findViewById(R.id.phone)
        card_mail = findViewById(R.id.card_mail)
        card_web = findViewById(R.id.card_web)
        head = findViewById(R.id.head)
        fav = findViewById(R.id.fav)
        btShowmore = findViewById(R.id.btShowmore)
        btShowmore1 = findViewById(R.id.btShowmore1)
        fav_show = ArrayList()
        intent1 = getIntent()
        extra = intent1!!.getExtras()
        id_gift = extra!!.getString("full_view")
        pos_id = extra!!.getInt("position")
        giftprize!!.setPaintFlags(giftprize!!.getPaintFlags() or Paint.STRIKE_THRU_TEXT_FLAG)
        Utils_Class.mProgress(this@Full_Details, "Loading please wait...", false)!!.show()
        btShowmore!!.setOnClickListener({
            if (btShowmore!!.getText().toString().equals("Show more...", ignoreCase = true)) {
                description!!.setMaxLines(Int.MAX_VALUE)
                btShowmore!!.setText("Show less")
            } else {
                description!!.setMaxLines(3)
                btShowmore!!.setText("Show more...")
            }
        })
        btShowmore1!!.setOnClickListener({
            if (btShowmore1!!.getText().toString().equals("Show more...", ignoreCase = true)) {
                detail_add!!.setMaxLines(Int.MAX_VALUE)
                btShowmore1!!.setText("Show less")
            } else {
                detail_add!!.setMaxLines(3)
                btShowmore1!!.setText("Show more...")
            }
        })
        back!!.setOnClickListener({ finish() })
        get_cat()
        fav!!.setOnClickListener({ fav1() })
        phone!!.setOnClickListener({
            val dialog: Dialog
            dialog = Dialog(
                this@Full_Details,
                android.R.style.Theme_DeviceDefault_Light_Dialog_NoActionBar_MinWidth
            )
            dialog.setContentView(R.layout.call_dialog)
            dialog.window!!.setBackgroundDrawableResource(android.R.color.transparent)
            dialog.setCanceledOnTouchOutside(false)
            val call1: TextView
            val call2: TextView
            val lay1: LinearLayout
            val lay2: LinearLayout
            call1 = dialog.findViewById(R.id.call1)
            call2 = dialog.findViewById(R.id.call2)
            lay1 = dialog.findViewById(R.id.lay1)
            lay2 = dialog.findViewById(R.id.lay2)
            dialog.show()
            call1.text = gift_show!![0].sellerMobile!!.trim { it <= ' ' }
            call2.text = gift_show!![0].sellerMobile2!!.trim { it <= ' ' }
            val phone = gift_show!![0].sellerMobile!!.trim { it <= ' ' }
            val phone1 = gift_show!![0].sellerMobile2!!.trim { it <= ' ' }
            if (gift_show!![0].sellerMobile2!!.isEmpty()) {
                dialog.dismiss()
                //                    lay2.setVisibility(View.GONE);
                val intent = Intent(Intent.ACTION_DIAL, Uri.fromParts("tel", phone, null))
                startActivity(intent)
            }
            lay1.setOnClickListener {
                val intent = Intent(Intent.ACTION_DIAL, Uri.fromParts("tel", phone, null))
                startActivity(intent)
                dialog.dismiss()
            }
            lay2.setOnClickListener {
                val intent = Intent(Intent.ACTION_DIAL, Uri.fromParts("tel", phone1, null))
                startActivity(intent)
                dialog.dismiss()
            }
        })
        IVPreviewImage!!.setOnClickListener({
            val currentString = gift_show!![0].giftImage
            val i = Intent(applicationContext, ImageSlide::class.java)
            i.putExtra("pos", currentString)
            startActivity(i)

        })
        card_mail!!.setOnClickListener({
            if (gift_show!![0].shopEmail != null && !gift_show!![0].shopEmail
                !!.trim { it <= ' ' }
                    .isEmpty()
            ) {
                if (Utils_Class.isNetworkAvailable(this@Full_Details)) {
                    val intent = Intent(Intent.ACTION_SENDTO)
                    intent.data = Uri.parse("mailto:") // only email apps should handle this
                    intent.putExtra(
                        Intent.EXTRA_EMAIL,
                        arrayOf(gift_show!![0].shopEmail!!.trim { it <= ' ' })
                    )
                    intent.putExtra(Intent.EXTRA_SUBJECT, "Gift Suggestions")
                    intent.putExtra(Intent.EXTRA_TEXT, "Body Here")
                    if (intent.resolveActivity(packageManager) != null) {
                        startActivity(intent)
                    }
                } else {
                    Utils_Class.toast_center(this@Full_Details, "Check Your Internet Connection...")
                }
            } else {
                Utils_Class.toast_center(this@Full_Details, "Email not available...")
            }
        })
        card_web!!.setOnClickListener({
            if (gift_show!![0].shopWebsite != null && !gift_show!![0].shopWebsite
                !!.trim { it <= ' ' }
                    .isEmpty()
            ) {
                if (Utils_Class.isNetworkAvailable(this@Full_Details)) {
                    val url = gift_show!![0].shopWebsite!!.trim { it <= ' ' }
                    if (URLUtil.isValidUrl(url)) {
                        val builder = CustomTabsIntent.Builder()
                        val customTabsIntent = builder.build()
                        customTabsIntent.launchUrl(this@Full_Details, Uri.parse(url))
                    } else {
                        Utils_Class.toast_center(this@Full_Details, "URL not valid...")
                    }
                } else {
                    Utils_Class.toast_center(this@Full_Details, "Check Your Internet Connection...")
                }
            } else {
                Utils_Class.toast_center(this@Full_Details, "Website not available...")
            }
        })
    }

    fun get_cat() {
        val map = HashMap<String, String?>()
        map["action"] = "get_cat"
        map["id"] = id_gift
        map["user_id"] = sharedPreference.getString(applicationContext, "android_userid")
        val retrofitAPI = RetrofitApiClient.retrofit!!.create(
            RetrofitAPI::class.java
        )
        val call = retrofitAPI.gift_cat(map)
        call.enqueue(object : Callback<ArrayList<Gift_Cat>> {
            override fun onResponse(
                call: Call<ArrayList<Gift_Cat>>,
                response: Response<ArrayList<Gift_Cat>>
            ) {
                if (response.isSuccessful) {
                    if (response.body()!![0].status == "Success") {
                        gift_show!!.clear()
                        gift_show!!.addAll(response.body()!!)
                        val currentString = gift_show!![0].giftImage
                        val separated =
                            currentString!!.split(",".toRegex()).dropLastWhile { it.isEmpty() }
                                .toTypedArray()
                        Glide.with(applicationContext).load(separated[0])
                            .error(R.drawable.ic_gift_default_img)
                            .placeholder(R.drawable.ic_gift_default_img)
                            .diskCacheStrategy(DiskCacheStrategy.ALL)
                            .into(IVPreviewImage!!)
                        giftname!!.text = gift_show!![0].giftName
                        giftprize!!.text = "\u20B9 " + gift_show!![0].totalAmount
                        offerprize!!.text = "\u20B9 " + gift_show!![0].giftAmount
                        description!!.text = gift_show!![0].giftDescription
                        detail_shop_name!!.text = gift_show!![0].shopName
                        detail_add!!.text =
                            """${gift_show!![0].address}, ${gift_show!![0].city} - ${gift_show!![0].pincode}
${gift_show!![0].state}${gift_show!![0].country}"""
                        head!!.text =
                            gift_show!![0].discount + "% offer"
                        if (gift_show!![0].fav == 1) {
                            fav!!.setBackgroundResource(R.drawable.favorite_red)
                        } else {
                            fav!!.setBackgroundResource(R.drawable.favorite_grey)
                        }
                        if (description!!.lineCount > 3) {
                            btShowmore!!.visibility = View.VISIBLE
                        } else {
                            btShowmore!!.visibility = View.GONE
                        }
                        if (detail_add!!.lineCount > 3) {
                            btShowmore1!!.visibility = View.VISIBLE
                        } else {
                            btShowmore1!!.visibility = View.GONE
                        }
                    }
                    Utils_Class.mProgress!!.dismiss()
                }
            }

            override fun onFailure(call: Call<ArrayList<Gift_Cat>>, t: Throwable) {
            }
        })
    }

    fun fav1() {
        val map = HashMap<String, String?>()
        map["action"] = "favourite"
        map["gift_id"] = id_gift
        map["user_id"] = sharedPreference.getString(this@Full_Details, "android_userid")
        val retrofitAPI = RetrofitApiClient.retrofit!!.create(
            RetrofitAPI::class.java
        )
        val call = retrofitAPI.fav_add_del(map)
        call.enqueue(object : Callback<ArrayList<Fav_Add_Del>> {
            override fun onResponse(
                call: Call<ArrayList<Fav_Add_Del>>,
                response: Response<ArrayList<Fav_Add_Del>>
            ) {
                if (response.isSuccessful) {
                    if (response.body()!![0].status == "Success") {
                        if (response.body()!![0].fvAction == 1) {
                            gift_show!![0].fav = 1
                            Sellerproducts.gift_show!![pos_id].fav = 1
                            fav!!.setBackgroundResource(R.drawable.favorite_red)
                            Utils_Class.toast_center(
                                this@Full_Details,
                                "Your gift added to favourite..."
                            )
                        } else {
                            gift_show!![0].fav = 0
                            Sellerproducts.gift_show!![pos_id].fav = 0
                            fav!!.setBackgroundResource(R.drawable.favorite_grey)
                            Utils_Class.toast_center(
                                this@Full_Details,
                                "Your gift removed from favourite..."
                            )
                        }
                        Sellerproducts.adapter!!.notifyDataSetChanged()
                    }
                }
            }

            override fun onFailure(call: Call<ArrayList<Fav_Add_Del>>, t: Throwable) {
            }
        })
    }
}