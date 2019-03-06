import org.apache.thrift.TException;
import org.apache.thrift.server.*;
import org.apache.thrift.protocol.*;
import org.apache.thrift.transport.*;
import org.apache.thrift.transport.TSSLTransportFactory.TSSLTransportParameters;

import java.io.*;
import java.util.*;
import java.lang.*;

public class MapTask extends Thread {

    private static HashSet<String> NegLib;
    private static HashSet<String> PosLib;
    static {
        String NegativeFile = "./data/negative.txt";
        String PositiveFile = "./data/positive.txt";
        File nfile = new File(NegativeFile);
        File pfile = new File(PositiveFile);
        NegLib = new HashSet<>();
        PosLib = new HashSet<>();
        if (nfile.isFile() && nfile.exists()) {
            try {
                Scanner nscanner = new Scanner(nfile);
                while (nscanner.hasNext()) {
                    String word = nscanner.next();
                    NegLib.add(word);
                }
                nscanner.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        if (pfile.isFile() && pfile.exists()) {
            try {
                Scanner pscanner = new Scanner(pfile);
                while (pscanner.hasNext()) {
                    String word = pscanner.next();
                    PosLib.add(word);
                }
                pscanner.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private String serverIP;
    private int serverPort;
    private String inputFilename;
    private String resultFilename;
    private float loadProbability;
    private long delayTime = 3000;

    public MapTask(String serverIP, int serverPort, String inputFilename, String resultFilename, float loadProbability, long delay)
    {
        this.serverIP = serverIP;
        this.serverPort = serverPort;
        this.inputFilename = inputFilename;
        this.resultFilename = resultFilename;
        this.loadProbability = loadProbability;
        this.delayTime = delay;
    }

    public void run()
    {
        try {
            // load injecting
            double delay = Math.random();
            if (delay < loadProbability){
                sleep(delayTime);
            }

            //count
            int[] score = countScore();
            if (score == null) {
                throw new Exception("file does not exist: " + inputFilename);
            }
            int negativeCounter = score[0];
            int positiveCounter = score[1];
            //calculate sentiment
//            System.out.println("pos points: " + Double.toString(positiveCounter));
//            System.out.println("neg points: " + Double.toString(negativeCounter));
            double sentiment = 1.0 * (positiveCounter - negativeCounter) / (positiveCounter + negativeCounter);
//            System.out.println(Double.toString(sentiment));
            //write to result file
            try {
                File outputFile = new File(resultFilename);
                outputFile.getParentFile().mkdirs();
                //outputFile.createNewFile();
                BufferedWriter out = new BufferedWriter(new FileWriter(outputFile));
                String sentimentString = Double.toString(sentiment);
                String toFileString = new StringBuilder(inputFilename).append(" ").append(sentimentString).toString();
                out.write(toFileString);
                out.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
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
        if (input.exists() && input.isFile()) {
            int[] score = {0, 0};
            try {
                Scanner scn = new Scanner(input).useDelimiter("[^-a-zA-Z]+");
                while (scn.hasNext()) {
                    String temp = scn.next();
                    String str = temp.toLowerCase();
                    for (String word : str.split("--")) {
                        if (NegLib.contains(word)) {
                            score[0]++;
                        }
                        if (PosLib.contains(word)) {
                            score[1]++;
                        }
                    }
                }
                scn.close();
            } catch (Exception e) {
                e.printStackTrace();
                return null;
            }
            return score;
        }
        return null;
    }
}