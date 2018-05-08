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
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Hashtable;

import studio.emcorp.monitoringsiswa.Library.JSONParser;
import studio.emcorp.monitoringsiswa.Library.Variable;
import studio.emcorp.monitoringsiswa.ListAdapter.ListPembayaranAdapter;
import studio.emcorp.monitoringsiswa.R;

public class FragmentPembayaran extends Fragment {
    public static FragmentPembayaran newInstance(String param1, String param2) {
        FragmentPembayaran fragment = new FragmentPembayaran();
        return fragment;
    }
    ProgressBar pB;
    ListView list;
    List<String> IDPEMBAYARAN = new ArrayList<String>();
    List<String> PERIODEPEMBAYARAN = new ArrayList<String>();
    List<String> JMLPEMBAYARAN = new ArrayList<String>();
    List<String> TGLPEMBAYARAN = new ArrayList<String>();

    // added by Andre
    List<String> BLNPEMBAYARAN = new ArrayList<String>();
    List<String> THNPEMBAYARAN = new ArrayList<String>();
    Hashtable<String, String> hBulan = new Hashtable<String, String>();

    SharedPreferences sharedpreferences;
    SharedPreferences.Editor editor;
    String idKelas = "",idSsiwa = "",nama = "", nis="", namaKelas="";
    Spinner spinTahun, spinBulan;
    Button btnCek;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_pembayaran, container, false);
        pB = (ProgressBar)view.findViewById(R.id.pB);
        list = (ListView)view.findViewById(R.id.listView);
        spinBulan = (Spinner)view.findViewById(R.id.spinBulan);
        spinTahun = (Spinner)view.findViewById(R.id.spinTahun);
        btnCek = (Button)view.findViewById(R.id.btnCek);
        ((AppCompatActivity)getActivity()).getSupportActionBar().setTitle("Pembayaran");
        sharedpreferences = getActivity().getSharedPreferences(Variable.PREFERENCES_NAME, Context.MODE_PRIVATE);
        editor = sharedpreferences.edit();

        hBulan.put("01", "JANUARI");
        hBulan.put("02", "FEBRUARI");
        hBulan.put("03", "MARET");
        hBulan.put("04", "APRIL");
        hBulan.put("05", "MEI");
        hBulan.put("06", "JUNI");
        hBulan.put("07", "JULI");
        hBulan.put("08", "AGUSTUS");
        hBulan.put("09", "SEPTEMBER");
        hBulan.put("10", "OKTOBER");
        hBulan.put("11", "NOVEMBER");
        hBulan.put("12", "DESEMBER");

        getBulanTahunTransaksi();

        idKelas = sharedpreferences.getString(Variable.IDKELAS_KEY, null);
        idSsiwa = sharedpreferences.getString(Variable.IDSISWA_KEY, null);
        nama = sharedpreferences.getString(Variable.NAMA_KEY, null);
        nis = sharedpreferences.getString(Variable.NIS_KEY, null);
        namaKelas = sharedpreferences.getString(Variable.NAMAKELAS_KEY, null);


        spinTahun.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if(i==0){
                    spinBulan.setSelection(0);
                    spinBulan.setEnabled(false);
                }else{
                    spinBulan.setSelection(0);
                    spinBulan.setEnabled(true);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        btnCek.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(isNetworkConnected()){
                    cekPembayaran();
                }else {
                    Toast.makeText(getContext(),"Internet tidak tersedia", Toast.LENGTH_SHORT).show();
                }
            }
        });



        return view;
    }

    public void cekPembayaran(){
        pB.setVisibility(View.VISIBLE);
        new CekAsync().execute(
                Variable.API_KEY,
                Variable.FUNGSI_PEMBAYARAN,
                nis,
                spinTahun.getSelectedItem().toString(),
                spinBulan.getSelectedItem().toString()
        );
    }

    // added by Andre
    public void getBulanTahunTransaksi(){
        pB.setVisibility(View.VISIBLE);
        new CekAsync1().execute(
                Variable.API_KEY,
                Variable.FUNGSI_TAHUNBULANPEMBAYARAN
        );


    }

    class CekAsync1 extends AsyncTask<String, String, JSONObject> {
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
                    THNPEMBAYARAN.clear();
                    BLNPEMBAYARAN.clear();

                    // added by Andre
                    THNPEMBAYARAN.add("Semua");
                    BLNPEMBAYARAN.add("Semua");
                    String tmpTahun = "", tmpBulan = "";

                    JSONArray jsonArray = json.getJSONArray("hasil");
                    if(jsonArray.length()==0){
                        Toast.makeText(getContext(),"Data tidak ada", Toast.LENGTH_SHORT).show();
                        list.setAdapter(null);
                    }else{
                        for (int i=0; i<jsonArray.length(); i++) {
                            JSONObject isiArray = jsonArray.getJSONObject(i);
                            String tgl_bayar = isiArray.getString("tgl_bayar");

                            tmpBulan = hBulan.get(tgl_bayar.substring(3,5));
                            tmpTahun = tgl_bayar.substring(6, 10);

                            if (!BLNPEMBAYARAN.contains(tmpBulan)){
                                BLNPEMBAYARAN.add(tmpBulan);
                            }

                            if(!THNPEMBAYARAN.contains(tmpTahun)){
                                THNPEMBAYARAN.add(tmpTahun);
                            }

                        }
                        ArrayAdapter<String> bulanAdapter = new ArrayAdapter<String>(getContext(), android.R.layout.simple_spinner_item, BLNPEMBAYARAN);
                        bulanAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                        spinBulan.setAdapter(bulanAdapter);

                        ArrayAdapter<String> tahunAdapter = new ArrayAdapter<String>(getContext(), android.R.layout.simple_spinner_item, THNPEMBAYARAN);
                        tahunAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                        spinTahun.setAdapter(tahunAdapter);
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                    Toast.makeText(getContext(), e.toString(), Toast.LENGTH_LONG).show();
                }
            }
            pB.setVisibility(View.GONE);
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
                params.put(Variable.PARAM_TAHUN, args[3]);
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
            if (json != null) {
//                Toast.makeText(getContext(),json.toString(),Toast.LENGTH_SHORT).show();
                try {
                    IDPEMBAYARAN.clear();
                    PERIODEPEMBAYARAN.clear();
                    JMLPEMBAYARAN.clear();

                    JSONArray jsonArray = json.getJSONArray("hasil");
                    if(jsonArray.length()==0){
                        Toast.makeText(getContext(),"Data tidak ada", Toast.LENGTH_SHORT).show();
                        list.setAdapter(null);
                    }else{
                        for (int i=0; i<jsonArray.length(); i++) {
                            JSONObject isiArray = jsonArray.getJSONObject(i);
                            String id_pembayaran = isiArray.getString("id_pembayaran");
                            String periode_bayar = isiArray.getString("periode_bayar");
                            String jml_bayar = isiArray.getString("jml_bayar");
                            String tgl_bayar = isiArray.getString("tgl_bayar");

                            IDPEMBAYARAN.add(id_pembayaran);
                            PERIODEPEMBAYARAN.add(periode_bayar);
                            JMLPEMBAYARAN.add(jml_bayar);
                            TGLPEMBAYARAN.add(tgl_bayar);
                        }
                       getAllData();
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                    Toast.makeText(getContext(), e.toString(), Toast.LENGTH_LONG).show();
                }
            }
            pB.setVisibility(View.GONE);
        }

    };

    public void getAllData(){
        list.setAdapter(null);
        ListPembayaranAdapter adapter = new ListPembayaranAdapter(getActivity(), IDPEMBAYARAN,PERIODEPEMBAYARAN,JMLPEMBAYARAN,TGLPEMBAYARAN);
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
