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
import nithra.tamil.word.game.giftsuggestions.Fragment.EnterOTP;
import nithra.tamil.word.game.giftsuggestions.Fragment.Favourite;
import nithra.tamil.word.game.giftsuggestions.Fragment.Home;
import nithra.tamil.word.game.giftsuggestions.Fragment.Location;
import nithra.tamil.word.game.giftsuggestions.Fragment.Product;
import nithra.tamil.word.game.giftsuggestions.Fragment.SendOTP;
import nithra.tamil.word.game.giftsuggestions.Fragment.Settings;

public class MainActivity extends AppCompatActivity implements BottomNavigationView.OnItemSelectedListener, FragMove {
    ViewPager2 viewpager2;
    Frag_Adapter frag_adapter;
    ImageView home, favourite, location, settings;
    FloatingActionButton add_shop;
    BottomAppBar bottomAppBar;

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
        frag_adapter.addFragment(new SendOTP());
        frag_adapter.addFragment(new EnterOTP());
        bottomAppBar = findViewById(R.id.bottomAppBar);

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
                viewpager2.setCurrentItem(6, false);
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


    @Override
    public void onBackPressed() {
        bottomAppBar.setVisibility(View.VISIBLE);
        add_shop.setVisibility(View.VISIBLE);

        if (viewpager2.getCurrentItem() == 1) {
            viewpager2.setCurrentItem(0, false);
            home.setBackgroundResource(R.drawable.background2);
            favourite.setBackgroundResource(0);
            location.setBackgroundResource(0);
            settings.setBackgroundResource(0);
        } else if (viewpager2.getCurrentItem() == 2) {
            viewpager2.setCurrentItem(0, false);
            home.setBackgroundResource(R.drawable.background2);
            favourite.setBackgroundResource(0);
            location.setBackgroundResource(0);
            settings.setBackgroundResource(0);
        } else if (viewpager2.getCurrentItem() == 3) {
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
        } else if (viewpager2.getCurrentItem() == 5) {
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
        } else {
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