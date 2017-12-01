package WebMail;

/*
 * Confidential Source Code. No unauthorized copying or redistribution in parts or whole allowed. 
 * Copyright 2017. Chiraag Bangera
 */


/**
 *
 * @author Chiraag Bangera
 */

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class TimeKeeper 
{
    
   public static final String DATE_FORMAT = "EEE, d MMM yyyy HH:mm:ss";
   
    public static String Now()
    {
        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat dateFormat = new SimpleDateFormat(DATE_FORMAT);
        return dateFormat.format(calendar.getTime());
    }
    
    
    public static ArrayList<Email> SortEmailsByTime(ArrayList<Email> emails)
    {
        SimpleDateFormat dateFormat = new SimpleDateFormat(TimeKeeper.DATE_FORMAT);
        try
        {
            for(int i=0;i<emails.size()-1;i++)
            {
                Date date1 = dateFormat.parse(emails.get(i).TimeStamp);
                for(int j=i+1;j<emails.size();j++)
                {
                    Date date2 = dateFormat.parse(emails.get(j).TimeStamp);
                    if(date1.compareTo(date2) < 0)
                    {
                        Email t = emails.get(i);
                        emails.set(i, emails.get(j));
                        emails.set(j, t);
                    }
                }
            }
            return emails;
        }
        catch(Exception e)
        {
            WebMail.SOE("Error During Sorting Mails", true);
            e.printStackTrace();
        }
        return null;
    }

}
