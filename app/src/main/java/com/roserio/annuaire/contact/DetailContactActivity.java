package com.roserio.annuaire.contact;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.roserio.annuaire.R;
import com.roserio.annuaire.contact.adapter.ContactViewHolder;
import com.roserio.annuaire.sqLite.MyDatabaseHelper;

public class DetailContactActivity extends AppCompatActivity {

    ImageView back_menu_principal,supprimer_contact, favoris_contact,photoCover, image_phone, image_whatsapp;
    TextView tv_nom_prenom, tv_email, tv_adresse,tv_telephone_contact, call;
    Context context;
    String idContact, favori;
    private static final int MY_PERMISSION = 0;
    MyDatabaseHelper db;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_contact);

        colorStatusBar();
        context = this;
        db = new MyDatabaseHelper(context);
        back_menu_principal = findViewById(R.id.back_menu_principal);
        supprimer_contact = findViewById(R.id.supprimer_contact);
        favoris_contact = findViewById(R.id.favoris_contact);
        photoCover = findViewById(R.id.photoCover);
        tv_nom_prenom = findViewById(R.id.tv_nom_prenom);
        tv_telephone_contact = findViewById(R.id.tv_telephone_contact);
        image_phone = findViewById(R.id.image_phone);
        image_whatsapp = findViewById(R.id.image_whatsapp);
        call = findViewById(R.id.call);
        tv_email = findViewById(R.id.tv_email);
        tv_adresse = findViewById(R.id.tv_adresse);

        afficherInformationContact();

        back_menu_principal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        supprimer_contact.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                AlertDialog.Builder dialog = new AlertDialog.Builder(DetailContactActivity.this);
                dialog.setCancelable(false);
                //dialog.setTitle("Suppression");
                dialog.setMessage(R.string.confirm_delete );
                dialog.setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                        db.supprimerContact(idContact);
                        Toast.makeText(context,R.string.delete_success,Toast.LENGTH_LONG).show();
                        finish();
                    }
                })
                        .setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                            }
                        });
                final AlertDialog alert = dialog.create();
                alert.show();

            }
        });

        call.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String numero = Uri.encode(tv_telephone_contact.getText().toString());
                Intent i = new Intent(Intent.ACTION_CALL);
                i.setData(Uri.parse("tel:"+numero));

                if (ContextCompat.checkSelfPermission(context, Manifest.permission.CALL_PHONE)!= PackageManager.PERMISSION_GRANTED)
                {
                    ActivityCompat.requestPermissions(DetailContactActivity.this, new String[]{Manifest.permission.CALL_PHONE},MY_PERMISSION) ;
                }else{
                    context.startActivity(i);
                }

            }

        });

        image_whatsapp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String phoneNumberWithCountryCode = tv_telephone_contact.getText().toString();
                String message = "";

                startActivity(
                        new Intent(Intent.ACTION_VIEW,
                                Uri.parse(
                                        String.format("https://api.whatsapp.com/send?phone=%s&text=%s", phoneNumberWithCountryCode, message)
                                )
                        )
                );
            }
        });

        favoris_contact.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(favori.equals("false")){
                    favori = "true";
                    favoris_contact.setImageResource(R.drawable.ic_baseline_star_full_white_24);
                }else{
                    favori = "false";
                    favoris_contact.setImageResource(R.drawable.ic_baseline_star_border_white_24);
                }
                db.updateFavori(idContact,favori);

            }
        });

        image_phone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String numero = Uri.encode(tv_telephone_contact.getText().toString());
                Intent i = new Intent(Intent.ACTION_CALL);
                i.setData(Uri.parse("tel:"+numero));
                if (ContextCompat.checkSelfPermission(context, Manifest.permission.CALL_PHONE)!= PackageManager.PERMISSION_GRANTED)
                {
                    ActivityCompat.requestPermissions(DetailContactActivity.this, new String[]{Manifest.permission.CALL_PHONE},MY_PERMISSION) ;
                }else{
                    context.startActivity(i);
                }
            }
        });

    }


    private void afficherInformationContact(){

        idContact = ContactViewHolder.idContact;
        tv_nom_prenom.setText(ContactViewHolder.nomPrenomContact);
        tv_telephone_contact.setText(ContactViewHolder.telContact);
        tv_email.setText(ContactViewHolder.emailContact);
        tv_adresse.setText(ContactViewHolder.adresseContact);
        favori = ContactViewHolder.favorisContact;

        if(!ContactViewHolder.photoContact.equals("")){
            Glide.with(context)
                    .load(BitmapFactory.decodeFile(ContactViewHolder.photoContact))
                    .centerCrop()
                    .placeholder(R.drawable.a)
                    .into(photoCover);
        }

        if(favori.equals("true")){
            favoris_contact.setImageResource(R.drawable.ic_baseline_star_full_white_24);
        }else{
            favoris_contact.setImageResource(R.drawable.ic_baseline_star_border_white_24);
        }
    }


    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults){

        switch (requestCode){
            case MY_PERMISSION:{
                if(grantResults.length>0&& grantResults[0]==PackageManager.PERMISSION_GRANTED){
                    // information();
                }else{
                    // finish();
                    Toast.makeText(this,"Vous devez accepter l'autorisation pour passer un appel",Toast.LENGTH_LONG).show();
                }
            }
        }
    }

    private void colorStatusBar(){
        Window window = this.getWindow();
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            window.setStatusBarColor(this.getResources().getColor(R.color.couleur_app_bar));
        }
    }
}