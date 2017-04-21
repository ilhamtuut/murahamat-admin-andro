package wad.wan.murahamatdistro.adapter;

import android.app.Activity;
import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

import java.util.List;

import wad.wan.murahamatdistro.R;
import wad.wan.murahamatdistro.data.DataTestimonial;
import wad.wan.murahamatdistro.data.Image;

/**
 * Created by user on 02/04/2017.
 */
public class ViewPagerAdapter extends PagerAdapter {

    private Activity activity;
    private LayoutInflater inflater;
    private List<Image> items;

    public ViewPagerAdapter(Activity activity, List<Image> items) {
        this.activity = activity;
        this.items = items;
    }

    @Override
    public int getCount() {
        return items.size();
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view==object;
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        inflater = (LayoutInflater) activity.getApplicationContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View itemView = inflater.inflate(R.layout.view_pager_item,container,false);

        ImageView image;
        image = (ImageView) itemView.findViewById(R.id.imageView);
        DisplayMetrics displayMetrics = new DisplayMetrics();
        activity.getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        int height = displayMetrics.heightPixels;
        int width = displayMetrics.widthPixels;
        image.setMinimumHeight(height);
        image.setMinimumWidth(width);

        Image data = items.get(position);

        try{
            Picasso.with(activity.getApplicationContext())
                    .load(data.getGambar())
                    .placeholder(R.mipmap.ic_launcher)
                    .error(R.mipmap.ic_launcher)
                    .into(image);
        }catch (Exception ex){

        }
        container.addView(itemView);
        return  itemView;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        ((ViewPager) container).removeView((View)object);
    }
}
