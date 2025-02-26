package com.sin.inforsystembackend.service;

import com.sin.inforsystembackend.dto.NotificationSendDTO;
import com.sin.inforsystembackend.dto.DepartmentDTO;
import com.sin.inforsystembackend.entity.*;
import com.sin.inforsystembackend.repository.UserDepartmentRepository;
import com.sin.inforsystembackend.repository.UserRepository;
import org.hibernate.Hibernate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private DepartmentService departmentService;

    @Autowired
    private UserDepartmentRepository userDepartmentRepository;

    /**
     * 用户登录验证
     * @param email 用户邮箱
     * @param password 用户密码
     * @return 匹配的用户，如果不存在则返回 null
     */
    public User login(String email, String password) {
        User user = userRepository.findByEmailAndPassword(email, password).orElse(null);
        if (user != null) {
            Hibernate.initialize(user.getUserDepartments());
        }
        return user;
    }

    /**
     * 获取按类型筛选的用户列表
     * @param userType 用户类型（如 ADMIN、STUDENT、TEACHER）
     * @return 匹配的用户列表
     */
    public List<User> getUsersByType(User.UserType userType) {
        if (userType == null) {
            return List.of();
        }
        return userRepository.findByUserType(userType);
    }

    /**
     * 添加新用户
     * @param user 要添加的用户
     * @return 保存后的用户对象，如果邮箱已存在则返回 null
     */
    public User addUser(User user) {
        if (user == null || userRepository.existsByEmail(user.getEmail())) {
            return null;
        }
        return userRepository.save(user);
    }

    /**
     * 修改用户密码
     * @param userId 用户 ID
     * @param newPassword 新密码
     * @return 更新后的用户对象，如果用户不存在则返回 null
     */
    public User updatePassword(Integer userId, String newPassword) {
        User user = userRepository.findById(userId).orElse(null);
        if (user != null) {
            user.setPassword(newPassword);
            return userRepository.save(user);
        }
        return null;
    }

    /**
     * 修改用户类型（仅限管理员）
     * @param userId 用户 ID
     * @param userType 新的用户类型
     * @return 更新后的用户对象，如果用户不存在则返回 null
     */
    public User updateUserType(Integer userId, User.UserType userType) {
        User user = userRepository.findById(userId).orElse(null);
        if (user != null) {
            user.setUserType(userType);
            return userRepository.save(user);
        }
        return null;
    }

    /**
     * 删除用户（仅限管理员）
     * @param userId 用户 ID
     * @return 如果删除成功返回 true，否则返回 false
     */
    public boolean deleteUser(Integer userId) {
        if (userRepository.existsById(userId)) {
            userRepository.deleteById(userId);
            return true;
        }
        return false;
    }

    /**
     * 根据用户邮箱查询用户
     * @param email 用户邮箱
     * @return 匹配的用户对象，如果不存在则返回 null
     */
    public User findUserByEmail(String email) {
        return userRepository.findByEmail(email).orElse(null);
    }

    /**
     * 更新用户信息
     * @param userId 用户 ID
     * @param updatedUser 更新后的用户对象
     * @return 更新后的用户对象，如果用户不存在则返回 null
     */
    public User updateUser(Integer userId, User updatedUser) {
        User existingUser = userRepository.findById(userId).orElse(null);
        if (existingUser != null) {
            existingUser.setName(updatedUser.getName());
            existingUser.setEmail(updatedUser.getEmail());
            existingUser.setPassword(updatedUser.getPassword());
            existingUser.setUserType(updatedUser.getUserType());
            return userRepository.save(existingUser);
        }
        return null;
    }

    /**
     * 按条件查询用户
     * @param name 用户名（可选）
     * @param email 邮箱（可选）
     * @param userTypeValue 用户类型（可选，以 Integer 表示）
     * @return 匹配的用户列表
     */
    public List<User> findUsersByConditions(String name, String email, Integer userTypeValue) {
        final String finalName = name; // 声明为 final
        final String finalEmail = email; // 声明为 final
        final User.UserType userType = userTypeValue != null ? User.UserType.values()[userTypeValue] : null; // 声明为 final

        return userRepository.findAll((root, query, criteriaBuilder) -> {
            var predicates = criteriaBuilder.conjunction();
            if (finalName != null) {
                predicates = criteriaBuilder.and(predicates, criteriaBuilder.like(root.get("name"), "%" + finalName + "%"));
            }
            if (finalEmail != null) {
                predicates = criteriaBuilder.and(predicates, criteriaBuilder.equal(root.get("email"), finalEmail));
            }
            if (userType != null) {
                predicates = criteriaBuilder.and(predicates, criteriaBuilder.equal(root.get("userType"), userType));
            }
            return predicates;
        });
    }

    /**
     * 分页查询用户
     * @param pageable 分页信息
     * @return 分页用户列表
     */
    public Page<User> findUsersWithPagination(PageRequest pageable) {
        return userRepository.findAll(pageable);
    }

    /**
     * 根据部门 ID 获取该部门下的所有用户
     *
     * @param departmentId 部门的唯一标识 ID
     * @return 属于该部门的用户列表
     */
    public List<User> findUsersByDepartmentId(Integer departmentId) {
        // 调用 userRepository 的方法，通过部门 ID 获取用户列表
        return userRepository.findUsersByDepartmentId(departmentId);
    }

    /**
     * 根据用户 ID 获取用户信息
     * @param userId 用户 ID
     * @return 用户对象
     */
    public User getUserById(Integer userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("用户不存在，ID: " + userId));
    }

    /**
     * 根据用户ID获取用户所属的部门列表
     * @param userId 用户ID
     * @return 部门列表
     */
    public List<Department> getUserDepartmentsByUserId(Integer userId) {
        // 调用数据访问层从数据库查询该用户所属的部门
        return userDepartmentRepository.findDepartmentsByUserId(userId);
    }

    public List<DepartmentDTO> getUserDepartmentsAsDTO(Integer userId) {
        List<Department> departments = userDepartmentRepository.findDepartmentsByUserId(userId);
        return departments.stream()
                .map(department -> new DepartmentDTO(department.getDepartmentId(), department.getDepartmentName()))
                .collect(Collectors.toList());
    }

}