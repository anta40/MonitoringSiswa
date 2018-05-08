package studio.emcorp.monitoringsiswa;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

public class BeritaActivity extends AppCompatActivity {
    String judul = "",isi = "",tgl = "",gambar = "", menu = "";
    TextView tvJudul,tvIsi,tvTgl;
    ImageView imgGambar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_berita);

        getSupportActionBar().setTitle("Berita");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        tvJudul = (TextView)findViewById(R.id.tvJudul);
        tvIsi = (TextView)findViewById(R.id.tvIsi);
        tvTgl = (TextView)findViewById(R.id.tvTgl);
        imgGambar = (ImageView) findViewById(R.id.imgGambar);

        judul = getIntent().getStringExtra("judul");
        isi = getIntent().getStringExtra("isi");
        tgl = getIntent().getStringExtra("tgl");
        gambar = getIntent().getStringExtra("gambar");
        menu = getIntent().getStringExtra("menu");

        tvJudul.setText(judul);
        tvIsi.setText(isi);
        tvTgl.setText(tgl);
        Picasso.with(BeritaActivity.this)
                .load(gambar)
                .error(R.drawable.ic_your_logo)
                .into(imgGambar);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                Intent i = new Intent(BeritaActivity.this,MainActivity.class);
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
        Intent i = new Intent(BeritaActivity.this,MainActivity.class);
        i.putExtra("menu",menu);
        startActivity(i);
        finish();
    }
}
