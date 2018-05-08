package studio.emcorp.monitoringsiswa;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.TextView;

public class AgendaActivity extends AppCompatActivity {
    String judul = "",isi = "",tgl_mulai = "",tgl_selesai = "",tgl_posting = "",tempat = "",jam = "",keterangan = "",menu = "";
    TextView tvJudul,tvIsi,tvTglMulai,tvTglSelesai,tvTglPosting,tvTglTempat,tvTglJam,tvTglKeterangan;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_agenda);
        getSupportActionBar().setTitle("Agenda");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        tvJudul = (TextView)findViewById(R.id.tvJudul);
        tvIsi = (TextView)findViewById(R.id.tvIsi);
        tvTglMulai = (TextView)findViewById(R.id.tvTglMulai);
        tvTglSelesai = (TextView)findViewById(R.id.tvTglSelesai);
        tvTglPosting = (TextView)findViewById(R.id.tvTglPosting);
        tvTglTempat = (TextView)findViewById(R.id.tvTempat);
        tvTglJam = (TextView)findViewById(R.id.tvJam);
        tvTglKeterangan = (TextView)findViewById(R.id.tvKeterangan);

        judul = getIntent().getStringExtra("judul");
        isi = getIntent().getStringExtra("isi");
        tgl_mulai = getIntent().getStringExtra("tgl_mulai");
        tgl_selesai = getIntent().getStringExtra("tgl_selesai");
        tgl_posting = getIntent().getStringExtra("tgl_posting");
        tempat = getIntent().getStringExtra("tempat");
        jam = getIntent().getStringExtra("jam");
        keterangan = getIntent().getStringExtra("keterangan");
        menu = getIntent().getStringExtra("menu");

        tvJudul.setText(judul);
        tvIsi.setText(isi);
        tvTglMulai.setText(tgl_mulai);
        tvTglSelesai.setText(tgl_selesai);
        tvTglPosting.setText(tgl_posting);
        tvTglTempat.setText(tempat);
        tvTglJam.setText(jam);
        tvTglKeterangan.setText(keterangan);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                Intent i = new Intent(AgendaActivity.this,MainActivity.class);
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
        Intent i = new Intent(AgendaActivity.this,MainActivity.class);
        i.putExtra("menu",menu);
        startActivity(i);
        finish();
    }
}
