package ttr.Controllers;

import com.google.cloud.firestore.DocumentSnapshot;

public interface Controller {
    void update(DocumentSnapshot ds);
}
