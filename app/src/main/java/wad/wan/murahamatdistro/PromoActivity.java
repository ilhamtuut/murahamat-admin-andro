package wad.wan.murahamatdistro;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
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

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import wad.wan.murahamatdistro.adapter.PromoAdapter;
import wad.wan.murahamatdistro.adapter.TesAdapter;
import wad.wan.murahamatdistro.adapter.TestimonialAdapter;
import wad.wan.murahamatdistro.app.AppController;
import wad.wan.murahamatdistro.app.RequestHandler;
import wad.wan.murahamatdistro.data.DataPromo;
import wad.wan.murahamatdistro.data.DataTestimonial;
import wad.wan.murahamatdistro.url.Url;

public class PromoActivity extends AppCompatActivity {

    FloatingActionButton fab;
    ListView list;
    SwipeRefreshLayout swipe;
    List<DataPromo> itemList = new ArrayList<DataPromo>();
    PromoAdapter adapter;
    AlertDialog.Builder dialog;
    LayoutInflater inflater;
    View dialogView;
    private RecyclerView recyclerView;
    EditText text_id,text_nama_brg,text_promo,text_harga,text_ukuran,text_stock,text_merek,text_deskripsi,text_kategori;
    String id,nama_brg,promo,gambar,ukuran,harga,kategori,merek,stock,deskripsi;
    NetworkImageView networkImageView;
    ImageView imageView;

    ImageLoader imageLoader = AppController.geInstance().getImageLoader();

    Bitmap bitmap;
    int success;
    int PICK_IMAGE_REQUEST = 1;

    private static final String TAG = TestimonialActivity.class.getSimpleName();

    private static String url_select = Url.URL_PROMO;
    private static String url_insert = Url.URL_PROMO_SAVE;
    private static String url_update = Url.URL_PROMO_UPDATE;
    private static String url_delete = Url.URL_PROMO_DELETE;
    private static String url_edit = Url.URL_PROMO_ID;

    public static final String TAG_ID ="id";
    public static final String TAG_NAMABARANG ="nama_barang";
    public static final String TAG_PROMO ="promo";
    public static final String TAG_HARGA ="harga";
    public static final String TAG_UKURAN ="ukuran";
    public static final String TAG_STOCK ="stock";
    public static final String TAG_MEREK ="merek";
    public static final String TAG_KATEGORI ="kategori";
    public static final String TAG_DESKRIPSI ="promo";
    public static final String TAG_GAMBAR ="gambar";
    public static final String TAG_SUCCESS ="success";
    public static final String TAG_MESSAGE ="message";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_promo);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Promo");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        fab =(FloatingActionButton) findViewById(R.id.fabs);
        swipe = (SwipeRefreshLayout) findViewById(R.id.swipe);

        itemList = new ArrayList<>();
        adapter = new PromoAdapter(this, itemList);
        recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        //recyclerView.setHasFixedSize(true);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(this);
//        RecyclerView.LayoutManager mLayoutManager = new GridLayoutManager(this, 2);
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setAdapter(adapter);

        swipe = (SwipeRefreshLayout) findViewById(R.id.swipe);
        swipe.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                itemList.clear();
                adapter.notifyDataSetChanged();
                callVolley();
            }
        });
        swipe.post(new Runnable() {
            @Override
            public void run() {
                swipe.setRefreshing(true);
                itemList.clear();
                adapter.notifyDataSetChanged();
                callVolley();
            }
        });
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DialogForm("","","","","","","","","","","SIMPAN");
            }
        });

//        recyclerView.setOnLongClickListener(new View.OnLongClickListener() {
//            @Override
//            public boolean onLongClick(View v) {
//                Intent intent = new Intent(PromoActivity.this, DetailTestimonialActivity.class);
//                startActivity(intent);
//                return false;
//            }
//        });
//
//        list.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
//            @Override
//            public boolean onItemLongClick(final AdapterView<?> parent, View view,final int position, long id) {
//                final String idx = itemList.get(position).getId();
//                final String namax = itemList.get(position).getNama_barang();
//                final String deskripsix = itemList.get(position).getDeskripsi();
//                final String gambarx = itemList.get(position).getGambar();
//                final CharSequence[] dialogitem = {"View","Edit","Delete"};
//                dialog = new AlertDialog.Builder(PromoActivity.this);
//                dialog.setCancelable(true);
//                dialog.setItems(dialogitem, new DialogInterface.OnClickListener(){
//                    @Override
//                    public  void onClick(DialogInterface dialog,int which){
//                        switch (which){
//                            case 0:
//                                Intent intent = new Intent(PromoActivity.this, DetailTestimonialActivity.class);
//                                Bundle bn = new Bundle();
//                                bn.putString("id", idx);
//                                bn.putString("nama",namax);
//                                bn.putString("deskripsi",deskripsix);
//                                bn.putString("gambar",gambarx);
//                                intent.putExtras(bn);
//                                startActivity(intent);
//                                break;
//                            case 1:
//                                edit(idx); //edit(idx);
//                                break;
//                            case 2:
//                                delete(idx); //delete(idx);
//                                break;
//                        }
//                    }
//                }).show();
//
//                return false;
//            }
//        });
    }

    private void kosong(){
        text_id.setText(null);
        text_nama_brg.setText(null);
        text_kategori.setText(null);
        text_ukuran.setText(null);
        text_harga.setText(null);
        text_merek.setText(null);
        text_deskripsi.setText(null);
        text_stock.setText(null);
        text_promo.setText(null);
        imageView.setImageResource(0);
    }

    private void DialogForm(String idx, String namax, String promox,String gambarx,String stockx,String merekx ,
                            String kategorix, String ukuranx, String hargax,String deskripsix,String button){
        dialog = new AlertDialog.Builder(PromoActivity.this);
        inflater = getLayoutInflater();
        dialogView = inflater.inflate(R.layout.form_promo,null);
        dialog.setView(dialogView);
        dialog.setCancelable(true);
//        dialog.setIcon(R.mipmap.ic_launcher);
        dialog.setTitle("Promo");

        text_id = (EditText) dialogView.findViewById(R.id.edittext_id);
        text_nama_brg = (EditText) dialogView.findViewById(R.id.edittext_namaBarang);
        text_promo = (EditText) dialogView.findViewById(R.id.edittext_promo);
        text_kategori = (EditText) dialogView.findViewById(R.id.edittext_kategori);
        text_ukuran = (EditText) dialogView.findViewById(R.id.edittext_ukuran);
        text_harga = (EditText) dialogView.findViewById(R.id.edittext_harga);
        text_merek = (EditText) dialogView.findViewById(R.id.edittext_merek);
        text_deskripsi = (EditText) dialogView.findViewById(R.id.edittext_deskripsi);
        text_stock = (EditText) dialogView.findViewById(R.id.edittext_jumlahstock);
        imageView = (ImageView) dialogView.findViewById(R.id.gambar_barang);
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showFileChooser();
            }
        });

        if(!idx.isEmpty()){
            text_id.setText(idx);
            text_nama_brg.setText(namax);
            text_promo.setText(promox);
            text_kategori.setText(kategorix);
            text_ukuran.setText(ukuranx);
            text_harga.setText(hargax);
            text_merek.setText(merekx);
            text_deskripsi.setText(deskripsix);
            text_stock.setText(stockx);

            ImageLoader.ImageListener listener = ImageLoader.getImageListener(imageView,R.drawable.ic_menu_camera, R.drawable.ic_menu_camera);
            imageLoader.get(gambarx, listener);


        }else {
            kosong();
        }

        dialog.setPositiveButton(button,new DialogInterface.OnClickListener(){
            @Override
            public void onClick(DialogInterface dialog, int which){
                id = text_id.getText().toString();
                nama_brg = text_nama_brg.getText().toString();
                promo = text_promo.getText().toString();
                harga = text_harga.getText().toString();
                ukuran = text_ukuran.getText().toString();
                kategori = text_kategori.getText().toString();
                merek = text_merek.getText().toString();
                deskripsi = text_deskripsi.getText().toString();
                stock = text_stock.getText().toString();

                simpan_update();
                dialog.dismiss();
                kosong();
            }

        });

        dialog.setNegativeButton("Batal",new DialogInterface.OnClickListener(){
            @Override
            public void onClick(DialogInterface dialog, int which){
                dialog.dismiss();
                kosong();
            }
        });
        dialog.show();
    }

    private void callVolley(){
        itemList.clear();
        adapter.notifyDataSetChanged();
        swipe.setRefreshing(true);

        JsonArrayRequest jArr = new JsonArrayRequest(url_select, new Response.Listener<JSONArray>(){

            @Override
            public void onResponse(JSONArray response){
                Log.d(TAG,response.toString());

                for (int i=0; i<response.length(); i++){
                    try {
                        JSONObject obj = response.getJSONObject(i);

                        DataPromo item = new DataPromo();
                        item.setId(obj.getString(TAG_ID));
                        item.setNama_barang(obj.getString(TAG_NAMABARANG));
                        item.setKategori(obj.getString(TAG_KATEGORI));
                        item.setHarga(obj.getString(TAG_HARGA));
                        item.setPromo(obj.getString(TAG_PROMO));
                        item.setStock(obj.getString(TAG_STOCK));
                        item.setUkuran(obj.getString(TAG_UKURAN));
                        item.setMerek(obj.getString(TAG_MEREK));
                        item.setDeskripsi(obj.getString(TAG_DESKRIPSI));
                        item.setGambar(obj.getString(TAG_GAMBAR));

                        itemList.add(item);

                    }catch (JSONException e){
                        e.printStackTrace();
                    }
                }
                adapter.notifyDataSetChanged();
                swipe.setRefreshing(false);
            }
        }, new Response.ErrorListener(){
            @Override
            public void onErrorResponse(VolleyError error){
                VolleyLog.d(TAG,"Error" + error.getMessage());
                swipe.setRefreshing(false);
            }
        });
        RequestHandler.getInstance(this).addToRequestQueue(jArr);
    }

    private void simpan_update(){
        String url;
        if(id.isEmpty()){
            url = url_insert;
        }else {
            url = url_update;
        }

        StringRequest strReq=new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d(TAG, "Response:" + response.toString());
                try {
                    JSONObject jObj = new JSONObject(response);
                    success = jObj.getInt(TAG_SUCCESS);

                    if (success == 1) {
                        Log.d("add/update", jObj.toString());
                        callVolley();
                        kosong();

                        Toast.makeText(PromoActivity.this, jObj.getString(TAG_MESSAGE),Toast.LENGTH_LONG).show();
                        adapter.notifyDataSetChanged();
                    } else {
                        Toast.makeText(PromoActivity.this, jObj.getString(TAG_MESSAGE),Toast.LENGTH_LONG).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d(TAG,"Error"+ error.getMessage());
                Toast.makeText(PromoActivity.this, error.getMessage(),Toast.LENGTH_LONG).show();
            }
        }){
            @Override
            protected Map<String,String> getParams(){
                Map<String, String> params = new HashMap<String, String>();
                if(id.isEmpty()){
                    params.put("nama_barang",nama_brg);
                    params.put("id_kategori",kategori);
                    params.put("promo",promo);
                    params.put("harga",harga);
                    params.put("stock",stock);
                    params.put("ukuran",ukuran);
                    params.put("merek",merek);
                    params.put("deskripsi",deskripsi);
                    params.put("gambar",getStringImage(bitmap));
                }else {
                    params.put("id",id);
                    params.put("nama",nama_brg);
                    params.put("id_kategori",kategori);
                    params.put("promo",promo);
                    params.put("harga",harga);
                    params.put("stock",stock);
                    params.put("ukuran",ukuran);
                    params.put("merek",merek);
                    params.put("deskripsi",deskripsi);
                    params.put("gambar",getStringImage(bitmap));
                }
                return params;
            }
        };
        RequestHandler.getInstance(this).addToRequestQueue(strReq);
    }

    private void edit(final String idx){
        StringRequest strReq = new StringRequest(Request.Method.POST, url_edit, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d(TAG,"Response:" + response.toString());
                try{
                    JSONObject jObj = new JSONObject(response);
                    success = jObj.getInt(TAG_SUCCESS);

                    if(success==1){
                        Log.d("get edit data", jObj.toString());
                        String idx = jObj.getString(TAG_ID);
                        String namax = jObj.getString(TAG_NAMABARANG);
                        String promox = jObj.getString(TAG_PROMO);
                        String gambarx = jObj.getString(TAG_GAMBAR);
                        String hargax = jObj.getString(TAG_HARGA);
                        String stockx = jObj.getString(TAG_STOCK);
                        String ukuranx = jObj.getString(TAG_UKURAN);
                        String kategorix = jObj.getString(TAG_KATEGORI);
                        String deskripsix = jObj.getString(TAG_DESKRIPSI);
                        String merekx = jObj.getString(TAG_MEREK);

                        DialogForm(idx, namax, promox, gambarx, stockx, merekx ,
                                 kategorix, ukuranx, hargax,deskripsix,"UPDATE");
                        adapter.notifyDataSetChanged();
                    }else{
                        Toast.makeText(PromoActivity.this, jObj.getString(TAG_MESSAGE),Toast.LENGTH_LONG).show();
                    }
                }catch (JSONException e){
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener(){
            @Override
            public void onErrorResponse(VolleyError error){
                Log.e(TAG,"Error" + error.getMessage());
                Toast.makeText(PromoActivity.this, error.getMessage(),Toast.LENGTH_LONG).show();
            }
        }){
            @Override
            protected Map<String, String> getParams(){
                Map<String, String> params = new HashMap<String, String>();
                params.put("id",idx);
                return params;
            }
        };
        RequestHandler.getInstance(this).addToRequestQueue(strReq);
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
                        callVolley();
                        Toast.makeText(PromoActivity.this, jObj.getString(TAG_MESSAGE),Toast.LENGTH_LONG).show();
                        adapter.notifyDataSetChanged();
                    }else{
                        Toast.makeText(PromoActivity.this, jObj.getString(TAG_MESSAGE),Toast.LENGTH_LONG).show();
                    }
                }catch (JSONException e){
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener(){
            @Override
            public void onErrorResponse(VolleyError error){
                Log.e(TAG,"Error" + error.getMessage());
                Toast.makeText(PromoActivity.this, error.getMessage(),Toast.LENGTH_LONG).show();
            }
        }){
            @Override
            protected Map<String, String> getParams(){
                Map<String, String> params = new HashMap<String, String>();
                params.put("id",idx);
                return params;
            }
        };
        RequestHandler.getInstance(this).addToRequestQueue(strReq);
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
