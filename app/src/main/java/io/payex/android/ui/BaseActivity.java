package io.payex.android.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.IdRes;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.MenuItem;
import android.view.Window;
import android.view.WindowManager;

import io.payex.android.R;


public class BaseActivity extends AppCompatActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Window window = getWindow();

        // clear FLAG_TRANSLUCENT_STATUS flag:
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        // add FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS flag to the window
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        // finally change the color
        window.setStatusBarColor(getResources().getColor(R.color.colorStatusBar));

    }

    protected void setBackButton() {
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem menuItem) {
        if (menuItem.getItemId() == android.R.id.home) {
            finish();
        }
        return super.onOptionsItemSelected(menuItem);
    }

    protected void startActivity(Class<?> cls) {
        startActivity(cls, false);
    }

    protected void startActivity(Class<?> cls, boolean finished) {
        Intent i = new Intent(this, cls);
        startActivity(i);
        if (finished) {
            finish();
        }
    }

    protected void addFragment(@IdRes int containerViewId, Fragment fragment) {
        getSupportFragmentManager().beginTransaction().add(containerViewId, fragment).commit();
    }

    protected void changeFragment(@IdRes int containerViewId, Fragment fragment) {
        changeFragment(containerViewId, fragment, null);
    }

    protected void changeFragment(@IdRes int containerViewId, Fragment fragment, String addToBackStack) {
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();

        // Replace whatever is in the fragment_container mRootView with this fragment,
        // and add the transaction to the back stack so the user can navigate back
        ft.replace(containerViewId, fragment);
        if (!TextUtils.isEmpty(addToBackStack)) {
            ft.addToBackStack(null);
        }

        // Commit the transaction
        ft.commit();
    }

    protected void startFragment(@IdRes int containerViewId, Fragment fragment) {
        replaceFragment(containerViewId, fragment, null, true);
    }

    protected void startFragment(@IdRes int containerViewId, Fragment fragment, String addToBackStack) {
        replaceFragment(containerViewId, fragment, addToBackStack, true);
    }

    protected void exitFragment(@IdRes int containerViewId, Fragment fragment) {
        replaceFragment(containerViewId, fragment, null, false);
    }

    protected void exitFragment(@IdRes int containerViewId, Fragment fragment, String addToBackStack) {
        replaceFragment(containerViewId, fragment, addToBackStack, false);
    }

    private void replaceFragment(@IdRes int containerViewId, Fragment fragment, String addToBackStack, boolean forward) {
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();

        if (forward) {
            ft.setCustomAnimations(R.anim.slide_from_right, R.anim.slide_to_left, R.anim.slide_from_left, R.anim.slide_to_right);
        } else {
            ft.setCustomAnimations(R.anim.slide_from_left, R.anim.slide_to_right, R.anim.slide_from_right, R.anim.slide_to_left);
        }

        // Replace whatever is in the fragment_container mRootView with this fragment,
        // and add the transaction to the back stack so the user can navigate back
        ft.replace(containerViewId, fragment);
        if (!TextUtils.isEmpty(addToBackStack)) {
            ft.addToBackStack(null);
        }

        // Commit the transaction
        ft.commit();
    }

}
