package nithra.gift.suggestion.shop.birthday.marriage

import android.app.AlertDialog
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
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.google.gson.Gson
import nithra.gift.suggestion.shop.birthday.marriage.ProductEdit
import nithra.gift.suggestion.shop.birthday.marriage.Retrofit.DeleteGift
import nithra.gift.suggestion.shop.birthday.marriage.Retrofit.GetGift
import nithra.gift.suggestion.shop.birthday.marriage.Retrofit.RetrofitAPI
import nithra.gift.suggestion.shop.birthday.marriage.Retrofit.RetrofitApiClient.retrofit
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ProductFullView : AppCompatActivity() {
    var gift: ArrayList<GetGift>? = null
    var IVPreviewImage: ImageView? = null
    var giftname: TextView? = null
    var giftcategory: TextView? = null
    var giftgender: TextView? = null
    var giftprize: TextView? = null
    var offerprize: TextView? = null
    var description: TextView? = null
    var head: TextView? = null
    var detail_shop_name: TextView? = null
    var detail_add: TextView? = null
    var sharedPreference = SharedPreference()
    var intent1: Intent? = null
    var extra: Bundle? = null
    var id_gift: String? = null
    var profile_edit: ImageView? = null
    var profile_delete: ImageView? = null
    var back: LinearLayout? = null
    var btShowmore: TextView? = null
    var btShowmore1: TextView? = null
    var card_mail: CardView? = null
    var card_web: CardView? = null
    var phone: LinearLayout? = null
    var builder: AlertDialog.Builder? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requestWindowFeature(Window.FEATURE_NO_TITLE)
        this.window.setFlags(
            WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN
        )
        setContentView(R.layout.seller_view)
        gift = ArrayList()
        IVPreviewImage = findViewById(R.id.IVPreviewImage)
        giftname = findViewById(R.id.giftname)
        giftcategory = findViewById(R.id.giftcategory)
        giftgender = findViewById(R.id.giftgender)
        giftprize = findViewById(R.id.giftprize)
        offerprize = findViewById(R.id.offerprize)
        description = findViewById(R.id.description)
        head = findViewById(R.id.head)
        detail_shop_name = findViewById(R.id.detail_shop_name)
        detail_add = findViewById(R.id.detail_add)
        intent1 = getIntent()
        extra = intent1!!.getExtras()
        id_gift = extra!!.getString("id")
        back = findViewById(R.id.back)
        profile_edit = findViewById(R.id.profile_edit)
        btShowmore = findViewById(R.id.btShowmore)
        btShowmore1 = findViewById(R.id.btShowmore1)
        card_mail = findViewById(R.id.card_mail)
        card_web = findViewById(R.id.card_web)
        phone = findViewById(R.id.phone)
        profile_delete = findViewById(R.id.profile_delete)
        builder = AlertDialog.Builder(this@ProductFullView)
        giftprize!!.setPaintFlags(giftprize!!.getPaintFlags() or Paint.STRIKE_THRU_TEXT_FLAG)
        btShowmore!!.setOnClickListener(View.OnClickListener {
            if (btShowmore!!.getText().toString().equals("Show more...", ignoreCase = true)) {
                description!!.setMaxLines(Int.MAX_VALUE) //your TextView
                btShowmore!!.setText("Show less")
            } else {
                description!!.setMaxLines(3) //your TextView
                btShowmore!!.setText("Show more...")
            }
        })
        btShowmore1!!.setOnClickListener(View.OnClickListener {
            if (btShowmore1!!.getText().toString().equals("Show more...", ignoreCase = true)) {
                detail_add!!.setMaxLines(Int.MAX_VALUE) //your TextView
                btShowmore1!!.setText("Show less")
            } else {
                detail_add!!.setMaxLines(3) //your TextView
                btShowmore1!!.setText("Show more...")
            }
        })
        back!!.setOnClickListener(View.OnClickListener { finish() })
        profile_delete!!.setOnClickListener(View.OnClickListener {
            builder!!.setMessage("Do you want to delete this Product?").setCancelable(false)
                .setPositiveButton("No") { dialog, id -> dialog.cancel() }
                .setNegativeButton("Yes") { dialog, id -> delete_gift() }
            val alert = builder!!.create()
            alert.show()
        })
        profile_edit!!.setOnClickListener(View.OnClickListener {
            val i = Intent(applicationContext, ProductEdit::class.java)
            i.putExtra("id", id_gift)
            startActivity(i)
        })
        phone!!.setOnClickListener(View.OnClickListener {
            val dialog: Dialog
            dialog = Dialog(
                this@ProductFullView,
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
            call1.text = gift!![0].sellerMobile!!.trim { it <= ' ' }
            call2.text = gift!![0].sellerMobile2!!.trim { it <= ' ' }
            val phone = gift!![0].sellerMobile!!.trim { it <= ' ' }
            val phone1 = gift!![0].sellerMobile2!!.trim { it <= ' ' }
            if (gift!![0].sellerMobile2!!.isEmpty()) {
                dialog.dismiss()
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
        card_mail!!.setOnClickListener(View.OnClickListener {
            if (gift!![0].shopEmail != null && !gift!![0].shopEmail!!.trim { it <= ' ' }
                    .isEmpty()) {
                if (Utils_Class.isNetworkAvailable(this@ProductFullView)) {
                    val intent = Intent(Intent.ACTION_SENDTO)
                    intent.data = Uri.parse("mailto:") // only email apps should handle this
                    intent.putExtra(
                        Intent.EXTRA_EMAIL, arrayOf(gift!![0].shopEmail!!.trim { it <= ' ' })
                    )
                    intent.putExtra(Intent.EXTRA_SUBJECT, "Gift Suggestions")
                    intent.putExtra(Intent.EXTRA_TEXT, "Body Here")
                    if (intent.resolveActivity(packageManager) != null) {
                        startActivity(intent)
                    }
                } else {
                    Utils_Class.toast_center(
                        this@ProductFullView, "Check Your Internet Connection..."
                    )
                }
            } else {
                Utils_Class.toast_center(this@ProductFullView, "Email not available...")
            }
        })
        card_web!!.setOnClickListener(View.OnClickListener {
            if (gift!![0].shopWebsite != null && !gift!![0].shopWebsite!!.trim { it <= ' ' }
                    .isEmpty()) {
                if (Utils_Class.isNetworkAvailable(this@ProductFullView)) {
                    val url = gift!![0].shopWebsite!!.trim { it <= ' ' }
                    if (URLUtil.isValidUrl(url)) {
                        println("urlprint$url")
                        val builder = CustomTabsIntent.Builder()
                        val customTabsIntent = builder.build()
                        customTabsIntent.launchUrl(this@ProductFullView, Uri.parse(url))
                    } else {
                        Utils_Class.toast_center(this@ProductFullView, "URL not valid...")
                    }
                } else {
                    Utils_Class.toast_center(
                        this@ProductFullView, "Check Your Internet Connection..."
                    )
                }
            } else {
                Utils_Class.toast_center(this@ProductFullView, "Website not available...")
            }
        })
        IVPreviewImage!!.setOnClickListener(View.OnClickListener {
            val currentString = gift!![0].giftImage
            val separated =
                currentString!!.split(",".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
            println("print_url1== $currentString")
            val i = Intent(applicationContext, ImageSlide::class.java)
            i.putExtra("pos", currentString)
            startActivity(i)
        })
        Utils_Class.mProgress(this@ProductFullView, "Loading please wait...", false)!!.show()
        category()
    }

    override fun onResume() {
        super.onResume()
        if (sharedPreference.getInt(applicationContext, "finish_product") == 1) {
            category()
            sharedPreference.putInt(this@ProductFullView, "finish_product", 0)
        }
    }

    fun category() {
        val map = HashMap<String, String?>()
        map["action"] = "get_gift"
        map["id"] = id_gift
        println("print map : $map")
        val retrofitAPI = retrofit!!.create(RetrofitAPI::class.java)
        val call = retrofitAPI.getgift(map)
        call.enqueue(object : Callback<ArrayList<GetGift>?> {
            override fun onResponse(
                call: Call<ArrayList<GetGift>?>, response: Response<ArrayList<GetGift>?>
            ) {
                if (response.isSuccessful) {
                    val result = Gson().toJson(response.body())
                    println("======response result:$result")
                    gift!!.clear()
                    gift!!.addAll(response.body()!!)
                    val currentString = gift!![0].giftImage
                    val separated =
                        currentString!!.split(",".toRegex()).dropLastWhile { it.isEmpty() }
                            .toTypedArray()
                    println("print_comma== $separated")
                    Glide.with(applicationContext).load(separated[0])
                        .error(R.drawable.ic_gift_default_img)
                        .placeholder(R.drawable.ic_gift_default_img)
                        .diskCacheStrategy(DiskCacheStrategy.ALL).into(IVPreviewImage!!)
                    giftname!!.text = gift!![0].giftName
                    giftcategory!!.text = gift!![0].giftCat!!.replace(",", ", ")
                    giftgender!!.text = gift!![0].giftForPeople!!.replace(",", ", ")
                    giftprize!!.text = "\u20B9 " + gift!![0].totalAmount
                    //                    offerpercen.setText(gift.get(0).getDiscount());
                    offerprize!!.text = "\u20B9 " + gift!![0].giftAmount
                    description!!.text = gift!![0].giftDescription
                    head!!.text = gift!![0].discount + "% offer"
                    detail_shop_name!!.text = gift!![0].shopName
                    detail_add!!.text =
                        """${gift!![0].address}, ${gift!![0].city} - ${gift!![0].pincode}
${gift!![0].state}${gift!![0].countryName}"""
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
                    Utils_Class.mProgress!!.dismiss()
                }
                println("======response :$response")
            }

            override fun onFailure(call: Call<ArrayList<GetGift>?>, t: Throwable) {
                println("======response t:$t")
            }
        })
    }

    fun delete_gift() {
        val map = HashMap<String, String?>()
        map["action"] = "gift_delete"
        map["id"] = id_gift
        map["user_id"] = sharedPreference.getString(this@ProductFullView, "user_id")
        println("print map : $map")
        val retrofitAPI = retrofit!!.create(RetrofitAPI::class.java)
        val call = retrofitAPI.delete_gift(map)
        call.enqueue(object : Callback<ArrayList<DeleteGift>> {
            override fun onResponse(
                call: Call<ArrayList<DeleteGift>>, response: Response<ArrayList<DeleteGift>>
            ) {
                if (response.isSuccessful) {
                    val result = Gson().toJson(response.body())
                    println("======response result:$result")
                    Utils_Class.toast_center(applicationContext, "Your product deleted...")
                    SellerProfileProductList.adapter!!.notifyDataSetChanged()
                }
                println("======response :$response")
            }

            override fun onFailure(call: Call<ArrayList<DeleteGift>>, t: Throwable) {
                println("======response t:$t")
            }
        })
    }
}