package com.idbiintech.visitormgmtsystem.util;


public class BcpIdForAES 
{
    public static String getsecretkey(String num1, String num2, String numericValue) 
    {

        String revStr = null;
        try {

            String newStr="";
            for(int i = 0; i < num1.length() ; i++)
            {
                char character = num1.charAt(i);
                int ascii = (int) character;
                newStr = newStr+ascii;
            }

            for(int i = 0; i < num2.length() ; i++)
            {
                char num2Character = num2.charAt(i);
                int num2Ascii;
                if(i==0)
                {
                    num2Ascii = (int) num2Character;
                    newStr = newStr+num2Ascii;
                }
                else if(i==num2.length())
                {
                    num2Ascii = (int) num2Character;
                    newStr = newStr+num2Ascii;
                }
            }

            char[] try1 = newStr.toCharArray();
            revStr = "";
            for (int i = try1.length-1;i >= 0; i--) {
                //System.out.print(try1[i]);
                revStr = revStr+try1[i];
            }

        } catch (Exception ex) {
            ex.printStackTrace();
        }

        String value = revStr+numericValue;

        return value;

    }
}
