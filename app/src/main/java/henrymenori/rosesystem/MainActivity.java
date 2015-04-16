package henrymenori.rosesystem;

import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;


public class MainActivity extends ActionBarActivity {

    String s;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // firebase initialization
        Firebase.setAndroidContext(this);
        Firebase f = new Firebase("https://webservice.firebaseio.com");

        // write to firebase
        //f.child("users").setValue("menori");

        // load from firebase
        /*f.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                System.out.println(snapshot.getValue());
                System.out.println(snapshot.child("users").getValue());
            }
            @Override
            public void onCancelled(FirebaseError firebaseError) {
                System.out.println("The read failed: " + firebaseError.getMessage());
            }
        });*/
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
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

    public void selectKasir(View view) {
        Intent in = new Intent(this, KasirActivity.class);
        startActivity(in);
    }

    public void selectKoki(View view) {
        Intent in = new Intent(this, KokiActivity.class);
        startActivity(in);
    }

    public void selectPelayan(View view) {
        Intent in = new Intent(this, PelayanActivity.class);
        startActivity(in);
    }

    public void selectMenu(View view) {
        Intent in = new Intent(this, MenuActivity.class);
        startActivity(in);
    }
}
