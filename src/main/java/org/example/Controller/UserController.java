package org.example.Controller;

import org.example.DTO.SignInDTO;
import org.example.DTO.UserDTO;
import org.example.Entity.UserEntity;
import org.example.Repository.UserRepository;
import org.example.Service.UserService;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@Controller
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
            userService.createUser(userDTO);
            return ResponseEntity.ok("회원가입 성공!");
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping("/check-id")
    public ResponseEntity<String> checkId(@RequestParam("id") String id) {
        boolean isDuplicate = userService.isDuplicate(id);
        if (isDuplicate) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body("이미 사용 중인 아이디입니다.");
        }
        return ResponseEntity.ok("사용 가능한 아이디입니다.");
    }

    @PostMapping("/sign-in")
    public ResponseEntity<String> signIn(@RequestBody SignInDTO signInDTO) {
        boolean success = userService.checkUser(signInDTO);

        if (success) {
            return ResponseEntity.ok("로그인 성공!");
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("아이디 혹은 비밀번호가 틀렸습니다.");
        }
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
}
