package oligoDesigner;


import java.util.ArrayList;
import java.io.PrintWriter;

public class PrintIfTimeExceeded {
    public static void printS(String seq,ArrayList<FmxrTuple> repeats,int[] seqSFA) {
        int len = seq.length();
        String extendedSeq = seq + FusionSite.reverseComplement(seq);
        try {
            PrintWriter pw = new PrintWriter("output.txt","UTF-8");
            pw.write("Sequence: " + seq);
            pw.write("\n\nDetected repeats: ");
            int repeatNr = 0;
            for(int i=0; i<repeats.size(); i++) {
                FmxrTuple currTuple = repeats.get(i);
                int startIndex = seqSFA[currTuple.posInSFA];
                int repeatLength = currTuple.length;

                
                if(startIndex < len) {
                    repeatNr++;
                    pw.write("\nRepeat " + repeatNr + ": "+ extendedSeq.substring(startIndex,startIndex+repeatLength));
                    pw.write("\nRepeat length: " + repeatLength);
                    pw.write("\nIndices of occurences: \n");
                    for(int j=0; j<currTuple.nrOfOccurences; j++) {
                        startIndex = seqSFA[currTuple.posInSFA+j];
                        if(startIndex < len) {
                            pw.write(startIndex + " (FOR)\n");
                        }
                        else {
                            pw.write((2*len-1-startIndex)+" (REV)\n");
                        }
                    }
                }
            }
            pw.print("\nExecution time limit exceeded (3 minutes), no split was found in this time.");
            pw.close();
        }
        
        catch(Exception e) {
            System.out.println(e.getMessage());
        }
        
        
    }
}
