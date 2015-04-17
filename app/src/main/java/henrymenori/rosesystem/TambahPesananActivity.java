package henrymenori.rosesystem;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.text.Editable;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;


public class TambahPesananActivity extends ActionBarActivity {

    Firebase f;
    ListView lv;
    ArrayList listMenu;
    int id_meja, id_pesanan, id_terdiri;
    EditText e;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tambah_pesanan);

        id_meja = getIntent().getExtras().getInt("id_meja");

        // firebase initialization
        Firebase.setAndroidContext(this);
        f = new Firebase("https://webservice.firebaseio.com");

        // listview initialization
        lv = (ListView) findViewById(R.id.listView5);
        e = new EditText(this);

        // load data
        loadDataMenu();

        // setting listener to list
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, final int position, long id) {
                new AlertDialog.Builder(TambahPesananActivity.this)
                        .setMessage("Isi jumlah pemesanan")
                        .setView(e)
                        .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int whichButton) {
                                int value = Integer.parseInt(e.getText().toString());
                                RMenu r = (RMenu) listMenu.get(position);
                                r.setJumlah(value);
                                listMenu.set(position, r);

                                PesananArrayAdapter adapter = new PesananArrayAdapter(TambahPesananActivity.this, listMenu);
                                lv.setAdapter(adapter);
                            }
                        }).setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        // Do nothing.
                    }
                }).show();
            }
        });
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_tambah_pesanan, menu);
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

    public void loadDataMenu() {
        f.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                listMenu = new ArrayList<RMenu>();

                id_pesanan = Integer.parseInt((String)dataSnapshot.child("total_pesanan").getValue());
                id_terdiri = Integer.parseInt((String)dataSnapshot.child("total_terdiri").getValue());

                System.out.println(id_pesanan + " " + id_terdiri);

                ArrayList menu = (ArrayList) dataSnapshot.child("menu").getValue();
                for(int i=1; i<menu.size(); i++) {
                    HashMap<String, Object> mapMenu = (HashMap<String, Object>) menu.get(i);
                    RMenu r = new RMenu((String) mapMenu.remove("nama"), (String) mapMenu.remove("harga"), (String) mapMenu.remove("ketersediaan"), 0);
                    listMenu.add(r);
                }
                System.out.println(listMenu.size());

                PesananArrayAdapter adapter = new PesananArrayAdapter(TambahPesananActivity.this, listMenu);
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
            View rowView = inflater.inflate(R.layout.list_pesanan, parent, false);
            TextView textView = (TextView) rowView.findViewById(R.id.textView7);
            TextView textView2 = (TextView) rowView.findViewById(R.id.textView8);
            TextView textView3 = (TextView) rowView.findViewById(R.id.textView9);
            TextView textView4 = (TextView) rowView.findViewById(R.id.textView10);

            RMenu r = (RMenu) values.get(position);
            textView.setText(r.getNama());
            textView2.setText(r.getHarga());
            textView3.setText(r.getKetersediaan());
            textView4.setText(""+r.getJumlah());

            return rowView;
        }
    }

    public void submit(View view) {
        // submit meja
        f.child("meja").child("" + id_meja).setValue("false");

        // submit pesanan
        id_pesanan++;
        f.child("pesanan").child("" + (id_pesanan)).child("no_meja").setValue("" + (id_meja));
        f.child("pesanan").child("" + (id_pesanan)).child("status_pembayaran").setValue("false");

        // submit terdiri
        for(int i=0; i<listMenu.size(); i++) {
            RMenu r = (RMenu) listMenu.get(i);
            if(r.getJumlah() > 0) {
                id_terdiri++;
                f.child("terdiri_dari").child("" + (id_terdiri)).child("id_menu").setValue("" + (i+1));
                f.child("terdiri_dari").child("" + (id_terdiri)).child("id_pesanan").setValue("" + id_pesanan);
                f.child("terdiri_dari").child("" + (id_terdiri)).child("kuantitas").setValue("" + r.getJumlah());
                f.child("terdiri_dari").child("" + (id_terdiri)).child("status_penyajian").setValue("false");
            }
        }

        // submit total
        f.child("total_pesanan").setValue("" + id_pesanan);
        f.child("total_terdiri").setValue("" + id_terdiri);

        Intent in = new Intent(TambahPesananActivity.this, PelayanActivity.class);
        startActivity(in);
    }
}
