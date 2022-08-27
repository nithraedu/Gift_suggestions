package nithra.tamil.word.game.giftsuggestions.Fragment;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.HashMap;

import nithra.tamil.word.game.giftsuggestions.FragMove;
import nithra.tamil.word.game.giftsuggestions.Full_Details;
import nithra.tamil.word.game.giftsuggestions.R;
import nithra.tamil.word.game.giftsuggestions.Retrofit.Fav_Add_Del;
import nithra.tamil.word.game.giftsuggestions.Retrofit.Fav_view;
import nithra.tamil.word.game.giftsuggestions.Retrofit.RetrofitAPI;
import nithra.tamil.word.game.giftsuggestions.Retrofit.RetrofitApiClient;
import nithra.tamil.word.game.giftsuggestions.SharedPreference;
import nithra.tamil.word.game.giftsuggestions.Utils_Class;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Favourite extends Fragment {
    public static Adapter adapter;
    public static ArrayList<Fav_view> fav_show;
    RecyclerView list;
    LinearLayout no_item;
    SwipeRefreshLayout pullToRefresh;
    SharedPreference sharedPreference = new SharedPreference();
    FragMove fragMove;
    ImageView back;
    public static int set_flag = 0;

    public Favourite() {
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_favourite, container, false);
        fav_show = new ArrayList<Fav_view>();
        list = view.findViewById(R.id.list);
        no_item = view.findViewById(R.id.no_item);
        pullToRefresh = view.findViewById(R.id.pullToRefresh);
        back = view.findViewById(R.id.back);
        fragMove = (FragMove) getContext();

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fragMove.home();
            }
        });


        Utils_Class.mProgress(getContext(), "Loading please wait...", false).show();

        GridLayoutManager gridLayoutManager = new GridLayoutManager(getContext(), 2, GridLayoutManager.VERTICAL, false);
        list.setLayoutManager(gridLayoutManager);
        adapter = new Adapter(getContext(), fav_show);
        list.setAdapter(adapter);
        //fav();
        pullToRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                fav_show.clear();
                //fav();
                pullToRefresh.setRefreshing(false);
            }
        });
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        fav();
    }

    public void fav() {
        fav_show.clear();
        HashMap<String, String> map = new HashMap<>();
        map.put("action", "get_fav");
        //map.put("user_id",sharedPreference.getString(getContext(), "android_userid"));
        map.put("user_id", sharedPreference.getString(getContext(), "android_userid"));

        System.out.println("print_map==" + map);
        RetrofitAPI retrofitAPI = RetrofitApiClient.getRetrofit().create(RetrofitAPI.class);
        Call<ArrayList<Fav_view>> call = retrofitAPI.fav_view(map);
        call.enqueue(new Callback<ArrayList<Fav_view>>() {
            @Override
            public void onResponse(Call<ArrayList<Fav_view>> call, Response<ArrayList<Fav_view>> response) {
                if (response.isSuccessful()) {
                    String result = new Gson().toJson(response.body());
                    System.out.println("======response result:" + result);
                    if (response.body().get(0).getStatus().equals("Success")) {
                        fav_show.addAll(response.body());
                        adapter.notifyDataSetChanged();
                    }
                    if (fav_show.size() == 0) {
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
            public void onFailure(Call<ArrayList<Fav_view>> call, Throwable t) {
                System.out.println("======response t:" + t);
            }
        });
    }


    public void fav1(String id_gift, int pos) {
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
                        if (response.body().get(0).getFvAction() == 1) {
                            fav_show.get(pos).setFav(1);
                            Utils_Class.toast_center(getContext(), "Your gift added to favourite...");

                        } else {
                            fav_show.get(pos).setFav(0);
                            Utils_Class.toast_center(getContext(), "Your gift removed from favourite...");
                            System.out.println("print__id== " + fav_show.get(pos).getGiftId());
                        }
                        //fav();
                        adapter.notifyDataSetChanged();
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


    public class Adapter extends RecyclerView.Adapter<Adapter.ViewHolder> {
        // ArrayList<Fav_view> gift_show;
        LayoutInflater inflater;
        Context context;

        public Adapter(Context ctx, ArrayList<Fav_view> gift_show) {
            // this.gift_show = gift_show;
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

            if (fav_show.get(pos).getFav() == 1) {
                holder.favourite.setBackgroundResource(R.drawable.favorite_red);
            } else {
                holder.favourite.setBackgroundResource(R.drawable.favorite_grey);
            }

            Glide.with(context).load(fav_show.get(pos).getGiftImage())
                    //.error(R.drawable.gift_1)
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .into(holder.img_slide);
            holder.gridText.setText(fav_show.get(pos).getGiftName());
            holder.head.setText(fav_show.get(pos).getDiscount() + "% offer");
            holder.category.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent i = new Intent(getContext(), Full_Details.class);
                    i.putExtra("full_view", fav_show.get(pos).getGiftId());
                    //i.putExtra("favourite",fav_show.get(pos).getFav());
                    startActivity(i);
                }
            });

            holder.favourite.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    fav1(fav_show.get(pos).getGiftId(), pos);

                }
            });
        }


        @Override
        public int getItemCount() {
            return fav_show.size();
        }

        public class ViewHolder extends RecyclerView.ViewHolder {
            ImageView img_slide, favourite;
            TextView gridText, head;
            CardView category;

            public ViewHolder(@NonNull View itemView) {
                super(itemView);
                img_slide = itemView.findViewById(R.id.imageGrid);
                gridText = itemView.findViewById(R.id.gridText);
                head = itemView.findViewById(R.id.head);
                category = itemView.findViewById(R.id.category);
                favourite = itemView.findViewById(R.id.favourite);
            }
        }
    }


}