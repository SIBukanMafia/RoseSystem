package henrymenori.rosesystem;

import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;

import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;

import java.util.HashMap;
import java.util.Map;


public class TambahMenuActivity extends ActionBarActivity {

    Firebase f; // firebase connection
    int counter; // menu data counter

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tambah_menu);

        Firebase.setAndroidContext(this);
        f = new Firebase("https://webservice.firebaseio.com");

        f.child("menu").child("total").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                counter = Integer.parseInt(snapshot.getValue().toString());
            }
            @Override
            public void onCancelled(FirebaseError firebaseError) {
                System.out.println("The read failed: " + firebaseError.getMessage());
            }
        });
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_tambah_menu, menu);
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

    public void submit(View view) {
        // get input data
        EditText editText = (EditText) findViewById(R.id.editText);
        EditText editText2 = (EditText) findViewById(R.id.editText2);

        // submit to firebase
        Map<String, String> menus = new HashMap<String, String>();
        menus.put("nama", editText.getText().toString());
        menus.put("harga", editText2.getText().toString());
        menus.put("ketersediaan", "true");

        counter++;
        f.child("menu").child("total").setValue(counter);
        f.child("menu").child(""+counter).setValue(menus);

        // back to menu page
        Intent in = new Intent(this, MenuActivity.class);
        startActivity(in);
    }
}
