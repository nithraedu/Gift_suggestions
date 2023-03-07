package nithra.gift.suggestion.shop.birthday.marriage

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.view.Window
import android.view.WindowManager
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import androidx.viewpager.widget.ViewPager
import com.google.android.material.tabs.TabLayout
import nithra.gift.suggestion.shop.birthday.marriage.Fragment.NithraProducts
import nithra.gift.suggestion.shop.birthday.marriage.Fragment.Sellerproducts

class ActivitySecond : AppCompatActivity() {
    var adapter: ViewPagerAdapter? = null
    var viewPager: ViewPager? = null
    var tabLayout: TabLayout? = null
    var cat_title: TextView? = null
    var intent1: Intent? = null
    var title: String? = null
    var title1: String? = null
    var title3: String? = null
    var extra: Bundle? = null
    var back: ImageView? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requestWindowFeature(Window.FEATURE_NO_TITLE)
        this.window.setFlags(
            WindowManager.LayoutParams.FLAG_FULLSCREEN,
            WindowManager.LayoutParams.FLAG_FULLSCREEN
        )
        setContentView(R.layout.activity_second)
        cat_title = findViewById(R.id.cat_title)
        viewPager = findViewById(R.id.viewpager)
        adapter = ViewPagerAdapter(supportFragmentManager)
        adapter!!.add(NithraProducts(), "Our Suggestions")
        adapter!!.add(Sellerproducts(), "Seller Suggestions")
        viewPager!!.setAdapter(adapter)
        tabLayout = findViewById(R.id.tab_layout)
        tabLayout!!.setupWithViewPager(viewPager)
        intent1 = getIntent()
        extra = intent1!!.getExtras()
        back = findViewById(R.id.back)
        if (extra != null) {
            title = extra!!.getString("title")
            title1 = extra!!.getString("cat_idd")
            title3 = extra!!.getString("gender_id")
            println("gender= $title3")
        }
        back!!.setOnClickListener(View.OnClickListener { finish() })
        cat_title!!.setText("$title Gifts")
    }

    inner class ViewPagerAdapter(fm: FragmentManager?) : FragmentPagerAdapter(
        fm!!
    ) {
        private val fragments: MutableList<Fragment> = ArrayList()
        private val fragmentTitle: MutableList<String> = ArrayList()
        fun add(fragment: Fragment, title: String) {
            fragments.add(fragment)
            fragmentTitle.add(title)
        }

        override fun getItem(position: Int): Fragment {
            return fragments[position]
        }

        override fun getCount(): Int {
            return fragments.size
        }

        override fun getPageTitle(position: Int): CharSequence? {
            return fragmentTitle[position]
        }
    }
}