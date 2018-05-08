package studio.emcorp.monitoringsiswa;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Html;
import android.view.MenuItem;
import android.widget.TextView;

public class PengumumanActivity extends AppCompatActivity {
    String judul = "",isi = "",tgl = "",penulis = "", menu = "";
    TextView tvJudul,tvIsi,tvTgl,tvPenulis;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pengumuman);
        getSupportActionBar().setTitle("Pengumuman");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        tvJudul = (TextView)findViewById(R.id.tvJudul);
        tvIsi = (TextView)findViewById(R.id.tvIsi);
        tvTgl = (TextView)findViewById(R.id.tvTgl);
        tvTgl = (TextView)findViewById(R.id.tvTgl);
        tvPenulis = (TextView)findViewById(R.id.tvPenulis);

        judul = getIntent().getStringExtra("judul");
        isi = getIntent().getStringExtra("isi");
        tgl = getIntent().getStringExtra("tgl");
        penulis = getIntent().getStringExtra("penulis");
        menu = getIntent().getStringExtra("menu");

        tvJudul.setText(judul);
        tvIsi.setText(Html.fromHtml(isi));
        tvTgl.setText(tgl);
        tvPenulis.setText(penulis);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                Intent i = new Intent(PengumumanActivity.this,MainActivity.class);
                i.putExtra("menu",menu);
                startActivity(i);
                finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onBackPressed() {
        Intent i = new Intent(PengumumanActivity.this,MainActivity.class);
        i.putExtra("menu",menu);
        startActivity(i);
        finish();
    }
}
