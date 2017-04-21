package wad.wan.murahamatdistro.adapter;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.NetworkImageView;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

import wad.wan.murahamatdistro.DetailBarangActivity;
import wad.wan.murahamatdistro.MainActivity;
import wad.wan.murahamatdistro.R;
import wad.wan.murahamatdistro.app.AppController;
import wad.wan.murahamatdistro.app.RequestHandler;
import wad.wan.murahamatdistro.data.DataBarang;
import wad.wan.murahamatdistro.data.Products;

/**
 * Created by user on 18/03/2017.
 */
public class BarangAdapter extends RecyclerView.Adapter<BarangAdapter.MyViewHolder>{
    private Context mContext;
    private List<Products> albumList;
    ImageLoader imageLoader = AppController.geInstance().getImageLoader();

    String status;
    private static String url = "http://192.168.43.174/mrmht/public/api/product";

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView title, count,id;
        public ImageView overflow;
        public NetworkImageView thumbnail;

        public MyViewHolder(View view) {
            super(view);
            id = (TextView) view.findViewById(R.id.id_a);
            title = (TextView) view.findViewById(R.id.title);
            count = (TextView) view.findViewById(R.id.count);
            thumbnail = (NetworkImageView) view.findViewById(R.id.thumbnail);
            overflow = (ImageView) view.findViewById(R.id.overflow);
        }

    }


    public BarangAdapter(Context mContext, List<Products> albumList) {
        this.mContext = mContext;
        this.albumList = albumList;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.list_cardview, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {
        final Products products = albumList.get(position);
        holder.id.setText(products.getId());
        holder.title.setText(products.getName());
        holder.count.setText(products.getMerk());
        holder.thumbnail.setImageUrl(products.getImage1(),imageLoader);

        holder.overflow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showPopupMenu(holder.overflow, products.getId());
            }
        });
        holder.thumbnail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String idx = products.getId();
                Bundle bn = new Bundle();
                bn.putString("id", idx);

                v.getContext().startActivity(new Intent(mContext, DetailBarangActivity.class).putExtras(bn));

            }
        });
    }

    /**
     * Showing popup menu when tapping on 3 dots
     */
    private void showPopupMenu(View view, String position) {
        // inflate menu
        PopupMenu popup = new PopupMenu(mContext, view);
        MenuInflater inflater = popup.getMenuInflater();
        inflater.inflate(R.menu.menu_card, popup.getMenu());
        popup.setOnMenuItemClickListener(new MyMenuItemClickListener(position));
        popup.show();
    }

    /**
     * Click listener for popup menu items
     */
    class MyMenuItemClickListener implements PopupMenu.OnMenuItemClickListener {
        String id;
        public MyMenuItemClickListener(String id) {
            this.id = id;
        }

        @Override
        public boolean onMenuItemClick(MenuItem menuItem) {

            switch (menuItem.getItemId()) {
                case R.id.action_delete:
                    delete(id);
//                    ((MainActivity)mContext).callVolley();
                    return true;
                default:
            }
            return false;
        }
    }

    @Override
    public int getItemCount() {
        return albumList.size();
    }

    public void delete(final String idx){
        StringRequest strReq = new StringRequest(
                Request.Method.DELETE, url +"/"+ idx, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
//                Log.d(TAG,"Response:" + response.toString());
                try{
                    JSONObject jObj = new JSONObject(response);
                    status = jObj.getString("status");
                    if(status.equals("ok")){
//                        callVolley();
                        ((MainActivity)mContext).callVolley();
                        Toast.makeText(mContext, "Success delete product",Toast.LENGTH_LONG).show();
//                        adapter.notifyDataSetChanged();
                    }else {
                        Toast.makeText(mContext, "Failed delete product",Toast.LENGTH_LONG).show();
                    }

                }catch (JSONException e){
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener(){
            @Override
            public void onErrorResponse(VolleyError error){
//                Log.e(TAG,"Error" + error.getMessage());
                Toast.makeText(mContext, "Failed Connect to server",Toast.LENGTH_LONG).show();
            }
        });
        RequestHandler.getInstance(mContext).addToRequestQueue(strReq);
    }

}
