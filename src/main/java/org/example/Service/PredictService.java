package org.example.Service;

import com.google.api.core.ApiFuture;
import com.google.cloud.firestore.*;
import com.google.firebase.cloud.FirestoreClient;
import org.example.DTO.PredictDTO;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutionException;

@Service
public class PredictService {

    private final static String COLLECTION_NAME = "quiz";

    public void savePredict(String email, PredictDTO predictDTO) {
        Firestore fb = FirestoreClient.getFirestore();
        CollectionReference collectionReference = fb.collection(COLLECTION_NAME);
        String docId = collectionReference.document(email).getId();

        String todayMMdd = LocalDate.now(ZoneId.of("Asia/Seoul")).format(DateTimeFormatter.ofPattern("MM-dd"));
        Map<String, String> predict = new HashMap<>();

        String rank = predictDTO.getRank();
        String state = predictDTO.getState();

        predict.put("rank", rank);
        predict.put("state", state);

        try {
            fb.collection(COLLECTION_NAME)
                    .document(docId)
                    .collection("predict")
                    .document(todayMMdd)
                    .set(predict);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    // 당일 예측 가져오기
    public Map<String, String> getPredict(String email, String date) {
        Firestore fb = FirestoreClient.getFirestore();

        try {
            CollectionReference collectionReference = fb.collection(COLLECTION_NAME);
            String docId = collectionReference.document(email).getId();

            DocumentReference docRef = collectionReference
                    .document(docId)
                    .collection("predict")
                    .document(date);

            ApiFuture<DocumentSnapshot> future = docRef.get();
            DocumentSnapshot document = future.get();

            if (document.exists()) {
                Map<String, String> result = new HashMap<>();
                result.put("rank", document.getString("rank"));
                result.put("state", document.getString("state"));

                return result;
            }

        } catch (ExecutionException | InterruptedException e) {
            throw new RuntimeException(e);
        }

        return null;
    }
}
