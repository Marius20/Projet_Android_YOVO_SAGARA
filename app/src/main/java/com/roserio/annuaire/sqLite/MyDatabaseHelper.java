package com.roserio.annuaire.sqLite;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.roserio.annuaire.sqLite.model.ContactModel;

import java.util.ArrayList;

public class MyDatabaseHelper  extends SQLiteOpenHelper {

    private static final String TAG = "SQLite";
    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "db_annuaire_telephonique";
    private static final String TABLE_CONTACT = "contact";

    private static final String COLUMN_CONTACT_ID ="id";
    private static final String COLUMN_CONTACT_NOM ="nom";
    private static final String COLUMN_CONTACT_PRENOM ="prenom";
    private static final String COLUMN_CONTACT_TELEPHONE = "telephone";
    private static final String COLUMN_CONTACT_EMAIL = "email";
    private static final String COLUMN_CONTACT_ADRESSE = "adresse";
    private static final String COLUMN_CONTACT_FAVORIS = "favoris";
    private static final String COLUMN_CONTACT_PHOTO = "photo";


    public MyDatabaseHelper(Context context)  {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(creerTableContact());
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_CONTACT);
        onCreate(db);
    }


    public void ajouterContact(ContactModel contact){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_CONTACT_NOM, contact.getNom());
        values.put(COLUMN_CONTACT_PRENOM, contact.getPrenom());
        values.put(COLUMN_CONTACT_TELEPHONE, contact.getTelephone());
        values.put(COLUMN_CONTACT_EMAIL, contact.getEmail());
        values.put(COLUMN_CONTACT_ADRESSE, contact.getAdresse());
        values.put(COLUMN_CONTACT_FAVORIS, contact.getFavoris());
        values.put(COLUMN_CONTACT_PHOTO, contact.getPhoto());
        db.insert(TABLE_CONTACT, null, values);
        db.close();
    }

    public ArrayList<ContactModel> recupererContact(){

        ArrayList<ContactModel> contactList = new ArrayList<ContactModel>();
        // Select All Query
        String selectQuery = "SELECT  * FROM " + TABLE_CONTACT;

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        if (cursor.moveToFirst()) {
            do {
                ContactModel contactModel = new ContactModel();
                contactModel.setId(cursor.getString(0));
                contactModel.setNom(cursor.getString(1));
                contactModel.setPrenom(cursor.getString(2));
                contactModel.setTelephone(cursor.getString(3));
                contactModel.setEmail(cursor.getString(4));
                contactModel.setAdresse(cursor.getString(5));
                contactModel.setFavoris(cursor.getString(6));
                contactModel.setPhoto(cursor.getString(7));

                contactList.add(contactModel);
            } while (cursor.moveToNext());
        }
        // return note list
        return contactList;
    }

    public ArrayList<ContactModel> recupererContactFavoris(){

        ArrayList<ContactModel> contactList = new ArrayList<ContactModel>();
        // Select All Query
       // String selectQuery = "select * from contact where favoris = ?", new String[] { id };

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery("select * from contact where favoris = ?", new String[] { "true" });

        if (cursor.moveToFirst()) {
            do {
                ContactModel contactModel = new ContactModel();
                contactModel.setId(cursor.getString(0));
                contactModel.setNom(cursor.getString(1));
                contactModel.setPrenom(cursor.getString(2));
                contactModel.setTelephone(cursor.getString(3));
                contactModel.setEmail(cursor.getString(4));
                contactModel.setAdresse(cursor.getString(5));
                contactModel.setFavoris(cursor.getString(6));
                contactModel.setPhoto(cursor.getString(7));

                contactList.add(contactModel);
            } while (cursor.moveToNext());
        }
        // return note list
        return contactList;
    }

    public void supprimerContact(String id) {

        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_CONTACT, COLUMN_CONTACT_ID + " = ?",
                new String[] {id});
        db.close();
    }


    public void updateFavori(String idContact, String fav){

        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_CONTACT_FAVORIS, fav);
        db.update(TABLE_CONTACT, values, COLUMN_CONTACT_ID +" = ?",
                new String[]{String.valueOf(idContact)});
    }

    private String creerTableContact(){
        String script = "CREATE TABLE " + TABLE_CONTACT + "("
                + COLUMN_CONTACT_ID + " INTEGER PRIMARY KEY," + COLUMN_CONTACT_NOM + " TEXT,"
                + COLUMN_CONTACT_PRENOM + " TEXT," + COLUMN_CONTACT_TELEPHONE + " TEXT,"
                + COLUMN_CONTACT_EMAIL + " TEXT," + COLUMN_CONTACT_ADRESSE + " TEXT,"
                + COLUMN_CONTACT_FAVORIS + " TEXT," + COLUMN_CONTACT_PHOTO + " TEXT" + ")";
        return  script;
    }
}
