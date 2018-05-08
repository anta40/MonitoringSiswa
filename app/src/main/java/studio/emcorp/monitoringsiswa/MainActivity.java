package studio.emcorp.monitoringsiswa;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.messaging.FirebaseMessaging;

import org.json.JSONException;
import org.json.JSONObject;

import studio.emcorp.monitoringsiswa.Library.Variable;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {
    final String[] fragment_aktifitas={
            "studio.emcorp.monitoringsiswa.Fragment.FragmentBerita",
            "studio.emcorp.monitoringsiswa.Fragment.FragmentPengumuman",
            "studio.emcorp.monitoringsiswa.Fragment.FragmentAgenda",
            "studio.emcorp.monitoringsiswa.Fragment.FragmentPembayaran",
            "studio.emcorp.monitoringsiswa.Fragment.FragmentAbsensi",
            "studio.emcorp.monitoringsiswa.Fragment.FragmentNilai",
            "studio.emcorp.monitoringsiswa.Fragment.FragmentProfile",
            "studio.emcorp.monitoringsiswa.Fragment.FragmentHome",
            "studio.emcorp.monitoringsiswa.Fragment.FragmentKepegawaian"};
    TextView tvNama, tvKelas;
    String idKelas = "",idSsiwa = "",nama = "", nis="", namaKelas="";
    SharedPreferences sharedpreferences;
    SharedPreferences.Editor editor;
    Toolbar toolbar;
    private static final String TAG = MainActivity.class.getSimpleName();
    private BroadcastReceiver mRegistrationBroadcastReceiver;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        mRegistrationBroadcastReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                // checking for type intent filter
                if (intent.getAction().equals(NotificationConfig.REGISTRATION_COMPLETE)) {
                    // gcm successfully registered
                    // now subscribe to `global` topic to receive app wide notifications
                    FirebaseMessaging.getInstance().subscribeToTopic(NotificationConfig.TOPIC_NEWS);
                    Toast.makeText(getApplicationContext(), "Subscribed to " + NotificationConfig.TOPIC_NEWS, Toast.LENGTH_LONG).show();
                    displayFirebaseRegId();

                } else if (intent.getAction().equals(NotificationConfig.PUSH_NOTIFICATION)) {
                    // new push notification is received

                    String message = intent.getStringExtra("message");

                  //  Toast.makeText(getApplicationContext(), "New announcement: " + message, Toast.LENGTH_LONG).show();

                    JSONObject obj = null;
                    try {
                        obj = new JSONObject(message);
                     //   txtMessage.setText("Title: "+ obj.getString("title")+", message: "+obj.getString("message"));
                       Toast.makeText(getApplicationContext(), "Message: "+obj.get("message"), Toast.LENGTH_SHORT).show();
                    }
                    catch (JSONException je){

                    }
                    //txtMessage.setText(message);
                }
            }
        };

        displayFirebaseRegId();


        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        sharedpreferences = getSharedPreferences(Variable.PREFERENCES_NAME, Context.MODE_PRIVATE);
        editor = sharedpreferences.edit();

        idKelas = sharedpreferences.getString(Variable.IDKELAS_KEY, null);
        idSsiwa = sharedpreferences.getString(Variable.IDSISWA_KEY, null);
        nama = sharedpreferences.getString(Variable.NAMA_KEY, null);
        nis = sharedpreferences.getString(Variable.NIS_KEY, null);
        namaKelas = sharedpreferences.getString(Variable.NAMAKELAS_KEY, null);

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        View header=navigationView.getHeaderView(0);
        tvNama = (TextView)header.findViewById(R.id.tvNama);
        tvKelas = (TextView)header.findViewById(R.id.tvKelas);
        tvNama.setText(nama);
        tvKelas.setText(namaKelas);

        FragmentTransaction tx = getSupportFragmentManager().beginTransaction();
        String fromMenu = getIntent().getStringExtra("menu");
        if(fromMenu!=null) {
            if (fromMenu.equals("Agenda")) {
//                getSupportActionBar().setTitle("Agenda");
                tx.replace(R.id.main, Fragment.instantiate(MainActivity.this, fragment_aktifitas[2]));
                tx.addToBackStack(null);
                tx.commit();
                tx.addToBackStack(null);
            }else if (fromMenu.equals("Berita")) {
//                getSupportActionBar().setTitle("Berita");
                tx.replace(R.id.main, Fragment.instantiate(MainActivity.this, fragment_aktifitas[0]));
                tx.addToBackStack(null);
                tx.commit();
                tx.addToBackStack(null);
            }else if (fromMenu.equals("Pengumuman")) {
//                getSupportActionBar().setTitle("Pengumuman");
                tx.replace(R.id.main, Fragment.instantiate(MainActivity.this, fragment_aktifitas[1]));
                tx.addToBackStack(null);
                tx.commit();
                tx.addToBackStack(null);
            }
        }else{
//            getSupportActionBar().setTitle("Home");
            tx.replace(R.id.main, Fragment.instantiate(MainActivity.this, fragment_aktifitas[7]));
            tx.addToBackStack(null);
            tx.commit();
        }


    }

    // Fetches reg id from shared preferences
    // and displays on the screen
    private void displayFirebaseRegId() {
        SharedPreferences pref = getApplicationContext().getSharedPreferences(NotificationConfig.SHARED_PREF, 0);
        String regId = pref.getString("regId", null);

        Log.e(TAG, "Firebase reg id: " + regId);

        if (!TextUtils.isEmpty(regId))
            Toast.makeText(this, "Firebase Reg Id: " + regId, Toast.LENGTH_SHORT).show();
        else
            Toast.makeText(this, "Firebase Reg Id is not received yet!", Toast.LENGTH_SHORT).show();
    }


    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            if(String.valueOf(getSupportFragmentManager().getBackStackEntryCount()).equals("1")){
                menuExit();
            }else{
                getSupportFragmentManager().popBackStack();
            }
//            menuExit();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();

        // register GCM registration complete receiver
        LocalBroadcastManager.getInstance(this).registerReceiver(mRegistrationBroadcastReceiver,
                new IntentFilter(NotificationConfig.REGISTRATION_COMPLETE));

        // register new push message receiver
        // by doing this, the activity will be notified each time a new message arrives
        LocalBroadcastManager.getInstance(this).registerReceiver(mRegistrationBroadcastReceiver,
                new IntentFilter(NotificationConfig.PUSH_NOTIFICATION));

        // clear the notification area when the app is opened
        NotificationUtil.clearNotifications(getApplicationContext());
    }

    @Override
    protected void onPause() {
        LocalBroadcastManager.getInstance(this).unregisterReceiver(mRegistrationBroadcastReceiver);
        super.onPause();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_absensi) {
            FragmentTransaction tx = getSupportFragmentManager().beginTransaction();
            tx.replace(R.id.main, Fragment.instantiate(MainActivity.this, fragment_aktifitas[4]));
            tx.addToBackStack(null);
            tx.commit();
            tx.addToBackStack(null);
//            toolbar.setTitle("Absensi");
        } else if (id == R.id.nav_agenda) {
            FragmentTransaction tx = getSupportFragmentManager().beginTransaction();
            tx.replace(R.id.main, Fragment.instantiate(MainActivity.this, fragment_aktifitas[2]));
            tx.addToBackStack(null);
            tx.commit();
            tx.addToBackStack(null);
//            toolbar.setTitle("Agenda");
        } else if (id == R.id.nav_berita) {
            FragmentTransaction tx = getSupportFragmentManager().beginTransaction();
            tx.replace(R.id.main, Fragment.instantiate(MainActivity.this, fragment_aktifitas[0]));
            tx.addToBackStack(null);
            tx.commit();
            tx.addToBackStack(null);
//            toolbar.setTitle("Berita");
        } else if (id == R.id.nav_pengumuman) {
            FragmentTransaction tx = getSupportFragmentManager().beginTransaction();
            tx.replace(R.id.main, Fragment.instantiate(MainActivity.this, fragment_aktifitas[1]));
            tx.addToBackStack(null);
            tx.commit();
            tx.addToBackStack(null);
//            toolbar.setTitle("Pengumuman");
        } else if (id == R.id.nav_pembayaran) {
            FragmentTransaction tx = getSupportFragmentManager().beginTransaction();
            tx.replace(R.id.main, Fragment.instantiate(MainActivity.this, fragment_aktifitas[3]));
            tx.addToBackStack(null);
            tx.commit();
            tx.addToBackStack(null);
//            toolbar.setTitle("Pembayaran");
        } else if (id == R.id.nav_profile) {
            FragmentTransaction tx = getSupportFragmentManager().beginTransaction();
            tx.replace(R.id.main, Fragment.instantiate(MainActivity.this, fragment_aktifitas[6]));
            tx.addToBackStack(null);
            tx.commit();
            tx.addToBackStack(null);
//            toolbar.setTitle("Profil");
        } else if (id == R.id.nav_nilai) {
            FragmentTransaction tx = getSupportFragmentManager().beginTransaction();
            tx.replace(R.id.main, Fragment.instantiate(MainActivity.this, fragment_aktifitas[5]));
            tx.addToBackStack(null);
            tx.commit();
            tx.addToBackStack(null);
//            toolbar.setTitle("Nilai");
        } else if (id == R.id.nav_home) {
            FragmentTransaction tx = getSupportFragmentManager().beginTransaction();
            tx.replace(R.id.main, Fragment.instantiate(MainActivity.this, fragment_aktifitas[7]));
            tx.addToBackStack(null);
            tx.commit();
            tx.addToBackStack(null);
//            toolbar.setTitle("Home");
        } else if (id == R.id.nav_kepegawaian) {
            FragmentTransaction tx = getSupportFragmentManager().beginTransaction();
            tx.replace(R.id.main, Fragment.instantiate(MainActivity.this, fragment_aktifitas[8]));
            tx.addToBackStack(null);
            tx.commit();
            tx.addToBackStack(null);
//            toolbar.setTitle("Kepegawaian");
        } else if (id == R.id.nav_send) {

        } else if (id == R.id.nav_logout) {
            menuLogout();
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    public void menuLogout(){
        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
        builder.setTitle(R.string.app_name);
        builder.setIcon(R.drawable.ic_your_logo);
        builder.setMessage("Anda yakin ingin logout?");
        builder.setPositiveButton("Cancel",
                new DialogInterface.OnClickListener()
                {
                    public void onClick(DialogInterface dialog, int id)
                    {
                        dialog.cancel();
                    }
                });

        builder.setNegativeButton("Logout",
                new DialogInterface.OnClickListener()
                {
                    public void onClick(DialogInterface dialog, int id)
                    {
                        editor.remove(Variable.LOGIN_KEY);
                        editor.remove(Variable.IDKELAS_KEY);
                        editor.remove(Variable.IDSISWA_KEY);
                        editor.remove(Variable.NAMA_KEY);
                        editor.remove(Variable.NIS_KEY);
                        editor.remove(Variable.NAMAKELAS_KEY);
                        editor.commit();
                        Intent i = new Intent(MainActivity.this,LoginActivity.class);
                        startActivity(i);
                        finish();
                    }
                });

        builder.show();
    }

    public void menuExit(){
        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
        builder.setTitle(R.string.app_name);
        builder.setIcon(R.drawable.ic_your_logo);
        builder.setMessage("Anda yakin ingin keluar?");
        builder.setPositiveButton("Cancel",
                new DialogInterface.OnClickListener()
                {
                    public void onClick(DialogInterface dialog, int id)
                    {
                        dialog.cancel();
                    }
                });

        builder.setNegativeButton("Keluar",
                new DialogInterface.OnClickListener()
                {
                    public void onClick(DialogInterface dialog, int id)
                    {
                        System.exit(0);
                        finish();
                    }
                });

        builder.show();
    }
}
