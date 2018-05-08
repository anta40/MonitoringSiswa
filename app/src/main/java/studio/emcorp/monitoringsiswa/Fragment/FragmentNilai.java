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
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.Spinner;
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
import studio.emcorp.monitoringsiswa.R;

public class FragmentNilai extends Fragment {
    public static FragmentNilai newInstance(String param1, String param2) {
        FragmentNilai fragment = new FragmentNilai();
        return fragment;
    }
    ProgressBar pB;
    TextView nilai_aqidah,nilai_fiqih,nilai_tafsir,nilai_tauhid,nilai_hafalan_quran,nilai_hafalan_hadist,nilai_bhs_arab,nilai_bhs_indo,nilai_bhs_inggris,nilai_ti,nilai_seni,nilai_mtk,nilai_pkn,nilai_ips,nilai_ipa,nilai_jasmani;
    SharedPreferences sharedpreferences;
    SharedPreferences.Editor editor;
    String idKelas = "",idSsiwa = "",nama = "", nis="", namaKelas="";
    Button btnCek;
    Spinner spinSemester;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_nilai, container, false);
        pB = (ProgressBar)view.findViewById(R.id.pB);
        btnCek = (Button)view.findViewById(R.id.btnCek);
        spinSemester = (Spinner) view.findViewById(R.id.spinSemester);
        nilai_aqidah = (TextView) view.findViewById(R.id.nilai_aqidah);
        nilai_fiqih = (TextView) view.findViewById(R.id.nilai_fiqih);
        nilai_tafsir = (TextView) view.findViewById(R.id.nilai_tafsir);
        nilai_tauhid = (TextView) view.findViewById(R.id.nilai_tauhid);
        nilai_hafalan_quran = (TextView) view.findViewById(R.id.nilai_hafalan_quran);
        nilai_hafalan_hadist = (TextView) view.findViewById(R.id.nilai_hafalan_hadist);
        nilai_bhs_arab = (TextView) view.findViewById(R.id.nilai_bhs_arab);
        nilai_bhs_indo = (TextView) view.findViewById(R.id.nilai_bhs_indo);
        nilai_bhs_inggris = (TextView) view.findViewById(R.id.nilai_bhs_inggris);
        nilai_ti = (TextView) view.findViewById(R.id.nilai_ti);
        nilai_seni = (TextView) view.findViewById(R.id.nilai_seni);
        nilai_mtk = (TextView) view.findViewById(R.id.nilai_mtk);
        nilai_pkn = (TextView) view.findViewById(R.id.nilai_pkn);
        nilai_ips = (TextView) view.findViewById(R.id.nilai_ips);
        nilai_ipa = (TextView) view.findViewById(R.id.nilai_ipa);
        nilai_jasmani = (TextView) view.findViewById(R.id.nilai_jasmani);
        ((AppCompatActivity)getActivity()).getSupportActionBar().setTitle("Nilai");
        sharedpreferences = getActivity().getSharedPreferences(Variable.PREFERENCES_NAME, Context.MODE_PRIVATE);
        editor = sharedpreferences.edit();

        idKelas = sharedpreferences.getString(Variable.IDKELAS_KEY, null);
        idSsiwa = sharedpreferences.getString(Variable.IDSISWA_KEY, null);
        nama = sharedpreferences.getString(Variable.NAMA_KEY, null);
        nis = sharedpreferences.getString(Variable.NIS_KEY, null);
        namaKelas = sharedpreferences.getString(Variable.NAMAKELAS_KEY, null);

        List<String> ListSemester = new ArrayList<String>();
        ListSemester.add("Pilih Semester");
        ListSemester.add("Semester 1");
        ListSemester.add("Semester 2");
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(getContext(), android.R.layout.simple_spinner_item, ListSemester);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinSemester.setAdapter(dataAdapter);

        btnCek.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(spinSemester.getSelectedItemPosition()>0){
                    if(isNetworkConnected()){
                        cekNilai();
                    }else {
                        Toast.makeText(getContext(),"Internet tidak tersedia", Toast.LENGTH_SHORT).show();
                    }
                }else{
                    Toast.makeText(getContext(),"Pilih semester terlebih dahulu !", Toast.LENGTH_SHORT).show();
                }
            }
        });



        return view;
    }

    public void cekNilai(){
        pB.setVisibility(View.VISIBLE);
        new CekAsync().execute(
                Variable.API_KEY,
                Variable.FUNGSI_NILAI,
                nis,
                String.valueOf(spinSemester.getSelectedItemPosition())
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
                params.put(Variable.PARAM_NIS, args[2]);
                params.put(Variable.PARAM_SEMESTER, args[3]);
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
                    JSONArray jsonArray = json.getJSONArray("hasil");
                    if(jsonArray.length()==0){
                        Toast.makeText(getContext(),"Data tidak ada", Toast.LENGTH_SHORT).show();
                        //list.setAdapter(null);
                    }else{
                        for (int i=0; i<jsonArray.length(); i++) {
                            JSONObject isiArray = jsonArray.getJSONObject(i);
                            String val_nilai_aqidah = isiArray.getString("nilai_aqidah");
                            String val_nilai_fiqih = isiArray.getString("nilai_fiqih");
                            String val_nilai_tafsir = isiArray.getString("nilai_tafsir");
                            String val_nilai_tauhid = isiArray.getString("nilai_tauhid");
                            String val_nilai_hafalan_quran = isiArray.getString("nilai_hafalan_quran");
                            String val_nilai_hafalan_hadist = isiArray.getString("nilai_hafalan_hadist");
                            String val_nilai_bhs_arab = isiArray.getString("nilai_bhs_arab");
                            String val_nilai_bhs_indo = isiArray.getString("nilai_bhs_indo");
                            String val_nilai_bhs_inggris = isiArray.getString("nilai_bhs_inggris");
                            String val_nilai_ti = isiArray.getString("nilai_ti");
                            String val_nilai_seni = isiArray.getString("nilai_seni");
                            String val_nilai_mtk = isiArray.getString("nilai_mtk");
                            String val_nilai_pkn = isiArray.getString("nilai_pkn");
                            String val_nilai_ips = isiArray.getString("nilai_ips");
                            String val_nilai_ipa = isiArray.getString("nilai_ipa");
                            String val_nilai_jasmani = isiArray.getString("nilai_jasmani");

                            nilai_aqidah.setText(val_nilai_aqidah);
                            nilai_fiqih.setText(val_nilai_fiqih);
                            nilai_tafsir.setText(val_nilai_tafsir);
                            nilai_tauhid.setText(val_nilai_tauhid);
                            nilai_hafalan_quran.setText(val_nilai_hafalan_quran);
                            nilai_hafalan_hadist.setText(val_nilai_hafalan_hadist);
                            nilai_bhs_arab.setText(val_nilai_bhs_arab);
                            nilai_bhs_indo.setText(val_nilai_bhs_indo);
                            nilai_bhs_inggris.setText(val_nilai_bhs_inggris);
                            nilai_ti.setText(val_nilai_ti);
                            nilai_seni.setText(val_nilai_seni);
                            nilai_mtk.setText(val_nilai_mtk);
                            nilai_pkn.setText(val_nilai_pkn);
                            nilai_ips.setText(val_nilai_ips);
                            nilai_ipa.setText(val_nilai_ipa);
                            nilai_jasmani.setText(val_nilai_jasmani);
                        }
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                    Toast.makeText(getContext(), e.toString(), Toast.LENGTH_LONG).show();
                }
            }
            pB.setVisibility(View.GONE);
        }

    };

    public boolean isNetworkConnected() {
        ConnectivityManager cm = (ConnectivityManager)getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);
        return cm.getActiveNetworkInfo() != null;
    }

}
