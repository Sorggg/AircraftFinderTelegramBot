import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.send.SendPhoto;
import org.telegram.telegrambots.meta.api.objects.InputFile;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;



public class MyAmazingBot extends TelegramLongPollingBot {
    boolean started = false;
    public static Connection conn1 = null;
    @Override
    public void onUpdateReceived(Update update) {
        SendMessage message = new SendMessage();
        SendPhoto messagePhoto = new SendPhoto();
        message.setChatId(update.getMessage().getChatId());
        // We check if the update has a message and the message has text
        if (update.hasMessage() && update.getMessage().hasText()) {
             // Create a SendMessage object with mandatory fields


            if(update.getMessage().getText().equals("/start")){
                started = true;

                try {
                    String seed = "https://en.wikipedia.org/wiki/List_of_aircraft_by_date_and_usage_category";
                    DB.populate(seed);




                    String query = "SELECT DISTINCT Marca FROM Aerei GROUP BY Marca HAVING COUNT(*)> 2 ;";
                    PreparedStatement stmt = conn1.prepareStatement(query);
                    ResultSet rs = null;
                    try {
                        rs = stmt.executeQuery();
                    } catch (SQLException ecc) {
                        ecc.printStackTrace();
                    }
                    int i = 0;
                    message.setText("");
                    while (rs.next()) {
                        message.setText(message.getText() + "/" + rs.getString("Marca").replace("-","_") + "\n");
                        i++;
                        if(i % 25 == 0){
                            execute(message);
                            message.setText("");
                        }

                    }
                    execute(message);
                    message.setText("Seleziona la marca di aereo che vuoi scoprire");
                    execute(message);
                } catch (IOException | TelegramApiException e) {
                    e.printStackTrace();
                } catch (SQLException e) {
                    throw new RuntimeException(e);
                }
            }
            else {
                if(update.getMessage().getText().contains("/")){
                    //verifica se il messaggio è una marca di aereo
                    boolean marcafound = false;
                    try{
                        String query = "SELECT DISTINCT Modello FROM Aerei WHERE Marca = ?;";
                        PreparedStatement stmt = conn1.prepareStatement(query);
                        stmt.setString(1,update.getMessage().getText().replace("/",""));
                        ResultSet rs = null;
                        try {
                            rs = stmt.executeQuery();
                        } catch (SQLException ecc) {
                            ecc.printStackTrace();
                        }
                        message.setText("");
                        int i = 0;
                        while (rs.next()){
                            message.setText(message.getText() + "/" + rs.getString("Modello") + "\n");
                            i++;
                        }
                        if (i > 1){
                            marcafound = true;
                        }
                        try {
                            if (marcafound){
                                execute(message);
                            }

                        } catch (TelegramApiException e) {
                            e.printStackTrace();

                        }
                        if(!marcafound){
                            query = "SELECT DISTINCT * FROM Aerei WHERE Modello = ?;";
                            stmt = conn1.prepareStatement(query);
                            stmt.setString(1,update.getMessage().getText().replace("/",""));
                            rs = null;
                            try {
                                rs = stmt.executeQuery();
                            } catch (SQLException ecc) {
                                ecc.printStackTrace();
                            }

                            String aircraftUrl = "";
                            while (rs.next()){
                                aircraftUrl = rs.getString("Indirizzo");
                            }
                            if(aircraftUrl.isEmpty()){
                                message.setText("Elemento non trovato");
                                try {
                                    execute(message);
                                } catch (TelegramApiException e) {
                                    e.printStackTrace();
                                }
                            }
                            else {
                                try {
                                    URL imgUrl = new URL(Crawler.getPicture(aircraftUrl));
                                    InputStream is = imgUrl.openStream();
                                    SendPhoto mandaFoto = new SendPhoto();
                                    mandaFoto.setPhoto(new InputFile(is, "aereo.jpg"));
                                    mandaFoto.setChatId(update.getMessage().getChatId());
                                    execute(mandaFoto);
                                    //message.setText(rs.getString("Marca") + " " + rs.getString("Modello") + "\n");
                                    if((Crawler.getManufacturer(aircraftUrl) != null &&!(Crawler.getManufacturer(aircraftUrl).isBlank()))){
                                        message.setText(message.getText() + "Costruttore: "+Crawler.getManufacturer(aircraftUrl)+"\n");
                                    }
                                    if(Crawler.getRole(aircraftUrl)!=null && !Crawler.getRole(aircraftUrl).isBlank()){
                                        message.setText(message.getText()+"Ruolo: "+Crawler.getRole(aircraftUrl)+ "\n");
                                    }
                                    if(Crawler.getVariants(aircraftUrl)!=null && !Crawler.getVariants(aircraftUrl).isBlank()){
                                        message.setText(message.getText()+"Varianti: "+Crawler.getVariants(aircraftUrl)+ "\n");
                                    }
                                    if(Crawler.getFirst(aircraftUrl)!=null && !Crawler.getFirst(aircraftUrl).isBlank()){
                                        message.setText(message.getText()+"Data del primo volo: "+Crawler.getFirst(aircraftUrl)+ "\n");
                                    }
                                    if(Crawler.getStatus(aircraftUrl)!=null && !Crawler.getStatus(aircraftUrl).isBlank()){
                                        message.setText(message.getText()+"Status: "+Crawler.getStatus(aircraftUrl)+ "\n");
                                    }
                                    if(Crawler.getNaz(aircraftUrl)!=null && !Crawler.getNaz(aircraftUrl).isBlank()){
                                        message.setText(message.getText()+"Nazionalità: "+Crawler.getNaz(aircraftUrl)+ "\n");
                                    }
                                    execute(message);

                                } catch (IOException | TelegramApiException e) {

                                    e.printStackTrace();
                                }
                            }
                            try {
                                Crawler.getPicture(aircraftUrl);
                                message.setText(aircraftUrl);
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }
                    }catch (SQLException ecc) {
                        ecc.printStackTrace();
                    }

                }
            }
        }
    }


    @Override
    public String getBotUsername() {
        return "Sorgente_bot";
    }

    @Override
    public String getBotToken() {
        return "6784092395:AAHZ4k1DN3rPoXyhmY76R714LThcNuKmdA0";
    }
}