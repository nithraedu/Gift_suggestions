package nithra.gift.suggestion.shop.birthday.marriage.Fragment;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Paint;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.HashMap;

import nithra.gift.suggestion.shop.birthday.marriage.ActivitySecond;
import nithra.gift.suggestion.shop.birthday.marriage.Full_Details;
import nithra.gift.suggestion.shop.birthday.marriage.R;
import nithra.gift.suggestion.shop.birthday.marriage.Retrofit.Fav_Add_Del;
import nithra.gift.suggestion.shop.birthday.marriage.Retrofit.Gift_Cat;
import nithra.gift.suggestion.shop.birthday.marriage.Retrofit.RetrofitAPI;
import nithra.gift.suggestion.shop.birthday.marriage.Retrofit.RetrofitApiClient;
import nithra.gift.suggestion.shop.birthday.marriage.SharedPreference;
import nithra.gift.suggestion.shop.birthday.marriage.Utils_Class;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Sellerproducts extends Fragment {
    public  static Adapter adapter;
    public  static  ArrayList<Gift_Cat> gift_show;
    ArrayList<Fav_Add_Del> fav_add_dels;
    RecyclerView list;
    Intent intent;
    Bundle extra;
    String title, title1, title3;
    SharedPreference sharedPreference = new SharedPreference();
    LinearLayout no_item;
    SwipeRefreshLayout pullToRefresh;
    SQLiteDatabase mydb;
    ArrayList<String> gift_id = new ArrayList<>();
    String send_id;
    int check_fav = 0;
    ProgressDialog mProgress;


    public Sellerproducts() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
         View view=inflater.inflate(R.layout.fragment_sellerproducts, container, false);
        gift_show = new ArrayList<Gift_Cat>();
        list = view.findViewById(R.id.list);
        no_item = view.findViewById(R.id.no_item);
        pullToRefresh = view.findViewById(R.id.pullToRefresh);
        fav_add_dels = new ArrayList<Fav_Add_Del>();




        intent = getActivity().getIntent();
        extra = intent.getExtras();
        if (extra != null) {
            title = extra.getString("title");
            title1 = extra.getString("cat_idd");
            title3 = extra.getString("gender_id");
            System.out.println("gender= " + title3);
        }



        //Utils_Class.mProgress(getContext(), "Loading please wait...", false).show();
        mProgress = new ProgressDialog(getContext());
        mProgress.show();
        mProgress.setMessage("Loading please wait...");

        GridLayoutManager gridLayoutManager = new GridLayoutManager(getContext(), 2, GridLayoutManager.VERTICAL, false);
        list.setLayoutManager(gridLayoutManager);
        adapter = new Adapter(getContext(), gift_show);
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

        return view;
    }


    public void get_cat() {
        HashMap<String, String> map = new HashMap<>();
        map.put("action", "get_cat");
        map.put("gift_category", title1);
        map.put("user_id", sharedPreference.getString(getContext(), "android_userid"));
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
                    mProgress.dismiss();

                }
                System.out.println("======response :" + response);
            }

            @Override
            public void onFailure(Call<ArrayList<Gift_Cat>> call, Throwable t) {
                System.out.println("======response t:" + t);
            mProgress.dismiss();

            }
        });
    }

    public void get_cat1() {
        HashMap<String, String> map = new HashMap<>();
        map.put("action", "get_cat");
        map.put("gift_for", title3);
        map.put("user_id", sharedPreference.getString(getContext(), "android_userid"));
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
                 mProgress.dismiss();
                }
                System.out.println("======response :" + response);
            }

            @Override
            public void onFailure(Call<ArrayList<Gift_Cat>> call, Throwable t) {
                System.out.println("======response t:" + t);
                mProgress.dismiss();

            }
        });
    }


    public void fav(String id_gift, int pos) {
        HashMap<String, String> map = new HashMap<>();
        map.put("action", "favourite");
        map.put("gift_id", id_gift);
        map.put("user_id", sharedPreference.getString(getContext(), "android_userid"));

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
                        if (response.body().get(0).getFvAction()==1) {
                            gift_show.get(pos).setFav(1);
                            //Favourite.set_flag=1;
                            Utils_Class.toast_center(getContext(), "Your gift added to favourite...");
                            System.out.println("print__id1== "+gift_show.get(pos).getId());
                        } else {
                            gift_show.get(pos).setFav(0);
                            Utils_Class.toast_center(getContext(), "Your gift removed from favourite...");
                        }
                        adapter.notifyDataSetChanged();
                    }
                    Utils_Class.mProgress.dismiss();

                }
                System.out.println("======response :" + response);
            }

            @Override
            public void onFailure(Call<ArrayList<Fav_Add_Del>> call, Throwable t) {
                System.out.println("======response t:" + t);
            }
        });
    }

    public class Adapter extends RecyclerView.Adapter<Adapter.ViewHolder> {
        //ArrayList<Gift_Cat> gift_show;
        LayoutInflater inflater;
        Context context;

        public Adapter(Context ctx, ArrayList<Gift_Cat> gift_show) {
            //this.gift_show = gift_show;
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

            if (gift_show.get(pos).getFav() == 1) {
                holder.favourite.setBackgroundResource(R.drawable.favorite_red);
            } else {
                holder.favourite.setBackgroundResource(R.drawable.favorite_grey);
            }
            holder.giftprize.setPaintFlags(holder.giftprize.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);

            String currentString = gift_show.get(pos).getGiftImage();
            String[] separated = currentString.split(",");


            Glide.with(context).load(separated[0])
                    .error(R.drawable.ic_gift_default_img)
                    .placeholder(R.drawable.ic_gift_default_img)
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .into(holder.img_slide);
            holder.gridText.setText(gift_show.get(pos).getGiftName());
            //holder.head.setText(title);
            holder.head.setText(gift_show.get(pos).getDiscount() + "% offer");
            holder. giftprize.setText("\u20B9 " + gift_show.get(pos).getTotalAmount());
            holder. offerprize.setText("\u20B9 " + gift_show.get(pos).getGiftAmount());
            holder.category.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent i = new Intent(getContext(), Full_Details.class);
                    i.putExtra("full_view", gift_show.get(pos).getId());
                    i.putExtra("position", pos);
                    //i.putExtra("favourite",gift_show.get(pos).getFav());
                    startActivity(i);
                }
            });

            holder.favourite.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    fav(gift_show.get(pos).getId(), pos);

                }
            });
        }


        @Override
        public int getItemCount() {
            return gift_show.size();
        }

        public class ViewHolder extends RecyclerView.ViewHolder {
            ImageView img_slide, favourite;
            TextView gridText, head,giftprize, offerprize;
            CardView category;

            public ViewHolder(@NonNull View itemView) {
                super(itemView);
                img_slide = itemView.findViewById(R.id.imageGrid);
                favourite = itemView.findViewById(R.id.favourite);
                gridText = itemView.findViewById(R.id.gridText);
                head = itemView.findViewById(R.id.head);
                category = itemView.findViewById(R.id.category);
                giftprize = itemView.findViewById(R.id.giftprize);
                offerprize = itemView.findViewById(R.id.offerprize);
            }
        }
    }

}