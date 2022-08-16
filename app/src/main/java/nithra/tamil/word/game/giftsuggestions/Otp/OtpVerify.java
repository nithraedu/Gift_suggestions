package nithra.tamil.word.game.giftsuggestions.Otp;

import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.HashMap;

import nithra.tamil.word.game.giftsuggestions.FragMove;
import nithra.tamil.word.game.giftsuggestions.MainActivity;
import nithra.tamil.word.game.giftsuggestions.MyProduct;
import nithra.tamil.word.game.giftsuggestions.R;
import nithra.tamil.word.game.giftsuggestions.Retrofit.CheckOtp;
import nithra.tamil.word.game.giftsuggestions.Retrofit.RetrofitAPI;
import nithra.tamil.word.game.giftsuggestions.Retrofit.RetrofitApiClient;
import nithra.tamil.word.game.giftsuggestions.Retrofit.SendOtppojo;
import nithra.tamil.word.game.giftsuggestions.SharedPreference;
import nithra.tamil.word.game.giftsuggestions.Utils_Class;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class OtpVerify extends AppCompatActivity {
    TextView enterotp;
    EditText enter_otp;
    String verify;
    SharedPreference sharedPreference = new SharedPreference();
    ArrayList<SendOtppojo> send_otp;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.fragment_enter_o_t_p);

        TextView _tv = findViewById(R.id.timer);
        send_otp=new ArrayList<SendOtppojo>();

        enterotp = findViewById(R.id.enterotp);
        enter_otp=findViewById(R.id.enter_otp);


        new CountDownTimer(60000, 1000) { // adjust the milli seconds here
            public void onTick(long millisUntilFinished) {
                _tv.setText("If you didn't receive a otp? " + millisUntilFinished / 1000);
            }
            public void onFinish() {
                _tv.setText("If you didn't receive a otp? Resend");
                sharedPreference.putString(OtpVerify.this, "register_otp", "" + 0);
            }
        }.start();


        _tv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //sharedPreference.putString(OtpVerify.this, "register_otp_1", "register_otp");
                otp_generate();
                new CountDownTimer(60000, 1000) { // adjust the milli seconds here

                    public void onTick(long millisUntilFinished) {

                        _tv.setText("If you didn't receive a otp? " + millisUntilFinished / 1000);
                    }

                    public void onFinish() {
                        _tv.setText("If you didn't receive a otp? Resend");
                    }

                }.start();

            }
        });

        enterotp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                if (enter_otp.getText().toString().equals("")) {
                    Utils_Class.toast_center(OtpVerify.this, "Enter your otp");
                } else if (sharedPreference.getString(OtpVerify.this, "register_otp").equals(enter_otp.getText().toString())) {

                    otp_verify();


                } else {
                    Utils_Class.toast_center(OtpVerify.this, "Invalid otp");
                }


            }
        });
    }

    public void otp_verify() {
        verify = enter_otp.getText().toString().trim();
        HashMap<String, String> map = new HashMap<>();
        map.put("action", "check_otp");
        map.put("user_id", sharedPreference.getString(OtpVerify.this, "user_id"));
        //map.put("mobile_num", "" + sharedPreference.getString(OtpVerify.this, "resend"));
        map.put("otp", verify);
        RetrofitAPI retrofitAPI = RetrofitApiClient.getRetrofit().create(RetrofitAPI.class);
        Call<ArrayList<CheckOtp>> call = retrofitAPI.checkotp(map);
        call.enqueue(new Callback<ArrayList<CheckOtp>>() {
            @Override
            public void onResponse(Call<ArrayList<CheckOtp>> call, Response<ArrayList<CheckOtp>> response) {
                if (response.isSuccessful()) {
                    String result = new Gson().toJson(response.body());
                    System.out.println("======response result:" + result);
                    sharedPreference.putInt(OtpVerify.this, "yes", 1);
                    enter_otp.getText().clear();
                    String user=sharedPreference.getString(OtpVerify.this, "user_status");

                    if (user.equals("exiting")) {

                        Intent i=new Intent(OtpVerify.this, MyProduct.class);
                        startActivity(i);
                        finish();
                        //fragMove.product();
                        //fragMove.seller();
                    }else if(user.equals("new")){
                        Intent i=new Intent(OtpVerify.this, ShopAdd.class);
                        startActivity(i);
                        finish();
                        //fragMove.seller();

                    }

                }
                System.out.println("======response :" + response);
            }

            @Override
            public void onFailure(Call<ArrayList<CheckOtp>> call, Throwable t) {
                System.out.println("======response t:" + t);
            }
        });
    }


    public void otp_generate() {
        HashMap<String, String> map = new HashMap<>();
        map.put("action", "check_seller");
        map.put("gmail", sharedPreference.getString(OtpVerify.this, "resend"));

        RetrofitAPI retrofitAPI = RetrofitApiClient.getRetrofit().create(RetrofitAPI.class);
        Call<ArrayList<SendOtppojo>> call = retrofitAPI.getotp(map);
        call.enqueue(new Callback<ArrayList<SendOtppojo>>() {
            @Override
            public void onResponse(Call<ArrayList<SendOtppojo>> call, Response<ArrayList<SendOtppojo>> response) {
                if (response.isSuccessful()) {

                    String result = new Gson().toJson(response.body());
                    System.out.println("======response result:" + result);
                    send_otp.addAll(response.body());
                    sharedPreference.putString(OtpVerify.this, "register_otp", "" + send_otp.get(0).getOtp());


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
