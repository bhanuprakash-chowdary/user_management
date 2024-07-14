package com.assignment.journalservice.service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import com.assignment.journalservice.bean.Journal;
import com.assignment.journalservice.bean.ResponseBean;

import java.util.List;

@Service
public class JournalService {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @KafkaListener(topics = "user-events", groupId = "journal-group")
    public void consumeUserEvent(String message) {
        System.out.println("Received: " + message);
        saveJournal(message);
    }

    private void saveJournal(String message) {
        String sql = "insert into journals (event_message) values (?)";
        jdbcTemplate.update(sql, message);
    }

    public ResponseBean getJournals() {
        ResponseBean resp = new ResponseBean();
        try {
            String sql = "select * from journals";
            List<Journal> journals = jdbcTemplate.query(sql, (rs, rowNum) -> {
                Journal journal = new Journal();
                journal.setId(rs.getInt("id"));
                journal.setEventMessage(rs.getString("event_message"));
                journal.setCreatedAt(rs.getString("created_at"));
                return journal;
            });
            resp.setData(journals);
            resp.setMessage("Journals retrieved successfully");
            resp.setValid(true);
        } catch (Exception e) {
            resp.setMessage("Error: " + e.getMessage());
        }
        return resp;
    }

	public ResponseBean getJournalById(Long id) {
		ResponseBean resp = new ResponseBean();
		try {

			String sqlQuery = "select * from journals where id=?";
			Journal journals = jdbcTemplate.queryForObject(sqlQuery, (rs, rowNum) -> {
				Journal journal = new Journal();
				journal.setId(rs.getInt("id"));
				journal.setEventMessage(rs.getString("event_message"));
				journal.setCreatedAt(rs.getString("created_at"));
				return journal;
			}, id);

			resp.setData(journals);
			resp.setMessage("Journal retrieved successfully");
			resp.setValid(true);

		} catch (Exception e) {
			resp.setMessage("Invalid Id");
		}

		return resp;
	}
}

