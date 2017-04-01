package wad.wan.murahamatdistro;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
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

import java.util.HashMap;
import java.util.Map;

import wad.wan.murahamatdistro.app.AppController;
import wad.wan.murahamatdistro.app.RequestHandler;
import wad.wan.murahamatdistro.data.DataPromo;
import wad.wan.murahamatdistro.url.Url;

public class DetailPromoActivity extends AppCompatActivity {

    EditText txt_id;
    Button btn_edit,btn_update,btn_batal;
    EditText txt_nama,txt_promo,txt_deskripsi;
    NetworkImageView niv;
    ImageLoader imageLoader = AppController.geInstance().getImageLoader();

    int success;

    private static final String TAG = DetailPromoActivity.class.getSimpleName();

    private static String url_update = Url.URL_PROMO_UPDATE;
    public static final String TAG_SUCCESS ="success";
    public static final String TAG_MESSAGE ="message";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_promo);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Detail Promo");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        txt_id = (EditText) findViewById(R.id.text_id);
        txt_nama = (EditText) findViewById(R.id.text_namabrg);
        txt_promo = (EditText) findViewById(R.id.text_promo);
        txt_deskripsi = (EditText) findViewById(R.id.text_deskripsi);
        btn_update = (Button) findViewById(R.id.btn_update);
        btn_edit = (Button) findViewById(R.id.btn_edit);
        btn_batal = (Button) findViewById(R.id.btn_batal);

        if(imageLoader == null)
            imageLoader = AppController.geInstance().getImageLoader();
        niv = (NetworkImageView) findViewById(R.id.thumbnail);

        Bundle b = getIntent().getExtras();
        txt_id.setText(b.getCharSequence("id"));
        txt_nama.setText(b.getCharSequence("nama"));
        txt_promo.setText(b.getCharSequence("promo"));
        txt_deskripsi.setText(b.getCharSequence("deskripsi"));
        niv.setImageUrl(b.getString("gambar"),imageLoader);

        btn_update.setVisibility(View.GONE);
        txt_nama.setEnabled(false);
        txt_promo.setEnabled(false);
        txt_deskripsi.setEnabled(false);
        niv.setEnabled(false);

        btn_edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                txt_nama.setEnabled(true);
                txt_promo.setEnabled(true);
                txt_deskripsi.setEnabled(true);
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
                txt_promo.setEnabled(false);
                txt_deskripsi.setEnabled(false);
                niv.setEnabled(false);
                DetailPromoActivity.this.finish();
//                Intent intent = new Intent(DetailPromoActivity.this,PromoActivity.class);
//                startActivity(intent);
            }
        });

        btn_batal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                btn_update.setVisibility(View.GONE);
                txt_nama.setEnabled(false);
                txt_promo.setEnabled(false);
                txt_deskripsi.setEnabled(false);
                niv.setEnabled(false);
                DetailPromoActivity.this.finish();
//                Intent intent = new Intent(DetailPromoActivity.this,PromoActivity.class);
//                startActivity(intent);
            }
        });

    }

    private void update(){
        final String id = txt_id.getText().toString().trim();
        final String promo = txt_promo.getText().toString().trim();
        final String deskripsi = txt_deskripsi.getText().toString().trim();

        StringRequest strReq=new StringRequest(Request.Method.POST, url_update, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d(TAG, "Response:" + response.toString());
                try {
                    JSONObject jObj = new JSONObject(response);
                    success = jObj.getInt(TAG_SUCCESS);

                    if (success == 1) {
                        Log.d("add/update", jObj.toString());
                        Toast.makeText(DetailPromoActivity.this, jObj.getString(TAG_MESSAGE),Toast.LENGTH_LONG).show();
                    } else {
                        Toast.makeText(DetailPromoActivity.this, jObj.getString(TAG_MESSAGE),Toast.LENGTH_LONG).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d(TAG,"Error"+ error.getMessage());
                Toast.makeText(DetailPromoActivity.this, "Failed Connect to server",Toast.LENGTH_LONG).show();
            }
        }){
            @Override
            protected Map<String,String> getParams(){
                Map<String, String> params = new HashMap<String, String>();
                    params.put("id",id);
                    params.put("promo",promo);
                    params.put("deskripsi",deskripsi);

                return params;
            }
        };
        RequestHandler.getInstance(this).addToRequestQueue(strReq);
    }


    @Override
    public boolean onSupportNavigateUp(){
        finish();
        return true;
    }


}
