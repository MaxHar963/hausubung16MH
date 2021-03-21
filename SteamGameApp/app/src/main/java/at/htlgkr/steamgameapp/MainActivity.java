package at.htlgkr.steamgameapp;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.res.AssetManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.lang.reflect.Array;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import at.htlgkr.steam.Game;
import at.htlgkr.steam.ReportType;
import at.htlgkr.steam.SteamBackend;

import static at.htlgkr.steamgameapp.SteamGameAppConstants.*;

public class MainActivity extends AppCompatActivity {
    private static final String GAMES_CSV = "games.csv";
    SteamBackend sb = new SteamBackend();
    GameAdapter ga;
    ListView lv;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        lv = (ListView) findViewById(R.id.gamesList);
        loadGamesIntoListView();
        setUpReportSelection();
        setUpSearchButton();
        setUpAddGameButton();
        setUpSaveButton();


    }

    private void loadGamesIntoListView() {
        AssetManager assets = getAssets();
        try {
            sb.loadGames(assets.open(GAMES_CSV));
            ga = new GameAdapter(this, R.layout.game_item_layout, sb.getGames());
            lv.setAdapter(ga);

        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    private void setUpReportSelection() {

    }

    private void setUpSearchButton() {
        Spinner spinner = (Spinner) findViewById(R.id.chooseReport);
        ArrayList<ReportTypeSpinnerItem> list = new ArrayList<>();

        list.add(new ReportTypeSpinnerItem(ReportType.NONE, SELECT_ONE_SPINNER_TEXT));
        list.add(new ReportTypeSpinnerItem(ReportType.SUM_GAME_PRICES, SUM_GAME_PRICES_SPINNER_TEXT));
        list.add(new ReportTypeSpinnerItem(ReportType.AVERAGE_GAME_PRICES, AVERAGE_GAME_PRICES_SPINNER_TEXT));
        list.add(new ReportTypeSpinnerItem(ReportType.UNIQUE_GAMES, UNIQUE_GAMES_SPINNER_TEXT));
        list.add(new ReportTypeSpinnerItem(ReportType.MOST_EXPENSIVE_GAMES, MOST_EXPENSIVE_GAMES_SPINNER_TEXT));

        ArrayAdapter<ReportTypeSpinnerItem> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, list);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        spinner.setAdapter(adapter);

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

                switch (i) {
                    case 1:
                        builddialog(1);
                        break;
                    case 2:
                        builddialog(2);
                        break;
                    case 3:
                        builddialog(3);
                        break;
                    case 4:
                        builddialog(4);
                        break;
                    default:
                        return;
                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }

        });
    }

    private void setUpAddGameButton() {
        // Implementieren Sie diese Methode.
    }

    private void setUpSaveButton() {
        // Implementieren Sie diese Methode.
    }

    public void onSearch(View view) {

        Dialog dialog = new Dialog(this);
        dialog.setContentView(R.layout.search_dialog_layout);

        EditText eText = (EditText) dialog.findViewById(R.id.editTextSearch);
        ListView eList = (ListView) dialog.findViewById(R.id.ListViewID);

        Button bntSearch = (Button) dialog.findViewById(R.id.searchButton);
        Button bntCancel = (Button) dialog.findViewById(R.id.cancelButton);

        bntSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (eText.getText().equals("") || eText.getText().equals(null)) {
                    eList.setAdapter(null);
                } else {
                    ArrayList<Game> al = new ArrayList<>();
                    for (Game g : sb.getUniqueGames()) {

                        if (g.getName().toUpperCase().contains(eText.getText().toString().toUpperCase())) {
                            al.add(g);
                        }
                        ga = new GameAdapter(getApplicationContext(), R.layout.game_item_layout, al);
                        eList.setAdapter(ga);
                    }
                }
            }
        });
        bntCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        dialog.show();

    }

    public void builddialog(int i) {
        switch (i) {
            case 1:
                new AlertDialog.Builder(this)

                        .setMessage(ALL_PRICES_SUM + sb.sumGamePrices())
                        .setNeutralButton("Cancel", null)
                        .show();
                break;
            case 2:
                new AlertDialog.Builder(this)

                        .setMessage(ALL_PRICES_AVERAGE + sb.averageGamePrice())
                        .setNeutralButton("Cancel", null)
                        .show();
                break;
            case 3:
                new AlertDialog.Builder(this)

                        .setMessage(UNIQUE_GAMES_COUNT + sb.getUniqueGames().size())
                        .setNeutralButton("Cancel", null)
                        .show();
                break;
            case 4:
                new AlertDialog.Builder(this)

                        .setMessage(MOST_EXPENSIVE_GAMES + sb.selectTopNGamesDependingOnPrice(0) + "" + sb.selectTopNGamesDependingOnPrice(1) + "" + sb.selectTopNGamesDependingOnPrice(2))
                        .setNeutralButton("Cancel", null)
                        .show();
                break;
        }


    }

    public void onAddGame(View view) {

        Dialog dialog = new Dialog(this);
        dialog.setContentView(R.layout.addgame_dialog_layout);

        EditText eTextName = (EditText) dialog.findViewById(R.id.add_dialog_name);
        EditText eTextDate = (EditText) dialog.findViewById(R.id.add_dialog_date);
        EditText eTextPrice = (EditText) dialog.findViewById(R.id.add_dialog_price);

        Button bntAdd = (Button) dialog.findViewById(R.id.add_dialog_Add);
        Button bntCancel = (Button) dialog.findViewById(R.id.add_dialog_Cancel);

        bntAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (eTextDate.equals("") || eTextName.equals("") || eTextPrice.equals("")) {

                    Toast.makeText(MainActivity.this, "You have to fill in all fields !", Toast.LENGTH_SHORT).show();


                } else {

                    try {

                        Date date1 = new SimpleDateFormat("dd.MM.yyyy").parse(eTextDate.getText().toString());
                        sb.addGame(new Game(eTextName.getText().toString(), date1, Double.parseDouble(eTextPrice.getText().toString())));
                        Toast.makeText(MainActivity.this, "Game added ! ", Toast.LENGTH_SHORT).show();

                    } catch (Exception e) {

                        Toast.makeText(MainActivity.this, "Wrong input Format", Toast.LENGTH_SHORT).show();

                    }

                }
            }
        });
        bntCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        dialog.show();

    }

    public void onSave(View view) {

        String filename = "testfile.txt";

        try {
            FileOutputStream fos = openFileOutput(filename, MODE_PRIVATE);
            PrintWriter out = new PrintWriter(new OutputStreamWriter(fos));
            for (Game g : sb.getGames()) {

                out.println(g.toString());


            }
        } catch (FileNotFoundException exp) {
            exp.printStackTrace();
        }

    }
}
