package PCHWRMServer;

import java.io.*;

public class io {

    public void pln(String txt)
    {
        System.out.println(txt);
    }

    public String readFileRaw(String fileLocation) throws Exception
    {
        String tbr;
        File f = new File(fileLocation);
        BufferedReader bf = new BufferedReader(new FileReader(f));
        tbr = bf.readLine();
        bf.close();
        return tbr;
    }

    public void writeToFile(String content,String fileName)
    {
        try
        {
            File f = new File(fileName);
            if(!f.exists())
                if(!f.createNewFile()) throw new Exception("Insufficient Permissions to create "+fileName);
            BufferedWriter bf = new BufferedWriter(new FileWriter(fileName));
            bf.write(content);
            bf.close();
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    public String readConsoleLine() throws Exception
    {
        BufferedReader bf = new BufferedReader(new InputStreamReader(System.in));
        return bf.readLine();
    }

    public String[] readFileArranged(String fileLocation, String sep) throws Exception
    {
        return readFileRaw(fileLocation).split(sep);
    }

    public String getShellOutput(String cmd) throws Exception
    {
        Process p = Runtime.getRuntime().exec(cmd);
        InputStreamReader isr = new InputStreamReader(p.getInputStream());

        StringBuilder sb = new StringBuilder();
        while (true)
        {
            int x = isr.read();
            if(x!=-1)
            {
                sb.append((char) x);
            }
            else break;
        }

        isr.close();
        p.destroy();

        return sb.toString();
    }
}
