package nithra.gift.suggestion.shop.birthday.marriage

import android.content.Intent
import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import nithra.gift.suggestion.shop.birthday.marriage.fragment.NithraProducts
import nithra.gift.suggestion.shop.birthday.marriage.fragment.Sellerproducts

class ActivitySecond : AppCompatActivity() {
    var adapter: ViewPagerAdapter? = null
    private var viewPager: ViewPager2? = null
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
        adapter = ViewPagerAdapter(this)
        adapter!!.addFragment(NithraProducts(), "Our Suggestions")
        adapter!!.addFragment(Sellerproducts(), "Seller Suggestions")
        viewPager!!.adapter = adapter
        tabLayout = findViewById(R.id.tab_layout)
        TabLayoutMediator(tabLayout!!, viewPager!!) { tab, position ->
            tab.text = adapter!!.getTabTitle(position)
        }.attach()
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

    inner class ViewPagerAdapter(activity: FragmentActivity?) : FragmentStateAdapter(activity!!) {
        private val fragments: MutableList<Fragment> = ArrayList()
        private val fragmentTitle: MutableList<String> = ArrayList()

        public fun getTabTitle(position : Int): String{
            return fragmentTitle[position]
        }

        fun addFragment(fragment: Fragment, title: String) {
            fragments.add(fragment)
            fragmentTitle.add(title)
        }

        override fun getItemCount(): Int {
            return fragments.size
        }

        override fun createFragment(position: Int): Fragment {
            return fragments[position]
        }

    }
}