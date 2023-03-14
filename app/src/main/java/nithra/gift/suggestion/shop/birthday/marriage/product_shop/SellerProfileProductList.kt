package nithra.gift.suggestion.shop.birthday.marriage.product_shop

import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.*
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import nithra.gift.suggestion.shop.birthday.marriage.ImageSlide
import nithra.gift.suggestion.shop.birthday.marriage.MainActivity
import nithra.gift.suggestion.shop.birthday.marriage.R
import nithra.gift.suggestion.shop.birthday.marriage.retrofit.DeleteGift
import nithra.gift.suggestion.shop.birthday.marriage.retrofit.GiftList
import nithra.gift.suggestion.shop.birthday.marriage.retrofit.RetrofitAPI
import nithra.gift.suggestion.shop.birthday.marriage.retrofit.RetrofitApiClient.retrofit
import nithra.gift.suggestion.shop.birthday.marriage.retrofit.SellerProfilePojo
import nithra.gift.suggestion.shop.birthday.marriage.support.SharedPreference
import nithra.gift.suggestion.shop.birthday.marriage.support.Utils_Class.mProgress
import nithra.gift.suggestion.shop.birthday.marriage.support.Utils_Class.toast_center
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class SellerProfileProductList : AppCompatActivity() {
    var back: ImageView? = null
    var IVPreviewImage: ImageView? = null
    var sellerName: TextView? = null
    var shopName: TextView? = null
    var city: TextView? = null
    private var profileEdit: TextView? = null
    private var addProduct: TextView? = null
    var sharedPreference = SharedPreference()
    var gift: ArrayList<SellerProfilePojo>? = null
    var giftAda: ArrayList<GiftList>? = null
    var list: RecyclerView? = null
    var pullToRefresh: SwipeRefreshLayout? = null
    var noItem: LinearLayout? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.seller_profile_productlist)
        IVPreviewImage = findViewById(R.id.IVPreviewImage)
        sellerName = findViewById(R.id.seller_name)
        shopName = findViewById(R.id.shop_name)
        city = findViewById(R.id.city)
        profileEdit = findViewById(R.id.profile_edit)
        addProduct = findViewById(R.id.add_product)
        gift = ArrayList()
        giftAda = ArrayList()
        list = findViewById(R.id.list)
        back = findViewById(R.id.back)
        pullToRefresh = findViewById(R.id.pullToRefresh)
        noItem = findViewById(R.id.no_item)
        IVPreviewImage!!.setOnClickListener {
            val i = Intent(applicationContext, ImageSlide::class.java)
            i.putExtra("pos", gift!![0].logo)
            startActivity(i)
        }
        back!!.setOnClickListener {
            finishAffinity()
            val i = Intent(this@SellerProfileProductList, MainActivity::class.java)
            startActivity(i)
        }
        profileEdit!!.setOnClickListener {
            val i = Intent(applicationContext, ShopEdit::class.java)
            startActivity(i)
        }
        addProduct!!.setOnClickListener {
            if (sharedPreference.getInt(this@SellerProfileProductList, "profile") == 1) {
                val i = Intent(this@SellerProfileProductList, ShopAdd::class.java)
                startActivity(i)
            } else if (sharedPreference.getInt(this@SellerProfileProductList, "profile") == 2) {
                val i = Intent(this@SellerProfileProductList, ProductAdd::class.java)
                startActivity(i)
            }
        }
        val gridLayoutManager = GridLayoutManager(this, 1, GridLayoutManager.VERTICAL, false)
        list!!.layoutManager = gridLayoutManager
        adapter = Adapter(this, giftAda!!)
        list!!.adapter = adapter
        mProgress(this)!!.show()
        pullToRefresh!!.setOnRefreshListener {
            gift!!.clear()
            category1()
            pullToRefresh!!.isRefreshing = false
        }
    }

    override fun onResume() {
        super.onResume()
        category1()
        category()
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
                    gift!!.clear()
                    gift!!.addAll(response.body()!!)
                    Glide.with(applicationContext).load(gift!![0].logo)
                        .error(R.drawable.ic_gift_default_img)
                        .placeholder(R.drawable.ic_gift_default_img)
                        .diskCacheStrategy(DiskCacheStrategy.ALL)
                        .into(IVPreviewImage!!)
                    sellerName!!.text = gift!![0].name
                    shopName!!.text = gift!![0].shopName + ", "
                    city!!.text = gift!![0].city
                    mProgress!!.dismiss()

                }
            }

            override fun onFailure(call: Call<ArrayList<SellerProfilePojo>?>, t: Throwable) {
            }
        })
    }

    private fun category1() {
        val map = HashMap<String, String?>()
        map["action"] = "get_gift_list"
        map["user_id"] = sharedPreference.getString(this, "user_id")
        val retrofitAPI = retrofit!!.create(RetrofitAPI::class.java)
        val call = retrofitAPI.gift_giftlist(map)
        call.enqueue(object : Callback<ArrayList<GiftList>> {
            override fun onResponse(
                call: Call<ArrayList<GiftList>>, response: Response<ArrayList<GiftList>>
            ) {
                if (response.isSuccessful) {
                    if (response.body()!![0].status == "Success") {
                        giftAda!!.clear()
                        giftAda!!.addAll(response.body()!!)
                        adapter!!.notifyDataSetChanged()
                    }
                    if (giftAda!!.size == 0) {
                        pullToRefresh!!.visibility = View.GONE
                        noItem!!.visibility = View.VISIBLE
                    } else {
                        pullToRefresh!!.visibility = View.VISIBLE
                        noItem!!.visibility = View.GONE
                    }
                    mProgress!!.dismiss()
                }
            }

            override fun onFailure(call: Call<ArrayList<GiftList>>, t: Throwable) {
            }
        })
    }

    fun deleteGift(id_gift: String?, pos: Int) {
        val map = HashMap<String, String?>()
        map["action"] = "gift_delete"
        map["id"] = id_gift
        map["user_id"] = sharedPreference.getString(this@SellerProfileProductList, "user_id")
        val retrofitAPI = retrofit!!.create(RetrofitAPI::class.java)
        val call = retrofitAPI.delete_gift(map)
        call.enqueue(object : Callback<ArrayList<DeleteGift>> {
            override fun onResponse(
                call: Call<ArrayList<DeleteGift>>, response: Response<ArrayList<DeleteGift>>
            ) {
                if (response.isSuccessful) {
                    if (response.body()!![0].status == "Success") {
                        toast_center(applicationContext, "Your product deleted...")
                        giftAda!!.removeAt(pos)
                        adapter!!.notifyDataSetChanged()
                    }
                }
            }

            override fun onFailure(call: Call<ArrayList<DeleteGift>>, t: Throwable) {
            }
        })
    }

    inner class Adapter(ctx: Context, var gift: ArrayList<GiftList>) :
        RecyclerView.Adapter<Adapter.ViewHolder>() {
        private var inflater: LayoutInflater
        var context: Context
        var builder = AlertDialog.Builder(this@SellerProfileProductList)

        init {
            inflater = LayoutInflater.from(ctx)
            context = ctx
        }

        override fun onCreateViewHolder(
            parent: ViewGroup, viewType: Int
        ): ViewHolder {
            val layoutInflater = LayoutInflater.from(parent.context)
            val listItem = layoutInflater.inflate(R.layout.adapter_myproduct, parent, false)
            return ViewHolder(listItem)
        }

        override fun onBindViewHolder(holder: ViewHolder, position: Int) {
            holder.gridText.text = gift[position].giftName
            val currentString = gift[position].giftImage
            val separated =
                currentString!!.split(",".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
            if (separated.isNotEmpty()) {
                Glide.with(context).load(separated[0])
                    .error(R.drawable.ic_gift_default_img)
                    .placeholder(R.drawable.ic_gift_default_img)
                    .diskCacheStrategy(DiskCacheStrategy.ALL).into(holder.imgSlide)
            } else {
                // handle the case where the array is empty or null
            }



            holder.head.text = gift[position].discount + "% offer"
            holder.editProduct.setOnClickListener {
                val i = Intent(applicationContext, ProductEdit::class.java)
                i.putExtra("id", gift[position].id)
                startActivity(i)
            }
            holder.category.setOnClickListener {
                val i = Intent(applicationContext, ProductFullView::class.java)
                i.putExtra("id", gift[position].id)
                startActivity(i)
            }
            holder.profileDelete.setOnClickListener {
                builder.setMessage("Do you want to delete this Product?").setCancelable(false)
                    .setPositiveButton("No") { dialog, _ -> dialog.cancel() }
                    .setNegativeButton("Yes") { _, _ ->
                        deleteGift(
                            gift[position].id, position
                        )
                    }
                val alert = builder.create()
                alert.show()
            }
        }

        override fun getItemCount(): Int {
            return gift.size
        }

        inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
            var imgSlide: ImageView
            var profileDelete: ImageView
            var gridText: TextView
            var editProduct: TextView
            var head: TextView
            var category: CardView

            init {
                imgSlide = itemView.findViewById(R.id.imageGrid)
                gridText = itemView.findViewById(R.id.gridText)
                editProduct = itemView.findViewById(R.id.edit_product)
                category = itemView.findViewById(R.id.category)
                head = itemView.findViewById(R.id.head)
                profileDelete = itemView.findViewById(R.id.profile_delete)
            }
        }
    }

    companion object {
        var adapter: Adapter? = null
    }
}