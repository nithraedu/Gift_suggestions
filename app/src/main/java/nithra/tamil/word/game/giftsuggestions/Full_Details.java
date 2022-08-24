package nithra.tamil.word.game.giftsuggestions;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Paint;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.HashMap;

import nithra.tamil.word.game.giftsuggestions.Retrofit.Gift_Cat;
import nithra.tamil.word.game.giftsuggestions.Retrofit.RetrofitAPI;
import nithra.tamil.word.game.giftsuggestions.Retrofit.RetrofitApiClient;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Full_Details extends AppCompatActivity {
    ArrayList<Gift_Cat> gift_show;
    Intent intent;
    Bundle extra;
    String id_gift;
    ImageView back, company_logo, IVPreviewImage;
    TextView giftname, giftprize, offerprize, description, detail_shop_name, detail_add, owner_name;
    LinearLayout phone;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_full_details);
        gift_show = new ArrayList<Gift_Cat>();
        back = findViewById(R.id.back);
        giftname = findViewById(R.id.giftname);
        giftname = findViewById(R.id.giftname);
        giftprize = findViewById(R.id.giftprize);
        offerprize = findViewById(R.id.offerprize);
        description = findViewById(R.id.description);
        detail_shop_name = findViewById(R.id.detail_shop_name);
        detail_add = findViewById(R.id.detail_add);
        owner_name = findViewById(R.id.owner_name);
        company_logo = findViewById(R.id.company_logo);
        IVPreviewImage = findViewById(R.id.IVPreviewImage);
        phone = findViewById(R.id.phone);
        intent = getIntent();
        extra = intent.getExtras();
        id_gift = extra.getString("full_view");

        giftprize.setPaintFlags(giftprize.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);


        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        get_cat();

        phone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String phone = gift_show.get(0).getSellerMobile().trim();
                Intent intent = new Intent(Intent.ACTION_DIAL, Uri.fromParts("tel", phone, null));
                startActivity(intent);
              /*  if (phone.equals("")) {
                    Utils_Class.toast_center(Category_Full_View.this, "Mobile number not available");
                } else {
                    Intent intent = new Intent(Intent.ACTION_DIAL, Uri.fromParts("tel", phone, null));
                    startActivity(intent);
                }*/
            }
        });

        IVPreviewImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ImageView img_view;
                Dialog dialog = new Dialog(Full_Details.this, android.R.style.Theme_DeviceDefault);
                dialog.setContentView(R.layout.image_view);
                //dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
                dialog.setCanceledOnTouchOutside(true);
                img_view = dialog.findViewById(R.id.img_view);
                Glide.with(getApplicationContext()).load(gift_show.get(0).getGiftImage())
                        //.error(R.drawable.gift_1)
                        .diskCacheStrategy(DiskCacheStrategy.ALL)
                        .into(img_view);
                dialog.show();
            }
        });
    }


    public void get_cat() {
        HashMap<String, String> map = new HashMap<>();
        map.put("action", "get_cat");
        map.put("id", id_gift);
        System.out.println("printing==" + map);
        RetrofitAPI retrofitAPI = RetrofitApiClient.getRetrofit().create(RetrofitAPI.class);
        Call<ArrayList<Gift_Cat>> call = retrofitAPI.gift_cat(map);
        call.enqueue(new Callback<ArrayList<Gift_Cat>>() {
            @Override
            public void onResponse(Call<ArrayList<Gift_Cat>> call, Response<ArrayList<Gift_Cat>> response) {
                if (response.isSuccessful()) {
                    String result = new Gson().toJson(response.body());
                    System.out.println("======response result:" + result);
                    if (response.body().get(0).getStatus().equals("Success")) {
                        gift_show.clear();
                        gift_show.addAll(response.body());
                        Glide.with(getApplicationContext()).load(gift_show.get(0).getGiftImage())
                                //.error(R.drawable.gift_1)
                                .diskCacheStrategy(DiskCacheStrategy.ALL)
                                .into(IVPreviewImage);
                        giftname.setText(gift_show.get(0).getGiftName());
                        giftprize.setText("\u20B9 " + gift_show.get(0).getGiftAmount());
                        offerprize.setText("\u20B9 " + gift_show.get(0).getTotalAmount());
                        description.setText(gift_show.get(0).getGiftDescription());
                        detail_shop_name.setText(gift_show.get(0).getShopName());
                        detail_add.setText(gift_show.get(0).getAddress() + ", " + gift_show.get(0).getCity() + ", " + gift_show.get(0).getDistrict() + "," + gift_show.get(0).getState() + ", " + gift_show.get(0).getCountry()+ " - " + gift_show.get(0).getPincode());
                        Glide.with(getApplicationContext()).load(gift_show.get(0).getLogo())
                                //.error(R.drawable.gift_1)
                                .diskCacheStrategy(DiskCacheStrategy.ALL)
                                .into(company_logo);
                        owner_name.setText(gift_show.get(0).getName());
                        System.out.println("gift_show== " + gift_show.size());

                    }

                    Utils_Class.mProgress.dismiss();

                }
                System.out.println("======response :" + response);
            }

            @Override
            public void onFailure(Call<ArrayList<Gift_Cat>> call, Throwable t) {
                System.out.println("======response t:" + t);
            }
        });
    }

}