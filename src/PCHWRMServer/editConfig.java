package PCHWRMServer;

public class editConfig {

    static String screenWidth, screenHeight, serverPort;

    public static void main(String[] args) throws Exception
    {
        io io = new io();

        io.pln("PCHWRM_Client"+
                "\nVersion : 1.0"+
                "\nAuthor : Debayan Sutradhar"+
                "\nSource : https://github.com/dubbadhar/PCHWRM_Client"+
                "\nBuild Date : 17th April 2020\nEnter 'N' if you want to Quit.\n\n");

        String[] confArr = io.readFileArranged("config","::");

        screenWidth = confArr[0];
        screenHeight = confArr[1];
        serverPort = confArr[2];

        while(true)
        {
            io.pln("Enter Screen Width \n"+
                    "Current Value : "+screenWidth+"\n" +
                    "New Value : ");

            String userInput = io.readConsoleLine();

            if(userInput.equalsIgnoreCase("N"))
            {
                io.pln("Abort!");
                return;
            }
            else
            {
                if(checkNo(userInput))
                {
                    screenWidth = userInput;
                    break;
                }
                else
                {
                    io.pln("Must be a number!");
                }
            }
        }

        while(true)
        {
            io.pln("Enter Screen Height \n"+
                    "Current Value : "+screenHeight+"\n" +
                    "New Value : ");

            String userInput = io.readConsoleLine();

            if(userInput.equalsIgnoreCase("N"))
            {
                io.pln("Abort!");
                return;
            }
            else
            {
                if(checkNo(userInput))
                {
                    screenHeight = userInput;
                    break;
                }
                else
                {
                    io.pln("Must be a number!");
                }
            }
        }

        while(true)
        {
            io.pln("Enter Server Port \n"+
                    "Current Value : "+serverPort+"\n" +
                    "New Value : ");

            String userInput = io.readConsoleLine();

            if(userInput.equalsIgnoreCase("N"))
            {
                io.pln("Abort!");
                return;
            }
            else
            {
                if(checkNo(userInput))
                {
                    if(Integer.parseInt(userInput) < 1024)
                    {
                        io.pln("Must be > 1024 since you're on linux!");
                    }
                    else
                    {
                        serverPort = userInput;
                        break;
                    }
                }
                else
                {
                    io.pln("Must be a number!");
                }
            }
        }

        io.pln("\n\nNew Config :" +
                "\nScreen Width : "+screenWidth+
                "\nScreen Height : "+screenHeight+
                "\nServer Port : "+serverPort+
                "\nAPPLY SETTINGS? [Y/N]");

        String userInput = io.readConsoleLine();
        if(userInput.equalsIgnoreCase("y"))
        {
            io.pln("\nWriting New Config ...");
            io.writeToFile(screenWidth+"::"+screenHeight+"::"+serverPort+"::","config");
            io.pln("... Done!");
        }
        else
        {
            io.pln("Abort!");
        }
    }

    public static boolean checkNo(String no)
    {
        try
        {
            Integer.parseInt(no);
            return true;
        }
        catch (Exception e)
        {
            return false;
        }
    }
}
