package inc.bado.app.home;

import android.content.Context;
import android.content.DialogInterface;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AlertDialog;
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
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.navigation.NavigationView;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import inc.bado.app.R;
import inc.bado.app.adapters.DebitListAdapter;
import inc.bado.app.models.Credit;
import inc.bado.app.models.Debit;
import inc.bado.app.models.General;
import inc.bado.app.models.User;
import inc.bado.app.storage.debitStorage.DebitViewModel;
import inc.bado.app.storage.generaStorage.GeneralViewModel;
import inc.bado.app.storage.userStorage.UserViewModel;

public class DebitFragment extends Fragment implements
        NavigationView.OnNavigationItemSelectedListener{


    @BindView(R.id.app_bar) Toolbar toolbar;
    @BindView(R.id.drawer_layout) public DrawerLayout drawer;
    @BindView(R.id.debit_rv) RecyclerView recyclerView;

    @BindView(R.id.tot_debit) TextView totalDebit;

    private View view;
    private Context mContext;
    private TextView navUsername;
    private TextView navUserEmail;
    private DebitListAdapter adapter;

    private float totalDebitAmount;
    private User userData;
    private FirebaseDatabase database;

    private DatabaseReference myRef;
    private DatabaseReference myDebitRef;

    private List<Debit> debitList = new ArrayList<>();
    private UserViewModel userViewModel;
    private DebitViewModel debitViewModel;
    private GeneralViewModel generalViewModel;
    private OnDebitInteractionListener mListener;

    private DebitFragment() {
        // Required empty public constructor
    }

    public static DebitFragment newInstance() {
        DebitFragment fragment = new DebitFragment();
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
        view = inflater.inflate(R.layout.fragment_debit, container, false);
        ButterKnife.bind(this, view);

        mContext = getContext();

        // Write a message to the database
        database = FirebaseDatabase.getInstance();
        myDebitRef = database.getReference("shame/Debit");

        adapter = new DebitListAdapter(debitList, mContext);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.addItemDecoration(new DividerItemDecoration(getContext(), LinearLayoutManager.VERTICAL));
        recyclerView.setAdapter(adapter);

        loadDebitData();
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

    private void fetchAllDebit(){
        if(userData == null){
            return;
        }

        if (myDebitRef.child(userData.getuID()) != null){
            myDebitRef.child(userData.getuID()).addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    for (DataSnapshot dataSnap:dataSnapshot.getChildren()) {

    //                    if(!new  String(""+dataSnap.child("Status").getValue()).equals("Pending")) {
                            add("" + dataSnap.child("Title").getValue(),
                                    "" + dataSnap.child("DebitBy").getValue(),
                                    Float.parseFloat("" + dataSnap.child("Amount").getValue()) + 0);
    //                    }
                    }

                }



                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });
        }else {
            Toast.makeText(mContext,"Error fetching your debit list: its Null",Toast.LENGTH_SHORT).show();
        }

    }

    public void loadDebitData(){
        userViewModel = ViewModelProviders.of(this).get(UserViewModel.class);
        generalViewModel = ViewModelProviders.of(this).get(GeneralViewModel.class);
        debitViewModel = ViewModelProviders.of(this).get(DebitViewModel.class);
        debitViewModel.getAllDebits().observe(getViewLifecycleOwner(), new Observer<List<Debit>>() {
            @Override
            public void onChanged(List<Debit> debits) {
                totalDebitAmount = 0;
                if(debits.size() ==0){
                    fetchAllDebit();
                }
                adapter.addItems(debits);

                for (Debit debit:debits
                ) {
                    totalDebitAmount += debit.getAmount();
                }

                totalDebit.setText(""+totalDebitAmount);
            }
        });
    }

//    public void addDebit(){
//        MaterialAlertDialogBuilder debitDialog = new MaterialAlertDialogBuilder(mContext);
//        LayoutInflater inflater = getLayoutInflater();
//        View dialogView = inflater.inflate(R.layout.add_credit_layout, null);
//        debitDialog.setView(dialogView);
//
//        TextView titleText = dialogView.findViewById(R.id.dialog_title);
//        titleText.setText(mContext.getString(R.string.bado_debit_dialog_title));
//
//        TextInputLayout titleLayout = dialogView.findViewById(R.id.title_text_input);
//        EditText title = dialogView.findViewById(R.id.title_edit_text);
//        TextInputLayout nameLayout = dialogView.findViewById(R.id.name_text_input);
//        EditText name = dialogView.findViewById(R.id.name_edit_text);
//        TextInputLayout amountLayout = dialogView.findViewById(R.id.amount_text_input);
//        EditText amount = dialogView.findViewById(R.id.amount_edit_text);
//
//        MaterialButton add = dialogView.findViewById(R.id.add_button);
//
//        AlertDialog dialog = debitDialog.create();
//
//        add.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                add(title.getText().toString(),name.getText().toString(),Float.parseFloat(amount.getText().toString()));
//                dialog.dismiss();
//            }
//        });
//
//
//
//        dialog.show();
//    }

    public void add(String title,String name,float amount){
        Date createdAt = Calendar.getInstance().getTime();
        debitViewModel.insert(new Debit(title,name, amount,createdAt));
        generalViewModel.insert(new General(title,name, amount,createdAt,false));
//        debitList.add(new Debit(title,name, amount,createdAt));
//        adapter.notifyDataSetChanged();
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
        //revision: this don't works, use setOnChildClickListener() and setOnGroupClickListener() above instead
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
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle beezcovery_navigation view item clicks here.
        int id = item.getItemId();

        DrawerLayout drawer = view.findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
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
        if (context instanceof OnDebitInteractionListener) {
            mListener = (OnDebitInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnDebitInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    public interface OnDebitInteractionListener {
        void onDrawerOpened();
        void onDrawerClosed();
        void onLogout();
    }
}
