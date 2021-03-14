package at.htlgkr.steam;


import android.util.Log;

import java.io.BufferedReader;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.OptionalDouble;
import java.util.stream.Collectors;

public class SteamBackend {

    ArrayList<Game> list;

    public SteamBackend() {
        list = new ArrayList<>();
    }

    public void loadGames(InputStream inputStream) {
        list.clear();
        try {
            BufferedReader in = new BufferedReader(new InputStreamReader(inputStream));
            String line;
            StringBuffer buffer = new StringBuffer();
            while ((line = in.readLine()) != null) {
                String[] arr = line.split(";");
                if (!arr[0].equals("Name")) {

                    Date d = new Date();
                    d.setTime(Long.parseLong(arr[1]));
                    list.add(new Game(arr[0], d, Double.parseDouble(arr[2])));

                }
            }
            in.close();
        } catch (Exception e) {

            System.out.println("Something went wrong [LoadGames]");

        }

    }

    public void store(OutputStream fileOutputStream) {

        try {
            PrintWriter out = new PrintWriter(new OutputStreamWriter(fileOutputStream));
            for (Game e : list) {
                out.println(e.getName() + ";" + e.getReleaseDate().toString() + ";" + e.getPrice());

            }
            out.flush();
            out.close();
        } catch (Exception e) {
            System.out.println("Something went wrong [store]");
        }


    }

    public List<Game> getGames() {
        return list;
    }

    public void setGames(List<Game> games) {
        list = (ArrayList<Game>) games;
    }

    public void addGame(Game newGame) {
        list.add(newGame);
    }

    public double sumGamePrices() {

        return list.stream()
                .mapToDouble(a -> a.getPrice())
                .sum();
    }

    public double averageGamePrice() {
        return list.stream()
                .mapToDouble(a -> a.getPrice())
                .average()
                .getAsDouble();
    }

    public List<Game> getUniqueGames() {
        return list.stream()
                .distinct()
                .collect(Collectors.toList());
    }

    public List<Game> selectTopNGamesDependingOnPrice(int n) {
        List<Game> list1 = list.stream().sorted().collect(Collectors.toList());
        List<Game> list2 = new ArrayList<>();
        for (int i = 0; i < n; i++) {
            list2.add(list1.get(i));
        }

        return list2;

    }
}
