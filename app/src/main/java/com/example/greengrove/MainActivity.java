package com.example.greengrove;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.os.Bundle;
import android.widget.TextView;

import com.example.greengrove.Fragment.CartsFragment;
import com.example.greengrove.Fragment.FavouriteFragment;
import com.example.greengrove.Fragment.MeatsFragment;
import com.example.greengrove.Fragment.HomeFragment;
import com.example.greengrove.Fragment.ProfileFragment;
import com.example.greengrove.Model.Fruit;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.imageview.ShapeableImageView;
import com.google.android.material.navigation.NavigationView;

import java.util.List;

public class MainActivity extends AppCompatActivity{
    private DrawerLayout drawerLayout;
    private Toolbar toolbar;
    private NavigationView navigationView;
    private TextView tvWelcome;
    private ShapeableImageView shapeableImageView;
    private BottomNavigationView bottomNavigationView;
    private static final int FRAGMENT_HOME = 1;
    private static final int FRAGMENT_CART=2;
    private static final int FRAGMENT_FAVOURITE = 3;
    private static final int FRAGMENT_PROFILE = 4;
    private int current = FRAGMENT_HOME;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        bottomNavigationView = findViewById(R.id.btnbottom);
        drawerLayout = findViewById(R.id.drawerlayout);
//        navigationView = findViewById(R.id.navigation_view);
        tvWelcome = findViewById(R.id.tvWelcome);
        shapeableImageView = findViewById(R.id.shapeableImageViewNavigetion);
        onResume();
//        SharedPreferences sharedPreferences = getSharedPreferences("INFO", MODE_PRIVATE);
//        String username = sharedPreferences.getString("username", "");
//        tvWelcome.setText(username);
//        toolbar = findViewById(R.id.toolbar);
//        setSupportActionBar(toolbar);
//        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar,
//                R.string.nav_drawer_open, R.string.nav_drawer_close);
//        drawerLayout.addDrawerListener(toggle);
//        toggle.syncState();

//        navigationView.setNavigationItemSelectedListener(this);
//        navigationView.setCheckedItem(R.id.nav_home);
        bottomNavigationView.setOnItemSelectedListener(item -> {
            int itemId = item.getItemId();
            if (itemId == R.id.bottom_home) {
                openHomeFragment();
//                navigationView.setCheckedItem(R.id.nav_home);
                return true;
            } else if (itemId == R.id.bottom_cart) {
                openCartFragment();
                return true;
            }else if (itemId == R.id.bottom_favourite) {
                openFavouriteFragment();
//                navigationView.setCheckedItem(R.id.nav_favourite);
                return true;
            }else if (itemId == R.id.bottom_profile) {
                openProfileFragment();
                return true;
            }
            return false;
        });


    }

    private void replaceFragment(Fragment fragment) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.framelayout, fragment);
        fragmentTransaction.commit();
    }

    private void openHomeFragment() {
        if (current != FRAGMENT_HOME) {
            replaceFragment(new HomeFragment());
            current = FRAGMENT_HOME;
        }
    }
    private void openCartFragment() {
        if (current != FRAGMENT_CART) {
            replaceFragment(new CartsFragment());
            current = FRAGMENT_CART;
        }
    }
    private void openFavouriteFragment() {
        if (current != FRAGMENT_FAVOURITE) {
            replaceFragment(new FavouriteFragment());
            current = FRAGMENT_FAVOURITE;
        }
    }
    private void openProfileFragment() {
        if (current != FRAGMENT_PROFILE) {
            replaceFragment(new ProfileFragment());
            current = FRAGMENT_PROFILE;
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (getIntent().hasExtra("bottom_fragment")) {
            int fragmentIndex = getIntent().getIntExtra("bottom_fragment", -1);
            if (fragmentIndex == 3) { // 3 là index của Fragment 4 trong bottom navigation
                replaceFragment(new ProfileFragment());
                bottomNavigationView.getMenu().findItem(R.id.bottom_profile).setChecked(true);
            }
        }else {
            replaceFragment(new HomeFragment());
            bottomNavigationView.getMenu().findItem(R.id.bottom_home).setChecked(true);
        }
    }
    //    @Override
//    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
//        int id = item.getItemId();
//        if (id == R.id.nav_home) {
//            openHomeFragment();
//            bottomNavigationView.getMenu().findItem(R.id.bottom_home).setChecked(true);
//        } else if (id == R.id.nav_favourite) {
//            openFavouriteFragment();
//            bottomNavigationView.getMenu().findItem(R.id.bottom_favourite).setChecked(true);
//        }
//        setTitleToolbar();
//        drawerLayout.closeDrawer(GravityCompat.START);
//        return true;
//    }

//    private void setTitleToolbar() {
//        String title = "";
//        switch (current) {
//            case FRAGMENT_HOME:
//                title = getString(R.string.nav_home);
//                break;
//            case FRAGMENT_FAVOURITE:
//                title = getString(R.string.nav_favourite);
//                break;
//            case FRAGMENT_CART:
//                title = "CART";
//                break;
//            case FRAGMENT_PROFILE:
//                title = "PROFILE";
//                break;
//        }
//        if (getSupportActionBar() != null) {
//            getSupportActionBar().setTitle(title);
//        }
//    }
}