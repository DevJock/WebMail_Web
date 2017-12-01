package WebMail;

/*
 * Confidential Source Code. No unauthorized copying or redistribution in parts or whole allowed. 
 * Copyright 2017. Chiraag Bangera
 */


/**
 *
 * @author Chiraag Bangera
 */
public class Account 
{
    public int ID;
    public String GroupID;
    public String From;
    public String To;
    public String Subject;
    public String Message;
    public boolean IsNew;
    public String TimeStamp;

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
    
    
    
    public String Name;
    public String Password;
    public  String Email;
    public String UserName;

    public String getName() {
        return Name;
    }

    public void setName(String Name) {
        this.Name = Name;
    }

    public String getPassword() {
        return Password;
    }

    public void setPassword(String Password) {
        this.Password = Password;
    }

    public String getEmail() {
        return Email;
    }

    public void setEmail(String Email) {
        this.Email = Email;
    }

    public String getUserName() {
        return UserName;
    }

    public void setUserName(String UserName) {
        this.UserName = UserName;
    }


    
    public Account()
    {
        Name = null;
        UserName = null;
        Email = null;
        Password = null;
    }
    public Account(String name, String password)
    {
        Name = name;
        Password = password;
    }
    
    
    
    public Account(String userName, String name, String email, String password)
    {
        Name = name;
        UserName = userName;
        Email = email;
        Password = password;
    }
    
    public String getNameForEmail(String email)
    {
        if(email.length() < 1)
        {
            return null;
        }
        return email.substring(0, email.indexOf("@"));
    }
    
    public static String UserNameFromEmail(String email)
    {
        if(email.length() < 1)
        {
            return null;
        }
        if(!email.contains("@"))
        {
            return email;
        }
        return email.substring(0, email.indexOf("@"));
    }
}
