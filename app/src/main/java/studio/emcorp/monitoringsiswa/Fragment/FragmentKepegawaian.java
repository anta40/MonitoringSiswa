package studio.emcorp.monitoringsiswa.Fragment;

import android.content.Context;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
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
import android.widget.ImageView;
import android.widget.ListView;
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


public class FragmentKepegawaian extends Fragment {
   public static FragmentKepegawaian newInstance(String param1, String param2) {
        FragmentKepegawaian fragment = new FragmentKepegawaian();
        return fragment;
   }
    ProgressBar pB;
    List<String> NAMA = new ArrayList<String>();
    List<String> NIP = new ArrayList<String>();
    List<String> MAPEL = new ArrayList<String>();
    List<String> STATUS = new ArrayList<String>();
    List<String> ALAMAT = new ArrayList<String>();
    List<String> PHONE = new ArrayList<String>();
    List<String> TGLLAHIR = new ArrayList<String>();
    TextView tvNama,tvNip,tvMapel,tvStatus,tvPhone,tvTglLahir, tvAlamat;
    TextView tNama, tNIP, tMapel, tStatus, tPhone, tTgl, tAlamat;
    ImageView imgPegawai;
    SharedPreferences sharedpreferences;
    SharedPreferences.Editor editor;
    String idKelas = "",idSsiwa = "",nama = "", nis="", namaKelas="", alamat="";
    Spinner spinGuru;
    Button btnCek;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_kepegawaian, container, false);
        pB = (ProgressBar)view.findViewById(R.id.pB);
        spinGuru = (Spinner)view.findViewById(R.id.spinGuru);
        btnCek = (Button)view.findViewById(R.id.btnCek);
        tvNama = (TextView) view.findViewById(R.id.tvNama);
        tvNip = (TextView) view.findViewById(R.id.tvNip);
        tvMapel = (TextView) view.findViewById(R.id.tvMapel);
        tvStatus = (TextView) view.findViewById(R.id.tvStatus);
        tvPhone = (TextView) view.findViewById(R.id.tvPhone);
        tvTglLahir = (TextView) view.findViewById(R.id.tvTglLahir);
        tvAlamat = (TextView) view.findViewById(R.id.tvAlamat);
        imgPegawai = (ImageView) view.findViewById(R.id.imgPegawai);

        tNama = (TextView) view.findViewById(R.id.tNama);
        tStatus = (TextView) view.findViewById(R.id.tStatus);
        tTgl = (TextView) view.findViewById(R.id.tTgl);
        tPhone = (TextView) view.findViewById(R.id.tPhone);
        tMapel = (TextView) view.findViewById(R.id.tMapel);
        tAlamat= (TextView) view.findViewById(R.id.tAlamat);
        tNIP = (TextView) view.findViewById(R.id.tNIP);

        tvNama.setVisibility(View.INVISIBLE);
        tvNip.setVisibility(View.INVISIBLE);
        tvMapel.setVisibility(View.INVISIBLE);
        tvStatus.setVisibility(View.INVISIBLE);
        tvPhone.setVisibility(View.INVISIBLE);
        tvTglLahir.setVisibility(View.INVISIBLE);
        tvAlamat.setVisibility(View.INVISIBLE);
        imgPegawai.setVisibility(View.INVISIBLE);

        tNama.setVisibility(View.INVISIBLE);
        tNIP.setVisibility(View.INVISIBLE);
        tMapel.setVisibility(View.INVISIBLE);
        tStatus.setVisibility(View.INVISIBLE);
        tPhone.setVisibility(View.INVISIBLE);
        tTgl.setVisibility(View.INVISIBLE);
        tAlamat.setVisibility(View.INVISIBLE);

        ((AppCompatActivity)getActivity()).getSupportActionBar().setTitle("Kepegawaian");
        sharedpreferences = getActivity().getSharedPreferences(Variable.PREFERENCES_NAME, Context.MODE_PRIVATE);
        editor = sharedpreferences.edit();

        idKelas = sharedpreferences.getString(Variable.IDKELAS_KEY, null);
        idSsiwa = sharedpreferences.getString(Variable.IDSISWA_KEY, null);
        nama = sharedpreferences.getString(Variable.NAMA_KEY, null);
        nis = sharedpreferences.getString(Variable.NIS_KEY, null);
        namaKelas = sharedpreferences.getString(Variable.NAMAKELAS_KEY, null);
        alamat = sharedpreferences.getString(Variable.ALAMAT_KEY, null);
//        List<String> ListGuru = new ArrayList<String>();
//        ListGuru.add("Pilih Guru");
//        ListGuru.add("Drs.Yudiana");
//        ListGuru.add("Denny Kusumawardhana");
//        ListGuru.add("Yudiana");
//        ArrayAdapter<String> guruAdapter = new ArrayAdapter<String>(getContext(), android.R.layout.simple_spinner_item, ListGuru);
//        guruAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
//        spinGuru.setAdapter(guruAdapter);


//       btnCek.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                if(spinGuru.getSelectedItemPosition()>0){
        btnCek.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                tvNama.setVisibility(View.VISIBLE);
                tvNip.setVisibility(View.VISIBLE);
                tvMapel.setVisibility(View.VISIBLE);
                tvStatus.setVisibility(View.VISIBLE);
                tvPhone.setVisibility(View.VISIBLE);
                tvTglLahir.setVisibility(View.VISIBLE);
                tvAlamat.setVisibility(View.VISIBLE);
                imgPegawai.setVisibility(View.VISIBLE);

                tNama.setVisibility(View.VISIBLE);
                tNIP.setVisibility(View.VISIBLE);
                tMapel.setVisibility(View.VISIBLE);
                tStatus.setVisibility(View.VISIBLE);
                tPhone.setVisibility(View.VISIBLE);
                tTgl.setVisibility(View.VISIBLE);
                tAlamat.setVisibility(View.VISIBLE);

                tvNama.setText(NAMA.get(spinGuru.getSelectedItemPosition()));
                tvNip.setText(NIP.get(spinGuru.getSelectedItemPosition()));
                tvMapel.setText(MAPEL.get(spinGuru.getSelectedItemPosition()));
                tvStatus.setText(STATUS.get(spinGuru.getSelectedItemPosition()));
                tvPhone.setText(PHONE.get(spinGuru.getSelectedItemPosition()));
                tvTglLahir.setText(TGLLAHIR.get(spinGuru.getSelectedItemPosition()));
                tvAlamat.setText(ALAMAT.get(spinGuru.getSelectedItemPosition()));
            }
        });

                    if(isNetworkConnected()){
                        cekPegawai();
                    }else {
                        Toast.makeText(getContext(),"Internet tidak tersedia", Toast.LENGTH_SHORT).show();
        }


//        spinGuru.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
//            public void onItemSelected(AdapterView<?> arg0, View view, int position, long id) {
//                tvNama.setText(NAMA.get(position));
//                tvNip.setText(MAPEL.get(position));
//                tvMapel.setText(MAPEL.get(position));
//                tvStatus.setText(STATUS.get(position));
//                tvPhone.setText(PHONE.get(position));
//                tvTglLahir.setText(TGLLAHIR.get(position));
//           }
//            public void onNothingSelected(AdapterView<?> arg0) {
//          }
//        });


        return view;
    }
    public void cekPegawai(){
        pB.setVisibility(View.VISIBLE);
        new CekAsync().execute(
                Variable.API_KEY,
                Variable.FUNGSI_KEPEGAWAIAN,
                String.valueOf(spinGuru.getSelectedItemPosition())

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
                params.put(Variable.PARAM_IDKEPEGAWAIAN, args[2]);
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
                    NAMA.clear();
                    NIP.clear();
                    MAPEL.clear();
                    STATUS.clear();
                    TGLLAHIR.clear();
                    ALAMAT.clear();
                   PHONE.clear();
                    JSONArray jsonArray = json.getJSONArray("hasil");
                    if(jsonArray.length()==0){
                        Toast.makeText(getContext(),"Data tidak ada", Toast.LENGTH_SHORT).show();
                        //list.setAdapter(null);
                    }else{
                        for (int i=0; i<jsonArray.length(); i++) {
                            JSONObject isiArray = jsonArray.getJSONObject(i);
                            String val_tvNama = isiArray.getString("Nama Pegawai");
                            String val_tvNip = isiArray.getString("NIP");
                            String val_tvMapel = isiArray.getString("Mata Pelajaran");
                            String val_tvStatus = isiArray.getString("Status");
                            String val_tvPhone = isiArray.getString("telepon");
                            String val_tvAlamat = isiArray.getString("alamat");
                            String val_tvTglLahir = isiArray.getString("Tempat/Tanggal Lahir");

                            NAMA.add(val_tvNama);
                            NIP.add(val_tvNip);
                            MAPEL.add(val_tvMapel);
                            STATUS.add(val_tvStatus);
                            TGLLAHIR.add(val_tvTglLahir);
                            PHONE.add(val_tvPhone);
                            ALAMAT.add(val_tvAlamat);

//                            Toast.makeText(getContext(), val_tvNama +" , "+ val_tvNip +" , "+val_tvMapel +" , "+ val_tvStatus +" , "+ val_tvTglLahir, Toast.LENGTH_LONG).show();

//                          tvNama.setText(val_tvNama);
//                          tvNip.setText(val_tvNip);
//                          tvMapel.setText(val_tvMapel);
//                          tvStatus.setText(val_tvStatus);
//                          tvPhone.setText(val_tvPhone);
//                          tvTglLahir.setText(val_tvTglLahir);
                        }
                        ArrayAdapter<String> guruAdapter = new ArrayAdapter<String>(getContext(), android.R.layout.simple_spinner_item, NAMA);
                        guruAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                        spinGuru.setAdapter(guruAdapter);
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

