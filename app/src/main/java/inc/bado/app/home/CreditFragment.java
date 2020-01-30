package inc.bado.app.home;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ExpandableListView;
import android.widget.TextView;

import com.google.android.material.navigation.NavigationView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import inc.bado.app.R;
import inc.bado.app.adapters.CreditListAdapter;
import inc.bado.app.models.Credit;

public class CreditFragment extends Fragment implements
        NavigationView.OnNavigationItemSelectedListener{


    @BindView(R.id.app_bar) Toolbar toolbar;
    @BindView(R.id.drawer_layout) public DrawerLayout drawer;
    @BindView(R.id.credit_rv) RecyclerView recyclerView;

    private View view;
    private Context mContext;
    private CreditListAdapter adapter;

    private List<Credit> creditList = new ArrayList<>();

    private OnCreditInteractionListener mListener;

    private CreditFragment() {
    }

    public static CreditFragment newInstance() {
        CreditFragment fragment = new CreditFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_credit, container, false);
        ButterKnife.bind(this, view);

        mContext = getContext();

        adapter = new CreditListAdapter(creditList,mContext);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.addItemDecoration(new DividerItemDecoration(getContext(), LinearLayoutManager.VERTICAL));
        recyclerView.setAdapter(adapter);


        loadCreditData();
        setUpDrawer(view);
        return view;
    }

    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
        }
    }

    public void loadCreditData(){
        creditList.add(new Credit("To Buy 20 Shoes","Daniel G", "200"));
        creditList.add(new Credit("To Buy 20 Shoes","Adam G", "200"));
        creditList.add(new Credit("To Buy 20 Shoes","Abel G", "200"));
        creditList.add(new Credit("To Buy 20 Shoes","Ashe G", "200"));
    }

    private void setUpDrawer(View view){

        final ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                getActivity(), drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();


        NavigationView navigationView = view.findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        View headerView = navigationView.getHeaderView(0);
        TextView navUsername = (TextView) headerView.findViewById(R.id.nav_name);

        drawer.addDrawerListener(new DrawerLayout.DrawerListener() {
            @Override
            public void onDrawerSlide(@NonNull View drawerView, float slideOffset) {

            }

            @Override
            public void onDrawerOpened(@NonNull View drawerView) {
                mListener.onDrawerOpened();
            }

            @Override
            public void onDrawerClosed(@NonNull View drawerView) {
                mListener.onDrawerClosed();
            }

            @Override
            public void onDrawerStateChanged(int newState) {
                if( newState == DrawerLayout.STATE_SETTLING && !drawer.isDrawerOpen(GravityCompat.START)){
                    mListener.onDrawerOpened();
                }
                else if (newState == DrawerLayout.STATE_SETTLING && drawer.isDrawerOpen(GravityCompat.START)){
                    mListener.onDrawerClosed();
                }
                else if (!drawer.isDrawerOpen(GravityCompat.START)){
                    mListener.onDrawerClosed();
                }
                else if (drawer.isDrawerOpen(GravityCompat.START)){
                    mListener.onDrawerOpened();
                }

            }
        });



        if (navigationView != null) {
            setupDrawerContent(navigationView);
        }


    }

    private void setupDrawerContent(NavigationView navigationView) {
        //revision: this don't works, use setOnChildClickListener() and setOnGroupClickListener() above instead
        navigationView.setNavigationItemSelectedListener(
                new NavigationView.OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(MenuItem menuItem) {
                        menuItem.setChecked(true);
                        drawer.closeDrawers();
                        return true;
                    }
                });
    }

    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle beezcovery_navigation view item clicks here.
        int id = item.getItemId();

        DrawerLayout drawer = view.findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }


    public boolean closeDrawer(){

        if (drawer.isDrawerOpen(GravityCompat.START)){
            drawer.closeDrawers();
            return true;
        }

        return false;
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnCreditInteractionListener) {
            mListener = (OnCreditInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnCreditInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    public interface OnCreditInteractionListener {
        void onDrawerOpened();
        void onDrawerClosed();
    }
}
