package com.assignment.userservice.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.assignment.userservice.beans.ResponseBean;
import com.assignment.userservice.beans.User;
import com.assignment.userservice.dao.UserDao;

@RestController
@RequestMapping("/api/user")
public class UserController {

	@Autowired
	UserDao userDao;

	@PostMapping("/registration")
	public ResponseBean registration(@RequestBody User user) {
		ResponseBean resp = new ResponseBean();
		if (!StringUtils.hasText(user.getUserName()) || !StringUtils.hasText(user.getUserPassword())
				|| !StringUtils.hasText(user.getUserEmail()) || !StringUtils.hasText(user.getUserMobileNumber())) {

			resp.setMessage("User Name,Password,Mobile Number and email are mandatory fields");
		} else {
			resp = userDao.registration(user);
		}

		return resp;

	}

	@GetMapping("/getUserDetails")
	public ResponseBean getUserDetails() {
		return userDao.getUserDetails();
	}

	@GetMapping("/getUserDetailsById/{userId}")
	public ResponseBean getUserDetailsById(@PathVariable int userId) {
		return userDao.getUserDetailsById(userId);
	}

	@PutMapping("/updateUserDetails")
	public ResponseBean updateUserDetails(@RequestBody User user) {
		return userDao.updateUserDetails(user);
	}

	@DeleteMapping("/deleteUserDetails/{userId}")
	public ResponseBean deleteUserDetails(@PathVariable int userId) {
		return userDao.deleteUserDetails(userId);
	}

}
