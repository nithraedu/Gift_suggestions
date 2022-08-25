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
import androidx.browser.customtabs.CustomTabsIntent;
import androidx.cardview.widget.CardView;

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
    TextView giftname, giftprize, offerprize, description, detail_shop_name, detail_add, owner_name,website,email;
    LinearLayout phone;
    CardView card_mail,card_web;

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
        website = findViewById(R.id.website);
        email = findViewById(R.id.email);
        card_mail = findViewById(R.id.card_mail);
        card_web = findViewById(R.id.card_web);
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


        card_mail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (gift_show.get(0).getShopEmail() != null && !gift_show.get(0).getShopEmail().trim().isEmpty()) {
                    if (Utils_Class.isNetworkAvailable(Full_Details.this)) {
                        Intent intent = new Intent(Intent.ACTION_SENDTO);
                        intent.setData(Uri.parse("mailto:")); // only email apps should handle this
                        intent.putExtra(Intent.EXTRA_EMAIL, new String[]{gift_show.get(0).getShopEmail().trim()});
                        intent.putExtra(Intent.EXTRA_SUBJECT, "Gift Suggestions");
                        intent.putExtra(Intent.EXTRA_TEXT, "Body Here");
                        if (intent.resolveActivity(getPackageManager()) != null) {
                            startActivity(intent);
                        }
                    } else {
                        Utils_Class.toast_center(Full_Details.this, "Check Your Internet Connection...");
                    }
                } else {
                    Utils_Class.toast_center(Full_Details.this, "Email not available...");
                }
            }
        });

        card_web.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (gift_show.get(0).getShopWebsite() != null && !gift_show.get(0).getShopWebsite().trim().isEmpty()) {
                    if (Utils_Class.isNetworkAvailable(Full_Details.this)) {
                        String url = gift_show.get(0).getShopWebsite().trim();
                        System.out.println("urlprint" + url);
                        CustomTabsIntent.Builder builder = new CustomTabsIntent.Builder();
                        CustomTabsIntent customTabsIntent = builder.build();
                        customTabsIntent.launchUrl(Full_Details.this, Uri.parse(url));
                    } else {
                        Utils_Class.toast_center(Full_Details.this, "Check Your Internet Connection...");
                    }
                } else {
                    Utils_Class.toast_center(Full_Details.this, "Website not available...");
                }
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
                        giftprize.setText("\u20B9 " + gift_show.get(0).getTotalAmount());
                        offerprize.setText("\u20B9 " + gift_show.get(0).getGiftAmount());
                        description.setText(gift_show.get(0).getGiftDescription());
                        detail_shop_name.setText(gift_show.get(0).getShopName());
                        detail_add.setText(gift_show.get(0).getAddress() + ", " + gift_show.get(0).getCity() + ", " + gift_show.get(0).getDistrict() + "," + gift_show.get(0).getState() + ", " + gift_show.get(0).getCountry()+ " - " + gift_show.get(0).getPincode());
                        Glide.with(getApplicationContext()).load(gift_show.get(0).getLogo())
                                //.error(R.drawable.gift_1)
                                .diskCacheStrategy(DiskCacheStrategy.ALL)
                                .into(company_logo);
                        owner_name.setText(gift_show.get(0).getName());
                        website.setText(gift_show.get(0).getShopWebsite());
                        email.setText(gift_show.get(0).getShopEmail());
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