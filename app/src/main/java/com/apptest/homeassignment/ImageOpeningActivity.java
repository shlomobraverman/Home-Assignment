package com.apptest.homeassignment;

import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DecodeFormat;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.Target;

import static com.apptest.homeassignment.MainActivity.*;

import java.util.HashMap;

public class ImageOpeningActivity extends AppCompatActivity {

        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.imageopening);
            ImageView image = (ImageView) findViewById(R.id.image_fill);
            TextView description = (TextView) findViewById(R.id.description);
            ImageView image_profile = (ImageView) findViewById(R.id.image_profile);
            TextView name_user = (TextView) findViewById(R.id.name_user);
            TextView name = (TextView) findViewById(R.id.name);
            TextView bio = (TextView) findViewById(R.id.bio);

            if(getIntent()!=null){
                int position  = getIntent().getExtras().getInt("MY_IMAGE");
                HashMap data = images.get(position);
                Glide.with(this)
                        .load(data.get("url"))
                        .override(Target.SIZE_ORIGINAL)
                        .diskCacheStrategy(DiskCacheStrategy.ALL)
                        .centerCrop()
                        .into(image);

                Glide.with(this)
                        .load(data.get("profile_image"))
                        .format(DecodeFormat.PREFER_ARGB_8888)
                        .diskCacheStrategy(DiskCacheStrategy.ALL)
                        .override(Target.SIZE_ORIGINAL)
                        .centerCrop()
                        .into(image_profile);

                description.setText((String) data.get("description"));
                name_user.setText((String) data.get("username"));
                name.setText((String) data.get("name"));
                bio.setText((String) data.get("bio"));
            }

        }
}
