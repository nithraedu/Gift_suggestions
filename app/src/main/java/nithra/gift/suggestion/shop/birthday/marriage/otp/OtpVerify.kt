package nithra.gift.suggestion.shop.birthday.marriage.otp

import android.content.Intent
import android.os.Bundle
import android.os.CountDownTimer
import android.text.Editable
import android.text.TextWatcher
import android.view.Window
import android.view.WindowManager
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
    var enterotp: TextView? = null
    var edit_email: TextView? = null
    var enter_otp: EditText? = null
    var otp_1: EditText? = null
    var otp_2: EditText? = null
    var otp_3: EditText? = null
    var otp_4: EditText? = null
    var verify: String? = null
    var edit_otp: String? = null
    var sharedPreference = SharedPreference()
    var send_otp: ArrayList<SendOtppojo>? = null
    var edit: ImageView? = null
    var back: ImageView? = null
    var extra: Bundle? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requestWindowFeature(Window.FEATURE_NO_TITLE)
        this.window.setFlags(
            WindowManager.LayoutParams.FLAG_FULLSCREEN,
            WindowManager.LayoutParams.FLAG_FULLSCREEN
        )
        setContentView(R.layout.fragment_enter_o_t_p)
        val _tv = findViewById<TextView>(R.id.timer)
        send_otp = ArrayList()
        back = findViewById(R.id.back)
        enterotp = findViewById(R.id.enterotp)
        edit_email = findViewById(R.id.edit_email)
        otp_1 = findViewById(R.id.otp_1)
        otp_2 = findViewById(R.id.otp_2)
        otp_3 = findViewById(R.id.otp_3)
        otp_4 = findViewById(R.id.otp_4)
        edit = findViewById(R.id.edit)
        enter_otp = findViewById(R.id.enter_otp)
        back!!.setOnClickListener({ finish() })
        edit_email!!.setText("" + sharedPreference.getString(this@OtpVerify, "resend"))
        object : CountDownTimer(120000, 1000) {
            override fun onTick(millisUntilFinished: Long) {
                _tv.text = "If you didn't receive a otp? " + millisUntilFinished / 1000
            }

            override fun onFinish() {
                _tv.text = "If you didn't receive a otp? Resend"
                sharedPreference.putString(this@OtpVerify, "register_otp", "" + 0)
            }
        }.start()
        _tv.setOnClickListener {
            //sharedPreference.putString(OtpVerify.this, "register_otp_1", "register_otp");
            if (Utils_Class.isNetworkAvailable(this@OtpVerify)) {
                otp_generate()
            } else {
                Utils_Class.toast_normal(this@OtpVerify, "Please connect to your internet")
            }
            object : CountDownTimer(120000, 1000) {
                override fun onTick(millisUntilFinished: Long) {
                    _tv.text = "If you didn't receive a otp? " + millisUntilFinished / 1000
                }

                override fun onFinish() {
                    _tv.text = "If you didn't receive a otp? Resend"
                }
            }.start()
        }
        edit!!.setOnClickListener({
            finish()
        })
        otp_1!!.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {}
            override fun afterTextChanged(s: Editable) {
                if (s.length != 0) {
                    otp_2!!.requestFocus()
                }
            }
        })
        otp_2!!.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {}
            override fun afterTextChanged(s: Editable) {
                if (s.length != 0) {
                    otp_3!!.requestFocus()
                } else {
                    otp_1!!.requestFocus()
                }
            }
        })
        otp_3!!.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {}
            override fun afterTextChanged(s: Editable) {
                if (s.length != 0) {
                    otp_4!!.requestFocus()
                } else {
                    otp_2!!.requestFocus()
                }
            }
        })
        otp_4!!.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {}
            override fun afterTextChanged(s: Editable) {
                if (s.length != 0) {
                    otp_4!!.requestFocus()
                } else {
                    otp_3!!.requestFocus()
                }
            }
        })
        enterotp!!.setOnClickListener({
            edit_otp =
                otp_1!!.getText().toString().trim { it <= ' ' } + otp_2!!.getText().toString()
                    .trim { it <= ' ' } + otp_3!!.getText().toString()
                    .trim { it <= ' ' } + otp_4!!.getText().toString().trim { it <= ' ' }
            if (otp_1!!.getText().toString() == "" && otp_2!!.getText()
                    .toString() == "" && otp_3!!.getText().toString() == "" && otp_4!!.getText()
                    .toString() == ""
            ) {
                Utils_Class.toast_center(this@OtpVerify, "Enter your otp")
            } else {
                if (Utils_Class.isNetworkAvailable(this@OtpVerify)) {
                    otp_verify()
                } else {
                    Utils_Class.toast_normal(this@OtpVerify, "Please connect to your internet")
                }
            }
        })
    }

    fun otp_verify() {
        Utils_Class.mProgress(this@OtpVerify, "Loading please wait...", false)!!.show()
        verify = edit_otp!!.trim { it <= ' ' }
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
                        enter_otp!!.text.clear()
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

    fun otp_generate() {
        Utils_Class.mProgress(this@OtpVerify, "Loading please wait...", false)!!.show()
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
                    send_otp!!.addAll(response.body()!!)
                    sharedPreference.putString(
                        this@OtpVerify,
                        "register_otp",
                        "" + send_otp!![0].otp
                    )
                    Utils_Class.mProgress!!.dismiss()
                }
            }

            override fun onFailure(call: Call<ArrayList<SendOtppojo>?>, t: Throwable) {
            }
        })
    }
}