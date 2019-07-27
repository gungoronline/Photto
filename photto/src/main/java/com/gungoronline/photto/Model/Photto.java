package com.gungoronline.photto.Model;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.net.ConnectivityManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.util.Base64;
import android.util.Log;
import android.widget.ImageView;

import androidx.annotation.Nullable;

import com.gungoronline.photto.Helper.ImageLibrary;

import java.io.File;
import java.io.InputStream;

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

    @Override
    public String toString() {
        return "Photto{" +
                "context=" + context +
                ", url='" + url + '\'' +
                ", uniqueName='" + uniqueName + '\'' +
                '}';
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