package nithra.gift.suggestion.shop.birthday.marriage.fragment

import android.app.ProgressDialog
import android.content.Context
import android.content.Intent
import android.graphics.Paint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import nithra.gift.suggestion.shop.birthday.marriage.Full_Details
import nithra.gift.suggestion.shop.birthday.marriage.R
import nithra.gift.suggestion.shop.birthday.marriage.retrofit.Fav_Add_Del
import nithra.gift.suggestion.shop.birthday.marriage.retrofit.Gift_Cat
import nithra.gift.suggestion.shop.birthday.marriage.retrofit.RetrofitAPI
import nithra.gift.suggestion.shop.birthday.marriage.retrofit.RetrofitApiClient
import nithra.gift.suggestion.shop.birthday.marriage.support.SharedPreference
import nithra.gift.suggestion.shop.birthday.marriage.support.Utils_Class
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class Sellerproducts : Fragment() {
    var fav_add_dels: ArrayList<Fav_Add_Del>? = null
    var list: RecyclerView? = null
    var intent1: Intent? = null
    var extra: Bundle? = null
    var title: String? = null
    var title1: String? = null
    var title3: String? = null
    var sharedPreference = SharedPreference()
    var no_item: LinearLayout? = null
    var pullToRefresh: SwipeRefreshLayout? = null
    var mProgress: ProgressDialog? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_sellerproducts, container, false)
        gift_show = ArrayList()
        list = view.findViewById(R.id.list)
        no_item = view.findViewById(R.id.no_item)
        pullToRefresh = view.findViewById(R.id.pullToRefresh)
        fav_add_dels = ArrayList()
        intent1 = requireActivity().intent
        extra = intent1!!.getExtras()
        if (extra != null) {
            title = extra!!.getString("title")
            title1 = extra!!.getString("cat_idd")
            title3 = extra!!.getString("gender_id")
        }
        mProgress = ProgressDialog(context)
        mProgress!!.show()
        mProgress!!.setMessage("Loading please wait...")
        val gridLayoutManager = GridLayoutManager(context, 2, GridLayoutManager.VERTICAL, false)
        list!!.setLayoutManager(gridLayoutManager)
        adapter = context?.let {
            Adapter(
                it, gift_show
            )
        }
        list!!.setAdapter(adapter)
        get_cat()
        get_cat1()
        pullToRefresh!!.setOnRefreshListener({
            gift_show!!.clear()
            get_cat()
            get_cat1()
            pullToRefresh!!.setRefreshing(false)
        })
        return view
    }

    fun get_cat() {
        val map = HashMap<String, String?>()
        map["action"] = "get_cat"
        map["gift_category"] = title1
        map["user_id"] = context?.let { sharedPreference.getString(it, "android_userid") }
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
                        adapter!!.notifyDataSetChanged()
                    }
                    if (gift_show!!.size == 0) {
                        pullToRefresh!!.visibility = View.GONE
                        no_item!!.visibility = View.VISIBLE
                    } else {
                        pullToRefresh!!.visibility = View.VISIBLE
                        no_item!!.visibility = View.GONE
                    }
                    mProgress!!.dismiss()
                }
            }

            override fun onFailure(call: Call<ArrayList<Gift_Cat>>, t: Throwable) {
                mProgress!!.dismiss()
            }
        })
    }

    fun get_cat1() {
        val map = HashMap<String, String?>()
        map["action"] = "get_cat"
        map["gift_for"] = title3
        map["user_id"] = context?.let { sharedPreference.getString(it, "android_userid") }
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
                        adapter!!.notifyDataSetChanged()
                    }
                    if (gift_show!!.size == 0) {
                        pullToRefresh!!.visibility = View.GONE
                        no_item!!.visibility = View.VISIBLE
                    } else {
                        pullToRefresh!!.visibility = View.VISIBLE
                        no_item!!.visibility = View.GONE
                    }
                    mProgress!!.dismiss()
                }
            }

            override fun onFailure(call: Call<ArrayList<Gift_Cat>>, t: Throwable) {
                mProgress!!.dismiss()
            }
        })
    }

    fun fav(id_gift: String, pos: Int) {
        val map = HashMap<String, String?>()
        map["action"] = "favourite"
        map["gift_id"] = id_gift
        map["user_id"] = context?.let { sharedPreference.getString(it, "android_userid") }
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
                            gift_show!![pos].fav = 1
                            Utils_Class.toast_center(context, "Your gift added to favourite...")
                        } else {
                            gift_show!![pos].fav = 0
                            Utils_Class.toast_center(context, "Your gift removed from favourite...")
                        }
                        adapter!!.notifyDataSetChanged()
                    }
                    Utils_Class.mProgress!!.dismiss()
                }
            }

            override fun onFailure(call: Call<ArrayList<Fav_Add_Del>>, t: Throwable) {
            }
        })
    }

    inner class Adapter(ctx: Context, gift_show: ArrayList<Gift_Cat>?) :
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
            if (gift_show!![position].fav == 1) {
                holder.favourite.setBackgroundResource(R.drawable.favorite_red)
            } else {
                holder.favourite.setBackgroundResource(R.drawable.favorite_grey)
            }
            holder.giftprize.paintFlags = holder.giftprize.paintFlags or Paint.STRIKE_THRU_TEXT_FLAG
            val currentString = gift_show!![position].giftImage
            val separated = currentString!!.split(",".toRegex()).dropLastWhile { it.isEmpty() }
                .toTypedArray()
            if (separated.isNotEmpty()) {
                Glide.with(context).load(separated[0])
                    .error(R.drawable.ic_gift_default_img)
                    .placeholder(R.drawable.ic_gift_default_img)
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .into(holder.img_slide)
            }
            holder.gridText.text = gift_show!![position].giftName
            holder.head.text = gift_show!![position].discount + "% offer"
            holder.giftprize.text = "\u20B9 " + gift_show!![position].totalAmount
            holder.offerprize.text = "\u20B9 " + gift_show!![position].giftAmount
            holder.category.setOnClickListener {
                val i = Intent(getContext(), Full_Details::class.java)
                i.putExtra("full_view", gift_show!![position].id)
                i.putExtra("position", position)
                startActivity(i)
            }
            holder.favourite.setOnClickListener {
                gift_show!![position].id?.let { it1 ->
                    fav(
                        it1,
                        position
                    )
                }
            }
        }

        override fun getItemCount(): Int {
            return gift_show!!.size
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
                favourite = itemView.findViewById(R.id.favourite)
                gridText = itemView.findViewById(R.id.gridText)
                head = itemView.findViewById(R.id.head)
                category = itemView.findViewById(R.id.category)
                giftprize = itemView.findViewById(R.id.giftprize)
                offerprize = itemView.findViewById(R.id.offerprize)
            }
        }
    }

    companion object {
        @JvmField
        var adapter: Adapter? = null
        @JvmField
        var gift_show: ArrayList<Gift_Cat>? = null
    }
}