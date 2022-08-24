package nithra.tamil.word.game.giftsuggestions.Fragment;

import static android.content.Context.MODE_PRIVATE;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

import java.util.ArrayList;
import java.util.HashMap;

import nithra.tamil.word.game.giftsuggestions.Full_Details;
import nithra.tamil.word.game.giftsuggestions.R;
import nithra.tamil.word.game.giftsuggestions.Retrofit.Fav_view;
import nithra.tamil.word.game.giftsuggestions.Retrofit.RetrofitAPI;
import nithra.tamil.word.game.giftsuggestions.Retrofit.RetrofitApiClient;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Favourite extends Fragment {
    Adapter adapter;
    ArrayList<Fav_view> fav_show;
    SQLiteDatabase mydb;


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
        mydb = getContext().openOrCreateDatabase("mydb", MODE_PRIVATE, null);
        adapter = new Adapter(getContext(), fav_show);
        Cursor c = mydb.rawQuery("select * from Bookmark", null);
        if (c.getCount() != 0) {
            c.getString(c.getColumnIndexOrThrow("id"));
        }
        return view;
    }

    public void fav() {
        HashMap<String, String> map = new HashMap<>();
        map.put("action", "fav_image");
        map.put("user_id", "");
        RetrofitAPI retrofitAPI = RetrofitApiClient.getRetrofit().create(RetrofitAPI.class);
        Call<ArrayList<Fav_view>> call = retrofitAPI.fav_view(map);
        call.enqueue(new Callback<ArrayList<Fav_view>>() {
            @Override
            public void onResponse(Call<ArrayList<Fav_view>> call, Response<ArrayList<Fav_view>> response) {
                if (response.isSuccessful()) {

                }
                System.out.println("======response :" + response);
            }

            @Override
            public void onFailure(Call<ArrayList<Fav_view>> call, Throwable t) {
                System.out.println("======response t:" + t);
            }
        });
    }


    public class Adapter extends RecyclerView.Adapter<Adapter.ViewHolder> {
        ArrayList<Fav_view> gift_show;
        LayoutInflater inflater;
        Context context;

        public Adapter(Context ctx, ArrayList<Fav_view> gift_show) {
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
            holder.head.setText(gift_show.get(pos).getDiscount() + "% offer");
            holder.category.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent i = new Intent(getContext(), Full_Details.class);
                    i.putExtra("full_view", gift_show.get(pos).getId());
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
                category = itemView.findViewById(R.id.category);
            }
        }
    }


}