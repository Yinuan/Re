package com.klcxkj.rs;

import android.graphics.Bitmap;
import android.support.v4.util.LruCache;

import com.android.volley.toolbox.ImageLoader.ImageCache;

public class ImageCacheInstance {
	
	   private ImageCache imageCache;
	
	   private static ImageCacheInstance mInstance;

	    private ImageCacheInstance() {
	    	init();
	    }

	    private void init()
	    {
	        imageCache = new ImageCache() {
	            private final LruCache<String, Bitmap> mCache = new LruCache<String, Bitmap>(10);
	            public void putBitmap(String url, Bitmap bitmap) {
	                mCache.put(url, bitmap);
	            }
	            public Bitmap getBitmap(String url) {
	                return mCache.get(url);
	            }
	        };
	    }
	    public static ImageCacheInstance getInstance() {
	        if (mInstance == null) {
	            mInstance = new ImageCacheInstance();
	        }
	        return mInstance;
	    }

		public ImageCache getImageCache() {
			return imageCache;
		}

		public void setImageCache(ImageCache imageCache) {
			this.imageCache = imageCache;
		}

	
        
}
