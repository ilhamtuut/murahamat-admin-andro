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
import wad.wan.murahamatdistro.data.DataUser;

/**
 * Created by user on 19/03/2017.
 */
public class UserAdapter extends BaseAdapter {
    private Activity activity;
    private LayoutInflater inflater;
    private List<DataUser> items;

    public UserAdapter(Activity activity, List<DataUser> items) {
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
            convertView = inflater.inflate(R.layout.list_user,null);
        TextView id = (TextView) convertView.findViewById(R.id.text_id);
        TextView username = (TextView) convertView.findViewById(R.id.text_username);
        TextView pass = (TextView) convertView.findViewById(R.id.text_password);

        DataUser data = items.get(position);

        id.setText(data.getId());
        username.setText(data.getUsername());
        pass.setText(data.getPassword());

        return  convertView;
    }
}
