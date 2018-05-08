package studio.emcorp.monitoringsiswa.Fragment;

import android.app.Dialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.text.Html;
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

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import studio.emcorp.monitoringsiswa.Library.JSONParser;
import studio.emcorp.monitoringsiswa.Library.Variable;
import studio.emcorp.monitoringsiswa.ListAdapter.ListPengumumanAdapter;
import studio.emcorp.monitoringsiswa.R;

public class FragmentPengumuman extends Fragment {
    public static FragmentPengumuman newInstance(String param1, String param2) {
        FragmentPengumuman fragment = new FragmentPengumuman();
        return fragment;
    }
    ProgressBar pB;
    ListView list;
    List<String> IDPENGUMUMAN = new ArrayList<String>();
    List<String> JUDULPENGUMUMAN = new ArrayList<String>();
    List<String> ISI = new ArrayList<String>();
    List<String> PENULIS = new ArrayList<String>();
    List<String> TANGGAL = new ArrayList<String>();
    SharedPreferences sharedpreferences;
    SharedPreferences.Editor editor;
    String idKelas = "",idSsiwa = "",nama = "", nis="", namaKelas="";
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_pengumuman, container, false);
        pB = (ProgressBar)view.findViewById(R.id.pB);
        list = (ListView)view.findViewById(R.id.listView);
        ((AppCompatActivity)getActivity()).getSupportActionBar().setTitle("Pengumuman");
        sharedpreferences = getActivity().getSharedPreferences(Variable.PREFERENCES_NAME, Context.MODE_PRIVATE);
        editor = sharedpreferences.edit();

        idKelas = sharedpreferences.getString(Variable.IDKELAS_KEY, null);
        idSsiwa = sharedpreferences.getString(Variable.IDSISWA_KEY, null);
        nama = sharedpreferences.getString(Variable.NAMA_KEY, null);
        nis = sharedpreferences.getString(Variable.NIS_KEY, null);
        namaKelas = sharedpreferences.getString(Variable.NAMAKELAS_KEY, null);

        if(isNetworkConnected()){
            cekPengumuman();
        }else {
            Toast.makeText(getContext(),"Internet tidak tersedia", Toast.LENGTH_SHORT).show();
        }

        return view;
    }

    public void cekPengumuman(){
        pB.setVisibility(View.VISIBLE);
        new CekAsync().execute(
                Variable.API_KEY,
                Variable.FUNGSI_PENGUMUMAN
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
                    IDPENGUMUMAN.clear();
                    JUDULPENGUMUMAN.clear();
                    ISI.clear();
                    PENULIS.clear();
                    TANGGAL.clear();
                    JSONArray jsonArray = json.getJSONArray("hasil");
                    if(jsonArray.length()==0){
                        Toast.makeText(getContext(),"Data tidak ada", Toast.LENGTH_SHORT).show();
                        list.setAdapter(null);
                    }else{
                        for (int i=0; i<jsonArray.length(); i++) {
                            JSONObject isiArray = jsonArray.getJSONObject(i);
                            String id_pengumuman = isiArray.getString("id_pengumuman");
                            String judul_pengumuman = isiArray.getString("judul_pengumuman");
                            String isi = isiArray.getString("isi");
                            String penulis = isiArray.getString("penulis");
                            String tanggal = isiArray.getString("tanggal");
                            IDPENGUMUMAN.add(id_pengumuman);
                            JUDULPENGUMUMAN.add(judul_pengumuman);
                            ISI.add(isi);
                            PENULIS.add(penulis);
                            TANGGAL.add(tanggal);
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
        ListPengumumanAdapter adapter = new ListPengumumanAdapter(getActivity(), IDPENGUMUMAN,JUDULPENGUMUMAN,ISI,PENULIS,TANGGAL);
        list.setAdapter(adapter);
        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                showDetail(position);
//                Intent i = new Intent(getActivity(), PengumumanActivity.class);
//                i.putExtra("judul",JUDULPENGUMUMAN.get(position));
//                i.putExtra("isi",ISI.get(position));
//                i.putExtra("tgl",TANGGAL.get(position));
//                i.putExtra("penulis",PENULIS.get(position));
//                i.putExtra("menu","Pengumuman");
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
        dialog.setContentView(R.layout.activity_pengumuman);
        dialog.setTitle(null);
        dialog.setCancelable(false);
        dialog.setCanceledOnTouchOutside(false);

        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        lp.copyFrom(dialog.getWindow().getAttributes());
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
        lp.gravity = Gravity.CENTER;

        dialog.getWindow().setAttributes(lp);

        String judul = "",isi = "",tgl = "",penulis = "", menu = "";
        TextView tvJudul,tvIsi,tvTgl,tvPenulis;

        tvJudul = (TextView)dialog.findViewById(R.id.tvJudul);
        tvIsi = (TextView)dialog.findViewById(R.id.tvIsi);
        tvTgl = (TextView)dialog.findViewById(R.id.tvTgl);
        tvTgl = (TextView)dialog.findViewById(R.id.tvTgl);
        tvPenulis = (TextView)dialog.findViewById(R.id.tvPenulis);

        judul = JUDULPENGUMUMAN.get(pos);
        isi = ISI.get(pos);
        tgl = TANGGAL.get(pos);
        penulis = PENULIS.get(pos);

        tvJudul.setText(judul);
        tvIsi.setText(Html.fromHtml(isi));
        tvTgl.setText(tgl);
        tvPenulis.setText(penulis);
        ImageView imgClose = (ImageView) dialog.findViewById(R.id.imgClose);

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
