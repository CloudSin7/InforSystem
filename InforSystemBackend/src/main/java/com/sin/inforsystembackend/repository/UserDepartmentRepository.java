package com.sin.inforsystembackend.repository;

import com.sin.inforsystembackend.entity.Department;
import com.sin.inforsystembackend.entity.User;
import com.sin.inforsystembackend.entity.UserDepartment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
public interface UserDepartmentRepository extends JpaRepository<UserDepartment, Integer> {

    /**
     * 根据部门 ID 查询所有用户
     * @param departmentId 部门 ID
     * @return 用户列表
     */
    @Query("SELECT ud.user FROM UserDepartment ud WHERE ud.department.departmentId = :departmentId")
    List<User> findUsersByDepartmentId(@Param("departmentId") Integer departmentId);

    /**
     * 根据用户 ID 查询所属部门
     * @param userId 用户 ID
     * @return 用户所属的部门列表
     */
    @Query("SELECT ud.department FROM UserDepartment ud WHERE ud.user.userId = :userId")
    List<com.sin.inforsystembackend.entity.Department> findDepartmentsByUserId(@Param("userId") Integer userId);


    /**
     * 查询用户在部门的角色
     * @param userId 用户 ID
     * @param departmentId 部门 ID
     * @return 用户在该部门的角色
     */
    @Query("SELECT ud.role FROM UserDepartment ud WHERE ud.user.userId = :userId AND ud.department.departmentId = :departmentId")
    String findUserRoleInDepartment(@Param("userId") Integer userId, @Param("departmentId") Integer departmentId);


    /**
     * 根据部门 ID 删除所有用户部门的关联记录
     * @param departmentId 部门的唯一标识符
     * 通过部门 ID 删除所有与该部门相关联的用户部门关系，避免外键冲突
     */
    @Modifying
    @Transactional
    @Query("DELETE FROM UserDepartment ud WHERE ud.department.departmentId = :departmentId")
    void deleteAllByDepartmentId(@Param("departmentId") Integer departmentId);

    /**
     * 根据角色查询用户
     * @param role 用户角色
     * @return 用户列表
     */
    @Query("SELECT ud.user FROM UserDepartment ud WHERE ud.role = :role")
    List<User> findUsersByRole(@Param("role") String role);
}