package org.example.Service;

import com.google.api.core.ApiFuture;
import com.google.api.gax.rpc.ApiException;
import com.google.cloud.firestore.*;
import com.google.firebase.cloud.FirestoreClient;
import org.springframework.stereotype.Service;

import org.example.DTO.FriendDTO;
import org.example.Entity.FriendEntity;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.concurrent.ExecutionException;

@Service
public class FriendService {



    public void addFriend(FriendDTO friendDTO) throws Exception {
        Firestore db = FirestoreClient.getFirestore();
        CollectionReference friends = db.collection("friends");
        FriendEntity newFriend = friendDTO.toEntity();

        String email = friendDTO.getEmail();
        String name = friendDTO.getName();

        try {
            ApiFuture<WriteResult> future = friends
                    .document(email)
                    .collection("friendList")
                    .document(name)
                    .set(newFriend);
            future.get();
        } catch (ApiException e) {
            throw new RuntimeException(e);
        }
    }

    public List<Map<String, String>> findFriend(String email, LocalDate date) throws InterruptedException, ExecutionException {
        Firestore db = FirestoreClient.getFirestore();

        // 날짜를 "MM-dd" 포맷으로 변환
        String dateDoc = date.format(DateTimeFormatter.ofPattern("MM-dd"));

        // 친구 리스트 가져오기
        CollectionReference friendCollection = db.collection("friends").document(email).collection("friendList");
        ApiFuture<QuerySnapshot> friendFuture = friendCollection.get();
        List<QueryDocumentSnapshot> friendList = friendFuture.get().getDocuments();

        // 결과 리스트
        List<Map<String, String>> result = new ArrayList<>();

        for (QueryDocumentSnapshot friendDoc : friendList) {
            Map<String, String> item = new HashMap<>();
            String friendZodiac = friendDoc.getString("zodiac");
            item.put("name", friendDoc.getString("name"));
            item.put("zodiac", friendZodiac);

            // today/{dateDoc}/zodiacList/{friendZodiac} 문서에서 rank 가져오기
            DocumentReference zodiacDocRef = db
                    .collection("today")
                    .document(dateDoc)
                    .collection("zodiacList")
                    .document(friendZodiac);

            ApiFuture<DocumentSnapshot> zodiacFuture = zodiacDocRef.get();
            DocumentSnapshot zodiacDoc = zodiacFuture.get();

            String rank = "";
            if (zodiacDoc.exists()) {
                Object rankObj = zodiacDoc.get("rank");
                rank = rankObj != null ? rankObj.toString() : "";
            }
            item.put("rank", rank);

            result.add(item);
        }

        return result;
    }


    public void deleteFriend(FriendDTO friendDTO) throws InterruptedException, ExecutionException {
        Firestore db = FirestoreClient.getFirestore();
        CollectionReference friends = db.collection("friends");

        // 친구 이름 가지고 삭제하는 걸로
        String email = friendDTO.getEmail();
        String name = friendDTO.getName();

        CollectionReference friendCollection = friends.document(email).collection("friendList");

        Query findQuery = friendCollection.whereEqualTo("name", name);
        ApiFuture<QuerySnapshot> querySnapshot = findQuery.get();
        QuerySnapshot snapshot = querySnapshot.get();

        if (snapshot.isEmpty()) {
            throw new IllegalArgumentException("해당 친구가 존재하지 않습니다.");
        }

        for (DocumentSnapshot doc : snapshot.getDocuments()) {
            friendCollection.document(doc.getId()).delete();
            break;
        }
    }

}
