package wad.wan.murahamatdistro;

import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.widget.Toast;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonArrayRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import wad.wan.murahamatdistro.adapter.ViewPagerAdapter;
import wad.wan.murahamatdistro.app.RequestHandler;
import wad.wan.murahamatdistro.data.DataTestimonial;
import wad.wan.murahamatdistro.data.Image;
import wad.wan.murahamatdistro.url.Url;

public class ImageSlide extends AppCompatActivity {
    ViewPager viewPager;
    ViewPagerAdapter adapter;
    List<Image> itemList = new ArrayList<Image>();

    private static final String TAG = ImageSlide.class.getSimpleName();

    private static String url_select = "http://192.168.43.174/murahamat/gambar/data";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image_slide);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Image Slide");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        viewPager = (ViewPager) findViewById(R.id.viewPager);
        adapter = new ViewPagerAdapter(this,itemList);
        viewPager.setAdapter(adapter);
        callVolley();
    }

    private void callVolley(){
        itemList.clear();
        adapter.notifyDataSetChanged();


        JsonArrayRequest jArr = new JsonArrayRequest(url_select, new Response.Listener<JSONArray>(){

            @Override
            public void onResponse(JSONArray response){
                Log.e(TAG,response.toString());

                for (int i=0; i<response.length(); i++){
                    try {
                        JSONObject obj = response.getJSONObject(i);

                        Image item = new Image();
                        item.setId(obj.getString("id"));
                        item.setGambar(obj.getString("gambar"));

                        itemList.add(item);

                    }catch (JSONException e){
                        e.printStackTrace();
                    }
                }
                adapter.notifyDataSetChanged();

            }
        }, new Response.ErrorListener(){
            @Override
            public void onErrorResponse(VolleyError error){
                VolleyLog.d(TAG,"Error" + error.getMessage());
                Toast.makeText(ImageSlide.this, error.getMessage(),Toast.LENGTH_LONG).show();
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
