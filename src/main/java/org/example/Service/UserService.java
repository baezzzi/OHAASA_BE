package org.example.Service;

import com.google.api.core.ApiFuture;
import com.google.api.gax.rpc.ApiException;
import com.google.cloud.firestore.*;
import com.google.firebase.cloud.FirestoreClient;
import org.example.DTO.UserDTO;
import org.example.Entity.UserEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ExecutionException;

@Service
public class UserService {

    private final static String COLLECTION_NAME = "users";

    public String createUser(UserDTO userDTO) throws ExecutionException, InterruptedException {
        Firestore db = FirestoreClient.getFirestore();

        userDTO.setFirstLogin(true);
        UserEntity newUser = userDTO.toEntity();

        // 이메일이 문서 Id
        String docId = newUser.getEmail();
        String token = newUser.getFcmToken();

        try {
            ApiFuture<WriteResult> future = db
                    .collection("users")
                    .document(docId)
                    .set(newUser);
            future.get();
            return "fcmtoken" + token;
        } catch (ApiException e) {
            throw new RuntimeException(e);
        }
    }

    // zodiac setting 에서 쓰는 거 (생일, 별자리 저장)
    public void updateBirthInfoByEmail(String email, UserDTO userDTO) throws InterruptedException, ExecutionException {
        Firestore db = FirestoreClient.getFirestore();
        CollectionReference collection = db.collection(COLLECTION_NAME);

        String docId = collection.document(email).getId();

        Map<String, Object> updates = new HashMap<>();
        updates.put("birth", userDTO.getBirth());
        updates.put("zodiac", userDTO.getZodiac());

        DocumentReference docRef = collection.document(docId);
        try {
            ApiFuture<WriteResult> writeResult = docRef.update(updates);
            writeResult.get(); // 예외 발생 시 catch 로 이동
        } catch (InterruptedException | ExecutionException e) {
            // 예외 처리 로직
            throw new RuntimeException("업데이트 실패", e);
        }

    }

    // 닉네임 저장하는 거
    public void updateNicknameByEmail(String email, String nickname) throws InterruptedException, ExecutionException {
        Firestore db = FirestoreClient.getFirestore();
        CollectionReference collection = db.collection(COLLECTION_NAME);

        Query query = collection.whereEqualTo("email", email);
        ApiFuture<QuerySnapshot> querySnapshot = query.get();
        QuerySnapshot snapshot = querySnapshot.get();

        if (snapshot.isEmpty()) {
            throw new IllegalArgumentException("해당 이메일로 등록된 사용자가 없습니다.");
        }

        DocumentSnapshot document = snapshot.getDocuments().get(0);

        Map<String, Object> updates = new HashMap<>();
        updates.put("nickname", nickname);

        DocumentReference docRef = collection.document(document.getId());
        try {
            ApiFuture<WriteResult> writeResult = docRef.update(updates);
            writeResult.get();
        } catch (InterruptedException | ExecutionException e) {
            throw new RuntimeException("업데이트 실패", e);
        }
    }

    public String getNicknameByEmail(String email) throws InterruptedException, ExecutionException {
        Firestore db = FirestoreClient.getFirestore();
        CollectionReference collection = db.collection(COLLECTION_NAME);

        Query query = collection.whereEqualTo("email", email);
        ApiFuture<QuerySnapshot> querySnapshot = query.get();
        QuerySnapshot snapshot = querySnapshot.get();

        if (snapshot.isEmpty()) {
            throw new IllegalArgumentException("해당 이메일로 등록된 사용자가 없습니다.");
        }

        DocumentSnapshot document = snapshot.getDocuments().get(0);
        return document.getString("nickname");
    }

    public int getZodiacByEmail(String email) throws InterruptedException, ExecutionException {
        Firestore db = FirestoreClient.getFirestore();
        CollectionReference collection = db.collection(COLLECTION_NAME);

        Query query = collection.whereEqualTo("email", email);
        ApiFuture<QuerySnapshot> querySnapshot = query.get();
        QuerySnapshot snapshot = querySnapshot.get();

        if (snapshot.isEmpty()) {
            throw new IllegalArgumentException("해당 이메일로 등록된 사용자가 없습니다.");
        }

        DocumentSnapshot document = snapshot.getDocuments().get(0);
        Long zodiacLong = document.getLong("zodiac");

        return Objects.requireNonNull(zodiacLong).intValue();
    }

    public Date getBirthByEmail(String email) throws InterruptedException, ExecutionException {
        Firestore db = FirestoreClient.getFirestore();
        CollectionReference collection = db.collection(COLLECTION_NAME);

        Query query = collection.whereEqualTo("email", email);
        ApiFuture<QuerySnapshot> querySnapshot = query.get();
        QuerySnapshot snapshot = querySnapshot.get();

        if (snapshot.isEmpty()) {
            throw new IllegalArgumentException("해당 이메일로 등록된 사용자가 없습니다.");
        }

        DocumentSnapshot document = snapshot.getDocuments().get(0);
        return document.getDate("birth");
    }

    public void deleteUser(String email) throws InterruptedException, ExecutionException {
        Firestore db = FirestoreClient.getFirestore();
        CollectionReference collection = db.collection(COLLECTION_NAME);

        Query query = collection.whereEqualTo("email", email);
        ApiFuture<QuerySnapshot> querySnapshot = query.get();
        QuerySnapshot snapshot = querySnapshot.get();

        if (snapshot.isEmpty()) {
            throw new IllegalArgumentException("해당 이메일로 등록된 사용자가 없습니다.");
        }

        DocumentSnapshot document = snapshot.getDocuments().get(0);
        collection.document(document.getId()).delete();
    }

    public ResponseEntity<String> findUser(String email) throws InterruptedException, ExecutionException {
        Firestore db = FirestoreClient.getFirestore();
        CollectionReference collection = db.collection(COLLECTION_NAME);

        Query query = collection.whereEqualTo("email", email);
        ApiFuture<QuerySnapshot> querySnapshot = query.get();
        QuerySnapshot snapshot = querySnapshot.get();

        if(snapshot.isEmpty()) {
            return ResponseEntity.badRequest().build();
        }

        DocumentSnapshot document = snapshot.getDocuments().get(0);
        return ResponseEntity.ok().body(document.getString("nickname"));
    }
}