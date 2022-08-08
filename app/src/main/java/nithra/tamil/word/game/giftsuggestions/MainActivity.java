package nithra.tamil.word.game.giftsuggestions;

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

import java.util.ArrayList;

import nithra.tamil.word.game.giftsuggestions.Fragment.Add;
import nithra.tamil.word.game.giftsuggestions.Fragment.Favourite;
import nithra.tamil.word.game.giftsuggestions.Fragment.Home;
import nithra.tamil.word.game.giftsuggestions.Fragment.Location;
import nithra.tamil.word.game.giftsuggestions.Fragment.Product;
import nithra.tamil.word.game.giftsuggestions.Fragment.Settings;

public class MainActivity extends AppCompatActivity implements BottomNavigationView.OnItemSelectedListener,FragMove {
    ViewPager2 viewpager2;
    Frag_Adapter frag_adapter;
    BottomNavigationView bottomnavigationview;
    ImageView home,favourite,location,settings;
    FloatingActionButton add_shop;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_main);
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
        frag_adapter.addFragment(new Add());
        frag_adapter.addFragment(new Location());
        frag_adapter.addFragment(new Settings());
        frag_adapter.addFragment(new Product());
        bottomnavigationview = findViewById(R.id.bottomnavigationview);
        bottomnavigationview.setOnItemSelectedListener(this);
        bottomnavigationview.setSelectedItemId(R.id.bottom_home);
        BottomAppBar bottomAppBar = findViewById(R.id.bottomAppBar);

        MaterialShapeDrawable bottomBarBackground = (MaterialShapeDrawable) bottomAppBar.getBackground();
        bottomBarBackground.setShapeAppearanceModel(
                bottomBarBackground.getShapeAppearanceModel()
                        .toBuilder()
                        .setTopRightCorner(CornerFamily.ROUNDED,15)
                        .setTopLeftCorner(CornerFamily.ROUNDED,15)
                        .setBottomLeftCorner(CornerFamily.ROUNDED,15)
                        .setBottomRightCorner(CornerFamily.ROUNDED,15)
                        .build());
        home.setBackgroundResource(R.drawable.background2);

        home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                home.setBackgroundResource(R.drawable.background2);
                favourite.setBackgroundResource(0);
                location.setBackgroundResource(0);
                settings.setBackgroundResource(0);

                viewpager2.setCurrentItem(0,false);
            }
        });
        favourite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                home.setBackgroundResource(0);
                favourite.setBackgroundResource(R.drawable.background2);
                location.setBackgroundResource(0);
                settings.setBackgroundResource(0);

                viewpager2.setCurrentItem(1,false);
            }
        });
        add_shop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                viewpager2.setCurrentItem(2,false);
            }
        });
        location.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                home.setBackgroundResource(0);
                favourite.setBackgroundResource(0);
                location.setBackgroundResource(R.drawable.background2);
                settings.setBackgroundResource(0);

                viewpager2.setCurrentItem(3,false);
            }
        });
        settings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                home.setBackgroundResource(0);
                favourite.setBackgroundResource(0);
                location.setBackgroundResource(0);
                settings.setBackgroundResource(R.drawable.background2);

                viewpager2.setCurrentItem(4,false);
            }
        });


        bottomnavigationview.setOnItemSelectedListener(item -> {
            switch (item.getItemId()) {
                case R.id.bottom_home:
                    //getSupportFragmentManager().beginTransaction().replace(R.id.container, firstFragment).commit();
                    viewpager2.setCurrentItem(0,false);
                    return true;

                case R.id.bottom_fav:
                    //getSupportFragmentManager().beginTransaction().replace(R.id.container, secondFragment).commit();
                    viewpager2.setCurrentItem(1,false);

                    return true;
                case R.id.bottom_shop:
                    //getSupportFragmentManager().beginTransaction().replace(R.id.container, secondFragment).commit();
                    viewpager2.setCurrentItem(2,false);

                    return true;

                case R.id.bottom_location:
                    //getSupportFragmentManager().beginTransaction().replace(R.id.container, thirdFragment).commit();
                    viewpager2.setCurrentItem(3,false);

                    return true;

                case R.id.bottom_settings:
                    //getSupportFragmentManager().beginTransaction().replace(R.id.container, thirdFragment).commit();
                    viewpager2.setCurrentItem(4,false);
                    return true;
            }
            return false;
        });
        viewpager2.setAdapter(frag_adapter);
    }


    @Override
    public void onBackPressed() {
        if (viewpager2.getCurrentItem() == 1) {
            viewpager2.setCurrentItem(0, false);
        } else if (viewpager2.getCurrentItem() == 2) {
            viewpager2.setCurrentItem(0, false);
        }else if (viewpager2.getCurrentItem() == 3) {
            viewpager2.setCurrentItem(0, false);
        }else if (viewpager2.getCurrentItem() == 4) {
            viewpager2.setCurrentItem(0, false);
        }
        else {
            finish();
        }
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        return false;
    }

    @Override
    public void product() {
        viewpager2.setCurrentItem(5,false);

    }

    @Override
    public void register() {

    }

    @Override
    public void verify() {

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