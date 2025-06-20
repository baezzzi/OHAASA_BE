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
import java.time.LocalDate;

@Service
public class UserService {
    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public void createUser(UserDTO userDTO) {
        userDTO.setFirstLogin(true);
        UserEntity newUser = userDTO.toEntity();
        userRepository.save(newUser);
    }

    // zodiac_setting에서 쓰는 거 (생일, 별자리 저장)
    public void updateBirthInfoByEmail(String email, UserDTO userDTO) {
        UserEntity user = userRepository.findByEmail(email)
                .orElseThrow(() -> new IllegalArgumentException("해당 이메일로 등록된 유저가 없습니다."));

        // 생일, 별자리 등 업데이트
        user.setBirth(userDTO.getBirth());
        user.setZodiac(userDTO.getZodiac());

        userRepository.save(user); // 변경사항 저장
    }

    // 닉네임 저장하는 거
    public void updateNicknameByEmail(String email, String nickname) {
        UserEntity user = userRepository.findByEmail(email)
                .orElseThrow(() -> new IllegalArgumentException("해당 이메일로 등록된 유저가 없습니다."));

        user.setNickname(nickname);
        userRepository.save(user);
    }

    public String getNicknameByEmail(String email) {
        UserEntity userEntity = userRepository.findByEmail(email)
                .orElseThrow(() -> new IllegalArgumentException("해당 이메일로 등록된 유저가 없습니다."));
        return userEntity.getNickname();
    }

    public int getZodiacByEmail(String email) {
        UserEntity user = userRepository.findByEmail(email)
                .orElseThrow(() -> new IllegalArgumentException("해당 이메일로 등록된 유저가 없습니다."));
        return user.getZodiac();
    }

    public LocalDate getBirthByEmail(String email) {
        UserEntity user = userRepository.findByEmail(email)
                .orElseThrow(() -> new IllegalArgumentException("해당 이메일로 등록된 유저가 없습니다."));
        return user.getBirth();
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

    public void deleteUser(String email) {
        UserEntity user = userRepository.findByEmail(email)
                .orElseThrow(() -> new IllegalArgumentException("해당 이메일로 등록된 유저가 없습니다."));
        userRepository.delete(user);
    }
}