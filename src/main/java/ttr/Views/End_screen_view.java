package ttr.Views;
import javafx.scene.text.Text;
import ttr.Constants.ClientConstants;
import ttr.Services.FirestoreService;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import static ttr.Constants.ClientConstants.FINAL_SCORES;

public class End_screen_view{
    public Text Number_1;
    public Text Number_2;
    public Text Number_3;
    public Text Number_4;
    public Text Number_5;
    private FirestoreService fs;
    private ClientConstants cc = new ClientConstants();
    private ArrayList<String> scoreList = new ArrayList<>();
    private ArrayList<Text> textField = new ArrayList<>();

    public void initialize(){
        fs = FirestoreService.getInstance();
        Collections.addAll(textField,Number_1, Number_2, Number_3, Number_4, Number_5);
        mapToList();
        setScore();
    }

    public void mapToList() {
        HashMap<String, Object> scores = (HashMap<String, Object>) fs.get(cc.getID()).get(FINAL_SCORES);
        for (Map.Entry<String, Object> entry : scores.entrySet()) {
            String name = entry.getKey();
            String score = entry.getValue().toString();
            String name_score = name + ": " + score;
            scoreList.add(name_score);
        }
    }

    public void setScore(){
        for (int i = 0; i < scoreList.size(); i++) {
            textField.get(i).setText(scoreList.get(i));
        }
    }
}
