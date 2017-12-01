package WebMail;

/*
 * Confidential Source Code. No unauthorized copying or redistribution in parts or whole allowed. 
 * Copyright 2017. Chiraag Bangera
 */


/**
 *
 * @author Chiraag Bangera
 */

import java.sql.*;
import java.util.ArrayList;



// Global SQL Command Center.


public class DBHelper
{

    private String _databaseURL;
    private String _userName;
    private String _password;
    private boolean developmentBuild = false;
    
    private Connection connection = null;
    private ResultSet result = null;
    private Statement statement = null;

    public DBHelper(String databaseURL, String userName, String password)
    {
        _databaseURL = databaseURL;
        _userName = userName;
        _password = password;
        ConnectSQL();
    }
    
    public boolean ConnectSQL ()
    {
        
        try
        {
            if(developmentBuild)
            {
                WebMail.SOP("Attempting Database Connection...",true);
            }
            connection = DriverManager.getConnection("jdbc:mysql://"+_databaseURL,_userName,_password);
            statement = connection.createStatement();
            // test to see if connection to db is sucessfull.
            if(!isOpen())
            { 
                if(developmentBuild)
                {
                    WebMail.SOE("Database Connection... FAILED",true);
                }
                return false;
            }
            if(developmentBuild)
            {
                WebMail.SOP("Connection Successful",true);   
            }
            return true;
        }
        catch (Exception e)
        {
            WebMail.SOE("Error While Connecting",true);
            e.printStackTrace();
        }       
        return false;
    }
    
    
    public boolean isOpen()
    {
        try
        {
            if(connection.isValid(0))
            {
                return true;
            }
            return false;
        }
        catch (Exception e)
        {
            WebMail.SOE("Error While accessing Connection Info", true);
            e.printStackTrace();
        }
        return false;
    }
    
    
    public Account GetUserAccount(String userName, String password) 
    {
        if(isOpen())
        {
            try
            {
                if(developmentBuild)
                {
                    WebMail.SOP("Attempting to get User Account",true);
                }
                result = statement.executeQuery("select * from users where userid = '"+userName+"'");
                if(result.next())
                {
                    if(userName.equals(result.getString("userid")) && password.equals(result.getString("password")))
                    {
                        if(developmentBuild)
                        {
                            WebMail.SOP("Fetch Successful",true);
                        }
                        Account account = new Account(result.getString("userid"),
                                result.getString("name"),result.getString("email"),
                                result.getString("password"));  
                        return account;
                    }
                    if(developmentBuild)
                    {
                        WebMail.SOP("Invalid Credentials Entered",true);
                    }
                    return null;
                }
            }
            catch(Exception e)
            {
                WebMail.SOE("Error Occured While Fetching User Accounts", true);
                e.printStackTrace();
            }
        }
        if(developmentBuild)
        {
            WebMail.SOP("Fetch FAILED",true);
        }
        return null;
    }
   
    
    public boolean VerifyUserAccount(String userID)
    {
        if(isOpen())
        {
            try
            {
                if(developmentBuild)
                {
                    WebMail.SOP("Verifying Records",true);
                }
                result = statement.executeQuery("select userid from users where userid = '"+userID+"'"); 
                if(result.next())
                {
                    if(developmentBuild)
                    {
                        WebMail.SOP("Record Exists",true);
                    }
                    return true; 
                }
                return false;
            }
            catch(Exception e)
            {
                WebMail.SOE("Error Occured while Accessing Records", true);
                e.printStackTrace();
            }
        }
        if(developmentBuild)
        {
            WebMail.SOP("Accessing Recoreds FAILED",true);
        }
        return false;
    }
    
    
    
    public boolean SaveUserAccount(Account account) 
    {
        if(isOpen())
        {
            try
            {
                if(developmentBuild)
                {
                    WebMail.SOP("Attempting to Save New User",true);
                }
                statement.executeUpdate("insert into users values ('"+account.UserName+"','"
                        +account.Name+"','"+account.Email+"','"+account.Password+"')"); 
                 if(developmentBuild)
                {
                    WebMail.SOP("Save Successfull",true);
                }
                return true;
            }
            catch(Exception e)
            {
                WebMail.SOE("Error Occured while Saving User Account", true);
                e.printStackTrace();
            }
        }
        if(developmentBuild)
        {
            WebMail.SOP("Save FAILED",true);
        }
        return false;
    }
    
    
    public String GenerateRandomID(String name)
    {
        String userID = "";
        if(isOpen())
        {
            try
            {
                if(developmentBuild)
                {
                    WebMail.SOP("Attempting to Get Counter Data",true);
                }
                result = statement.executeQuery("select number from accountkeys");
                if(result.next())
                {
                    int value = result.getInt("number");
                    if(name.length() > 10)
                    {
                        name = name.substring(0,10);
                    }
                    userID = name+value++;
                    statement.executeUpdate("update accountkeys set number = "+value +" WHERE number="+(value-1));
                     if(developmentBuild)
                    {
                        WebMail.SOP("Update  Counter Successfull",true);
                    }
                    return userID.toLowerCase();
                }
            }
            catch(Exception e)
            {
                WebMail.SOE("Error Occured while Accessing Online DB", true);
                e.printStackTrace();
            }
        }
        if(developmentBuild)
        {
            WebMail.SOE("Query Failed",true);
        }
        return null;
    }
    
    
    public String NameForEmailID(String emailID)
    {
        String userID = Account.UserNameFromEmail(emailID);
        if(isOpen())
        {
            try
            {
                if(developmentBuild)
                {
                    WebMail.SOP("Fetching Name",true);
                }
                result = statement.executeQuery("select name from users where userid = '"+userID+"'"); 
                if(result.next())
                {
                    if(developmentBuild)
                    {
                        WebMail.SOP("Name Found",true);
                    }
                    return result.getString("name"); 
                }
                return null;
            }
            catch(Exception e)
            {
                WebMail.SOE("Error Occured while Accessing Records", true);
                e.printStackTrace();
            }
        }
        if(developmentBuild)
        {
            WebMail.SOP("Accessing Recoreds FAILED",true);
        }
        return null;
    }
    
    
    public void MarkEmailAsRead(Email email)
    {
       if(isOpen())
        {
            try
            {
                if(developmentBuild)
                {
                    WebMail.SOP("Attempting to Mark Email as Read",true);
                }
                statement.executeUpdate("update emailstore set isnew=0 where id="+email.ID);   
            }
            catch(Exception e)
            {
                WebMail.SOE("Error Occured While Marking Email", true);
                e.printStackTrace();
            }
        }
        if(developmentBuild)
        {
            WebMail.SOP("Marking Email FAILED",true);
        } 
    }
    
    
    
    
    public ArrayList<Email> GetInbox(Account account)
    {
        ArrayList<Email> emails = new ArrayList<Email>();
        if(isOpen())
        {
            try
            {
                if(developmentBuild)
                {
                    WebMail.SOP("Attempting to Fetch Emails",true);
                }
                result = statement.executeQuery("select * from emailstore where tousername ='"+account.UserName+"' and deletestatus = -1"); 
                if(developmentBuild)
                {
                    WebMail.SOP("Fetching Emails Successfull",true);
                }
                while(result.next())
                {
                    Email email = new Email(result.getInt("id"),result.getBoolean("isnew"),result.getString(("groupid")),
                            result.getString("fromusername")+"@webmail.com",result.getString("tousername")+"@webmail.com",
                            result.getString("subject"),result.getString("message"),
                            result.getString("timestamp"), result.getInt("notificationstatus"), result.getInt("deletestatus"));
                    if(email.IsNew)
                    {
                        email.Subject = "(NEW)"+email.Subject;
                    }
                    int flag = 0;
                    // i had missed to add this check hence the error was being shown ... 
                    if(email.GroupID != null && email.GroupID != "")
                    {
                        for(int i=0;i<emails.size();i++)
                        {
                            if(emails.get(i).GroupID != null && emails.get(i).GroupID != "")
                            {
                                if(emails.get(i).GroupID.equalsIgnoreCase(email.GroupID))
                                {
                                    flag = 1;
                                    break;
                                }
                            }
                        }
                    }
                    if(flag == 0)
                    {
                        emails.add(email);
                    }
                }
                if(developmentBuild)
                {
                    WebMail.SOP("Fetched "+emails.size()+" Email(s)",true);
                }
            return emails;   
            }
            catch(Exception e)
            {
                WebMail.SOE("Error Occured While Fetching Emails", true);
                e.printStackTrace();
            }
        }
        if(developmentBuild)
        {
            WebMail.SOP("Fetching Email's FAILED",true);
        }
        return null;
    }
    
    
    public ArrayList<Email> GetSent(Account account)
    {
        ArrayList<Email> emails = new ArrayList<Email>();
        if(isOpen())
        {
            try
            {
                if(developmentBuild)
                {
                    WebMail.SOP("Attempting to Fetch Sent Emails",true);
                }
                result = statement.executeQuery("select * from emailstore where fromusername ='"+account.UserName+"' "
                        + "and (deletestatus= -1 or deletestatus = 0 or deletestatus = 1)"); 
                if(developmentBuild)
                {
                    WebMail.SOP("Fetch Sent Emails Successfull",true);
                }
                while(result.next())
                {
                    Email email = new Email(result.getInt("id"),result.getBoolean("isnew"),result.getString("groupid"),
                            result.getString("fromusername")+"@webmail.com",result.getString("tousername")+"@webmail.com",
                            result.getString("subject"),result.getString("message"),
                            result.getString("timestamp"), result.getInt("notificationstatus"), result.getInt("deletestatus"));
                    emails.add(email);
                }
                if(developmentBuild)
                {
                    WebMail.SOP("Fetched "+emails.size()+" Email(s)",true);
                } 
             return emails;   
            }
            catch(Exception e)
            {
                WebMail.SOE("Error Occured While Fetching Sent Emails", true);
                e.printStackTrace();
            }
        }
        if(developmentBuild)
        {
            WebMail.SOP("Fetching Email's FAILED",true);
        }
        return null;
    }
    


    public ArrayList<Email> GetTrash(Account account)
    {
        ArrayList<Email> emails = new ArrayList<Email>();
        if(isOpen())
        {
            try
            {
                if(developmentBuild)
                {
                    WebMail.SOP("Attempting to Fetch Trashed Emails",true);
                }
                result = statement.executeQuery("select * from emailstore where tousername ='"+account.UserName+"' "
                        + "and (deletestatus = 0 or deletestatus = 2 or deletestatus = 4 or deletestatus = 5 or deletestatus = 6)"); 
                if(developmentBuild)
                {
                    WebMail.SOP("Fetch Trashed Emails Successfull",true);
                }
                while(result.next())
                {
                    Email email = new Email(result.getInt("id"),result.getBoolean("isnew"),result.getString("groupid"),
                            result.getString("fromusername")+"@webmail.com",result.getString("tousername")+"@webmail.com",
                            result.getString("subject"),result.getString("message"),
                            result.getString("timestamp"), result.getInt("notificationstatus"), result.getInt("deletestatus"));
                    emails.add(email);
                }
                if(developmentBuild)
                {
                    WebMail.SOP("Fetched "+emails.size()+" Email(s)",true);
                } 
             return emails;   
            }
            catch(Exception e)
            {
                WebMail.SOE("Error Occured While Fetching Trashed Emails", true);
                e.printStackTrace();
            }
        }
        if(developmentBuild)
        {
            WebMail.SOP("Fetching Trashed Email's FAILED",true);
        }
        return null;
    }
    
    public void SendEmail(Email email)
    {
        if(isOpen())
        {
            try
            {
                if(developmentBuild)
                {
                    WebMail.SOP("Attempting to Send Email",true);
                }
                result = statement.executeQuery("select mailnumber from accountkeys");
                int emailID = 0;
                if(result.next())
                {
                    emailID = result.getInt("mailnumber");
                    emailID++;
                    statement.executeUpdate("update accountkeys set mailnumber = "+emailID +" where mailnumber = "+(emailID-1));
                }
                statement.executeUpdate("insert into emailstore values(1, NULL, "+emailID+",'"+email.From+
                        "','"+email.To+"','"+email.Subject+"','"+email.Message+"','"+
                        TimeKeeper.Now()+"',"+email.NotificationStatus+",-1)");   
            }
            catch(Exception e)
            {
                WebMail.SOE("Error Occured While Sending Email", true);
                e.printStackTrace();
            }
        }
        if(developmentBuild)
        {
            WebMail.SOP("Sending Email FAILED",true);
        }
    }
    

    public String GenerateConversationID(Email email)
    {
        if(isOpen())
        {
            try
            {
                if(developmentBuild)
                {
                    WebMail.SOP("Attempting to Generate Convo ID",true);
                }
                String gID = "";
                if(Account.UserNameFromEmail(email.From).compareTo(Account.UserNameFromEmail(email.To)) < 0)
                {
                    gID = Account.UserNameFromEmail(email.From)+Account.UserNameFromEmail(email.To);
                }
                else
                {
                    gID = Account.UserNameFromEmail(email.To)+Account.UserNameFromEmail(email.From);
                }
                result = statement.executeQuery("select groupid from emailstore where groupid like '"+gID+"_%' order by groupid desc");
                if(result.next())
                {
                    String ingID = result.getString("groupid");
                    int newGID = Integer.parseInt(ingID.substring(ingID.indexOf('_')+1,ingID.length())) + 1;
                    gID  = gID+"_"+newGID;
                }
                else
                {
                    gID += "_0";
                }
                return gID;
            }
            catch(Exception e)
            {
                WebMail.SOE("Error Occured While Creating Convo ID", true);
                e.printStackTrace();
            }
        }
        if(developmentBuild)
        {
            WebMail.SOP("Creating Convo ID FAILED",true);
        }
        return null;
    }
    // fix reply module
    public void ReplyEmail(Email email)
    {
        if(isOpen())
        {
            try
            {
                if(developmentBuild)
                {
                    WebMail.SOP("Attempting to Reply Email",true);
                }
                result = statement.executeQuery("select mailnumber from accountkeys");
                int emailID = 0;
                if(result.next())
                {
                    emailID = result.getInt("mailnumber");
                    emailID++;
                    statement.executeUpdate("update accountkeys set mailnumber = "+emailID +" where mailnumber = "+(emailID-1));
                }
                statement.executeUpdate("update emailstore set subject = '"+email.Subject+"',groupid ='"+email.GroupID+"' "
                        + "where id = "+email.ID);  
                statement.executeUpdate("update emailstore set subject = '"+email.Subject+"' where groupid = '"+email.GroupID+"'");
                statement.executeUpdate("insert into emailstore values(1,'"+email.GroupID+"',"+emailID+",'"+email.From+
                        "','"+email.To+"','"+email.Subject+"','"+email.Message+"','"+TimeKeeper.Now()+"',"
                        +email.NotificationStatus+",-1)");
            }
            catch(Exception e)
            {
                WebMail.SOE("Error Occured While Sending Email", true);
                e.printStackTrace();
            }
        }
        if(developmentBuild)
        {
            WebMail.SOP("Sending Email FAILED",true);
        }
    }

    public void TrashEmail(Account account,Email email)
    {
        if(isOpen())
        {
            try
            {
                if(developmentBuild)
                {
                    WebMail.SOP("Attempting to Mark Email as Trashed",true);
                }
                int deleteStatus = -1;
                if(email.DeleteStatus == 0 || email.DeleteStatus == 2)
                {
                    deleteStatus = 6;
                }
                else if(email.DeleteStatus == 3)
                {
                    deleteStatus = 4;
                }
                else
                {
                    if(account.UserName.equals(Account.UserNameFromEmail(email.To)))
                    {
                        deleteStatus = 0;
                    }
                    else
                    {
                        deleteStatus = 2;
                    }
                }
                
                statement.executeUpdate("update emailstore set deletestatus = "+deleteStatus+" where id = "+email.ID);
            }
            catch(Exception e)
            {
                WebMail.SOE("Error Occured While Trashing Email", true);
                e.printStackTrace();
            }
        }
        if(developmentBuild)
        {
            WebMail.SOP("Trashing Email FAILED",true);
        }
    }

    public void DeleteEmail(Account account,Email email)
    {
        if(isOpen())
        {
            try
            {
                if(developmentBuild)
                {
                    WebMail.SOP("Attempting to Mark Email as Deleted",true);
                }
                int deleteStatus = -1;
                if(email.DeleteStatus == 1 || email.DeleteStatus == 3)
                {
                    statement.executeUpdate("delete emailstore where id = "+email.ID);
                }
                else if(email.DeleteStatus == 2)
                {
                    deleteStatus = 5;
                }
                else
                {
                    if(account.UserName.equals(Account.UserNameFromEmail(email.To)))
                    {
                        deleteStatus = 1;
                    }
                    else
                    {
                        deleteStatus = 3;
                    }
                }
                statement.executeUpdate("update emailstore set deletestatus = "+deleteStatus+" where id = "+email.ID);
            }
            catch(Exception e)
            {
                WebMail.SOE("Error Occured While Deleting Email", true);
                e.printStackTrace();
            }
        }
        if(developmentBuild)
        {
            WebMail.SOP("Deleting Email FAILED",true);
        }
    }
    
    
    public ArrayList<Email> GetConversation(Account account, Email conduitEmail)
    {
        ArrayList<Email> emails = new ArrayList<Email>();
        if(isOpen())
        {
            try
            {
                if(developmentBuild)
                {
                    WebMail.SOP("Attempting to Fetch Conversation Emails",true);
                }
                result = statement.executeQuery("select * from emailstore where groupid ='"+conduitEmail.GroupID+"'"); 
                if(developmentBuild)
                {
                    WebMail.SOP("Fetch Conversation Emails Successfull",true);
                }
                while(result.next())
                {
                    Email email = new Email(result.getInt("id"),result.getBoolean("isnew"),result.getString("groupid"),
                            result.getString("fromusername")+"@webmail.com",result.getString("tousername")+"@webmail.com",
                            result.getString("subject"),result.getString("message"),
                            result.getString("timestamp"), result.getInt("notificationstatus"), result.getInt("deletestatus"));
                    emails.add(email);
                }
                if(developmentBuild)
                {
                    WebMail.SOP("Fetched "+emails.size()+" Conversation Email(s)",true);
                } 
             return emails;   
            }
            catch(Exception e)
            {
                WebMail.SOE("Error Occured While Fetching Conversation Emails", true);
                e.printStackTrace();
            }
        }
        if(developmentBuild)
        {
            WebMail.SOP("Fetching Conversation Email's FAILED",true);
        }
        return null;
    }
    
        
    public String[] getStringsFromQuery(String query)
    {
        if(isOpen())
        {
            try
            {
                if(developmentBuild)
                {
                    WebMail.SOP("Attempting to Query",true);
                }
                result = statement.executeQuery(query); 
                if(result.next())
                {
                    if(developmentBuild)
                    {
                        WebMail.SOP("Query Successfull",true);
                    }
                    int count = 0;
                    ArrayList<String> results = new ArrayList<String>();
                    while(result.next())
                    {
                        results.add(result.getString(count++));
                    }
                    return results.toArray(new String[0]);
                }
             return null;   
            }
            catch(Exception e)
            {
                WebMail.SOE("Error Occured During Query", true);
                e.printStackTrace();
            }
        }
        if(developmentBuild)
        {
            WebMail.SOP("Query FAILED",true);
        }
        return null;
    }
    
    
    public boolean Close()
    {
        if(this.isOpen())
        {
            try
            {
                if(developmentBuild)
                {
                    WebMail.SOP("Closing Connection",true);
                }
                connection.close();
                statement.close();
                if(result != null)
                {
                    result.close();
                }
                if(developmentBuild)
                {
                    WebMail.SOP("Connection Closed",true);
                }
                return true;
            }
            catch(Exception e)
            {
                WebMail.SOP("Error While Closing Connection", true);
                e.printStackTrace();
            }
        }
        return false;
    }
        
    
}
