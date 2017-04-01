package wad.wan.murahamatdistro;

import android.content.Intent;
import android.graphics.Bitmap;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import wad.wan.murahamatdistro.app.AppController;
import wad.wan.murahamatdistro.app.RequestHandler;
import wad.wan.murahamatdistro.data.DataKategori;
import wad.wan.murahamatdistro.url.Url;

public class DetailBarangActivity extends AppCompatActivity {
    EditText txt_id;
    EditText txt_nama,txt_ukuran,txt_kategori,txt_stock,txt_harga,txt_merek,txt_deskripsi;
    NetworkImageView niv;
    Button btn_edit,btn_update,btn_batal;
    Spinner spinner;
    private ArrayList<String> arrkategori;
    List<DataKategori> listKategori = new ArrayList<DataKategori>();


    ImageLoader imageLoader = AppController.geInstance().getImageLoader();

    int success;

    private static final String TAG = DetailBarangActivity.class.getSimpleName();

    private static String url_update = Url.URL_BARANG_UPDATE;
    private static String url_kategori = Url.URL_KATEGORI;
    public static final String TAG_SUCCESS ="success";
    public static final String TAG_MESSAGE ="message";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_barang);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Detail Barang");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        txt_id = (EditText) findViewById(R.id.text_id);
        txt_nama = (EditText) findViewById(R.id.text_namabrg);
        txt_ukuran = (EditText) findViewById(R.id.text_ukuran);
        txt_kategori = (EditText) findViewById(R.id.text_kategori);
        txt_stock = (EditText) findViewById(R.id.text_stock);
        txt_harga = (EditText) findViewById(R.id.text_harga);
        txt_merek = (EditText) findViewById(R.id.text_merek);
        txt_deskripsi = (EditText) findViewById(R.id.text_deskripsi);
        btn_update = (Button) findViewById(R.id.btn_update);
        btn_edit = (Button) findViewById(R.id.btn_edit);
        btn_batal = (Button) findViewById(R.id.btn_batal);
        spinner=(Spinner) findViewById(R.id.spin_kategori);


        if(imageLoader == null)
            imageLoader = AppController.geInstance().getImageLoader();
        niv = (NetworkImageView) findViewById(R.id.thumbnail);

        Bundle b = getIntent().getExtras();
        txt_id.setText(b.getCharSequence("id"));
        txt_nama.setText(b.getCharSequence("nama"));
        txt_ukuran.setText(b.getCharSequence("ukuran"));
        txt_kategori.setText(b.getCharSequence("id_kategori"));
        spinner.setPrompt(b.getCharSequence("kategori"));
        spinner.setSelected(true);
        txt_stock.setText(b.getCharSequence("stock"));
        txt_harga.setText(b.getCharSequence("harga"));
        txt_merek.setText(b.getCharSequence("merek"));
        txt_deskripsi.setText(b.getCharSequence("deskripsi"));
        niv.setImageUrl(b.getString("gambar"),imageLoader);

        btn_update.setVisibility(View.GONE);
        txt_nama.setEnabled(false);
        txt_ukuran.setEnabled(false);
        txt_kategori.setEnabled(false);
        txt_stock.setEnabled(false);
        txt_harga.setEnabled(false);
        txt_merek.setEnabled(false);
        txt_deskripsi.setEnabled(false);
        spinner.setEnabled(false);
        niv.setEnabled(false);

        btn_edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                txt_nama.setEnabled(true);
                txt_ukuran.setEnabled(true);
                txt_kategori.setEnabled(true);
                txt_stock.setEnabled(true);
                txt_harga.setEnabled(true);
                txt_merek.setEnabled(true);
                txt_deskripsi.setEnabled(true);
                spinner.setEnabled(true);
                niv.setEnabled(true);
                btn_update.setVisibility(View.VISIBLE);
            }
        });


        btn_update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                update();
                btn_update.setVisibility(View.GONE);
                txt_nama.setEnabled(false);
                txt_ukuran.setEnabled(false);
                txt_kategori.setEnabled(false);
                txt_stock.setEnabled(false);
                txt_harga.setEnabled(false);
                txt_merek.setEnabled(false);
                txt_deskripsi.setEnabled(false);
                spinner.setEnabled(false);
                niv.setEnabled(false);
                DetailBarangActivity.this.finish();
//                Intent intent = new Intent(DetailBarangActivity.this,MainActivity.class);
//                startActivity(intent);
            }
        });

        btn_batal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                btn_update.setVisibility(View.GONE);
                txt_nama.setEnabled(false);
                txt_ukuran.setEnabled(false);
                txt_kategori.setEnabled(false);
                txt_stock.setEnabled(false);
                txt_harga.setEnabled(false);
                txt_merek.setEnabled(false);
                txt_deskripsi.setEnabled(false);
                niv.setEnabled(false);
                DetailBarangActivity.this.finish();
//                Intent intent = new Intent(DetailBarangActivity.this,MainActivity.class);
//                startActivity(intent);
            }
        });

        arrkategori = new ArrayList<String>();
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                txt_kategori.setText(listKategori.get(position).getId());
            }


            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                txt_kategori.setText(null);
            }
        });

        callKategori();
    }

    private void update(){
        final String id = txt_id.getText().toString().trim();
        final String nama = txt_nama.getText().toString().trim();
        final String ukuran = txt_ukuran.getText().toString().trim();
        final String kategori = txt_kategori.getText().toString().trim();
        final String stock = txt_stock.getText().toString().trim();
        final String harga = txt_harga.getText().toString().trim();
        final String merek = txt_merek.getText().toString().trim();
        final String deskripsi = txt_deskripsi.getText().toString().trim();
//        final String gambar = niv.setImageBitmap(Bitmap.createBitmap("gambar"));

        StringRequest strReq=new StringRequest(Request.Method.POST, url_update, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d(TAG, "Response:" + response.toString());
                try {
                    JSONObject jObj = new JSONObject(response);
                    success = jObj.getInt(TAG_SUCCESS);

                    if (success == 1) {
                        Log.d("add/update", jObj.toString());
                        Toast.makeText(DetailBarangActivity.this, jObj.getString(TAG_MESSAGE),Toast.LENGTH_LONG).show();
                    } else {
                        Toast.makeText(DetailBarangActivity.this, jObj.getString(TAG_MESSAGE),Toast.LENGTH_LONG).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d(TAG,"Error"+ error.getMessage());
                Toast.makeText(DetailBarangActivity.this, "Failed Connect to server",Toast.LENGTH_LONG).show();
            }
        }){
            @Override
            protected Map<String,String> getParams(){
                Map<String, String> params = new HashMap<String, String>();
                params.put("id",id);
                params.put("nama_barang",nama);
                params.put("kategori",kategori);
                params.put("ukuran",ukuran);
                params.put("harga",harga);
                params.put("stock",stock);
                params.put("merek",merek);
                params.put("deskripsi",deskripsi);

                return params;
            }
        };
        RequestHandler.getInstance(this).addToRequestQueue(strReq);
    }

    private void callKategori(){

        JsonArrayRequest jArr = new JsonArrayRequest(url_kategori, new Response.Listener<JSONArray>(){

            @Override
            public void onResponse(JSONArray response){
                Log.d(TAG,response.toString());

                for (int i=0; i<response.length(); i++){
                    try {
                        JSONObject obj = response.getJSONObject(i);

                        DataKategori item = new DataKategori();
                        item.setId(obj.getString("id"));
                        item.setKategori(obj.getString("kategori"));
                        listKategori.add(item);

                        arrkategori.add(obj.getString("kategori"));
                        spinner.setAdapter(new ArrayAdapter<String>(DetailBarangActivity.this, android.R.layout.simple_spinner_dropdown_item, arrkategori));
                    }catch (JSONException e){
                        e.printStackTrace();
                    }
                }

            }
        }, new Response.ErrorListener(){
            @Override
            public void onErrorResponse(VolleyError error){
                VolleyLog.d(TAG,"Error" + error.getMessage());

            }
        });
        RequestHandler.getInstance(this).addToRequestQueue(jArr);
    }



    @Override
    public boolean onSupportNavigateUp(){
        finish();
        return true;
    }
}
