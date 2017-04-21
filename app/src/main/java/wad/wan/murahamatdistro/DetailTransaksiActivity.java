package wad.wan.murahamatdistro;

import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.widget.EditText;
import android.widget.TextView;

public class DetailTransaksiActivity extends AppCompatActivity {
    TextView id,name,product_name,tot_qty,tot_price,date;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_transaksi);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Detail Transaksi");
        toolbar.setTitleTextColor(Color.parseColor("#FFFFFF"));
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        id = (EditText) findViewById(R.id.id_transaction);
        name = (TextView) findViewById(R.id.name);
        product_name = (TextView) findViewById(R.id.product_name);
        tot_qty = (TextView) findViewById(R.id.qty);
        tot_price = (TextView) findViewById(R.id.tot_price);
        date = (TextView) findViewById(R.id.date_transaction);

        Bundle b = getIntent().getExtras();
        id.setText(b.getString("id"));
        name.setText(b.getString("name"));
        product_name.setText(b.getString("product"));
        tot_qty.setText(b.getString("qty"));
        tot_price.setText(b.getString("price"));
        date.setText(b.getString("date"));

    }

    @Override
    public boolean onSupportNavigateUp(){
        finish();
        return true;
    }
}
