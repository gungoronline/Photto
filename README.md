# Photto
Photto is a Image Caching and ImageShow Library for Android Projects.

- Project by [@serifgungor](https://github.com/serifgungor) and Generated in 26.07.2019.

# Installation

> allprojects {
>  repositories {
>			...
>			**maven { url 'https://jitpack.io' }**
>  }
> }

> dependencies {
>  **implementation 'com.github.gungoronline:Photto:0.1.0'**
> }

or Maven

> <dependency>
>    <groupId>com.github.gungoronline</groupId>
>    <artifactId>Photto</artifactId>
>    <version>0.1.0</version>
> </dependency>



# How to use ?

## FROM URL
- If you want to display an image in the ImageView object on the website, you can use the following method. It downloads the image from the website and caches it from being downloaded continuously.
> new Photto.PhottoBuilder(
>  this,
>  "https://images.unsplash.com/photo-1555992643-a97955e6aae6?ixlib=rb-1.2.1&ixid=eyJhcHBfaWQiOjEyMDd9&auto=format&fit=crop&w=401&q=80",
>  (ImageView) findViewById(R.id.imageView)
> ).build();

## FROM URI
> new Photto.PhottoBuilder(
>   this,
>   Uri.parse("android.resource://"+getPackageName()+"/drawable/gungoronline_logo"),
>   (ImageView) findViewById(R.id.imageView)
> ).build();

## FROM DRAWABLE
> new Photto.PhottoBuilder(
>   this,
>   R.drawable.ic_launcher_background,
>   (ImageView) findViewById(R.id.imageView)
> ).build();

## FROM ASSETS FOLDER
> try {
>   new Photto.PhottoBuilder(
>     this,
>     getAssets().open("android.jpg"),
>     (ImageView) findViewById(R.id.imageView)
>   ).build();
> } catch (IOException e) {
>   e.printStackTrace();
> }

## FROM BASE64 STRING
> new Photto.PhottoBuilder(
>   this,
>   "BASE64_STR",
>   null,
>   (ImageView) findViewById(R.id.imageView)
> ).build();

# Coming Soon (0.2.0)
- FROM FILE
- We Thinking to add UploadBuilder

# License

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
