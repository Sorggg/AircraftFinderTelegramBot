import java.io.IOException;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

public class DB {
    private static boolean full = false;
    public static void populate(String seed) throws IOException, SQLException {
        String query = "SELECT COUNT(*) AS conta FROM Aerei";
        PreparedStatement stmt = MyAmazingBot.conn1.prepareStatement(query);
        ResultSet rs = null;
        try {
            rs = stmt.executeQuery();
        } catch (SQLException ecc) {
            ecc.printStackTrace();
        }
        while (rs.next()){
            if(rs.getString("conta").equals("0")){
                full = true;
            }
        }
        if (full) {
            System.out.println("Riempimento database");
            List<String> aerei = Crawler.findUrls(seed);
            for (int i = 0; i < aerei.size(); i++) {
                for (int j = 0; j < aerei.size() - i - 1; j++) {
                    if (aerei.get(j).compareTo(aerei.get(j + 1)) > 0) {
                        String temp = aerei.get(j);
                        aerei.set(j, aerei.get(j + 1));
                        aerei.set(j + 1, temp);
                    }
                }
            }
            for(String aereo : aerei){
                String noLink = aereo.replace("https://en.wikipedia.org/wiki/","");
                String words[] = noLink.split("_");
                String marca = "";
                if (words[0].length() < 3) {
                    marca = words[0] + "_" + words[1];
                } else {
                    marca = words[0];
                }
                String modello = noLink.replace(marca, "");
                try {
                    modello = modello.substring(1);
                } catch (Exception ecc) {
                    ecc.printStackTrace();
                }
                modello = modello.replace(".","_");
                modello = modello.replace("/","_");
                modello = modello.replace("-","_");
                query = "INSERT INTO Aerei (Id,Marca,Modello,Indirizzo) VALUES (null,?,?,?)";
                stmt = MyAmazingBot.conn1.prepareStatement(query);
                stmt.setString(1,marca);
                stmt.setString(2,modello);
                stmt.setString(3,aereo);
                try {
                    stmt.executeUpdate();
                } catch (SQLException ecc) {
                    ecc.printStackTrace();
                }
            }
        }
    }

}
