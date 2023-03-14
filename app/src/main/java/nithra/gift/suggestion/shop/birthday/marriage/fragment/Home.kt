package nithra.gift.suggestion.shop.birthday.marriage.fragment

import android.app.Dialog
import android.content.Context
import android.content.Context.MODE_PRIVATE
import android.content.Intent
import android.database.sqlite.SQLiteDatabase
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.widget.*
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.cardview.widget.CardView
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.google.android.material.navigation.NavigationView
import nithra.gift.suggestion.shop.birthday.marriage.*
import nithra.gift.suggestion.shop.birthday.marriage.feedback.Feedback
import nithra.gift.suggestion.shop.birthday.marriage.feedback.Method
import nithra.gift.suggestion.shop.birthday.marriage.feedback.RetrofitClient
import nithra.gift.suggestion.shop.birthday.marriage.notification.NotificationView
import nithra.gift.suggestion.shop.birthday.marriage.product_shop.SellerEntry
import nithra.gift.suggestion.shop.birthday.marriage.product_shop.SellerProfileProductList
import nithra.gift.suggestion.shop.birthday.marriage.product_shop.ShopAdd
import nithra.gift.suggestion.shop.birthday.marriage.retrofit.GiftFor
import nithra.gift.suggestion.shop.birthday.marriage.retrofit.Occasion
import nithra.gift.suggestion.shop.birthday.marriage.retrofit.RetrofitAPI
import nithra.gift.suggestion.shop.birthday.marriage.retrofit.RetrofitApiClient
import nithra.gift.suggestion.shop.birthday.marriage.support.SharedPreference
import nithra.gift.suggestion.shop.birthday.marriage.support.Utils_Class
import org.json.JSONException
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.UnsupportedEncodingException
import java.net.URLEncoder

class Home : Fragment(), NavigationView.OnNavigationItemSelectedListener {
    private var drawer: DrawerLayout? = null
    var giftfor: ArrayList<GiftFor>? = null
    var giftoccasion: ArrayList<Occasion>? = null
    var adapter2: Adapter2? = null
    var adapter3: Adapter3? = null
    var notification: LinearLayout? = null
    private var profileView: LinearLayout? = null
    private var favourite: LinearLayout? = null
    var sharedPreference = SharedPreference()
    var code: TextView? = null
    var name: TextView? = null
    private var versionCode = BuildConfig.VERSION_CODE
    private var versionName = BuildConfig.VERSION_NAME
    var pullToRefresh: SwipeRefreshLayout? = null
    private var hintTextNoInternet: TextView? = null
    private var fullLay: LinearLayout? = null
    var a = 0
    private var notifiCount: TextView? = null
    private var db1: SQLiteDatabase? = null

    override fun onResume() {
        super.onResume()
        visible()

    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_home, container, false)
        val toolbar = view.findViewById<Toolbar>(R.id.toolbar)
        drawer = view.findViewById(R.id.drawer_layout)
        notification = view.findViewById(R.id.notification)
        notifiCount = view.findViewById(R.id.notifi_count)
        profileView = view.findViewById(R.id.profile_view)
        favourite = view.findViewById(R.id.favourite)
        hintTextNoInternet = view.findViewById(R.id.tv_hinds)
        fullLay = view.findViewById(R.id.full_lay)
        giftfor = ArrayList()
        giftoccasion = ArrayList()
        val list = view.findViewById<RecyclerView>(R.id.list)
        val list2 = view.findViewById<RecyclerView>(R.id.list2)
        pullToRefresh = view.findViewById(R.id.pullToRefresh)

        db1 = requireActivity().openOrCreateDatabase("myDB", MODE_PRIVATE, null)
        val tablenew = "noti_cal"
        db1!!.execSQL(
            "CREATE TABLE IF NOT EXISTS " + tablenew
                    + " (id integer NOT NULL PRIMARY KEY AUTOINCREMENT,title VARCHAR,message VARCHAR,date VARCHAR,time VARCHAR,isclose INT(4),isshow INT(4) default 0,type VARCHAR,"
                    + "bm VARCHAR,ntype VARCHAR,url VARCHAR);"
        )


        favourite!!.setOnClickListener {
            val intent = Intent(context, Favclass::class.java)
            startActivity(intent)
        }
        notification!!.setOnClickListener {
            val intent = Intent(context, NotificationView::class.java)
            startActivity(intent)
        }
        (activity as AppCompatActivity?)!!.setSupportActionBar(toolbar)
        val toggle = ActionBarDrawerToggle(
            activity,
            drawer,
            toolbar,
            R.string.navigation_drawer_open,
            R.string.navigation_drawer_close
        )
        drawer!!.addDrawerListener(toggle)
        toggle.syncState()
        (activity as AppCompatActivity?)!!.supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        (activity as AppCompatActivity?)!!.supportActionBar!!.setHomeButtonEnabled(true)
        (activity as AppCompatActivity?)!!.supportActionBar!!.setDisplayShowTitleEnabled(true)
        (activity as AppCompatActivity?)!!.supportActionBar!!.setHomeAsUpIndicator(R.drawable.ic_menu_navigate)
        val navigationView = view.findViewById<NavigationView>(R.id.nav_mm_view)
        navigationView.setNavigationItemSelectedListener { item: MenuItem ->
            onNavigationItemSelected(item)
        }
        val v = navigationView.inflateHeaderView(R.layout.header)
        code = v.findViewById(R.id.code)
        name = v.findViewById(R.id.name)
        code!!.text = "" + versionCode
        name!!.text = versionName
        val gridLayoutManager2 = GridLayoutManager(context, 1, GridLayoutManager.HORIZONTAL, false)
        list.layoutManager = gridLayoutManager2
        val gridLayoutManager = GridLayoutManager(context, 3, GridLayoutManager.VERTICAL, false)
        list2.layoutManager = gridLayoutManager
        if (context?.let { Utils_Class.isNetworkAvailable(it) } == true) {
            Utils_Class.mProgress(context)!!.show()
            hintTextNoInternet!!.visibility = View.GONE

        } else {
            Utils_Class.mProgress(context)
            fullLay!!.visibility = View.GONE
            hintTextNoInternet!!.visibility = View.VISIBLE
        }
        adapter2 = Adapter2(context, giftfor!!)
        list.adapter = adapter2
        adapter3 = Adapter3(context, giftoccasion!!)
        list2.adapter = adapter3

        giftOccasion()
        genderGift()



        pullToRefresh!!.setOnRefreshListener {
            if (context?.let { Utils_Class.isNetworkAvailable(it) } == true) {
                hintTextNoInternet!!.visibility = View.GONE
                fullLay!!.visibility = View.VISIBLE

                giftoccasion!!.clear()
                giftfor!!.clear()
                giftOccasion()
                genderGift()
                pullToRefresh!!.isRefreshing = false
            } else {
                pullToRefresh!!.isRefreshing = false
                fullLay!!.visibility = View.GONE
                hintTextNoInternet!!.visibility = View.VISIBLE
            }
        }
        return view
    }

    fun visible() {
        val c = db1!!.rawQuery("select * from noti_cal where isclose=0", null)
        val notiCount = c.count
        if (notiCount != 0) {
            notifiCount!!.visibility = View.VISIBLE
            if (notiCount <= 9) {
                notifiCount!!.text = "" + notiCount
            } else {
                notifiCount!!.text = "9+"
            }
        } else {
            notifiCount!!.visibility = View.INVISIBLE
        }
        val notiShake: Animation?
        if (notiCount != 0) {
            notiShake = AnimationUtils.loadAnimation(activity, R.anim.noti_shake)
            notifiCount!!.startAnimation(notiShake)
        }
        c.close()
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        item.isChecked = false
        val id = item.itemId
        if (id == R.id.nav_home) {
            drawer!!.closeDrawer(GravityCompat.START)
        } else if (id == R.id.nav_share) {
            val shareBody =
                "If you like to suggest the gift to your friends and relatives, Install this Gift Suggestions app.\n\n Click the below link to download this app:\n https://bit.ly/3LdV0Gj"
            val sharingIntent = Intent(Intent.ACTION_SEND)
            sharingIntent.type = "text/plain"
            sharingIntent.putExtra(Intent.EXTRA_SUBJECT, "Gift Suggestions")
            sharingIntent.putExtra(Intent.EXTRA_TEXT, shareBody)
            startActivity(Intent.createChooser(sharingIntent, "Share Via"))
        } else if (id == R.id.nav_feedback) {
            feedback()

        } else if (id == R.id.nav_rateus) {
            startActivity(
                Intent(
                    Intent.ACTION_VIEW,
                    Uri.parse("https://play.google.com/store/apps/details?id=nithra.gift.suggestion.shop.birthday.marriage")
                )
            )
        } else if (id == R.id.nav_policy) {
            if (context?.let { Utils_Class.isNetworkAvailable(it) } == true) {
                val i = Intent(context, PrivacyPolicy::class.java)
                startActivity(i)
            } else {
                Utils_Class.toast_normal(context, "Please connect to your internet")
            }
        } else if (id == R.id.add_shop_nav) {
            if (context?.let { sharedPreference.getInt(it, "profile") } == 1) {
                val i = Intent(context, ShopAdd::class.java)
                startActivity(i)
            } else if (context?.let { sharedPreference.getInt(it, "profile") } == 2) {
                val i = Intent(context, SellerProfileProductList::class.java)
                startActivity(i)
            } else {
                val i = Intent(context, SellerEntry::class.java)
                startActivity(i)
            }
        }
        drawer!!.closeDrawer(GravityCompat.START)
        return true
    }


    fun feedback() {
        val emailEdt: EditText
        val feedbackEdt: EditText
        val privacy: TextView
        val submitBtn: TextView
        val dialog =
            Dialog(
                requireContext(),
                android.R.style.Theme_DeviceDefault_Dialog_NoActionBar_MinWidth
            )
        dialog.setContentView(R.layout.feed_back)
        dialog.window!!.setBackgroundDrawableResource(android.R.color.transparent)
        dialog.setCanceledOnTouchOutside(false)
        emailEdt = dialog.findViewById(R.id.edit_email)
        feedbackEdt = dialog.findViewById(R.id.editText1)
        submitBtn = dialog.findViewById(R.id.btnSend)
        privacy = dialog.findViewById(R.id.policy)
        privacy.setOnClickListener {
            if (context?.let { it1 -> Utils_Class.isNetworkAvailable(it1) } == true) {
                val intent = Intent(context, PrivacyPolicy::class.java)
                startActivity(intent)
            } else {
                Toast.makeText(context, "please connect to the internet...", Toast.LENGTH_SHORT)
                    .show()
            }
        }
        submitBtn.setOnClickListener(View.OnClickListener {
            var feedback = feedbackEdt.text.toString().trim { it <= ' ' }
            val email = emailEdt.text.toString().trim { it <= ' ' }
            if (feedback == "") {
                Toast.makeText(
                    context,
                    "Please type your feedback or suggestion, Thank you",
                    Toast.LENGTH_SHORT
                ).show()
                return@OnClickListener
            }
            if (context?.let { it1 -> Utils_Class.isNetworkAvailable(it1) } == false) {
                Toast.makeText(context, "please connect to the internet...", Toast.LENGTH_SHORT)
                    .show()
                return@OnClickListener
            }
            try {
                feedback = URLEncoder.encode(feedback, "UTF-8")
            } catch (e: UnsupportedEncodingException) {
                e.printStackTrace()
            }
            val map = HashMap<String, String?>()
            map["type"] = "Gift_Suggestions"
            map["feedback"] = feedback
            map["email"] = email
            map["model"] = Build.MODEL
            map["vcode"] = "" + BuildConfig.VERSION_CODE
            val method = RetrofitClient.retrofit!!.create(Method::class.java)
            val call = method.getAlldata(map)
            call.enqueue(object : Callback<List<Feedback>> {
                override fun onResponse(
                    call: Call<List<Feedback>>,
                    response: Response<List<Feedback>>
                ) {
                    if (response.isSuccessful) {
                        try {
                            dialog.dismiss()
                            Toast.makeText(context, "Feedback sent, Thank you", Toast.LENGTH_SHORT)
                                .show()
                        } catch (e: JSONException) {
                            e.printStackTrace()
                        }
                    }
                }

                override fun onFailure(call: Call<List<Feedback>>, t: Throwable) {
                }
            })
        })

        dialog.show()
    }

    private fun giftOccasion() {
        val map = HashMap<String, String?>()
        map["action"] = "category"
        val retrofitAPI = RetrofitApiClient.retrofit!!.create(
            RetrofitAPI::class.java
        )
        val call = retrofitAPI.gift_occasion(map)
        call.enqueue(object : Callback<ArrayList<Occasion>?> {
            override fun onResponse(
                call: Call<ArrayList<Occasion>?>,
                response: Response<ArrayList<Occasion>?>
            ) {
                if (response.isSuccessful) {
                    giftoccasion!!.addAll(response.body()!!)
                    adapter3!!.notifyDataSetChanged()
                    Utils_Class.mProgress!!.dismiss()
                }
            }

            override fun onFailure(call: Call<ArrayList<Occasion>?>, t: Throwable) {
            }
        })
    }

    private fun genderGift() {
        val map = HashMap<String, String>()
        map["action"] = "gift_for"
        val retrofitAPI = RetrofitApiClient.retrofit!!.create(
            RetrofitAPI::class.java
        )
        val call = retrofitAPI.gift_giftfor(map)
        call.enqueue(object : Callback<ArrayList<GiftFor>?> {
            override fun onResponse(
                call: Call<ArrayList<GiftFor>?>,
                response: Response<ArrayList<GiftFor>?>
            ) {
                if (response.isSuccessful) {
                    giftfor!!.addAll(response.body()!!)
                    adapter2!!.notifyDataSetChanged()
                }
            }

            override fun onFailure(call: Call<ArrayList<GiftFor>?>, t: Throwable) {
            }
        })
    }

    inner class Adapter2(ctx: Context?, var giftfor: ArrayList<GiftFor>) :
        RecyclerView.Adapter<Adapter2.ViewHolder>() {
        private var inflater: LayoutInflater
        var context: Context?

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
                layoutInflater.inflate(R.layout.adapter_2, parent, false)
            return ViewHolder(listItem)
        }

        override fun onBindViewHolder(holder: ViewHolder, position: Int) {
            Glide.with(context!!).load(giftfor[position].peopleLogo)
                .error(R.drawable.ic_gift_default_img)
                .placeholder(R.drawable.ic_gift_default_img)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(holder.slideMat)
            holder.gridText.text = giftfor[position].people
            holder.gender.setOnClickListener {
                val i = Intent(getContext(), ActivitySecond::class.java)
                i.putExtra("title", giftfor[position].people)
                i.putExtra("gender_id", giftfor[position].id)
                startActivity(i)
            }
        }

        override fun getItemCount(): Int {
            return giftfor.size
        }

        inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
            var slideMat: ImageView
            var gridText: TextView
            var gender: LinearLayout

            init {
                slideMat = itemView.findViewById(R.id.imageGrid)
                gridText = itemView.findViewById(R.id.gridText)
                gender = itemView.findViewById(R.id.gender)
            }
        }
    }

    inner class Adapter3(ctx: Context?, private var giftoccasion: ArrayList<Occasion>) :
        RecyclerView.Adapter<Adapter3.ViewHolder>() {
        private var inflater: LayoutInflater
        var context: Context?

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
                layoutInflater.inflate(R.layout.adapter_3, parent, false)
            return ViewHolder(listItem)
        }

        override fun onBindViewHolder(holder: ViewHolder, position: Int) {
            Glide.with(context!!).load(giftoccasion[position].categoryLogo)
                .error(R.drawable.ic_gift_default_img)
                .placeholder(R.drawable.ic_gift_default_img)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(holder.imgSlide)
            holder.gridText.text = giftoccasion[position].category
            holder.category.setOnClickListener {
                val i = Intent(getContext(), ActivitySecond::class.java)
                i.putExtra("title", giftoccasion[position].category)
                i.putExtra("cat_idd", giftoccasion[position].id)
                startActivity(i)
            }
        }

        override fun getItemCount(): Int {
            return giftoccasion.size
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