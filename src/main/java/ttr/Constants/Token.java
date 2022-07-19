package ttr.Constants;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Scanner;

public class Token {
    private String token;
    private final String filename = "token.txt";

    public Token() {
        checkToken();
    }

    public void createToken(String fileToken) {
        try {
            //create token-file
            File tokenFile = new File(filename);
            tokenFile.createNewFile();

            //write token to token-file
            FileWriter myWriter = new FileWriter(filename);
            myWriter.write(fileToken);
            myWriter.close();

            checkToken();
        }
        catch (IOException ignored) {
        }
    }

    public void checkToken() {
        try {
            File tokenFile = new File(filename);
            Scanner myReader = new Scanner(tokenFile);
            while (myReader.hasNextLine()) {
                token = myReader.nextLine();
            }
        }
        catch (FileNotFoundException ignored) {
        }
    }

    public String getToken() {
        return token;
    }
}
