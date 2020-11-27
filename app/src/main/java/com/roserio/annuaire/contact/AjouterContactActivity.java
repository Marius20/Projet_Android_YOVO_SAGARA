package com.roserio.annuaire.contact;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;

import android.Manifest;
import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.AlertDialog;
import android.app.Notification;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.roserio.annuaire.R;
import com.roserio.annuaire.sqLite.MyDatabaseHelper;
import com.roserio.annuaire.sqLite.model.ContactModel;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import static com.roserio.annuaire.App.CHANNEL_1_ID;
import static java.security.AccessController.getContext;

public class AjouterContactActivity extends AppCompatActivity {

    ImageView back_menu_principal;
    TextView tv_save_contact;
    EditText et_nom_contact,et_prenom_contact,et_tel_contact,et_email_contact,et_adresse_contact;
    ImageView photo,photoCover;
    Context context;
    MyDatabaseHelper db;
    private static final int MY_PERMISSION = 0;
    Notification notification;
    private NotificationManagerCompat notificationManager;
    public static String mCurrentPhotoPath = "";
    public static Uri photoURI;
    static final int REQUEST_TAKE_PHOTO = 100;
    static final int REQUEST_PICK_IMAGE = 200;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ajouter_contact);

        colorStatusBar();
        context = this;
        notificationManager = NotificationManagerCompat.from(this);
        db = new MyDatabaseHelper(context);
        back_menu_principal = findViewById(R.id.back_menu_principal);
        tv_save_contact = findViewById(R.id.tv_save_contact);
        et_nom_contact = findViewById(R.id.et_nom_contact);
        et_prenom_contact = findViewById(R.id.et_prenom_contact);
        et_tel_contact = findViewById(R.id.et_tel_contact);
        et_email_contact = findViewById(R.id.et_email_contact);
        et_adresse_contact = findViewById(R.id.et_adresse_contact);
        photo = findViewById(R.id.photo);
        photoCover = findViewById(R.id.photoCover);


        back_menu_principal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        photo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if ((ContextCompat.checkSelfPermission(context, Manifest.permission.CAMERA)!= PackageManager.PERMISSION_GRANTED) ||
                        (ContextCompat.checkSelfPermission(context, Manifest.permission.READ_EXTERNAL_STORAGE)!= PackageManager.PERMISSION_GRANTED) ||
                        (ContextCompat.checkSelfPermission(context, Manifest.permission.WRITE_EXTERNAL_STORAGE)!= PackageManager.PERMISSION_GRANTED))
                {
                    ActivityCompat.requestPermissions(AjouterContactActivity.this, new String[]{Manifest.permission.CAMERA,Manifest.permission.READ_EXTERNAL_STORAGE,Manifest.permission.WRITE_EXTERNAL_STORAGE},MY_PERMISSION) ;
                }else{
                    showPhotoPopUp();
                }
            }
        });

        tv_save_contact.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String sNom,sPrenom,sTelephone,sEmail,sAdresse;
                sNom = et_nom_contact.getText().toString();
                sPrenom = et_prenom_contact.getText().toString();
                sTelephone = et_tel_contact.getText().toString();
                sEmail = et_email_contact.getText().toString();
                sAdresse = et_adresse_contact.getText().toString();
                if((sNom.length() == 0) && (sTelephone.length() == 0)){
                    Toast.makeText(context,R.string.saisir_prenom_telephone,Toast.LENGTH_LONG).show();
                }else{
                    saveContact(sNom,sPrenom,sTelephone,sEmail,sAdresse);
                }
            }
        });
    }


    void showPhotoPopUp(){
        final CharSequence[] items = {"Prendre photo", "Galerie",
                "Annuler"};
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("Changer Photo");
        builder.setIcon(R.drawable.ic_gallery);
        builder.setItems(items, new DialogInterface.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.KITKAT)
            @Override
            public void onClick(DialogInterface dialog, int item) {
                //boolean result=Utility.checkPermission(MainActivity.this)
                if (items[item].equals("Prendre photo")) {
                    //startActivity(new Intent(getContext(), CameraActivity.class));
                    // if (shouldAskPermissions()) {
                    //  askPermissions();
                    //   }
                    prendreImage();

                } else if (items[item].equals("Galerie")) {
                    //   if (shouldAskPermissions()) {
                    // askPermissions();
                    //   }
                    startActivityForResult(new Intent(context, GaleryActivity.class), REQUEST_PICK_IMAGE);
                } else if (items[item].equals("Annuler")) {
                    dialog.dismiss();
                }
            }
        });
        builder.show();
    }


    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    public void prendreImage(){

        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        // Ensure that there's a camera activity to handle the intent
        if (takePictureIntent.resolveActivity(context.getPackageManager()) != null) {
            // Create the File where the photo should go
            File photoFile = null;
            try {
                photoFile = createImageFile();
                //Toast.makeText(getContext(), mCurrentPhotoPath, Toast.LENGTH_LONG).show();
            } catch (IOException ex) {
                // Error occurred while creating the File
            }
            // Continue only if the File was successfully created
            if (photoFile != null) {
                photoURI = FileProvider.getUriForFile(context,
                        "com.roserio.annuaire.provider",
                        photoFile);
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
                startActivityForResult(takePictureIntent, REQUEST_TAKE_PHOTO);
            }
        }
    }

    private File createImageFile() throws IOException {
        // Create an image file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        File storageDir = context.getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(
                imageFileName,  /* prefix */
                ".jpg",         /* suffix */
                storageDir      /* directory */
        );

        // Save a file: path for use with ACTION_VIEW intents
        mCurrentPhotoPath = image.getAbsolutePath();
        //Toast.makeText(getContext(),mCurrentPhotoPath, Toast.LENGTH_LONG).show();


        //Toast.makeText(getContext(), mCurrentPhotoPath, Toast.LENGTH_LONG).show();
        //uploadImage(mCurrentPhotoPath);

        return image;
    }

    private void saveContact(String nom,String prenom,String telephone,String email,String adresse){

        ContactModel contact = new ContactModel("",nom,prenom,telephone,email,adresse,"false",mCurrentPhotoPath);
        db.ajouterContact(contact);
        Toast.makeText(context,R.string.save_contact_done,Toast.LENGTH_LONG).show();
        et_nom_contact.setText("");
        et_prenom_contact.setText("");
        et_tel_contact.setText("");
        et_email_contact.setText("");
        et_adresse_contact.setText("");
        afficherNotification("Enregistrement de contact","Concact save");
        Glide.with(context)
                .load(R.drawable.ic_baseline_person_24)
                .centerCrop()
                .into(photo);
        finish();
    }

    private void colorStatusBar(){
        Window window = this.getWindow();
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            window.setStatusBarColor(this.getResources().getColor(R.color.couleur_status_bar));
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults){

        switch (requestCode){
            case MY_PERMISSION:{
                if(grantResults.length>0&& grantResults[0]==PackageManager.PERMISSION_GRANTED){
                    showPhotoPopUp();
                    // information();
                }else{
                    // finish();
                    Toast.makeText(this,"Vous devez accepter l'autorisation pour pouvoir acceder à la galerie ou prendre une photo",Toast.LENGTH_LONG).show();
                }
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case 100:
                if (resultCode == RESULT_OK) {

                    Glide.with(context)
                            .load(BitmapFactory.decodeFile(mCurrentPhotoPath))
                            .centerCrop()
                            .into(photoCover);
                }
                break;

            case 200:
                if(resultCode == RESULT_OK){

                    Uri imageUri=null;
                    Bitmap bitmap= null;

                    if(resultCode== RESULT_OK){

                        if(requestCode==REQUEST_PICK_IMAGE){
                            imageUri = Uri.parse(data.getStringExtra(GaleryActivity.KEY_PHOTO_URI));
                            bitmap = GaleryActivity.getImageOnUri(context, imageUri);

                            try {
                                File file = GaleryActivity.saveSeletedImage(context,bitmap);
                                mCurrentPhotoPath = file.getAbsolutePath();

                                Glide.with(context)
                                        .load(BitmapFactory.decodeFile(mCurrentPhotoPath))
                                        .centerCrop()
                                        .into(photoCover);


                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }
                    }
                }
                break;
        }

    }


    private void afficherNotification(String titre,String message){

        notification = new NotificationCompat.Builder(this, CHANNEL_1_ID)
                .setSmallIcon(R.drawable.ic_baseline_contacts_black_24)
                .setContentTitle(titre)
                .setContentText(message)
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setCategory(NotificationCompat.CATEGORY_MESSAGE)
                .setGroup("rappel_group")
                .build();
        notificationManager.notify(0, notification);


    }



    //    private void requestPermission() {
//        ActivityCompat.requestPermissions(getActivity(), new String[]{"android.permission.CAMERA"}, REQUEST_TAKE_PHOTO );
//    }
}