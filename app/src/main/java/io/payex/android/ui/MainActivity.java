package io.payex.android.ui;

import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.customtabs.CustomTabsIntent;
import android.support.design.widget.Snackbar;
import android.util.Log;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;

import org.chromium.customtabsclient.CustomTabsActivityHelper;

import butterknife.BindColor;
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
import me.zhanghai.android.customtabshelper.CustomTabsHelperFragment;

public class MainActivity extends BaseActivity
        implements NavigationView.OnNavigationItemSelectedListener,
        SaleHistoryFragment.OnListFragmentInteractionListener,
        SaleFragment.OnFragmentInteractionListener {

    private static final Uri PROJECT_URI = Uri.parse(
            "https://www.google.com");

    private final CustomTabsActivityHelper.CustomTabsFallback mCustomTabsFallback =
            new CustomTabsActivityHelper.CustomTabsFallback() {
                @Override
                public void openUri(Activity activity, Uri uri) {
                    try {
                        activity.startActivity(new Intent(Intent.ACTION_VIEW, uri));
                    } catch (ActivityNotFoundException e) {
                        Log.e(getLocalClassName(), "Activity not found");
                        Snackbar.make(mDrawer, "Activity not found", Snackbar.LENGTH_LONG).show();
                    }
                }
            };

    @BindView(R.id.toolbar) Toolbar mToolbar;
    @BindView(R.id.drawer_layout) DrawerLayout mDrawer;
    @BindView(R.id.nav_view) NavigationView mNavView;

    @BindColor(R.color.colorPrimary) int mColorPrimary;

    private CustomTabsHelperFragment mCustomTabsHelperFragment;
    private CustomTabsIntent mCustomTabsIntent;

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
        mNavView.setCheckedItem(mNavView.getMenu().getItem(0).getItemId());
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
            changeFragment(R.id.fragment_container, SaleFragment.newInstance(), null);
        } else if (id == R.id.nav_void_transaction) {
            changeFragment(R.id.fragment_container, SaleHistoryFragment.newInstance(), null);
        } else if (id == R.id.nav_sale_history) {
            changeFragment(R.id.fragment_container, SaleHistoryFragment.newInstance(), null);
        } else if (id == R.id.nav_about) {
            openWithCustomTabs();
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
            SaleHistoryItem saleHistoryItem = (SaleHistoryItem) item;
            Log.i(getLocalClassName(), saleHistoryItem.getPrimaryText());
            startActivity(SaleSlipActivity.class, false);
        }
    }

    @Override
    public void onAmountEntered() {
        startActivity(CardReaderActivity.class, false);
    }

    private void openWithCustomTabs() {
        // fixme may not be needed
        mCustomTabsHelperFragment = CustomTabsHelperFragment.attachTo(this);

        mCustomTabsHelperFragment.setConnectionCallback(
                new CustomTabsActivityHelper.ConnectionCallback() {
                    @Override
                    public void onCustomTabsConnected() {
                        mCustomTabsHelperFragment.mayLaunchUrl(PROJECT_URI, null, null);
                    }
                    @Override
                    public void onCustomTabsDisconnected() {}
                });

        mCustomTabsIntent = new CustomTabsIntent.Builder()
                .enableUrlBarHiding()
                .setToolbarColor(mColorPrimary)
                .setShowTitle(true)
                .build();

        CustomTabsHelperFragment.open(this, mCustomTabsIntent, PROJECT_URI, mCustomTabsFallback);
    }
}
