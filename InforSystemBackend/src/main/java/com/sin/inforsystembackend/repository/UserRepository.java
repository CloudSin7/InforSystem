package com.sin.inforsystembackend.repository;

import com.sin.inforsystembackend.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.List;

@Repository
public interface UserRepository extends JpaRepository<User, Integer>, JpaSpecificationExecutor<User> {

    /**
     * 根据邮箱和密码查找用户（用于用户登录验证）
     * @param email 用户邮箱
     * @param password 用户密码
     * @return 匹配的用户（如果存在）
     */
    Optional<User> findByEmailAndPassword(String email, String password);

    /**
     * 根据用户类型查找用户
     * @param userType 用户类型（如学生、教师等）
     * @return 匹配的用户列表
     */
    List<User> findByUserType(User.UserType userType);

    /**
     * 根据姓名模糊查询用户
     * @param name 部分或全部用户姓名
     * @return 匹配的用户列表
     */
    List<User> findByNameContaining(String name);

    /**
     * 根据邮箱判断用户是否存在
     * @param email 用户邮箱
     * @return 如果用户存在则返回 true，否则返回 false
     */
    boolean existsByEmail(String email);

    /**
     * 根据邮箱查找用户
     * @param email 用户邮箱
     * @return 匹配的用户（如果存在）
     */
    Optional<User> findByEmail(String email);


    /**
     * 根据部门 ID 查询用户
     *
     * @param departmentId 部门的唯一标识 ID
     * @return 属于该部门的用户列表
     */
    @Query("SELECT u FROM User u JOIN u.userDepartments ud WHERE ud.department.departmentId = :departmentId")
    List<User> findUsersByDepartmentId(@Param("departmentId") Integer departmentId);

}