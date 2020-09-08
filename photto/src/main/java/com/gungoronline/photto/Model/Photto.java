package com.gungoronline.photto.Model;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.media.ThumbnailUtils;
import android.net.ConnectivityManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.util.Base64;
import android.util.Log;
import android.widget.ImageView;

import androidx.annotation.Nullable;

import com.gungoronline.photto.Helper.HttpUtility;
import com.gungoronline.photto.Helper.ImageLibrary;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.InputStream;
import java.util.HashMap;

public class Photto {
    private Context context;
    private String url;
    private String uniqueName;
    private ImageView into;
    private Uri uri;
    private int drawableResourceId;
    private InputStream inputStream;
    private byte[] decodedString;
    private File file;

    private String uploadDecodedString;
    private String uploadUrl;
    private HashMap<String,String> uploadParams;
    private ImageView uploadImageView;
    private int imageResize;


    private Bitmap bitmap;

  public Photto(BitmapBuilder bitmapBuilder){
      this.bitmap = bitmap;
      this.context = bitmapBuilder.context;
  }

    public Photto(UploadBuilder uploadBuilder){
        this.uploadUrl = uploadBuilder.uploadUrl;
        this.uploadImageView = uploadBuilder.uploadImageView;
        this.uploadDecodedString = uploadBuilder.uploadDecodedString;
        this.uploadParams = uploadBuilder.uploadParams;
        this.imageResize = uploadBuilder.imageResize;
    }

    public Photto(PhottoBuilder phottoBuilder) {
        this.context = phottoBuilder.context;
        this.url = phottoBuilder.url;
        this.uniqueName = phottoBuilder.uniqueName;
        this.into = phottoBuilder.into;
        this.uri = phottoBuilder.uri;
        this.drawableResourceId = phottoBuilder.drawableResourceId;
        this.inputStream = phottoBuilder.inputStream;
        this.decodedString = phottoBuilder.decodedString;
        this.file = phottoBuilder.file;
    }

    public File getFile() {
        return file;
    }

    public Uri getUri() {
        return uri;
    }

    public int getDrawableResourceId() {
        return drawableResourceId;
    }

    public byte[] getDecodedString() {
        return decodedString;
    }

    public Context getContext() {
        return context;
    }

    public String getUrl() {
        return url;
    }

    public String getUniqueName() {
        return uniqueName;
    }

    public InputStream getInputStream() {
        return inputStream;
    }

    public static class UploadBuilder{
        private ImageView uploadImageView;
        private HashMap<String,String> uploadParams;
        private String uploadUrl;
        private String uploadDecodedString;
        private int imageResize;

        Photto photto;
        private boolean networkConnection() {
            ConnectivityManager conMgr = (ConnectivityManager) uploadImageView.getContext().getSystemService(Context.CONNECTIVITY_SERVICE);
            if (conMgr.getActiveNetworkInfo() != null && conMgr.getActiveNetworkInfo().isAvailable() && conMgr.getActiveNetworkInfo().isConnected()) {
                return true;
            } else {
                return false;
            }
        }

        public UploadBuilder(){
        }
        public UploadBuilder(String uploadUrl,HashMap<String,String> hashMap,ImageView imageView){
            this.uploadImageView = imageView;
            this.uploadUrl = uploadUrl;
            this.uploadParams = hashMap;
        }
        public UploadBuilder(String uploadUrl,HashMap<String,String> hashMap,int imageResize,ImageView imageView){
            this.uploadImageView = imageView;
            this.uploadUrl = uploadUrl;
            this.uploadParams = hashMap;
            this.imageResize = imageResize;
        }



        public UploadBuilder hashMap(HashMap<String,String> hashMap) {
            this.uploadParams = hashMap;
            return this;
        }
        public UploadBuilder uploadUrl(String uploadUrl) {
            this.uploadUrl = uploadUrl;
            return this;
        }
        public UploadBuilder imageView(ImageView imageView) {
            this.uploadImageView = imageView;
            return this;
        }
        public UploadBuilder imageResize(int imageResize){
            this.imageResize = imageResize;
            return this;
        }


        private Bitmap getResizedBitmap(Bitmap bitmapOrg,int resize) {
            Paint paint = new Paint();
            paint.setFilterBitmap(true);
            int targetWidth  = bitmapOrg.getWidth() / resize;
            int targetHeight = bitmapOrg.getHeight() / resize;
            Bitmap bmp = Bitmap.createBitmap(targetWidth, targetHeight,Bitmap.Config.ARGB_8888);
            RectF rectf = new RectF(0, 0, targetWidth, targetHeight);
            Canvas c = new Canvas(bmp);
            Path path = new Path();
            path.addRect(rectf, Path.Direction.CW);
            c.clipPath(path);
            c.drawBitmap( bitmapOrg, new Rect(0, 0, bitmapOrg.getWidth(), bitmapOrg.getHeight()),
                    new Rect(0, 0, targetWidth, targetHeight), paint);
            Matrix matrix = new Matrix();
            matrix.postScale(1f, 1f);
            Bitmap resizedBitmap = Bitmap.createBitmap(bmp, 0, 0, targetWidth, targetHeight, matrix, true);
            return resizedBitmap;
        }


        public Photto upload() {
            photto = new Photto(this);



            Bitmap bitmap = ((BitmapDrawable) this.uploadImageView.getDrawable()).getBitmap();


            if(imageResize!=0){
                bitmap = getResizedBitmap(bitmap,imageResize);
            }

            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
            byte[] imageInByte = baos.toByteArray();

            // send params with Hash Map
            HashMap<String, String> params = this.uploadParams;
            params.put("imgBase64",Base64.encodeToString(imageInByte, 0));
            String url = this.uploadUrl;

            // static class "HttpUtility" with static method "newRequest(url,method,callback)"
            HttpUtility.newRequest(url,HttpUtility.METHOD_POST,params, new HttpUtility.Callback() {
                @Override
                public void OnSuccess(String response) {
                    // on success
                    Log.d("UploadBuilder","Server OnSuccess response="+response);
                }
                @Override
                public void OnError(int status_code, String message) {
                    // on error
                    Log.d("UploadBuilder","Server OnError status_code="+status_code+" message="+message);
                }
            });


            return photto;
        }

    }

    public static class BitmapBuilder{
        private Context context;
        Photto photto;
        ImageLibrary il;
        private int type;
        private String message;
        private File[] files;

        public Bitmap storiesCanvas_type1(String text,Bitmap image){
            Bitmap bm1 = null;
            Bitmap newBitmap = null;

            //FOR FULL QUALITY
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            image.compress(Bitmap.CompressFormat.PNG, 100, out);
            BitmapFactory.Options options = new BitmapFactory.Options();
            options.inJustDecodeBounds = false;
            Bitmap decoded = BitmapFactory.decodeStream(new ByteArrayInputStream(out.toByteArray()),null,options);


            bm1 = Bitmap.createScaledBitmap(decoded, 740, 1600, false);
            Bitmap.Config config = bm1.getConfig();
            if (config == null) {
                config = Bitmap.Config.ARGB_8888;
            }


            newBitmap = Bitmap.createBitmap(1080, 1920, config);
            Canvas newCanvas = new Canvas(newBitmap);


            Paint paintText = new Paint();
            paintText.setColor(Color.BLACK);
            paintText.setTextSize(80);
            paintText.setTextAlign(Paint.Align.RIGHT);
            paintText.setStyle(Paint.Style.FILL);

            Rect rectText = new Rect();
            paintText.getTextBounds(text, 0, text.length(), rectText);
            newCanvas.translate(800, 50);

            newCanvas.drawColor(Color.WHITE);
            newCanvas.rotate(90);
            newCanvas.drawText(text,0,text.length(),1000, -110, paintText);

            newCanvas.save();
            newCanvas.rotate(-90);
            newCanvas.translate(-800,50);
            newCanvas.drawBitmap(bm1, 50, 50, null);
            return newBitmap;
        }

        public Bitmap storiesCanvas_type2(Bitmap image1,Bitmap image2){

            Bitmap bm1 = null;
            Bitmap bm2 = null;
            Bitmap newBitmap = null;

            bm1 = Bitmap.createScaledBitmap(image1, 904, 764, false);
            bm2 = Bitmap.createScaledBitmap(image2, 904, 764, false);


            Bitmap.Config config = bm1.getConfig();
            if (config == null) {
                config = Bitmap.Config.ARGB_8888;
            }

            newBitmap = Bitmap.createBitmap(1080, 1920, config);
            Canvas newCanvas = new Canvas(newBitmap);

            newCanvas.drawColor(Color.WHITE);
            newCanvas.rotate(90);

            newCanvas.save();
            newCanvas.rotate(-90);
            newCanvas.translate(40,50);
            newCanvas.drawBitmap(bm1, 50, 100, null);
            newCanvas.drawBitmap(bm2, 50, 950, null);
            return newBitmap;
        }
        public Bitmap storiesCanvas_type3(Bitmap image1,Bitmap image2){

            Bitmap bm1 = null;
            Bitmap bm2 = null;
            Bitmap newBitmap = null;

            bm1 = Bitmap.createScaledBitmap(image1, 904, 992, false);
            bm2 = Bitmap.createScaledBitmap(image2, 904, 592, false);


            Bitmap.Config config = bm1.getConfig();
            if (config == null) {
                config = Bitmap.Config.ARGB_8888;
            }

            newBitmap = Bitmap.createBitmap(1080, 1920, config);
            Canvas newCanvas = new Canvas(newBitmap);

            newCanvas.drawColor(Color.WHITE);
            newCanvas.rotate(90);

            newCanvas.save();
            newCanvas.rotate(-90);
            newCanvas.translate(40,50);
            newCanvas.drawBitmap(bm1, 50, 100, null);
            newCanvas.drawBitmap(bm2, 50, 1150, null);
            return newBitmap;
        }
        public Bitmap storiesCanvas_type4(Bitmap image1){

            Bitmap bm1 = null;
            Bitmap newBitmap = null;

            bm1 = Bitmap.createScaledBitmap(image1, 1080, 900, false);


            Bitmap.Config config = bm1.getConfig();
            if (config == null) {
                config = Bitmap.Config.ARGB_8888;
            }

            newBitmap = Bitmap.createBitmap(1080, 1920, config);
            Canvas newCanvas = new Canvas(newBitmap);

            newCanvas.drawColor(Color.WHITE);
            newCanvas.rotate(90);

            newCanvas.save();
            newCanvas.rotate(-90);
            newCanvas.translate(0,50);
            newCanvas.drawBitmap(bm1, 0, 444, null);
            return newBitmap;
        }

        public Bitmap storiesCanvas_type5(Bitmap image1,Bitmap image2,Bitmap image3,Bitmap image4){

            Bitmap bm1 = null;
            Bitmap bm2 = null;
            Bitmap bm3 = null;
            Bitmap bm4 = null;
            Bitmap newBitmap = null;

            bm1 = Bitmap.createScaledBitmap(image1, 520, 490, false);
            bm2 = Bitmap.createScaledBitmap(image2, 520, 490, false);
            bm3 = Bitmap.createScaledBitmap(image3, 520, 490, false);
            bm4 = Bitmap.createScaledBitmap(image4, 520, 490, false);


            Bitmap.Config config = bm1.getConfig();
            if (config == null) {
                config = Bitmap.Config.ARGB_8888;
            }

            newBitmap = Bitmap.createBitmap(1080, 1920, config);
            Canvas newCanvas = new Canvas(newBitmap);

            newCanvas.drawColor(Color.WHITE);
            newCanvas.rotate(90);

            newCanvas.save();
            newCanvas.rotate(-90);
            newCanvas.translate(0,50);
            newCanvas.drawBitmap(bm1, 0, 344, null);
            newCanvas.drawBitmap(bm2, 600, 344, null);
            newCanvas.drawBitmap(bm3, 0, 944, null);
            newCanvas.drawBitmap(bm4, 600, 944, null);
            return newBitmap;
        }
        public BitmapBuilder(Context context, File[] files,String message,int bitmapType) {
            this.context = context;
            this.files = files;
            il = new ImageLibrary();
            this.type = bitmapType;
            this.message = message;
        }

        public BitmapBuilder context(Context context) {
            this.context = context;
            il = new ImageLibrary();
            return this;
        }
        public Bitmap build() {
            photto = new Photto(this);

            Bitmap b=null;
            if(this.type == BitmapType.TYPE_1){
                b = storiesCanvas_type1(message,il.loadImageFromFile(files[0]));
            }else if(this.type == BitmapType.TYPE_2){
                b = storiesCanvas_type2(il.loadImageFromFile(files[0]),il.loadImageFromFile(files[1]));
            }else if(this.type == BitmapType.TYPE_3){
                b = storiesCanvas_type3(il.loadImageFromFile(files[0]),il.loadImageFromFile(files[1]));
            }else if(this.type == BitmapType.TYPE_4){
                b = storiesCanvas_type4(il.loadImageFromFile(files[0]));
            }else if(this.type == BitmapType.TYPE_5){
                b = storiesCanvas_type5(il.loadImageFromFile(files[0]),il.loadImageFromFile(files[1]),il.loadImageFromFile(files[2]),il.loadImageFromFile(files[3]));
            }


            return b;
        }
    }

    public static class PhottoBuilder {
        private Context context;
        private String url;
        private String uniqueName;
        private ImageView into;
        private Uri uri;
        private int drawableResourceId;
        private InputStream inputStream;
        private byte[] decodedString;
        private File file;

        Photto photto;
        ImageLibrary il;

        public interface ImageLoadListener {
            public void onImageLoaded();

            public void onError(int errorCode);

            public void onImageLoading();
        }

        private ImageLoadListener imageLoadListener;

        public boolean networkConnection() {
            ConnectivityManager conMgr = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
            if (conMgr.getActiveNetworkInfo() != null && conMgr.getActiveNetworkInfo().isAvailable() && conMgr.getActiveNetworkInfo().isConnected()) {
                return true;
            } else {
                return false;
            }
        }


        private String reverseString(String str) {
            String reverse = "";
            for (int i = str.length() - 1; i >= 0; i--) {
                reverse = reverse + str.charAt(i);
            }
            return reverse;
        }

        public PhottoBuilder(Context context, String url, ImageView into) {
            this.context = context;
            this.url = url;
            if (!url.isEmpty()) {
                String str = reverseString(url.replaceAll("[^a-zA-Z0-9]", ""));
                this.uniqueName = str.substring(60);
            } else {
                imageLoadListener.onError(2);
            }
            this.into = into;
        }

        public PhottoBuilder(Context context, String url, ImageLoadListener imageLoadListener, ImageView into) {
            this.context = context;
            this.url = url;
            if (!url.isEmpty()) {
                String str = reverseString(url.replaceAll("[^a-zA-Z0-9]", ""));
                this.uniqueName = str.substring(60);
            } else {
                imageLoadListener.onError(2);
            }
            this.into = into;
            this.imageLoadListener = imageLoadListener;
        }


        public PhottoBuilder(Context context, Uri uri, ImageView into) {
            this.context = context;
            this.uri = uri;
            this.into = into;
        }

        public PhottoBuilder(Context context, File file, ImageView into) {
            this.context = context;
            this.file = file;
            this.into = into;
        }

        public PhottoBuilder(Context context, int drawableResourceId, ImageView into) {
            this.context = context;
            this.drawableResourceId = drawableResourceId;
            this.into = into;
        }

        public PhottoBuilder(Context context, InputStream inputStream, ImageView into) {
            this.context = context;
            this.inputStream = inputStream;
            this.into = into;
        }

        public PhottoBuilder(Context context, String base64Str, @Nullable byte[] decodedString, ImageView into) {
            this.decodedString = Base64.decode(base64Str, Base64.DEFAULT);
            this.context = context;
            this.inputStream = inputStream;
            this.into = into;
        }


        public PhottoBuilder context(Context context) {
            this.context = context;
            return this;
        }

        public PhottoBuilder url(String url) {
            this.url = url;
            return this;
        }

        public PhottoBuilder uri(Uri uri) {
            this.uri = uri;
            return this;
        }

        public PhottoBuilder decodedString(byte[] array) {
            this.decodedString = array;
            return this;
        }

        public PhottoBuilder inputStream(InputStream inputStream) {
            this.inputStream = inputStream;
            return this;
        }

        public PhottoBuilder drawableResourceId(int drawableResourceId) {
            this.drawableResourceId = drawableResourceId;
            return this;
        }


        public PhottoBuilder into(ImageView into) {
            this.into = into;
            return this;
        }

        public Photto build() {
            photto = new Photto(this);
            il = new ImageLibrary();

            new AsyncTask<Void, Void, Void>() {
                @Override
                protected void onPreExecute() {
                    super.onPreExecute();
                }

                @Override
                protected void onPostExecute(Void aVoid) {
                    super.onPostExecute(aVoid);
                    if (photto.getUri() != null) {
                        into.setImageURI(photto.uri);
                    } else if (photto.getUrl() != null) {
                        if (!photto.getUrl().isEmpty()) {
                            if (networkConnection()) {
                                if (imageLoadListener != null) {
                                    into.setImageBitmap(il.loadImageFromStorage("/data/data/" + context.getPackageName() + "/app_imageDir", uniqueName + ".jpg"));
                                    imageLoadListener.onImageLoaded();
                                }
                            } else {
                                Bitmap bp = il.loadImageFromStorage("/data/data/" + context.getPackageName() + "/app_imageDir", uniqueName + ".jpg");
                                if (bp != null) {
                                    into.setImageBitmap(bp);
                                }
                            }
                        }

                    } else if (photto.getDrawableResourceId() != 0) {
                        into.setImageResource(photto.drawableResourceId);
                    } else if (photto.getInputStream() != null) {
                        into.setImageDrawable(Drawable.createFromStream(inputStream, null));
                    } else if (photto.getDecodedString() != null) {
                        Bitmap bitmap = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
                        into.setImageBitmap(bitmap);
                    } else if (photto.getFile() != null) {
                        into.setImageBitmap(BitmapFactory.decodeFile(photto.getFile().getAbsolutePath()));
                    }

                }

                @Override
                protected Void doInBackground(Void... voids) {
                    if (photto.getUrl() != null) {

                        if (!photto.getUrl().isEmpty()) {
                            if (networkConnection()) {
                                if (il.isImageInStorage("/data/data/" + context.getPackageName() + "/app_imageDir", uniqueName + ".jpg") == false) {
                                    Bitmap bitmap = il.getBitmapFromURL(url);
                                    il.saveImageToInternalStorage(bitmap, uniqueName + ".jpg", context.getApplicationContext());
                                }
                                if (imageLoadListener != null) {
                                    imageLoadListener.onImageLoading();
                                }
                            } else {
                                Bitmap bp = il.loadImageFromStorage("/data/data/" + context.getPackageName() + "/app_imageDir", uniqueName + ".jpg");
                                if (bp != null) {
                                    imageLoadListener.onError(3);
                                } else {
                                    imageLoadListener.onError(1);
                                }
                            }
                        }
                    }
                    return null;
                }
            }.execute();


            return photto;
        }
    }

}