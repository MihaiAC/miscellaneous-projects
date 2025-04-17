package oligoDesigner;

import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.ArrayList;
import java.util.LinkedList;
import java.io.PrintWriter;

public class Main {
    
    public static LinkedList<FSAndLocation> splitSequence(String seq, int repeatMinLen, int partMinLen, int partMaxLen, int maxPartCommonSeq) {
        //Make the sequence uppercase.
        seq = seq.toUpperCase();
        int len = seq.length();
        String extendedSeq = seq + FusionSite.reverseComplement(seq); //extendedSeq contains the sequence and its 
                                                                      //reverse complement back to back;
        
        int[] seqSFA = SuffixArray.buildSuffixArray(extendedSeq,2*len); //create suffix array of the extended sequence;
        int[] invSFA = new int[2*len];                                  //inverse of SFA;
        
        //Inverse of SFA; SFA[i] = j => SFA[j] = i;
        for(int i=0; i<2*len; i++) {
            invSFA[seqSFA[i]] = i;
        }
        
        //Calculate the largest common preffix (LCP) array.
        int[] LCP = Kasai.buildLCP(extendedSeq,seqSFA,2*len,invSFA);
        
        //Find the repeats larger than repeatMinLen.
        ArrayList<FmxrTuple> repeats = Findmaxr.findmaxr(extendedSeq,2*len,seqSFA,LCP,repeatMinLen,invSFA);
        
        HashMap<String,ArrayList<Integer>> fsPositions;             //fusion sites and their positions in the array;
        HashMap<String,HashSet<String>> constraintGraph;            //<s1:s2> is in constraintGraph if s1 and s2 cannot 
                                                                    //be selected as fusion sites at the same time;
        
        
        
        //We will use only the fusion sites in the normal sequence (we don't need the ones in the extended seq).
        fsPositions = FusionSite.generateFusionSites(seq);
        ArrayList<String> aL1 = new ArrayList<>(fsPositions.keySet());
        constraintGraph = FusionSite.generateConstraintGraph(aL1);
        
        //Initialising a FusionSite object.
        FusionSite fs = new FusionSite(seq,seqSFA,repeats,partMinLen,partMaxLen,repeatMinLen,maxPartCommonSeq,fsPositions,constraintGraph);
        LinkedList<FSAndLocation> ans = fs.findFusionSites();

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
            if(ans.isEmpty()) {
                pw.write("\nNo split has been found. You can try again with other parameters. \n");
            }
            else {
                pw.write("\nThe found parts are: \n");
                int prev = 0;
                int i;
                for(i=0; i<ans.size();i++) {
                    pw.write("Part " + (i+1) + ", between indices " + (prev+1) + " and " + (ans.get(i).location+1) + " :\n" + seq.substring(prev,ans.get(i).location)+"\n");
                    prev = ans.get(i).location;
                }
                pw.write("Part " + (i+1) + ", between indices " + (prev+1) + " and " + (seq.length()) + " :\n" + seq.substring(prev,seq.length())+"\n");
            }
            pw.close();
            
        }
        catch(Exception e) {
            System.out.println(e.getMessage());
        }
        
        return ans;
    }
    
    public static void main(String[] args) {
        //If only the sequence was provided, run the program with the default parameters.
        if(args.length == 1) {
            String seq = args[0];
            splitSequence(seq,15,45,200,10).toString();
        }
        //else, run it with the parameters which were provided.
        else {
            String seq = args[0];
            int repeatMinLen = Integer.parseInt(args[1]);
            int partMinLen = Integer.parseInt(args[2]);
            int partMaxLen = Integer.parseInt(args[3]);
            int maxPartCommonSeq = Integer.parseInt(args[4]);
            splitSequence(seq,repeatMinLen,partMinLen,partMaxLen,maxPartCommonSeq).toString();
        }
    }
    
    
    
}
