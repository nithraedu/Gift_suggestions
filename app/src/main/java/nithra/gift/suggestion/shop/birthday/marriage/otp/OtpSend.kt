package nithra.gift.suggestion.shop.birthday.marriage.otp

import android.content.Intent
import android.os.Bundle
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
    private var getotp: TextView? = null
    var name: EditText? = null
    private var gmail: EditText? = null
    private var nameOtp: String? = null
    private var gmailOtp: String? = null
    var sharedPreference = SharedPreference()
    var sendOtp: ArrayList<SendOtppojo>? = null
    var back: ImageView? = null
    var extra: Bundle? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.fragment_send_o_t_p)
        back = findViewById(R.id.back)
        getotp = findViewById(R.id.getotp)
        name = findViewById(R.id.name)
        gmail = findViewById(R.id.gmail)
        sendOtp = ArrayList()
        back!!.setOnClickListener {
            val i = Intent(this@OtpSend, SellerEntry::class.java)
            startActivity(i)
            finish()
        }
        getotp!!.setOnClickListener {
            nameOtp = name!!.text.toString().trim { it <= ' ' }
            gmailOtp = gmail!!.text.toString().trim { it <= ' ' }
            val emailPattern: Regex = "^[A-Za-z](.*)(@)(.+)(\\.)(.+)".toRegex()

            if (nameOtp == "") {
                Utils_Class.toast_center(this@OtpSend, "Please Enter Your Name...")
            } else if (gmailOtp == "") {
                Utils_Class.toast_center(this@OtpSend, "Please Enter Your Email...")
            } else if (!gmailOtp!!.matches(emailPattern)) {
                Toast.makeText(this@OtpSend, "Invalid email address", Toast.LENGTH_SHORT).show()
            } else {
                if (Utils_Class.isNetworkAvailable(this@OtpSend)) {
                    otpGenerate()
                } else {
                    Utils_Class.toast_normal(this@OtpSend, "Please connect to your internet")
                }
            }
            sharedPreference.putString(this@OtpSend, "resend", "" + gmailOtp)
        }
    }

    private fun otpGenerate() {
        Utils_Class.mProgress(this@OtpSend)!!.show()
        val map = HashMap<String, String?>()
        map["action"] = "check_seller"
        map["name"] = nameOtp
        map["gmail"] = gmailOtp
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
                        sendOtp!!.addAll(response.body()!!)
                        sharedPreference.putString(
                            this@OtpSend,
                            "register_otp",
                            "" + sendOtp!![0].otp
                        )
                        sharedPreference.putString(
                            this@OtpSend,
                            "user_id",
                            "" + sendOtp!![0].id
                        )
                        sharedPreference.putString(
                            this@OtpSend,
                            "user_status",
                            "" + sendOtp!![0].userStatus
                        )
                        sharedPreference.putString(
                            this@OtpSend,
                            "user_mail",
                            "" + sendOtp!![0].gmail
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