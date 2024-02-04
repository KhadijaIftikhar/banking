package com.projects.banking.ExternalAPIServices;
import org.springframework.stereotype.Service;

import java.io.*;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;

public class SMSGatewayCenterService {



        public static void generateOTP()
        {
//Your apikey key
            String apiKey = "025e22dc9bd35d22d0ef78e6ce3b4b8aeed8ec79";
//OR
            String userId = "alikhalidramay";
            String password = "VUPHY7R6asd";

//Send Method (generate|verify)
            String sendMethod = "generate";

//Message type text/unicode
            String msgType = "text";

//Multiple mobiles numbers separated by comma
            String mobile = "923129784472";
//Your approved sender id
            String senderId = "SGCSMS";
//Your message to terminate, URLEncode the content
            String msg = "Your SMSGatewayCenter OTP Code is $otp$";

//Final OTP Code Expiry
            String codeExpiry = "600";

//OTP Code Length
            String codeLength = "6";

//OTP Code Type
            String codeType = "num";

//Retry After time
            String retryExpiry = "60";

//set to true if you need to regenerate new OTP while resending OTP code
            String renew = "false";

//Valid http url to receive response on your server. E.g http://example.com/getOTPResponse.php
            String callback = "";

//use comma separated for multiple. E.g. sms,email
            String medium = "sms";

//if medium is used with comma separated for multiple. E.g. sms,email then use valid user's email address
            String emailId = "";

//response format
            String format = "json";

//Prepare Url
            URLConnection myURLConnection=null;
            URL myURL=null;
            BufferedReader reader=null;

//URL encode message
            String urlencodedmsg=URLEncoder.encode(msg);

//API End Point
            String mainUrl="https://www.smsgateway.center/OTPApi/send?";

//API Paramters
            StringBuilder sendSmsData= new StringBuilder(mainUrl);
            sendSmsData.append("apiKey="+apiKey);
            sendSmsData.append("&userId="+userId);
            sendSmsData.append("&password="+password);
            sendSmsData.append("&sendMethod="+sendMethod);
            sendSmsData.append("&msgType="+msgType);
            sendSmsData.append("&mobile="+mobile);
            sendSmsData.append("&senderId="+senderId);
            sendSmsData.append("&msg="+urlencodedmsg);
            sendSmsData.append("&codeExpiry="+codeExpiry);
            sendSmsData.append("&codeLength="+codeLength);
            sendSmsData.append("&codeType="+codeType);
            sendSmsData.append("&retryExpiry="+retryExpiry);
            sendSmsData.append("&medium="+medium);
            sendSmsData.append("&emailId="+emailId);
            sendSmsData.append("&format="+format);
//final string
            mainUrl = sendSmsData.toString();
            try
            {
//prepare connection
                myURL = new URL(mainUrl);
                myURLConnection = myURL.openConnection();
                myURLConnection.connect();
                reader= new BufferedReader(new InputStreamReader(myURLConnection.getInputStream()));
//reading response
                String response;
                while ((response = reader.readLine()) != null)
//print response
                    System.out.println(response);

//finally close connection
                reader.close();
            }
            catch (IOException e)
            {
                e.printStackTrace();
            }
        }

}
