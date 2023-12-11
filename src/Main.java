import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.updatesreceivers.DefaultBotSession;
import java.io.IOException;
import java.sql.DriverManager;
import java.sql.SQLException;
//cose da fare
//riempi database

public class Main {
    public static void main(String[] args) throws IOException {
        try {
            TelegramBotsApi botsApi = new TelegramBotsApi(DefaultBotSession.class);
            botsApi.registerBot(new MyAmazingBot());
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
        try {
            MyAmazingBot.conn1 = DriverManager.getConnection("jdbc:mysql://127.0.0.1/AereiBotDB", "root", "");
            if (MyAmazingBot.conn1.isValid(5)) {
                System.out.println("Connection is successful.");
            } else {
                System.out.println("Connection is not valid.");
            }
        } catch (SQLException e) {
            System.out.println("Connection failed. Error message: " + e.getMessage());
        }
    }
}
