package nithra.tamil.word.game.giftsuggestions;

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

import nithra.tamil.word.game.giftsuggestions.Retrofit.Gift_Cat;
import nithra.tamil.word.game.giftsuggestions.Retrofit.RetrofitAPI;
import nithra.tamil.word.game.giftsuggestions.Retrofit.RetrofitApiClient;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ActivitySecond extends AppCompatActivity {
    Adapter adapter;
    ArrayList<Gift_Cat> gift_show;
    RecyclerView list;
    ImageView back;
    Intent intent;
    Bundle extra;
    String title,title1,title3;
    TextView cat_title;
    SharedPreference sharedPreference = new SharedPreference();
    LinearLayout no_item;
    SwipeRefreshLayout pullToRefresh;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_second);
        gift_show = new ArrayList<Gift_Cat>();
        list = findViewById(R.id.list);
        cat_title = findViewById(R.id.cat_title);
        back = findViewById(R.id.back);
        no_item = findViewById(R.id.no_item);
        pullToRefresh = findViewById(R.id.pullToRefresh);

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        intent = getIntent();
        extra = intent.getExtras();
        if (extra != null) {
            title = extra.getString("title");
            title1 = extra.getString("cat_idd");
            title3 = extra.getString("gender_id");
        }


        cat_title.setText("" + title + " Gifts");

        Utils_Class.mProgress(ActivitySecond.this, "Loading please wait...", false).show();


        GridLayoutManager gridLayoutManager = new GridLayoutManager(this, 2, GridLayoutManager.VERTICAL, false);
        list.setLayoutManager(gridLayoutManager);
        adapter = new Adapter(this, gift_show);
        list.setAdapter(adapter);
        get_cat();
        get_cat1();

        pullToRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                gift_show.clear();
                get_cat();
                get_cat1();
                pullToRefresh.setRefreshing(false);
            }
        });
    }


    public void get_cat() {
        HashMap<String, String> map = new HashMap<>();
        map.put("action", "get_cat");
        map.put("gift_category", title1);
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
                        System.out.println("gift_show== " + gift_show.size());

                        adapter.notifyDataSetChanged();
                    }
                    if (gift_show.size() == 0) {
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
            public void onFailure(Call<ArrayList<Gift_Cat>> call, Throwable t) {
                System.out.println("======response t:" + t);
            }
        });
    }

    public void get_cat1() {
        HashMap<String, String> map = new HashMap<>();
        map.put("action", "get_cat");
        map.put("gift_for", title3);
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
                        System.out.println("gift_show== " + gift_show.size());

                        adapter.notifyDataSetChanged();
                    }
                    if (gift_show.size() == 0) {
                        list.setVisibility(View.GONE);
                        no_item.setVisibility(View.VISIBLE);
                    } else {
                        list.setVisibility(View.VISIBLE);
                        no_item.setVisibility(View.GONE);
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


    public class Adapter extends RecyclerView.Adapter<Adapter.ViewHolder> {
        ArrayList<Gift_Cat> gift_show;
        LayoutInflater inflater;
        Context context;

        public Adapter(Context ctx, ArrayList<Gift_Cat> gift_show) {
            this.gift_show = gift_show;
            this.inflater = LayoutInflater.from(ctx);
            this.context = ctx;
        }

        @NonNull
        @Override
        public Adapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
            View listItem = layoutInflater.inflate(R.layout.adapter, parent, false);
            Adapter.ViewHolder viewHolder = new Adapter.ViewHolder(listItem);
            return viewHolder;
        }

        @Override
        public void onBindViewHolder(@NonNull Adapter.ViewHolder holder, int position) {
            int pos = position;

            Glide.with(context).load(gift_show.get(pos).getGiftImage())
                    //.error(R.drawable.gift_1)
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .into(holder.img_slide);
            holder.gridText.setText(gift_show.get(pos).getGiftName());
            //holder.head.setText(title);
            holder.head.setText(gift_show.get(pos).getDiscount()+"% offer");
            holder.category.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent i=new Intent(ActivitySecond.this,Full_Details.class);
                    i.putExtra("full_view",gift_show.get(pos).getId());
                    startActivity(i);
                }
            });
        }


        @Override
        public int getItemCount() {
            return gift_show.size();
        }

        public class ViewHolder extends RecyclerView.ViewHolder {
            ImageView img_slide;
            TextView gridText, head;
            CardView category;

            public ViewHolder(@NonNull View itemView) {
                super(itemView);
                img_slide = itemView.findViewById(R.id.imageGrid);
                gridText = itemView.findViewById(R.id.gridText);
                head = itemView.findViewById(R.id.head);
                category=itemView.findViewById(R.id.category);
            }
        }
    }
}