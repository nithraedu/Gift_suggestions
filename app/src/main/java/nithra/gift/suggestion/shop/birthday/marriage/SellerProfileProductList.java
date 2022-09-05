package nithra.gift.suggestion.shop.birthday.marriage;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.HashMap;

import nithra.gift.suggestion.shop.birthday.marriage.Otp.ProductAdd;
import nithra.gift.suggestion.shop.birthday.marriage.Otp.ShopAdd;
import nithra.gift.suggestion.shop.birthday.marriage.Retrofit.GiftList;
import nithra.gift.suggestion.shop.birthday.marriage.Retrofit.RetrofitAPI;
import nithra.gift.suggestion.shop.birthday.marriage.Retrofit.RetrofitApiClient;
import nithra.gift.suggestion.shop.birthday.marriage.Retrofit.SellerProfilePojo;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SellerProfileProductList extends AppCompatActivity {
    ImageView IVPreviewImage, back;
    TextView seller_name, shop_name, city, profile_edit, add_product;
    SharedPreference sharedPreference = new SharedPreference();
    ArrayList<SellerProfilePojo> gift;
    Adapter adapter;
    ArrayList<GiftList> gift_ada;
    RecyclerView list;
    SwipeRefreshLayout pullToRefresh;
    LinearLayout no_item;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.seller_profile_productlist);
        IVPreviewImage = findViewById(R.id.IVPreviewImage);
        seller_name = findViewById(R.id.seller_name);
        shop_name = findViewById(R.id.shop_name);
        city = findViewById(R.id.city);
        profile_edit = findViewById(R.id.profile_edit);
        add_product = findViewById(R.id.add_product);
        gift = new ArrayList<SellerProfilePojo>();
        gift_ada = new ArrayList<GiftList>();
        list = findViewById(R.id.list);
        back = findViewById(R.id.back);
        pullToRefresh = findViewById(R.id.pullToRefresh);
        no_item = findViewById(R.id.no_item);

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
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

        add_product.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (sharedPreference.getInt(SellerProfileProductList.this, "profile") == 1) {
                    Intent i = new Intent(SellerProfileProductList.this, ShopAdd.class);
                    startActivity(i);
                } else if (sharedPreference.getInt(SellerProfileProductList.this, "profile") == 2) {
                    Intent i = new Intent(SellerProfileProductList.this, ProductAdd.class);
                    startActivity(i);
                }
            }
        });
        GridLayoutManager gridLayoutManager = new GridLayoutManager(this, 1, GridLayoutManager.VERTICAL, false);
        list.setLayoutManager(gridLayoutManager);
        adapter = new Adapter(this, gift_ada);
        list.setAdapter(adapter);
        Utils_Class.mProgress(this, "Loading please wait...", false).show();
        category();
        category1();
        pullToRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                gift.clear();
                category1();
                pullToRefresh.setRefreshing(false);
            }
        });

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
                    shop_name.setText(gift.get(0).getShopName() + ", ");
                   /* mobile.setText(gift.get(0).getSellerMobile());
                    mail.setText(gift.get(0).getShopEmail());
                    web.setText(gift.get(0).getShopWebsite());
                    address.setText(gift.get(0).getAddress());
                    pincode.setText(gift.get(0).getPincode());
                    state.setText(gift.get(0).getState());
                    district.setText(gift.get(0).getDistrict());*/
                    city.setText(gift.get(0).getCity());
                   /* country.setTag(gift.get(0).getCountry());
                    latitude.setText(gift.get(0).getLatitude());
                    longitude.setText(gift.get(0).getLongitude());
                    total_gifts.setText("My total gifts : " + gift.get(0).getTotalGifts());*/
                    Utils_Class.mProgress.dismiss();

                   /* if (gift.get(0).getShopWebsite().equals("")) {
                        web_gone.setVisibility(View.GONE);
                    }*/

                }
                System.out.println("======response :" + response);
            }

            @Override
            public void onFailure(Call<ArrayList<SellerProfilePojo>> call, Throwable t) {
                System.out.println("======response t:" + t);
            }
        });
    }

    public void category1() {
        HashMap<String, String> map = new HashMap<>();
        map.put("action", "get_gift_list");
        map.put("user_id", sharedPreference.getString(this, "user_id"));

        RetrofitAPI retrofitAPI = RetrofitApiClient.getRetrofit().create(RetrofitAPI.class);
        Call<ArrayList<GiftList>> call = retrofitAPI.gift_giftlist(map);
        call.enqueue(new Callback<ArrayList<GiftList>>() {
            @Override
            public void onResponse(Call<ArrayList<GiftList>> call, Response<ArrayList<GiftList>> response) {
                if (response.isSuccessful()) {
                    String result = new Gson().toJson(response.body());
                    System.out.println("======response result:" + result);
                    if (response.body().get(0).getStatus().equals("Success")) {
                        gift_ada.clear();
                        gift_ada.addAll(response.body());
                        System.out.println("print_size==" + gift_ada.size());
                        adapter.notifyDataSetChanged();
                    }
                    if (gift_ada.size() == 0) {
                        pullToRefresh.setVisibility(View.GONE);
                        no_item.setVisibility(View.VISIBLE);
                    } else {
                        pullToRefresh.setVisibility(View.VISIBLE);
                        no_item.setVisibility(View.GONE);
                    }
                    Utils_Class.mProgress.dismiss();
                }
                System.out.println("======response :" + response);
            }

            @Override
            public void onFailure(Call<ArrayList<GiftList>> call, Throwable t) {
                System.out.println("======response t:" + t);
            }
        });
    }

    public class Adapter extends RecyclerView.Adapter<Adapter.ViewHolder> {
        ArrayList<GiftList> gift;
        LayoutInflater inflater;
        Context context;

        public Adapter(Context ctx, ArrayList<GiftList> images) {
            this.gift = images;
            this.inflater = LayoutInflater.from(ctx);
            this.context = ctx;
        }

        @NonNull
        @Override
        public Adapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
            View listItem = layoutInflater.inflate(R.layout.adapter_myproduct, parent, false);
            Adapter.ViewHolder viewHolder = new Adapter.ViewHolder(listItem);
            return viewHolder;
        }

        @Override
        public void onBindViewHolder(@NonNull Adapter.ViewHolder holder, int position) {
            int pos = position;
           /* holder.img_slide.setImageResource(images.get(position));
            holder.gridText.setText(titles.get(position));*/

            holder.gridText.setText(gift.get(pos).getGiftName());

            String currentString = gift.get(pos).getGiftImage();
            String[] separated = currentString.split(",");

            Glide.with(context).load(separated[0])
                    //.error(R.drawable.gift_1)
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .into(holder.img_slide);
            System.out.println("print_img " + gift.get(pos).getGiftImage());
            holder.head.setText(gift.get(pos).getDiscount() + "% offer");

            holder.edit_product.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //sharedPreference.getString(this, "gift_id");
                    Intent i = new Intent(getApplicationContext(), ProductEdit.class);
                    i.putExtra("id", gift.get(pos).getId());
                    startActivity(i);
                }
            });
            holder.category.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent i = new Intent(getApplicationContext(), ProductFullView.class);
                    i.putExtra("id", gift.get(pos).getId());
                    startActivity(i);
                }
            });
        }


        @Override
        public int getItemCount() {
            return gift.size();
        }

        public class ViewHolder extends RecyclerView.ViewHolder {
            ImageView img_slide;
            TextView gridText, edit_product, head;
            CardView category;

            public ViewHolder(@NonNull View itemView) {
                super(itemView);
                img_slide = itemView.findViewById(R.id.imageGrid);
                gridText = itemView.findViewById(R.id.gridText);
                edit_product = itemView.findViewById(R.id.edit_product);
                category = itemView.findViewById(R.id.category);
                head = itemView.findViewById(R.id.head);
            }
        }
    }

}
