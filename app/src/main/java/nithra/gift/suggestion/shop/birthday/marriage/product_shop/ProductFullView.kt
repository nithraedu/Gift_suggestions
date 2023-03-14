package nithra.gift.suggestion.shop.birthday.marriage.product_shop

import android.app.AlertDialog
import android.app.Dialog
import android.content.Intent
import android.graphics.Paint
import android.net.Uri
import android.os.Bundle
import android.view.View
import android.webkit.URLUtil
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.browser.customtabs.CustomTabsIntent
import androidx.cardview.widget.CardView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import nithra.gift.suggestion.shop.birthday.marriage.*
import nithra.gift.suggestion.shop.birthday.marriage.retrofit.DeleteGift
import nithra.gift.suggestion.shop.birthday.marriage.retrofit.GetGift
import nithra.gift.suggestion.shop.birthday.marriage.retrofit.RetrofitAPI
import nithra.gift.suggestion.shop.birthday.marriage.retrofit.RetrofitApiClient.retrofit
import nithra.gift.suggestion.shop.birthday.marriage.support.SharedPreference
import nithra.gift.suggestion.shop.birthday.marriage.support.Utils_Class
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
    var detailShopName: TextView? = null
    var detailAdd: TextView? = null
    var sharedPreference = SharedPreference()
    private var intent1: Intent? = null
    var extra: Bundle? = null
    private var idGift: String? = null
    private var profileEdit: ImageView? = null
    private var profileDelete: ImageView? = null
    var back: LinearLayout? = null
    var btShowmore: TextView? = null
    var btShowmore1: TextView? = null
    private var cardMail: CardView? = null
    private var cardWeb: CardView? = null
    private var phone: LinearLayout? = null
    var builder: AlertDialog.Builder? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
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
        detailShopName = findViewById(R.id.detail_shop_name)
        detailAdd = findViewById(R.id.detail_add)
        intent1 = intent
        extra = intent1!!.extras
        idGift = extra!!.getString("id")
        back = findViewById(R.id.back)
        profileEdit = findViewById(R.id.profile_edit)
        btShowmore = findViewById(R.id.btShowmore)
        btShowmore1 = findViewById(R.id.btShowmore1)
        cardMail = findViewById(R.id.card_mail)
        cardWeb = findViewById(R.id.card_web)
        phone = findViewById(R.id.phone)
        profileDelete = findViewById(R.id.profile_delete)
        builder = AlertDialog.Builder(this@ProductFullView)
        giftprize!!.paintFlags = giftprize!!.paintFlags or Paint.STRIKE_THRU_TEXT_FLAG
        btShowmore!!.setOnClickListener {
            if (btShowmore!!.text.toString().equals("Show more...", ignoreCase = true)) {
                description!!.maxLines = Int.MAX_VALUE //your TextView
                btShowmore!!.text = "Show less"
            } else {
                description!!.maxLines = 3 //your TextView
                btShowmore!!.text = "Show more..."
            }
        }
        btShowmore1!!.setOnClickListener {
            if (btShowmore1!!.text.toString().equals("Show more...", ignoreCase = true)) {
                detailAdd!!.maxLines = Int.MAX_VALUE //your TextView
                btShowmore1!!.text = "Show less"
            } else {
                detailAdd!!.maxLines = 3 //your TextView
                btShowmore1!!.text = "Show more..."
            }
        }
        back!!.setOnClickListener { finish() }
        profileDelete!!.setOnClickListener {
            builder!!.setMessage("Do you want to delete this Product?").setCancelable(false)
                .setPositiveButton("No") { dialog, _ -> dialog.cancel() }
                .setNegativeButton("Yes") { _, _ -> deleteGift() }
            val alert = builder!!.create()
            alert.show()
        }
        profileEdit!!.setOnClickListener {
            val i = Intent(applicationContext, ProductEdit::class.java)
            i.putExtra("id", idGift)
            startActivity(i)
        }
        phone!!.setOnClickListener {
            val dialog = Dialog(
                this@ProductFullView,
                android.R.style.Theme_DeviceDefault_Light_Dialog_NoActionBar_MinWidth
            )
            dialog.setContentView(R.layout.call_dialog)
            dialog.window!!.setBackgroundDrawableResource(android.R.color.transparent)
            dialog.setCanceledOnTouchOutside(false)
            val call1: TextView = dialog.findViewById(R.id.call1)
            val call2: TextView = dialog.findViewById(R.id.call2)
            val lay1: LinearLayout = dialog.findViewById(R.id.lay1)
            val lay2: LinearLayout = dialog.findViewById(R.id.lay2)
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
        }
        cardMail!!.setOnClickListener {
            if (gift!![0].shopEmail != null && gift!![0].shopEmail!!.trim { it <= ' ' }.isNotEmpty()) {
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
        }
        cardWeb!!.setOnClickListener {
            if (gift!![0].shopWebsite != null && gift!![0].shopWebsite!!.trim { it <= ' ' }.isNotEmpty()) {
                if (Utils_Class.isNetworkAvailable(this@ProductFullView)) {
                    val url = gift!![0].shopWebsite!!.trim { it <= ' ' }
                    if (URLUtil.isValidUrl(url)) {
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
        }
        IVPreviewImage!!.setOnClickListener {
            val currentString = gift!![0].giftImage
            val i = Intent(applicationContext, ImageSlide::class.java)
            i.putExtra("pos", currentString)
            startActivity(i)
        }
        Utils_Class.mProgress(this@ProductFullView)!!.show()
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
        map["id"] = idGift
        val retrofitAPI = retrofit!!.create(RetrofitAPI::class.java)
        val call = retrofitAPI.getgift(map)
        call.enqueue(object : Callback<ArrayList<GetGift>?> {
            override fun onResponse(
                call: Call<ArrayList<GetGift>?>, response: Response<ArrayList<GetGift>?>
            ) {
                if (response.isSuccessful) {
                    gift!!.clear()
                    gift!!.addAll(response.body()!!)
                    val currentString = gift!![0].giftImage
                    val separated =
                        currentString!!.split(",".toRegex()).dropLastWhile { it.isEmpty() }
                            .toTypedArray()
                    Glide.with(applicationContext).load(separated[0])
                        .error(R.drawable.ic_gift_default_img)
                        .placeholder(R.drawable.ic_gift_default_img)
                        .diskCacheStrategy(DiskCacheStrategy.ALL).into(IVPreviewImage!!)
                    giftname!!.text = gift!![0].giftName
                    giftcategory!!.text = gift!![0].giftCat!!.replace(",", ", ")
                    giftgender!!.text = gift!![0].giftForPeople!!.replace(",", ", ")
                    giftprize!!.text = "\u20B9 " + gift!![0].totalAmount
                    offerprize!!.text = "\u20B9 " + gift!![0].giftAmount
                    description!!.text = gift!![0].giftDescription
                    head!!.text = gift!![0].discount + "% offer"
                    detailShopName!!.text = gift!![0].shopName
                    detailAdd!!.text =
                        """${gift!![0].address}, ${gift!![0].city} - ${gift!![0].pincode}
${gift!![0].state}${gift!![0].countryName}"""
                    if (description!!.lineCount > 3) {
                        btShowmore!!.visibility = View.VISIBLE
                    } else {
                        btShowmore!!.visibility = View.GONE
                    }
                    if (detailAdd!!.lineCount > 3) {
                        btShowmore1!!.visibility = View.VISIBLE
                    } else {
                        btShowmore1!!.visibility = View.GONE
                    }
                    Utils_Class.mProgress!!.dismiss()
                }
            }

            override fun onFailure(call: Call<ArrayList<GetGift>?>, t: Throwable) {
            }
        })
    }

    private fun deleteGift() {
        val map = HashMap<String, String?>()
        map["action"] = "gift_delete"
        map["id"] = idGift
        map["user_id"] = sharedPreference.getString(this@ProductFullView, "user_id")
        val retrofitAPI = retrofit!!.create(RetrofitAPI::class.java)
        val call = retrofitAPI.delete_gift(map)
        call.enqueue(object : Callback<ArrayList<DeleteGift>> {
            override fun onResponse(
                call: Call<ArrayList<DeleteGift>>, response: Response<ArrayList<DeleteGift>>
            ) {
                if (response.isSuccessful) {
                    Utils_Class.toast_center(applicationContext, "Your product deleted...")
                    SellerProfileProductList.adapter!!.notifyDataSetChanged()
                }
            }

            override fun onFailure(call: Call<ArrayList<DeleteGift>>, t: Throwable) {
            }
        })
    }
}