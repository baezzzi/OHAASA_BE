package org.example.Controller;

import org.example.DTO.FriendDTO;
//import org.example.Repository.FriendRepository;
import org.example.Service.FriendService;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/friend")
public class FriendController {

    private final FriendService friendService;
//    private final FriendRepository friendRepository;

    public FriendController(FriendService friendService) {
        this.friendService = friendService;
//        this.friendRepository = friendRepository;
    }

    @PostMapping("/add-friend")
    public ResponseEntity<String> addFriend(@RequestBody FriendDTO friendDTO) {
        try {
            friendService.addFriend(friendDTO);
            return ResponseEntity.ok().body("친구 추가 완료");
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping(value = "/find-friend", produces = MediaType.APPLICATION_JSON_VALUE + ";charset=UTF-8")
    public List<Map<String, String>> findFriend(@RequestParam String email) {
        LocalDate date = LocalDate.now();
        return friendService.findFriend(email, date);
    }

    @PostMapping("/delete-friend")
    public void deleteFriend(@RequestBody FriendDTO friendDTO) {
        friendService.deleteFriend(friendDTO);
    }

    @GetMapping("/find-friend-name")
    public String findFriendName(@RequestParam int id) {
        return friendService.findFriendName(id);
    }

    @GetMapping("/find-friend-zodiac")
    public String findFriendZodiac(@RequestParam int id) {
        return friendService.findFriendZodiac(id);
    }
}
