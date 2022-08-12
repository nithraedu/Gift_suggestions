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
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.HashMap;

import nithra.tamil.word.game.giftsuggestions.Retrofit.GiftList;
import nithra.tamil.word.game.giftsuggestions.Retrofit.RetrofitAPI;
import nithra.tamil.word.game.giftsuggestions.Retrofit.RetrofitApiClient;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MyProduct extends AppCompatActivity {
    Adapter adapter;
    ArrayList<GiftList> gift;
    RecyclerView list;
    ImageView back;
    Bundle extra;
    String title;
    TextView cat_title,profile_edit;
    SharedPreference sharedPreference = new SharedPreference();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_my_product);
        gift = new ArrayList<GiftList>();
        list = findViewById(R.id.list);
        profile_edit = findViewById(R.id.profile_edit);
        cat_title = findViewById(R.id.cat_title);
        back = findViewById(R.id.back);

        profile_edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i=new Intent(getApplicationContext(),ShopEdit.class);
                startActivity(i);
            }
        });
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        cat_title.setText("My Products");

        GridLayoutManager gridLayoutManager = new GridLayoutManager(this, 2, GridLayoutManager.VERTICAL, false);
        list.setLayoutManager(gridLayoutManager);
        adapter = new Adapter(this, gift);
        list.setAdapter(adapter);
        category();
    }


    public void category() {
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
                    gift.addAll(response.body());
                    adapter.notifyDataSetChanged();
                }
                System.out.println("======response :" + response);
            }

            @Override
            public void onFailure(Call<ArrayList<GiftList>> call, Throwable t) {
                System.out.println("======response t:" + t);
            }
        });
    }


    public class Adapter extends RecyclerView.Adapter<MyProduct.Adapter.ViewHolder> {
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
        public MyProduct.Adapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
            View listItem = layoutInflater.inflate(R.layout.adapter_myproduct, parent, false);
            MyProduct.Adapter.ViewHolder viewHolder = new Adapter.ViewHolder(listItem);
            return viewHolder;
        }

        @Override
        public void onBindViewHolder(@NonNull MyProduct.Adapter.ViewHolder holder, int position) {
            int pos = position;
           /* holder.img_slide.setImageResource(images.get(position));
            holder.gridText.setText(titles.get(position));*/

            holder.gridText.setText(gift.get(pos).getGiftName());
            Glide.with(context).load(gift.get(position).getGiftImage())
                    //.error(R.drawable.warning)
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .into(holder.img_slide);

            holder.edit_product.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent i=new Intent(getApplicationContext(),ProductEdit.class);
                    startActivity(i);
                }
            });
        }


        @Override
        public int getItemCount() {
            return gift.size();
        }

        public class ViewHolder extends RecyclerView.ViewHolder {
            ImageView img_slide, edit_product;
            TextView gridText;

            public ViewHolder(@NonNull View itemView) {
                super(itemView);
                img_slide = itemView.findViewById(R.id.imageGrid);
                gridText = itemView.findViewById(R.id.gridText);
                edit_product = itemView.findViewById(R.id.edit_product);
            }
        }
    }

}