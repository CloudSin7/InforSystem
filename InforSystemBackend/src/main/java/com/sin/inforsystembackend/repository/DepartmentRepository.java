package com.sin.inforsystembackend.repository;

import com.sin.inforsystembackend.entity.Department;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
public interface DepartmentRepository extends JpaRepository<Department, Integer> {

    /**
     * 查询所有部门信息
     * @return 部门列表
     */
    // 不需要添加自定义的 findAllDepartments 方法，使用 JPA 的 findAll 即可

    /**
     * 根据部门名称精确查询
     * @param departmentName 部门名称
     * @return 匹配的部门（如果存在）
     */
    Department findByDepartmentName(String departmentName);

    /**
     * 根据部门名称模糊查询
     * @param keyword 部分或全部部门名称
     * @return 匹配的部门列表
     */
    List<Department> findByDepartmentNameContaining(String keyword);

    /**
     * 检查部门是否存在
     * @param departmentName 部门名称
     * @return 如果部门存在则返回 true，否则返回 false
     */
    boolean existsByDepartmentName(String departmentName);

    /**
     * 使用 JOIN FETCH 优化查询，避免 N+1 问题，获取部门和相关联的用户信息
     * @return 包含用户信息的部门列表
     */
    @Query("SELECT d FROM Department d JOIN FETCH d.userDepartments")
    List<Department> findAllWithUsers();
}