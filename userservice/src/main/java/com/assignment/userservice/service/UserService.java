package com.assignment.userservice.service;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import com.assignment.userservice.beans.ResponseBean;
import com.assignment.userservice.beans.User;
import com.assignment.userservice.dao.UserDao;

@Service
public class UserService implements UserDao {

	@Autowired
	JdbcTemplate jdbcTemplate;
	
	@Autowired
	private KafkaTemplate<String, String> kafkaTemplate;

	private final Logger LOGGER = LoggerFactory.getLogger(UserService.class);

	@Override
	public ResponseBean registration(User user) {
		ResponseBean resp = new ResponseBean();
		try {
			String queryToCheckUserExists = "select case when exists(Select user_id from user_details where user_mobile_number='"+ user.getUserMobileNumber() + "') then true else false end";
			boolean exists = jdbcTemplate.queryForObject(queryToCheckUserExists, Boolean.class);
			if (!exists) {

				String queryToInsert = "insert into user_details(user_name,user_password,user_mobile_number,user_email_address) values (?,?,?,?)";
				jdbcTemplate.update(queryToInsert, user.getUserName(), user.getUserPassword(),user.getUserMobileNumber(), user.getUserEmail());
				
//				 Integer userId = jdbcTemplate.queryForObject("SELECT LAST_INSERT_ID()", Integer.class);
//				 String queryToInsertRole = "insert into user_roles(user_id, role_id) values (?, ?)";
//		                Integer roleId = jdbcTemplate.queryForObject("select role_id from roles where role_name=?", Integer.class, user.getUserRole());
//		                jdbcTemplate.update(queryToInsertRole,userId, roleId);
		            
		                kafkaTemplate.send("user-events", "User details: " + user.getUserName());
				resp.setValid(true);
				resp.setMessage("Registration is Successful");
			} else {
				resp.setValid(false);
				resp.setMessage("User Details are already exists with the given number");
			}

		} catch (Exception e) {
			resp.setValid(false);
			resp.setMessage("##Exception");
			LOGGER.error("#Error while registrating user => " + e.getMessage());
		}
		return resp;
	}

	@Override
	public ResponseBean getUserDetails() {

		ResponseBean resp = new ResponseBean();
		try {
			List<User> list = new ArrayList<User>();
			String queryToGetUserDetails = "Select * from user_details";
			SqlRowSet rs = jdbcTemplate.queryForRowSet(queryToGetUserDetails);
			while (rs.next()) {
				User user = new User();
				user.setUserId(rs.getString("user_id"));
				user.setUserMobileNumber(rs.getString("user_mobile_number"));
				user.setUserName(rs.getString("user_name"));
				user.setUserPassword(rs.getString("user_password"));
				user.setUserEmail(rs.getString("user_email_address"));
				list.add(user);
			}

			if (!list.isEmpty()) {
				resp.setData(list);
				resp.setValid(true);
				resp.setMessage("List of users");
			}
		} catch (Exception e) {
			resp.setValid(false);
			resp.setMessage("##Exception");
			LOGGER.error("#Error while getting user Details => " + e.getMessage());
		}
		return resp;

	}

	@Override
	public ResponseBean getUserDetailsById(int userId) {

		ResponseBean resp = new ResponseBean();
		try {
			User user = new User();
			String queryToGetUserDetails = "Select * from user_details where user_id=?";
			SqlRowSet rs = jdbcTemplate.queryForRowSet(queryToGetUserDetails, userId);
			while (rs.next()) {

				user.setUserId(rs.getString("user_id"));
				user.setUserMobileNumber(rs.getString("user_mobile_number"));
				user.setUserName(rs.getString("user_name"));
				user.setUserPassword(rs.getString("user_password"));
				user.setUserEmail(rs.getString("user_email_address"));
			}

			if (user.getUserId() != null) {
				resp.setData(user);
				resp.setValid(true);
				resp.setMessage("User Details");
			}
		} catch (Exception e) {
			resp.setValid(false);
			resp.setMessage("##Exception");
			LOGGER.error("#Error while user details by id => " + e.getMessage());
		}
		return resp;

	}

	@Override
	public ResponseBean updateUserDetails(User user) {

		ResponseBean resp = new ResponseBean();
		try {
			StringBuffer queryBuilder = new StringBuffer();
			List<Object> params = new ArrayList<>();

			if (StringUtils.hasText(user.getUserName())) {
				queryBuilder.append("user_name=?,");
				params.add(user.getUserName());
			}
			if (StringUtils.hasText(user.getUserPassword())) {
				queryBuilder.append("user_password=?,");
				params.add(user.getUserPassword());
			}
			if (StringUtils.hasText(user.getUserEmail())) {
				queryBuilder.append("user_email_address=?,");
				params.add(user.getUserEmail());
			}
			if (StringUtils.hasText(user.getUserMobileNumber())) {
				queryBuilder.append("user_mobile_number=?,");
				params.add(user.getUserMobileNumber());
			}

			if (params.isEmpty()) {
				resp.setValid(false);
				resp.setMessage("No details provided to update.");
				return resp;
			}

			queryBuilder.setLength(queryBuilder.length() - 1);
			queryBuilder.append(" where user_id=? ");
			params.add(user.getUserId());

			String queryToUpdateUserDetails = "Update user_details set " + queryBuilder + " ";
			jdbcTemplate.update(queryToUpdateUserDetails, params.toArray());
			resp.setValid(true);
			resp.setMessage("Updated user details ");
			kafkaTemplate.send("user-events", "User updated: " + user.getUserName());
		} catch (Exception e) {
			resp.setValid(false);
			resp.setMessage("##Exception");
			LOGGER.error("#Error while updating user details => " + e.getMessage());
		}
		return resp;
	}

	@Override
	public ResponseBean deleteUserDetails(int userId) {
		ResponseBean resp = new ResponseBean();
		try {
			String queryToDeleteUserDetails = "Delete from user_details where user_id=?";
			jdbcTemplate.update(queryToDeleteUserDetails, userId);
			resp.setValid(true);
			resp.setMessage("Deleted user details successfully");
			kafkaTemplate.send("user-events", "User deleted: " + userId);
		} catch (Exception e) {
			resp.setValid(false);
			resp.setMessage("##Exception");
			LOGGER.error("#Error while deleting user details => " + e.getMessage());
		}
		return resp;
	}
	
	public User findByUsername(String username) {
        String sql = "SELECT * FROM users WHERE user_name = ?";
        List<User> users = jdbcTemplate.query(sql, (rs, rowNum) -> {
            User user = new User();
            user.setUserId(rs.getString("user_id"));
            user.setUserName(rs.getString("user_name"));
            user.setUserPassword(rs.getString("user_password"));
            user.setUserEmail(rs.getString("user_email"));
            user.setUserMobileNumber(rs.getString("user_mobile_number"));
            user.setRoles(getUserRoles(rs.getInt("user_id")));
            return user;
        });
        return users.isEmpty() ? null : users.get(0);
    }

    private Set<String> getUserRoles(int userId) {
        String sql = "SELECT role_name FROM user_roles WHERE user_id = ?";
        List<String> roles = jdbcTemplate.queryForList(sql,String.class,userId);
        return new HashSet<>(roles);
    }
}
