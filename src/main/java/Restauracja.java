

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

public class Restauracja {
    public String nazwa;
    public HashMap<String, String> godziny_otwarcia;
    public ArrayList<Stolik> stoliki;


    public void readFile(String filePath) throws IOException {
        godziny_otwarcia = new HashMap<>();
        FileReader fileReader = new FileReader(filePath);
        BufferedReader bufferedReader = new BufferedReader(fileReader);

        nazwa = bufferedReader.readLine();
        String line = bufferedReader.readLine();

        while (!line.equals("Stoliki")) {

            String[] podzielone = line.split(" ");
            godziny_otwarcia.put(podzielone[0], podzielone[1]);
            line = bufferedReader.readLine();

        }
        line = bufferedReader.readLine();
        stoliki = new ArrayList<>();
        while (line != null) {

            String[] podzielone = line.split(" ");
            Stolik stolik = new Stolik(Integer.parseInt(podzielone[0]), Integer.parseInt(podzielone[1]));
            stoliki.add(stolik);
            line = bufferedReader.readLine();

        }
        bufferedReader.close();
    }
}
