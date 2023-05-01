package com.quadratyx.dispatcher.enitiy;

import javax.persistence.*;
import java.sql.Timestamp;

/**
 * @author Shashi
 */
@Entity
@Table(name = "scheduler_job_info")
public class SchedulerJobInfo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String jobId;
    private String projectCode;
    private String campaignId;
    private String reportType;
    private String reportSubType;
    private String scheduleExpression;
    private String purpose;
    @Lob
    private String toEmailId;
    private String fromEmailId;
    @Lob
    private String ccEmailIds;
    @Lob
    private String emailSubject;
    @Lob
    private String htmlData;
    @Lob
    private String comments;
    private String mailStatus;
    @Column(columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP")
    private Timestamp insertionTime;
    private Timestamp mailTimestamp;


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getJobId() {
        return jobId;
    }

    public void setJobId(String jobId) {
        this.jobId = jobId;
    }

    public String getProjectCode() {
        return projectCode;
    }

    public void setProjectCode(String projectCode) {
        this.projectCode = projectCode;
    }

    public String getCampaignId() {
        return campaignId;
    }

    public void setCampaignId(String campaignId) {
        this.campaignId = campaignId;
    }

    public String getReportType() {
        return reportType;
    }

    public void setReportType(String reportType) {
        this.reportType = reportType;
    }

    public String getReportSubType() {
        return reportSubType;
    }

    public void setReportSubType(String reportSubType) {
        this.reportSubType = reportSubType;
    }

    public String getScheduleExpression() {
        return scheduleExpression;
    }

    public void setScheduleExpression(String scheduleExpression) {
        this.scheduleExpression = scheduleExpression;
    }

    public String getPurpose() {
        return purpose;
    }

    public void setPurpose(String purpose) {
        this.purpose = purpose;
    }

    public String getToEmailId() {
        return toEmailId;
    }

    public void setToEmailId(String toEmailId) {
        this.toEmailId = toEmailId;
    }

    public String getFromEmailId() {
        return fromEmailId;
    }

    public void setFromEmailId(String fromEmailId) {
        this.fromEmailId = fromEmailId;
    }

    public String getCcEmailIds() {
        return ccEmailIds;
    }

    public void setCcEmailIds(String ccEmailIds) {
        this.ccEmailIds = ccEmailIds;
    }

    public String getEmailSubject() {
        return emailSubject;
    }

    public void setEmailSubject(String emailSubject) {
        this.emailSubject = emailSubject;
    }

    public String getHtmlData() {
        return htmlData;
    }

    public void setHtmlData(String htmlData) {
        this.htmlData = htmlData;
    }

    public String getComments() {
        return comments;
    }

    public void setComments(String comments) {
        this.comments = comments;
    }

    public String getMailStatus() {
        return mailStatus;
    }

    public void setMailStatus(String mailStatus) {
        this.mailStatus = mailStatus;
    }

    public Timestamp getInsertionTime() {
        return insertionTime;
    }

    public void setInsertionTime(Timestamp insertionTime) {
        this.insertionTime = insertionTime;
    }

    public Timestamp getMailTimestamp() {
        return mailTimestamp;
    }

    public void setMailTimestamp(Timestamp mailTimestamp) {
        this.mailTimestamp = mailTimestamp;
    }

    @Override
    public String toString() {
        return "SchedulerJobInfo{" +
                "id=" + id +
                ", jobId='" + jobId + '\'' +
                ", projectCode='" + projectCode + '\'' +
                ", campaignId='" + campaignId + '\'' +
                ", reportType='" + reportType + '\'' +
                ", reportSubType='" + reportSubType + '\'' +
                ", scheduleExpression='" + scheduleExpression + '\'' +
                ", purpose='" + purpose + '\'' +
                ", toEmailId='" + toEmailId + '\'' +
                ", fromEmailId='" + fromEmailId + '\'' +
                ", ccEmailIds='" + ccEmailIds + '\'' +
                ", emailSubject='" + emailSubject + '\'' +
                ", htmlData='" + htmlData + '\'' +
                ", comments='" + comments + '\'' +
                ", mailStatus='" + mailStatus + '\'' +
                ", insertionTime=" + insertionTime +
                ", mailTimestamp=" + mailTimestamp +
                '}';
    }
}