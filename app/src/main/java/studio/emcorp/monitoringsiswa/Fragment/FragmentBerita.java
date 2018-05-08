package studio.emcorp.monitoringsiswa.Fragment;

import android.app.Dialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import studio.emcorp.monitoringsiswa.Library.JSONParser;
import studio.emcorp.monitoringsiswa.Library.Variable;
import studio.emcorp.monitoringsiswa.ListAdapter.ListBeritaAdapter;
import studio.emcorp.monitoringsiswa.R;

public class FragmentBerita extends Fragment {
    public static FragmentBerita newInstance(String param1, String param2) {
        FragmentBerita fragment = new FragmentBerita();
        return fragment;
    }
    ProgressBar pB;
    ListView list;
    List<String> IDBERITA = new ArrayList<String>();
    List<String> JUDULBERITA = new ArrayList<String>();
    List<String> ISI = new ArrayList<String>();
    List<String> GAMBAR = new ArrayList<String>();
    List<String> TANGGAL = new ArrayList<String>();
    SharedPreferences sharedpreferences;
    SharedPreferences.Editor editor;
    String idKelas = "",idSsiwa = "",nama = "", nis="", namaKelas="";
    final String[] bln = new String[] { "Jan", "Feb", "Mar", "Apr", "Mei", "Jun", "Jul", "Agu", "Sep", "Okt", "Nov", "Des" };
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_berita, container, false);
        pB = (ProgressBar)view.findViewById(R.id.pB);
        list = (ListView)view.findViewById(R.id.listView);
        ((AppCompatActivity)getActivity()).getSupportActionBar().setTitle("Berita");
        sharedpreferences = getActivity().getSharedPreferences(Variable.PREFERENCES_NAME, Context.MODE_PRIVATE);
        editor = sharedpreferences.edit();

        idKelas = sharedpreferences.getString(Variable.IDKELAS_KEY, null);
        idSsiwa = sharedpreferences.getString(Variable.IDSISWA_KEY, null);
        nama = sharedpreferences.getString(Variable.NAMA_KEY, null);
        nis = sharedpreferences.getString(Variable.NIS_KEY, null);
        namaKelas = sharedpreferences.getString(Variable.NAMAKELAS_KEY, null);

        if(isNetworkConnected()){
            cekBerita();
        }else {
            Toast.makeText(getContext(),"Internet tidak tersedia", Toast.LENGTH_SHORT).show();
        }

        return view;
    }

    public void cekBerita(){
        pB.setVisibility(View.VISIBLE);
        new CekAsync().execute(
                Variable.API_KEY,
                Variable.FUNGSI_BERITA
        );
    }

    class CekAsync extends AsyncTask<String, String, JSONObject> {
        JSONParser jsonParser = new JSONParser();
        private final String LOGIN_URL = Variable.BASE_URL;

        @Override
        protected void onPreExecute() {

        }

        @Override
        protected JSONObject doInBackground(String... args) {

            try {

                HashMap<String, String> params = new HashMap<>();
                params.put(Variable.PARAM_KEY, args[0]);
                params.put(Variable.PARAM_FUNCTION, args[1]);
                Log.d("request", "starting");

                JSONObject json = jsonParser.makeHttpRequest(
                        LOGIN_URL, "POST", params);

                if (json != null) {
                    Log.d("JSON result", json.toString());

                    return json;
                }

            } catch (Exception e) {
                e.printStackTrace();
            }

            return null;
        }

        protected void onPostExecute(JSONObject json) {
            if (json != null) {
//                Toast.makeText(getContext(),json.toString(),Toast.LENGTH_SHORT).show();
                try {
                    IDBERITA.clear();
                    JUDULBERITA.clear();
                    ISI.clear();
                    GAMBAR.clear();
                    TANGGAL.clear();
                    JSONArray jsonArray = json.getJSONArray("hasil");
                    if(jsonArray.length()==0){
                        Toast.makeText(getContext(),"Data tidak ada", Toast.LENGTH_SHORT).show();
                        list.setAdapter(null);
                    }else{
                        for (int i=0; i<jsonArray.length(); i++) {
                            JSONObject isiArray = jsonArray.getJSONObject(i);
                            String id_berita = isiArray.getString("id_berita");
                            String judul_berita = isiArray.getString("judul_berita");
                            String isi = isiArray.getString("isi");
                            String gambar = isiArray.getString("gambar");
                            String tanggal = isiArray.getString("tanggal");
                            String waktu = isiArray.getString("waktu");
                            IDBERITA.add(id_berita);
                            JUDULBERITA.add(judul_berita);
                            ISI.add(isi);
                            GAMBAR.add(gambar);
                            TANGGAL.add(tanggal+" "+waktu);
                        }
                        getAllData();
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                    Toast.makeText(getContext(), e.toString(), Toast.LENGTH_LONG).show();
                }
            }
            pB.setVisibility(View.INVISIBLE);
        }

    };

    public void getAllData(){
        list.setAdapter(null);
        ListBeritaAdapter adapter = new ListBeritaAdapter(getActivity(), IDBERITA,JUDULBERITA,ISI,GAMBAR,TANGGAL);
        list.setAdapter(adapter);
        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                showDetail(position);
//                Intent i = new Intent(getActivity(), BeritaActivity.class);
//                i.putExtra("judul",JUDULBERITA.get(position));
//                i.putExtra("isi",ISI.get(position));
//                i.putExtra("tgl",TANGGAL.get(position));
//                i.putExtra("gambar",GAMBAR.get(position));
//                i.putExtra("menu","Berita");
//                startActivity(i);
//                getActivity().finish();
            }
        });
        list.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {

                return true;
            }
        });
    }

    public void showDetail(int pos){
        final Dialog dialog = new Dialog(getContext());
        dialog.setContentView(R.layout.activity_berita);
        dialog.setTitle(null);
        dialog.setCancelable(false);
        dialog.setCanceledOnTouchOutside(false);

        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        lp.copyFrom(dialog.getWindow().getAttributes());
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
        lp.gravity = Gravity.CENTER;

        dialog.getWindow().setAttributes(lp);

        String judul = "",isi = "",tgl = "",gambar = "", menu = "";
        TextView tvJudul,tvIsi,tvTgl;
        ImageView imgGambar;

        tvJudul = (TextView)dialog.findViewById(R.id.tvJudul);
        tvIsi = (TextView)dialog.findViewById(R.id.tvIsi);
        tvTgl = (TextView)dialog.findViewById(R.id.tvTgl);
        imgGambar = (ImageView)dialog.findViewById(R.id.imgGambar);
        ImageView imgClose = (ImageView) dialog.findViewById(R.id.imgClose);

        judul = JUDULBERITA.get(pos);
        isi = ISI.get(pos);
        tgl = TANGGAL.get(pos);
        gambar = GAMBAR.get(pos);

        tvJudul.setText(judul);
        tvIsi.setText(isi);
        tvTgl.setText(tgl);
        Picasso.with(getActivity())
                .load(gambar)
                .error(R.drawable.ic_your_logo)
                .into(imgGambar);

        imgClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });

        dialog.show();
    }

    public boolean isNetworkConnected() {
        ConnectivityManager cm = (ConnectivityManager)getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);
        return cm.getActiveNetworkInfo() != null;
    }

}
