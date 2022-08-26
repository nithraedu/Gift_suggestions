package nithra.tamil.word.game.giftsuggestions.Otp;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.HashMap;

import nithra.tamil.word.game.giftsuggestions.FragMove;
import nithra.tamil.word.game.giftsuggestions.MainActivity;
import nithra.tamil.word.game.giftsuggestions.R;
import nithra.tamil.word.game.giftsuggestions.Retrofit.GetGift;
import nithra.tamil.word.game.giftsuggestions.Retrofit.RetrofitAPI;
import nithra.tamil.word.game.giftsuggestions.Retrofit.RetrofitApiClient;
import nithra.tamil.word.game.giftsuggestions.Retrofit.SendOtppojo;
import nithra.tamil.word.game.giftsuggestions.SharedPreference;
import nithra.tamil.word.game.giftsuggestions.Utils_Class;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class OtpSend extends AppCompatActivity {
    TextView getotp;
    EditText name,gmail;
    String name_otp,gmail_otp,emailPattern;
    SharedPreference sharedPreference = new SharedPreference();
    ArrayList<SendOtppojo> send_otp;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.fragment_send_o_t_p);

        getotp = findViewById(R.id.getotp);
        name=findViewById(R.id.name);
        gmail=findViewById(R.id.gmail);
        send_otp=new ArrayList<SendOtppojo>();
        getotp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Utils_Class.mProgress(OtpSend.this, "Loading please wait...", false).show();

                otp_generate();
                sharedPreference.putString(OtpSend.this, "resend", "" +gmail_otp);

            }
        });
    }

    public void otp_generate() {

        name_otp = name.getText().toString().trim();
        gmail_otp = gmail.getText().toString().trim();
        emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";

        System.out.println("print"+gmail_otp);
        HashMap<String, String> map = new HashMap<>();
        map.put("action", "check_seller");
        map.put("name", name_otp);
        map.put("gmail", gmail_otp);

        RetrofitAPI retrofitAPI = RetrofitApiClient.getRetrofit().create(RetrofitAPI.class);
        Call<ArrayList<SendOtppojo>> call = retrofitAPI.getotp(map);
        call.enqueue(new Callback<ArrayList<SendOtppojo>>() {
            @Override
            public void onResponse(Call<ArrayList<SendOtppojo>> call, Response<ArrayList<SendOtppojo>> response) {
                if (response.isSuccessful()) {
                   /* String result = new Gson().toJson(response.body());
                    System.out.println("======response result:" + result);
                    send_otp.addAll(response.body());
                    name.getText().clear();
                    gmail.getText().clear();
                    fragMove.enterotp();*/

                    if (response.body().get(0).getStatus().equals("Success")) {
                        if (name_otp.equals("")) {
                            Utils_Class.toast_center(OtpSend.this, "Please Enter Your Name...");
                        }else if (gmail_otp.equals("")) {
                            Utils_Class.toast_center(OtpSend.this, "Please Enter Your Email...");
                        } else if (!gmail_otp.matches(emailPattern)) {
                            Toast.makeText(OtpSend.this, "Invalid email address", Toast.LENGTH_SHORT).show();
                        }
                        else {
                            String result = new Gson().toJson(response.body());
                            System.out.println("======response result:" + result);
                            send_otp.addAll(response.body());
                            sharedPreference.putString(OtpSend.this, "register_otp", "" + send_otp.get(0).getOtp());
                            sharedPreference.putString(OtpSend.this, "user_id", "" + send_otp.get(0).getId());
                            sharedPreference.putString(OtpSend.this, "user_status", "" + send_otp.get(0).getUserStatus());

                            name.getText().clear();
                            gmail.getText().clear();
                            Intent i=new Intent(OtpSend.this, OtpVerify.class);
                            startActivity(i);
                            finish();
                            //fragMove.enterotp();
                        }
                    }
                    Utils_Class.mProgress.dismiss();

                }
                System.out.println("======response :" + response);
            }

            @Override
            public void onFailure(Call<ArrayList<SendOtppojo>> call, Throwable t) {
                System.out.println("======response t:" + t);
            }
        });


    }

}
