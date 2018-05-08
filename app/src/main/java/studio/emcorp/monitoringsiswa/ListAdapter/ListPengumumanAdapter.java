package studio.emcorp.monitoringsiswa.ListAdapter;


import android.app.Activity;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import studio.emcorp.monitoringsiswa.R;

public class ListPengumumanAdapter extends ArrayAdapter<String> {
    private final Activity context;
    List<String> IDPENGUMUMAN = new ArrayList<String>();
    List<String> JUDULPENGUMUMAN = new ArrayList<String>();
    List<String> ISI = new ArrayList<String>();
    List<String> PENULIS = new ArrayList<String>();
    List<String> TANGGAL = new ArrayList<String>();

    public ListPengumumanAdapter(Activity context,
                                 List<String> IDPENGUMUMAN, List<String> JUDULPENGUMUMAN, List<String> ISI, List<String> PENULIS, List<String> TANGGAL) {
        super(context, R.layout.list_berita, IDPENGUMUMAN);

        this.context = context;
        this.IDPENGUMUMAN = IDPENGUMUMAN;
        this.ISI = ISI;
        this.JUDULPENGUMUMAN = JUDULPENGUMUMAN;
        this.PENULIS = PENULIS;
        this.TANGGAL = TANGGAL;
    }

    @Override
    public View getView(final int position, View view, ViewGroup parent) {
        LayoutInflater inflater = context.getLayoutInflater();
        View rowView= inflater.inflate(R.layout.list_pengumuman, null, true);
        TextView txtJudul = (TextView) rowView.findViewById(R.id.tvJudul);
        TextView txtIsi = (TextView) rowView.findViewById(R.id.tvIsi);
        TextView txtTanggal = (TextView) rowView.findViewById(R.id.tvTanggal);

        txtTanggal.setText(TANGGAL.get(position)+" - "+PENULIS.get(position));
        txtJudul.setText(JUDULPENGUMUMAN.get(position));
//        txtIsi.setText(Html.fromHtml(ISI.get(position)).toString().substring(0,100)+" ...");
        if(ISI.get(position).length()>=100){
            txtIsi.setText(Html.fromHtml(ISI.get(position)).toString().substring(0,100)+" ...");
        }else{
            txtIsi.setText(Html.fromHtml(ISI.get(position)).toString()+" ...");
        }

        return rowView;
    }


}