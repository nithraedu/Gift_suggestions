package nithra.gift.suggestion.shop.birthday.marriage

import android.content.Intent
import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import androidx.viewpager.widget.ViewPager
import com.google.android.material.tabs.TabLayout
import nithra.gift.suggestion.shop.birthday.marriage.fragment.NithraProducts
import nithra.gift.suggestion.shop.birthday.marriage.fragment.Sellerproducts

class ActivitySecond : AppCompatActivity() {
    var adapter: ViewPagerAdapter? = null
    private var viewPager: ViewPager? = null
    private var tabLayout: TabLayout? = null
    private var catTitle: TextView? = null
    private var intent1: Intent? = null
    var title: String? = null
    private var title1: String? = null
    private var title3: String? = null
    var extra: Bundle? = null
    var back: ImageView? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_second)
        catTitle = findViewById(R.id.cat_title)
        viewPager = findViewById(R.id.viewpager)
        adapter = ViewPagerAdapter(supportFragmentManager)
        adapter!!.add(NithraProducts(), "Our Suggestions")
        adapter!!.add(Sellerproducts(), "Seller Suggestions")
        viewPager!!.adapter = adapter
        tabLayout = findViewById(R.id.tab_layout)
        tabLayout!!.setupWithViewPager(viewPager)
        intent1 = intent
        extra = intent1!!.extras
        back = findViewById(R.id.back)
        if (extra != null) {
            title = extra!!.getString("title")
            title1 = extra!!.getString("cat_idd")
            title3 = extra!!.getString("gender_id")
        }
        back!!.setOnClickListener { finish() }
        catTitle!!.text = "$title Gifts"
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

        override fun getPageTitle(position: Int): CharSequence {
            return fragmentTitle[position]
        }
    }
}