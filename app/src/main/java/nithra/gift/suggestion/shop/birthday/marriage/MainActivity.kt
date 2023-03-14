package nithra.gift.suggestion.shop.birthday.marriage

import android.Manifest
import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.content.IntentSender.SendIntentException
import android.content.SharedPreferences
import android.content.pm.PackageManager
import android.net.Uri
import android.os.*
import android.view.MenuItem
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.AppCompatButton
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import androidx.viewpager2.widget.ViewPager2
import com.android.installreferrer.api.InstallReferrerClient
import com.android.installreferrer.api.InstallReferrerStateListener
import com.google.android.material.navigation.NavigationBarView
import com.google.android.play.core.appupdate.AppUpdateInfo
import com.google.android.play.core.appupdate.AppUpdateManager
import com.google.android.play.core.appupdate.AppUpdateManagerFactory
import com.google.android.play.core.install.model.AppUpdateType
import com.google.android.play.core.install.model.UpdateAvailability
import com.google.android.play.core.review.ReviewInfo
import com.google.android.play.core.review.ReviewManagerFactory
import com.google.android.play.core.tasks.Task
import nithra.gift.suggestion.shop.birthday.marriage.feedback.Feedback
import nithra.gift.suggestion.shop.birthday.marriage.feedback.Method
import nithra.gift.suggestion.shop.birthday.marriage.feedback.RetrofitClient
import nithra.gift.suggestion.shop.birthday.marriage.fragment.Home
import nithra.gift.suggestion.shop.birthday.marriage.retrofit.Androidid
import nithra.gift.suggestion.shop.birthday.marriage.retrofit.RetrofitAPI
import nithra.gift.suggestion.shop.birthday.marriage.retrofit.RetrofitApiClient
import nithra.gift.suggestion.shop.birthday.marriage.support.SharedPreference
import nithra.gift.suggestion.shop.birthday.marriage.support.Utils_Class
import org.json.JSONException
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.UnsupportedEncodingException
import java.net.URLEncoder
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*
import java.util.concurrent.TimeUnit

class MainActivity : AppCompatActivity(), InstallReferrerStateListener,
    NavigationBarView.OnItemSelectedListener {
    private var viewpager2: ViewPager2? = null
    private var fragAdapter: FragAdapter? = null
    private var pref: SharedPreferences? = null
    var sharedPreference = SharedPreference()
    var andId: ArrayList<Androidid>? = null
    private var appUpdateManager: AppUpdateManager? = null
    private var mReferrerClient: InstallReferrerClient? = null
    var source = ""
    var medium = ""
    var comp = ""
    var a = 0
    private var spa: SharedPreferences? = null
    private var doubleBackToExitPressedOnce = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        pref = getSharedPreferences("register", MODE_PRIVATE)

        if (pref!!.getInt("p_p", 0) == 0) {
            privacy()
        }



        viewpager2 = findViewById(R.id.viewpager2)
        viewpager2!!.isUserInputEnabled = false
        fragAdapter = FragAdapter(this)
        fragAdapter!!.addFragment(Home())
        andId = ArrayList()
        if (sharedPreference.getInt(applicationContext, "android_id_check") == 0) {
            android()
        }
        spa = applicationContext.getSharedPreferences("MyPref", MODE_PRIVATE)


        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU && sp.getInt(
                this,
                "permission"
            ) == 0
        ) {
            sp.putInt(this, "permission", 1)
            requestPermissions(arrayOf(Manifest.permission.POST_NOTIFICATIONS), 113)
        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU && sp.getInt(
                this,
                "permission"
            ) == 10
        ) {
            sp.putInt(this, "permission", 1)
            requestPermissions(arrayOf(Manifest.permission.POST_NOTIFICATIONS), 113)
        } else {
            sp.putInt(this, "permission", sp.getInt(this, "permission") + 1)
        }


        //in_app cods start
        appUpdateManager = AppUpdateManagerFactory.create(this@MainActivity)
        val appUpdateInfoTask = appUpdateManager!!.appUpdateInfo
        appUpdateInfoTask.addOnSuccessListener { appUpdateInfo: AppUpdateInfo ->
            if (appUpdateInfo.updateAvailability() == UpdateAvailability.UPDATE_AVAILABLE
                && appUpdateInfo.isUpdateTypeAllowed(AppUpdateType.IMMEDIATE)
            ) {
                try {
                    appUpdateManager!!.startUpdateFlowForResult(
                        appUpdateInfo,
                        AppUpdateType.IMMEDIATE,
                        this@MainActivity,
                        200
                    )
                } catch (e: SendIntentException) {
                    e.printStackTrace()
                }
            }
        }
        val cdate = Calendar.getInstance().time
        val df = SimpleDateFormat("dd/MMM/yyyy")
        val currentdate = df.format(cdate)
        val cal = Calendar.getInstance()
        cal.add(Calendar.DATE, -1)
        val yesterdaydate = df.format(cal.time)
        if (sp.getInt(this@MainActivity, currentdate) == 0) {
            sp.putInt(this@MainActivity, currentdate, 1)
            if (sp.getInt(this@MainActivity, yesterdaydate) != 0) {
                sp.removeInt(this@MainActivity, yesterdaydate)
            }
        } else {
            if (sp.getString(this@MainActivity, "review_time") == "") {
                val currentDate = Calendar.getInstance()
                val currentdateMills = currentDate.timeInMillis
                sp.putString(this@MainActivity, "review_time", "" + currentdateMills)
            }
            appUpdateManager()
        }
        //in_app cods end

        viewpager2!!.adapter = fragAdapter
    }

    fun android() {
        val map = HashMap<String, String?>()
        map["action"] = "check_android_id"
        map["android_id"] = Utils_Class.androidId(this)
        val retrofitAPI = RetrofitApiClient.retrofit!!.create(
            RetrofitAPI::class.java
        )
        val call = retrofitAPI.androidid(map)
        call.enqueue(object : Callback<ArrayList<Androidid>> {
            override fun onResponse(
                call: Call<ArrayList<Androidid>>,
                response: Response<ArrayList<Androidid>>
            ) {
                if (response.isSuccessful) {
                    if (response.body()!![0].status == "success") {
                        andId!!.addAll(response.body()!!)
                        sharedPreference.putInt(applicationContext, "android_id_check", 1)
                        andId!![0].userId?.let {
                            sharedPreference.putString(
                                applicationContext,
                                "android_userid",
                                it
                            )
                        }
                    }
                }
            }

            override fun onFailure(call: Call<ArrayList<Androidid>>, t: Throwable) {
            }
        })
    }

    fun feedback() {
        val emailEdt: EditText
        val feedbackEdt: EditText
        val privacy: TextView
        val submitBtn: TextView
        val dialog = Dialog(
            this@MainActivity,
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
            if (Utils_Class.isNetworkAvailable(this@MainActivity)) {
                val intent = Intent(this@MainActivity, PrivacyPolicy::class.java)
                startActivity(intent)
            } else {
                Toast.makeText(
                    this@MainActivity,
                    "please connect to the internet...",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
        submitBtn.setOnClickListener(View.OnClickListener {
            var feedback = feedbackEdt.text.toString().trim { it <= ' ' }
            val email = emailEdt.text.toString().trim { it <= ' ' }
            if (feedback == "") {
                Toast.makeText(
                    this@MainActivity,
                    "Please type your feedback or suggestion, Thank you",
                    Toast.LENGTH_SHORT
                ).show()
                return@OnClickListener
            }
            if (!Utils_Class.isNetworkAvailable(this@MainActivity)) {
                Toast.makeText(
                    this@MainActivity,
                    "please connect to the internet...",
                    Toast.LENGTH_SHORT
                ).show()
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
                            Toast.makeText(
                                this@MainActivity,
                                "Feedback sent, Thank you",
                                Toast.LENGTH_SHORT
                            ).show()
                        } catch (e: JSONException) {
                            e.printStackTrace()
                        }
                    }
                }

                override fun onFailure(call: Call<List<Feedback>>, t: Throwable) {
                }
            })
        })
        dialog.setOnDismissListener {
            if (a == 1) {
                finish()

            }
        }
        dialog.show()
    }

    private fun privacy() {
        val dialog = Dialog(
            this@MainActivity,
            android.R.style.Theme_DeviceDefault_Dialog_NoActionBar_MinWidth
        )
        dialog.setContentView(R.layout.dialog_privacy_policy)
        dialog.window!!.setBackgroundDrawableResource(android.R.color.transparent)
        //dialog.setCanceledOnTouchOutside(false);
        dialog.setCancelable(false)
        dialog.show()
        val head = dialog.findViewById<TextView>(R.id.head)
        val firstLine = dialog.findViewById<TextView>(R.id.first_line)
        val b1 = dialog.findViewById<TextView>(R.id.b1)
        val b2 = dialog.findViewById<TextView>(R.id.b2)
        head.text = "Privacy & Terms"
        firstLine.text = """
               Thanks for downloading or updating Gift Suggestions.
               
              
               """.trimIndent() + "By clicking privacy policy tab you can read our privacy policy and agree to the terms of privacy policy to continue using Gift Suggestions."
        b1.text = "Privacy Policy"
        b2.text = "Agree & Continue"
        b1.setOnClickListener {
            if (Utils_Class.isNetworkAvailable(this@MainActivity)) {
                val intent = Intent(this@MainActivity, PrivacyPolicy::class.java)
                startActivity(intent)
            } else {
                Toast.makeText(
                    this@MainActivity,
                    "Please connect the internet...",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
        b2.setOnClickListener {
            val editer = pref!!.edit()
            editer.putInt("p_p", 1)
            editer.apply()
            dialog.dismiss()
        }
    }

    override fun onBackPressed() {


        val drawerLayout = findViewById<DrawerLayout>(R.id.drawer_layout)
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START)
        } else {
            if (sharedPreference.getInt(this, "ratecheckval") == 0) {

                val dialog = Dialog(
                    this@MainActivity,
                    android.R.style.Theme_DeviceDefault_Dialog_NoActionBar_MinWidth
                )
                dialog.setContentView(R.layout.backpress)
                dialog.window!!.setBackgroundDrawableResource(android.R.color.transparent)
                dialog.setCancelable(false)
                val yes: Button = dialog.findViewById(R.id.yes)
                val no: Button = dialog.findViewById(R.id.no)
                val yesratedialog =
                    Dialog(
                        this@MainActivity,
                        android.R.style.Theme_Translucent_NoTitleBar_Fullscreen
                    )
                yesratedialog.setContentView(R.layout.rate1)
                val yesbut1: AppCompatButton = yesratedialog.findViewById(R.id.button2)
                val nobut1: AppCompatButton = yesratedialog.findViewById(R.id.button1)
                yesbut1.setOnClickListener {
                    try {
                        sharedPreference.putInt(applicationContext, "ratecheckval", 1)
                        val appPackageName = packageName
                        val marketIntent =
                            Intent(
                                Intent.ACTION_VIEW,
                                Uri.parse("market://details?id=$appPackageName")
                            )
                        startActivity(marketIntent)
                    } catch (_: Exception) {
                    }
                    yesratedialog.dismiss()
                    finish()
                }
                nobut1.setOnClickListener {
                    sharedPreference.putInt(applicationContext, "ratecheckval", 1)
                    if (dialog.isShowing) {
                        dialog.dismiss()
                    }
                    a = 1
                    feedback()
                }
                dialog.show()

                yesratedialog.setOnDismissListener {
                    finish()

                }

                yes.setOnClickListener {
                    sharedPreference.putInt(applicationContext, "ratecheckval", 1)
                    Utils_Class.date_put(this@MainActivity, "rate_date", 90)
                    yesratedialog.show()
                    dialog.dismiss()
                }
                no.setOnClickListener {
                    dialog.dismiss()
                }
            } else {
                if (doubleBackToExitPressedOnce) {
                    super.onBackPressed()
                    return
                }

                this.doubleBackToExitPressedOnce = true
                Toast.makeText(this, "Please click again to exit", Toast.LENGTH_SHORT).show()

                Handler(Looper.getMainLooper()).postDelayed({
                    doubleBackToExitPressedOnce = false
                }, 2000)

            }
        }
    }


    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        return false
    }


    inner class FragAdapter(fragmentActivity: FragmentActivity) :
        FragmentStateAdapter(fragmentActivity) {
        private val fragmentList = ArrayList<Fragment>()
        override fun createFragment(position: Int): Fragment {
            return fragmentList[position]
        }

        fun addFragment(fragment: Fragment) {
            fragmentList.add(fragment)
        }

        override fun getItemCount(): Int {
            return fragmentList.size
        }
    }

    //in_app cods start
    private fun appUpdateManager() {
        appUpdateManager = AppUpdateManagerFactory.create(this@MainActivity)
        val appUpdateInfoTask = appUpdateManager!!.appUpdateInfo
        appUpdateInfoTask.addOnSuccessListener { appUpdateInfo: AppUpdateInfo ->
            if (appUpdateInfo.updateAvailability() == UpdateAvailability.UPDATE_AVAILABLE
                && appUpdateInfo.isUpdateTypeAllowed(AppUpdateType.IMMEDIATE)
            ) {
                try {
                    appUpdateManager!!.startUpdateFlowForResult(
                        appUpdateInfo,
                        AppUpdateType.IMMEDIATE,
                        this@MainActivity,
                        200
                    )
                } catch (e: SendIntentException) {
                    e.printStackTrace()
                }
            } else {
                if (sp.getString(this@MainActivity, "review_complete") == "") {
                    if (sp.getString(this@MainActivity, "review_time") != "") {
                        val beforeDate = sp.getString(this@MainActivity, "review_time")
                        val currentDate = Calendar.getInstance()
                        val currentdateMills = currentDate.timeInMillis
                        val sdf = SimpleDateFormat("dd/MM/yyyy")
                        val stringCurrentDate = sdf.format(currentdateMills)
                        val stringBeforeDate = sdf.format(beforeDate)
                        var timediff: Long = 0
                        try {
                            timediff = TimeUnit.DAYS.convert(
                                sdf.parse(stringCurrentDate).time - sdf.parse(stringBeforeDate).time,
                                TimeUnit.MILLISECONDS
                            )
                        } catch (e1: ParseException) {
                            e1.printStackTrace()
                        }
                        if (timediff.toInt() >= 10) {
                            if (Utils_Class.isNetworkAvailable(this@MainActivity)) {
                                inappReviewDialog()
                            }
                        }
                    }
                }
            }
        }
    }

    private fun inappReviewDialog() {
        val manager = ReviewManagerFactory.create(this@MainActivity)
        val request = manager.requestReviewFlow()
        request.addOnCompleteListener { task: Task<ReviewInfo?> ->
            if (task.isSuccessful) {
                // We can get the ReviewInfo object
                val reviewInfo = task.result
                val flow = manager.launchReviewFlow(this@MainActivity, reviewInfo)
                flow.addOnCompleteListener {
                    sp.putString(
                        this@MainActivity,
                        "review_complete",
                        "review_completed"
                    )
                }
            }
        }
    }

    override fun onInstallReferrerSetupFinished(responseCode: Int) {
        when (responseCode) {
            InstallReferrerClient.InstallReferrerResponse.OK -> {
                sp.putInt(this@MainActivity, "referrerCheck", 1)

                /*get response from referrer*/
                try {
                    val response = mReferrerClient!!.installReferrer
                    if (response != null) {
                        val referrerUrl = response.installReferrer
                        if (referrerUrl != null) {
                            if (referrerUrl.isNotEmpty()) {
                                val referrerList =
                                    referrerUrl.split("&".toRegex()).dropLastWhile { it.isEmpty() }
                                        .toTypedArray()
                                var i = 0
                                while (i < referrerList.size) {
                                    when (i) {
                                        0 -> {
                                            source =
                                                referrerList[i].substring(referrerList[i].indexOf("=") + 1)
                                        }
                                        1 -> {
                                            medium =
                                                referrerList[i].substring(referrerList[i].indexOf("=") + 1)
                                        }
                                        2 -> {
                                            comp =
                                                referrerList[i].substring(referrerList[i].indexOf("=") + 1)
                                        }
                                    }
                                    i++
                                }
                            }
                            /*sent data to server*/
                            val checkUpdate: Thread = object : Thread() {
                                override fun run() {
                                    try {
                                        send(this@MainActivity, source, medium, comp)
                                    } catch (_: Exception) {
                                    }
                                }
                            }
                            checkUpdate.start()
                        }
                    }
                } catch (e: RemoteException) {
                    e.printStackTrace()
                }
            }
            InstallReferrerClient.InstallReferrerResponse.FEATURE_NOT_SUPPORTED -> println("FEATURE_NOT_SUPPORTED")
            InstallReferrerClient.InstallReferrerResponse.SERVICE_UNAVAILABLE -> println("SERVICE_DISCONNECTED")               // Connection could not be established
            InstallReferrerClient.InstallReferrerResponse.SERVICE_DISCONNECTED -> println("SERVICE_DISCONNECTED")
            InstallReferrerClient.InstallReferrerResponse.DEVELOPER_ERROR -> println("DEVELOPER_ERROR")
        }
        mReferrerClient!!.endConnection()
    }

    override fun onInstallReferrerServiceDisconnected() {
        mReferrerClient!!.startConnection(this)
    }

    fun send(context: Context?, utm: String?, utm1: String?, utm2: String?) {
        val postDataParams = JSONObject()
        try {
            postDataParams.put("app", "Gift Suggestions")
            postDataParams.put("ref", utm)
            postDataParams.put("mm", utm1)
            postDataParams.put("cn", utm2)
            postDataParams.put("email", context?.let { Utils_Class.androidId(it) })
        } catch (e: JSONException) {
            e.printStackTrace()
        }
    } //in_app cods end

    companion object {
        var sp = SharedPreference()
    }

    @RequiresApi(api = Build.VERSION_CODES.TIRAMISU)
    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == 113) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                sp.putInt(applicationContext, "permission", 1)
            }
        }
    }
}