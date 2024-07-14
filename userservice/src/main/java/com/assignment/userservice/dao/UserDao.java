package com.assignment.userservice.dao;

import com.assignment.userservice.beans.ResponseBean;
import com.assignment.userservice.beans.User;

public interface UserDao {

	public ResponseBean deleteUserDetails(int userId);

	public ResponseBean getUserDetails();

	public ResponseBean getUserDetailsById(int userId);

	public ResponseBean registration(User user);

	public ResponseBean updateUserDetails(User user);

	public User findByUsername(String username);
}
