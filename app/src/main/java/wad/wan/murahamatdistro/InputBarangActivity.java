package wad.wan.murahamatdistro;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import wad.wan.murahamatdistro.app.RequestHandler;
import wad.wan.murahamatdistro.data.DataKategori;
import wad.wan.murahamatdistro.url.Url;

public class InputBarangActivity extends AppCompatActivity {
    EditText text_nama_brg,text_harga,text_ukuran,text_stock,text_merek,text_deskripsi,text_kategori;
//    String nama_brg,ukuran,harga,id_kategori,merek,stock,deskripsi;
    Button btn_simpan;
    private ProgressDialog progressDialog;
    int success;
    Spinner spinner;
    ImageView imageView;
    Bitmap bitmap;
    int PICK_IMAGE_REQUEST = 1;
    public static final String TAG_SUCCESS ="success";
    public static final String TAG_MESSAGE ="message";
    private static String url_insert = Url.URL_BARANG_SAVE;
    private static String url_kategori = Url.URL_KATEGORI;


    private static final String TAG = InputBarangActivity.class.getSimpleName();

    private ArrayList<String> arrkategori;
    List<DataKategori> listKategori = new ArrayList<DataKategori>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_input_barang);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Input Barang");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        progressDialog = new ProgressDialog(this);

        text_nama_brg = (EditText) findViewById(R.id.edittext_namaBarang);
        text_kategori = (EditText) findViewById(R.id.edittext_kategori);
        text_ukuran = (EditText) findViewById(R.id.edittext_ukuran);
        text_harga = (EditText) findViewById(R.id.edittext_harga);
        text_merek = (EditText) findViewById(R.id.edittext_merek);
        text_deskripsi = (EditText) findViewById(R.id.edittext_deskripsi);
        text_stock = (EditText) findViewById(R.id.edittext_jumlahstock);
        imageView = (ImageView) findViewById(R.id.gambar_barang);
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showFileChooser();
            }
        });

        btn_simpan = (Button) findViewById(R.id.btn_simpan);
        btn_simpan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                simpan();
                inputBarang();
            }
        });

        arrkategori = new ArrayList<String>();
        spinner=(Spinner) findViewById(R.id.spin_kategori);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                text_kategori.setText(listKategori.get(position).getId());
            }


            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                text_kategori.setText(null);
            }
        });
        callKategori();
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
                        spinner.setAdapter(new ArrayAdapter<String>(InputBarangActivity.this, android.R.layout.simple_spinner_dropdown_item, arrkategori));
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

    private void kosong(){
        text_nama_brg.setText(null);
        text_kategori.setText(null);
        text_ukuran.setText(null);
        text_harga.setText(null);
        text_merek.setText(null);
        text_deskripsi.setText(null);
        text_stock.setText(null);
        imageView.setImageResource(0);
    }

    private void simpan(){
        final String nama_brg = text_nama_brg.getText().toString().trim();
        final String kategori = text_kategori.getText().toString().trim();
        final String ukuran = text_ukuran.getText().toString().trim();
        final String harga = text_harga.getText().toString().trim();
        final String stock = text_stock.getText().toString().trim();
        final String merek = text_merek.getText().toString().trim();
        final String deskripsi = text_deskripsi.getText().toString().trim();

        StringRequest strReq=new StringRequest(Request.Method.POST, url_insert, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d(TAG, "Response:" + response.toString());
                try {
                    JSONObject jObj = new JSONObject(response);
                    success = jObj.getInt(TAG_SUCCESS);

                    if (success == 1) {
                        Log.d("add", jObj.toString());
                        kosong();
                        Toast.makeText(InputBarangActivity.this, jObj.getString(TAG_MESSAGE),Toast.LENGTH_LONG).show();
                    } else {
                        Toast.makeText(InputBarangActivity.this, jObj.getString(TAG_MESSAGE),Toast.LENGTH_LONG).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d(TAG,"Error"+ error.getMessage());
                Toast.makeText(InputBarangActivity.this, "Failed Connect to server",Toast.LENGTH_LONG).show();
            }
        }){
            @Override
            protected Map<String,String> getParams(){
                Map<String, String> params = new HashMap<String, String>();
                    params.put("nama_barang",nama_brg);
                    params.put("id_kategori",kategori);
                    params.put("harga",harga);
                    params.put("stock",stock);
                    params.put("ukuran",ukuran);
                    params.put("merek",merek);
                    params.put("deskripsi",deskripsi);
                    params.put("gambar",getStringImage(bitmap));
                return params;
            }
        };
        RequestHandler.getInstance(this).addToRequestQueue(strReq);
    }

    private void inputBarang() {
        final String nama_brg = text_nama_brg.getText().toString().trim();
        final String kategori = text_kategori.getText().toString().trim();
        final String ukuran = text_ukuran.getText().toString().trim();
        final String harga = text_harga.getText().toString().trim();
        final String stock = text_stock.getText().toString().trim();
        final String merek = text_merek.getText().toString().trim();
        final String deskripsi = text_deskripsi.getText().toString().trim();

        progressDialog.setMessage("Wait ...");
        progressDialog.show();
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url_insert,
                new Response.Listener<String>(){
                    @Override
                    public void onResponse(String response){
                        progressDialog.dismiss();
                        Log.d(TAG,"Response:" + response.toString());
                        try {
                            JSONObject jObj = new JSONObject(response);
                            success = jObj.getInt(TAG_SUCCESS);

                            if (success == 1) {
                                Log.d("register", jObj.toString());
                                kosong();
                                Toast.makeText(InputBarangActivity.this, jObj.getString(TAG_MESSAGE),Toast.LENGTH_LONG).show();
                            } else {
                                Toast.makeText(InputBarangActivity.this, jObj.getString(TAG_MESSAGE),Toast.LENGTH_LONG).show();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener(){
                    @Override
                    public void onErrorResponse(VolleyError error){
                        progressDialog.hide();
                        Log.d(TAG,"Error"+ error.getMessage());
                        Toast.makeText(InputBarangActivity.this, error.getMessage(),Toast.LENGTH_LONG).show();
                    }
                }
        ){
            @Override
            protected Map<String,String> getParams(){
                HashMap<String,String> params = new HashMap<>();
                params.put("nama_barang",nama_brg);
                params.put("id_kategori",kategori);
                params.put("harga",harga);
                params.put("stock",stock);
                params.put("ukuran",ukuran);
                params.put("merek",merek);
                params.put("deskripsi",deskripsi);
                params.put("gambar",getStringImage(bitmap));
                return params;
            }
        };
        RequestHandler.getInstance(this).addToRequestQueue(stringRequest);

    }



    public String getStringImage (Bitmap bmp){
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bmp.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        byte[] imageBytes = baos.toByteArray();
        String encodedIMage = Base64.encodeToString(imageBytes, Base64.DEFAULT);
        return encodedIMage;
    }

    private void showFileChooser(){
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent,"Select Picture"), PICK_IMAGE_REQUEST);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data){
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null){
            Uri filePatch = data.getData();
            try {
                bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), filePatch);
                imageView.setImageBitmap(bitmap);
            }catch (IOException e){
                e.printStackTrace();
            }
        }
    }

    @Override
    public boolean onSupportNavigateUp(){
        finish();
        return true;
    }
}
