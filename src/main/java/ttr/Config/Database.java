package ttr.Config;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.cloud.firestore.Firestore;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.cloud.FirestoreClient;

import java.io.FileInputStream;
import java.io.IOException;

public class Database {
    private static final String privateKey = "src/main/resources/ttr/firebase/iipsene-ttr-firebase-adminsdk-uea7f-1bc1be03bb.json";
    private static final String databaseUrl = "https://console.firebase.google.com/project/iipsene-ttr/firestore/data/";
    private Firestore db;

    public Database() {
        try {
            FileInputStream serviceAccount =
                    new FileInputStream(privateKey);

            FirebaseOptions options = new FirebaseOptions.Builder()
                    .setCredentials(GoogleCredentials.fromStream(serviceAccount))
                    .setDatabaseUrl(databaseUrl)
                    .build();

            if (FirebaseApp.getApps().isEmpty()) {
                FirebaseApp.initializeApp(options);
            }
            this.db = FirestoreClient.getFirestore();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public Firestore getDb() {
        return this.db;
    }
}
