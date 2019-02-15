package com.project.nyamka.testcloudinary;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.cloudinary.Cloudinary;
import com.cloudinary.Transformation;
import com.cloudinary.android.MediaManager;
import com.squareup.picasso.Picasso;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

public class MainActivity extends AppCompatActivity {
    public static final int PICK_IMAGE_REQUEST = 1;
    Uri mImageUri = null;
    Map config = new HashMap();
    ImageView mainImage;
    Button submit;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //bind to xml
        mainImage = (ImageView) findViewById(R.id.image);
        submit = (Button) findViewById(R.id.submit);

        //config of cloudinary
        config.put("cloud_name","#");
        config.put("api_key", "#");
        config.put("api_secret", "#");
        MediaManager.init(this, config);

        mainImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent go = new Intent();
                go.setType("image/*");
                go.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(go, PICK_IMAGE_REQUEST);
            }
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == Activity.RESULT_OK) {
            mImageUri = data.getData();
            // SDK < API11
            //realPath = RealPathUtil.getRealPath(getApplicationContext(), data.getData());
            String fileName = generateFileName();
            Picasso.get().load(mImageUri).into(mainImage);
            String url = MediaManager.get().upload(mImageUri).option("folder", "files/").option("public_id", fileName).dispatch(this);
            String mainLink = MediaManager.get().url().transformation(new Transformation().width(50).height(50).crop("fill")).generate("files/"+fileName+".jpg");

            Log.i("ALDAA", mainLink);
            Log.i("ALDAA", mImageUri.getPath());
        }
    }

    private String generateFileName() {
        final String ALLOWED_CHARS = "1qwe2rt3yu4io5pa6sd7fg8hj9klz0xcvbnm_$";
        final Random random = new Random();
        final StringBuilder sb = new StringBuilder(20);
        for(int i = 0; i < 20; i++) {
            sb.append(random.nextInt(ALLOWED_CHARS.length()));
        }
        return sb.toString();
    }
}
