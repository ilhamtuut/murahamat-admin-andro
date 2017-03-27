package wad.wan.murahamatdistro;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.widget.EditText;
import android.widget.TextView;

import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;

import wad.wan.murahamatdistro.app.AppController;

public class DetailPromoActivity extends AppCompatActivity {

    EditText txt_id;
    TextView txt_nama,txt_promo,txt_deskripsi;
    NetworkImageView niv;
    ImageLoader imageLoader = AppController.geInstance().getImageLoader();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_promo);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Detail Promo");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        txt_id = (EditText) findViewById(R.id.text_id);
        txt_nama = (TextView) findViewById(R.id.text_namabrg);
        txt_deskripsi = (TextView) findViewById(R.id.text_deskripsi);
        txt_promo = (TextView) findViewById(R.id.text_promo);

        if(imageLoader == null)
            imageLoader = AppController.geInstance().getImageLoader();
        niv = (NetworkImageView) findViewById(R.id.thumbnail);

        Bundle b = getIntent().getExtras();
        txt_id.setText(b.getCharSequence("id"));
        txt_nama.setText(b.getCharSequence("nama"));
        txt_promo.setText(b.getCharSequence("promo"));
        txt_deskripsi.setText(b.getCharSequence("deskripsi"));
        niv.setImageUrl(b.getString("gambar"),imageLoader);

    }

    @Override
    public boolean onSupportNavigateUp(){
        finish();
        return true;
    }
}
