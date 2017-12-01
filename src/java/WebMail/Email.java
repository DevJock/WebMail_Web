package WebMail;

/*
 * Confidential Source Code. No unauthorized copying or redistribution in parts or whole allowed. 
 * Copyright 2017. Chiraag Bangera
 */


/**
 *
 * @author Chiraag Bangera
 */
public class Email 
{
    public int ID;
    public String GroupID;
    public String From;
    public String To;
    public String Subject;
    public String Message;
    public boolean IsNew;
    public String TimeStamp;
    public int DeleteStatus;
    public int NotificationStatus;

    public int getDeleteStatus() {
        return DeleteStatus;
    }

    public void setDeleteStatus(int DeleteStatus) {
        this.DeleteStatus = DeleteStatus;
    }

    public int getNotificationStatus() {
        return NotificationStatus;
    }

    public void setNotificationStatus(int NotificationStatus) {
        this.NotificationStatus = NotificationStatus;
    }

    public int getID() {
        return ID;
    }

    public void setID(int ID) {
        this.ID = ID;
    }

    public String getGroupID() {
        return GroupID;
    }

    public void setGroupID(String GroupID) {
        this.GroupID = GroupID;
    }

    public String getFrom() {
        return From;
    }

    public void setFrom(String From) {
        this.From = From;
    }

    public String getTo() {
        return To;
    }

    public void setTo(String To) {
        this.To = To;
    }

    public String getSubject() {
        return Subject;
    }

    public void setSubject(String Subject) {
        this.Subject = Subject;
    }

    public String getMessage() {
        return Message;
    }

    public void setMessage(String Message) {
        this.Message = Message;
    }

    public boolean isIsNew() {
        return IsNew;
    }

    public void setIsNew(boolean IsNew) {
        this.IsNew = IsNew;
    }

    public String getTimeStamp() {
        return TimeStamp;
    }

    public void setTimeStamp(String TimeStamp) {
        this.TimeStamp = TimeStamp;
    }
    
    public Email()
    {
        ID = 0;
        IsNew = false;
        GroupID = null;
        Subject = null;
        From = null;
        To = null;
        Message = null;
        TimeStamp = null; 
    }
    
    public Email(int id, boolean isNew, String groupID, String from, String to, 
            String subject, String message, String timeStamp,int notificationStatus, int deleteStatus)
    {
        ID = id;
        IsNew = isNew;
        GroupID = groupID;
        Subject = subject;
        From = from;
        To = to;
        Message = message;
        TimeStamp = timeStamp;
        NotificationStatus = notificationStatus;
        DeleteStatus = deleteStatus;
    }
}
