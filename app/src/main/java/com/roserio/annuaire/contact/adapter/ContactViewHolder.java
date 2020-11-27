package com.roserio.annuaire.contact.adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.roserio.annuaire.R;
import com.roserio.annuaire.contact.DetailContactActivity;
import com.roserio.annuaire.sqLite.model.ContactModel;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class ContactViewHolder extends RecyclerView.ViewHolder {

    Context context;
    ArrayList<ContactModel> contactModels;
    TextView tv_id_contact, tv_nom_prenom_contact, tv_email, tv_adresse, tv_telephone, tv_adresse_photo, tv_favoris;
    CircleImageView photo;
    RelativeLayout layout_contact;
    public static String idContact, telContact, adresseContact, emailContact, nomPrenomContact, photoContact, favorisContact;


    public ContactViewHolder(@NonNull View itemView, final Context ctx, final ArrayList<com.roserio.annuaire.sqLite.model.ContactModel> contactModels ) {
        super(itemView);
        this.context = ctx;
        this.contactModels = contactModels;
        tv_id_contact = itemView.findViewById(R.id.tv_id_contact);
        tv_nom_prenom_contact = itemView.findViewById(R.id.tv_nom_prenom_contact);
        tv_email = itemView.findViewById(R.id.tv_email);
        tv_adresse = itemView.findViewById(R.id.tv_adresse);
        tv_telephone = itemView.findViewById(R.id.tv_telephone);
        tv_adresse_photo = itemView.findViewById(R.id.tv_adresse_photo);
        layout_contact = itemView.findViewById(R.id.layout_contact);
        photo = itemView.findViewById(R.id.photo);
        tv_favoris = itemView.findViewById(R.id.tv_favoris);


        layout_contact.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                idContact = tv_id_contact.getText().toString();
                nomPrenomContact = tv_nom_prenom_contact.getText().toString();
                telContact = tv_telephone.getText().toString();
                emailContact = tv_email.getText().toString();
                adresseContact = tv_adresse.getText().toString();
                photoContact = tv_adresse_photo.getText().toString();
                favorisContact = tv_favoris.getText().toString();
                Intent intent=new Intent(ctx, DetailContactActivity.class);
                ctx.startActivity(intent);
            }
        });
    }
}
