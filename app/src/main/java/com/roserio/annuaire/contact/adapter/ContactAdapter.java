package com.roserio.annuaire.contact.adapter;

import android.content.Context;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.roserio.annuaire.R;
import com.roserio.annuaire.sqLite.model.ContactModel;

import java.util.ArrayList;

public class ContactAdapter extends RecyclerView.Adapter<ContactViewHolder>{

    Context context;
    ArrayList<com.roserio.annuaire.sqLite.model.ContactModel> contactModels;


    public void setContactModels(ArrayList<ContactModel> contactModels) {
        this.contactModels = contactModels;
    }


    public ContactAdapter(Context context, ArrayList<com.roserio.annuaire.sqLite.model.ContactModel> contactModels) {
        this.context = context;
        this.contactModels = contactModels;
    }

    @NonNull
    @Override
    public ContactViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_contact, viewGroup,false);
        return new ContactViewHolder(view,context,contactModels);
    }

    @Override
    public void onBindViewHolder(@NonNull final ContactViewHolder holder, final int i) {
        final ContactModel model = contactModels.get(i);

        holder.tv_id_contact.setText(model.getId());
        holder.tv_nom_prenom_contact.setText(model.getNom()+" "+ model.getPrenom());
        holder.tv_telephone.setText(model.getTelephone());
        holder.tv_email.setText(model.getEmail());
        holder.tv_adresse.setText(model.getAdresse());
        holder.tv_adresse_photo.setText(model.getPhoto());
        holder.tv_favoris.setText(model.getFavoris());

        if(!model.getPhoto().equals("")){
            Glide.with(context)
                    .load(BitmapFactory.decodeFile(model.getPhoto()))
                    .centerCrop()
                    .placeholder(R.drawable.a)
                    .into(holder.photo);
        }else{
            Glide.with(context)
                    .load(R.drawable.a)
                    .centerCrop()
                    .placeholder(R.drawable.a)
                    .into(holder.photo);
        }
    }

    @Override
    public int getItemCount() {
        return contactModels.size();
    }
}


