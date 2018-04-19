package models;

/**
 * Created by vinee_000 on 25-01-2018.
 */

public class Ticket {
    String ticketNo,value;
    String request;
    String createdDate="",confirmedDate="",inProgressDate="",completedDate="";
    float feedBack=0f;

    public Ticket(String ticketNo, String value, String request, String createdDate, String confirmedDate, String inProgressDate, String completedDate, float feedBack) {
        this.ticketNo = ticketNo;
        this.value = value;
        this.request = request;
        this.createdDate = createdDate;
        this.confirmedDate = confirmedDate;
        this.inProgressDate = inProgressDate;
        this.completedDate = completedDate;
        this.feedBack = feedBack;
    }

    public String getTicketNo() {
        return ticketNo;
    }

    public void setTicketNo(String ticketNo) {
        this.ticketNo = ticketNo;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getRequest() {
        return request;
    }

    public void setRequest(String request) {
        this.request = request;
    }

    public String getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(String createdDate) {
        this.createdDate = createdDate;
    }

    public String getConfirmedDate() {
        return confirmedDate;
    }

    public void setConfirmedDate(String confirmedDate) {
        this.confirmedDate = confirmedDate;
    }

    public String getInProgressDate() {
        return inProgressDate;
    }

    public void setInProgressDate(String inProgressDate) {
        this.inProgressDate = inProgressDate;
    }

    public String getCompletedDate() {
        return completedDate;
    }

    public void setCompletedDate(String completedDate) {
        this.completedDate = completedDate;
    }

    public float getFeedBack() {
        return feedBack;
    }

    public void setFeedBack(float feedBack) {
        this.feedBack = feedBack;
    }
}
