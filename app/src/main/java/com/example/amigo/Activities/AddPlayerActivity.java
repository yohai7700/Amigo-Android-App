package com.example.amigo.Activities;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.amigo.R;
import com.example.amigo.Utility.PictureLoading;
import com.google.android.material.textfield.TextInputEditText;
import com.mikhaellopez.circularimageview.CircularImageView;

public class AddPlayerActivity extends AppCompatActivity {
    public static final int PICK_IMAGE_REQUEST = 1, MY_PERMISSIONS_READ_EXTERNAL_STORAGE = 2;

    public static final String EXTRA_PLAYER_FIRST_NAME = "com.example.amigo.Activities.AddEditGroupActivity.EXTRA_PLAYER_FIRST_NAME";
    public static final String EXTRA_PLAYER_LAST_NAME = "com.example.amigo.Activities.AddEditGroupActivity.EXTRA_PLAYER_LAST_NAME";
    public static final String EXTRA_PLAYER_PICTURE_URI = "com.example.amigo.Activities.AddEditGroupActivity.EXTRA_PLAYER_PICTURE_URI";

    private TextInputEditText editTextFirstName;
    private TextInputEditText editTextLastName;
    private CircularImageView buttonPicture;
    private Uri pictureUri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_player);

        editTextFirstName = findViewById(R.id.edit_text_player_first_name);
        editTextLastName = findViewById(R.id.edit_text_player_last_name);
        buttonPicture = findViewById(R.id.add_player_picture);

        buttonPicture.setOnClickListener(getPictureOnClickListener());

        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_close);
    }

    private void savePlayer(){
        String fName = editTextFirstName.getText().toString();
        String lName = editTextLastName.getText().toString();

        if(fName.trim().isEmpty()){
            Toast.makeText(getApplicationContext(), "Please insert a first name", Toast.LENGTH_SHORT).show();
            return;
        }
        else if(lName.trim().isEmpty()){
            Toast.makeText(getApplicationContext(), "Please insert a last name.", Toast.LENGTH_SHORT).show();
            return;
        }

        Intent data = new Intent();
        data.putExtra(EXTRA_PLAYER_FIRST_NAME, fName);
        data.putExtra(EXTRA_PLAYER_LAST_NAME, lName);
        data.putExtra(EXTRA_PLAYER_PICTURE_URI, pictureUri);

        setResult(RESULT_OK,data);
        finish();
    }


    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.save_player_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch(item.getItemId()){
            case R.id.save_player_menu:
                savePlayer();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private View.OnClickListener getPictureOnClickListener(){
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PictureLoading.startGalleryForResult(AddPlayerActivity.this);
            }
        };
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode != RESULT_OK)
            return;
        switch(requestCode){
            case PictureLoading.PICK_IMAGE_REQUEST:
                pictureUri = data.getData();
                Bitmap pictureBM = BitmapFactory.decodeFile(PictureLoading.getPicturePath(this, pictureUri));
                buttonPicture.setImageDrawable(new BitmapDrawable(getResources(), pictureBM));
                break;
            default:
                Toast.makeText(getApplicationContext(), "Couldn't get photo", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode){
            case MY_PERMISSIONS_READ_EXTERNAL_STORAGE:
                PictureLoading.tryOpenGallery(AddPlayerActivity.this);
        }
    }
}
