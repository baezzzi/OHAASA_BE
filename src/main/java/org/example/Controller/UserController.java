package org.example.Controller;

import com.google.api.core.ApiFuture;
import com.google.cloud.firestore.*;
import com.google.firebase.cloud.FirestoreClient;
import org.example.DTO.UserDTO;
import org.example.Service.UserService;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutionException;

@RestController
@RequestMapping("/users")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/sign-up")
    public ResponseEntity<String> signUp(@RequestBody UserDTO userDTO) {
        try {
            // email, uid 전달받음
            userService.createUser(userDTO);
            return ResponseEntity.ok("회원가입 성공!");
        } catch (ExecutionException | InterruptedException  e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PostMapping("/birth")
    public ResponseEntity<String> birth(@RequestParam String email, @RequestBody UserDTO userDTO) {
        try {
            userService.updateBirthInfoByEmail(email, userDTO);
            return ResponseEntity.ok("별자리와 생일 저장완료");
        } catch (ExecutionException | InterruptedException  e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping("/find-birth")
    public Date findBirth(@RequestParam String email) {
        try {
            return userService.getBirthByEmail(email);
        } catch (ExecutionException | InterruptedException  e) {
            return new Date();
        }
    }

    // 튜토리얼 true 일 때
    @GetMapping("/is-first-login")
    public ResponseEntity<Map<String, Boolean>> isFirstLogin(@RequestParam String email) throws InterruptedException, ExecutionException {
        Firestore db = FirestoreClient.getFirestore();
        CollectionReference collection = db.collection("users");

        // email 로 사용자 문서 검색
        Query query = collection.whereEqualTo("email", email);
        ApiFuture<QuerySnapshot> querySnapshot = query.get();
        QuerySnapshot snapshot = querySnapshot.get();

        Map<String, Boolean> response = new HashMap<>();
        if (snapshot.isEmpty()) {
            // 사용자가 없으면 false 반환 (또는 예외 처리 가능)
            response.put("firstLogin", false);
        } else {
            DocumentSnapshot document = snapshot.getDocuments().get(0);
            Boolean firstLogin = document.getBoolean("firstLogin");
            response.put("firstLogin", firstLogin != null && firstLogin);
        }
        return ResponseEntity.ok(response);
    }


    // 튜토리얼 완료
    @PostMapping("/complete-tutorial")
    public ResponseEntity<String> completeTutorial(@RequestBody Map<String, String> request){
        Firestore db = FirestoreClient.getFirestore();
        CollectionReference collection = db.collection("users");

        Map<String, Object> updates = new HashMap<>();
        updates.put("firstLogin", false);

        DocumentReference documentReference = collection.document(request.get("email"));
        ApiFuture<WriteResult> writeResult = documentReference.update(updates);
        return ResponseEntity.ok("튜토리얼 완료");
    }

    @GetMapping("/find-nickname")
    public String getNicknameById(@RequestParam String email) {
        try {
            return userService.getNicknameByEmail(email);
        } catch (ExecutionException | InterruptedException  e) {
            return e.getMessage();
        }
    }

    @PostMapping("/save-nickname")
    public ResponseEntity<String> saveNickname(@RequestBody UserDTO userDTO) {
        String email = userDTO.getEmail();
        String nickname = userDTO.getNickname();
        try{
            userService.updateNicknameByEmail(email, nickname);
            return ResponseEntity.ok("닉네임 저장 완료");
        }  catch (ExecutionException | InterruptedException  e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping("/find-zodiac")
    public int getZodiacByEmail(@RequestParam String email) {
        try {
            return userService.getZodiacByEmail(email);
        } catch (ExecutionException | InterruptedException  e) {
            return Integer.parseInt(e.getCause().getMessage());
        }
    }

    @PostMapping("/delete-user")
    public ResponseEntity<String> deleteUser(@RequestBody UserDTO userDTO) {
        String email = userDTO.getEmail();
        try {
            userService.deleteUser(email);
            return ResponseEntity.ok("사용자 삭제");
        } catch (ExecutionException | InterruptedException  e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping("/find-user")
    public ResponseEntity<String> findUser(@RequestParam String email) {
        try {
            return userService.findUser(email);
        } catch (ExecutionException | InterruptedException  e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}
