package nithra.gift.suggestion.shop.birthday.marriage.otp

import android.content.Intent
import android.os.Bundle
import android.os.CountDownTimer
import android.text.Editable
import android.text.TextWatcher
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import nithra.gift.suggestion.shop.birthday.marriage.product_shop.ShopAdd
import nithra.gift.suggestion.shop.birthday.marriage.R
import nithra.gift.suggestion.shop.birthday.marriage.retrofit.CheckOtp
import nithra.gift.suggestion.shop.birthday.marriage.retrofit.RetrofitAPI
import nithra.gift.suggestion.shop.birthday.marriage.retrofit.RetrofitApiClient
import nithra.gift.suggestion.shop.birthday.marriage.retrofit.SendOtppojo
import nithra.gift.suggestion.shop.birthday.marriage.product_shop.SellerProfileProductList
import nithra.gift.suggestion.shop.birthday.marriage.support.SharedPreference
import nithra.gift.suggestion.shop.birthday.marriage.support.Utils_Class
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class OtpVerify : AppCompatActivity() {
    private var enterotp: TextView? = null
    private var editEmail: TextView? = null
    var enterOtp: EditText? = null
    var otp1: EditText? = null
    var otp2: EditText? = null
    var otp3: EditText? = null
    var otp4: EditText? = null
    private var verify: String? = null
    private var editOtp: String? = null
    var sharedPreference = SharedPreference()
    var sendOtp: ArrayList<SendOtppojo>? = null
    private var edit: ImageView? = null
    var back: ImageView? = null
    var extra: Bundle? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.fragment_enter_o_t_p)
        val ttv = findViewById<TextView>(R.id.timer)
        sendOtp = ArrayList()
        back = findViewById(R.id.back)
        enterotp = findViewById(R.id.enterotp)
        editEmail = findViewById(R.id.edit_email)
        otp1 = findViewById(R.id.otp_1)
        otp2 = findViewById(R.id.otp_2)
        otp3 = findViewById(R.id.otp_3)
        otp4 = findViewById(R.id.otp_4)
        edit = findViewById(R.id.edit)
        enterOtp = findViewById(R.id.enter_otp)
        back!!.setOnClickListener { finish() }
        editEmail!!.text = "" + sharedPreference.getString(this@OtpVerify, "resend")
        object : CountDownTimer(120000, 1000) {
            override fun onTick(millisUntilFinished: Long) {
                ttv.text = "If you didn't receive a otp? " + millisUntilFinished / 1000
            }

            override fun onFinish() {
                ttv.text = "If you didn't receive a otp? Resend"
                sharedPreference.putString(this@OtpVerify, "register_otp", "" + 0)
            }
        }.start()
        ttv.setOnClickListener {
            if (Utils_Class.isNetworkAvailable(this@OtpVerify)) {
                otpGenerate()
            } else {
                Utils_Class.toast_normal(this@OtpVerify, "Please connect to your internet")
            }
            object : CountDownTimer(120000, 1000) {
                override fun onTick(millisUntilFinished: Long) {
                    ttv.text = "If you didn't receive a otp? " + millisUntilFinished / 1000
                }

                override fun onFinish() {
                    ttv.text = "If you didn't receive a otp? Resend"
                }
            }.start()
        }
        edit!!.setOnClickListener {
            finish()
        }
        otp1!!.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {}
            override fun afterTextChanged(s: Editable) {
                if (s.isNotEmpty()) {
                    otp2!!.requestFocus()
                }
            }
        })
        otp2!!.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {}
            override fun afterTextChanged(s: Editable) {
                if (s.isNotEmpty()) {
                    otp3!!.requestFocus()
                } else {
                    otp1!!.requestFocus()
                }
            }
        })
        otp3!!.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {}
            override fun afterTextChanged(s: Editable) {
                if (s.isNotEmpty()) {
                    otp4!!.requestFocus()
                } else {
                    otp2!!.requestFocus()
                }
            }
        })
        otp4!!.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {}
            override fun afterTextChanged(s: Editable) {
                if (s.isNotEmpty()) {
                    otp4!!.requestFocus()
                } else {
                    otp3!!.requestFocus()
                }
            }
        })
        enterotp!!.setOnClickListener {
            editOtp =
                otp1!!.text.toString().trim { it <= ' ' } + otp2!!.text.toString()
                    .trim { it <= ' ' } + otp3!!.text.toString()
                    .trim { it <= ' ' } + otp4!!.text.toString().trim { it <= ' ' }
            if (otp1!!.text.toString() == "" && otp2!!.text
                    .toString() == "" && otp3!!.text.toString() == "" && otp4!!.text
                    .toString() == ""
            ) {
                Utils_Class.toast_center(this@OtpVerify, "Enter your otp")
            } else {
                if (Utils_Class.isNetworkAvailable(this@OtpVerify)) {
                    otpVerify()
                } else {
                    Utils_Class.toast_normal(this@OtpVerify, "Please connect to your internet")
                }
            }
        }
    }

    private fun otpVerify() {
        Utils_Class.mProgress(this@OtpVerify)!!.show()
        verify = editOtp!!.trim { it <= ' ' }
        val map = HashMap<String, String?>()
        map["action"] = "check_otp"
        map["user_id"] = sharedPreference.getString(this@OtpVerify, "user_id")
        map["otp"] = verify!!
        val retrofitAPI = RetrofitApiClient.retrofit!!.create(
            RetrofitAPI::class.java
        )
        val call = retrofitAPI.checkotp(map)
        call.enqueue(object : Callback<ArrayList<CheckOtp>> {
            override fun onResponse(
                call: Call<ArrayList<CheckOtp>>,
                response: Response<ArrayList<CheckOtp>>
            ) {
                if (response.isSuccessful) {
                    if (response.body()!![0].status == "Success") {
                        sharedPreference.putInt(this@OtpVerify, "yes", 1)
                        sharedPreference.putInt(this@OtpVerify, "profile", 1)
                        enterOtp!!.text.clear()
                        val user = sharedPreference.getString(this@OtpVerify, "user_status")
                        if (user == "exiting") {
                            sharedPreference.putInt(this@OtpVerify, "profile", 2)
                            finishAffinity()
                            val i = Intent(this@OtpVerify, SellerProfileProductList::class.java)
                            startActivity(i)
                        } else if (user == "new") {
                            finishAffinity()
                            val i = Intent(this@OtpVerify, ShopAdd::class.java)
                            startActivity(i)
                        }
                    }
                    if (response.body()!![0].status == "failure") {
                        Utils_Class.toast_center(this@OtpVerify, "Invalid otp")
                    }
                    Utils_Class.mProgress!!.dismiss()
                }
            }

            override fun onFailure(call: Call<ArrayList<CheckOtp>>, t: Throwable) {
            }
        })
    }

    private fun otpGenerate() {
        Utils_Class.mProgress(this@OtpVerify)!!.show()
        val map = HashMap<String, String?>()
        map["action"] = "check_seller"
        map["gmail"] = sharedPreference.getString(this@OtpVerify, "resend")
        val retrofitAPI = RetrofitApiClient.retrofit!!.create(
            RetrofitAPI::class.java
        )
        val call = retrofitAPI.getotp(map)
        call.enqueue(object : Callback<ArrayList<SendOtppojo>?> {
            override fun onResponse(
                call: Call<ArrayList<SendOtppojo>?>,
                response: Response<ArrayList<SendOtppojo>?>
            ) {
                if (response.isSuccessful) {
                    sendOtp!!.addAll(response.body()!!)
                    sharedPreference.putString(
                        this@OtpVerify,
                        "register_otp",
                        "" + sendOtp!![0].otp
                    )
                    Utils_Class.mProgress!!.dismiss()
                }
            }

            override fun onFailure(call: Call<ArrayList<SendOtppojo>?>, t: Throwable) {
            }
        })
    }
}