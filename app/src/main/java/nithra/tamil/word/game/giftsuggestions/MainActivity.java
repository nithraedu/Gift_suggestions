package nithra.tamil.word.game.giftsuggestions;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;
import androidx.viewpager2.widget.ViewPager2;

import com.google.android.material.bottomappbar.BottomAppBar;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.shape.CornerFamily;
import com.google.android.material.shape.MaterialShapeDrawable;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.HashMap;

import nithra.tamil.word.game.giftsuggestions.Fragment.Favourite;
import nithra.tamil.word.game.giftsuggestions.Fragment.Home;
import nithra.tamil.word.game.giftsuggestions.Fragment.Location;
import nithra.tamil.word.game.giftsuggestions.Fragment.Settings;
import nithra.tamil.word.game.giftsuggestions.Otp.OtpSend;
import nithra.tamil.word.game.giftsuggestions.Retrofit.Androidid;
import nithra.tamil.word.game.giftsuggestions.Retrofit.RetrofitAPI;
import nithra.tamil.word.game.giftsuggestions.Retrofit.RetrofitApiClient;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity implements BottomNavigationView.OnItemSelectedListener, FragMove {
    ViewPager2 viewpager2;
    Frag_Adapter frag_adapter;
    ImageView home, favourite, location, settings;
    FloatingActionButton add_shop;
    BottomAppBar bottomAppBar;
    SharedPreferences pref;
    SharedPreference sharedPreference = new SharedPreference();
    ArrayList<Androidid> and_id;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_main);
        pref = getSharedPreferences("register", Context.MODE_PRIVATE);
        System.out.println("android_id" + Utils_Class.android_id(this));

        viewpager2 = findViewById(R.id.viewpager2);
        home = findViewById(R.id.home);
        favourite = findViewById(R.id.favourite);
        location = findViewById(R.id.location);
        settings = findViewById(R.id.settings);
        add_shop = findViewById(R.id.add_shop);
        viewpager2.setUserInputEnabled(false);
        frag_adapter = new Frag_Adapter(this);
        frag_adapter.addFragment(new Home());
        frag_adapter.addFragment(new Favourite());
        //frag_adapter.addFragment(new Add());
        frag_adapter.addFragment(new Location());
        frag_adapter.addFragment(new Settings());
        /*frag_adapter.addFragment(new Product());
        frag_adapter.addFragment(new SendOTP());
        frag_adapter.addFragment(new EnterOTP());*/
        bottomAppBar = findViewById(R.id.bottomAppBar);
        and_id = new ArrayList<Androidid>();

        if (sharedPreference.getInt(getApplicationContext(), "android_id_check") == 0) {
            android();
        }


        MaterialShapeDrawable bottomBarBackground = (MaterialShapeDrawable) bottomAppBar.getBackground();
        bottomBarBackground.setShapeAppearanceModel(
                bottomBarBackground.getShapeAppearanceModel()
                        .toBuilder()
                        .setTopRightCorner(CornerFamily.ROUNDED, 15)
                        .setTopLeftCorner(CornerFamily.ROUNDED, 15)
                        .setBottomLeftCorner(CornerFamily.ROUNDED, 15)
                        .setBottomRightCorner(CornerFamily.ROUNDED, 15)
                        .build());

        home.setBackgroundResource(R.drawable.background2);

        home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                home.setBackgroundResource(R.drawable.background2);
                favourite.setBackgroundResource(0);
                location.setBackgroundResource(0);
                settings.setBackgroundResource(0);

                viewpager2.setCurrentItem(0, false);
            }
        });

        favourite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                home.setBackgroundResource(0);
                favourite.setBackgroundResource(R.drawable.background2);
                location.setBackgroundResource(0);
                settings.setBackgroundResource(0);
                viewpager2.setCurrentItem(1, false);
            }
        });

        add_shop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                if (sharedPreference.getInt(getApplicationContext(), "yes") == 0) {
                    //viewpager2.setCurrentItem(6, false);
                    Intent i = new Intent(MainActivity.this, OtpSend.class);
                    startActivity(i);

                } else {
                    Intent i = new Intent(MainActivity.this, MyProduct.class);
                    startActivity(i);
                    // viewpager2.setCurrentItem(5, false);
                    //viewpager2.setCurrentItem(2, false);
                }

                home.setBackgroundResource(0);
                favourite.setBackgroundResource(0);
                location.setBackgroundResource(0);
                settings.setBackgroundResource(0);
              /*bottomAppBar.setVisibility(View.GONE);
                add_shop.setVisibility(View.GONE);*/
            }
        });

        location.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                home.setBackgroundResource(0);
                favourite.setBackgroundResource(0);
                location.setBackgroundResource(R.drawable.background2);
                settings.setBackgroundResource(0);
                viewpager2.setCurrentItem(3, false);
            }
        });

        settings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                home.setBackgroundResource(0);
                favourite.setBackgroundResource(0);
                location.setBackgroundResource(0);
                settings.setBackgroundResource(R.drawable.background2);
                viewpager2.setCurrentItem(4, false);
            }
        });

        viewpager2.setAdapter(frag_adapter);
    }


    public void android() {
        HashMap<String, String> map = new HashMap<>();
        map.put("action", "check_android_id");
        map.put("android_id", Utils_Class.android_id(this));
        RetrofitAPI retrofitAPI = RetrofitApiClient.getRetrofit().create(RetrofitAPI.class);
        Call<ArrayList<Androidid>> call = retrofitAPI.androidid(map);
        call.enqueue(new Callback<ArrayList<Androidid>>() {
            @Override
            public void onResponse(Call<ArrayList<Androidid>> call, Response<ArrayList<Androidid>> response) {
                if (response.isSuccessful()) {
                    String result = new Gson().toJson(response.body());
                    System.out.println("======response result:" + result);
                    if (response.body().get(0).getStatus().equals("success")) {
                        and_id.addAll(response.body());
                        sharedPreference.putInt(getApplicationContext(), "android_id_check", 1);
                        sharedPreference.putString(getApplicationContext(), "android_userid", and_id.get(0).getUserId());
                    }
                }
                System.out.println("======response :" + response);
            }

            @Override
            public void onFailure(Call<ArrayList<Androidid>> call, Throwable t) {
                System.out.println("======response t:" + t);
            }
        });
    }


    @Override
    public void onBackPressed() {


        if (viewpager2.getCurrentItem() == 1) {
            viewpager2.setCurrentItem(0, false);
            home.setBackgroundResource(R.drawable.background2);
            favourite.setBackgroundResource(0);
            location.setBackgroundResource(0);
            settings.setBackgroundResource(0);
        } /*else if (viewpager2.getCurrentItem() == 2) {
            viewpager2.setCurrentItem(0, false);
            home.setBackgroundResource(R.drawable.background2);
            favourite.setBackgroundResource(0);
            location.setBackgroundResource(0);
            settings.setBackgroundResource(0);
        }*/ else if (viewpager2.getCurrentItem() == 3) {
            viewpager2.setCurrentItem(0, false);
            home.setBackgroundResource(R.drawable.background2);
            favourite.setBackgroundResource(0);
            location.setBackgroundResource(0);
            settings.setBackgroundResource(0);
        } else if (viewpager2.getCurrentItem() == 4) {
            viewpager2.setCurrentItem(0, false);
            home.setBackgroundResource(R.drawable.background2);
            favourite.setBackgroundResource(0);
            location.setBackgroundResource(0);
            settings.setBackgroundResource(0);
        } /*else if (viewpager2.getCurrentItem() == 5) {
            viewpager2.setCurrentItem(0, false);
            home.setBackgroundResource(R.drawable.background2);
            favourite.setBackgroundResource(0);
            location.setBackgroundResource(0);
            settings.setBackgroundResource(0);
        } else if (viewpager2.getCurrentItem() == 6) {
            viewpager2.setCurrentItem(0, false);
            home.setBackgroundResource(R.drawable.background2);
            favourite.setBackgroundResource(0);
            location.setBackgroundResource(0);
            settings.setBackgroundResource(0);
        } else if (viewpager2.getCurrentItem() == 7) {
            viewpager2.setCurrentItem(6, false);
            bottomAppBar.setVisibility(View.GONE);
            add_shop.setVisibility(View.GONE);
            favourite.setBackgroundResource(0);
            location.setBackgroundResource(0);
            settings.setBackgroundResource(0);
        }*/ else {
            finish();
        }
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        return false;
    }

    @Override
    public void product() {
        viewpager2.setCurrentItem(5, false);

    }

    @Override
    public void seller() {
        viewpager2.setCurrentItem(2, false);

    }

    @Override
    public void enterotp() {
        viewpager2.setCurrentItem(7, false);

    }

    @Override
    public void home() {
        viewpager2.setCurrentItem(0, false);
        home.setBackgroundResource(R.drawable.background2);
        favourite.setBackgroundResource(0);
        location.setBackgroundResource(0);
        settings.setBackgroundResource(0);
    }

    public class Frag_Adapter extends FragmentStateAdapter {

        private ArrayList<Fragment> fragmentList = new ArrayList<>();

        public Frag_Adapter(@NonNull FragmentActivity fragmentActivity) {
            super(fragmentActivity);
        }


        @NonNull
        @Override
        public Fragment createFragment(int position) {
            return fragmentList.get(position);
        }

        public void addFragment(Fragment fragment) {
            fragmentList.add(fragment);
        }

        @Override
        public int getItemCount() {
            return fragmentList.size();
        }
    }


}