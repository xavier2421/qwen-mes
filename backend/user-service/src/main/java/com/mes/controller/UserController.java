package com.mes.controller;

import com.mes.dto.ApiResponse;
import com.mes.dto.UserDTO;
import com.mes.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/api/v1/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @PostMapping
    public ApiResponse<UserDTO> createUser(@Valid @RequestBody UserDTO userDTO) {
        UserDTO created = userService.createUser(userDTO);
        return ApiResponse.success("用户创建成功", created);
    }

    @PutMapping("/{id}")
    public ApiResponse<UserDTO> updateUser(@PathVariable Long id, @Valid @RequestBody UserDTO userDTO) {
        UserDTO updated = userService.updateUser(id, userDTO);
        return ApiResponse.success("用户更新成功", updated);
    }

    @DeleteMapping("/{id}")
    public ApiResponse<Void> deleteUser(@PathVariable Long id) {
        userService.deleteUser(id);
        return ApiResponse.success("用户删除成功", null);
    }

    @GetMapping("/{id}")
    public ApiResponse<UserDTO> getUserById(@PathVariable Long id) {
        UserDTO user = userService.getUserById(id);
        return ApiResponse.success(user);
    }

    @GetMapping
    public ApiResponse<List<UserDTO>> getAllUsers(@RequestParam Long tenantId) {
        List<UserDTO> users = userService.getAllUsers(tenantId);
        return ApiResponse.success(users);
    }
}
