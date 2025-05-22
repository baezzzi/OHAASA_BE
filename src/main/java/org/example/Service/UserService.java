package org.example.Service;

import org.example.DTO.UserDTO;
import org.example.DTO.SignInDTO;
import org.example.Entity.UserEntity;
import org.example.Repository.UserRepository;
import org.springframework.stereotype.Service;

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

}
