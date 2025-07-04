package org.example.Controller;

import org.example.DTO.FriendDTO;
import org.example.Service.FriendService;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;

@RestController
@RequestMapping("/friend")
public class FriendController {

    private final FriendService friendService;

    public FriendController(FriendService friendService) {
        this.friendService = friendService;
    }

    @PostMapping("/add-friend")
    public ResponseEntity<String> addFriend(@RequestBody FriendDTO friendDTO) {
        try {
            friendService.addFriend(friendDTO);
            return ResponseEntity.ok().body("친구 추가 완료");
        } catch (InterruptedException | ExecutionException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @GetMapping(value = "/find-friend", produces = MediaType.APPLICATION_JSON_VALUE + ";charset=UTF-8")
    public List<Map<String, String>> findFriend(@RequestParam String email) {
        LocalDate date = LocalDate.now();
        try {
            return friendService.findFriend(email, date);
        } catch (InterruptedException | ExecutionException e) {
            return (List<Map<String, String>>) e;
        }
    }

    @PostMapping("/delete-friend")
    public void deleteFriend(@RequestBody FriendDTO friendDTO) {
        try {
            friendService.deleteFriend(friendDTO);
        } catch (InterruptedException | ExecutionException e) {
            throw new RuntimeException(e);
        }
    }
}
