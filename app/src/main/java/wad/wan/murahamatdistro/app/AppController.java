package wad.wan.murahamatdistro.app;

import android.app.Application;
import android.text.TextUtils;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.Volley;

/**
 * Created by user on 09/02/2017.
 */
public class AppController extends Application {
    public static final String TAG = AppController.class.getSimpleName();
    private RequestQueue mRequestQueue;
    private ImageLoader mImageLoader;
    LruBitmapCache mLruBitmapCache;

    private static AppController mInstance;

    @Override
    public void onCreate(){
        super.onCreate();
        mInstance = this;
    }

    public static synchronized AppController geInstance(){
        return mInstance;
    }

    public RequestQueue getRequestQueue() {
        if( mRequestQueue == null){
            mRequestQueue = Volley.newRequestQueue(getApplicationContext());
        }
        return mRequestQueue;
    }

    public ImageLoader getImageLoader(){
        getRequestQueue();
        if(mImageLoader == null){
            getLruBitmapCache();
            mImageLoader = new ImageLoader(this.mRequestQueue, new LruBitmapCache());
        }
        return this.mImageLoader;
    }

    public LruBitmapCache getLruBitmapCache(){
        if(mLruBitmapCache == null)
            mLruBitmapCache = new LruBitmapCache();
            return this.mLruBitmapCache;
    }

    public <T> void addToRequestQueue(Request<T> reg, String tag){
        reg.setTag(TextUtils.isEmpty(tag)? TAG:tag);
        getRequestQueue().add(reg);
    }

    public <T> void addToRequestQueue(Request<T> reg){
        reg.setTag(TAG);
        getRequestQueue().add(reg);
    }

    public void cancelPendingRequests(Object tag){
        if(mRequestQueue != null){
            mRequestQueue.cancelAll(tag);
        }
    }
}
