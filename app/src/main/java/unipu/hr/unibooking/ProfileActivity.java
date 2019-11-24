package unipu.hr.unibooking;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.content.ClipData;
import android.content.Intent;
import android.os.Bundle;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class ProfileActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    private DrawerLayout drawer;

    CalendarView kalendar;
    Spinner razlog;
    TextView userEmailSpremi;
    EditText userText;
    Button Spremi;

    FirebaseAuth firebaseAuth;
    FirebaseUser firebaseUser;
    DatabaseReference reff;
    Rezervacija rezervacija;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        kalendar = findViewById(R.id.kalendarRezervacije);
        razlog = findViewById(R.id.razlogSpinner);
        userEmailSpremi = findViewById(R.id.userEmailSpremi);
        userText = findViewById(R.id.userText);
        Spremi = findViewById(R.id.btnSave);


        List<String> categories = new ArrayList<String>();
        categories.add("Ispis kolegija");
        categories.add("Upis na studij");

        // Creating adapter for spinner
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, categories);

        // Drop down layout style - list view with radio button
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        // attaching data adapter to spinner
        razlog.setAdapter(dataAdapter);

        razlog.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                // your code here
                // On selecting a spinner item
                 rezervacija.setRazlog(parentView.getItemAtPosition(position).toString());
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
                // your code here
            }
        });


        firebaseAuth = FirebaseAuth.getInstance();
        firebaseUser = firebaseAuth.getCurrentUser();
        rezervacija = new Rezervacija();
        reff = FirebaseDatabase.getInstance().getReference().child("Rezervacije");

        Spremi.setOnClickListener(new View.OnClickListener() {
            public void onClick (View view) {
                //int a = Integer.parseInt(datum.getTex...)
                // rezervacija.setDatum(a);
                Calendar cl = Calendar.getInstance();
                cl.setTimeInMillis(kalendar.getDate());
                String date = "" + cl.get(Calendar.DAY_OF_MONTH) + "." + cl.get(Calendar.MONTH) + "." + cl.get(Calendar.YEAR);
                //String time = "" + cl.get(Calendar.HOUR_OF_DAY) + ":" + cl.get(Calendar.MINUTE) + ":" + cl.get(Calendar.SECOND);

                rezervacija.setDatum(date);
                rezervacija.setEmailUsera(userEmailSpremi.getText().toString().trim());
                rezervacija.setUserTekst(userText.getText().toString().trim());
                reff.push().setValue(rezervacija);
            }
        });


        userEmailSpremi.setText(firebaseUser.getEmail());


        drawer = findViewById(R.id.drawer_layout);
        NavigationView navigationView =findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer, toolbar, R.string.open_nav_drawer, R.string.close_nav_drawer);
        drawer.addDrawerListener(toggle);
        toggle.syncState();
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
        switch (menuItem.getItemId()){
            case R.id.glavniizbornik:
                Intent intent = new Intent(ProfileActivity.this, ProfileActivity.class);
                startActivity(intent);
                break;
            case R.id.odjava:
                FirebaseAuth.getInstance().signOut();
                intent = new Intent(ProfileActivity.this, MainActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
                break;

        }

        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void onBackPressed() {
        if(drawer.isDrawerOpen(GravityCompat.START)){
            drawer.closeDrawer(GravityCompat.START);
        }else {
            super.onBackPressed();
        }
    }

}
