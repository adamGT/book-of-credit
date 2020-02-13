package inc.bado.app.home;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import butterknife.BindView;
import butterknife.ButterKnife;
import inc.bado.app.R;

public class HomeActivity extends AppCompatActivity implements
        BottomNavigationView.OnNavigationItemSelectedListener,
        CreditFragment.OnCreditInteractionListener,
        DebitFragment.OnDebitInteractionListener,
        GeneralFragment.OnGeneralInteractionListener{


    @BindView(R.id.navigation) BottomNavigationView navigation;
    @BindView(R.id.navigationLayout) ConstraintLayout navigationLayout;
    @BindView(R.id.fab_add) FloatingActionButton addButton;


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

    // The fragment manager
    private FragmentManager fm;
    private Fragment currentActiveFragment;
    private Context mContext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        ButterKnife.bind(this);
        fm = getSupportFragmentManager();
        mContext = getApplicationContext();


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
                    onBackpressedOptions("options");
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

            onBackpressedOptions("device");
        }
    }




    public void onAddClicked(){
        if(currentActiveFragment == creditFragment){
            creditFragment.addCredit();
        }else if(currentActiveFragment == debitFragment){
            debitFragment.addDebit();
        }
    }

    public void showHomeFragments(){
        fm.beginTransaction()
                .show(currentActiveFragment)
                .commit();
    }


    @Override
    public void onDrawerOpened(){
        addButton.setVisibility(View.GONE);
        navigationLayout.setVisibility(View.GONE);
    }

    @Override
    public void onDrawerClosed(){
        addButton.setVisibility(View.GONE);
        navigationLayout.setVisibility(View.VISIBLE);
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

                addButton.setVisibility(View.VISIBLE);

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


    public void onBackpressedOptions(String option){

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
