package wad.wan.murahamatdistro;

import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import wad.wan.murahamatdistro.adapter.ViewPagerAdapter;
import wad.wan.murahamatdistro.app.RequestHandler;
import wad.wan.murahamatdistro.data.Image;

public class ImageSlide extends AppCompatActivity {
    ViewPager viewPager;
    ViewPagerAdapter adapter;
    List<Image> itemList = new ArrayList<Image>();

    private static final String TAG = ImageSlide.class.getSimpleName();

    private static String url = "http://192.168.43.174/mrmht/public/api/product";

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
//        callVolley();
        product();
    }

    private void product(){
        StringRequest strReq = new StringRequest(
                Request.Method.GET, url+"/"+6, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d(TAG,"Response:" + response.toString());

                for (int i=0; i<6; i++){
                    try {
                        JSONObject obj = new JSONObject(response);
                        String img[]={
                                obj.getString("image1"),
                                obj.getString("image2"),
                                obj.getString("image3"),
                                obj.getString("image4"),
                                obj.getString("image5"),
                                obj.getString("image6")};
                        Image item = new Image();
                        item.setGambar(img[i]);
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
                Log.e(TAG,"Error" + error.getMessage());
            }
        });
        RequestHandler.getInstance(this).addToRequestQueue(strReq);
    }


    @Override
    public boolean onSupportNavigateUp(){
        finish();
        return true;
    }
}
