package nithra.tamil.word.game.giftsuggestions.Fragment;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.HashMap;

import nithra.tamil.word.game.giftsuggestions.FragMove;
import nithra.tamil.word.game.giftsuggestions.R;
import nithra.tamil.word.game.giftsuggestions.Retrofit.RetrofitAPI;
import nithra.tamil.word.game.giftsuggestions.Retrofit.RetrofitApiClient;
import nithra.tamil.word.game.giftsuggestions.Retrofit.SellerRegister;
import nithra.tamil.word.game.giftsuggestions.Retrofit.SendOtppojo;
import nithra.tamil.word.game.giftsuggestions.SharedPreference;
import nithra.tamil.word.game.giftsuggestions.Utils_Class;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class SendOTP extends Fragment {

    FragMove fragMove;
    TextView getotp;
    EditText name,gmail;
    String name_otp,gmail_otp,emailPattern;
    SharedPreference sharedPreference = new SharedPreference();
    ArrayList<SendOtppojo> send_otp;
    SharedPreferences pref;


    public SendOTP() {
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_send_o_t_p, container, false);
        pref = getContext().getSharedPreferences("register", Context.MODE_PRIVATE);

        fragMove = (FragMove) getContext();
        getotp = view.findViewById(R.id.getotp);
        name=view.findViewById(R.id.name);
        gmail=view.findViewById(R.id.gmail);
        send_otp=new ArrayList<SendOtppojo>();
        getotp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                otp_generate();
                sharedPreference.putString(getContext(), "resend", "" +gmail_otp);

            }
        });
        return view;
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
                            Utils_Class.toast_center(getContext(), "Please Enter Your Name...");
                        }else if (gmail_otp.equals("")) {
                            Utils_Class.toast_center(getContext(), "Please Enter Your Email...");
                        } else if (!gmail_otp.matches(emailPattern)) {
                            Toast.makeText(getContext(), "Invalid email address", Toast.LENGTH_SHORT).show();
                        }
                        else {
                            String result = new Gson().toJson(response.body());
                            System.out.println("======response result:" + result);
                            send_otp.addAll(response.body());
                            sharedPreference.putString(getContext(), "register_otp", "" + send_otp.get(0).getOtp());
                            name.getText().clear();
                            gmail.getText().clear();
                            fragMove.enterotp();
                        }
                    }

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