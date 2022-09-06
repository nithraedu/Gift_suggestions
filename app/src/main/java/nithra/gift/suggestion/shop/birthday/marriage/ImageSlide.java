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

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.widget.ViewPager2;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

public class ImageSlide extends AppCompatActivity {
    ViewPager2 viewpager2;
    Adapter adapter;
    Intent intent;
    Bundle extra;
    String pos_gift;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_image_slide);
        viewpager2 = findViewById(R.id.viewpager2);

        intent = getIntent();
        extra = intent.getExtras();
        pos_gift = extra.getString("pos");
        String[] separated = pos_gift.split(",");

        System.out.println("print_url== "+separated[0]);
        adapter = new Adapter(this, viewpager2, separated);
        viewpager2.setAdapter(adapter);
    }

    public class Adapter extends RecyclerView.Adapter<Adapter.ViewHolder> {
        ViewPager2 viewpager;
        private Context context;
        String[] image;

        public Adapter(Context context, ViewPager2 viewpager, String[] img) {
            this.context = context;
            this.viewpager = viewpager;
            this.image = img;
        }

        @NonNull
        @Override
        public Adapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(context).inflate(R.layout.image_view, parent, false);
            return new ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull Adapter.ViewHolder holder, int position) {

            Glide.with(getApplicationContext()).load(image[position])
                    //.error(R.drawable.gift_1)
                    //.diskCacheStrategy(DiskCacheStrategy.ALL)
                    .into(holder.img_view);
        }


        @Override
        public int getItemCount() {
            return image.length;
        }

        public class ViewHolder extends RecyclerView.ViewHolder {
           ImageView img_view;

            public ViewHolder(@NonNull View view) {
                super(view);
                img_view = view.findViewById(R.id.img_view);
            }
        }
    }


}