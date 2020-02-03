package com.example.bottomnavigationandroidx.act;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.os.Bundle;
import android.view.MenuItem;

import com.example.bottomnavigationandroidx.R;
import com.example.bottomnavigationandroidx.frg.HomeFragment;
import com.example.bottomnavigationandroidx.frg.SearchFragment;
import com.example.bottomnavigationandroidx.frg.SettingFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class MainActivity extends AppCompatActivity {

    private static final String SELECTED_NAV = "SELECTED_NAV";
    private BottomNavigationView mBottomNav;
    private int mSelectedNav;
    private BottomNavigationView.OnNavigationItemSelectedListener mNavigationBottomListener = new BottomNavigationView.OnNavigationItemSelectedListener() {
        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
            selectFragment(menuItem.getItemId());
            return true;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mBottomNav = findViewById(R.id.nav_view);
        mBottomNav.setOnNavigationItemSelectedListener(mNavigationBottomListener);
        mBottomNav.setOnNavigationItemReselectedListener(new BottomNavigationView.OnNavigationItemReselectedListener() {
            @Override
            public void onNavigationItemReselected(@NonNull MenuItem menuItem) {
            }
        });

        if (savedInstanceState != null) {
            mSelectedNav = savedInstanceState.getInt(SELECTED_NAV, 0);
        } else {
            mSelectedNav = R.id.navigation_home;
        }
        selectFragment(mSelectedNav);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        outState.putInt(SELECTED_NAV, mSelectedNav);
        super.onSaveInstanceState(outState);
    }

    @Override
    public void onBackPressed() {
        if (mSelectedNav != R.id.navigation_home) {
            selectFragment(R.id.navigation_home);
        } else {
            super.onBackPressed();
        }
    }

    private void selectFragment(int menuId) {
        Fragment frag = null;
        String tag = null;
        switch (menuId) {
            case R.id.navigation_home:
                frag = HomeFragment.newInstance();
                tag = "HomeFragment";
                break;
            case R.id.navigation_search:
                frag = SearchFragment.newInstance();
                tag = "SearchFragment";
                break;
            case R.id.navigation_setting:
                frag = SettingFragment.newInstance();
                tag = "SettingFragment";
                break;
        }

        // update selected item
        mSelectedNav = menuId;

        FragmentManager fragmentManager = getSupportFragmentManager();
        Fragment currentFragment = fragmentManager.getPrimaryNavigationFragment();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

        // hide current active tab fragment
        if (currentFragment != null) {
            fragmentTransaction.hide(currentFragment);
        }
        // check request fragment to show or add new it
        Fragment fragmentTemp = fragmentManager.findFragmentByTag(tag);
        if (fragmentTemp == null) {
            fragmentTemp = frag;
            if (fragmentTemp != null) {
                fragmentTransaction.add(R.id.container, fragmentTemp, tag);
            }
        } else {
            fragmentTransaction.show(fragmentTemp);
        }
        fragmentTransaction.setPrimaryNavigationFragment(fragmentTemp);
        fragmentTransaction.setReorderingAllowed(true);
        fragmentTransaction.commitAllowingStateLoss();

        mBottomNav.setOnNavigationItemSelectedListener(null);
        mBottomNav.setSelectedItemId(menuId);
        mBottomNav.setOnNavigationItemSelectedListener(mNavigationBottomListener);
    }
}
