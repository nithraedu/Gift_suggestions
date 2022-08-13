/*
package nithra.tamil.word.game.giftsuggestions;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.textfield.TextInputEditText;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.HashMap;

import nithra.tamil.word.game.giftsuggestions.Retrofit.AddSeller;
import nithra.tamil.word.game.giftsuggestions.Retrofit.RetrofitAPI;
import nithra.tamil.word.game.giftsuggestions.Retrofit.RetrofitApiClient;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ShopAdd extends AppCompatActivity {

    TextInputEditText sellername, shopname, shopaddress, mobilenumber, city, state, country, latitude, longitude, pincode, district;
    TextView save, remove;
    String sell_name, shop_name, shop_add, mob_num, shop_city, shop_country, shop_state, shop_pincode, shop_district, shop_latitude, shop_longitude;
    ImageView IVPreviewImage;
    int SELECT_PICTURE = 200;
    FragMove fragMove;
    SharedPreference sharedPreference = new SharedPreference();
    String pack = "nithra.tamil.word.game.giftsuggestions";

    Uri uri_1;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.fragment_add);

        fragMove = (FragMove) getApplicationContext();
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
        IVPreviewImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //imageChooser();
                openSomeActivityForResult();

            }
        });
        remove.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                IVPreviewImage.setImageResource(R.drawable.logo_add);
                //share(pack);
            }
        });
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


    public void openSomeActivityForResult() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        someActivityResultLauncher1.launch(intent);
    }

    // You can do the assignment inside onAttach or onCreate, i.e, before the activity is displayed
   */
/* ActivityResultLauncher<Intent> someActivityResultLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(){

            });*//*



    ActivityResultLauncher<Intent> someActivityResultLauncher1 = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            new ActivityResultCallback<ActivityResult>() {
                @Override
                public void onActivityResult(ActivityResult result) {
                    if (result.getResultCode() == Activity.RESULT_OK) {
                        // There are no request codes
                        Intent data = result.getData();
                        IVPreviewImage.setImageURI(data.getData());
                        uri_1 = data.getData();
                    }
                }
            });

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
                        sharedPreference.putInt(getApplicationContext(), "yes", 1);
                        Toast.makeText(getApplicationContext(), "Your shop added successfully, Thank you", Toast.LENGTH_SHORT).show();
                        fragMove.product();

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
*/
