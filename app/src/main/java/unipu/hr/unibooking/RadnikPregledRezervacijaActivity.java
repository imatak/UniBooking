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
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class RadnikPregledRezervacijaActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener,View.OnClickListener {


    FirebaseAuth firebaseAuth;
    FirebaseUser firebaseUser;
    DatabaseReference reff;
    Rezervacija rezervacija;
    Toolbar toolbar1;
    private DrawerLayout drawer;

    EditText EnterTextPregledRadnik;
    TextView emailtab;

    private Button ButtonRadnikDanas;
    private Button ButtonRadnikSutra;
    private Button ButtonRadnikNaprijed;
    private Button ButtonRadnikTrazi;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_radnik_pregled_rezervacija);
        toolbar1 = findViewById(R.id.toolbarPregled);
        setSupportActionBar(toolbar1);

        ButtonRadnikDanas =findViewById(R.id.ButtonRadnikDanas);
        ButtonRadnikDanas.setOnClickListener(this);
        ButtonRadnikSutra =findViewById(R.id.ButtonRadnikSutra);
        ButtonRadnikSutra.setOnClickListener(this);
        ButtonRadnikNaprijed =findViewById(R.id.ButtonRadnikNaprijed);
        ButtonRadnikNaprijed.setOnClickListener(this);
        ButtonRadnikTrazi =findViewById(R.id.ButtonRadnikTrazi);
        ButtonRadnikTrazi.setOnClickListener(this);

        EnterTextPregledRadnik = findViewById(R.id.EnterTextPregledRadnik);
        emailtab = findViewById(R.id.emailtab);

        firebaseAuth = FirebaseAuth.getInstance();
        firebaseUser = firebaseAuth.getCurrentUser();
        rezervacija = new Rezervacija();
        reff = FirebaseDatabase.getInstance().getReference().child("Rezervacije");



        drawer = findViewById(R.id.drawer_layoutRadnikPregled2);
        NavigationView navigationView =findViewById(R.id.nav_viewRadnikPregled1);
        navigationView.setNavigationItemSelectedListener(this);

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer, toolbar1, R.string.open_nav_drawer, R.string.close_nav_drawer);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        Calendar c = Calendar.getInstance();
        c.set(Calendar.HOUR_OF_DAY, 0);
        c.set(Calendar.MINUTE, 0);
        c.set(Calendar.SECOND, 0);
        c.set(Calendar.MILLISECOND, 0);

        Date now = c.getTime();
        FillTable(now, "main");


    }

    public void FillTable(Date now, String Poz){

        ArrayList<MojeRezervacijeStudent> ListaMojihRezervacija = new ArrayList<>();
        ListView mListView = (ListView)findViewById(R.id.listViewRadnikPregled);
        MojeRezervacijeListAdapter adapter =new MojeRezervacijeListAdapter(this, R.layout.adapterviewlayout, ListaMojihRezervacija);
        mListView.setAdapter(adapter);
        SimpleDateFormat dateFormat= new SimpleDateFormat("dd.MM.yyyy");

        FirebaseDatabase.getInstance().getReference().child("Rezervacije")
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                            Rezervacija a = snapshot.getValue(Rezervacija.class);

                            String NDatum = a.getDatum();
                            String NTermin = a.getTermin();
                            String NEmail = a.getEmailUsera();
                            String NRazlog = a.getRazlog();
                            String NStatus = "Odobreno";
                            String NID = a.getID();

                            try {
                                Date d=dateFormat.parse(NDatum);
                                if (d.after(now) && Poz=="sve") {
                                        ListaMojihRezervacija.add(new MojeRezervacijeStudent(NTermin, NDatum, NStatus, NRazlog,  NID));
                                        emailtab.setText("Datum");

                                }
                                if(d.equals(now) && Poz!="sve"){
                                    ListaMojihRezervacija.add(new MojeRezervacijeStudent(NTermin, NEmail,NStatus, NRazlog, NID));
                                    emailtab.setText("Email");
                                }

                            }
                            catch(Exception e) {
                                //java.text.ParseException: Unparseable date: Geting error
                                System.out.println("Excep"+e);
                            }

                            adapter.notifyDataSetChanged();


                        }

                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                    }
                });
    }


    @Override
    public void onClick(View view)
    {
        Calendar c = Calendar.getInstance();
        switch (view.getId()) {
            case R.id.ButtonRadnikDanas:
                c.set(Calendar.HOUR_OF_DAY, 0);
                c.set(Calendar.MINUTE, 0);
                c.set(Calendar.SECOND, 0);
                c.set(Calendar.MILLISECOND, 0);

                Date now = c.getTime();

                FillTable(now, "danas");
                break;
            case R.id.ButtonRadnikSutra:
                Calendar.getInstance();
                c.set(Calendar.HOUR_OF_DAY, 1);
                c.set(Calendar.MINUTE, 1);
                c.set(Calendar.SECOND, 1);
                c.set(Calendar.MILLISECOND, 1);

                now = c.getTime();

                FillTable(now, "sutra");

                break;
            case R.id.ButtonRadnikNaprijed:
                c.set(Calendar.HOUR_OF_DAY, -1);
                c.set(Calendar.MINUTE, -1);
                c.set(Calendar.SECOND, -1);
                c.set(Calendar.MILLISECOND, -1);

                now = c.getTime();

                FillTable(now, "sve");

                break;
            case R.id.ButtonRadnikTrazi:
                SimpleDateFormat dateFormat= new SimpleDateFormat("dd.MM.yyyy");
                try {
                    c.setTime(dateFormat.parse(EnterTextPregledRadnik.getText().toString()));
                    now = c.getTime();
                    FillTable(now, "trazi");
                }
                catch(Exception e) {
                    //java.text.ParseException: Unparseable date: Geting error
                    System.out.println("Excep"+e);
                }
                break;
        }
    }



    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
        switch (menuItem.getItemId()){
            case R.id.glavniizbornikRadnik:
                Intent intent = new Intent(RadnikPregledRezervacijaActivity.this, RadnikDashboardActivity.class);
                startActivity(intent);
                break;
            case R.id.Pregledrezervacija:
                intent = new Intent(RadnikPregledRezervacijaActivity.this,RadnikPregledRezervacijaActivity.class);
                startActivity(intent);
                break;
            case R.id.Pretrazivanje:
                intent = new Intent(RadnikPregledRezervacijaActivity.this, RadnikPretrazivanjeActivity.class);
                startActivity(intent);
                break;
            case R.id.odjava:
                FirebaseAuth.getInstance().signOut();
                intent = new Intent(RadnikPregledRezervacijaActivity.this, MainActivity.class);
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
