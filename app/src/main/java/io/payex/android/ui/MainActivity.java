package io.payex.android.ui;

import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.util.Log;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;

import butterknife.BindView;
import butterknife.ButterKnife;
import eu.davidea.flexibleadapter.items.IFlexible;
import io.payex.android.R;
import io.payex.android.ui.login.LoginActivity;
import io.payex.android.ui.sale.CardReaderActivity;
import io.payex.android.ui.sale.SaleFragment;
import io.payex.android.ui.sale.history.SaleHistoryFragment;
import io.payex.android.ui.sale.history.SaleHistoryItem;
import io.payex.android.ui.sale.history.SaleSlipActivity;

public class MainActivity extends BaseActivity
        implements NavigationView.OnNavigationItemSelectedListener,
        SaleHistoryFragment.OnListFragmentInteractionListener,
        SaleFragment.OnFragmentInteractionListener {

    @BindView(R.id.toolbar) Toolbar mToolbar;
    @BindView(R.id.drawer_layout) DrawerLayout mDrawer;
    @BindView(R.id.nav_view) NavigationView mNavView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setSupportActionBar(mToolbar);
        ButterKnife.bind(this);

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, mDrawer, mToolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        mDrawer.setDrawerListener(toggle);
        toggle.syncState();

        mNavView.setNavigationItemSelectedListener(this);

        // select the default
        onNavigationItemSelected(mNavView.getMenu().getItem(0));
    }

    @Override
    public void onBackPressed() {
        if (mDrawer.isDrawerOpen(GravityCompat.START)) {
            mDrawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.nav_sale) {
            changeFragment(R.id.fragment_container, SaleFragment.newInstance());
        } else if (id == R.id.nav_void_transaction) {
            changeFragment(R.id.fragment_container, SaleHistoryFragment.newInstance());
        } else if (id == R.id.nav_sale_history) {
            changeFragment(R.id.fragment_container, SaleHistoryFragment.newInstance());
        } else if (id == R.id.nav_about) {
            // todo about page
            Snackbar.make(mDrawer, "About page under construction", Snackbar.LENGTH_LONG).show();
        } else if (id == R.id.nav_logout) {
            // todo clear all the cache before logout
            startActivity(LoginActivity.class, true);
        }

        mDrawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void onListFragmentInteraction(IFlexible item) {
        if (item instanceof SaleHistoryItem) {
            Log.i(getLocalClassName(), ((SaleHistoryItem) item).getTitle());
            startActivity(SaleSlipActivity.class, false);
        }
    }

    @Override
    public void onAmountEntered() {
        startActivity(CardReaderActivity.class, false);
    }
}
