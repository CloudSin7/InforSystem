package com.sin.inforsystembackend.service;

import com.sin.inforsystembackend.entity.Department;
import com.sin.inforsystembackend.entity.DepartmentDTO;
import com.sin.inforsystembackend.entity.User;
import com.sin.inforsystembackend.repository.DepartmentRepository;
import com.sin.inforsystembackend.repository.UserDepartmentRepository;
import com.sin.inforsystembackend.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.http.HttpStatus;


import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class DepartmentService {

    @Autowired
    private DepartmentRepository departmentRepository;

    @Autowired
    private UserDepartmentRepository userDepartmentRepository;


    @Autowired
    private UserRepository userRepository;

    /**
     * 获取所有部门信息（无分页）
     * @return 部门列表
     */
    public List<DepartmentDTO> getAllDepartments() {
        // 使用 findAll() 方法来获取所有 Department 实体
        List<Department> departments = departmentRepository.findAll();

        // 将 Department 实体转换为 DepartmentDTO
        return departments.stream().map(department -> {
            DepartmentDTO dto = new DepartmentDTO();
            dto.setDepartmentId(department.getDepartmentId());
            dto.setDepartmentName(department.getDepartmentName());
            return dto;
        }).collect(Collectors.toList());
    }



    /**
     * 根据部门 ID 查询部门DTO
     * @param departmentId 部门 ID
     * @return 匹配的部门DTO对象，如果不存在则返回 null
     */
    public DepartmentDTO getDepartmentDTOById(Integer departmentId) {
        Optional<Department> department = departmentRepository.findById(departmentId);
        return department.map(this::convertToDTO).orElse(null);
    }

    /**
     * 根据部门名称精确查询并返回DTO
     * @param departmentName 部门名称
     * @return 匹配的部门DTO对象，或 null 如果未找到
     */
    public DepartmentDTO getDepartmentDTOByName(String departmentName) {
        Department department = departmentRepository.findByDepartmentName(departmentName);
        return department != null ? convertToDTO(department) : null;
    }

    /**
     * 根据部门名称模糊查询并返回DTO列表
     * @param keyword 搜索关键词
     * @return 匹配的部门DTO列表
     */
    public List<DepartmentDTO> searchDepartmentsByName(String keyword) {
        List<Department> departments = departmentRepository.findByDepartmentNameContaining(keyword);
        return departments.stream().map(this::convertToDTO).collect(Collectors.toList());
    }

    /**
     * 检查部门是否存在
     * @param departmentName 部门名称
     * @return 如果部门存在则返回 true，否则返回 false
     */
    public boolean isDepartmentExists(String departmentName) {
        return departmentRepository.existsByDepartmentName(departmentName);
    }

    /**
     * 添加新部门（仅限管理员），返回DepartmentDTO
     * @param departmentDTO 要添加的部门DTO对象
     * @return 保存后的部门DTO对象
     */
    public DepartmentDTO addDepartment(DepartmentDTO departmentDTO) {
        if (!isDepartmentExists(departmentDTO.getDepartmentName())) {
            Department department = new Department();
            department.setDepartmentName(departmentDTO.getDepartmentName());
            Department savedDepartment = departmentRepository.save(department);
            return convertToDTO(savedDepartment);
        }
        return null;
    }

    /**
     * 删除部门（仅限管理员）
     * @param departmentId 要删除的部门 ID
     * @return 删除成功返回 true，否则返回 false
     */
    @Transactional
    public boolean deleteDepartment(Integer departmentId) {
        if (departmentRepository.existsById(departmentId)) {
            userDepartmentRepository.deleteAllByDepartmentId(departmentId);  // 删除所有用户-部门关联
            departmentRepository.deleteById(departmentId);
            return true;
        }
        return false;
    }


    /**
     * 根据部门 ID 查询用户列表
     * @param departmentId 部门 ID
     * @return 该部门下的用户列表
     */
    public List<User> getUsersByDepartmentId(Integer departmentId) {
        return userDepartmentRepository.findUsersByDepartmentId(departmentId);
    }

    /**
     * 将 Department 转换为 DepartmentDTO
     * @param department 部门实体
     * @return 部门DTO对象
     */
    private DepartmentDTO convertToDTO(Department department) {
        DepartmentDTO dto = new DepartmentDTO();
        dto.setDepartmentId(department.getDepartmentId());
        dto.setDepartmentName(department.getDepartmentName());
        // 设置关联的用户信息，避免懒加载问题
        dto.setUserRoles(department.getUserDepartments().stream()
                .map(userDepartment -> userDepartment.getUser().getName() + " - " + userDepartment.getRole())
                .collect(Collectors.toList()));
        return dto;
    }


    /**
     * 添加新用户
     * @param newUser 新用户信息
     * @return 已保存的用户对象
     */
    public User addUser(User newUser) {
        return userRepository.save(newUser);
    }

    /**
     * 更新现有用户信息
     * @param userId 用户ID
     * @param updatedUser 更新后的用户对象
     * @return 已更新的用户对象
     */
    public User updateUser(Integer userId, User updatedUser) {
        return userRepository.findById(userId)
                .map(user -> {
                    user.setName(updatedUser.getName());
                    user.setEmail(updatedUser.getEmail());
                    user.setUserType(updatedUser.getUserType());
                    user.setPassword(updatedUser.getPassword());
                    return userRepository.save(user);
                })
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found with id " + userId));
    }

    /**
     * 删除用户
     * @param userId 用户ID
     */
    public void deleteUser(Integer userId) {
        userDepartmentRepository.deleteById(userId);
    }

    /**
     * 根据部门 ID 获取部门信息
     * @param departmentId 部门 ID
     * @return 部门对象
     */
    public Department getDepartmentById(Integer departmentId) {
        return departmentRepository.findById(departmentId)
                .orElseThrow(() -> new IllegalArgumentException("部门不存在，ID: " + departmentId));
    }
}