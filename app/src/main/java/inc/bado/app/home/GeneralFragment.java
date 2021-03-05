package inc.bado.app.home;

import android.content.Context;
import android.content.DialogInterface;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.navigation.NavigationView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import inc.bado.app.R;
import inc.bado.app.adapters.GeneralListAdapter;
import inc.bado.app.models.Debit;
import inc.bado.app.models.General;
import inc.bado.app.models.User;
import inc.bado.app.storage.generaStorage.GeneralViewModel;

public class GeneralFragment extends Fragment {


    @BindView(R.id.app_bar) Toolbar toolbar;
    @BindView(R.id.drawer_layout) public DrawerLayout drawer;
    @BindView(R.id.general_rv) RecyclerView recyclerView;

    @BindView(R.id.tot_general) TextView totalGeneral;

    private View view;
    private Context mContext;
    private TextView navUsername;

    private User userData;
    private TextView navUserEmail;
    private GeneralListAdapter adapter;
    private float totalGeneralAmount;

    private List<General> generalList = new ArrayList<>();
    private GeneralViewModel generalViewModel;
    private OnGeneralInteractionListener mListener;

    private GeneralFragment() {

    }

    public static GeneralFragment newInstance() {
        GeneralFragment fragment = new GeneralFragment();
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
        view = inflater.inflate(R.layout.fragment_general, container, false);
        ButterKnife.bind(this, view);

        mContext = getContext();

        adapter = new GeneralListAdapter(generalList,mContext);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.addItemDecoration(new DividerItemDecoration(getContext(), LinearLayoutManager.VERTICAL));
        recyclerView.setAdapter(adapter);

        loadGeneralData();
        setUpDrawer(view);
        return view;
    }

    public void setUserData(User user) {

        if(user != null){
            this.userData = user;
//            Toast.makeText(mContext,userData.getName(),Toast.LENGTH_SHORT).show();

            navUsername.setText(userData.getName());
            navUserEmail.setText(userData.getEmail());


        }else {
            Toast.makeText(mContext,"user is null",Toast.LENGTH_SHORT).show();
        }
    }

    public void loadGeneralData(){
        generalViewModel = ViewModelProviders.of(this).get(GeneralViewModel.class);
        generalViewModel.getAllDebits().observe(getViewLifecycleOwner(), new Observer<List<General>>() {
            @Override
            public void onChanged(List<General> generals) {
                totalGeneralAmount = 0;
                adapter.addItems(generals);

                for (General general:generals
                ) {
                    if(general.isCredit()) {
                        totalGeneralAmount += general.getAmount();
                    }else {
                        totalGeneralAmount -= general.getAmount();
                    }
                }

                totalGeneral.setText(""+totalGeneralAmount);
                if(totalGeneralAmount < 0){
                    totalGeneral.setTextColor(mContext.getResources().getColor(R.color.skip));
                }else {
                    totalGeneral.setTextColor(mContext.getResources().getColor(R.color.colorAccent));
                }
            }
        });
    }


    private void setUpDrawer(View view){

        final ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                getActivity(), drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);

        toggle.syncState();


        NavigationView navigationView = view.findViewById(R.id.nav_view);

        View headerView = navigationView.getHeaderView(0);
        navUsername = (TextView) headerView.findViewById(R.id.nav_name);
        navUserEmail = (TextView) headerView.findViewById(R.id.nav_email);

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
        navigationView.setNavigationItemSelectedListener(
                new NavigationView.OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(MenuItem menuItem) {
                        menuItem.setChecked(true);
                        drawer.closeDrawers();

                        int id = menuItem.getItemId();

                        if (id == R.id.nav_logout) {
                            logOut();
                        }

                        return true;
                    }
                });
    }

    public boolean closeDrawer(){

        if (drawer.isDrawerOpen(GravityCompat.START)){
            drawer.closeDrawers();
            return true;
        }

        return false;
    }

    private void logOut(){
        MaterialAlertDialogBuilder dialogBuilder = new MaterialAlertDialogBuilder(getContext());

        dialogBuilder.setTitle("Log Out").setMessage("Are you sure you want to logout?");
        dialogBuilder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                mListener.onLogout();

            }
        });
        dialogBuilder.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });

        dialogBuilder.show();

    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnGeneralInteractionListener) {
            mListener = (OnGeneralInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnGeneralInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }


    public interface OnGeneralInteractionListener {
        void onDrawerOpened();
        void onDrawerClosed();
        void onLogout();
    }
}
