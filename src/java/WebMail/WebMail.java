package WebMail;

/*
 * Confidential Source Code. No unauthorized copying or redistribution in parts or whole allowed. 
 * Copyright 2017. Chiraag Bangera
 */


/**
 *
 * @author Chiraag Bangera
 */

import java.io.Serializable;
import java.util.ArrayList;
import javax.enterprise.context.SessionScoped;
import javax.faces.context.FacesContext;
import javax.inject.Named;

@Named(value = "webMail")
@SessionScoped
public class WebMail implements Serializable 
{
    public static DBHelper db;
    private Account account;
    private Email email;
    private ArrayList<Email> inbox;
    private ArrayList<Email> sentbox;
    private ArrayList<Email> trashbox;
    ArrayList<Email> conversationbox;
    int selectedEmailID;
    
    public Email getEmail() {
        return email;
    }
    public Account getAccount() {
        return account;
    }

    public ArrayList<Email> getInbox() {
        return inbox;
    }
    
    public ArrayList<Email> getSentBox() {
        return sentbox;
    }

    public ArrayList<Email> getTrashBox() {
        return trashbox;
    }

    public ArrayList<Email> getConversationBox() {
        return conversationbox;
    }
    
    public WebMail()
    {
        if(db == null)
        {
            try {
               Class.forName("com.mysql.jdbc.Driver");
           } catch (Exception e) {
               e.printStackTrace();
           }
           db = new DBHelper("mis-sql.uhcl.edu/bangerac8361","bangerac8361","1603798");   
        }
        account = new Account();
        email = new Email();
        inbox = new ArrayList<Email>();
        sentbox = new ArrayList<Email>();
        trashbox = new ArrayList<Email>();
        conversationbox = new ArrayList<Email>();
    }
    
   
    
    public String Login() 
    {
        account = db.GetUserAccount(account.UserName, account.Password);
        if(account == null )
        {
            FacesContext.getCurrentInstance().addMessage("WebMail", new javax.faces.application.FacesMessage("Incorrect Credentials"));
            return null;
        }
        refresh();
        return "account.xhtml"; 
    }
   
    
     
     public String register()
    {
        if(!account.Password.equals(account.Password))
        {
            FacesContext.getCurrentInstance().addMessage("WebMail", new javax.faces.application.FacesMessage("Password Mismatch"));
            return null;
        }
        else
        {
            String id = db.GenerateRandomID(account.Name.toLowerCase());
            String email = id+"@webmail.com";
            email=email.toLowerCase();
            if(WebMail.db.VerifyUserAccount(id))
            {
                 WebMail.SOE("User Already Exists...",true);
                 FacesContext.getCurrentInstance().addMessage("WebMail", new javax.faces.application.FacesMessage("User Already Exists..."));
                 return null;
            }
            account.Email = email;
            account.UserName = id;
            db.SaveUserAccount(account);
            WebMail.SOP("Registered.... Your UserName is "+id, true);
            FacesContext.getCurrentInstance().addMessage("WebMail", new javax.faces.application.FacesMessage("Registered.... Your UserName is "+id));
            refresh();
            return "account.xhtml";
        }
    }
    
    public String refresh()
    {
        inbox = TimeKeeper.SortEmailsByTime(db.GetInbox(account));
        sentbox = TimeKeeper.SortEmailsByTime(db.GetSent(account));
        trashbox = TimeKeeper.SortEmailsByTime(db.GetTrash(account));
        return null;
    }
    
    public String previewEmailByID(int i)
    {
         selectedEmailID = i;
         return "previewdelete.xhtml";         
    
    }
    
    public Email previewSelectedEmail()
    {
        for(Email e: trashbox)
        {
            if(e.ID == selectedEmailID)
            {
                return e;
            }
        }
        return null;
    }
    
    
    public String getSentEmailByID(int i)
    {
         selectedEmailID = i;
         return "viewsentemail.xhtml";         
    
    }
    
    public Email readSentSelectedEmail()
    {
        for(Email e: sentbox)
        {
            if(e.ID == selectedEmailID)
            {
                return e;
            }
        }
        return null;
    }


    public String getEmailByID(int i)
    {
         selectedEmailID = i;
         return "viewemail.xhtml";         
    
    }
    
    public Email readSelectedEmail()
    {
        for(Email e: inbox)
        {
            if(e.ID == selectedEmailID)
            {
                db.MarkEmailAsRead(e);
                refresh();
                return e;
            }
        }
        return null;
    }


    public String conversationView(int emailID)
    {
        selectedEmailID = emailID;
        email = readSelectedEmail();
        conversationbox = TimeKeeper.SortEmailsByTime(db.GetConversation(account, email));    
        if(conversationbox.size() > 1)
        {
            return "viewconversation.xhtml";
        }
        else
        {
            return "viewemail.xhtml";
        }
    }

    
    public String getNameForEmailID(String emailID)
    {
        return db.NameForEmailID(emailID);
    }
    
    
    public String send()
    {
        if(email.To == null)
        {
            return "error";
        }
        email.From = account.UserName;
        email.To = Account.UserNameFromEmail(email.To);
        db.SendEmail(email);
        email = new Email();
        refresh();
        return "confirmsend.xhtml";
    }
    
    
    public String reply(int emailID)
    {
        selectedEmailID = emailID;
        email = readSelectedEmail();
        email.Subject = "RE: "+ email.Subject;
        String temp = email.From;
        email.From = email.To;
        email.To = temp;
        email.Message = "";
        email.To = Account.UserNameFromEmail(email.To);
        email.From = Account.UserNameFromEmail(email.From);
        return "reply.xhtml";
    }
    
    public String sendReply()
    {
        String gID = "";
        if(email.GroupID == null || email.GroupID == "")
        {
           gID = db.GenerateConversationID(email);
        }
        else
        {
            gID = email.GroupID;
        }
        email = new Email(email.ID,true,gID,Account.UserNameFromEmail(email.From),Account.UserNameFromEmail(email.To),
                email.Subject,email.Message,"",email.NotificationStatus,-1);
        db.ReplyEmail(email);
        email = new Email();
        refresh();
        return "confirmsend.xhtml";
    }

    public String forward(int emailID)
    {
        selectedEmailID = emailID;
        email = readSelectedEmail();
        email.Subject = "FW: "+ email.Subject;
        email.To = "";
        return "compose.xhtml";
    }

    public String trash(int emailID)
    {
        selectedEmailID = emailID;
        email = readSelectedEmail();
        db.TrashEmail(account,email);
        email = new Email();
        refresh();
        return "account.xhtml";
    }

    public String delete(int emailID)
    {
        selectedEmailID = emailID;
        email = previewSelectedEmail();
        db.DeleteEmail(account,email);
        email = new Email();
        refresh();
        return "trash.xhtml";
    }


   
    
    
    public String compose(String address, String subject, String message, int notifyEnable)
    {
        Email email = new Email(0,true,"",account.UserName,address,subject,message,"",notifyEnable,-1);
        db.SendEmail(email);
        refresh();
        return "confirmsend.xhtml";
    }
    
    
    
    public String signOut()
    {
        FacesContext.getCurrentInstance().getExternalContext().invalidateSession();
        return "index.xhtml";
    }
    
    public static void SOE(String str, boolean newLine)
    {
        if(newLine)
        {
            System.err.println(str);
        }
        else
        {
            System.err.print(str);
        }
    }
    
    
    public static void SOP(String str, boolean newLine)
    {
        if(newLine)
        {
            System.out.println(str);
        }
        else
        {
            System.out.print(str);
        }
    }
}
