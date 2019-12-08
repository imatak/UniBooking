package unipu.hr.unibooking;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class EditRezervacijaActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    private DrawerLayout drawer;


    CalendarView kalendar;
    Spinner termin;
    Spinner razlog;
    TextView userEmailSpremi;
    EditText userText;
    Button Spremi;
    Toolbar toolbar;
    ProgressBar progressBar;
    String curDate;
    Button izbrisi;

    TextView Teksttermin;
    TextView TekstRazlog;

    FirebaseAuth firebaseAuth;
    FirebaseUser firebaseUser;
    DatabaseReference reff;
    Rezervacija rezervacija;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_rezervacija);

        Intent i = getIntent();
        MojeRezervacijeStudent value = (MojeRezervacijeStudent) i.getSerializableExtra("Uredi");
        String ID_rezervacije = value.getID();

        Toolbar toolbar = findViewById(R.id.toolbar1);
        setSupportActionBar(toolbar);

        kalendar = findViewById(R.id.kalendarRezervacijeR);
        termin = findViewById(R.id.terminSpinnerR);
        razlog = findViewById(R.id.razlogSpinnerR);
        userEmailSpremi = findViewById(R.id.userEmailSpremiR);
        userText = findViewById(R.id.userTextR);
        progressBar = findViewById(R.id.progressBar_userR);
        Spremi = findViewById(R.id.btnSaveR);
        izbrisi = findViewById(R.id.btnDeleteR);

        firebaseAuth = FirebaseAuth.getInstance();
        firebaseUser = firebaseAuth.getCurrentUser();
        rezervacija = new Rezervacija();
        reff = FirebaseDatabase.getInstance().getReference().child("Rezervacije");

        List<String> categoriesTermin = new ArrayList<String>();
        categoriesTermin.add("12:00");
        categoriesTermin.add("12:10");
        categoriesTermin.add("12:20");
        categoriesTermin.add("12:30");
        categoriesTermin.add("12:40");
        categoriesTermin.add("12:50");
        categoriesTermin.add("13:00");
        categoriesTermin.add("13:10");
        categoriesTermin.add("13:20");
        categoriesTermin.add("13:30");
        categoriesTermin.add("13:40");
        categoriesTermin.add("13:50");

        List<String> categories = new ArrayList<String>();
        categories.add("Ispis kolegija");
        categories.add("Upis na studij");
        categories.add("Ispis potvrde");
        categories.add("Prebacivanje studija");

        // Creating adapter for spinner
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, categories);
        ArrayAdapter<String> dataAdapterTermin = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, categoriesTermin);

        // Drop down layout style - list view with radio button
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        dataAdapterTermin.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        // attaching data adapter to spinner
        razlog.setAdapter(dataAdapter);
        termin.setAdapter(dataAdapterTermin);

        int spinnerPositionRazlog = dataAdapter.getPosition(value.getRazlog());
        razlog.setSelection(spinnerPositionRazlog);
        //rezervacija.setRazlog(value.getRazlog());
        int spinnerPositionTermin = dataAdapterTermin.getPosition(value.getVrijeme());
        termin.setSelection(spinnerPositionTermin);
        SimpleDateFormat formatter1=new SimpleDateFormat("dd.MM.yyyy.");
        //rezervacija.setTermin(value.getVrijeme());
        Date date1 = new Date();
        try {
            date1=formatter1.parse(value.getDatum());
        } catch (ParseException e) {
            e.printStackTrace();
        }
        kalendar.setDate(date1.getTime());
        rezervacija.setDatum(value.getDatum());

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

        termin.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                // your code here
                // On selecting a spinner item
                rezervacija.setTermin(parentView.getItemAtPosition(position).toString());
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
                // your code here
            }
        });

        //kalendar listener za datum
        kalendar.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {

            @Override
            public void onSelectedDayChange(CalendarView view, int year, int month,
                                            int dayOfMonth) {
                String d = String.valueOf(dayOfMonth);
                String m = String.valueOf(month+1);
                String y = String.valueOf(year);
                curDate ="" + d + "." + m + "." + y + ".";
                rezervacija.setDatum(curDate);
                //String date = "" + cl.get(Calendar.DAY_OF_MONTH) + "." + cl.get(Calendar.MONTH) + "." + cl.get(Calendar.YEAR);
            }
        });


        Spremi.setOnClickListener(new View.OnClickListener() {
            public void onClick (View view) {
                // petlja za pretraživanje rezervacija za trenutno prijavljenog korisnika
                FirebaseDatabase.getInstance().getReference().child("Rezervacije")
                        .addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {
                                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                                    Rezervacija a = snapshot.getValue(Rezervacija.class);


                                    if (a.getID().equals(ID_rezervacije)) {

                                        //String key = snapshot.getKey();
                                        //rezervacija.setDatum(curDate);
                                        rezervacija.setEmailUsera(userEmailSpremi.getText().toString().trim());
                                        String x = rezervacija.getDatum();
                                        if(x != null && !x.isEmpty()){
                                            reff.child(a.getID()).child("datum").setValue(rezervacija.getDatum());
                                        }

                                        reff.child(a.getID()).child("razlog").setValue(rezervacija.getRazlog());
                                        reff.child(a.getID()).child("termin").setValue(rezervacija.getTermin());
                                    }
                                }

                                Intent intent = new Intent(EditRezervacijaActivity.this, ProfileActivity.class);
                                startActivity(intent);
                                Toast.makeText(EditRezervacijaActivity.this, "Termin je uspješno ažuriran!"
                                        , Toast.LENGTH_LONG).show();
                            }

                            @Override
                            public void onCancelled(DatabaseError databaseError) {
                            }
                        });
            }

        });

        izbrisi.setOnClickListener(new View.OnClickListener() {
            public void onClick (View view) {
                // petlja za pretraživanje rezervacija za trenutno prijavljenog korisnika
                FirebaseDatabase.getInstance().getReference().child("Rezervacije")
                        .addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {
                                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                                    Rezervacija a = snapshot.getValue(Rezervacija.class);


                                    if (a.getID().equals(value.getID())) {

                                        //String key = snapshot.getKey();
                                        reff.child(a.getID()).removeValue();
                                    }
                                }

                                Intent intent = new Intent(EditRezervacijaActivity.this, ProfileActivity.class);
                                startActivity(intent);
                                Toast.makeText(EditRezervacijaActivity.this, "Rezervacija je izbrisana!"
                                        , Toast.LENGTH_LONG).show();
                            }

                            @Override
                            public void onCancelled(DatabaseError databaseError) {
                            }
                        });
            }

        });


        userEmailSpremi.setText(firebaseUser.getEmail());


        drawer = findViewById(R.id.drawer_layoutRezerviraj);
        NavigationView navigationView =findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer, toolbar, R.string.open_nav_drawer, R.string.close_nav_drawer);
        drawer.addDrawerListener(toggle);
        toggle.syncState();
    }

    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
        switch (menuItem.getItemId()){
            case R.id.glavniizbornik:
                Intent intent = new Intent(EditRezervacijaActivity.this, ProfileActivity.class);
                startActivity(intent);
                break;
            case R.id.mojerezervacije:
                intent = new Intent(EditRezervacijaActivity.this, MojeRezervacijeActivity.class);
                startActivity(intent);
                break;
            case R.id.rezerviraj:
                intent = new Intent(EditRezervacijaActivity.this, RezervirajActivity.class);
                startActivity(intent);
                break;
            case R.id.proslerezervacije:
                intent = new Intent(EditRezervacijaActivity.this, ProsleRezervacijeActivity.class);
                startActivity(intent);
                break;
            case R.id.odjava:
                FirebaseAuth.getInstance().signOut();
                intent = new Intent(EditRezervacijaActivity.this, MainActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
                break;

        }
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

    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {

    }
}
