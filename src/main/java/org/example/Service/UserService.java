package org.example.Service;

import org.example.DTO.UserDTO;
import org.example.DTO.SignInDTO;
import org.example.Entity.UserEntity;
import org.example.Repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.swing.text.html.parser.Entity;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;

@Service
public class UserService {
    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public void createUser(UserDTO userDTO) {
        if(!userDTO.getPw().equals((userDTO.getCheckpw()))) {
            throw new IllegalArgumentException("비밀번호가 일치하지 않습니다.");
        }
        UserEntity newUser = userDTO.toEntity();
        userRepository.save(newUser);
    }

    public boolean isDuplicate(String id) {
        return userRepository.findById(id).isPresent();
    }

    public String getNicknameById(String id) {
        UserEntity userEntity = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("사용자를 찾을 수 없습니다."));
        return userEntity.getNickname();
    }

    public boolean checkUser(SignInDTO signInDTO) {
        if (signInDTO == null) {
            throw new IllegalArgumentException("입력값이 올바르지 않습니다.");
        }

        // DB에서 id로 유저 조회
        UserEntity userEntity = userRepository.findById(signInDTO.getId())
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 아이디입니다."));

        if (!signInDTO.getPw().equals(userEntity.getPw())) {
            throw new IllegalArgumentException("아이디 혹은 비밀번호가 틀렸습니다.");
        }
        return true;
    }

    public String saveImage(String id, MultipartFile file) throws IOException {
        String imagePath = "/Users/jiyeon/Desktop/ohaasa";

        if(file.isEmpty()) {
            throw new IllegalArgumentException("빈 파일은 저장할 수 없습니다.");
        }

        // 확장자 확인
        String originalFilename = file.getOriginalFilename();
        String extension = originalFilename.substring(originalFilename.lastIndexOf("."));
        if (!extension.matches("\\.(jpg|jpeg|png)$")) {
            throw new IllegalArgumentException("지원하지 않는 이미지 형식입니다.");
        }

        // 저장할 파일 이름
        String filname = id + "_" + extension;
        // 프로필 이미지 저장하는 로직
        Path filePath = Paths.get(imagePath, filname);

        // 파일저장
        file.transferTo(filePath.toFile());
        return filname;
    }
}