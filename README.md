# Photto
Photto is a Image Upload, Image Caching and Picture Display Library for Android Projects.

- Project by [@serifgungor](https://github.com/serifgungor) and Generated in 26.07.2019. Last revision at 27.07.2019

# Installation
Gradle
```groovy
allprojects {
  repositories {
    ...
    maven { url 'https://jitpack.io' }
  }
}
dependencies {
  implementation 'com.github.gungoronline:Photto:0.2.0'
}
```
or Maven
```xml
<dependency>
   <groupId>com.github.gungoronline</groupId>
   <artifactId>Photto</artifactId>
   <version>0.2.0</version>
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

## UploadBuilder (Version: 0.3.0)
-- Java Code
```groovy
HashMap<String, String> hm = new HashMap<String, String>();
new Photto.UploadBuilder("https://serifgungor.com/test.php",hm,imageView).upload();
```
-- Also Alternative Usage
```groovy
HashMap<String, String> hm = new HashMap<String, String>();
new Photto.UploadBuilder().uploadUrl("https://serifgungor.com/test.php").hashMap(hm).imageView(imageView).upload();
```
-- PHP Code
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

# Change Logs
## 26.07.2019
- Version 0.1.0 has launched. (PhottoBuilder class created.FROM URL, FROM URI, FROM BASE64 STRING, FROM ASSETS FOLDER, FROM DRAWABLE added)
## 27.07.2019
- Version 0.2.0 has launched. (FROM FILE, FROM URL WITH IMAGE LOAD LISTENER added)
## 03.08.2019 (Now Adding)
- Version 0.3.0 has launched. (UploadBuilder class created. Can you upload to website imageView on image)

# Coming Soon (0.3.0)
- We Thinking to add UploadBuilder

Helpers
--------
- Java Thread, AsyncTask classes used.
- Java HttpUrlConnection/HttpsUrlConnection classes used.

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
