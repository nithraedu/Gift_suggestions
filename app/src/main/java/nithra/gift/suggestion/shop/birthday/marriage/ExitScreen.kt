package nithra.gift.suggestion.shop.birthday.marriage

import android.os.Bundle
import android.os.Handler
import androidx.appcompat.app.AppCompatActivity

class ExitScreen : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_exit_screen)
        val handel = Handler()
        handel.postDelayed({ finish() }, 1500)
    }
}