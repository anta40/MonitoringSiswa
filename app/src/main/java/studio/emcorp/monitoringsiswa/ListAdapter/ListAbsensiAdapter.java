package studio.emcorp.monitoringsiswa.ListAdapter;



import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import studio.emcorp.monitoringsiswa.R;

public class ListAbsensiAdapter extends ArrayAdapter<String> {
    private final Activity context;
    List<String> ABSENSI = new ArrayList<String>();
    List<String> NAMAKELAS = new ArrayList<String>();
    List<String> TANGGAL = new ArrayList<String>();

    public ListAbsensiAdapter(Activity context,
                              List<String> ABSENSI, List<String> NAMAKELAS, List<String> TANGGAL) {
        super(context, R.layout.list_berita, ABSENSI);

        this.context = context;
        this.ABSENSI = ABSENSI;
        this.NAMAKELAS = NAMAKELAS;
        this.TANGGAL = TANGGAL;
    }

    @Override
    public View getView(final int position, View view, ViewGroup parent) {
        LayoutInflater inflater = context.getLayoutInflater();
        View rowView= inflater.inflate(R.layout.list_pengumuman, null, true);
        TextView txtJudul = (TextView) rowView.findViewById(R.id.tvJudul);
        TextView txtIsi = (TextView) rowView.findViewById(R.id.tvIsi);
        TextView txtTanggal = (TextView) rowView.findViewById(R.id.tvTanggal);

        txtTanggal.setText(TANGGAL.get(position));
        txtJudul.setText("Kelas : " + NAMAKELAS.get(position));
        txtIsi.setText("Status : " + ABSENSI.get(position));

        return rowView;
    }


}