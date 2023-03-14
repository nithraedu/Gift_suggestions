package nithra.gift.suggestion.shop.birthday.marriage

import android.content.Context
import android.content.Intent
import android.graphics.Paint
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
import nithra.gift.suggestion.shop.birthday.marriage.retrofit.Fav_Add_Del
import nithra.gift.suggestion.shop.birthday.marriage.retrofit.Fav_view
import nithra.gift.suggestion.shop.birthday.marriage.retrofit.RetrofitAPI
import nithra.gift.suggestion.shop.birthday.marriage.retrofit.RetrofitApiClient
import nithra.gift.suggestion.shop.birthday.marriage.support.SharedPreference
import nithra.gift.suggestion.shop.birthday.marriage.support.Utils_Class
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class Fav_class : AppCompatActivity() {
    var list: RecyclerView? = null
    var no_item: LinearLayout? = null
    var pullToRefresh: SwipeRefreshLayout? = null
    var sharedPreference = SharedPreference()
    var back: ImageView? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requestWindowFeature(Window.FEATURE_NO_TITLE)
        this.window.setFlags(
            WindowManager.LayoutParams.FLAG_FULLSCREEN,
            WindowManager.LayoutParams.FLAG_FULLSCREEN
        )
        setContentView(R.layout.fragment_favourite)
        fav_show = ArrayList()
        list = findViewById(R.id.list)
        no_item = findViewById(R.id.no_item)
        pullToRefresh = findViewById(R.id.pullToRefresh)
        back = findViewById(R.id.back)
        back!!.setOnClickListener({ finish() })
        Utils_Class.mProgress(this@Fav_class, "Loading please wait...", false)!!.show()
        val gridLayoutManager =
            GridLayoutManager(this@Fav_class, 2, GridLayoutManager.VERTICAL, false)
        list!!.setLayoutManager(gridLayoutManager)
        adapter = Adapter(this@Fav_class, fav_show)
        list!!.setAdapter(adapter)
        pullToRefresh!!.setOnRefreshListener({
            fav_show!!.clear()
            fav()
            pullToRefresh!!.setRefreshing(false)
        })
    }

    public override fun onResume() {
        super.onResume()
        fav()
    }

    fun fav() {
        fav_show!!.clear()
        val map = HashMap<String, String?>()
        map["action"] = "get_fav"
        map["user_id"] = sharedPreference.getString(this, "android_userid")
        val retrofitAPI = RetrofitApiClient.retrofit!!.create(
            RetrofitAPI::class.java
        )
        val call = retrofitAPI.fav_view(map)
        call.enqueue(object : Callback<ArrayList<Fav_view>> {
            override fun onResponse(
                call: Call<ArrayList<Fav_view>>,
                response: Response<ArrayList<Fav_view>>
            ) {
                if (response.isSuccessful) {
                    if (response.body()!![0].status == "Success") {
                        fav_show!!.addAll(response.body()!!)
                        adapter!!.notifyDataSetChanged()
                    }
                    if (fav_show!!.size == 0) {
                        pullToRefresh!!.visibility = View.GONE
                        no_item!!.visibility = View.VISIBLE
                    } else {
                        pullToRefresh!!.visibility = View.VISIBLE
                        no_item!!.visibility = View.GONE
                    }
                    Utils_Class.mProgress!!.dismiss()
                }
            }

            override fun onFailure(call: Call<ArrayList<Fav_view>>, t: Throwable) {
            }
        })
    }

    fun fav1(id_gift: String, pos: Int) {
        val map = HashMap<String, String?>()
        map["action"] = "favourite"
        map["gift_id"] = id_gift
        map["user_id"] = sharedPreference.getString(this@Fav_class, "android_userid")
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
                            fav_show!![pos].fav = 1
                            Utils_Class.toast_center(
                                this@Fav_class,
                                "Your gift added to favourite..."
                            )
                        } else {
                            fav_show!![pos].fav = 0
                            Utils_Class.toast_center(
                                this@Fav_class,
                                "Your gift removed from favourite..."
                            )
                        }
                        adapter!!.notifyDataSetChanged()
                    }
                }
            }

            override fun onFailure(call: Call<ArrayList<Fav_Add_Del>>, t: Throwable) {
            }
        })
    }

    inner class Adapter(ctx: Context, gift_show: ArrayList<Fav_view>?) :
        RecyclerView.Adapter<Adapter.ViewHolder>() {
        var inflater: LayoutInflater
        var context: Context

        init {
            inflater = LayoutInflater.from(ctx)
            context = ctx
        }

        override fun onCreateViewHolder(
            parent: ViewGroup,
            viewType: Int
        ): ViewHolder {
            val layoutInflater = LayoutInflater.from(parent.context)
            val listItem =
                layoutInflater.inflate(R.layout.adapter, parent, false)
            return ViewHolder(listItem)
        }

        override fun onBindViewHolder(holder: ViewHolder, position: Int) {
            if (fav_show!![position].fav == 1) {
                holder.favourite.setBackgroundResource(R.drawable.favorite_red)
            } else {
                holder.favourite.setBackgroundResource(R.drawable.favorite_grey)
            }
            holder.giftprize.paintFlags = holder.giftprize.paintFlags or Paint.STRIKE_THRU_TEXT_FLAG
            val currentString = fav_show!![position].giftImage
            val separated = currentString!!.split(",".toRegex()).dropLastWhile { it.isEmpty() }
                .toTypedArray()
            if (separated.isNotEmpty()) {
                Glide.with(context).load(separated[0])
                    .error(R.drawable.ic_gift_default_img)
                    .placeholder(R.drawable.ic_gift_default_img)
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .into(holder.img_slide)
            } else {
                // handle the case where separated is empty or null
            }
            holder.gridText.text = fav_show!![position].giftName
            holder.head.text = fav_show!![position].discount + "% offer"
            holder.giftprize.text = "\u20B9 " + fav_show!![position].totalAmount
            holder.offerprize.text = "\u20B9 " + fav_show!![position].giftAmount
            holder.category.setOnClickListener {
                val i = Intent(this@Fav_class, Full_Details::class.java)
                i.putExtra("full_view", fav_show!![position].giftId)
                startActivity(i)
            }
            holder.favourite.setOnClickListener {
                fav_show!![position].giftId?.let { it1 ->
                    fav1(
                        it1,
                        position
                    )
                }
            }
        }

        override fun getItemCount(): Int {
            return fav_show!!.size
        }

        inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
            var img_slide: ImageView
            var favourite: ImageView
            var gridText: TextView
            var head: TextView
            var giftprize: TextView
            var offerprize: TextView
            var category: CardView

            init {
                img_slide = itemView.findViewById(R.id.imageGrid)
                gridText = itemView.findViewById(R.id.gridText)
                head = itemView.findViewById(R.id.head)
                category = itemView.findViewById(R.id.category)
                favourite = itemView.findViewById(R.id.favourite)
                giftprize = itemView.findViewById(R.id.giftprize)
                offerprize = itemView.findViewById(R.id.offerprize)
            }
        }
    }

    companion object {
        var adapter: Adapter? = null
        var fav_show: ArrayList<Fav_view>? = null
    }
}