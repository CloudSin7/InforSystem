package com.sin.inforsystembackend.controller;

import com.sin.inforsystembackend.entity.Department;
import com.sin.inforsystembackend.dto.DepartmentDTO;
import com.sin.inforsystembackend.entity.User;
import com.sin.inforsystembackend.service.UserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/users")
public class UserController {

    @Autowired
    private UserService userService;

    /**
     * 用户登录
     * @param email 用户邮箱
     * @param password 用户密码
     * @return 用户对象
     */
    @PostMapping("/login")
    public ResponseEntity<User> login(@RequestParam("email") String email, @RequestParam("password") String password) {
        User user = userService.login(email, password);
        if (user != null) {
            return ResponseEntity.ok(user);
        }
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
    }

    /**
     * 按邮箱查询用户
     * @param email 用户邮箱
     * @return 用户对象
     */
    @GetMapping("/email")
    public ResponseEntity<User> getUserByEmail(@RequestParam("email") String email) {
        User user = userService.findUserByEmail(email);
        if (user != null) {
            return ResponseEntity.ok(user);
        }
        return ResponseEntity.notFound().build();
    }

    /**
     * 添加新用户
     * @param user 新用户对象
     * @return 创建的用户对象
     */
    @PostMapping
    public ResponseEntity<User> addUser(@Valid @RequestBody User user) {
        User createdUser = userService.addUser(user);
        if (createdUser != null) {
            return ResponseEntity.status(HttpStatus.CREATED).body(createdUser);
        }
        return ResponseEntity.badRequest().build();
    }

    // 修改用户信息
    @PutMapping("/user/{userId}")
    public ResponseEntity<User> updateUser(@PathVariable("userId") Integer userId, @Valid @RequestBody User updatedUser) {
        User user = userService.updateUser(userId, updatedUser);
        if (user != null) {
            return ResponseEntity.ok(user);
        }
        return ResponseEntity.notFound().build();
    }

    // 删除用户
    @DeleteMapping("/user/{userId}")
    public ResponseEntity<Void> deleteUser(@PathVariable Integer userId) {
        System.out.println("Attempting to delete user with ID: " + userId);  // Debug log
        userService.deleteUser(userId);
        return ResponseEntity.noContent().build();  // Success response
    }

    /**
     * 按多条件筛选用户
     * @param username 用户名（可选）
     * @param email 邮箱（可选）
     * @param departmentId 部门 ID（可选）
     * @return 匹配的用户列表
     */
    @GetMapping("/search")
    public ResponseEntity<List<User>> searchUsers(
            @RequestParam(value = "username", required = false) String username,
            @RequestParam(value = "email", required = false) String email,
            @RequestParam(value = "departmentId", required = false) Integer departmentId) {
        List<User> users = userService.findUsersByConditions(username, email, departmentId);
        return ResponseEntity.ok(users);
    }

    /**
     * 分页查询用户
     * @param page 页码（从 0 开始）
     * @param size 每页大小
     * @return 分页用户列表
     */
    @GetMapping
    public ResponseEntity<Page<User>> getUsersWithPagination(
            @RequestParam(value = "page", defaultValue = "0") int page,
            @RequestParam(value = "size", defaultValue = "10") int size) {
        Page<User> users = userService.findUsersWithPagination(PageRequest.of(page, size));
        return ResponseEntity.ok(users);
    }

    /**
     * 获取用户所属的部门列表
     * @param userId 用户ID
     * @return 用户所属的部门列表
     */
    @GetMapping("/user/{userId}/departments")
    public ResponseEntity<List<DepartmentDTO>> getUserDepartments(@PathVariable("userId") Integer userId) {
        List<DepartmentDTO> departmentDTOs = userService.getUserDepartmentsAsDTO(userId);
        return ResponseEntity.ok(departmentDTOs);
    }
}