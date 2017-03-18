package wad.wan.murahamatdistro.app;

import android.graphics.Bitmap;
import android.support.v4.util.LruCache;

import com.android.volley.toolbox.ImageLoader.ImageCache;

/**
 * Created by user on 10/02/2017.
 */
public class LruBitmapCache extends LruCache<String,Bitmap> implements ImageCache {

    public static int getDefaultLruChaceSize(){
        final int maxMemory = (int) (Runtime.getRuntime().maxMemory()/1024);
        final int cacheSize = maxMemory/8;

        return cacheSize;
    }

    public LruBitmapCache(){
        this(getDefaultLruChaceSize());
    }

    public LruBitmapCache(int sizeInKiloBytes){
        super(sizeInKiloBytes);
    }

    protected int sizeOf(String key, Bitmap value){
        return value.getRowBytes() * value.getHeight()/1024;
    }

    @Override

    public Bitmap getBitmap(String url){
        return get(url);
    }

    public  void putBitmap(String url, Bitmap bitmap){
        put(url, bitmap);

    }

}
