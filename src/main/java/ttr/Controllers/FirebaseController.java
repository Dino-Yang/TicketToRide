package ttr.Controllers;

import com.google.cloud.firestore.DocumentSnapshot;

public class FirebaseController implements Controller {

    @Override
    public void update(DocumentSnapshot ds) {
        GameStartController.getInstance().update(ds);
        BoardController.getInstance().update(ds);
    }
}

