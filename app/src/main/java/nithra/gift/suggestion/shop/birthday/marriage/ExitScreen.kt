package nithra.gift.suggestion.shop.birthday.marriage;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.os.Handler;

public class ExitScreen extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_exit_screen);
        Handler handel = new Handler();
        handel.postDelayed(() -> {
            finish();
        }, 1500);
    }
}