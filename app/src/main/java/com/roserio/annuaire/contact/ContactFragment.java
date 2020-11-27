package com.roserio.annuaire.contact;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.roserio.annuaire.R;
import com.roserio.annuaire.contact.adapter.ContactAdapter;
import com.roserio.annuaire.sqLite.MyDatabaseHelper;
import com.roserio.annuaire.sqLite.model.ContactModel;

import java.util.ArrayList;

public class ContactFragment extends Fragment {

    SwipeRefreshLayout refresh_contact;
    FloatingActionButton ajouter_contact;
    RecyclerView recycler_contact;
    ArrayList<ContactModel> contactLDB, modelSearchContact;
    View rootView;
    MyDatabaseHelper db;
    Context context;
    RecyclerView.LayoutManager manager;
    ContactAdapter adapter;
    String vue = "";
    EditText et_rechercher_contact;


    @Override
    public void onResume() {
        super.onResume();
        if(!vue.equals("first")){
            afficherContact();
        }
    }


    public ContactFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_contact, container, false);

        context = getActivity();
        db = new MyDatabaseHelper(context);
        ajouter_contact = rootView.findViewById(R.id.ajouter_contact);
        recycler_contact = rootView.findViewById(R.id.recycler_contact);
        refresh_contact = rootView.findViewById(R.id.refresh_contact);
        et_rechercher_contact = rootView.findViewById(R.id.et_rechercher_contact);
        vue = "first";



        afficherContact();

        refresh_contact.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                afficherContact();
            }
        });

        et_rechercher_contact.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                String inputText = s.toString().trim();

                if(!et_rechercher_contact.getText().toString().isEmpty()){


                    modelSearchContact.clear();

                    //Rechercher dans la liste des trajets
                    for(ContactModel ci : contactLDB){

                        if((ci.getNom().toLowerCase().contains(inputText)) || (ci.getPrenom().toLowerCase().contains(inputText))){
                            modelSearchContact.add(ci);
                        }
                    }

                    adapter.setContactModels(modelSearchContact);
                    adapter.notifyDataSetChanged();

                }else{

                    adapter.setContactModels(contactLDB);
                    adapter.notifyDataSetChanged();
                    modelSearchContact.clear();
                }

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        ajouter_contact.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), AjouterContactActivity.class);
                startActivity(intent);
            }
        });

        return  rootView;
    }

    private void afficherContact(){

        contactLDB = new ArrayList<>();
        modelSearchContact = new ArrayList<>();

        contactLDB =  db.recupererContact();
        modelSearchContact =  db.recupererContact();

        recycler_contact.setHasFixedSize(true);
        manager = new GridLayoutManager(context,1);
        recycler_contact.setLayoutManager(manager);
        recycler_contact.setNestedScrollingEnabled(false);
        recycler_contact.setItemAnimator(new DefaultItemAnimator());
        adapter = new ContactAdapter(context, contactLDB);
        recycler_contact.setNestedScrollingEnabled(false);
        recycler_contact.setAdapter(adapter);
        adapter.notifyDataSetChanged();
        vue = "empty";
        refresh_contact.setRefreshing(false);
    }
}