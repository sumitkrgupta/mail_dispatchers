package com.quadratyx.dispatcher.service.impl;

import com.quadratyx.dispatcher.enitiy.SchedulerJobInfo;
import com.quadratyx.dispatcher.service.EmailDispatcher;
import com.quadratyx.dispatcher.service.EmailService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.persistence.EntityManager;
import javax.transaction.Transactional;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

@Transactional
@Service
public class EmailDispatcherImpl implements EmailDispatcher {

    private static final Logger logger = LoggerFactory.getLogger(EmailDispatcherImpl.class);
    @Autowired
    private EntityManager entityManager;
    @Value("${mail.New.Status}")
    private String mailNewStatus;
    @Value("${mail.Success.Status}")
    private String mailSuccessStatus;
    @Value("${mail.Fail.Status}")
    private String mailFailStatus;
    @Value("${mail.Retry.Count}")
    private String mailRetryCount;
    @Autowired
    private EmailService emailService;



    @Override
    @SuppressWarnings("unchecked")
    public void emailDispatcher() {
        try{
            List<SchedulerJobInfo> schedulerJobInfoList;
            String query = "FROM SchedulerJobInfo as L WHERE L.mailStatus = ?1";
            schedulerJobInfoList = entityManager.createQuery(query).setParameter(1, mailNewStatus).getResultList();
            if(!schedulerJobInfoList.isEmpty()){
                for(SchedulerJobInfo info:schedulerJobInfoList){
                    boolean mailSuccess = false;
                    int count = 0;
                    while(count<Integer.parseInt(mailRetryCount)){
//                        if(info.getImagePath()==null && info.getImageName()==null){
//                            mailSuccess = emailService.sendHtmlMessage(info.getFromEmailId(),info.getToEmailId(),info.getEmailSubject(),info.getHtmlData(),info.getCcEmailIds());
//                        }else {
//                            mailSuccess= emailService.sendHtmlMessageWithAttachment(info.getFromEmailId(),info.getToEmailId(),info.getEmailSubject(),info.getHtmlData(),info.getImagePath(),info.getCcEmailIds(),info.getImageName());
//                        }
                        mailSuccess = emailService.sendHtmlMessage(info.getFromEmailId(),info.getToEmailId(),info.getEmailSubject(),info.getHtmlData(),info.getCcEmailIds());
                        if(mailSuccess){
                            logger.info("Mail Sent Success ..........");
                            break;
                        }else {
                            logger.error("Mail Send Failed");
                        }
                        count = count +1;
                    }
                    Timestamp timestamp=Timestamp.valueOf(LocalDateTime.now());
                    SchedulerJobInfo schedulerJobInfo = entityManager.find(SchedulerJobInfo.class, info.getId());
                    schedulerJobInfo.setMailTimestamp(timestamp);
                    if(mailSuccess){
                        schedulerJobInfo.setMailStatus(mailSuccessStatus);
                    }
                    else{
                        schedulerJobInfo.setMailStatus(mailFailStatus);
                    }
                    entityManager.flush();
                }
            }else {
                logger.info("New Email Dispatcher Not Found in This Current Schedule....");
            }
        }catch (Exception ex){
            logger.error("Scheduler Failed " + ex.getMessage());
            logger.error(Arrays.toString(ex.getStackTrace()));
        }
    }
}
