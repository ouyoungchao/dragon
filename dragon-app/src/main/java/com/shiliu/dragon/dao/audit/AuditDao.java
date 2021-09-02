package com.shiliu.dragon.dao.audit;

import com.shiliu.dragon.model.Audit.Audits;
import com.shiliu.dragon.utils.utils.JsonUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.Collections;
import java.util.List;

/**
 * @author ouyangchao
 * @createTime
 * @description 获取上传的认证信息，修改认证信息
 */
@Repository
public class AuditDao {
    private static final Logger logger = LoggerFactory.getLogger(AuditDao.class);

    private static final String ADDAUDIT = "insert into user_audit_info(id,userId,meterials,status,postData,auditData,managerId,isManager,school) values(?,?,?,?,?,?,?,?,?)";

    private static final String QUEY_AUDIT_BYID = "select * from user_audit_info where id = ?";

    private static final String QUEY_AUDIT_BYMANAGER = "select * from user_audit_info where managerId = ?";

    private static final String QUEY_AUDIT_BYUSER = "select * from user_audit_info where userId = ?";

    private static final String UPDATE_AUDIT_STATUS = "update user_audit_info set status = ?  where id = ?";

    @Autowired
    JdbcTemplate jdbcTemplate;


    public void addAudit(Audits audits) {
        logger.info("Begin add audit {} ", audits);
        jdbcTemplate.update(ADDAUDIT, audits.getId(), audits.getUserId(), JsonUtil.toJson(audits.getMeterials()), audits.getStatus().toString(), audits.getPostData(), audits.getAuditData(), audits.getManagerId(), audits.getIsManager() ? 1 : 0,audits.getSchool());
        logger.info("Add audit success");
    }

    /**
     * 查询审批电子流
     *
     * @param id
     * @return
     */
    public Audits queryAuditInfoById(String id) {
        logger.info("Begin query auditinfo {}", id);
        try {
            Audits audits = jdbcTemplate.queryForObject(QUEY_AUDIT_BYID, new AuditRowMapping(), id);
            return audits;
        } catch (DataAccessException accessException){
            logger.warn("Query audit by id error ",accessException);
        }
        return null;
    }

    public List<Audits> queryAuditInfoByUser(String userId) {
        logger.info("Begin query auditinfo by userId {}", userId);
        try {
            List<Audits> audits = jdbcTemplate.query(QUEY_AUDIT_BYUSER, new AuditRowMapping(), userId);
            return audits;
        } catch (DataAccessException accessException){
            logger.warn("Query audit by userId error ",accessException);
        }
        return Collections.EMPTY_LIST;
    }

    public List<Audits> queryAuditInfoByManager(String managerId) {
        logger.info("Begin query auditinfo by managerId {}", managerId);
        try {
            List<Audits> audits = jdbcTemplate.query(QUEY_AUDIT_BYMANAGER, new AuditRowMapping(), managerId);
            return audits;
        } catch (DataAccessException accessException){
            logger.warn("Query audit by managerId error ",accessException);
        }
        return Collections.EMPTY_LIST;
    }


    /**
     * 更新审批状态
     * @param audits
     */
    public void updateExamineStatus(Audits audits) {
        logger.info("Begin to updata audit {} status {}",audits.getId(),audits.getStatus());
        try {
            jdbcTemplate.update(UPDATE_AUDIT_STATUS,audits.getStatus().toString(),audits.getId());
        }catch (DataAccessException exception){
            logger.error("Updata status error ",exception);
        }
    }
}
