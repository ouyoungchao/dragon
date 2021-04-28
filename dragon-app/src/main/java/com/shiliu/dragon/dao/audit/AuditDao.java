package com.shiliu.dragon.dao.audit;

import com.shiliu.dragon.model.Audit.Audits;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

/**
 * @author ouyangchao
 * @createTime
 * @description
 */
@Repository
public class AuditDao {
    private static final Logger logger = LoggerFactory.getLogger(AuditDao.class);

    private static final String ADDAUDIT = "insert into user_audit_info(id,userId,meterials,status,postData,auditData,managerId,isManager) valuse(?,?,?,?,?,?,?,?)";

    @Autowired
    JdbcTemplate jdbcTemplate;

    public void addAudit(Audits audits){
        logger.info("Begin add audit {} ",audits);
        jdbcTemplate.update(ADDAUDIT,audits.getId(),audits.getUserId(),audits.getMeterials(),audits.getStatus(),audits.getPostData(),audits.getAuditData(),audits.getManagerId(), audits.isManager()?1:0);
        logger.info("Begin add audit success");
    }

}
