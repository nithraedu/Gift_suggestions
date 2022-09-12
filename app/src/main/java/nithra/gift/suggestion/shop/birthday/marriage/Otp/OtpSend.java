package nithra.gift.suggestion.shop.birthday.marriage.Otp;

import android.content.Intent;
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

import nithra.gift.suggestion.shop.birthday.marriage.ProductEdit;
import nithra.gift.suggestion.shop.birthday.marriage.R;
import nithra.gift.suggestion.shop.birthday.marriage.Retrofit.RetrofitAPI;
import nithra.gift.suggestion.shop.birthday.marriage.Retrofit.RetrofitApiClient;
import nithra.gift.suggestion.shop.birthday.marriage.Retrofit.SendOtppojo;
import nithra.gift.suggestion.shop.birthday.marriage.SellerEntry;
import nithra.gift.suggestion.shop.birthday.marriage.SharedPreference;
import nithra.gift.suggestion.shop.birthday.marriage.Utils_Class;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class OtpSend extends AppCompatActivity {
    TextView getotp;
    EditText name, gmail;
    String name_otp, gmail_otp, emailPattern;
    SharedPreference sharedPreference = new SharedPreference();
    ArrayList<SendOtppojo> send_otp;
    ImageView back;
    Intent intent;
    Bundle extra;
    String mail_set;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.fragment_send_o_t_p);
        back = findViewById(R.id.back);

        getotp = findViewById(R.id.getotp);
        name = findViewById(R.id.name);
        gmail = findViewById(R.id.gmail);
        send_otp = new ArrayList<SendOtppojo>();
      /*  intent = getIntent();
        extra = intent.getExtras();
        mail_set=extra.getString("mail_set");
        if (!mail_set.isEmpty()) {
            gmail.setText(mail_set);
        }
*/

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(OtpSend.this, SellerEntry.class);
                startActivity(i);
                finish();
            }
        });

        /*if (sharedPreference.getInt(OtpSend.this, "clear") == 1) {
            gmail.setText(sharedPreference.getString(OtpSend.this, "user_mail"));

        }*/

        getotp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                name_otp = name.getText().toString().trim();
                gmail_otp = gmail.getText().toString().trim();
                emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";
                if (name_otp.equals("")) {
                    Utils_Class.toast_center(OtpSend.this, "Please Enter Your Name...");
                } else if (gmail_otp.equals("")) {
                    Utils_Class.toast_center(OtpSend.this, "Please Enter Your Email...");
                } else if (!gmail_otp.matches(emailPattern)) {
                    Toast.makeText(OtpSend.this, "Invalid email address", Toast.LENGTH_SHORT).show();
                } else {
                    if (Utils_Class.isNetworkAvailable(OtpSend.this)) {

                        otp_generate();
                    } else {
                        Utils_Class.toast_normal(OtpSend.this, "Please connect to your internet");
                    }
                }

                sharedPreference.putString(OtpSend.this, "resend", "" + gmail_otp);

            }
        });
    }

    public void otp_generate() {
        Utils_Class.mProgress(OtpSend.this, "Loading please wait...", false).show();


        System.out.println("print" + gmail_otp);
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

                        String result = new Gson().toJson(response.body());
                        System.out.println("======response result:" + result);
                        send_otp.addAll(response.body());
                        sharedPreference.putString(OtpSend.this, "register_otp", "" + send_otp.get(0).getOtp());
                        sharedPreference.putString(OtpSend.this, "user_id", "" + send_otp.get(0).getId());
                        sharedPreference.putString(OtpSend.this, "user_status", "" + send_otp.get(0).getUserStatus());
                        sharedPreference.putString(OtpSend.this, "user_mail", "" + send_otp.get(0).getGmail());
                            /*name.getText().clear();
                            gmail.getText().clear();*/


                        Intent i = new Intent(OtpSend.this, OtpVerify.class);
                        startActivity(i);
                        //finish();
                        //fragMove.enterotp();

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
