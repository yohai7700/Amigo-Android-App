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

import com.example.amigo.Handler.PermissionsHandler;
import com.example.amigo.Handler.PictureHandler;
import com.example.amigo.Handler.RequestHandler;
import com.example.amigo.R;
import com.example.amigo.StatsViewModel.StatsRepository.Entity.Group;
import com.example.amigo.StatsViewModel.ViewModel.GroupViewModel;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textview.MaterialTextView;

import java.text.DateFormat;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import de.hdodenhof.circleimageview.CircleImageView;

public class AddEditGroupActivity extends AppCompatActivity {

    public static final String EXTRA_GROUP_ID = "com.example.amigo.Activities.AddEditGroupActivity.EXTRA_GROUP_ID";

    private TextInputEditText editTextTitle;
    private TextInputEditText editTextDescription;
    private MaterialTextView textViewCreationDate;
    private MaterialTextView textViewCreatedOn;
    private CircleImageView pickPhotoButton;
    private GroupViewModel groupViewModel;
    private Uri iconUri;

    //TODO: add regions


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_group);

        groupViewModel = new ViewModelProvider(this).get(GroupViewModel.class);
        editTextTitle = findViewById(R.id.group_title);
        editTextDescription = findViewById(R.id.edit_text_group_description);
        textViewCreationDate = findViewById(R.id.text_view_creation_date_value);
        textViewCreatedOn = findViewById(R.id.text_view_creation_date_msg);
        textViewCreatedOn.setText("");
        pickPhotoButton = findViewById(R.id.add_a_icon_button);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_close);

        pickPhotoButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PictureHandler.openGalleryPermission(AddEditGroupActivity.this);
            }
        });

        Intent intent = getIntent();
        if (intent.hasExtra(EXTRA_GROUP_ID)) {
            setTitle("Edit Group");
            groupViewModel.getGroup(intent.getIntExtra(EXTRA_GROUP_ID, -1)).observe(this, new Observer<Group>() {
                @Override
                public void onChanged(Group group) {
                    editTextTitle.setText(group.getTitle());
                    editTextDescription.setText(group.getDescription());
                    textViewCreationDate.setText(DateFormat.getDateInstance().format(group.getCreationDate()));
                    pickPhotoButton.setImageDrawable(new BitmapDrawable(getResources(), group.getIcon()));
                }
            });
            textViewCreatedOn.setText(R.string.created_on);
        } else
            setTitle("Add Group");
    }

    private void saveGroup() {
        String title = editTextTitle.getText().toString();
        String description = editTextDescription.getText().toString().trim();

        if (title.trim().isEmpty() && description.isEmpty()) {
            Toast.makeText(getApplicationContext(), "Please insert a title and a description", Toast.LENGTH_SHORT).show();
            return;
        }
        if (title.trim().isEmpty()) {
            Toast.makeText(getApplicationContext(), "Please insert a title", Toast.LENGTH_SHORT).show();
            return;
        }
        if (description.trim().isEmpty()) {
            Toast.makeText(getApplicationContext(), "Please insert a description.", Toast.LENGTH_SHORT).show();
            return;
        }

        int id = getIntent().getIntExtra(EXTRA_GROUP_ID, -1); //only passing id if it's edit so that it will update and not add

        Bitmap bitmap = iconUri != null ? BitmapFactory.decodeFile(PictureHandler.getPicturePath(this, iconUri)) : null;
        if (id != -1) {
            Group group = new Group(title, description, bitmap);
            group.id = id;
            groupViewModel.update(group);
        } else
            groupViewModel.insert(new Group(title, description, bitmap));

        finish();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.save_group_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.save_group_menu:
                saveGroup();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case RequestHandler.PICK_IMAGE_REQUEST:
                    if(data.getData() != null) {
                        iconUri = data.getData();
                        Bitmap pictureBM = BitmapFactory.decodeFile(PictureHandler.getPicturePath(this, iconUri));
                        pickPhotoButton.setImageDrawable(new BitmapDrawable(getResources(), pictureBM));
                        break;
                    }
                default:
                    Toast.makeText(getApplicationContext(), "Couldn't get photo", Toast.LENGTH_SHORT).show();
            }
        }
    }

    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode){
            case PermissionsHandler.MY_PERMISSIONS_READ_EXTERNAL_STORAGE:
                PictureHandler.tryOpenGallery(AddEditGroupActivity.this);
        }
    }

}
