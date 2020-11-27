package com.roserio.annuaire;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;

import com.aurelhubert.ahbottomnavigation.AHBottomNavigation;
import com.aurelhubert.ahbottomnavigation.AHBottomNavigationItem;
import com.roserio.annuaire.contact.ContactFavorisFragment;
import com.roserio.annuaire.contact.ContactFragment;

public class MainActivity extends AppCompatActivity {

    public AHBottomNavigation bottomNavigation;
    Fragment fragment = null;
    Class fragmentClass;
    FragmentManager fragmentManager;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        colorStatusBar();
        bottomNavigation = findViewById(R.id.bottom_navigationMenuPrincipal);
        setBottomView();
        showContactFragment();

    }



    public void setBottomView(){

        AHBottomNavigationItem item1 = new AHBottomNavigationItem("Contact", R.drawable.ic_baseline_contacts_black_24, R.color.couleur_blanche);
        AHBottomNavigationItem item2 = new AHBottomNavigationItem(R.string.favoris, R.drawable.ic_baseline_star_outline_24, R.color.couleur_blanche);

        bottomNavigation.addItem(item1);
        bottomNavigation.addItem(item2);

        bottomNavigation.setForceTint(true);
        bottomNavigation.setTitleState(AHBottomNavigation.TitleState.ALWAYS_SHOW);
        bottomNavigation.setCurrentItem(0);
        bottomNavigation.setTitleTextSize(25,25);
        bottomNavigation.setNotificationBackgroundColor(Color.parseColor("#ffffff"));

        bottomNavigation.setOnTabSelectedListener(new AHBottomNavigation.OnTabSelectedListener() {
            @Override
            public boolean onTabSelected(int position, boolean wasSelected) {

                if(String.valueOf(position).equals("0")){
                    showContactFragment();
                }else if(String.valueOf(position).equals("1")){
                    showFavorisFragment();
                }
                return true;
            }
        });
        bottomNavigation.setOnNavigationPositionListener(new AHBottomNavigation.OnNavigationPositionListener() {
            @Override public void onPositionChange(int y) {
            }
        });
    }

    private void showContactFragment(){

        fragmentClass = ContactFragment.class;
        try {
            fragment = (Fragment) fragmentClass.newInstance();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InstantiationException e) {
            e.printStackTrace();
        }
        fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction().replace(R.id.menuPrincipalFrame, fragment).commit();

    }

    private void showFavorisFragment(){


        fragmentClass = ContactFavorisFragment.class;
        try {
            fragment = (Fragment) fragmentClass.newInstance();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InstantiationException e) {
            e.printStackTrace();
        }
        fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction().replace(R.id.menuPrincipalFrame, fragment).commit();

    }

    private void colorStatusBar(){
        Window window = this.getWindow();
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            window.setStatusBarColor(this.getResources().getColor(R.color.couleur_status_bar));
        }
    }
}