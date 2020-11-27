package com.roserio.annuaire.contact;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class GaleryActivity extends AppCompatActivity {


    static final int REQUEST_SELECT_PHOTO = 1;

    public static final String KEY_PHOTO_URI="PHOTO";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent,REQUEST_SELECT_PHOTO);
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        Intent intent = new Intent();

        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==REQUEST_SELECT_PHOTO && resultCode== Activity.RESULT_OK){
            if(data!=null){
                intent.putExtra(KEY_PHOTO_URI, data.getData().toString());
                setResult(Activity.RESULT_OK, intent);
            }
            else {
                setResult(Activity.RESULT_CANCELED, intent);
            }
        }

        finish();
    }


    public static File saveSeletedImage(Context context, Bitmap image) throws IOException {

        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";

        File storageDir = context.getExternalFilesDir(Environment.DIRECTORY_PICTURES);

        File file = File.createTempFile(
                imageFileName,  /* prefix */
                ".jpg",         /* suffix */
                storageDir      /* directory */
        );

        if (image!=null){

            ByteArrayOutputStream bytes=new ByteArrayOutputStream();
            image.compress(Bitmap.CompressFormat.JPEG,70,bytes);
            FileOutputStream fo =new FileOutputStream(file);
            fo.write(bytes.toByteArray());
            fo.close();
        }

        return file;

    }


    public static Bitmap getImageOnUri(Context context, Uri uri){

        Bitmap image=null;
        try {
            image = MediaStore.Images.Media.getBitmap(context.getContentResolver(),uri);
            Log.i("IMAGE","$$"+image+"$$");
        } catch (IOException e) {
            e.printStackTrace();
        }catch (java.lang.RuntimeException e){
            e.printStackTrace();
        }

        return image;

    }
}