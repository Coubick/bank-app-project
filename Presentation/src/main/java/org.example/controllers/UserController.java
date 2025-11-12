package org.example.controllers;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.example.services.UserService;
import org.example.dto.UserDTO;
import org.example.enums.AddFindDeleteResult;
import org.example.enums.Colors;
import org.example.enums.Gender;
import org.example.mappers.UserMapper;
import org.example.user_dao.User;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v3/bank_system/users")
@Tag(name = "Users", description = "User management API")
public class UserController {
    private final UserService userService;
    private final UserMapper userMapper;

    public UserController(UserService userService, UserMapper userMapper) {
        this.userService = userService;
        this.userMapper = userMapper;
    }

    @Operation(summary = "Create a new user")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "User created"),
            @ApiResponse(responseCode = "409", description = "User with this login already exists"),
            @ApiResponse(responseCode = "400", description = "Invalid user data")
    })
    @PostMapping
    public ResponseEntity<Void> createUser(@RequestBody UserDTO userDTO) {
        if (userService.findUserByLogin(userDTO.getLogin()) != null) {
            return ResponseEntity.status(HttpStatus.CONFLICT).build(); // 409
        }
        userService.addUser(userMapper.toEntity(userDTO));
        return ResponseEntity.status(HttpStatus.CREATED).build(); // 201
    }

    @Operation(summary = "Get information about user")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "User info returned"),
            @ApiResponse(responseCode = "404", description = "User not found")
    })

    @GetMapping("/{login}")
    public ResponseEntity<UserDTO> getUser(@PathVariable String login) {
        User user = userService.findUserByLogin(login);
        if (user == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build(); // 404
        }
        return ResponseEntity.ok(userMapper.toDto(user)); // 200
    }

    @Operation(summary = "Filter users by gender and/or hair color")
    @ApiResponse(responseCode = "200", description = "Filtered user list")
    @GetMapping
    public ResponseEntity<List<UserDTO>> getFilteredUsers(
            @RequestParam(required = false) Gender gender,
            @RequestParam(required = false) Colors color) {
        List<User> filteredUsers = userService.filterUsers(gender, color);
        return ResponseEntity.ok(userMapper.toDtoList(filteredUsers)); // 200
    }

    @Operation(summary = "Add a friend")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Friend added successfully"),
            @ApiResponse(responseCode = "409", description = "Friendship already exists"),
            @ApiResponse(responseCode = "404", description = "User not found")
    })

    @PutMapping("/{login1}/friends/{login2}")
    public ResponseEntity<Void> addFriend(@PathVariable String login1, @PathVariable String login2){
        AddFindDeleteResult result = userService.addFriend(login1, login2);
        return switch (result) {
            case Success -> ResponseEntity.ok().build();
            case AlreadyExists -> ResponseEntity.status(HttpStatus.CONFLICT).build();
            case UserNotFound -> ResponseEntity.status(HttpStatus.NOT_FOUND).build();
            default -> ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        };
    }

    @DeleteMapping("/{login1}/friends/{login2}")
    public ResponseEntity<String> deleteFriend(@PathVariable String login1, @PathVariable String login2) {
        AddFindDeleteResult result = userService.removeFriend(login1, login2);
        return switch (result) {
            case Success -> ResponseEntity.ok().body("User deleted successfully");
            case UserNotFound -> ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found");
            case UserNotFriend -> ResponseEntity.status(HttpStatus.CONFLICT).body("You don't have this user in your friends list");
            default -> ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Something went wrong");
        };
    }

    @Operation(summary = "Get all friends of a user")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Friends list returned"),
            @ApiResponse(responseCode = "404", description = "User not found")
    })
    @GetMapping("/{login}/friends")
    public ResponseEntity<List<UserDTO>> getFriends(@PathVariable String login) {
        List<User> friends = userService.findFriends(login);
        if (friends.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
        return ResponseEntity.ok(userMapper.toDtoList(friends));
    }

}
