package mini.com.shoppinglist;

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        FragmentManager fragmentManager = getFragmentManager();

        RecyclerFragment myRecyclerFragment = new RecyclerFragment();
        fragmentManager.beginTransaction()
                .add(R.id.content, myRecyclerFragment).commit();

    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main_menu, menu);

        MenuItem mi = menu.getItem(0);
        mi.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem menuItem) {
                FragmentManager fragmentManager = getFragmentManager();
                FragmentTransaction fragmentTransaction =
                        fragmentManager.beginTransaction();
                Fragment fragment = fragmentManager.findFragmentByTag("uniqueTag");

                if (fragment == null) {
                    fragment = new MyPreferenceFragment();
                    fragmentTransaction.replace(R.id.content, fragment, "uniqueTag")
                            .addToBackStack("uniqueTag").commit();
                }
                else {
                    fragmentTransaction.replace(R.id.content, fragment).commit();
                }
                return true;
            }
        });

        return true;
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
    }
}
