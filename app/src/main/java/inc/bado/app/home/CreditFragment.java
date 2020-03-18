package inc.bado.app.home;

import android.content.Context;
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

public class CreditFragment extends Fragment implements
        NavigationView.OnNavigationItemSelectedListener{


    @BindView(R.id.app_bar) Toolbar toolbar;
    @BindView(R.id.drawer_layout) public DrawerLayout drawer;
    @BindView(R.id.credit_rv) RecyclerView recyclerView;

    @BindView(R.id.tot_credit) TextView totalCredit;

    private View view;
//    private boolean haveData;
    private Context mContext;
    private CreditListAdapter adapter;

    private float totalCreditAmount;
    private User userData;
    private User creditedUser;
    private FirebaseDatabase database;

    private DatabaseReference myRef;
    private DatabaseReference myCreditRef;
    private DatabaseReference myDebitRef;
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

        // Write a message to the database
        database = FirebaseDatabase.getInstance();
        myRef = database.getReference("shame");
        myCreditRef = database.getReference("shame/Credit");
        myDebitRef = database.getReference("shame/Debit");

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
//        setUserData();
        setUpDrawer(view);
        return view;
    }

    public void setUserData(User user) {

//        this.userData = userViewModel.getAllUsers().getValue().get(0);

        if(user != null){
            this.userData = user;
            Toast.makeText(mContext,userData.getName(),Toast.LENGTH_SHORT).show();

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
                    users.add(new User(dataSnap.getKey(),""+dataSnap.child("Name").getValue(),""+dataSnap.child("Email").getValue(),null));

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
        myCreditRef.child(userData.getuID()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                List<Credit> credits = new ArrayList<>();
                for (DataSnapshot dataSnap:dataSnapshot.getChildren()) {
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
//        return haveData;

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
                Date createdAt = Calendar.getInstance().getTime();
                if(names.contains(creditor.getText().toString().trim())) {
                    creditedUser = users.get(names.indexOf(creditor.getText().toString().trim()));
                    if(!amount.getText().toString().trim().isEmpty()) {
                        if(!title.getText().toString().trim().isEmpty()) {
                            addToFirebase(title.getText().toString().trim(), creditor.getText().toString().trim(), amount.getText().toString().trim(),""+createdAtMills);
                            dialog.dismiss();
                        }else {
                            titleLayout.setError("Please Enter a Title for the credit");
                        }
                    }else {
                        amountLayout.setError("Please Enter the amount of the credit");
                    }
                }
                else {
                    Toast.makeText(mContext,"Please enter a correct user to credit to",Toast.LENGTH_SHORT).show();
                }
            }
        });

        dialog.show();
    }

    public void add(String title, String name, float amount,Date createdAt){
        creditViewModel.insert(new Credit(title,name, amount,createdAt));
        generalViewModel.insert(new General(title,name, amount,createdAt,true));
    }

    private void addToFirebase(String title, String name, String amount,String createdAtMills){
        DatabaseReference creditRef = myCreditRef.child(userData.getuID()).push();
        Map<String, String> credit = new HashMap<String, String>();
        credit.put("Title", title);
        credit.put("Amount", amount);
        credit.put("CreditedTo", name);
        credit.put("CreditedAt", createdAtMills);
        credit.put("Status", "Pending");
        creditRef.setValue(credit);


        DatabaseReference debitRef = myDebitRef.child(creditedUser.getuID()).push();
        Map<String, String> debit = new HashMap<String, String>();
        debit.put("Title", title);
        debit.put("Amount", amount);
        debit.put("DebitBy", userData.getName());
        debit.put("DebitedAt", createdAtMills);
        debit.put("Status", "Pending");
        debitRef.setValue(debit);

    }

    private void setUpDrawer(View view){

        final ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                getActivity(), drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();


        NavigationView navigationView = view.findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        View headerView = navigationView.getHeaderView(0);
        CircleImageView navUserImage = (CircleImageView) headerView.findViewById(R.id.nav_image);
        TextView navUsername = (TextView) headerView.findViewById(R.id.nav_name);
        TextView navUserEmail = (TextView) headerView.findViewById(R.id.nav_email);

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
