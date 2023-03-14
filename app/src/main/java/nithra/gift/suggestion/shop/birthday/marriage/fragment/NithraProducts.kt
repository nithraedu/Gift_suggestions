package nithra.gift.suggestion.shop.birthday.marriage.fragment

import android.app.ProgressDialog
import android.content.Context
import android.content.Intent
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
import nithra.gift.suggestion.shop.birthday.marriage.ImageSlide
import nithra.gift.suggestion.shop.birthday.marriage.R
import nithra.gift.suggestion.shop.birthday.marriage.retrofit.GiftList
import nithra.gift.suggestion.shop.birthday.marriage.retrofit.RetrofitAPI
import nithra.gift.suggestion.shop.birthday.marriage.retrofit.RetrofitApiClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class NithraProducts : Fragment() {
    var adapter: Adapter? = null
    var list: RecyclerView? = null
    var no_item: LinearLayout? = null
    var pullToRefresh: SwipeRefreshLayout? = null
    var gift_show: ArrayList<GiftList>? = null
    var intent1: Intent? = null
    var extra: Bundle? = null
    var title: String? = null
    var title1: String? = null
    var title3: String? = null
    var mProgress: ProgressDialog? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_nithra_products, container, false)
        gift_show = ArrayList()
        list = view.findViewById(R.id.list)
        no_item = view.findViewById(R.id.no_item)
        pullToRefresh = view.findViewById(R.id.pullToRefresh)
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
        map["action"] = "nithra"
        map["gift_category"] = title1
        map["user_id"] = "" + 33
        val retrofitAPI = RetrofitApiClient.retrofit!!.create(
            RetrofitAPI::class.java
        )
        val call = retrofitAPI.gift_giftlist(map)
        call.enqueue(object : Callback<ArrayList<GiftList>> {
            override fun onResponse(
                call: Call<ArrayList<GiftList>>,
                response: Response<ArrayList<GiftList>>
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

            override fun onFailure(call: Call<ArrayList<GiftList>>, t: Throwable) {
                mProgress!!.dismiss()
            }
        })
    }

    fun get_cat1() {
        val map = HashMap<String, String?>()
        map["action"] = "nithra"
        map["gift_for"] = title3
        map["user_id"] = "" + 33
        val retrofitAPI = RetrofitApiClient.retrofit!!.create(
            RetrofitAPI::class.java
        )
        val call = retrofitAPI.gift_giftlist(map)
        call.enqueue(object : Callback<ArrayList<GiftList>> {
            override fun onResponse(
                call: Call<ArrayList<GiftList>>,
                response: Response<ArrayList<GiftList>>
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

            override fun onFailure(call: Call<ArrayList<GiftList>>, t: Throwable) {
                mProgress!!.dismiss()
            }
        })
    }

    inner class Adapter(ctx: Context, gift_show: ArrayList<GiftList>?) :
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
                layoutInflater.inflate(R.layout.adapter_nithra_suggestions, parent, false)
            return ViewHolder(listItem)
        }

        override fun onBindViewHolder(holder: ViewHolder, position: Int) {
            val currentString = gift_show!![position].giftImage
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
            holder.gridText.text = gift_show!![position].giftName
            holder.category.setOnClickListener {
                val currentString = gift_show!![position].giftImage
                val i = Intent(getContext(), ImageSlide::class.java)
                i.putExtra("pos", currentString)
                i.putExtra("name", gift_show!![position].giftName)
                startActivity(i)
            }
        }

        override fun getItemCount(): Int {
            return gift_show!!.size
        }

        inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
            var img_slide: ImageView
            var gridText: TextView
            var category: CardView

            init {
                img_slide = itemView.findViewById(R.id.imageGrid)
                gridText = itemView.findViewById(R.id.gridText)
                category = itemView.findViewById(R.id.category)
            }
        }
    }
}