package nithra.gift.suggestion.shop.birthday.marriage;

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

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.browser.customtabs.CustomTabsIntent;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.HashMap;

import nithra.gift.suggestion.shop.birthday.marriage.Retrofit.Fav_Add_Del;
import nithra.gift.suggestion.shop.birthday.marriage.Retrofit.Fav_view;
import nithra.gift.suggestion.shop.birthday.marriage.Retrofit.Gift_Cat;
import nithra.gift.suggestion.shop.birthday.marriage.Retrofit.RetrofitAPI;
import nithra.gift.suggestion.shop.birthday.marriage.Retrofit.RetrofitApiClient;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Full_Details extends AppCompatActivity {
    ArrayList<Gift_Cat> gift_show;
    Intent intent;
    Bundle extra;
    String id_gift;
    ImageView back, company_logo, IVPreviewImage, fav;
    TextView giftname, giftprize, offerprize, description, detail_shop_name, detail_add, owner_name, website, email, head;
    LinearLayout phone, web_gone;
    CardView card_mail, card_web;
    SharedPreference sharedPreference = new SharedPreference();
    ArrayList<Fav_view> fav_show;
    SwipeRefreshLayout pullToRefresh;
    int pos_id;
    TextView btShowmore;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.buyer_view);
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
        web_gone = findViewById(R.id.web_gone);
        head = findViewById(R.id.head);
        fav = findViewById(R.id.fav);
        btShowmore = findViewById(R.id.btShowmore);
        fav_show = new ArrayList<Fav_view>();
        // pullToRefresh = findViewById(R.id.pullToRefresh);

        intent = getIntent();
        extra = intent.getExtras();
        id_gift = extra.getString("full_view");
        pos_id = extra.getInt("position");

        giftprize.setPaintFlags(giftprize.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);

        Utils_Class.mProgress(Full_Details.this, "Loading please wait...", false).show();

        btShowmore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (btShowmore.getText().toString().equalsIgnoreCase("Show more...")) {
                    description.setMaxLines(Integer.MAX_VALUE);//your TextView
                    btShowmore.setText("Show less");
                } else {
                    description.setMaxLines(3);//your TextView
                    btShowmore.setText("Show more...");
                }
            }
        });
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        get_cat();


        fav.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fav1();
            }
        });

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
                String currentString = gift_show.get(0).getGiftImage();
                String[] separated = currentString.split(",");
                Intent i = new Intent(getApplicationContext(), ImageSlide.class);
                i.putExtra("pos", currentString);
                startActivity(i);

               /* ImageView img_view;
                Dialog dialog = new Dialog(Full_Details.this, android.R.style.Theme_DeviceDefault);
                dialog.setContentView(R.layout.image_view);
                //dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
                dialog.setCanceledOnTouchOutside(true);
                img_view = dialog.findViewById(R.id.img_view);
                Glide.with(getApplicationContext()).load(separated[0])
                        //.error(R.drawable.gift_1)
                        .diskCacheStrategy(DiskCacheStrategy.ALL)
                        .into(img_view);
                dialog.show();*/
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
       /* pullToRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                get_cat();
                pullToRefresh.setRefreshing(false);
            }
        });*/

    }


    public void get_cat() {
        HashMap<String, String> map = new HashMap<>();
        map.put("action", "get_cat");
        map.put("id", id_gift);
        map.put("user_id", sharedPreference.getString(getApplicationContext(), "android_userid"));
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
                        String currentString = gift_show.get(0).getGiftImage();
                        String[] separated = currentString.split(",");

                        Glide.with(getApplicationContext()).load(separated[0])
                                //.error(R.drawable.gift_1)
                                .diskCacheStrategy(DiskCacheStrategy.ALL)
                                .into(IVPreviewImage);


                        giftname.setText(gift_show.get(0).getGiftName());
                        giftprize.setText("\u20B9 " + gift_show.get(0).getTotalAmount());
                        offerprize.setText("\u20B9 " + gift_show.get(0).getGiftAmount());
                        description.setText(gift_show.get(0).getGiftDescription());
                        detail_shop_name.setText(gift_show.get(0).getShopName());
                        //detail_add.setText(gift_show.get(0).getAddress() + ", " + gift_show.get(0).getCity() + ", " + gift_show.get(0).getDistrict() + "," + gift_show.get(0).getState() + ", " + gift_show.get(0).getCountry() + " - " + gift_show.get(0).getPincode());
                        detail_add.setText(gift_show.get(0).getAddress() + ", " + gift_show.get(0).getCity() + ", " + gift_show.get(0).getDistrict() + " - " + gift_show.get(0).getPincode() + "\n" + gift_show.get(0).getState() + ", " + gift_show.get(0).getCountry());
                       /* Glide.with(getApplicationContext()).load(gift_show.get(0).getLogo())
                                //.error(R.drawable.gift_1)
                                .diskCacheStrategy(DiskCacheStrategy.ALL)
                                .into(company_logo);*/
                        //owner_name.setText(gift_show.get(0).getName());
                        /*website.setText(gift_show.get(0).getShopWebsite());
                        email.setText(gift_show.get(0).getShopEmail());*/
                        head.setText(gift_show.get(0).getDiscount() + "% offer");

                        System.out.println("gift_show== " + gift_show.size());

                        if (gift_show.get(0).getShopWebsite().equals("")) {
                            web_gone.setVisibility(View.GONE);
                        }

                        if (gift_show.get(0).getFav() == 1) {
                            fav.setBackgroundResource(R.drawable.favorite_red);
                        } else {
                            fav.setBackgroundResource(R.drawable.favorite_grey);
                        }

                        if (description.getLineCount() > 3) {
                            btShowmore.setVisibility(View.VISIBLE);
                        } else {
                            btShowmore.setVisibility(View.GONE);
                        }
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


    public void fav1() {
        // fav_show.clear();
        HashMap<String, String> map = new HashMap<>();
        map.put("action", "favourite");
        map.put("gift_id", id_gift);
        map.put("user_id", sharedPreference.getString(Full_Details.this, "android_userid"));

        System.out.println("favroute==" + map);
        RetrofitAPI retrofitAPI = RetrofitApiClient.getRetrofit().create(RetrofitAPI.class);
        Call<ArrayList<Fav_Add_Del>> call = retrofitAPI.fav_add_del(map);
        call.enqueue(new Callback<ArrayList<Fav_Add_Del>>() {
            @Override
            public void onResponse(Call<ArrayList<Fav_Add_Del>> call, Response<ArrayList<Fav_Add_Del>> response) {
                if (response.isSuccessful()) {
                    String result = new Gson().toJson(response.body());
                    System.out.println("======response result:" + result);
                    if (response.body().get(0).getStatus().equals("Success")) {
                        if (response.body().get(0).getFvAction() == 1) {
                            gift_show.get(0).setFav(1);
                            ActivitySecond.gift_show.get(pos_id).setFav(1);
                            //Favourite.fav_show.get(0).setFav(1);
                            fav.setBackgroundResource(R.drawable.favorite_red);
                            Utils_Class.toast_center(Full_Details.this, "Your gift added to favourite...");
                        } else {
                            gift_show.get(0).setFav(0);
                            ActivitySecond.gift_show.get(pos_id).setFav(0);
                            //Favourite.fav_show.get(0).setFav(0);
                            fav.setBackgroundResource(R.drawable.favorite_grey);
                            Utils_Class.toast_center(Full_Details.this, "Your gift removed from favourite...");
                        }
                        ActivitySecond.adapter.notifyDataSetChanged();
                        //Favourite.adapter.notifyDataSetChanged();
                    }
                }
                System.out.println("======response :" + response);
            }

            @Override
            public void onFailure(Call<ArrayList<Fav_Add_Del>> call, Throwable t) {
                System.out.println("======response t:" + t);
            }
        });
    }

    public class Frag_Adapter extends FragmentStateAdapter {

        private ArrayList<Fragment> fragmentList = new ArrayList<>();

        public Frag_Adapter(@NonNull FragmentActivity fragmentActivity) {
            super(fragmentActivity);
        }


        @NonNull
        @Override
        public Fragment createFragment(int position) {
            return fragmentList.get(position);
        }

        public void addFragment(Fragment fragment) {
            fragmentList.add(fragment);
        }

        @Override
        public int getItemCount() {
            return fragmentList.size();
        }
    }

}