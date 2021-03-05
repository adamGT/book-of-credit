package inc.bado.app.home;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.FirebaseMessaging.*;

import java.io.IOException;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import inc.bado.app.R;
import inc.bado.app.models.Credit;
import inc.bado.app.models.User;
import inc.bado.app.storage.creditStorage.CreditDao;
import inc.bado.app.storage.creditStorage.CreditRepository;
import inc.bado.app.utils.MyFirebaseMessagingService;
import inc.bado.app.welcome.WelcomeActivity;

public class HomeActivity extends AppCompatActivity implements
        BottomNavigationView.OnNavigationItemSelectedListener,
        CreditFragment.OnCreditInteractionListener,
        DebitFragment.OnDebitInteractionListener,
        GeneralFragment.OnGeneralInteractionListener,
        MyFirebaseMessagingService.OnTokenRefreshed{


    @BindView(R.id.navigation) BottomNavigationView navigation;
    @BindView(R.id.navigationLayout) ConstraintLayout navigationLayout;
    @BindView(R.id.fab_add) FloatingActionButton addButton;
    @BindView(R.id.progress_bar) ProgressBar progressBar;


    // List of Fragments shown in home activity
    private enum HomeFragments {
        Credit("Credit"),
        Debit("Debit"),
        General("General");

        private final String name;

        HomeFragments(String s) {
            name = s;
        }


        public String toString() {
            return this.name;
        }
    }


    private CreditFragment creditFragment = CreditFragment.newInstance();
    private DebitFragment debitFragment = DebitFragment.newInstance();
    private GeneralFragment generalFragment = GeneralFragment.newInstance();

    private MyFirebaseMessagingService fcmService;

    private FragmentManager fm;
    private Fragment currentActiveFragment;
    private Context mContext;

    private FirebaseDatabase database;
    private DatabaseReference myRef;
    private DatabaseReference myCreditRef;
    private FirebaseUser user;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        ButterKnife.bind(this);
        fm = getSupportFragmentManager();
        mContext = getApplicationContext();

        progressBar.setVisibility(View.INVISIBLE);

        fcmService = new MyFirebaseMessagingService();
        user = FirebaseAuth.getInstance().getCurrentUser();
        database = FirebaseDatabase.getInstance();

        myRef = database.getReference("shame");
        myCreditRef = database.getReference("shame/Credit");

        if (user !=null) {
            myRef.child("users").child(user.getUid()).addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
//                    Toast.makeText(mContext,""+dataSnapshot.child("Name").getValue(),Toast.LENGTH_LONG).show();
                    User userData = new User(user.getUid(),
                            ""+dataSnapshot.child("Name").getValue(),
                            ""+dataSnapshot.child("Email").getValue(),
                            ""+dataSnapshot.child("Token").getValue(),
                            null);
                    debitFragment.setUserData(userData);
                    creditFragment.setUserData(userData);
                    generalFragment.setUserData(userData);
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });

        }

        navigation.setOnNavigationItemSelectedListener(HomeActivity.this);

        addButton.setOnClickListener(v -> onAddClicked());


        fm.beginTransaction()
                .add(R.id.contentLayout, generalFragment, HomeFragments.General.name)
                .hide(generalFragment)
                .commit();

        fm.beginTransaction()
                .add(R.id.contentLayout, debitFragment, HomeFragments.Debit.name)
                .hide(debitFragment)
                .commit();

        fm.beginTransaction()
                .add(R.id.contentLayout, creditFragment, HomeFragments.Credit.name)
                .commit();

        currentActiveFragment = creditFragment;

        subscribeFCM();
        getAccessToken();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                if (fm.getBackStackEntryCount() > 0) {
                    fm.popBackStack();
                    if (fm.getBackStackEntryCount() == 1) {
                        showHomeFragments();
                    }
                } else {
                    onBackPressedOptions("options");
                }
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onBackPressed() {
        if (fm.getBackStackEntryCount() > 0) {
            fm.popBackStack();
            if (fm.getBackStackEntryCount() == 1) {
                showHomeFragments();
            }

        } else {

            onBackPressedOptions("device");
        }
    }

    private void subscribeFCM(){
        FirebaseMessaging.getInstance().subscribeToTopic("shame")
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (!task.isSuccessful()) {
                            Toast.makeText(mContext, "Failed to subscribe to Firebase", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    private void getAccessToken(){
        FirebaseInstanceId.getInstance().getInstanceId()
                .addOnCompleteListener(new OnCompleteListener<InstanceIdResult>() {
                    @Override
                    public void onComplete(@NonNull Task<InstanceIdResult> task) {
                        if (!task.isSuccessful()) {
                            Toast.makeText(mContext, "Faild to get AccessToken",Toast.LENGTH_SHORT).show();
                            return;
                        }

                        // Get new Instance ID token
                        String token = task.getResult().getToken();
                        saveToken(token);

                    }
                });
    }

    private void saveToken(String token){
        myRef.child("users").child(user.getUid()).child("Token").setValue(token);
    }

    private void onAddClicked(){
        if(currentActiveFragment == creditFragment){
            creditFragment.addCredit();
        }
    }

    private void showHomeFragments(){
        fm.beginTransaction()
                .show(currentActiveFragment)
                .commit();
    }

    @Override
    public void sendRegistrationToServer(String token) {
        saveToken(token);
    }

    @Override
    public void onDrawerOpened(){
        addButton.setVisibility(View.GONE);
        navigationLayout.setVisibility(View.GONE);
    }

    @Override
    public void onDrawerClosed(){
        if(currentActiveFragment == creditFragment){
            addButton.setVisibility(View.VISIBLE);
        }
        navigationLayout.setVisibility(View.VISIBLE);
    }

    @Override
    public void onLogout() {

        deleteAccessToken();

        Intent intent = new Intent(HomeActivity.this, WelcomeActivity.class);
        finishAffinity();
        startActivity(intent);


    }

    private void deleteAccessToken(){

        progressBar.setVisibility(View.GONE);
        FirebaseAuth.getInstance().signOut();
        new AsyncTask() {

            @Override
            protected Object doInBackground(Object[] objects) {
                try {
                    FirebaseInstanceId.getInstance().deleteToken(user.getUid(), FirebaseMessaging.INSTANCE_ID_SCOPE);
                    Log.e("TASK","deleteToken EXECUTED");
                } catch (IOException e) {
                    Log.e("ERROR",""+e.getMessage());
                    e.printStackTrace();
                }
                return null;
            }
        }.execute();

    }


    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {

        switch (item.getItemId()) {
            case R.id.navigation_credit:

                addButton.setVisibility(View.VISIBLE);

                fm.beginTransaction()
                        .hide(currentActiveFragment)
                        .show(creditFragment)
                        .commit();

                currentActiveFragment = creditFragment;

                return true;

            case R.id.navigation_debit:

                addButton.setVisibility(View.GONE);

                fm.beginTransaction()
                        .hide(currentActiveFragment)
                        .show(debitFragment)
                        .commit();

                currentActiveFragment = debitFragment;

                return true;

            case R.id.navigation_general:

                addButton.setVisibility(View.GONE);

                fm.beginTransaction()
                        .hide(currentActiveFragment)
                        .show(generalFragment)
                        .commit();

                currentActiveFragment = generalFragment;


                return true;
        }
        return false;
    }


    public void onBackPressedOptions(String option){

        if (currentActiveFragment == creditFragment){

            if(!creditFragment.closeDrawer()){
                super.onBackPressed();
            }

        } else if(currentActiveFragment == debitFragment){

            if(!debitFragment.closeDrawer()){
                super.onBackPressed();
            }

        } else if(currentActiveFragment == generalFragment){

            if(!debitFragment.closeDrawer()){
                super.onBackPressed();
            }

        }
    }

}
