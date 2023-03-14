package nithra.gift.suggestion.shop.birthday.marriage.product_shop

import android.content.Intent
import android.os.Bundle
import android.widget.ImageView
import android.widget.LinearLayout
import androidx.appcompat.app.AppCompatActivity
import nithra.gift.suggestion.shop.birthday.marriage.otp.OtpSend
import nithra.gift.suggestion.shop.birthday.marriage.R

class SellerEntry : AppCompatActivity() {
    var register: LinearLayout? = null
    var back: ImageView? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_new_main)
        register = findViewById(R.id.register)
        back = findViewById(R.id.back)
        back!!.setOnClickListener { finish() }
        register!!.setOnClickListener {
            val i = Intent(this@SellerEntry, OtpSend::class.java)
            startActivity(i)
            finish()
        }
    }
}