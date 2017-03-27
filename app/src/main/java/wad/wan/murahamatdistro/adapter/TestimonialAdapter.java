package wad.wan.murahamatdistro.adapter;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;

import java.util.List;

import wad.wan.murahamatdistro.R;
import wad.wan.murahamatdistro.app.AppController;
import wad.wan.murahamatdistro.app.RequestHandler;
import wad.wan.murahamatdistro.data.DataTestimonial;

/**
 * Created by user on 19/03/2017.
 */
public class TestimonialAdapter extends BaseAdapter {

    private Activity activity;
    private LayoutInflater inflater;
    private List<DataTestimonial> items;
     //private String URL = "http://192.168.1.87/chat/assets/images/1487052976.jpg";
    ImageLoader imageLoader = AppController.geInstance().getImageLoader();

    public TestimonialAdapter(Activity activity, List<DataTestimonial> items) {
        this.activity = activity;
        this.items = items;
    }

    @Override
    public int getCount(){
        return items.size();
    }

    @Override
    public Object getItem(int location){
        return items.get(location);
    }

    public long getItemId(int position){
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent){
        if(inflater == null)
            inflater = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        if(convertView == null)
            convertView = inflater.inflate(R.layout.list_testimonial,null);
        if(imageLoader == null)
            imageLoader = AppController.geInstance().getImageLoader();

        NetworkImageView foto = (NetworkImageView) convertView.findViewById(R.id.thumbnail);
        TextView id = (TextView) convertView.findViewById(R.id.id);
        TextView nama = (TextView) convertView.findViewById(R.id.nama);
        TextView deskripsi = (TextView) convertView.findViewById(R.id.deskripsi);

        DataTestimonial data = items.get(position);

        id.setText(data.getId());
        nama.setText(data.getNama());
        deskripsi.setText(data.getDeskripsi());
        foto.setImageUrl(data.getGambar(), imageLoader);

        return  convertView;
    }
}
