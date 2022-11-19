package com.example.instagramclone;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.parse.FindCallback;
import com.parse.Parse;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.SaveCallback;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class ListPage extends AppCompatActivity {

    ArrayList<String> users;

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 1) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                imageChooser();
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.share_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.share) {
            if (checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 1);
            } else {
                imageChooser();
            }
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_page);

        setTitle("User Feed");

        Button logoutButton = (Button) findViewById(R.id.logout);
        ListView listView = (ListView) findViewById(R.id.userList);

        TextView usernameText = (TextView) findViewById(R.id.usernameText);
        Intent intent = getIntent();
        String username = intent.getStringExtra("username");
        usernameText.setText("Welcome " + username + " !");


        ParseQuery<ParseObject> query = ParseQuery.getQuery("_User");
        query.findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(List<ParseObject> objects, ParseException e) {
                if (e == null && objects.size() > 0) {
                    users = new ArrayList<>();
                    for (ParseObject object : objects) {
                        String user = object.getString("username");
                        if (Objects.equals(user, username)) {
                            continue;
                        }
                        users.add(user);
                    }
                    ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(ListPage.this, android.R.layout.simple_list_item_1, users);
                    listView.setAdapter(arrayAdapter);
                } else {
                    e.printStackTrace();
                }
            }
        });

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Log.i("Test", "Object on list clicked!");
                Intent intent = new Intent(getApplicationContext(), UserFeedActivity.class);
                intent.putExtra("user", users.get(i));
                startActivity(intent);
            }
        });


        logoutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ParseUser.logOut();
                finish();
            }
        });
    }

    private void imageChooser() {
        Intent i = new Intent();
        i.setType("image/*");
        i.setAction(Intent.ACTION_GET_CONTENT);

        launchSomeActivity.launch(i);
    }

    ActivityResultLauncher<Intent> launchSomeActivity = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), result -> {
        if (result.getResultCode() == Activity.RESULT_OK) {
            Intent data = result.getData();
            // do your operation from here....
            if (data != null && data.getData() != null) {
                Uri selectedImageUri = data.getData();
                Bitmap selectedImageBitmap;
                try {
                    selectedImageBitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), selectedImageUri);
                    Log.i("Test", "Image selected");

                    // For Parse Image selection
                    ByteArrayOutputStream stream = new ByteArrayOutputStream();
                    selectedImageBitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
                    byte[] byteArray = stream.toByteArray();
                    ParseFile file = new ParseFile("image.png", byteArray);

                    ParseObject object = new ParseObject("Image");
                    object.put("image", file);
                    object.put("username", ParseUser.getCurrentUser().getUsername());
                    object.saveInBackground(new SaveCallback() {
                        @Override
                        public void done(ParseException e) {
                            if (e == null) {
                                Toast.makeText(ListPage.this, "Image has been shared", Toast.LENGTH_SHORT).show();
                            } else {
                                Toast.makeText(ListPage.this, "Issue uploading image", Toast.LENGTH_SHORT).show();
                                e.printStackTrace();
                            }
                        }
                    });
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    });
}