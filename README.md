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

# How to use ?

## FROM URL
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

Thank you for the support ! :kissing_heart:
