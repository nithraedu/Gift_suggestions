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
    var viewpager2: ViewPager2? = null
    var adapter: Adapter? = null
    var intent1: Intent? = null
    var extra: Bundle? = null
    var pos_gift: String? = null
    var name: String? = null
    var swipe: LinearLayout? = null
    var back_arrow: CardView? = null
    var forward_arrow: CardView? = null
    var back: LinearLayout? = null
    var gift_name: TextView? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requestWindowFeature(Window.FEATURE_NO_TITLE)
        this.window.setFlags(
            WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN
        )
        setContentView(R.layout.activity_image_slide)
        viewpager2 = findViewById(R.id.viewpager2)
        swipe = findViewById(R.id.swipe)
        back_arrow = findViewById(R.id.back_arrow)
        forward_arrow = findViewById(R.id.forward_arrow)
        back = findViewById(R.id.back)
        gift_name = findViewById(R.id.gift_name)
        intent1 = getIntent()
        extra = intent1!!.getExtras()
        pos_gift = extra!!.getString("pos")
        name = extra!!.getString("name")
        val separated =
            pos_gift!!.split(",".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
        gift_name!!.setText(name)
        back!!.setOnClickListener({ finish() })
        forward_arrow!!.setOnClickListener({
            viewpager2!!.setCurrentItem(
                viewpager2!!.getCurrentItem() + 1, true
            )
        })
        back_arrow!!.setOnClickListener({
            viewpager2!!.setCurrentItem(
                viewpager2!!.getCurrentItem() - 1, true
            )
        })
        viewpager2!!.registerOnPageChangeCallback(object : OnPageChangeCallback() {
            override fun onPageScrolled(
                position: Int, positionOffset: Float, positionOffsetPixels: Int
            ) {
                super.onPageScrolled(position, positionOffset, positionOffsetPixels)
            }

            override fun onPageSelected(position: Int) {
                super.onPageSelected(position)
                if (position == 0) {
                    if (separated.size == 1) {
                        forward_arrow!!.setVisibility(View.INVISIBLE)
                        back_arrow!!.setVisibility(View.INVISIBLE)
                    } else {
                        back_arrow!!.setVisibility(View.INVISIBLE)
                        forward_arrow!!.setVisibility(View.VISIBLE)
                    }
                } else if (separated.size - 1 == position) {
                    forward_arrow!!.setVisibility(View.INVISIBLE)
                    back_arrow!!.setVisibility(View.VISIBLE)
                } else {
                    back_arrow!!.setVisibility(View.VISIBLE)
                    forward_arrow!!.setVisibility(View.VISIBLE)
                }
            }

            override fun onPageScrollStateChanged(state: Int) {
                super.onPageScrollStateChanged(state)
            }
        })
        adapter = Adapter(this, viewpager2!!, separated)
        viewpager2!!.setAdapter(adapter)
    }

    inner class Adapter(
        private val context: Context, var viewpager: ViewPager2, var image: Array<String>
    ) : RecyclerView.Adapter<Adapter.ViewHolder>() {
        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
            val view = LayoutInflater.from(context).inflate(R.layout.image_view, parent, false)
            return ViewHolder(view)
        }

        override fun onBindViewHolder(holder: ViewHolder, position: Int) {
            Glide.with(applicationContext).load(image[position])
                .error(R.drawable.ic_gift_default_img)
                .placeholder(R.drawable.ic_gift_default_img) //.diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(holder.img_view)
        }

        override fun getItemCount(): Int {
            return image.size
        }

        inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
            var img_view: ImageView

            init {
                img_view = view.findViewById(R.id.img_view)
            }
        }
    }
}