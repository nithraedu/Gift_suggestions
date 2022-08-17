/*
package nithra.tamil.word.game.giftsuggestions.Fragment;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.os.CountDownTimer;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.HashMap;

import nithra.tamil.word.game.giftsuggestions.FragMove;
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

public class EnterOTP extends Fragment {

    FragMove fragMove;
    TextView enterotp;
    EditText enter_otp;
    String verify;
    SharedPreference sharedPreference = new SharedPreference();
    ArrayList<SendOtppojo> send_otp;

    public EnterOTP() {
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view= inflater.inflate(R.layout.fragment_enter_o_t_p, container, false);
        fragMove = (FragMove) getContext();
        TextView _tv = view.findViewById(R.id.timer);
        send_otp=new ArrayList<SendOtppojo>();

        enterotp = view.findViewById(R.id.enterotp);
        enter_otp=view.findViewById(R.id.enter_otp);

        new CountDownTimer(60000, 1000) { // adjust the milli seconds here
            public void onTick(long millisUntilFinished) {
                _tv.setText("If you didn't receive a otp? " + millisUntilFinished / 1000);
            }
            public void onFinish() {
                _tv.setText("If you didn't receive a otp? Resend");
                sharedPreference.putString(getContext(), "register_otp", "" + 0);
            }
        }.start();


        _tv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //sharedPreference.putString(getContext(), "register_otp_1", "register_otp");
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
                    Utils_Class.toast_center(getContext(), "Enter your otp");
                } else if (sharedPreference.getString(getContext(), "register_otp").equals(enter_otp.getText().toString())) {

                    otp_verify();


                } else {
                    Utils_Class.toast_center(getContext(), "Invalid otp");
                }


            }
        });
        return view;
    }

    public void otp_verify() {
        verify = enter_otp.getText().toString().trim();
        HashMap<String, String> map = new HashMap<>();
        map.put("action", "check_otp");
        map.put("user_id", sharedPreference.getString(getContext(), "user_id"));
        //map.put("mobile_num", "" + sharedPreference.getString(getContext(), "resend"));
        map.put("otp", verify);
        RetrofitAPI retrofitAPI = RetrofitApiClient.getRetrofit().create(RetrofitAPI.class);
        Call<ArrayList<CheckOtp>> call = retrofitAPI.checkotp(map);
        call.enqueue(new Callback<ArrayList<CheckOtp>>() {
            @Override
            public void onResponse(Call<ArrayList<CheckOtp>> call, Response<ArrayList<CheckOtp>> response) {
                if (response.isSuccessful()) {
                    String result = new Gson().toJson(response.body());
                    System.out.println("======response result:" + result);
                    sharedPreference.putInt(getContext(), "yes", 1);
                    enter_otp.getText().clear();
                    String user=sharedPreference.getString(getContext(), "user_status");

                    if (user.equals("exiting")) {
                        fragMove.product();
                        //fragMove.seller();
                    }else if(user.equals("new")){
                        fragMove.seller();

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
        map.put("gmail", sharedPreference.getString(getContext(), "resend"));

        RetrofitAPI retrofitAPI = RetrofitApiClient.getRetrofit().create(RetrofitAPI.class);
        Call<ArrayList<SendOtppojo>> call = retrofitAPI.getotp(map);
        call.enqueue(new Callback<ArrayList<SendOtppojo>>() {
            @Override
            public void onResponse(Call<ArrayList<SendOtppojo>> call, Response<ArrayList<SendOtppojo>> response) {
                if (response.isSuccessful()) {

                    String result = new Gson().toJson(response.body());
                    System.out.println("======response result:" + result);
                    send_otp.addAll(response.body());
                    sharedPreference.putString(getContext(), "register_otp", "" + send_otp.get(0).getOtp());


                }
                System.out.println("======response :" + response);
            }

            @Override
            public void onFailure(Call<ArrayList<SendOtppojo>> call, Throwable t) {
                System.out.println("======response t:" + t);
            }
        });


    }


}*/
