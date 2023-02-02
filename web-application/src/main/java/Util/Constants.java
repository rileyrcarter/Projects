package Util;

import java.util.regex.Pattern;

public class Constants {
    static public String DBUserName = "root";
    static public String DBPassword = "root";
    public static String DBConnect = "jdbc:mysql://localhost:3306/CSCI201_project_database";
    
    static public Pattern namePattern = Pattern.compile("^[ A-Za-z]+$");
    static public Pattern emailPattern = Pattern.compile("^[a-zA-Z0-9_+&*-]+(?:\\."
            + "[a-zA-Z0-9_+&*-]+)*@"
            + "(?:[a-zA-Z0-9-]+\\.)+[a-z"
            + "A-Z]{2,7}$");
    
    static public Pattern urlPattern = Pattern.compile("^https?:\\/\\/.*$");
}
