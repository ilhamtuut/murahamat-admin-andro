package wad.wan.murahamatdistro.adapter;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;

import java.util.List;

import wad.wan.murahamatdistro.DetailBarangActivity;
import wad.wan.murahamatdistro.R;
import wad.wan.murahamatdistro.app.AppController;
import wad.wan.murahamatdistro.data.DataBarang;

/**
 * Created by user on 18/03/2017.
 */
public class BarangAdapter extends RecyclerView.Adapter<BarangAdapter.MyViewHolder>{
    private Context mContext;
    private List<DataBarang> albumList;
    private static final String TAG = "Barang";
    ImageLoader imageLoader = AppController.geInstance().getImageLoader();

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


    public BarangAdapter(Context mContext, List<DataBarang> albumList) {
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
        final DataBarang album = albumList.get(position);
        holder.id.setText(album.getId());
        holder.title.setText(album.getNama_barang());
        holder.count.setText(album.getMerek());
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
                final String ukuranx = album.getUkuran();
                final String id_kategorix = album.getId_kategori();
                final String kategorix = album.getKategori();
                final String stockx = album.getStock();
                final String hargax = album.getHarga();
                final String merekx = album.getMerek();
                final String deskripsix = album.getDeskripsi();
                final String gambarx = album.getGambar();
                Bundle bn = new Bundle();
                bn.putString("id", idx);
                bn.putString("nama",namax);
                bn.putString("ukuran",ukuranx);
                bn.putString("id_kategori",id_kategorix);
                bn.putString("kategori",kategorix);
                bn.putString("stock", stockx);
                bn.putString("harga",hargax);
                bn.putString("merek",merekx);
                bn.putString("deskripsi",deskripsix);
                bn.putString("gambar",gambarx);
//                Toast.makeText(mContext, "click "+album.getId(), Toast.LENGTH_SHORT).show();
//                Intent intent = new Intent(mContext, DetailBarangActivity.class);
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
            //final DataTestimonial album = albumList.get();
            switch (menuItem.getItemId()) {
//                case R.id.action_edit:
//                    Toast.makeText(mContext, "Edit "+id, Toast.LENGTH_SHORT).show();
//                    return true;
                case R.id.action_delete:
                    Toast.makeText(mContext, "Delete", Toast.LENGTH_SHORT).show();
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

}
