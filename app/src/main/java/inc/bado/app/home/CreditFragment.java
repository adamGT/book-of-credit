package inc.bado.app.home;

import android.content.Context;
import android.content.DialogInterface;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
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
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.ExpandableListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.navigation.NavigationView;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import de.hdodenhof.circleimageview.CircleImageView;
import inc.bado.app.R;
import inc.bado.app.adapters.CreditListAdapter;
import inc.bado.app.adapters.CustomAutoCompleteAdapter;
import inc.bado.app.models.Credit;
import inc.bado.app.models.General;
import inc.bado.app.models.User;
import inc.bado.app.storage.creditStorage.CreditViewModel;
import inc.bado.app.storage.generaStorage.GeneralViewModel;
import inc.bado.app.storage.userStorage.UserViewModel;

public class CreditFragment extends Fragment{


    @BindView(R.id.app_bar) Toolbar toolbar;
    @BindView(R.id.drawer_layout) public DrawerLayout drawer;
    @BindView(R.id.credit_rv) RecyclerView recyclerView;

    @BindView(R.id.tot_credit) TextView totalCredit;

    private View view;
    private Context mContext;
    private TextView navUsername;
    private TextView navUserEmail;
    private CreditListAdapter adapter;

    private float totalCreditAmount;
    private User userData;
    private User creditedUser;
    private FirebaseDatabase database;

    private DatabaseReference myRef;
    private DatabaseReference myCreditRef;
    private DatabaseReference myDebitRef;
    private DatabaseReference myNotificationRef;
    List<User> users = new ArrayList<>();
    List<String> names = new ArrayList<>();

    private List<Credit> creditList = new ArrayList<>();
    private UserViewModel userViewModel;
    private CreditViewModel creditViewModel;
    private GeneralViewModel generalViewModel;
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


        setHasOptionsMenu(true);

        // Write a message to the database
        database = FirebaseDatabase.getInstance();
        myRef = database.getReference("shame");
        myCreditRef = database.getReference("shame/Credit");
        myDebitRef = database.getReference("shame/Debit");
        myNotificationRef = database.getReference("shame/Notifications");

        adapter = new CreditListAdapter(creditList,mContext);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.addItemDecoration(new DividerItemDecoration(getContext(), LinearLayoutManager.VERTICAL));
        recyclerView.setAdapter(adapter);

        new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0,
                ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {
            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
                if(direction == ItemTouchHelper.LEFT) {
                    Credit credit = adapter.getItemAt(viewHolder.getAdapterPosition());
                    creditViewModel.delete(credit);
                    generalViewModel.delete(new General(credit.getTitle(),credit.getName(),credit.getAmount(),credit.getTime(),true));
                }
            }
        }).attachToRecyclerView(recyclerView);

        fetchUserList();
        loadCreditData();
        setUpDrawer(view);
        return view;
    }


    public void setUserData(User user) {
        if(user != null){

            this.userData = user;
            navUsername.setText(userData.getName());
            navUserEmail.setText(userData.getEmail());

            Toast.makeText(mContext,"Welcome "+userData.getName(),Toast.LENGTH_SHORT).show();
        }else {
            Toast.makeText(mContext,"user is null",Toast.LENGTH_SHORT).show();
        }
    }

    private void fetchUserList(){
        myRef.child("users").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot dataSnap:dataSnapshot.getChildren()) {
//                    Toast.makeText(mContext,""+dataSnap.child("Name").getValue(),Toast.LENGTH_SHORT).show();
                    names.add(""+dataSnap.child("Name").getValue());
                    users.add(new User(dataSnap.getKey(),
                            ""+dataSnap.child("Name").getValue(),
                            ""+dataSnap.child("Email").getValue(),
                            ""+dataSnap.child("Token").getValue(),
                            null));

                }

                if(names != null){
//                    Toast.makeText(mContext,""+names.get(0),Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(mContext,"No users right now",Toast.LENGTH_SHORT).show();
                }
            }



            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void fetchAllCredit(){
        if (userData == null){
            Toast.makeText(mContext,"user data null on credit",Toast.LENGTH_SHORT).show();
            return;
        }
        if (myCreditRef.child(userData.getuID()) != null) {
            myCreditRef.child(userData.getuID()).addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    for (DataSnapshot dataSnap : dataSnapshot.getChildren()) {
//                    if(!new  String(""+dataSnap.child("Status").getValue()).equals("Pending")) {
                        add("" + dataSnap.child("Title").getValue(),
                                "" + dataSnap.child("CreditedTo").getValue(),
                                Float.parseFloat("" + dataSnap.child("Amount").getValue()) + 0,
                                new Date(Long.valueOf("" + dataSnap.child("CreditedAt").getValue())));
//                    }
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });
        }else {
            Toast.makeText(mContext,"Error fetching your credit list: its Null",Toast.LENGTH_SHORT).show();
        }

    }

    public void loadCreditData(){
        userViewModel = ViewModelProviders.of(this).get(UserViewModel.class);
        generalViewModel = ViewModelProviders.of(this).get(GeneralViewModel.class);
        creditViewModel = ViewModelProviders.of(this).get(CreditViewModel.class);
        creditViewModel.getAllCredits().observe(getViewLifecycleOwner(), new Observer<List<Credit>>() {
            @Override
            public void onChanged(List<Credit> credits) {
                totalCreditAmount = 0;
                if(credits.size() == 0 ){
                    fetchAllCredit();
                }

                adapter.addItems(credits);

                for (Credit credit:credits
                     ) {
                    totalCreditAmount += credit.getAmount();
                }

                totalCredit.setText(""+totalCreditAmount);
            }
        });
    }


    private static String changePinNumberFormat(String pinNumber){

        String pin= pinNumber;
        if(pin.equals("")){
            return pin;
        }else {

            if (pin.length() >= 11) {
                pin = pin.substring(0, 3) + " " + pin.substring(3, 6) + " " + pin.substring(6, 10) + " " + pin.substring(10, pin.length());
            } else {
                pin = "ERROR";
            }
            return pin;
        }
    }

    public void addCredit(){

        MaterialAlertDialogBuilder creditDialog = new MaterialAlertDialogBuilder(mContext);
        LayoutInflater inflater = getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.add_credit_layout, null);
        creditDialog.setView(dialogView);

        TextView titleText = dialogView.findViewById(R.id.dialog_title);
        titleText.setText(mContext.getString(R.string.bado_credit_dialog_title));

        TextInputLayout titleLayout = dialogView.findViewById(R.id.title_text_input);
        EditText title = dialogView.findViewById(R.id.title_edit_text);
        AutoCompleteTextView creditor = dialogView.findViewById(R.id.creditor_user);
        TextInputLayout amountLayout = dialogView.findViewById(R.id.amount_text_input);
        EditText amount = dialogView.findViewById(R.id.amount_edit_text);

        MaterialButton add = dialogView.findViewById(R.id.add_button);

        AlertDialog dialog = creditDialog.create();


        CustomAutoCompleteAdapter adapter = new CustomAutoCompleteAdapter(mContext,users);
        creditor.setAdapter(adapter);

        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                long createdAtMills = Calendar.getInstance().getTimeInMillis();
                if(userExist(creditor.getText().toString().trim())){
                    if(!amount.getText().toString().trim().isEmpty()) {
                        if(!title.getText().toString().trim().isEmpty()) {

                            Map<String, String> credit = new HashMap<String, String>();
                            credit.put("Title", title.getText().toString().trim());
                            credit.put("Amount", amount.getText().toString().trim());
                            credit.put("CreditedTo", creditedUser.getName());
                            credit.put("CreditedAt", ""+createdAtMills);
                            credit.put("Status", "Pending");

                            addCredit(credit);
                            addToNotification(amount.getText().toString().trim(),""+createdAtMills);
                            Toast.makeText(mContext,"a confirmation has been sent to "+creditedUser.getName(),Toast.LENGTH_SHORT).show();
                            dialog.dismiss();
                        }else {
                            titleLayout.setError("Please Enter a Title for the credit");
                        }
                    }else {
                        amountLayout.setError("Please Enter the amount of the credit");
                    }
                }else {
                        Toast.makeText(mContext,"Please enter a correct user to credit to",Toast.LENGTH_SHORT).show();
                }
            }
        });

        dialog.show();
    }

    private boolean userExist(String userName){
        boolean exists = false;
        for (User user:users) {
            if(user.getName().equals(userName)){
                creditedUser = user;
                exists = true;
            }
        }
        return exists;
    }


    public void add(String title, String name, float amount,Date createdAt){
        creditViewModel.insert(new Credit(title,name, amount,createdAt));
        generalViewModel.insert(new General(title,name, amount,createdAt,true));
    }

    private void addCredit(Map credit){
        DatabaseReference creditRef = myCreditRef.child(userData.getuID()).push();
        creditRef.setValue(credit);
    }

    private void addToNotification(String amount,String createdAtMills){
        DatabaseReference notificationRef = myNotificationRef.push();
        Map<String, String> credit = new HashMap<String, String>();
        credit.put("Creditor", userData.getName());
        credit.put("Amount", amount);
        credit.put("CreditedTo", creditedUser.getuID());
        credit.put("token", creditedUser.getToken());
        credit.put("RequestedAt", createdAtMills);
        notificationRef.setValue(credit);

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
//        void creditAdded(Map credit, String creditedUId);
        void onDrawerOpened();
        void onDrawerClosed();
        void onLogout();
    }
}
