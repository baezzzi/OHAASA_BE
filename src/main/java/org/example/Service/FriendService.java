package org.example.Service;

import org.example.DTO.FriendDTO;
import org.example.Entity.FriendEntity;
import org.example.Repository.FriendRepository;
import org.example.Repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class FriendService {
    private final FriendRepository friendRepository;
    private final UserRepository userRepository;

    public FriendService(FriendRepository friendRepository, UserRepository userRepository) {
        this.friendRepository = friendRepository;
        this.userRepository = userRepository;
    }

    public void addFriend(FriendDTO friendDTO) {
        FriendEntity newFriend = friendDTO.toEntity();
        friendRepository.save(newFriend);
    }

    public List<Map<String, String>> findFriend(String email) {
        userRepository.findByEmail(email)
            .orElseThrow(() -> new IllegalArgumentException("이메일로 등록된 유저가 없습니다."));

        List<FriendEntity> friends = friendRepository.findByEmail(email);
        List<Map<String, String>> result = new ArrayList<>();
        for (FriendEntity friend : friends) {
            Map<String, String> item = new HashMap<>();
            item.put("id", String.valueOf(friend.getId()));
            item.put("name", friend.getName());
            item.put("zodiac", friend.getZodiac());
            result.add(item);
        }
        return result;
    }

    public void deleteFriend(FriendDTO friendDTO) {
        String email = friendDTO.getEmail();
        int id = friendDTO.getId();
        userRepository.findByEmail(email).orElseThrow(() -> new IllegalArgumentException("이메일로 등록된 유저가 없습니다."));

        FriendEntity specFriend = friendRepository.findById(id)
                        .orElseThrow(() -> new IllegalArgumentException("해당 친구가 존재하지 않습니다.."));

        friendRepository.delete(specFriend);
    }

    public String findFriendName(int id) {
        FriendEntity friend = friendRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("해당 친구가 존재하지 않습니다."));

        return friend.getName();
    }

    public String findFriendZodiac(int id) {
        FriendEntity friend = friendRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("해당 친구가 존재하지 않습니다."));

        return friend.getZodiac();
    }
}
