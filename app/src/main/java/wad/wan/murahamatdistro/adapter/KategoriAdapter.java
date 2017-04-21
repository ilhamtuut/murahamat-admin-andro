package wad.wan.murahamatdistro.adapter;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;

import wad.wan.murahamatdistro.R;
import wad.wan.murahamatdistro.data.Category;

/**
 * Created by user on 19/03/2017.
 */
public class KategoriAdapter extends BaseAdapter {
    private Activity activity;
    private LayoutInflater inflater;
    private List<Category> items;

    public KategoriAdapter(Activity activity, List<Category> items) {
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
            convertView = inflater.inflate(R.layout.list_kategori,null);
        TextView id = (TextView) convertView.findViewById(R.id.text_id);
        TextView kategori = (TextView) convertView.findViewById(R.id.text_kategori);

        Category data = items.get(position);

        id.setText(data.getId());
        kategori.setText(data.getName());

        return  convertView;
    }
}
