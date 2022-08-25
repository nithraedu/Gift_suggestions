package nithra.tamil.word.game.giftsuggestions;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.HashMap;

import nithra.tamil.word.game.giftsuggestions.Retrofit.RetrofitAPI;
import nithra.tamil.word.game.giftsuggestions.Retrofit.RetrofitApiClient;
import nithra.tamil.word.game.giftsuggestions.Retrofit.SellerProfilePojo;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SellerProfile extends AppCompatActivity {
    ArrayList<SellerProfilePojo> gift;
    ImageView IVPreviewImage;
    TextView seller_name, shop_name, mobile, address, pincode, state, district, city, country, latitude, longitude, total_gifts, mail, web;
    SharedPreference sharedPreference = new SharedPreference();
    ImageView back;
    ImageView profile_edit;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_seller_profile);

        final SwipeRefreshLayout pullToRefresh = findViewById(R.id.pullToRefresh);
        gift = new ArrayList<SellerProfilePojo>();
        IVPreviewImage = findViewById(R.id.IVPreviewImage);
        seller_name = findViewById(R.id.seller_name);
        shop_name = findViewById(R.id.shop_name);
        mobile = findViewById(R.id.mobile);
        mail = findViewById(R.id.mail);
        web = findViewById(R.id.web);
        address = findViewById(R.id.address);
        pincode = findViewById(R.id.pincode);
        state = findViewById(R.id.state);
        district = findViewById(R.id.district);
        IVPreviewImage = findViewById(R.id.IVPreviewImage);
        profile_edit = findViewById(R.id.profile_edit);
        country = findViewById(R.id.country);
        city = findViewById(R.id.city);
        back = findViewById(R.id.back);
        longitude = findViewById(R.id.longitude);
        latitude = findViewById(R.id.latitude);
        total_gifts = findViewById(R.id.total_gifts);

        TextView android_id=findViewById(R.id.android_id);
        android_id.setText(Utils_Class.android_id(this));

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        profile_edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getApplicationContext(), ShopEdit.class);
                startActivity(i);
            }
        });
        Utils_Class.mProgress(this, "Loading please wait...", false).show();

        category();
        pullToRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                gift.clear();
                category();
                pullToRefresh.setRefreshing(false);
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (sharedPreference.getInt(getApplicationContext(), "finish") == 1) {
            category();
            sharedPreference.putInt(SellerProfile.this, "finish", 0);
        }
    }

    public void category() {
        HashMap<String, String> map = new HashMap<>();
        map.put("action", "get_id");
        map.put("id", sharedPreference.getString(this, "user_id"));

        RetrofitAPI retrofitAPI = RetrofitApiClient.getRetrofit().create(RetrofitAPI.class);
        Call<ArrayList<SellerProfilePojo>> call = retrofitAPI.profile(map);
        call.enqueue(new Callback<ArrayList<SellerProfilePojo>>() {
            @Override
            public void onResponse(Call<ArrayList<SellerProfilePojo>> call, Response<ArrayList<SellerProfilePojo>> response) {
                if (response.isSuccessful()) {
                    String result = new Gson().toJson(response.body());
                    System.out.println("======response result:" + result);
                    gift.clear();
                    gift.addAll(response.body());
                    Glide.with(getApplicationContext()).load(gift.get(0).getLogo())
                            //.error(R.drawable.gift_1)
                            .diskCacheStrategy(DiskCacheStrategy.ALL)
                            .into(IVPreviewImage);
                    seller_name.setText(gift.get(0).getName());
                    shop_name.setText(gift.get(0).getShopName());
                    mobile.setText(gift.get(0).getSellerMobile());
                    mail.setText(gift.get(0).getShopEmail());
                    web.setText(gift.get(0).getShopWebsite());
                    address.setText(gift.get(0).getAddress());
                    pincode.setText(gift.get(0).getPincode());
                    state.setText(gift.get(0).getState());
                    district.setText(gift.get(0).getDistrict());
                    city.setText(gift.get(0).getCity());
                    country.setTag(gift.get(0).getCountry());
                    latitude.setText(gift.get(0).getLatitude());
                    longitude.setText(gift.get(0).getLongitude());
                    total_gifts.setText("My total gifts : " + gift.get(0).getTotalGifts());
                    Utils_Class.mProgress.dismiss();

                }
                System.out.println("======response :" + response);
            }

            @Override
            public void onFailure(Call<ArrayList<SellerProfilePojo>> call, Throwable t) {
                System.out.println("======response t:" + t);
            }
        });
    }

}