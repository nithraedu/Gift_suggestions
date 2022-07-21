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

import java.util.ArrayList;

public class ActivitySecond extends AppCompatActivity {
    Adapter adapter;
    ArrayList<Integer> images;
    ArrayList<String> text;
    RecyclerView list;
    ImageView back;
    Intent intent;
    Bundle extra;
    String title;
    TextView cat_title;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_second);
        images = new ArrayList<Integer>();
        text = new ArrayList<String>();
        list = findViewById(R.id.list);
        cat_title=findViewById(R.id.cat_title);
        back=findViewById(R.id.back);
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
        }
        cat_title.setText(""+title+" Day Gifts");
        images.add(R.drawable.birthday);
        images.add(R.drawable.childrensday);
        images.add(R.drawable.fathersday);
        images.add(R.drawable.friendship);
        images.add(R.drawable.mothersday);
        images.add(R.drawable.valentinesday);
        images.add(R.drawable.wedding);
        images.add(R.drawable.womensday);

        text.add("Birthday");
        text.add("Children's Day");
        text.add("Father's Day");
        text.add("Friendship");
        text.add("Mother's Day");
        text.add("Valentine's Day");
        text.add("Wedding");
        text.add("Women's Day");

        GridLayoutManager gridLayoutManager = new GridLayoutManager(this, 2, GridLayoutManager.VERTICAL, false);
        list.setLayoutManager(gridLayoutManager);
        adapter = new Adapter(this, images, text);
        list.setAdapter(adapter);

    }

    public class Adapter extends RecyclerView.Adapter<Adapter.ViewHolder> {
        ArrayList<Integer> images;
        LayoutInflater inflater;
        ArrayList<String> titles;


        public Adapter(Context ctx, ArrayList<Integer> images, ArrayList<String> titles) {
            this.images = images;
            this.titles = titles;
            this.inflater = LayoutInflater.from(ctx);
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
            holder.img_slide.setImageResource(images.get(position));
            holder.gridText.setText(titles.get(position));
            holder.head.setText(title);
        }


        @Override
        public int getItemCount() {
            return images.size();
        }

        public class ViewHolder extends RecyclerView.ViewHolder {
            ImageView img_slide;
            TextView gridText,head;

            public ViewHolder(@NonNull View itemView) {
                super(itemView);
                img_slide = itemView.findViewById(R.id.imageGrid);
                gridText = itemView.findViewById(R.id.gridText);
                head = itemView.findViewById(R.id.head);
            }
        }
    }

}