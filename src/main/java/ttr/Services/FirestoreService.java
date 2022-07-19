package ttr.Services;

import com.google.cloud.firestore.Firestore;

import java.util.*;
import java.util.concurrent.ExecutionException;
import com.google.api.core.ApiFuture;
import com.google.cloud.firestore.CollectionReference;
import com.google.cloud.firestore.DocumentReference;
import com.google.cloud.firestore.DocumentSnapshot;
import com.google.cloud.firestore.WriteResult;
import ttr.Config.Database;
import ttr.Constants.ClientConstants;
import ttr.Controllers.Controller;
import static ttr.Constants.ClientConstants.*;

public class FirestoreService {
    private Firestore firestore;
    private static final String GAMES_PATH = "games";
    private ClientConstants cc = new ClientConstants();
    private CollectionReference colRef;
    static FirestoreService firebaseService;

    public static FirestoreService getInstance() {
        if (firebaseService == null) {
            firebaseService = new FirestoreService();
        }
        return firebaseService;
    }

    public FirestoreService() {
        Database db = new Database();
        this.firestore = db.getDb();
        this.colRef = this.firestore.collection(GAMES_PATH);
    }

    public void listen(String documentId, final Controller controller) {
        DocumentReference docRef = this.colRef.document(documentId);
        docRef.addSnapshotListener((snapshot, e) -> {
            if (e != null) {
                System.err.println("Listen failed: " + e);
                return;
            }

            if (snapshot != null && snapshot.exists()) {
                controller.update(snapshot);
            }
        });
    }

    public void set(String documentId, Map<String, Object> docData) {
        ApiFuture<WriteResult> future = this.colRef.document(documentId).set(docData);
    }

    public DocumentSnapshot get(String documentId) {
        DocumentReference docRef = this.colRef.document(documentId);
        ApiFuture<DocumentSnapshot> future = docRef.get();
        DocumentSnapshot document;

        try {
            document = future.get();
            if (document.exists()) {
                return document;
            }
        } catch (InterruptedException | ExecutionException | NullPointerException ignored) {

        }
        return null;
    }

    public int getTrainCardValue(String color) {
        DocumentSnapshot ds = this.get(cc.getID());

        String field = TRAINCARD_DECK;

        HashMap td = (HashMap) ds.get(field);
        String s = new String();
        try {
            s = td.get(color).toString();
        } catch (NullPointerException ignored) {

        }
        int value = Integer.parseInt(s);
        return value;
    }

    public HashMap<Object, HashMap> getBoardState() {
        DocumentSnapshot ds = this.get(cc.getID());
        HashMap<Object, HashMap> td = (HashMap) ds.get(BOARD_STATE);

        return td;
    }

    public String getTrainOrStation(String route, String trainOrStation) {
        HashMap<Object, HashMap> td = getBoardState();

        HashMap<String, Object> target = td.get(route.toLowerCase(Locale.ROOT));
        Object value = target.get(trainOrStation);
        if (value == null) {
            return null;
        }

        return value.toString();
    }

    public void updateValue(String field, Object value) {
        DocumentSnapshot ds = this.get(cc.getID());
        Map<String, Object> currentMap = ds.getData();

        currentMap.put(field, value);
        this.set(cc.getID(), currentMap);
    }

    public void updateField(String field, String key, Object value) {
        DocumentSnapshot ds = this.get(cc.getID());

        Map<String, Object> currentMap = ds.getData();

        HashMap td = (HashMap) ds.get(field);
        td.put(key, value);
        currentMap.put(field, td);
        this.set(cc.getID(), currentMap);
    }

    public void removeTicket(String key) {
        DocumentSnapshot ds = this.get(cc.getID());

        Map<String, Object> currentMap = ds.getData();

        HashMap td = (HashMap) ds.get(TICKET_DECK);
        if (td.get(key) != null) {
            td.remove(key);
            currentMap.put(TICKET_DECK, td);
            this.set(cc.getID(), currentMap);
        }
    }

    public HashMap getTicketDeck() {
        DocumentSnapshot ds = this.get(cc.getID());
        HashMap td = (HashMap) ds.get(TICKET_DECK);
        return td;
    }

    public HashMap<String, Object> getTraincardDeck() {
        DocumentSnapshot ds = this.get(cc.getID());
        HashMap td = new HashMap<>();
        try {
            td = (HashMap) ds.get(TRAINCARD_DECK);
        } catch (NullPointerException ignored) {

        }
        return td;
    }

    public HashMap<String, Object> getDiscardDeck() {
        DocumentSnapshot ds = this.get(cc.getID());
        HashMap td = new HashMap();
        try {
            td = (HashMap) ds.get(DISCARD_DECK);
        } catch (NullPointerException ignored) {

        }
        return td;
    }

    public void updateTrainOrStation(String route, String trainOrStation, String color) {
        DocumentSnapshot ds = this.get(cc.getID());
        route = route.toLowerCase(Locale.ROOT);
        Map<String, Object> currentMap = ds.getData();
        HashMap<String, Object> td = (HashMap) ds.get(BOARD_STATE);
        HashMap<String, String> tosMap = new HashMap<>();
        tosMap.put(TRAIN, getTrainOrStation(route, TRAIN));
        tosMap.put(STATION, getTrainOrStation(route, STATION));
        tosMap.put(trainOrStation, color);
        td.put(route, tosMap);
        currentMap.put(BOARD_STATE, td);
        this.set(cc.getID(), currentMap);
    }
}
