package io.payex.android.ui;

import android.content.Intent;
import android.support.annotation.IdRes;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;


public class BaseActivity extends AppCompatActivity {

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
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();

        // Replace whatever is in the fragment_container mRootView with this fragment,
        // and add the transaction to the back stack so the user can navigate back
        ft.replace(containerViewId, fragment);
        ft.addToBackStack(null);

        // Commit the transaction
        ft.commit();
    }

}
