package nithra.gift.suggestion.shop.birthday.marriage.Fragment

import android.app.Dialog
import android.content.Context
import android.content.Context.MODE_PRIVATE
import android.content.Intent
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteDatabase.openOrCreateDatabase
import android.net.ConnectivityManager
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
import androidx.core.content.ContextCompat.getSystemService
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout.OnRefreshListener
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.google.android.material.navigation.NavigationView
import com.google.gson.Gson
import nithra.gift.suggestion.shop.birthday.marriage.*
import nithra.gift.suggestion.shop.birthday.marriage.Feedback.Feedback
import nithra.gift.suggestion.shop.birthday.marriage.Feedback.Method
import nithra.gift.suggestion.shop.birthday.marriage.Feedback.RetrofitClient
import nithra.gift.suggestion.shop.birthday.marriage.Notification.NotificationView
import nithra.gift.suggestion.shop.birthday.marriage.Otp.ShopAdd
import nithra.gift.suggestion.shop.birthday.marriage.Retrofit.GiftFor
import nithra.gift.suggestion.shop.birthday.marriage.Retrofit.Occasion
import nithra.gift.suggestion.shop.birthday.marriage.Retrofit.RetrofitAPI
import nithra.gift.suggestion.shop.birthday.marriage.Retrofit.RetrofitApiClient
import org.json.JSONArray
import org.json.JSONException
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.UnsupportedEncodingException
import java.net.URLEncoder

class Home : Fragment(), NavigationView.OnNavigationItemSelectedListener {
    var drawer: DrawerLayout? = null
    var giftfor: ArrayList<GiftFor>? = null
    var giftoccasion: ArrayList<Occasion>? = null
    var adapter2: Adapter2? = null
    var adapter3: Adapter3? = null
    var notification: LinearLayout? = null
    var profile_view: LinearLayout? = null
    var favourite: LinearLayout? = null
    var sharedPreference = SharedPreference()
    var code: TextView? = null
    var name: TextView? = null
    var versionCode = BuildConfig.VERSION_CODE
    var versionName = BuildConfig.VERSION_NAME
    var pullToRefresh: SwipeRefreshLayout? = null
    var hint_text_no_internet: TextView? = null
    var full_lay: LinearLayout? = null
    var a = 0
    var notifi_count: TextView? = null
    var db1: SQLiteDatabase? = null

    override fun onCreate(savedInstanceState: Bundle?) { super.onCreate(savedInstanceState)
    }

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
        notifi_count = view.findViewById(R.id.notifi_count)
        profile_view = view.findViewById(R.id.profile_view)
        favourite = view.findViewById(R.id.favourite)
        hint_text_no_internet = view.findViewById(R.id.tv_hinds)
        full_lay = view.findViewById(R.id.full_lay)
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
                    + "bm VARCHAR,ntype VARCHAR,url VARCHAR);")


        favourite!!.setOnClickListener(View.OnClickListener {
            val intent = Intent(context, Fav_class::class.java)
            startActivity(intent)
        })
        notification!!.setOnClickListener(View.OnClickListener {
            val intent = Intent(context, NotificationView::class.java)
            startActivity(intent)
        })
        (activity as AppCompatActivity?)!!.setSupportActionBar(toolbar)
        val toggle = ActionBarDrawerToggle(activity, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close)
        drawer!!.addDrawerListener(toggle)
        toggle.syncState()
        (activity as AppCompatActivity?)!!.supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        (activity as AppCompatActivity?)!!.supportActionBar!!.setHomeButtonEnabled(true)
        (activity as AppCompatActivity?)!!.supportActionBar!!.setDisplayShowTitleEnabled(true)
        (activity as AppCompatActivity?)!!.supportActionBar!!.setHomeAsUpIndicator(R.drawable.ic_menu_navigate)
        val navigationView = view.findViewById<NavigationView>(R.id.nav_mm_view)
        navigationView.setNavigationItemSelectedListener { item: MenuItem -> onNavigationItemSelected(item)
        }
        val v = navigationView.inflateHeaderView(R.layout.header)
        code = v.findViewById(R.id.code)
        name = v.findViewById(R.id.name)
        code!!.setText("" + versionCode)
        name!!.setText(versionName)
        val gridLayoutManager2 = GridLayoutManager(context, 1, GridLayoutManager.HORIZONTAL, false)
        list.layoutManager = gridLayoutManager2
        val gridLayoutManager = GridLayoutManager(context, 3, GridLayoutManager.VERTICAL, false)
        list2.layoutManager = gridLayoutManager
        if (context?.let { Utils_Class.isNetworkAvailable(it) } == true) {
            Utils_Class.mProgress(context, "Loading please wait...", false)!!.show()
            hint_text_no_internet!!.visibility = View.GONE

        } else {
            Utils_Class.mProgress(context, "Please connect your internet...", false)
            full_lay!!.visibility = View.GONE
            hint_text_no_internet!!.setVisibility(View.VISIBLE)
        }
        adapter2 = Adapter2(context, giftfor!!)
        list.adapter = adapter2
        adapter3 = Adapter3(context, giftoccasion!!)
        list2.adapter = adapter3

        gift_occasion()
        gender_gift()



        pullToRefresh!!.setOnRefreshListener(OnRefreshListener {
            if (context?.let { Utils_Class.isNetworkAvailable(it) } == true) {
                hint_text_no_internet!!.visibility = View.GONE
                full_lay!!.visibility = View.VISIBLE

                giftoccasion!!.clear()
                giftfor!!.clear()
                gift_occasion()
                gender_gift()
                pullToRefresh!!.setRefreshing(false)
            } else {
                pullToRefresh!!.setRefreshing(false)
                full_lay!!.visibility = View.GONE
                hint_text_no_internet!!.setVisibility(View.VISIBLE)
            }
        })
        return view
    }


    fun visible() {
        val c = db1!!.rawQuery("select * from noti_cal where isclose=0", null)
        val noti_count = c.count
        if (noti_count != 0) {
            notifi_count!!.visibility = View.VISIBLE
            if (noti_count <= 9) {
                notifi_count!!.text = "" + noti_count
            } else {
                notifi_count!!.text = "9+"
            }
        } else {
            notifi_count!!.visibility = View.INVISIBLE
        }
        var noti_shake: Animation? = null
        if (noti_count != 0) {
            noti_shake = AnimationUtils.loadAnimation(activity, R.anim.noti_shake)
            notifi_count!!.startAnimation(noti_shake)
        }
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        item.isChecked = false
        val id = item.itemId
        if (id == R.id.nav_home) {
            drawer!!.closeDrawer(GravityCompat.START)
        } else if (id == R.id.nav_share) {
            val shareBody = "Install this Gift Suggestions app.\n\n Click the below link to download this app:\n https://rb.gy/6gbzgq"
            val sharingIntent = Intent(Intent.ACTION_SEND)
            sharingIntent.type = "text/plain"
            sharingIntent.putExtra(Intent.EXTRA_SUBJECT, "Gift Suggestions")
            sharingIntent.putExtra(Intent.EXTRA_TEXT, shareBody)
            startActivity(Intent.createChooser(sharingIntent, "Share Via"))
        } else if (id == R.id.nav_feedback) {
            feedback()

        } else if (id == R.id.nav_rateus) {
            startActivity(Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=nithra.gift.suggestion.shop.birthday.marriage")))
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
        val email_edt: EditText
        val feedback_edt: EditText
        val privacy: TextView
        val submit_btn: TextView
        val dialog =
            Dialog(
                requireContext(),
                android.R.style.Theme_DeviceDefault_Dialog_NoActionBar_MinWidth
            )
        dialog.setContentView(R.layout.feed_back)
        dialog.window!!.setBackgroundDrawableResource(android.R.color.transparent)
        dialog.setCanceledOnTouchOutside(false)
        email_edt = dialog.findViewById(R.id.edit_email)
        feedback_edt = dialog.findViewById(R.id.editText1)
        submit_btn = dialog.findViewById(R.id.btnSend)
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
        submit_btn.setOnClickListener(View.OnClickListener {
            var feedback = feedback_edt.text.toString().trim { it <= ' ' }
            val email = email_edt.text.toString().trim { it <= ' ' }
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
            map["type"] = "Gift Suggestions"
            map["feedback"] = feedback
            map["email"] = email
            map["model"] = Build.MODEL
            map["vcode"] = ""+BuildConfig.VERSION_CODE
            val method = RetrofitClient.retrofit!!.create(Method::class.java)
            val call = method.getAlldata(map)
            call.enqueue(object : Callback<List<Feedback>> {
                override fun onResponse(
                    call: Call<List<Feedback>>,
                    response: Response<List<Feedback>>
                ) {
                    if (response.isSuccessful) {
                        try {
                            val feedbacks = response.body()!!
                            println("======response feedbacks:" + feedbacks[0].status)
                            val jsonArray = JSONArray(Gson().toJson(response.body()))
                            val jsonObject = jsonArray.getJSONObject(0)
                            println("======response feedbacks:" + jsonObject.getString("status"))
                            dialog.dismiss()
                            Toast.makeText(context, "Feedback sent, Thank you", Toast.LENGTH_SHORT)
                                .show()
                        } catch (e: JSONException) {
                            println("======response e:$e")
                            e.printStackTrace()
                        }
                    }
                    println("======response :$response")
                }

                override fun onFailure(call: Call<List<Feedback>>, t: Throwable) {
                    println("======response t:$t")
                }
            })
        })
        /* dialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialogInterface) {
                if (a == 1) {
                    getActivity().finish();
                    Intent intent = new Intent(getContext(), ExitScreen.class);
                    startActivity(intent);
                }
            }
        });*/
        dialog.show()
    }

    fun gift_occasion() {
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
                    val result = Gson().toJson(response.body())
                    println("======response result:$result")
                    giftoccasion!!.addAll(response.body()!!)
                    adapter3!!.notifyDataSetChanged()
                    Utils_Class.mProgress!!.dismiss()
                }
                println("======response :$response")
            }

            override fun onFailure(call: Call<ArrayList<Occasion>?>, t: Throwable) {
                println("======response t:$t")
            }
        })
    }

    fun gender_gift() {
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
                    val result = Gson().toJson(response.body())
                    println("======response result:$result")
                    giftfor!!.addAll(response.body()!!)
                    adapter2!!.notifyDataSetChanged()
                }
                println("======response :$response")
            }

            override fun onFailure(call: Call<ArrayList<GiftFor>?>, t: Throwable) {
                println("======response t:$t")
            }
        })
    }

    inner class Adapter2(ctx: Context?, var giftfor: ArrayList<GiftFor>) :
        RecyclerView.Adapter<Adapter2.ViewHolder>() {
        var inflater: LayoutInflater
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
                .into(holder.slide_mat)
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
            var slide_mat: ImageView
            var gridText: TextView
            var gender: LinearLayout

            init {
                slide_mat = itemView.findViewById(R.id.imageGrid)
                gridText = itemView.findViewById(R.id.gridText)
                gender = itemView.findViewById(R.id.gender)
            }
        }
    }

    inner class Adapter3(ctx: Context?, var giftoccasion: ArrayList<Occasion>) :
        RecyclerView.Adapter<Adapter3.ViewHolder>() {
        var inflater: LayoutInflater
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
                .into(holder.img_slide)
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