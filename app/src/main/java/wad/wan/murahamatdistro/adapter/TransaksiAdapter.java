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
import wad.wan.murahamatdistro.data.Transaksi;

/**
 * Created by user on 03/04/2017.
 */
public class TransaksiAdapter extends BaseAdapter {
    private Activity activity;
    private LayoutInflater inflater;
    private List<Transaksi> items;

    public TransaksiAdapter(Activity activity, List<Transaksi> items) {
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
            convertView = inflater.inflate(R.layout.list_transaksi,null);
        TextView id = (TextView) convertView.findViewById(R.id.id);
        TextView nama = (TextView) convertView.findViewById(R.id.nama);
        TextView brg = (TextView) convertView.findViewById(R.id.namaBarang);

        Transaksi data = items.get(position);

        id.setText(data.getId());
        nama.setText(data.getName());
        brg.setText(data.getName_product());

        return  convertView;
    }
}
