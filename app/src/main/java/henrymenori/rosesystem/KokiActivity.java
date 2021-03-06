package henrymenori.rosesystem;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;


public class KokiActivity extends ActionBarActivity {

    Firebase f;
    ListView lv;
    ArrayList listPesanan;
    int id_meja, id_pesanan;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_koki);

        // firebase initialization
        Firebase.setAndroidContext(this);
        f = new Firebase("https://webservice.firebaseio.com");

        // listview initialization
        lv = (ListView) findViewById(R.id.listView6);

        // load data
        loadDataPesanan();

        // setting listener to list
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                // update status_penyajian
                RPesanan p = (RPesanan) listPesanan.get(position);
                f.child("terdiri_dari").child(p.getStatus()).child("status_penyajian").setValue("true");

                PesananArrayAdapter adapter = new PesananArrayAdapter(KokiActivity.this, listPesanan);
                lv.setAdapter(adapter);
            }
        });
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_koki, menu);
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

                // get data pesanan
                ArrayList terdiri = (ArrayList) dataSnapshot.child("terdiri_dari").getValue();
                for(int i=1; i<terdiri.size(); i++) {
                    HashMap<String, Object> mapTerdiri = (HashMap<String, Object>) terdiri.get(i);
                    if(((String) mapTerdiri.remove("status_penyajian")).equals("false")) {
                        String nama = (String) dataSnapshot.child("menu").child((String) mapTerdiri.remove("id_menu")).child("nama").getValue();
                        int kuantitas = Integer.parseInt((String) mapTerdiri.remove("kuantitas"));
                        String status = "" + i;
                        RPesanan p = new RPesanan(nama, kuantitas, status);
                        listPesanan.add(p);
                    }
                }

                PesananArrayAdapter adapter = new PesananArrayAdapter(KokiActivity.this, listPesanan);
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
            textView3.setText(r.getStatus());

            return rowView;
        }
    }
}
