package henrymenori.rosesystem;

import android.content.Context;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;


public class StatusPesananActivity extends ActionBarActivity {

    Firebase f;
    ListView lv;
    ArrayList listPesanan;
    int id_meja, id_pesanan;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_status_pesanan);

        id_meja = getIntent().getExtras().getInt("id_meja");

        // firebase initialization
        Firebase.setAndroidContext(this);
        f = new Firebase("https://webservice.firebaseio.com");

        // listview initialization
        lv = (ListView) findViewById(R.id.listView2);

        // load data
        loadDataPesanan();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_status_pesanan, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void loadDataPesanan() {
        f.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                listPesanan = new ArrayList<RPesanan>();

                // get id_pesanan
                ArrayList pesanan = (ArrayList) dataSnapshot.child("pesanan").getValue();
                for(int i=1; i<pesanan.size(); i++) {
                    HashMap<String, Object> mapPesanan = (HashMap<String, Object>) pesanan.get(i);
                    int temp = Integer.parseInt((String) mapPesanan.remove("no_meja"));
                    if(temp == id_meja) {
                        id_pesanan = i;
                    }
                }

                // get data pesanan
                ArrayList terdiri = (ArrayList) dataSnapshot.child("terdiri_dari").getValue();
                for(int i=1; i<terdiri.size(); i++) {
                    HashMap<String, Object> mapTerdiri = (HashMap<String, Object>) terdiri.get(i);
                    int temp = Integer.parseInt((String) mapTerdiri.remove("id_pesanan"));
                    if(temp == id_pesanan) {
                        String nama = (String) dataSnapshot.child("menu").child((String) mapTerdiri.remove("id_menu")).child("nama").getValue();
                        RPesanan p = new RPesanan(nama, Integer.parseInt((String) mapTerdiri.remove("kuantitas")), (String) mapTerdiri.remove("status_penyajian"));
                        listPesanan.add(p);
                    }
                }

                PesananArrayAdapter adapter = new PesananArrayAdapter(StatusPesananActivity.this, listPesanan);
                lv.setAdapter(adapter);
            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {

            }
        });
    }

    public class PesananArrayAdapter extends ArrayAdapter<String> {
        private final Context context;
        private final ArrayList values;

        public PesananArrayAdapter(Context context, ArrayList values) {
            super(context, R.layout.list_meja, values);
            this.context = context;
            this.values = values;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            LayoutInflater inflater = (LayoutInflater) context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View rowView = inflater.inflate(R.layout.list_menu, parent, false);
            TextView textView = (TextView) rowView.findViewById(R.id.nama);
            TextView textView2 = (TextView) rowView.findViewById(R.id.jumlah);
            TextView textView3 = (TextView) rowView.findViewById(R.id.status);

            RPesanan r = (RPesanan) values.get(position);
            textView.setText(r.getNama());
            textView2.setText(""+r.getKuantitas());
            if(r.getStatus().equals("true")) {
                textView3.setText("Sudah Disajikan");
            }
            else {
                textView3.setText("Sedang Diproses");
            }

            return rowView;
        }
    }
}
