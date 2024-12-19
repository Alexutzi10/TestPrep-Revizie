package com.example.revizie;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.revizie.data.Revizie;
import com.example.revizie.database.RevizieService;
import com.example.revizie.network.AsyncTaskRunner;
import com.example.revizie.network.Callback;
import com.example.revizie.network.HttpManager;
import com.example.revizie.network.Parser;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class MainActivity extends AppCompatActivity {
    public static final String SPINNER_KEY = "spinner_key";
    public static final String ET_KEY = "et_key";
    public static final String SHARED_PREFERENCES = "shared_preferences";
    private final String url = "https://api.npoint.io/efcea1d46c36fa555d04";
    private FloatingActionButton fab;
    private TextView tv;
    private Spinner spinner;
    private EditText et;
    private Button bttn;
    private List<Revizie> revizii = new ArrayList<>();
    private SharedPreferences sharedPreferences;
    private AsyncTaskRunner asyncTaskRunner = new AsyncTaskRunner();
    private RevizieService revizieService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        initComponents();

        fab.setOnClickListener(click -> {
            asyncTaskRunner.executeAsync(new HttpManager(url), onMainThreadOperation());
        });

        bttn.setOnClickListener(click -> {
            if (et.getText() == null || et.getText().toString().isBlank()) {
                Toast.makeText(getApplicationContext(), R.string.price_value_field_is_empty, Toast.LENGTH_SHORT).show();
                return;
            }

            String comp = spinner.getSelectedItem().toString();
            int value = Integer.parseInt(et.getText().toString());

            List<Revizie> deleted;
            if (comp.equals("are valoarea mai mare decat")) {
                deleted = revizii.stream().filter(r -> r.getPrice() > value).collect(Collectors.toList());
            } else if (comp.equals("are valoarea mai mica decat")) {
                deleted = revizii.stream().filter(r -> r.getPrice() < value).collect(Collectors.toList());
            } else {
                deleted = revizii.stream().filter(r -> r.getPrice() == value).collect(Collectors.toList());
            }

            revizieService.delete(deleted, callbackDelete(comp, value));

        });
    }

    private Callback<List<Revizie>> callbackDelete(String comp, int value) {
        return result -> {
            if (result != null) {
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putString(SPINNER_KEY, comp);
                editor.putInt(ET_KEY, value);
                editor.apply();
                revizii.removeAll(result);
                Toast.makeText(getApplicationContext(), R.string.deleted, Toast.LENGTH_SHORT).show();
            }
        };
    }

    private Callback<String> onMainThreadOperation() {
        return result -> {
          List<Revizie> parsed = Parser.getFromJson(result);
          List<Revizie> insert = parsed.stream().filter(r -> !revizii.contains(r)).collect(Collectors.toList());

          if (!insert.isEmpty()) {
              revizieService.insertAll(insert, callbackInsertAll());
          }
        };
    }

    private Callback<List<Revizie>> callbackInsertAll() {
        return result -> {
          if (result != null) {
              revizii.addAll(result);
              Toast.makeText(getApplicationContext(), R.string.inserted, Toast.LENGTH_SHORT).show();
          }
        };
    }

    private void initComponents() {
        fab = findViewById(R.id.surugiu_george_alexandru_fab);
        tv = findViewById(R.id.surugiu_george_alexandru_tv);
        spinner = findViewById(R.id.surugiu_george_alexandru_spinner);
        et = findViewById(R.id.surugiu_george_alexandru_et);
        bttn = findViewById(R.id.surugiu_george_alexandru_bttn);

        revizieService = new RevizieService(getApplicationContext());
        sharedPreferences = getApplicationContext().getSharedPreferences(SHARED_PREFERENCES, MODE_PRIVATE);

        String comp = sharedPreferences.getString(SPINNER_KEY, "");
        int value = sharedPreferences.getInt(ET_KEY, 0);

        if (comp.equals("are valoarea mai mare decat")) {
            spinner.setSelection(0);
        } else if (comp.equals("are valoarea mai mica decat")) {
            spinner.setSelection(1);
        } else {
            spinner.setSelection(2);
        }

        if (value != 0) {
            et.setText(String.valueOf(value));
        }

        revizieService.getAll(callbackGetAll());
    }

    private Callback<List<Revizie>> callbackGetAll() {
        return result -> {
            if (result != null) {
                revizii.clear();
                revizii.addAll(result);
                Toast.makeText(getApplicationContext(), R.string.loaded, Toast.LENGTH_SHORT).show();
            }
        };
    }
}