package unipu.hr.unibooking;

import androidx.appcompat.app.AppCompatActivity;


import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;

import android.content.Intent;
import android.os.Bundle;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import android.content.ClipData;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;

public class MojeRezervacijeActiviy extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    private DrawerLayout drawer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_moje_rezervacije_activiy);

        Toolbar toolbar = findViewById(R.id.toolbar2);
        setSupportActionBar(toolbar);

        drawer = findViewById(R.id.drawer_layoutMojeRezervacije);
        NavigationView navigationView =findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer, toolbar, R.string.open_nav_drawer, R.string.close_nav_drawer);
        drawer.addDrawerListener(toggle);
        toggle.syncState();
    }

    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
        switch (menuItem.getItemId()){
            case R.id.glavniizbornik:
                Intent intent = new Intent(MojeRezervacijeActiviy.this, ProfileActivity.class);
                startActivity(intent);
                break;
            case R.id.mojerezervacije:
                intent = new Intent(MojeRezervacijeActiviy.this, MojeRezervacijeActiviy.class);
                startActivity(intent);
                break;
            case R.id.rezerviraj:
                intent = new Intent(MojeRezervacijeActiviy.this, RezervirajActivity.class);
                startActivity(intent);
                break;
            case R.id.proslerezervacije:
                intent = new Intent(MojeRezervacijeActiviy.this, ProsleRezervacijeActivity.class);
                startActivity(intent);
                break;
            case R.id.odjava:
                FirebaseAuth.getInstance().signOut();
                intent = new Intent(MojeRezervacijeActiviy.this, MainActivity.class);
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
}
