package henrymenori.rosesystem;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;


public class MenuActivity extends ActionBarActivity {

    Firebase f;
    ArrayList menus;
    ArrayList list;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);

        Firebase.setAndroidContext(this);
        f = new Firebase("https://webservice.firebaseio.com");

        final ListView listview = (ListView) findViewById(R.id.listView);
        String[] values = new String[] {"marcelinus", "henry", "menori"};

        menus = new ArrayList<MenuRose>();

        f.child("menu").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                HashMap<String, Object> menu = (HashMap<String, Object>) dataSnapshot.getValue();

                int counter = 1;
                for (Object o : menu.values()) {
                    if (counter > 1) {
                        HashMap<String, Object> menuMap = (HashMap<String, Object>) o;
                        MenuRose m = new MenuRose();
                        m.setNama((String) menuMap.remove("nama"));
                        m.setHarga(Integer.parseInt((String) menuMap.remove("harga")));
                        if ((String) menuMap.remove("ketersediaan") == "true") {
                            m.setKetersediaan(true);
                        } else {
                            m.setKetersediaan(false);
                        }
                        menus.add(m);
                        System.out.println(menus.size());
                    }
                    counter++;
                }
                list = new ArrayList<MenuRose>();
                for (int i = 0; i < menus.size(); ++i) {
                    MenuRose m = (MenuRose) menus.get(i);
                    list.add(m.getNama());
                }
                final StableArrayAdapter adapter = new StableArrayAdapter(MenuActivity.this,
                        android.R.layout.simple_list_item_1, list);
                listview.setAdapter(adapter);
            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {

            }
        });
    }

    private class StableArrayAdapter extends ArrayAdapter<String> {

        HashMap<String, Integer> mIdMap = new HashMap<String, Integer>();

        public StableArrayAdapter(Context context, int textViewResourceId,
                                  List<String> objects) {
            super(context, textViewResourceId, objects);
            for (int i = 0; i < objects.size(); ++i) {
                mIdMap.put(objects.get(i), i);
            }
        }

        @Override
        public long getItemId(int position) {
            String item = getItem(position);
            return mIdMap.get(item);
        }

        @Override
        public boolean hasStableIds() {
            return true;
        }

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_menu, menu);
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

    public void tambahMenu(View view) {
        Intent in = new Intent(this, TambahMenuActivity.class);
        startActivity(in);
    }
}
