package nithra.tamil.word.game.giftsuggestions;

import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.textfield.TextInputEditText;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.HashMap;

import nithra.tamil.word.game.giftsuggestions.Retrofit.AddSeller;
import nithra.tamil.word.game.giftsuggestions.Retrofit.GiftList;
import nithra.tamil.word.game.giftsuggestions.Retrofit.RetrofitAPI;
import nithra.tamil.word.game.giftsuggestions.Retrofit.RetrofitApiClient;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ShopEdit extends AppCompatActivity {

    TextInputEditText sellername, shopname, shopaddress, mobilenumber, city, state, country, latitude, longitude, pincode, district;
    String sell_name, shop_name, shop_add, mob_num, shop_city, shop_country, shop_state, shop_pincode, shop_district, shop_latitude, shop_longitude;
    TextView save, remove;
    ImageView IVPreviewImage;
    SharedPreference sharedPreference = new SharedPreference();
    ArrayList<AddSeller> list_shop;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_shop_edit);
        sellername = findViewById(R.id.sellername);
        shopname = findViewById(R.id.shopname);
        shopaddress = findViewById(R.id.shopaddress);
        mobilenumber = findViewById(R.id.mobilenumber);
        city = findViewById(R.id.city);
        state = findViewById(R.id.state);
        country = findViewById(R.id.country);
        save = findViewById(R.id.save);
        remove = findViewById(R.id.remove);
        IVPreviewImage = findViewById(R.id.IVPreviewImage);
        latitude = findViewById(R.id.latitude);
        longitude = findViewById(R.id.longitude);
        pincode = findViewById(R.id.pincode);
        pincode = findViewById(R.id.pincode);
        district = findViewById(R.id.district);
        list_shop = new ArrayList<AddSeller>();

        shopedit();

        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sell_name = sellername.getText().toString().trim();
                shop_name = shopname.getText().toString().trim();
                shop_add = shopaddress.getText().toString().trim();
                mob_num = mobilenumber.getText().toString().trim();
                shop_city = city.getText().toString().trim();
                shop_state = state.getText().toString().trim();
                shop_country = country.getText().toString().trim();
                shop_district = district.getText().toString().trim();
                shop_pincode = pincode.getText().toString().trim();
                shop_latitude = latitude.getText().toString().trim();
                shop_longitude = longitude.getText().toString().trim();


                if (sell_name.equals("")) {
                    Utils_Class.toast_center(getApplicationContext(), "Please Enter Seller Name...");
                } else if (shop_name.equals("")) {
                    Utils_Class.toast_center(getApplicationContext(), "Please Enter Shop address...");
                } else if (mob_num.equals("")) {
                    Utils_Class.toast_center(getApplicationContext(), "Please Enter Correct Mobile Number...");
                } else if (shop_add.equals("")) {
                    Utils_Class.toast_center(getApplicationContext(), "Please Enter Your address...");
                } else if (shop_pincode.equals("")) {
                    Utils_Class.toast_center(getApplicationContext(), "Please Enter Your pincode...");
                } else if (shop_country.equals("")) {
                    Utils_Class.toast_center(getApplicationContext(), "Please Enter Your country...");
                } else if (shop_state.equals("")) {
                    Utils_Class.toast_center(getApplicationContext(), "Please Enter Your state...");
                } else if (shop_district.equals("")) {
                    Utils_Class.toast_center(getApplicationContext(), "Please Enter Your district...");
                } else if (shop_city.equals("")) {
                    Utils_Class.toast_center(getApplicationContext(), "Please Enter Your city...");
                } else if (shop_latitude.equals("")) {
                    Utils_Class.toast_center(getApplicationContext(), "Please Enter Your latitude...");
                } else if (shop_longitude.equals("")) {
                    Utils_Class.toast_center(getApplicationContext(), "Please Enter Your longitude...");
                } else {
                    submit_res();

                }


            }
        });

    }

    public void submit_res() {
        HashMap<String, String> map = new HashMap<>();
        map.put("action", "add_seller");
        map.put("user_id", sharedPreference.getString(getApplicationContext(), "user_id"));
        map.put("shop_name", shop_name);
        map.put("seller_mobile", mob_num);
        map.put("name", sell_name);
        map.put("state", shop_state);
        map.put("address", shop_add);
        map.put("pincode", shop_pincode);
        map.put("latitude", shop_latitude);
        map.put("longitude", shop_longitude);
        map.put("district", shop_district);
        map.put("city", shop_city);
        //map.put("logo", mob_num);
        System.out.println("printmap" + map);

        RetrofitAPI retrofitAPI = RetrofitApiClient.getRetrofit().create(RetrofitAPI.class);
        Call<ArrayList<AddSeller>> call = retrofitAPI.add_seller(map);
        call.enqueue(new Callback<ArrayList<AddSeller>>() {
            @Override
            public void onResponse(Call<ArrayList<AddSeller>> call, Response<ArrayList<AddSeller>> response) {
                if (response.isSuccessful()) {
                    String result = new Gson().toJson(response.body());
                    System.out.println("======response result:" + result);
                    if (response.body().get(0).getStatus().equals("Success")) {
                        sellername.getText().clear();
                        shopname.getText().clear();
                        shopaddress.getText().clear();
                        mobilenumber.getText().clear();
                        city.getText().clear();
                        state.getText().clear();
                        country.getText().clear();
                        latitude.getText().clear();
                        longitude.getText().clear();
                        pincode.getText().clear();
                        district.getText().clear();
                        Toast.makeText(getApplicationContext(), "Your shop updated successfully, Thank you", Toast.LENGTH_SHORT).show();

                    }

                }
                System.out.println("======response :" + response);
            }

            @Override
            public void onFailure(Call<ArrayList<AddSeller>> call, Throwable t) {
                System.out.println("======response t:" + t);
            }
        });
    }

    public void shopedit() {
        HashMap<String, String> map = new HashMap<>();
        map.put("action", "add_seller");
        map.put("user_id", sharedPreference.getString(this, "user_id"));

        System.out.println("print_map " + map);
        RetrofitAPI retrofitAPI = RetrofitApiClient.getRetrofit().create(RetrofitAPI.class);
        Call<ArrayList<AddSeller>> call = retrofitAPI.add_seller(map);
        call.enqueue(new Callback<ArrayList<AddSeller>>() {
            @Override
            public void onResponse(Call<ArrayList<AddSeller>> call, Response<ArrayList<AddSeller>> response) {
                if (response.isSuccessful()) {
                    String result = new Gson().toJson(response.body());
                    System.out.println("======response result:" + result);
                    if (response.body().get(0).getStatus().equals("Success")) {
                        list_shop.addAll(response.body());

                        sellername.setText(list_shop.get(0).getName());
                        shopname.setText(list_shop.get(0).getShopName());
                        shopaddress.setText(list_shop.get(0).getName());
                        mobilenumber.setText(list_shop.get(0).getName());
                        city.setText(list_shop.get(0).getName());
                        state.setText(list_shop.get(0).getName());
                        country.setText(list_shop.get(0).getName());
                        latitude.setText(list_shop.get(0).getName());
                        longitude.setText(list_shop.get(0).getName());
                        pincode.setText(list_shop.get(0).getName());
                        district.setText(list_shop.get(0).getName());

                    }
                }
                System.out.println("======response :" + response);
            }

            @Override
            public void onFailure(Call<ArrayList<AddSeller>> call, Throwable t) {
                System.out.println("======response t:" + t);
            }
        });
    }

}