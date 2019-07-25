# Photto


- FROM URL
new Photto.PhottoBuilder(
                this,
                "https://images.unsplash.com/photo-1555992643-a97955e6aae6?ixlib=rb-1.2.1&ixid=eyJhcHBfaWQiOjEyMDd9&auto=format&fit=crop&w=401&q=80",
                (ImageView) findViewById(R.id.imageView)
).build();

- FROM URI
new Photto.PhottoBuilder(
                this,
                Uri.parse("android.resource://"+getPackageName()+"/drawable/gungoronline_logo"),
                (ImageView) findViewById(R.id.imageView)
).build();

- FROM DRAWABLE
new Photto.PhottoBuilder(
                this,
                R.drawable.ic_launcher_background,
                (ImageView) findViewById(R.id.imageView)
).build();

- FROM ASSETS FOLDER
try {
        new Photto.PhottoBuilder(
                this,
                getAssets().open("android.jpg"),
                (ImageView) findViewById(R.id.imageView)
        ).build();
} catch (IOException e) {
            e.printStackTrace();
}

- FROM BASE64 STRING
new Photto.PhottoBuilder(
        this,
        "BASE64_STR",
        null,
        (ImageView) findViewById(R.id.imageView)
).build();