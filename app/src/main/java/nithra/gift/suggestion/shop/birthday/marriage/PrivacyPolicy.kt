package nithra.gift.suggestion.shop.birthday.marriage

import android.os.Bundle
import android.view.View
import android.view.Window
import android.view.WindowManager
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.ProgressBar
import androidx.appcompat.app.AppCompatActivity

class PrivacyPolicy : AppCompatActivity() {
    var wv1: WebView? = null
    var pb1: ProgressBar? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requestWindowFeature(Window.FEATURE_NO_TITLE)
        this.window.setFlags(
            WindowManager.LayoutParams.FLAG_FULLSCREEN,
            WindowManager.LayoutParams.FLAG_FULLSCREEN
        )
        setContentView(R.layout.activity_privacy_policy)
        wv1 = findViewById(R.id.wv1)
        pb1 = findViewById(R.id.pb1)
        wv1!!.setOnLongClickListener({ true })
        val webSettings = wv1!!.getSettings()
        webSettings.javaScriptEnabled = true
        wv1!!.setWebViewClient(object : WebViewClient() {
            override fun onPageFinished(view: WebView, url: String) {
                super.onPageFinished(view, url)
                pb1!!.setVisibility(View.GONE)
            }
        })
        wv1!!.loadUrl("https://www.nithra.mobi/privacy.php")
    }

    override fun onBackPressed() {
        if (wv1!!.canGoBack()) {
            wv1!!.goBack()
            pb1!!.visibility = View.GONE
        } else {
            super.onBackPressed()
        }
    }
}