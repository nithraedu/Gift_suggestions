package nithra.gift.suggestion.shop.birthday.marriage;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.tabs.TabLayout;

import java.util.ArrayList;
import java.util.List;

import nithra.gift.suggestion.shop.birthday.marriage.Fragment.NithraProducts;
import nithra.gift.suggestion.shop.birthday.marriage.Fragment.Sellerproducts;

public class ActivitySecond extends AppCompatActivity {
    ViewPagerAdapter adapter;
    ViewPager viewPager;
    TabLayout tabLayout;
    TextView cat_title;
    Intent intent;
    String title, title1, title3;
    Bundle extra;
    ImageView back;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_second);
        cat_title = findViewById(R.id.cat_title);
        viewPager = findViewById(R.id.viewpager);
        adapter = new ViewPagerAdapter(getSupportFragmentManager());
        adapter.add(new Sellerproducts(), "Seller Gifts");
        adapter.add(new NithraProducts(), "Nithra Suggestions");
        viewPager.setAdapter(adapter);
        tabLayout = findViewById(R.id.tab_layout);
        tabLayout.setupWithViewPager(viewPager);
        intent = getIntent();
        extra = intent.getExtras();
        back = findViewById(R.id.back);
        if (extra != null) {
            title = extra.getString("title");
            title1 = extra.getString("cat_idd");
            title3 = extra.getString("gender_id");
            System.out.println("gender= " + title3);
        }
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        cat_title.setText("" + title + " Gifts");


    }


    public class ViewPagerAdapter extends FragmentPagerAdapter {
        private final List<Fragment> fragments = new ArrayList<>();
        private final List<String> fragmentTitle = new ArrayList<>();

        public ViewPagerAdapter(FragmentManager fm) {
            super(fm);

        }

        public void add(Fragment fragment, String title) {
            fragments.add(fragment);
            fragmentTitle.add(title);
        }

        @NonNull
        @Override
        public Fragment getItem(int position) {
            return fragments.get(position);
        }

        @Override
        public int getCount() {
            return fragments.size();
        }

        @Nullable
        @Override
        public CharSequence getPageTitle(int position) {
            return fragmentTitle.get(position);
        }
    }
}