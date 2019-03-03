import org.apache.thrift.TException;
import java.util.*;
import java.lang.*;

public class MapTask extends Thread {

    // private static String NegativeFile = "negative.txt";
    // private static String PositiveFile = "positive.txt";
    private static ArrayList<String> NegLib;// = new ArrayList<String>();
    private static ArrayList<String> PosLib;// = new ArrayList<String>();
    static {
        String NegativeFile = "negative.txt";
        String PositiveFile = "positive.txt";
        File nfile = new File(NegativeFile);
        File pfile = new File(PositiveFile);
        NegLib = new ArrayList<String>();
        PosLib = new ArrayList<String>();
        if (nfile.isFile() && nfile.exist()) {
            try {
                Scanner nscanner = new Scanner(nfile);
                while (nscanner.hasNext()) {
                    String word = nscanner.next();
                    NegLib.add(word);
                }
                nscanner.close();
            } catch (FileNotFoundException e) {
                System.out.println("SHIT happen at line 27");
            }
        }

        if (pfile.isFile() && pfile.exist()) {
            try {
                Scanner pscanner = new Scanner(pfile);
                while (pscanner.hasNext()) {
                    String word = nscanner.next();
                    PosLib.add(word);
                }
                pscanner.close();
            } catch (FileNotFoundException e) {
                System.out.println("SHIT happen at line 40");
            }
        }
    }

    private String serverIP;
    private int serverPort;
    private String inputFilename;
    private String resultFilename;

    public MapTask(String serverIP, int serverPort, String inputFilename, String resultFilename)
    {
        this.serverIP = serverIP;
        this.serverPort = serverPort;
        this.inputFilename = inputFilename;
        this.resultFilename = resultFilename;
    }

    public void run()
    {
        try {
            //count
            int[2] score = countScore();
            if (score == null) {
                throw new Exception("aaaaaaaaaaaaaa");
            }
            int negativeCounter = score[0];
            int positiveCounter = score[1];
            //calculate sentiment
            float sentiment = 1.0 * (positiveCounter - negativeCounter) / (positiveCounter + negativeCounter);
            //write to result file
            // @todo
            //notice server
            TTransport transport = new TSocket(serverIP, serverPort);
            TProtocol protocol = new TBinaryProtocol(new TFramedTransport(transport));
            MasterServer.Client server = new MasterServer.Client(protocol);
            //Try to connect
            transport.open();
            server.noticeFinishedMap(resultFilename);
            transport.close();
        } catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    private int[] countScore() {
        File input = new File(inputFilename);
        if (input.exist() && input.isFile()) {
            int[] score = {0, 0};
            try {
                Scanner scn = new Scanner(input);
                while (scn.hasNextLine()) {
                    String str = scn.nextLine();
                    for (String word : str.split("[ ',.:;/\\\\?!|]+|--")) {
                        if (NegLib.contains(word)) {
                            score[0]++;
                        }
                        if (PosLib.contains(word)) {
                            score[1]++;
                        }
                    }
                }
            } catch (FileNotFoundException e) {
                System.out.println("Error in count scores");
                return null;
            }
            return score;
        }
        return null;
    }
}