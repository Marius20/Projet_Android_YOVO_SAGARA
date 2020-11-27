package com.roserio.annuaire.contact;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.roserio.annuaire.R;
import com.roserio.annuaire.contact.adapter.ContactAdapter;
import com.roserio.annuaire.sqLite.MyDatabaseHelper;
import com.roserio.annuaire.sqLite.model.ContactModel;

import java.util.ArrayList;


public class ContactFavorisFragment extends Fragment {


    FloatingActionButton ajouter_contact;
    RecyclerView recycler_contact_favoris;
    ArrayList<ContactModel> contactLDB;
    View rootView;
    MyDatabaseHelper db;
    Context context;
    RecyclerView.LayoutManager manager;
    ContactAdapter adapter;
    String vue = "";


    @Override
    public void onResume() {
        super.onResume();
        if(!vue.equals("first")){
            afficherContact();
        }
    }



    public ContactFavorisFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {

        rootView =  inflater.inflate(R.layout.fragment_contact_favoris, container, false);

        context = getActivity();
        db = new MyDatabaseHelper(context);
        ajouter_contact = rootView.findViewById(R.id.ajouter_contact);
        recycler_contact_favoris = rootView.findViewById(R.id.recycler_contact_favoris);
        vue = "first";

        afficherContact();


        ajouter_contact.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), AjouterContactActivity.class);
                startActivity(intent);
            }
        });


        return rootView;
    }

    private void afficherContact(){

        contactLDB = new ArrayList<>();
        contactLDB =  db.recupererContactFavoris();

        recycler_contact_favoris.setHasFixedSize(true);
        manager = new GridLayoutManager(context,1);
        recycler_contact_favoris.setLayoutManager(manager);
        recycler_contact_favoris.setNestedScrollingEnabled(false);
        recycler_contact_favoris.setItemAnimator(new DefaultItemAnimator());
        adapter = new ContactAdapter(context, contactLDB);
        recycler_contact_favoris.setNestedScrollingEnabled(false);
        recycler_contact_favoris.setAdapter(adapter);
        adapter.notifyDataSetChanged();
        vue = "empty";

        // swipe_refresh_compte.setRefreshing(false);

    }
}