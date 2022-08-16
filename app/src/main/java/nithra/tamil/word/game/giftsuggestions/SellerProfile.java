package nithra.tamil.word.game.giftsuggestions;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

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
    TextView giftname, giftcategory, giftgender, giftprize, offerprize, offerpercen, description, city,profile_edit;
    SharedPreference sharedPreference = new SharedPreference();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_seller_profile);

        gift = new ArrayList<SellerProfilePojo>();
        IVPreviewImage = findViewById(R.id.IVPreviewImage);
        giftname = findViewById(R.id.giftname);
        giftcategory = findViewById(R.id.giftcategory);
        giftgender = findViewById(R.id.giftgender);
        giftprize = findViewById(R.id.giftprize);
        offerprize = findViewById(R.id.offerprize);
        offerpercen = findViewById(R.id.offerpercen);
        description = findViewById(R.id.description);
        IVPreviewImage = findViewById(R.id.IVPreviewImage);
        profile_edit = findViewById(R.id.profile_edit);

        city = findViewById(R.id.city);
        profile_edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i=new Intent(getApplicationContext(),ShopEdit.class);
                startActivity(i);
            }
        });
        Utils_Class.mProgress(this, "Loading please wait...", false).show();

        category();

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
                    gift.addAll(response.body());
                    Glide.with(getApplicationContext()).load(gift.get(0).getLogo())
                            //.error(R.drawable.gift_1)
                            .diskCacheStrategy(DiskCacheStrategy.ALL)
                            .into(IVPreviewImage);
                    giftname.setText(gift.get(0).getName());
                    giftcategory.setText(gift.get(0).getShopName());
                    giftgender.setText(gift.get(0).getSellerMobile());
                    giftprize.setText(gift.get(0).getAddress());
                    offerprize.setText(gift.get(0).getPincode());
                    offerpercen.setText(gift.get(0).getState());
                    description.setText(gift.get(0).getDistrict());
                    city.setText(gift.get(0).getCity());
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