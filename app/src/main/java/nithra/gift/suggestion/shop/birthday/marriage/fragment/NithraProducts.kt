package nithra.gift.suggestion.shop.birthday.marriage.fragment

import android.app.Dialog
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
    var noItem: LinearLayout? = null
    var pullToRefresh: SwipeRefreshLayout? = null
    var giftShow: ArrayList<GiftList>? = null
    private var intent1: Intent? = null
    var extra: Bundle? = null
    var title: String? = null
    private var title1: String? = null
    private var title3: String? = null
    var dialog:Dialog?=null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_nithra_products, container, false)
        giftShow = ArrayList()
        list = view.findViewById(R.id.list)
        noItem = view.findViewById(R.id.no_item)
        pullToRefresh = view.findViewById(R.id.pullToRefresh)

        intent1 = requireActivity().intent
        extra = intent1!!.extras


        dialog = context?.let {
            Dialog(
                it,
                android.R.style.Theme_DeviceDefault_Dialog_NoActionBar_MinWidth
            )
        }
        dialog!!.setContentView(R.layout.loading_dialog)
        dialog!!.window!!.setBackgroundDrawableResource(android.R.color.transparent)
        dialog!!.setCancelable(false)
        dialog!!.show()


        if (extra != null) {
            title = extra!!.getString("title")
            title1 = extra!!.getString("cat_idd")
            title3 = extra!!.getString("gender_id")
        }
        val gridLayoutManager = GridLayoutManager(context, 2, GridLayoutManager.VERTICAL, false)
        list!!.layoutManager = gridLayoutManager
        adapter = context?.let {
            Adapter(
                it
            )
        }
        list!!.adapter = adapter
        getCat()
        getCat1()
        pullToRefresh!!.setOnRefreshListener {
            giftShow!!.clear()
            getCat()
            getCat1()
            pullToRefresh!!.isRefreshing = false
        }
        return view
    }

    private fun getCat() {
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
                        giftShow!!.clear()
                        giftShow!!.addAll(response.body()!!)
                        adapter!!.notifyDataSetChanged()
                    }
                    if (giftShow!!.size == 0) {
                        pullToRefresh!!.visibility = View.GONE
                        noItem!!.visibility = View.VISIBLE
                    } else {
                        pullToRefresh!!.visibility = View.VISIBLE
                        noItem!!.visibility = View.GONE
                    }
                    dialog!!.dismiss()

                }
            }

            override fun onFailure(call: Call<ArrayList<GiftList>>, t: Throwable) {
                dialog!!.dismiss()

            }
        })
    }

    private fun getCat1() {
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
                        giftShow!!.clear()
                        giftShow!!.addAll(response.body()!!)
                        adapter!!.notifyDataSetChanged()
                    }
                    if (giftShow!!.size == 0) {
                        pullToRefresh!!.visibility = View.GONE
                        noItem!!.visibility = View.VISIBLE
                    } else {
                        pullToRefresh!!.visibility = View.VISIBLE
                        noItem!!.visibility = View.GONE
                    }
                    dialog!!.dismiss()

                }
            }

            override fun onFailure(call: Call<ArrayList<GiftList>>, t: Throwable) {
                dialog!!.dismiss()
            }
        })
    }

    inner class Adapter(ctx: Context) :
        RecyclerView.Adapter<Adapter.ViewHolder>() {
        private var inflater: LayoutInflater
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
            val currentString = giftShow!![position].giftImage
            val separated = currentString!!.split(",".toRegex()).dropLastWhile { it.isEmpty() }
                .toTypedArray()
            if (separated.isNotEmpty()) {
                Glide.with(context).load(separated[0])
                    .error(R.drawable.ic_gift_default_img)
                    .placeholder(R.drawable.ic_gift_default_img)
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .into(holder.imgSlide)
            } else {
                // handle the case where separated is empty or null
            }
            holder.gridText.text = giftShow!![position].giftName
            holder.category.setOnClickListener {
                val currentString = giftShow!![position].giftImage
                val i = Intent(getContext(), ImageSlide::class.java)
                i.putExtra("pos", currentString)
                i.putExtra("name", giftShow!![position].giftName)
                startActivity(i)
            }
        }

        override fun getItemCount(): Int {
            return giftShow!!.size
        }

        inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
            var imgSlide: ImageView
            var gridText: TextView
            var category: CardView

            init {
                imgSlide = itemView.findViewById(R.id.imageGrid)
                gridText = itemView.findViewById(R.id.gridText)
                category = itemView.findViewById(R.id.category)
            }
        }
    }
}