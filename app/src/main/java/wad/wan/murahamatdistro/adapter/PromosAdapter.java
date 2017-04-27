package wad.wan.murahamatdistro.adapter;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.toolbox.ImageLoader;
import com.squareup.picasso.Picasso;

import java.util.List;

import wad.wan.murahamatdistro.DetailPromoActivity;
import wad.wan.murahamatdistro.PromoActivity;
import wad.wan.murahamatdistro.R;
import wad.wan.murahamatdistro.app.AppController;
import wad.wan.murahamatdistro.data.Promo;

public class PromosAdapter extends RecyclerView.Adapter<PromosAdapter.MyViewHolder> {

    private Context mContext;
    private List<Promo> albumList;

    ImageLoader imageLoader = AppController.geInstance().getImageLoader();

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView title, id;
        public ImageView  overflow;
        public ImageView thumbnail;
        public CardView cardView;

        public MyViewHolder(View view) {
            super(view);
            id = (TextView) view.findViewById(R.id.id_a);
            title = (TextView) view.findViewById(R.id.title);
            thumbnail = (ImageView) view.findViewById(R.id.thumbnail);
            overflow = (ImageView) view.findViewById(R.id.overflow);
            cardView = (CardView) view.findViewById(R.id.card_view_promo);

        }

    }


    public PromosAdapter(Context mContext, List<Promo> albumList) {
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
        final Promo album = albumList.get(position);
        holder.id.setText(album.getId());
        holder.title.setText(album.getName());
        Picasso.with(mContext).load(album.getImage()).into(holder.thumbnail);
        holder.overflow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showPopupMenu(holder.overflow, album.getId());
            }
        });

        holder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String idx = album.getId();
                final String namax = album.getName();
                final String deskripsix = album.getDescription();
                final String gambarx = album.getImage();

                Bundle bn = new Bundle();
                bn.putString("id", idx);
                bn.putString("nama",namax);
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
        String id;
        public MyMenuItemClickListener(String id) {
            this.id = id;
        }

        @Override
        public boolean onMenuItemClick(MenuItem menuItem) {
            //final DataTestimonial album = albumList.get();
            switch (menuItem.getItemId()) {
                case R.id.action_delete:
                    ((PromoActivity)mContext).delete(id);
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

