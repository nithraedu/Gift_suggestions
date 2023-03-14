package nithra.gift.suggestion.shop.birthday.marriage.otp

import android.content.Intent
import android.os.Bundle
import android.view.Window
import android.view.WindowManager
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import nithra.gift.suggestion.shop.birthday.marriage.R
import nithra.gift.suggestion.shop.birthday.marriage.retrofit.RetrofitAPI
import nithra.gift.suggestion.shop.birthday.marriage.retrofit.RetrofitApiClient
import nithra.gift.suggestion.shop.birthday.marriage.retrofit.SendOtppojo
import nithra.gift.suggestion.shop.birthday.marriage.product_shop.SellerEntry
import nithra.gift.suggestion.shop.birthday.marriage.support.SharedPreference
import nithra.gift.suggestion.shop.birthday.marriage.support.Utils_Class
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class OtpSend : AppCompatActivity() {
    var getotp: TextView? = null
    var name: EditText? = null
    var gmail: EditText? = null
    var name_otp: String? = null
    var gmail_otp: String? = null
    var sharedPreference = SharedPreference()
    var send_otp: ArrayList<SendOtppojo>? = null
    var back: ImageView? = null
    var extra: Bundle? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requestWindowFeature(Window.FEATURE_NO_TITLE)
        this.window.setFlags(
            WindowManager.LayoutParams.FLAG_FULLSCREEN,
            WindowManager.LayoutParams.FLAG_FULLSCREEN
        )
        setContentView(R.layout.fragment_send_o_t_p)
        back = findViewById(R.id.back)
        getotp = findViewById(R.id.getotp)
        name = findViewById(R.id.name)
        gmail = findViewById(R.id.gmail)
        send_otp = ArrayList()
        back!!.setOnClickListener({
            val i = Intent(this@OtpSend, SellerEntry::class.java)
            startActivity(i)
            finish()
        })
        getotp!!.setOnClickListener({
            name_otp = name!!.getText().toString().trim { it <= ' ' }
            gmail_otp = gmail!!.getText().toString().trim { it <= ' ' }
            // emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+"
            val emailPattern: Regex = "^[A-Za-z](.*)([@]{1})(.{1,})(\\.)(.{1,})".toRegex()

            if (name_otp == "") {
                Utils_Class.toast_center(this@OtpSend, "Please Enter Your Name...")
            } else if (gmail_otp == "") {
                Utils_Class.toast_center(this@OtpSend, "Please Enter Your Email...")
            } else if (!gmail_otp!!.matches(emailPattern)) {
                Toast.makeText(this@OtpSend, "Invalid email address", Toast.LENGTH_SHORT).show()
            } else {
                if (Utils_Class.isNetworkAvailable(this@OtpSend)) {
                    otp_generate()
                } else {
                    Utils_Class.toast_normal(this@OtpSend, "Please connect to your internet")
                }
            }
            sharedPreference.putString(this@OtpSend, "resend", "" + gmail_otp)
        })
    }

    fun otp_generate() {
        Utils_Class.mProgress(this@OtpSend, "Loading please wait...", false)!!.show()
        val map = HashMap<String, String?>()
        map["action"] = "check_seller"
        map["name"] = name_otp
        map["gmail"] = gmail_otp
        val retrofitAPI = RetrofitApiClient.retrofit!!.create(
            RetrofitAPI::class.java
        )
        val call = retrofitAPI.getotp(map)
        call.enqueue(object : Callback<ArrayList<SendOtppojo>> {
            override fun onResponse(
                call: Call<ArrayList<SendOtppojo>>,
                response: Response<ArrayList<SendOtppojo>>
            ) {
                if (response.isSuccessful) {
                    if (response.body()!![0].status == "Success") {
                        send_otp!!.addAll(response.body()!!)
                        sharedPreference.putString(
                            this@OtpSend,
                            "register_otp",
                            "" + send_otp!![0].otp
                        )
                        sharedPreference.putString(
                            this@OtpSend,
                            "user_id",
                            "" + send_otp!![0].id
                        )
                        sharedPreference.putString(
                            this@OtpSend,
                            "user_status",
                            "" + send_otp!![0].userStatus
                        )
                        sharedPreference.putString(
                            this@OtpSend,
                            "user_mail",
                            "" + send_otp!![0].gmail
                        )
                        val i = Intent(this@OtpSend, OtpVerify::class.java)
                        startActivity(i)
                    }
                    Utils_Class.mProgress!!.dismiss()
                }
            }

            override fun onFailure(call: Call<ArrayList<SendOtppojo>>, t: Throwable) {
            }
        })
    }
}