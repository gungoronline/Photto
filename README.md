![Photto Image Cache](/phottov4.jpg)
# Photto
Photto is a Image Upload, Image Caching, Picture Display, Photo Collage, Circular Image View, Gif View and ImageView Pinch to Zoom Library for Android Projects.

- Project by [@serifgungor](https://github.com/serifgungor) and Generated in 26.07.2019. Last revision at 07.09.2019

# Installation
- [![](https://jitpack.io/v/gungoronline/Photto.svg)](https://jitpack.io/#gungoronline/Photto)
- Gradle
```groovy
allprojects {
  repositories {
    ...
    maven { url 'https://jitpack.io' }
  }
}
dependencies {
  implementation 'com.github.gungoronline:Photto:0.5.0'
}
```
or Maven
```xml
<dependency>
   <groupId>com.github.gungoronline</groupId>
   <artifactId>Photto</artifactId>
   <version>0.5.0</version>
</dependency>
```


# How to use ?

## PhottoBuilder class
### FROM URI
```groovy
new Photto.PhottoBuilder(
  getApplicationContext(),
  Uri.parse("android.resource://"+getPackageName()+"/drawable/ic_launcher_background"),
  (ImageView) findViewById(R.id.imageView)
).build();
```

### FROM URL
- If you want to display an image in the ImageView object on the website, you can use the following method. It downloads the image from the website and caches it from being downloaded continuously.
```groovy
new Photto.PhottoBuilder(
  this,
  "https://images.unsplash.com/photo-1555992643-a97955e6aae6?ixlib=rb-1.2.1&ixid=eyJhcHBfaWQiOjEyMDd9&auto=format&fit=crop&w=401&q=80",
  (ImageView) findViewById(R.id.imageView)
).build();
```

### FROM URL WITH IMAGE LOAD LISTENER (Version: 0.2.0)
```groovy
new Photto.PhottoBuilder(
  getApplicationContext(),
  "https://images.unsplash.com/photo-1564149504817-d1378368526f?ixlib=rb-1.2.1&auto=format&fit=crop&w=1534&q=80",
  new Photto.PhottoBuilder.ImageLoadListener() {
    @Override
    public void onImageLoaded() {
      Log.d("PHOTTO", "Image Loaded");
    }
    @Override
    public void onError(int errorCode) {
      if(errorCode == PhottoError.URL_IS_BLANK){
        Log.d("PHOTTO","URL can not be blank !");
      }else if(errorCode == PhottoError.NETWORK_ERROR){
        Log.d("PHOTTO","No internet connection !");
      }else if(errorCode == PhottoError.NETWORK_ERROR_BUT_CACHE){
        Log.d("PHOTTO","No internet connection, but image getted from cache !");
      }
    }
    @Override
    public void onImageLoading() {
      Log.d("PHOTTO", "Image Loading");
    }
  },
  (ImageView) findViewById(R.id.imageView)
).build();
```
### FROM FILE (Version: 0.2.0)
```groovy
// If you need show image, you must allow Storage permission
new Photto.PhottoBuilder(
  getApplicationContext(),
  new File("/storage/emulated/0/cat.jpeg"),
  (ImageView) findViewById(R.id.imageView)
).build();
```
### FROM DRAWABLE
```groovy
new Photto.PhottoBuilder(
  this,
  R.drawable.ic_launcher_background,
  (ImageView) findViewById(R.id.imageView)
).build();
```

### FROM ASSETS FOLDER
```groovy
try {
  new Photto.PhottoBuilder(
    this,
    getAssets().open("android.jpg"),
    (ImageView) findViewById(R.id.imageView)
  ).build();
} catch (IOException e) {
  e.printStackTrace();
}
```

### FROM BASE64 STRING
```groovy
new Photto.PhottoBuilder(
  this,
  "BASE64_STR",
  null,
  (ImageView) findViewById(R.id.imageView)
).build();
```

## UploadBuilder
![Photto Image Upload](/ftp_upload.jpg)

[Video on YouTube - Photto ImageUpload](https://youtu.be/6BrEVHpUv1s)

The easiest way, upload your images to the web server !

### UPLOAD FROM IMAGEVIEW (Version: 0.3.0)
NOTE: Only accept .jpg file
- max file sizes 1024x1024px (Upload Test Result: 00.07.08sec upload time)
- max file sizes 1200x1200px (Upload Test Result: 00.08.72sec upload time)

-- Java Code
```groovy
HashMap<String, String> hm = new HashMap<String, String>();
new Photto.UploadBuilder("https://siteurl.com/test.php",hm,imageView).upload();
```
-- Also Alternative Usage
```groovy
HashMap<String, String> hm = new HashMap<String, String>();
new Photto.UploadBuilder().uploadUrl("https://siteurl.com/test.php").hashMap(hm).imageView(imageView).upload();
```
-- PHP Code (test.php)
```groovy
<?php
// You must be POST to imgBase64, because imgBase64 is ImageView's converted decoded string !. If you should extra parameters, u should this.
if($_POST){
	$data = $_POST['imgBase64'];
	$data = str_replace('data:image/png;base64,', '', $data);
	$data = str_replace(' ', '+', $data);
	$data = base64_decode($data);
	$file = ''.rand() . '.png';
	$success = file_put_contents($file, $data);
	$data = base64_decode($data); 
	$source_img = imagecreatefromstring($data);
	$rotated_img = imagerotate($source_img, 90, 0); 
	$file = ''. rand(). '.png';
	$imageSave = imagejpeg($rotated_img, $file, 10);
	imagedestroy($source_img);
}
?>
```

-- C#(ASP.net) Not tested yet
```groovy
public Image Base64ToImage(string base64String)
 {
    // Convert base 64 string to byte[]
    byte[] imageBytes = Convert.FromBase64String(base64String);
    // Convert byte[] to Image
    using (var ms = new MemoryStream(imageBytes, 0, imageBytes.Length))
    {
        Image image = Image.FromStream(ms, true);
        return image;
    }
 }
 string value = Request.Form["imgBase64"];
 Image img = Base64ToImage(value);
 img.Save(path);
```

### UPLOAD FROM IMAGEVIEW WITH RESIZED (Version: 0.3.0)
- 600x600 (Upload Test Result: 00.04.68sec upload time)
- 400x400 (Upload Test Result: 00.01.98sec upload time)
```groovy
//If original image size is 1200x1200; imageResize(2) = 600x600 response, imageResize(3) = 300x300 response
HashMap<String, String> hm = new HashMap<String, String>();
new Photto.UploadBuilder().uploadUrl("https://siteurl.com/test.php").imageResize(3).hashMap(hm).imageView(imageView).upload();
```
Also alternative
```groovy
//If you set to imageResize value 0, image do not resizing.
HashMap<String, String> hm = new HashMap<String, String>();
new Photto.UploadBuilder("https://siteurl.com/test.php",hm,0,imageView).upload();
```

## PhotoView (View Class)

![Photto Image Zoom](/imageview_zoom.jpg)

### ZoomTouchView (Added This Version: 0.4.0)
ImageView supported Double-Touch to Zoom and Pinch to Zoom !

```groovy
<com.gungoronline.photto.PhotoView.ZoomTouchView
	android:layout_width="match_parent"
        android:layout_height="300dp"
        app:srcCompat="@mipmap/ic_launcher" />
```

## BitmapBuilder (Class)
[How to use on Kotlin](https://www.youtube.com/shorts/pDr144GdyYw)

### Bitmap Type 1
![Photto Image Upload](/bitmapbuilder.png)
```groovy
File[] files = new File[]{new File("/storage/emulated/0/tr.jpg")};
Bitmap b = new Photto.BitmapBuilder(getApplicationContext(),files,"@serifgungor", BitmapType.TYPE_1).build();
iv.setImageBitmap(b);
```

### Bitmap Type 2
![Photto Image Upload](/bitmapbuilder2.png)
```groovy
File[] files = new File[]{new File("/storage/emulated/0/photo1.jpg"),new File("/storage/emulated/0/photo2.jpg")};
Bitmap b = new Photto.BitmapBuilder(getApplicationContext(),files,"", BitmapType.TYPE_2).build();
iv.setImageBitmap(b);
```

### Bitmap Type 3
![Photto Image Upload](/bitmapbuilder3.png)
```groovy
File[] files = new File[]{new File("/storage/emulated/0/picture1.jpg"),new File("/storage/emulated/0/picture2.jpg")};
Bitmap b = new Photto.BitmapBuilder(getApplicationContext(),files,"", BitmapType.TYPE_3).build();
iv.setImageBitmap(b);
```
### Bitmap Type 4
![Photto Image Upload](/bitmapbuilder4.png)
```groovy
File[] files = new File[]{new File("/storage/emulated/0/picture1.jpg")};
Bitmap b = new Photto.BitmapBuilder(getApplicationContext(),files,"", BitmapType.TYPE_4).build();
iv.setImageBitmap(b);
```
### Bitmap Type 5
![Photto Image Upload](/bitmapbuilder5.png)
```groovy
File[] files = new File[]{
	new File("/storage/emulated/0/picture1.jpg"),
	new File("/storage/emulated/0/picture2.jpg"),
	new File("/storage/emulated/0/picture3.jpg"),
	new File("/storage/emulated/0/picture4.jpg")
};
Bitmap b = new Photto.BitmapBuilder(getApplicationContext(),files,"", BitmapType.TYPE_5).build();
iv.setImageBitmap(b);
```

### Bitmap Type 6
![Photto Image Upload](/bitmapbuilder6.png)
```groovy
File[] files = new File[]{
	new File("/storage/emulated/0/Download/p1.jpg"),
	new File("/storage/emulated/0/Download/p2.jpg"),
	new File("/storage/emulated/0/Download/p3.jpg"),
	new File("/storage/emulated/0/Download/p4.jpg")
};
Bitmap b = new Photto.BitmapBuilder(getApplicationContext(),files,"", BitmapType.TYPE_6).build();
iv.setImageBitmap(b);
```

### Bitmap Type 7
![Photto Image Upload](/bitmapbuilder7.png)
```groovy
File[] files = new File[]{
	new File("/storage/emulated/0/Download/p1.jpg"),
	new File("/storage/emulated/0/Download/p2.jpg"),
	new File("/storage/emulated/0/Download/p3.jpg"),
	new File("/storage/emulated/0/Download/p4.jpg")
};
Bitmap b = new Photto.BitmapBuilder(getApplicationContext(),files,"", BitmapType.TYPE_7).build();
iv.setImageBitmap(b);
```

# Change Logs
## 26.07.2019
- Version 0.1.0 has launched. (PhottoBuilder class created.FROM URL, FROM URI, FROM BASE64 STRING, FROM ASSETS FOLDER, FROM DRAWABLE added)
## 27.07.2019
- Version 0.2.0 has launched. (FROM FILE, FROM URL WITH IMAGE LOAD LISTENER added)
## 04.08.2019
- Version 0.3.0 has launched. (UploadBuilder class created. You can upload the imageView content to the Web site)
## 07.09.2019
- Version 0.4.0 has launched. (ZoomTouchView added. ImageView Supported Pinch Zoom and Double-Touch Zoom)
## 01.08.2020
- Version 0.5.0 has launched.
- The new version comes with the BitmapBuilder class. BitmapBuilder Type1, Type2 and Type3 is OK ! (27.02.2020)
- BitmapBuilder Type4 and Type5 is OK ! (02.03.2020)
# Maybe later... (0.6.0)
- BitmapBuilder Type6 is OK !
- BitmapBuilder Type7 is OK !
- CircleImageView added (28.01.2022)
# Maybe later... (0.7.0)
- You got a new idea? Send a message to: contact@serifgungor.com

Helpers
--------
- Java Thread, AsyncTask classes used.
- Java HttpUrlConnection/HttpsUrlConnection classes used.
- chrisbanes / PhotoView Library
- Mikhael LOPEZ / CircularImageView class used.

License
--------

Copyright 2019 GUNGORONLINE.

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.

Thank you for the support ! :kissing_heart:
