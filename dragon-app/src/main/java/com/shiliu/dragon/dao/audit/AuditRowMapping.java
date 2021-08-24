package com.shiliu.dragon.dao.audit;

import com.shiliu.dragon.model.Audit.AuditStatus;
import com.shiliu.dragon.model.Audit.Audits;
import com.shiliu.dragon.utils.utils.JsonUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

/**
 * @author ouyangchao
 * @createTime
 * @description
 */
public class AuditRowMapping implements RowMapper<Audits> {
    private static final Logger logger = LoggerFactory.getLogger(AuditRowMapping.class);

    private static final String TAG = "AuditRowMapping";



    @Override
    public Audits mapRow(ResultSet resultSet, int i) throws SQLException {
        return row2Audit(resultSet);
    }

    private Audits row2Audit(ResultSet resultSet) {
        Audits audits = new Audits();
        try {
            audits.setId(resultSet.getString("id"));
            audits.setAuditData(resultSet.getLong("auditData"));
            audits.setIsManager(resultSet.getBoolean("isManager"));
            audits.setStatus(AuditStatus.valueOf(resultSet.getString("status")));
            audits.setUserId(resultSet.getString("userId"));
            audits.setManagerId(resultSet.getString("managerId"));
            audits.setMeterials(JsonUtil.readValue(resultSet.getString("meterials"), List.class));
            audits.setPostData(resultSet.getLong("postData"));
            audits.setSchool(resultSet.getString("school"));
        } catch (SQLException throwables) {
            logger.warn("Row to AUdit error ",throwables);
        }
        return audits;
    }
}
