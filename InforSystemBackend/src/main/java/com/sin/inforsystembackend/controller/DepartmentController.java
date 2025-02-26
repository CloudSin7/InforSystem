package com.sin.inforsystembackend.controller;

import com.sin.inforsystembackend.entity.DepartmentDTO;
import com.sin.inforsystembackend.entity.User;
import com.sin.inforsystembackend.service.DepartmentService;
import com.sin.inforsystembackend.service.UserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/departments")
public class DepartmentController {

    @Autowired
    private DepartmentService departmentService;

    @Autowired
    private UserService userService;  // 注入 UserService


    /**
     * 获取所有部门信息（无分页）
     * @return 部门列表
     */
    @GetMapping("/all")
    public ResponseEntity<List<DepartmentDTO>> getAllDepartments() {
        List<DepartmentDTO> departments = departmentService.getAllDepartments();
        return ResponseEntity.ok(departments);
    }

    /**
     * 根据部门 ID 查询部门
     * @param departmentId 部门 ID
     * @return 匹配的部门对象
     */
    @GetMapping("/{id}")
    public ResponseEntity<DepartmentDTO> getDepartmentById(@PathVariable("id") Integer departmentId) {
        DepartmentDTO departmentDTO = departmentService.getDepartmentDTOById(departmentId);
        if (departmentDTO != null) {
            return ResponseEntity.ok(departmentDTO);
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
    }

    /**
     * 添加新部门
     * @param departmentDTO 新部门对象
     * @return 创建的部门对象
     */
    @PostMapping
    public ResponseEntity<DepartmentDTO> addDepartment(@Valid @RequestBody DepartmentDTO departmentDTO) {
        DepartmentDTO createdDepartment = departmentService.addDepartment(departmentDTO);
        if (createdDepartment != null) {
            return ResponseEntity.status(HttpStatus.CREATED).body(createdDepartment);
        }
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
    }

    /**
     * 删除部门
     * @param departmentId 部门 ID
     * @return 无内容响应
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteDepartment(@PathVariable("id") Integer departmentId) {
        boolean isDeleted = departmentService.deleteDepartment(departmentId);
        if (isDeleted) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
    }

    /**
     * 按名称模糊查询部门
     * @param keyword 查询关键词
     * @return 匹配的部门列表
     */
    @GetMapping("/search")
    public ResponseEntity<List<DepartmentDTO>> searchDepartmentsByName(@RequestParam("keyword") String keyword) {
        List<DepartmentDTO> departments = departmentService.searchDepartmentsByName(keyword);
        return ResponseEntity.ok(departments);
    }


    /**
     * 获取某部门下的所有用户
     * @param departmentId 部门 ID
     * @return 属于该部门的用户列表
     */
    @GetMapping("/{id}/users")
    public ResponseEntity<List<User>> getUsersByDepartmentId(@PathVariable("id") Integer departmentId) {
        // 使用实例调用 userService 的方法
        List<User> users = userService.findUsersByDepartmentId(departmentId);
        return ResponseEntity.ok(users);
    }

    /**
     * 添加新用户
     * @param newUser 新用户信息
     * @return 新用户对象
     */
    @PostMapping("/user")
    public ResponseEntity<User> addUser(@RequestBody User newUser) {
        User user = departmentService.addUser(newUser);
        return ResponseEntity.ok(user);
    }

    /**
     * 更新用户信息
     * @param userId 用户ID
     * @param updatedUser 更新后的用户信息
     * @return 已更新的用户对象
     */
    @PutMapping("/user/{userId}")
    public ResponseEntity<User> updateUser(@PathVariable Integer userId, @RequestBody User updatedUser) {
        User user = departmentService.updateUser(userId, updatedUser);
        return ResponseEntity.ok(user);
    }

    /**
     * 删除用户
     * @param userId 用户ID
     * @return 响应内容
     */
    @DeleteMapping("/user/{userId}")
    public ResponseEntity<Void> deleteUser(@PathVariable Integer userId) {
        departmentService.deleteUser(userId);
        return ResponseEntity.noContent().build();
    }
}