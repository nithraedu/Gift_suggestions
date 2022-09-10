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
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.widget.ViewPager2;

import com.bumptech.glide.Glide;

public class ImageSlide extends AppCompatActivity {
    ViewPager2 viewpager2;
    Adapter adapter;
    Intent intent;
    Bundle extra;
    String pos_gift,name;
    LinearLayout swipe;
    CardView back_arrow, forward_arrow;
    LinearLayout back;
    TextView gift_name;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_image_slide);
        viewpager2 = findViewById(R.id.viewpager2);
        swipe = findViewById(R.id.swipe);
        back_arrow = findViewById(R.id.back_arrow);
        forward_arrow = findViewById(R.id.forward_arrow);
        back = findViewById(R.id.back);
        gift_name = findViewById(R.id.gift_name);
        intent = getIntent();
        extra = intent.getExtras();
        pos_gift = extra.getString("pos");
        name = extra.getString("name");
        String[] separated = pos_gift.split(",");


        gift_name.setText(name);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        forward_arrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                viewpager2.setCurrentItem((viewpager2.getCurrentItem() + 1), true);
            }
        });

        back_arrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                viewpager2.setCurrentItem((viewpager2.getCurrentItem() - 1), true);
            }
        });
        viewpager2.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                super.onPageScrolled(position, positionOffset, positionOffsetPixels);
            }

            @Override
            public void onPageSelected(int position) {
                super.onPageSelected(position);
               /* if (separated.length - 1 == position) {
                    swipe.setVisibility(View.INVISIBLE);
                } else {
                    swipe.setVisibility(View.VISIBLE);
                }*/

               /* if (position == 0) {
                    back_arrow.setVisibility(View.INVISIBLE);
                } else if (separated.length - 1== position) {
                    forward_arrow.setVisibility(View.INVISIBLE);
                } else {
                    back_arrow.setVisibility(View.VISIBLE);
                    forward_arrow.setVisibility(View.VISIBLE);
                }
*/

                if (position == 0) {
                    if (separated.length == 1) {
                        forward_arrow.setVisibility(View.INVISIBLE);
                        back_arrow.setVisibility(View.INVISIBLE);
                    } else {
                        back_arrow.setVisibility(View.INVISIBLE);
                        forward_arrow.setVisibility(View.VISIBLE);
                    }
                } else if ((separated.length - 1) == position) {
                    forward_arrow.setVisibility(View.INVISIBLE);
                    back_arrow.setVisibility(View.VISIBLE);
                } else {
                    back_arrow.setVisibility(View.VISIBLE);
                    forward_arrow.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {
                super.onPageScrollStateChanged(state);
            }
        });

        System.out.println("print_url== " + separated[0]);
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
                    .error(R.drawable.ic_gift_default_img)
                    .placeholder(R.drawable.ic_gift_default_img)
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