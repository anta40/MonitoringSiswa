package studio.emcorp.monitoringsiswa.Fragment;

import android.content.Context;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.Toast;
import android.widget.Button;
import android.widget.ArrayAdapter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import studio.emcorp.monitoringsiswa.Library.JSONParser;
import studio.emcorp.monitoringsiswa.Library.Variable;
import studio.emcorp.monitoringsiswa.ListAdapter.ListAbsensiAdapter;
import studio.emcorp.monitoringsiswa.R;

public class FragmentAbsensi extends Fragment {
    public static FragmentAbsensi newInstance(String param1, String param2) {
        FragmentAbsensi fragment = new FragmentAbsensi();
        return fragment;
    }
    ProgressBar pB;
    ListView list;

    List<String> ABSEN = new ArrayList<String>();
    List<String> NAMAKELAS = new ArrayList<String>();
    List<String> TANGGAL = new ArrayList<String>();
    Spinner spnBulan, spnTanggal;
    Button btnCari;
    SharedPreferences sharedpreferences;
    SharedPreferences.Editor editor;
    String idKelas = "",idSsiwa = "",nama = "", nis="", namaKelas="";
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_pengumuman, container, false);
        pB = (ProgressBar)view.findViewById(R.id.pB);
        list = (ListView)view.findViewById(R.id.listView);

        sharedpreferences = getActivity().getSharedPreferences(Variable.PREFERENCES_NAME, Context.MODE_PRIVATE);
        editor = sharedpreferences.edit();
        ((AppCompatActivity)getActivity()).getSupportActionBar().setTitle("Absensi");
        idKelas = sharedpreferences.getString(Variable.IDKELAS_KEY, null);
        idSsiwa = sharedpreferences.getString(Variable.IDSISWA_KEY, null);
        nama = sharedpreferences.getString(Variable.NAMA_KEY, null);
        nis = sharedpreferences.getString(Variable.NIS_KEY, null);
        namaKelas = sharedpreferences.getString(Variable.NAMAKELAS_KEY, null);
        spnBulan = (Spinner) view.findViewById(R.id.spinAbsenBulan);
        spnTanggal = (Spinner) view.findViewById(R.id.spinAbsenTanggal);
        btnCari = (Button) view.findViewById(R.id.btnCekAbsen);

        ArrayList<String> arrTanggal = new ArrayList<String>();
        arrTanggal.add("SEMUA");
        for (int x = 1; x <= 31; x++){
            arrTanggal.add(""+x);
        }

        ArrayList<String> arrBulan = new ArrayList<String>();
        arrBulan.add("SEMUA");
        arrBulan.add("Januari");
        arrBulan.add("Februari");
        arrBulan.add("Maret");
        arrBulan.add("April");
        arrBulan.add("Mei");
        arrBulan.add("Juni");
        arrBulan.add("Juli");
        arrBulan.add("Agustus");
        arrBulan.add("September");
        arrBulan.add("Oktober");
        arrBulan.add("November");
        arrBulan.add("Desember");

        ArrayAdapter adapterTanggal = new ArrayAdapter(getContext(), android.R.layout.simple_spinner_item, arrTanggal);
        adapterTanggal.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        ArrayAdapter adapterBulan = new ArrayAdapter(getContext(), android.R.layout.simple_spinner_item, arrBulan);
        adapterBulan.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        spnTanggal.setAdapter(adapterTanggal);
        spnBulan.setAdapter(adapterBulan);

        btnCari.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(isNetworkConnected()){
                   cariAbsensi();
                }else {
                    Toast.makeText(getContext(),"Internet tidak tersedia", Toast.LENGTH_SHORT).show();
                }

            }
        });

       // if(isNetworkConnected()){
        //    cekAbsensi();
        //}else {
        //    Toast.makeText(getContext(),"Internet tidak tersedia", Toast.LENGTH_SHORT).show();
        //}

        return view;
    }

    public void cariAbsensi() {
        pB.setVisibility(View.VISIBLE);

        String vTanggal = spnTanggal.getSelectedItem().toString();
        String vBulan = "" + spnBulan.getSelectedItemPosition();

   //     Toast.makeText(getContext(), "Tanggal: "+vTanggal+", bulan: "+vBulan, Toast.LENGTH_LONG).show();


        new CariAbsensiSync().execute(
                Variable.API_KEY,
                Variable.FUNGSI_CARI_ABSEN,
                nis,
                vTanggal,
                vBulan
        );

    }

    public void cekAbsensi(){
        pB.setVisibility(View.VISIBLE);
        new CekAsync().execute(
                Variable.API_KEY,
                Variable.FUNGSI_ABSEN,
                nis
        );
    }

    class CariAbsensiSync extends AsyncTask<String, String, JSONObject> {
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
                params.put(Variable.PARAM_NIS, args[2]);
                params.put(Variable.PARAM_TGL, args[3]);
                params.put(Variable.PARAM_BULAN, args[4]);
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
           if (json != null){

               try {
                   JSONArray obj2 = json.getJSONArray("hasil");

                   ABSEN.clear();
                   NAMAKELAS.clear();
                   TANGGAL.clear();

                   if (obj2.length() == 0){
                       Toast.makeText(getContext(), "Data kosong", Toast.LENGTH_LONG).show();
                       list.setAdapter(null);
                   }
                   else {
                       int len = obj2.length();

                       for (int x = 0; x < len; x++){
                           JSONObject ob = obj2.getJSONObject(x);
                           ABSEN.add(ob.getString("absen"));
                           NAMAKELAS.add(ob.getString("nama_kelas"));
                          TANGGAL.add(ob.getString("tanggal"));
                       }

                       getAllData();
                   }
               } catch (JSONException ex) {
                   Toast.makeText(getContext(), ex.toString(), Toast.LENGTH_LONG).show();
               }
           }
            pB.setVisibility(View.INVISIBLE);
        }

    };

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
                params.put(Variable.PARAM_NIS, args[2]);
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
                    ABSEN.clear();
                    NAMAKELAS.clear();
                    TANGGAL.clear();
                    JSONArray jsonArray = json.getJSONArray("hasil");
                    if(jsonArray.length()==0){
                        Toast.makeText(getContext(),"Data tidak ada", Toast.LENGTH_SHORT).show();
                        list.setAdapter(null);
                    }else{
                        for (int i=0; i<jsonArray.length(); i++) {
                            JSONObject isiArray = jsonArray.getJSONObject(i);
                            String absen = isiArray.getString("absen");
                            String nama_kelas = isiArray.getString("nama_kelas");
                            String tanggal = isiArray.getString("tanggal");
                            ABSEN.add(absen);
                            NAMAKELAS.add(nama_kelas);
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
        ListAbsensiAdapter adapter = new ListAbsensiAdapter(getActivity(), ABSEN,NAMAKELAS,TANGGAL);
        list.setAdapter(adapter);
        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

            }
        });
        list.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {

                return true;
            }
        });
    }



    public boolean isNetworkConnected() {
        ConnectivityManager cm = (ConnectivityManager)getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);
        return cm.getActiveNetworkInfo() != null;
    }

}
