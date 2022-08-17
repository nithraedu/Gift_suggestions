package nithra.tamil.word.game.giftsuggestions.Fragment;

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

import com.google.android.material.navigation.NavigationView;

import java.util.ArrayList;

import nithra.tamil.word.game.giftsuggestions.ActivitySecond;
import nithra.tamil.word.game.giftsuggestions.MyProduct;
import nithra.tamil.word.game.giftsuggestions.Otp.OtpSend;
import nithra.tamil.word.game.giftsuggestions.Otp.OtpVerify;
import nithra.tamil.word.game.giftsuggestions.Otp.ShopAdd;
import nithra.tamil.word.game.giftsuggestions.R;
import nithra.tamil.word.game.giftsuggestions.SellerProfile;
import nithra.tamil.word.game.giftsuggestions.SharedPreference;

public class Home extends Fragment implements NavigationView.OnNavigationItemSelectedListener {
    DrawerLayout drawer;
    ArrayList<Integer> images2;
    ArrayList<Integer> images3;
    ArrayList<String> text2;
    ArrayList<String> text3;
    Adapter2 adapter2;
    Adapter3 adapter3;
    LinearLayout notification,profile_view;
    SharedPreference sharedPreference = new SharedPreference();

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
        images2 = new ArrayList<Integer>();
        images3 = new ArrayList<Integer>();
        text2 = new ArrayList<String>();
        text3 = new ArrayList<String>();
        RecyclerView list = view.findViewById(R.id.list);
        RecyclerView list2 = view.findViewById(R.id.list2);
        images2.add(R.drawable.ic_icon_1);
        images2.add(R.drawable.ic_icon_2);
        images2.add(R.drawable.ic_kids);
        images2.add(R.drawable.ic_icon_4);
        images2.add(R.drawable.ic_icon_5);
        text2.add("Men");
        text2.add("Women");
        text2.add("Kids");
        text2.add("Grandpa");
        text2.add("Grandma");

        images3.add(R.drawable.birthday);
        images3.add(R.drawable.childrensday);
        images3.add(R.drawable.fathersday);
        images3.add(R.drawable.friendship);
        images3.add(R.drawable.mothersday);
        images3.add(R.drawable.valentinesday);
        images3.add(R.drawable.wedding);
        images3.add(R.drawable.womensday);

        text3.add("Birthday");
        text3.add("Children's Day");
        text3.add("Father's Day");
        text3.add("Friendship");
        text3.add("Mother's Day");
        text3.add("Valentine's Day");
        text3.add("Wedding");
        text3.add("Women's Day");
        String user=sharedPreference.getString(getContext(), "user_status");

        profile_view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

               /* if (user.equals("exiting")) {

                    Intent i=new Intent(getContext(), SellerProfile.class);
                    startActivity(i);
                }else if(user.equals("new")){
                    Intent i=new Intent(getContext(), ShopAdd.class);
                    startActivity(i);

                }*/


                if (sharedPreference.getInt(getContext(), "profile") == 1) {
                    Intent i = new Intent(getContext(), ShopAdd.class);
                    startActivity(i);
                }else if (sharedPreference.getInt(getContext(), "profile") == 2){
                    Intent i = new Intent(getContext(), SellerProfile.class);
                    startActivity(i);
                }else {
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
       /* View v = navigationView.inflateHeaderView(R.layout.header);
        code = v.findViewById(R.id.code);
        name = v.findViewById(R.id.name);
        code.setText("" + versionCode);
        name.setText(versionName);*/


        GridLayoutManager gridLayoutManager2 = new GridLayoutManager(getContext(), 1, GridLayoutManager.HORIZONTAL, false);
        list.setLayoutManager(gridLayoutManager2);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(getContext(), 3, GridLayoutManager.VERTICAL, false);
        list2.setLayoutManager(gridLayoutManager);

        adapter2 = new Adapter2(getContext(), images2, text2);
        list.setAdapter(adapter2);

        adapter3 = new Adapter3(getContext(), images3, text3);
        list2.setAdapter(adapter3);

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

    public class Adapter2 extends RecyclerView.Adapter<Adapter2.ViewHolder> {
        ArrayList<Integer> images;
        ArrayList<String> titles;
        LayoutInflater inflater;

        public Adapter2(Context ctx, ArrayList<Integer> images, ArrayList<String> titles) {
            this.images = images;
            this.titles = titles;
            this.inflater = LayoutInflater.from(ctx);
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
            holder.slide_mat.setImageResource(images.get(position));
            holder.gridText.setText(titles.get(position));
        }

        @Override
        public int getItemCount() {
            return images.size();
        }

        public class ViewHolder extends RecyclerView.ViewHolder {
            ImageView slide_mat;
            TextView gridText;

            public ViewHolder(@NonNull View itemView) {
                super(itemView);
                slide_mat = itemView.findViewById(R.id.imageGrid);
                gridText = itemView.findViewById(R.id.gridText);
            }
        }
    }


    public class Adapter3 extends RecyclerView.Adapter<Adapter3.ViewHolder> {
        ArrayList<Integer> images;
        LayoutInflater inflater;
        ArrayList<String> titles;


        public Adapter3(Context ctx, ArrayList<Integer> images, ArrayList<String> titles) {
            this.images = images;
            this.titles = titles;
            this.inflater = LayoutInflater.from(ctx);
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
            holder.img_slide.setImageResource(images.get(pos));
            holder.gridText.setText(titles.get(pos));
            holder.category.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent i = new Intent(getContext(), ActivitySecond.class);
                    i.putExtra("title", titles.get(pos));
                    startActivity(i);
                }
            });

        }


        @Override
        public int getItemCount() {
            return images.size();
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