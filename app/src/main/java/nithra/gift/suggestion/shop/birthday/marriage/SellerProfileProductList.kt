package nithra.gift.suggestion.shop.birthday.marriage

import android.app.AlertDialog
import android.app.ProgressDialog.show
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
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout.OnRefreshListener
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.google.gson.Gson
import nithra.gift.suggestion.shop.birthday.marriage.Otp.ProductAdd
import nithra.gift.suggestion.shop.birthday.marriage.Otp.ShopAdd
import nithra.gift.suggestion.shop.birthday.marriage.Retrofit.DeleteGift
import nithra.gift.suggestion.shop.birthday.marriage.Retrofit.GiftList
import nithra.gift.suggestion.shop.birthday.marriage.Retrofit.RetrofitAPI
import nithra.gift.suggestion.shop.birthday.marriage.Retrofit.RetrofitApiClient.retrofit
import nithra.gift.suggestion.shop.birthday.marriage.Retrofit.SellerProfilePojo
import nithra.gift.suggestion.shop.birthday.marriage.Utils_Class.mProgress
import nithra.gift.suggestion.shop.birthday.marriage.Utils_Class.toast_center
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class SellerProfileProductList : AppCompatActivity() {
    var back: ImageView? = null
    var IVPreviewImage: ImageView? = null
    var seller_name: TextView? = null
    var shop_name: TextView? = null
    var city: TextView? = null
    var profile_edit: TextView? = null
    var add_product: TextView? = null
    var sharedPreference = SharedPreference()
    var gift: ArrayList<SellerProfilePojo>? = null
    var gift_ada: ArrayList<GiftList>? = null
    var list: RecyclerView? = null
    var pullToRefresh: SwipeRefreshLayout? = null
    var no_item: LinearLayout? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requestWindowFeature(Window.FEATURE_NO_TITLE)
        this.window.setFlags(
            WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN
        )
        setContentView(R.layout.seller_profile_productlist)
        IVPreviewImage = findViewById(R.id.IVPreviewImage)
        seller_name = findViewById(R.id.seller_name)
        shop_name = findViewById(R.id.shop_name)
        city = findViewById(R.id.city)
        profile_edit = findViewById(R.id.profile_edit)
        add_product = findViewById(R.id.add_product)
        gift = ArrayList()
        gift_ada = ArrayList()
        list = findViewById(R.id.list)
        back = findViewById(R.id.back)
        pullToRefresh = findViewById(R.id.pullToRefresh)
        no_item = findViewById(R.id.no_item)
        IVPreviewImage!!.setOnClickListener(View.OnClickListener {
            val i = Intent(applicationContext, ImageSlide::class.java)
            i.putExtra("pos", gift!![0].logo)
            startActivity(i)
        })
        back!!.setOnClickListener(View.OnClickListener {
            finishAffinity()
            val i = Intent(this@SellerProfileProductList, MainActivity::class.java)
            startActivity(i)
            // finish();
        })
        profile_edit!!.setOnClickListener(View.OnClickListener {
            val i = Intent(applicationContext, ShopEdit::class.java)
            startActivity(i)
        })
        add_product!!.setOnClickListener(View.OnClickListener {
            if (sharedPreference.getInt(this@SellerProfileProductList, "profile") == 1) {
                val i = Intent(this@SellerProfileProductList, ShopAdd::class.java)
                startActivity(i)
            } else if (sharedPreference.getInt(this@SellerProfileProductList, "profile") == 2) {
                val i = Intent(this@SellerProfileProductList, ProductAdd::class.java)
                startActivity(i)
            }
        })
        val gridLayoutManager = GridLayoutManager(this, 1, GridLayoutManager.VERTICAL, false)
        list!!.setLayoutManager(gridLayoutManager)
        adapter = Adapter(this, gift_ada!!)
        list!!.setAdapter(adapter)
        mProgress(this, "Loading please wait...", false)!!.show()
        pullToRefresh!!.setOnRefreshListener(OnRefreshListener {
            gift!!.clear()
            category1()
            pullToRefresh!!.setRefreshing(false)
        })
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
                    shop_name!!.text = gift!![0].shopName + ", "
                    city!!.text = gift!![0].city
                    mProgress!!.dismiss()

                }
                println("======response :$response")
            }

            override fun onFailure(call: Call<ArrayList<SellerProfilePojo>?>, t: Throwable) {
                println("======response t:$t")
            }
        })
    }

    fun category1() {
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
                    val result = Gson().toJson(response.body())
                    println("======response result:$result")
                    if (response.body()!![0].status == "Success") {
                        gift_ada!!.clear()
                        gift_ada!!.addAll(response.body()!!)
                        println("print_size==" + gift_ada!!.size)
                        adapter!!.notifyDataSetChanged()
                    }
                    if (gift_ada!!.size == 0) {
                        pullToRefresh!!.visibility = View.GONE
                        no_item!!.visibility = View.VISIBLE
                    } else {
                        pullToRefresh!!.visibility = View.VISIBLE
                        no_item!!.visibility = View.GONE
                    }
                    mProgress!!.dismiss()
                }
                println("======response :$response")
            }

            override fun onFailure(call: Call<ArrayList<GiftList>>, t: Throwable) {
                println("======response t:$t")
            }
        })
    }

    fun delete_gift(id_gift: String?, pos: Int) {
        val map = HashMap<String, String?>()
        map["action"] = "gift_delete"
        map["id"] = id_gift
        map["user_id"] = sharedPreference.getString(this@SellerProfileProductList, "user_id")
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
                    if (response.body()!![0].status == "Success") {
                        toast_center(applicationContext, "Your product deleted...")
                        gift_ada!!.removeAt(pos)
                        adapter!!.notifyDataSetChanged()
                    }
                }
                println("======response :$response")
            }

            override fun onFailure(call: Call<ArrayList<DeleteGift>>, t: Throwable) {
                println("======response t:$t")
            }
        })
    }

    inner class Adapter(ctx: Context, var gift: ArrayList<GiftList>) :
        RecyclerView.Adapter<Adapter.ViewHolder>() {
        var inflater: LayoutInflater
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
                Glide.with(context).load(separated[0]).error(R.drawable.ic_gift_default_img)
                    .placeholder(R.drawable.ic_gift_default_img)
                    .diskCacheStrategy(DiskCacheStrategy.ALL).into(holder.img_slide)
            } else {
                // handle the case where the array is empty or null
            }



            println("print_img " + gift[position].giftImage)
            holder.head.text = gift[position].discount + "% offer"
            holder.edit_product.setOnClickListener { //sharedPreference.getString(this, "gift_id");
                val i = Intent(applicationContext, ProductEdit::class.java)
                i.putExtra("id", gift[position].id)
                println("print_id==" + gift[position].id)
                startActivity(i)
            }
            holder.category.setOnClickListener {
                val i = Intent(applicationContext, ProductFullView::class.java)
                i.putExtra("id", gift[position].id)
                startActivity(i)
            }
            holder.profile_delete.setOnClickListener {
                builder.setMessage("Do you want to delete this Product?").setCancelable(false)
                    .setPositiveButton("No") { dialog, id -> dialog.cancel() }
                    .setNegativeButton("Yes") { dialog, id ->
                        delete_gift(
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
            var img_slide: ImageView
            var profile_delete: ImageView
            var gridText: TextView
            var edit_product: TextView
            var head: TextView
            var category: CardView

            init {
                img_slide = itemView.findViewById(R.id.imageGrid)
                gridText = itemView.findViewById(R.id.gridText)
                edit_product = itemView.findViewById(R.id.edit_product)
                category = itemView.findViewById(R.id.category)
                head = itemView.findViewById(R.id.head)
                profile_delete = itemView.findViewById(R.id.profile_delete)
            }
        }
    }

    companion object {
        var adapter: Adapter? = null
    }
}