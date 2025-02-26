import axios from 'axios';

const BASE_URL = process.env.REACT_APP_API_URL || 'http://localhost:8088/api/departments';

const apiClient = axios.create({
  baseURL: BASE_URL,
  timeout: 1000,
  headers: { 'Content-Type': 'application/json' }
});

export const getAllDepartments = async () => {
  try {
    const response = await apiClient.get('/');
    return response.data;
  } catch (error) {
    console.error('获取部门列表失败:', error);
    throw new Error('获取部门列表失败，请稍后重试。');
  }
};

export const getUsersByDepartmentId = async (departmentId) => {
  try {
    const response = await apiClient.get(`/${departmentId}/users`);
    return response.data;
  } catch (error) {
    console.error('获取部门用户列表失败:', error);
    throw new Error('获取部门用户列表失败，请稍后重试。');
  }
};

export const updateUser = async (user) => {
  try {
    const response = await apiClient.put(`/users/${user.id}`, user);
    return response.data;
  } catch (error) {
    console.error('更新用户信息失败:', error);
    throw new Error('更新用户信息失败，请检查输入是否正确。');
  }
};

export const addDepartment = async (newDepartment) => {
  try {
    const response = await apiClient.post('/', newDepartment);
    return response.data;
  } catch (error) {
    console.error('添加部门失败:', error);
    throw new Error('添加部门失败，请检查输入是否正确。');
  }
};

export const deleteDepartment = async (departmentId) => {
  try {
    const response = await apiClient.delete(`/${departmentId}`);
    return response.data;
  } catch (error) {
    console.error('删除部门失败:', error);
    throw new Error('删除部门失败，请稍后重试。');
  }
};

// Example of adding loading state and error handling in a function
export const fetchDepartments = async () => {
  let error = null;
  let data = [];
  try {
    const response = await getAllDepartments();
    data = response;
  } catch (err) {
    error = err;
  }
  return { data, error };
};