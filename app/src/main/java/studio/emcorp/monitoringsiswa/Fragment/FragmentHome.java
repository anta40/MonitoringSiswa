package studio.emcorp.monitoringsiswa.Fragment;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import studio.emcorp.monitoringsiswa.Library.Variable;
import studio.emcorp.monitoringsiswa.LoginActivity;
import studio.emcorp.monitoringsiswa.R;

public class FragmentHome extends Fragment {
    public static FragmentHome newInstance(String param1, String param2) {
        FragmentHome fragment = new FragmentHome();
        return fragment;
    }
    ProgressBar pB;
    ListView list;
    LinearLayout btnAbsensi,btnNilai,btnPembayaran,btnAgenda,btnBerita,btnPengumuman,btnProfile,btnLogout,btnKepegawaian;
    ImageButton imgAbsensi,imgNilai,imgPembayaran,imgAgenda,imgBerita,imgPengumuman,imgProfile,imgLogout,imgKepegawaian;
    TextView tvAbsensi,tvNilai,tvPembayaran,tvAgenda,tvBerita,tvPengumuman,tvProfile,tvLogout,tvKepegawaian;
    SharedPreferences sharedpreferences;
    SharedPreferences.Editor editor;
    String idKelas = "",idSsiwa = "",nama = "", nis="", namaKelas="", tglLahir = "";
    Fragment frg = null;
    FragmentTransaction transaction = null;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);
        pB = (ProgressBar)view.findViewById(R.id.pB);
        list = (ListView)view.findViewById(R.id.listView);
        btnAbsensi = (LinearLayout)view.findViewById(R.id.btnAbsensi);
        btnNilai = (LinearLayout)view.findViewById(R.id.btnNilai);
        btnPembayaran = (LinearLayout)view.findViewById(R.id.btnPembayaran);
        btnAgenda = (LinearLayout)view.findViewById(R.id.btnAgenda);
        btnPengumuman = (LinearLayout)view.findViewById(R.id.btnPengumuman);
        btnProfile = (LinearLayout)view.findViewById(R.id.btnProfile);
        btnLogout = (LinearLayout)view.findViewById(R.id.btnLogout);
        btnKepegawaian = (LinearLayout)view.findViewById(R.id.btnKepegawaian);
        imgAbsensi = (ImageButton)view.findViewById(R.id.imgAbsensi);
        imgNilai = (ImageButton)view.findViewById(R.id.imgNilai);
        imgPembayaran = (ImageButton)view.findViewById(R.id.imgPembayaran);
        imgAgenda = (ImageButton)view.findViewById(R.id.imgAgenda);
        imgPengumuman = (ImageButton)view.findViewById(R.id.imgPengumuman);
        imgProfile = (ImageButton)view.findViewById(R.id.imgProfile);
        imgLogout = (ImageButton)view.findViewById(R.id.imgLogout);
        imgKepegawaian = (ImageButton)view.findViewById(R.id.imgKepegawaian);
        tvAbsensi = (TextView)view.findViewById(R.id.tvAbsensi);
        tvNilai = (TextView)view.findViewById(R.id.tvNilai);
        tvPembayaran = (TextView)view.findViewById(R.id.tvPembayaran);
        tvAgenda = (TextView)view.findViewById(R.id.tvAgenda);
        tvPengumuman = (TextView)view.findViewById(R.id.tvPengumuman);
        tvProfile = (TextView)view.findViewById(R.id.tvProfile);
        tvLogout = (TextView)view.findViewById(R.id.tvLogout);
        tvKepegawaian = (TextView)view.findViewById(R.id.tvKepegawaian);

        ((AppCompatActivity)getActivity()).getSupportActionBar().setTitle("Home");
        sharedpreferences = getActivity().getSharedPreferences(Variable.PREFERENCES_NAME, Context.MODE_PRIVATE);
        editor = sharedpreferences.edit();

        idKelas = sharedpreferences.getString(Variable.IDKELAS_KEY, null);
        idSsiwa = sharedpreferences.getString(Variable.IDSISWA_KEY, null);
        nama = sharedpreferences.getString(Variable.NAMA_KEY, null);
        nis = sharedpreferences.getString(Variable.NIS_KEY, null);
        namaKelas = sharedpreferences.getString(Variable.NAMAKELAS_KEY, null);
        tglLahir = sharedpreferences.getString(Variable.TGLLAHIR_KEY, null);

        transaction = getFragmentManager().beginTransaction();

        imgAbsensi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openFragment(new FragmentAbsensi());
            }
        });
        tvAbsensi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openFragment(new FragmentAbsensi());
            }
        });
        imgNilai.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openFragment(new FragmentNilai());
            }
        });
        tvNilai.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openFragment(new FragmentNilai());
            }
        });
        imgPembayaran.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openFragment(new FragmentPembayaran());
            }
        });
        tvPembayaran.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openFragment(new FragmentPembayaran());
            }
        });
        imgAgenda.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openFragment(new FragmentAgenda());
            }
        });
        tvAgenda.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openFragment(new FragmentAgenda());
            }
        });
        imgPengumuman.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openFragment(new FragmentPengumuman());
            }
        });
        tvPengumuman.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openFragment(new FragmentPengumuman());
            }
        });
        imgKepegawaian.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openFragment(new FragmentKepegawaian());
            }
        });
        tvKepegawaian.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openFragment(new FragmentKepegawaian());
            }
        });
        imgProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openFragment(new FragmentProfile());
            }
        });
        tvProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openFragment(new FragmentProfile());
            }
        });
        imgLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                menuLogout();
            }
        });
        tvLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                menuLogout();
            }
        });
        return view;
    }

    public void openFragment(Fragment frag){
        frg = frag;
        transaction.replace(R.id.main, frg);
        transaction.addToBackStack(null);
        transaction.commit();
        transaction.addToBackStack(null);
    }

    public void menuLogout(){
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
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
                        Intent i = new Intent(getActivity(),LoginActivity.class);
                        startActivity(i);
                        getActivity().finish();
                    }
                });

        builder.show();
    }
}
