package nithra.gift.suggestion.shop.birthday.marriage.Notification;

import android.content.Context;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import nithra.gift.suggestion.shop.birthday.marriage.R;


public class MyAdapter extends BaseAdapter {
    PackageManager pm;
    java.util.List<ResolveInfo> listApp;
    Context context;

    public MyAdapter(Context context1, PackageManager pManager, java.util.List<ResolveInfo> listApp1) {
        pm = pManager;
        listApp = listApp1;
        context = context1;
    }

    @Override
    public int getCount() {
        return listApp.size();
    }

    @Override
    public Object getItem(int position) {
        return listApp.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View v, ViewGroup parent) {
        ViewHolder holder = null;
        if (v == null) {
            holder = new ViewHolder();
            v = LayoutInflater.from(context).inflate(R.layout.layout_share_app, parent, false);
            holder.ivLogo = v.findViewById(R.id.iv_logo);
            holder.tvAppName = v.findViewById(R.id.tv_app_name);
            holder.tvPackageName = v.findViewById(R.id.tv_app_package_name);
            v.setTag(holder);
        } else {
            holder = (ViewHolder) v.getTag();
        }
        ResolveInfo appInfo = listApp.get(position);
        holder.ivLogo.setImageDrawable(appInfo.loadIcon(pm));
        holder.tvAppName.setText(appInfo.loadLabel(pm));
        holder.tvPackageName.setText(appInfo.activityInfo.packageName);
        return v;
    }

    static class ViewHolder {
        ImageView ivLogo;
        TextView tvAppName, tvPackageName;
    }
}

