package wad.wan.murahamatdistro.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
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
import wad.wan.murahamatdistro.data.DataTestimonial;

/**
 * Created by user on 20/03/2017.
 */
public class TesAdapter extends RecyclerView.Adapter<TesAdapter.MyViewHolder> {

    private Context mContext;
    private List<DataBarang> albumList;
    private static final String TAG = "Tes";
    ImageLoader imageLoader = AppController.geInstance().getImageLoader();

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView title, count,id;
        public ImageView  overflow;
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


    public TesAdapter(Context mContext, List<DataBarang> albumList) {
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
        holder.count.setText(album.getDeskripsi() + " songs");
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
                Toast.makeText(mContext, "click "+album.getId(), Toast.LENGTH_SHORT).show();
//                Intent intent = new Intent(mContext, DetailBarangActivity.class);
                v.getContext().startActivity(new Intent(mContext, DetailBarangActivity.class));

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
                    Toast.makeText(mContext, "Delete"+id, Toast.LENGTH_SHORT).show();
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
