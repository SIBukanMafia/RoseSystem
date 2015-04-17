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
import android.widget.Toast;

import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;

import java.util.ArrayList;


public class PelayanActivity extends ActionBarActivity {

    Firebase f;
    ListView lv;
    ArrayList listMeja;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pelayan);

        // firebase initialization
        Firebase.setAndroidContext(this);
        f = new Firebase("https://webservice.firebaseio.com");

        // listview initialization
        lv = (ListView) findViewById(R.id.listView);

        // load data
        loadDataMeja();

        // setting listener to list
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if(listMeja.get(position).equals("false")) {
                    Intent in = new Intent(PelayanActivity.this, StatusPesananActivity.class);
                    in.putExtra("id_meja", position+1);
                    startActivity(in);
                }
                else {
                    Intent in = new Intent(PelayanActivity.this, TambahPesananActivity.class);
                    in.putExtra("id_meja", position+1);
                    startActivity(in);
                    /*Toast.makeText(getApplicationContext(),
                            "Click ListItem Number " + position, Toast.LENGTH_LONG)
                            .show();*/
                }
            }
        });
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_pelayan, menu);
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

    public void loadDataMeja() {
        f.child("meja").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                listMeja = new ArrayList<String>();

                for(int i=1; i<=5; i++) {
                    listMeja.add(dataSnapshot.child(""+i).getValue());
                }

                MejaArrayAdapter adapter = new MejaArrayAdapter(PelayanActivity.this, listMeja);
                lv.setAdapter(adapter);
            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {

            }
        });
    }

    public class MejaArrayAdapter extends ArrayAdapter<String> {
        private final Context context;
        private final ArrayList values;

        public MejaArrayAdapter(Context context, ArrayList values) {
            super(context, R.layout.list_meja, values);
            this.context = context;
            this.values = values;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            LayoutInflater inflater = (LayoutInflater) context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View rowView = inflater.inflate(R.layout.list_meja, parent, false);
            TextView textView = (TextView) rowView.findViewById(R.id.firstLine);
            TextView textView2 = (TextView) rowView.findViewById(R.id.secondLine);
            textView.setText("Meja " + (position+1));
            if(values.get(position).equals("true")) {
                textView2.setText("kosong");
            }
            else {
                textView2.setText("");
            }

            return rowView;
        }
    }
}
