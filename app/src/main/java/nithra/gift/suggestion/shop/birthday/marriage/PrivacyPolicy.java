package nithra.gift.suggestion.shop.birthday.marriage;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;

public class PrivacyPolicy extends AppCompatActivity {
    WebView wv1;
    ProgressBar pb1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_privacy_policy);

        wv1 = findViewById(R.id.wv1);
        pb1 = findViewById(R.id.pb1);
        wv1.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                return true;
            }
        });
        WebSettings webSettings = wv1.getSettings();
        webSettings.setJavaScriptEnabled(true);
        wv1.setWebViewClient(new WebViewClient() {
            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
                pb1.setVisibility(View.GONE);
            }
        });
        wv1.loadUrl("https://www.nithra.mobi/privacy.php");
    }
    public void onBackPressed() {
        if (wv1.canGoBack()) {
            wv1.goBack();
            pb1.setVisibility(View.GONE);
        } else {
            super.onBackPressed();
        }
    }
    }
