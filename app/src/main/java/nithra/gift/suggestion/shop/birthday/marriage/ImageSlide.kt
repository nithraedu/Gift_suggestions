package nithra.gift.suggestion.shop.birthday.marriage

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.*
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.widget.ViewPager2
import androidx.viewpager2.widget.ViewPager2.OnPageChangeCallback
import com.bumptech.glide.Glide

class ImageSlide : AppCompatActivity() {
    private var viewpager2: ViewPager2? = null
    var adapter: Adapter? = null
    private var intent1: Intent? = null
    var extra: Bundle? = null
    private var posGift: String? = null
    var name: String? = null
    private var swipe: LinearLayout? = null
    var backArrow: CardView? = null
    var forwardArrow: CardView? = null
    var back: LinearLayout? = null
    var giftName: TextView? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_image_slide)
        viewpager2 = findViewById(R.id.viewpager2)
        swipe = findViewById(R.id.swipe)
        backArrow = findViewById(R.id.back_arrow)
        forwardArrow = findViewById(R.id.forward_arrow)
        back = findViewById(R.id.back)
        giftName = findViewById(R.id.gift_name)
        intent1 = intent
        extra = intent1!!.extras
        posGift = extra!!.getString("pos")
        name = extra!!.getString("name")
        val separated =
            posGift!!.split(",".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
        giftName!!.text = name
        back!!.setOnClickListener { finish() }
        forwardArrow!!.setOnClickListener {
            viewpager2!!.setCurrentItem(
                viewpager2!!.currentItem + 1, true
            )
        }
        backArrow!!.setOnClickListener {
            viewpager2!!.setCurrentItem(
                viewpager2!!.currentItem - 1, true
            )
        }
        viewpager2!!.registerOnPageChangeCallback(object : OnPageChangeCallback() {

            override fun onPageSelected(position: Int) {
                super.onPageSelected(position)
                if (position == 0) {
                    if (separated.size == 1) {
                        forwardArrow!!.visibility = View.INVISIBLE
                        backArrow!!.visibility = View.INVISIBLE
                    } else {
                        backArrow!!.visibility = View.INVISIBLE
                        forwardArrow!!.visibility = View.VISIBLE
                    }
                } else if (separated.size - 1 == position) {
                    forwardArrow!!.visibility = View.INVISIBLE
                    backArrow!!.visibility = View.VISIBLE
                } else {
                    backArrow!!.visibility = View.VISIBLE
                    forwardArrow!!.visibility = View.VISIBLE
                }
            }

        })
        adapter = Adapter(this, separated)
        viewpager2!!.adapter = adapter
    }

    inner class Adapter(
        private val context: Context, var image: Array<String>
    ) : RecyclerView.Adapter<Adapter.ViewHolder>() {
        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
            val view = LayoutInflater.from(context).inflate(R.layout.image_view, parent, false)
            return ViewHolder(view)
        }

        override fun onBindViewHolder(holder: ViewHolder, position: Int) {
            Glide.with(applicationContext).load(image[position])
                .error(R.drawable.ic_gift_default_img)
                .placeholder(R.drawable.ic_gift_default_img) //.diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(holder.imgView)
        }

        override fun getItemCount(): Int {
            return image.size
        }

        inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
            var imgView: ImageView

            init {
                imgView = view.findViewById(R.id.img_view)
            }
        }
    }
}