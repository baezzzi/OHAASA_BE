package org.example.Controller;

import org.example.DTO.UserDTO;
import org.example.Entity.UserEntity;
import org.example.Repository.UserRepository;
import org.example.Service.UserService;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/users")
public class UserController {

    private final UserService userService;
    private final UserRepository userRepository;

    public UserController(UserService userService, UserRepository userRepository) {
        this.userService = userService;
        this.userRepository = userRepository;
    }

    @GetMapping("/")
    public String home() {
        System.out.print("hello");
        return "home";
    }

    @PostMapping("/sign-up")
    public ResponseEntity<String> signUp(@RequestBody UserDTO userDTO) {
        try {
            // email, uid 전달받음
            userService.createUser(userDTO);
            return ResponseEntity.ok("회원가입 성공!");
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PostMapping("/birth")
    public ResponseEntity<String> birth(@RequestParam String email, @RequestBody UserDTO userDTO) {
        try {
            userService.updateBirthInfoByEmail(email, userDTO);
            System.out.println(userDTO);
            return ResponseEntity.ok("별자리와 생일 저장완료");
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping("/find-birth")
    public LocalDate findBirth(@RequestParam String email) {
        return userService.getBirthByEmail(email);
    }

    // 튜토리얼 true일 때
    @GetMapping("/is-first-login")
    public ResponseEntity<Map<String, Boolean>> isFirstLogin(@RequestParam String email) {
        UserEntity user = userRepository.findByEmail(email)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 회원입니다."));

        Map<String, Boolean> response = new HashMap<>();
        response.put("firstLogin", user.isFirstLogin());
        System.out.println(user.isFirstLogin());
        return ResponseEntity.ok(response);
    }

    // 튜토리얼 완료
    @PostMapping("/complete-tutorial")
    public ResponseEntity<String> completeTutorial(@RequestBody Map<String, String> request) {
        String email = request.get("email");

        UserEntity user = userRepository.findByEmail(email)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 이메일입니다."));

        user.setFirstLogin(false);
        userRepository.save(user);

        return ResponseEntity.ok("튜토리얼 완료");
    }

    @PostMapping("/{id}/profile-image")
    public ResponseEntity<?> uploadProfileImage(@PathVariable("id") String id, @RequestParam("file") MultipartFile file) {
        try {
            String filename = userService.saveImage(id, file);
            UserEntity user = userRepository.findById(id)
                    .orElseThrow(() -> new RuntimeException("User not fount"));
            user.setImage(filename);
            userRepository.save(user);
            return ResponseEntity.ok("이미지 저장 완료");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping("/find-nickname")
    public String getNicknameById(@RequestParam String email) {
        return userService.getNicknameByEmail(email);
    }

    @PostMapping("/save-nickname")
    public ResponseEntity<String> saveNickname(@RequestBody UserDTO userDTO) {
        String email = userDTO.getEmail();
        String nickname = userDTO.getNickname();
        try{
            userService.updateNicknameByEmail(email, nickname);
            return ResponseEntity.ok("닉네임 저장 완료");
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping("/find-zodiac")
    public int getZodiacByEmail(@RequestParam String email) {
        try {
            return userService.getZodiacByEmail(email);
        } catch (RuntimeException e) {
            throw new RuntimeException(e);
        }
    }

    @PostMapping("/delete-user")
    public ResponseEntity<String> deleteUser(@RequestBody UserDTO userDTO) {
        String email = userDTO.getEmail();
        try {
            userService.deleteUser(email);
            return ResponseEntity.ok("사용자 삭제");
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}
