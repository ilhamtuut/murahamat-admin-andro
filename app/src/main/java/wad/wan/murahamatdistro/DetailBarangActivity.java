package wad.wan.murahamatdistro;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.widget.EditText;
import android.widget.TextView;

import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;

import wad.wan.murahamatdistro.app.AppController;

public class DetailBarangActivity extends AppCompatActivity {
    EditText txt_id;
    TextView txt_nama,txt_ukuran,txt_kategori,txt_stock,txt_harga,txt_merek,txt_deskripsi;
    NetworkImageView niv;
    ImageLoader imageLoader = AppController.geInstance().getImageLoader();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_barang);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Detail Barang");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        txt_id = (EditText) findViewById(R.id.text_id);
        txt_nama = (TextView) findViewById(R.id.text_namabrg);
        txt_ukuran = (TextView) findViewById(R.id.text_ukuran);
        txt_kategori = (TextView) findViewById(R.id.text_kategori);
        txt_stock = (TextView) findViewById(R.id.text_stock);
        txt_harga = (TextView) findViewById(R.id.text_harga);
        txt_merek = (TextView) findViewById(R.id.text_merek);
        txt_deskripsi = (TextView) findViewById(R.id.text_deskripsi);

        if(imageLoader == null)
            imageLoader = AppController.geInstance().getImageLoader();
        niv = (NetworkImageView) findViewById(R.id.thumbnail);

        Bundle b = getIntent().getExtras();
        txt_id.setText(b.getCharSequence("id"));
        txt_nama.setText(b.getCharSequence("nama"));
        txt_ukuran.setText(b.getCharSequence("ukuran"));
        txt_kategori.setText(b.getCharSequence("kategori"));
        txt_stock.setText(b.getCharSequence("stock"));
        txt_harga.setText(b.getCharSequence("harga"));
        txt_merek.setText(b.getCharSequence("merek"));
        txt_deskripsi.setText(b.getCharSequence("deskripsi"));
        niv.setImageUrl(b.getString("gambar"),imageLoader);

    }

    @Override
    public boolean onSupportNavigateUp(){
        finish();
        return true;
    }
}
