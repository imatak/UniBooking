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

import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class RadnikPregledRezervacijaActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {


    FirebaseAuth firebaseAuth;
    FirebaseUser firebaseUser;
    DatabaseReference reff;
    Rezervacija rezervacija;
    Toolbar toolbar;
    private DrawerLayout drawer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_radnik_pregled_rezervacija);

        firebaseAuth = FirebaseAuth.getInstance();
        firebaseUser = firebaseAuth.getCurrentUser();
        rezervacija = new Rezervacija();
        reff = FirebaseDatabase.getInstance().getReference().child("Rezervacije");


        toolbar = findViewById(R.id.toolbarRadnik);
        setSupportActionBar(toolbar);

        drawer = findViewById(R.id.drawer_layoutRadnikPregled);
        NavigationView navigationView =findViewById(R.id.nav_viewRadnikPregled);
        navigationView.setNavigationItemSelectedListener(this);

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer, toolbar, R.string.open_nav_drawer, R.string.close_nav_drawer);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

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
