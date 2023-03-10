package nithra.gift.suggestion.shop.birthday.marriage.Notification
import android.content.Context
import android.content.pm.PackageManager
import android.content.pm.ResolveInfo
import android.widget.BaseAdapter
import android.view.ViewGroup
import android.view.LayoutInflater
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import nithra.gift.suggestion.shop.birthday.marriage.R

class MyAdapter(var context: Context, var pm: PackageManager, var listApp: List<ResolveInfo>) :
    BaseAdapter() {
    override fun getCount(): Int {
        return listApp.size
    }

    override fun getItem(position: Int): Any {
        return listApp[position]
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun getView(position: Int, v: View?, parent: ViewGroup): View {
        var v = v
        var holder: ViewHolder? = null
        if (v == null) {
            holder = ViewHolder()
            v = LayoutInflater.from(context).inflate(R.layout.layout_share_app, parent, false)
            holder.ivLogo = v.findViewById(R.id.iv_logo)
            holder.tvAppName = v.findViewById(R.id.tv_app_name)
            holder.tvPackageName = v.findViewById(R.id.tv_app_package_name)
            v.tag = holder
        } else {
            holder = v.tag as ViewHolder
        }
        val appInfo = listApp[position]
        holder.ivLogo!!.setImageDrawable(appInfo.loadIcon(pm))
        holder!!.tvAppName!!.text = appInfo.loadLabel(pm)
        holder.tvPackageName!!.text = appInfo.activityInfo.packageName
        return v!!
    }

    internal class ViewHolder {
        var ivLogo: ImageView? = null
        var tvAppName: TextView? = null
        var tvPackageName: TextView? = null
    }
}