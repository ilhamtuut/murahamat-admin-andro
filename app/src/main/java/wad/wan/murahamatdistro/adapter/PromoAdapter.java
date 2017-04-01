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
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import wad.wan.murahamatdistro.DetailPromoActivity;
import wad.wan.murahamatdistro.R;
import wad.wan.murahamatdistro.app.AppController;
import wad.wan.murahamatdistro.app.RequestHandler;
import wad.wan.murahamatdistro.data.DataPromo;
import wad.wan.murahamatdistro.url.Url;

public class PromoAdapter extends RecyclerView.Adapter<PromoAdapter.MyViewHolder> {

    private Context mContext;
    private List<DataPromo> albumList;
    private static String url_delete = Url.URL_PROMO_DELETE;

    int success;
    private static final String TAG = "Promo";
    public static final String TAG_SUCCESS ="success";
    public static final String TAG_MESSAGE ="message";

    ImageLoader imageLoader = AppController.geInstance().getImageLoader();

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView title, id;
        public ImageView  overflow;
        public NetworkImageView thumbnail;

        public MyViewHolder(View view) {
            super(view);
            id = (TextView) view.findViewById(R.id.id_a);
            title = (TextView) view.findViewById(R.id.title);
            thumbnail = (NetworkImageView) view.findViewById(R.id.thumbnail);
            overflow = (ImageView) view.findViewById(R.id.overflow);
        }

    }


    public PromoAdapter(Context mContext, List<DataPromo> albumList) {
        this.mContext = mContext;
        this.albumList = albumList;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.list_cardview_promo, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {
        final DataPromo album = albumList.get(position);
        holder.id.setText(album.getId());
        holder.title.setText(album.getPromo());
        holder.thumbnail.setImageUrl(album.getGambar(),imageLoader);

        holder.overflow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showPopupMenu(holder.overflow, album.getId());
            }
        });

        holder.thumbnail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String idx = album.getId();
                final String namax = album.getNama_barang();
                final String promox = album.getPromo();
                final String deskripsix = album.getDeskripsi();
                final String gambarx = album.getGambar();

                Bundle bn = new Bundle();
                bn.putString("id", idx);
                bn.putString("nama",namax);
                bn.putString("promo",promox);
                bn.putString("deskripsi",deskripsix);
                bn.putString("gambar",gambarx);
                v.getContext().startActivity(new Intent(mContext, DetailPromoActivity.class).putExtras(bn));

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
        String id,nama;
        public MyMenuItemClickListener(String id) {
            this.id = id;
        }

        @Override
        public boolean onMenuItemClick(MenuItem menuItem) {
            //final DataTestimonial album = albumList.get();
            switch (menuItem.getItemId()) {
//                case R.id.action_edit:
//
//                    Toast.makeText(mContext, "Edit "+id+nama, Toast.LENGTH_SHORT).show();
//                    return true;
                case R.id.action_delete:
                    Toast.makeText(mContext, "Delete", Toast.LENGTH_SHORT).show();
                    delete(id);
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

    private void delete(final String idx){
        StringRequest strReq = new StringRequest(Request.Method.POST, url_delete, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d(TAG,"Response:" + response.toString());
                try{
                    JSONObject jObj = new JSONObject(response);
                    success = jObj.getInt(TAG_SUCCESS);

                    if(success==1){
                        Log.d("delete", jObj.toString());
                        Toast.makeText(mContext, jObj.getString(TAG_MESSAGE),Toast.LENGTH_LONG).show();
                    }else{
                        Toast.makeText(mContext, jObj.getString(TAG_MESSAGE),Toast.LENGTH_LONG).show();
                    }
                }catch (JSONException e){
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener(){
            @Override
            public void onErrorResponse(VolleyError error){
                Log.e(TAG,"Error" + error.getMessage());
                Toast.makeText(mContext, "No Internet Connection",Toast.LENGTH_LONG).show();
            }
        }){
            @Override
            protected Map<String, String> getParams(){
                Map<String, String> params = new HashMap<String, String>();
                params.put("id",idx);
                return params;
            }
        };
        RequestHandler.getInstance(mContext).addToRequestQueue(strReq);
    }
}

