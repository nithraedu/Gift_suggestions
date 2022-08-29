package nithra.gift.suggestion.shop.birthday.marriage.Fragment;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.google.android.material.navigation.NavigationView;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.HashMap;

import nithra.gift.suggestion.shop.birthday.marriage.ActivitySecond;
import nithra.gift.suggestion.shop.birthday.marriage.BuildConfig;
import nithra.gift.suggestion.shop.birthday.marriage.Otp.OtpSend;
import nithra.gift.suggestion.shop.birthday.marriage.Otp.ShopAdd;
import nithra.gift.suggestion.shop.birthday.marriage.R;
import nithra.gift.suggestion.shop.birthday.marriage.Retrofit.GiftFor;
import nithra.gift.suggestion.shop.birthday.marriage.Retrofit.Occasion;
import nithra.gift.suggestion.shop.birthday.marriage.Retrofit.RetrofitAPI;
import nithra.gift.suggestion.shop.birthday.marriage.Retrofit.RetrofitApiClient;
import nithra.gift.suggestion.shop.birthday.marriage.SellerProfile;
import nithra.gift.suggestion.shop.birthday.marriage.SharedPreference;
import nithra.gift.suggestion.shop.birthday.marriage.Utils_Class;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Home extends Fragment implements NavigationView.OnNavigationItemSelectedListener {
    DrawerLayout drawer;
    ArrayList<GiftFor> giftfor;
    ArrayList<Occasion> giftoccasion;
    Adapter2 adapter2;
    Adapter3 adapter3;
    LinearLayout notification, profile_view;
    SharedPreference sharedPreference = new SharedPreference();
    TextView code, name;
    int versionCode = BuildConfig.VERSION_CODE;
    String versionName = BuildConfig.VERSION_NAME;
    SwipeRefreshLayout pullToRefresh;

    public Home() {
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);
        Toolbar toolbar = view.findViewById(R.id.toolbar);
        drawer = view.findViewById(R.id.drawer_layout);
        notification = view.findViewById(R.id.notification);
        profile_view = view.findViewById(R.id.profile_view);
        giftfor = new ArrayList<GiftFor>();
        giftoccasion = new ArrayList<Occasion>();
        RecyclerView list = view.findViewById(R.id.list);
        RecyclerView list2 = view.findViewById(R.id.list2);
        pullToRefresh = view.findViewById(R.id.pullToRefresh);

        String user = sharedPreference.getString(getContext(), "user_status");

        profile_view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (sharedPreference.getInt(getContext(), "profile") == 1) {
                    Intent i = new Intent(getContext(), ShopAdd.class);
                    startActivity(i);
                } else if (sharedPreference.getInt(getContext(), "profile") == 2) {
                    Intent i = new Intent(getContext(), SellerProfile.class);
                    startActivity(i);
                } else {
                    Intent i = new Intent(getContext(), OtpSend.class);
                    startActivity(i);
                }
            }
        });


        ((AppCompatActivity) getActivity()).setSupportActionBar(toolbar);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                getActivity(), drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();
        ((AppCompatActivity) getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        ((AppCompatActivity) getActivity()).getSupportActionBar().setHomeButtonEnabled(true);
        ((AppCompatActivity) getActivity()).getSupportActionBar().setDisplayShowTitleEnabled(true);
        ((AppCompatActivity) getActivity()).getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_menu_navigate);
        NavigationView navigationView = view.findViewById(R.id.nav_mm_view);
        navigationView.setNavigationItemSelectedListener(this::onNavigationItemSelected);
        View v = navigationView.inflateHeaderView(R.layout.header);
        code = v.findViewById(R.id.code);
        name = v.findViewById(R.id.name);
        code.setText("" + versionCode);
        name.setText(versionName);


        GridLayoutManager gridLayoutManager2 = new GridLayoutManager(getContext(), 1, GridLayoutManager.HORIZONTAL, false);
        list.setLayoutManager(gridLayoutManager2);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(getContext(), 3, GridLayoutManager.VERTICAL, false);
        list2.setLayoutManager(gridLayoutManager);
        Utils_Class.mProgress(getContext(), "Loading please wait...", false).show();

        adapter2 = new Adapter2(getContext(), giftfor);
        list.setAdapter(adapter2);

        adapter3 = new Adapter3(getContext(), giftoccasion);
        list2.setAdapter(adapter3);


        gift_occasion();
        gender_gift();

        pullToRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                giftoccasion.clear();
                giftfor.clear();
                gift_occasion();
                gender_gift();
                pullToRefresh.setRefreshing(false);
            }
        });

        return view;
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        item.setChecked(false);
        int id = item.getItemId();
        if (id == R.id.nav_home) {
            drawer.closeDrawer(GravityCompat.START);

        } else if (id == R.id.nav_share) {

        } else if (id == R.id.nav_rateus) {

        } else if (id == R.id.nav_feedback) {

        } else if (id == R.id.nav_policy) {


        }

        drawer.closeDrawer(GravityCompat.START);
        return true;
    }


    public void gift_occasion() {
        HashMap<String, String> map = new HashMap<>();
        map.put("action", "category");

        RetrofitAPI retrofitAPI = RetrofitApiClient.getRetrofit().create(RetrofitAPI.class);
        Call<ArrayList<Occasion>> call = retrofitAPI.gift_occasion(map);
        call.enqueue(new Callback<ArrayList<Occasion>>() {
            @Override
            public void onResponse(Call<ArrayList<Occasion>> call, Response<ArrayList<Occasion>> response) {
                if (response.isSuccessful()) {
                    String result = new Gson().toJson(response.body());
                    System.out.println("======response result:" + result);
                    giftoccasion.addAll(response.body());
                    //sharedPreference.putString(getContext(), "occasio_id", giftoccasion.get(0).getId());

                    adapter3.notifyDataSetChanged();
                    Utils_Class.mProgress.dismiss();

                }
                System.out.println("======response :" + response);
            }

            @Override
            public void onFailure(Call<ArrayList<Occasion>> call, Throwable t) {
                System.out.println("======response t:" + t);
            }
        });
    }

    public void gender_gift() {
        HashMap<String, String> map = new HashMap<>();
        map.put("action", "gift_for");
        RetrofitAPI retrofitAPI = RetrofitApiClient.getRetrofit().create(RetrofitAPI.class);
        Call<ArrayList<GiftFor>> call = retrofitAPI.gift_giftfor(map);
        call.enqueue(new Callback<ArrayList<GiftFor>>() {
            @Override
            public void onResponse(Call<ArrayList<GiftFor>> call, Response<ArrayList<GiftFor>> response) {
                if (response.isSuccessful()) {
                    String result = new Gson().toJson(response.body());
                    System.out.println("======response result:" + result);
                    giftfor.addAll(response.body());
                    adapter2.notifyDataSetChanged();
                }
                System.out.println("======response :" + response);
            }

            @Override
            public void onFailure(Call<ArrayList<GiftFor>> call, Throwable t) {
                System.out.println("======response t:" + t);
            }
        });
    }


    public class Adapter2 extends RecyclerView.Adapter<Adapter2.ViewHolder> {
        ArrayList<GiftFor> giftfor;
        LayoutInflater inflater;
        Context context;


        public Adapter2(Context ctx, ArrayList<GiftFor> giftfor) {
            this.giftfor = giftfor;
            this.inflater = LayoutInflater.from(ctx);
            this.context = ctx;
        }

        @NonNull
        @Override
        public Adapter2.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
            View listItem = layoutInflater.inflate(R.layout.adapter_2, parent, false);
            Adapter2.ViewHolder viewHolder = new Adapter2.ViewHolder(listItem);
            return viewHolder;
        }

        @Override
        public void onBindViewHolder(@NonNull Adapter2.ViewHolder holder, int position) {
            int pos = position;

            Glide.with(context).load(giftfor.get(pos).getPeopleLogo())
                    //.error(R.drawable.gift_1)
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .into(holder.slide_mat);
            holder.gridText.setText(giftfor.get(pos).getPeople());
            holder.gender.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent i = new Intent(getContext(), ActivitySecond.class);
                    i.putExtra("title", giftfor.get(pos).getPeople());
                    i.putExtra("gender_id", giftfor.get(pos).getId());
                    startActivity(i);
                }
            });
        }

        @Override
        public int getItemCount() {
            return giftfor.size();
        }

        public class ViewHolder extends RecyclerView.ViewHolder {
            ImageView slide_mat;
            TextView gridText;
            LinearLayout gender;

            public ViewHolder(@NonNull View itemView) {
                super(itemView);
                slide_mat = itemView.findViewById(R.id.imageGrid);
                gridText = itemView.findViewById(R.id.gridText);
                gender = itemView.findViewById(R.id.gender);
            }
        }
    }


    public class Adapter3 extends RecyclerView.Adapter<Adapter3.ViewHolder> {
        ArrayList<Occasion> giftoccasion;
        LayoutInflater inflater;
        Context context;


        public Adapter3(Context ctx, ArrayList<Occasion> giftoccasion) {
            this.giftoccasion = giftoccasion;
            this.inflater = LayoutInflater.from(ctx);
            this.context = ctx;
        }

        @NonNull
        @Override
        public Adapter3.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
            View listItem = layoutInflater.inflate(R.layout.adapter_3, parent, false);
            Adapter3.ViewHolder viewHolder = new Adapter3.ViewHolder(listItem);
            return viewHolder;
        }

        @Override
        public void onBindViewHolder(@NonNull Adapter3.ViewHolder holder, int position) {
            int pos = position;
            Glide.with(context).load(giftoccasion.get(pos).getCategoryLogo())
                    //.error(R.drawable.gift_1)
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .into(holder.img_slide);
            holder.gridText.setText(giftoccasion.get(pos).getCategory());
            holder.category.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent i = new Intent(getContext(), ActivitySecond.class);
                    i.putExtra("title", giftoccasion.get(pos).getCategory());
                    i.putExtra("cat_idd", giftoccasion.get(pos).getId());
                    startActivity(i);
                }
            });

        }


        @Override
        public int getItemCount() {
            return giftoccasion.size();
        }

        public class ViewHolder extends RecyclerView.ViewHolder {
            ImageView img_slide;
            TextView gridText;
            CardView category;

            public ViewHolder(@NonNull View itemView) {
                super(itemView);
                img_slide = itemView.findViewById(R.id.imageGrid);
                gridText = itemView.findViewById(R.id.gridText);
                category = itemView.findViewById(R.id.category);
            }
        }
    }
}