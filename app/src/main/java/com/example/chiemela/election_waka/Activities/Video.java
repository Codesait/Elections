package com.example.chiemela.election_waka.Activities;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;

import com.example.chiemela.election_waka.Model.VideoModel;
import com.example.chiemela.election_waka.R;
import com.example.chiemela.election_waka.Utility.Util;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.valdesekamdem.library.mdtoast.MDToast;

import java.io.ByteArrayOutputStream;

public class Video extends AppCompatActivity {
    private ImageView reportElectionVideo;
    private VideoView videoView;
    private ImageButton play,pause;
    private TextView Coordinates;
    private TextView reporterName;
    private static final int REQUEST_VIDEO_CAPTURE = 1;
    private int CAMERA_PERMISSION_CODE = 24;
    private String firstname, lastname;
    private double latitude, longitude;
    private AppCompatActivity activity = Video.this;
    private Util util = new Util();
    private Button help;
    private Spinner spTreeType;
    private DatabaseReference addTreeRef;
    private FirebaseAuth mAuth;
    private FirebaseUser user;
    private String uid;
    private Uri videoUri;
    private ProgressDialog dialog;
    private StorageReference treeImageRef;
    private DatabaseReference subtree;
    private String typeOfElection;
    private String id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video);

        mAuth=FirebaseAuth.getInstance();
        addTreeRef = FirebaseDatabase.getInstance().getReference().child("Deforestation");
        treeImageRef = FirebaseStorage.getInstance().getReference();

        subtree=FirebaseDatabase.getInstance().getReference();
        //get current user
        user = FirebaseAuth.getInstance().getCurrentUser();
        uid = user.getUid();

        reportElectionVideo = findViewById(R.id.iv_add_tree1);
        videoView = findViewById(R.id.iv_addtreeImages);
        Coordinates = findViewById(R.id.tv_tree_coordinates1);
        reporterName = findViewById(R.id.tv_person_name1);
        help = findViewById(R.id.submit_tree1);
        spTreeType = findViewById(R.id.spTreeType1);
        play = findViewById(R.id.ibtplay);
        pause = findViewById(R.id.imbpause);
        dialog= new ProgressDialog(this);


        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{
                    Manifest.permission.CAMERA
            }, CAMERA_PERMISSION_CODE);

        }

        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();

        if (bundle != null) {
            firstname = bundle.getString("firstname");
            lastname = bundle.getString("lastname");
            latitude = bundle.getDouble("lat");
            longitude = bundle.getDouble("long");

        }

        play.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                videoView.start();
            }
        });

        pause.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                videoView.pause();

                onPause();
            }
        });


        reportElectionVideo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (util.isNetworkAvailable(activity)) {


                    typeOfElection = spTreeType.getItemAtPosition(spTreeType.getSelectedItemPosition()).toString();

                      if (typeOfElection.equalsIgnoreCase("Select the tree type")){
                        MDToast.makeText(getApplication(),"Pls Select a valid tree type",
                                MDToast.LENGTH_LONG, MDToast.TYPE_ERROR).show();
                    } else {
                        Intent captureVideo = new Intent(MediaStore.ACTION_VIDEO_CAPTURE);
                        if (captureVideo.resolveActivity(getPackageManager()) != null) {
                            startActivityForResult(captureVideo, REQUEST_VIDEO_CAPTURE);
                        }

                    }


                } else {
                    util.toastMessage(activity, "Check your Network");
                }


            }
        });


        Coordinates.setText(latitude + ", " + longitude);
        reporterName.setText(lastname + " " + firstname);

        //  final int Trees = Integer.parseInt(noOfElection);

        help.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                AlertDialog.Builder builder = new AlertDialog.Builder(Video.this);
            builder.setMessage("Please check out our new update")
                   .setTitle("Oops!")
                  .setIcon(R.drawable.ic_update)
                    .setPositiveButton(android.R.string.ok, null);
            AlertDialog dialog = builder.create();
           dialog.show();
            }
        });
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == CAMERA_PERMISSION_CODE) {

            // If permission is granted
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                // Displaying a toast
                Toast.makeText(this, "Permission granted", Toast.LENGTH_SHORT).show();

            } else {
                util.toastMessage(activity, "Oops you just denied the permission");
            }
        }
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_VIDEO_CAPTURE && resultCode == RESULT_OK) {
            videoUri = data.getData();
            videoView.setVideoURI(videoUri);
            uploadVideo();
            videoView.getDuration();

        }

    }

    public void uploadVideo(){
        dialog.setMessage("Reporting ...");
        dialog.show();
        StorageReference mountainsRef = treeImageRef.child("video").child(uid).child("video.3gp");
        if (videoView !=null) {

//            videoView.setDrawingCacheEnabled(true);
//            videoView.buildDrawingCache();
//            Bitmap bitmap = videoView.getDrawingCache();
//            ByteArrayOutputStream baos = new ByteArrayOutputStream();
//            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
//            byte[] data = baos.toByteArray();

            UploadTask uploadTask = mountainsRef.putFile(videoUri);
            uploadTask.addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    Uri downloadURI = taskSnapshot.getDownloadUrl();
                    id = addTreeRef.push().getKey();
                    VideoModel model = new VideoModel(lastname + " " + firstname,
                            latitude + ", " + longitude, typeOfElection);
                    addTreeRef.child(id).setValue(model);
                    dialog.dismiss();
                    addTreeRef.child(id).child("electionVideo").setValue(downloadURI.toString());
                    MDToast.makeText(getApplication(),"Video reported Successfully",
                            MDToast.LENGTH_LONG, MDToast.TYPE_SUCCESS).show();

//                    Intent intent = new Intent(activity, Home.class);
//                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
//                    startActivity(intent);
                }
            });
        } else{
            MDToast.makeText(getApplication(),"No video to submit",
                    MDToast.LENGTH_LONG, MDToast.TYPE_ERROR).show();
        }

    }

}
