package oligoDesigner;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.lang.Math;

public class FusionSite {
    
    public long startTime;
    public long maxTime = 180000L; //maximum execution time (roughly): 3 minutes.
    public long endTime;
    public String seq;
    public int[] seqSFA;
    public ArrayList<FmxrTuple> fmxr;
    public ArrayList<RepeatPair> repeats;
    public int partMinLen;
    public int partMaxLen;
    public int repeatMinLen;
    public int maxPartCommonSeq;
    public HashMap<String,HashSet<String>> constraintGraph;
    public HashMap<String,ArrayList<Integer>> fsPositions;
    
    public ArrayList<String> fsPositionsKeySet;
    public LinkedList<String> currentFS;                        //currently selected fusion sites (no positions)
    public LinkedList<FSAndLocation> possSOL;                   //currently selected fusion sites (with their positions)
    
    public FusionSite(String seq, int[] seqSFA, ArrayList<FmxrTuple> fmxr, int partMinLen, int partMaxLen, int repeatMinLen,
                      int maxPartCommonSeq, HashMap<String,ArrayList<Integer>> fsPositions, 
                      HashMap<String,HashSet<String>> constraintGraph) {
        this.seq = seq; this.seqSFA = seqSFA; this.fmxr = fmxr; this.partMinLen = partMinLen; this.repeatMinLen = repeatMinLen;
        this.partMaxLen = partMaxLen; this.maxPartCommonSeq = maxPartCommonSeq; this.fsPositions = fsPositions;
        this.constraintGraph = constraintGraph;
        fsPositionsKeySet = new ArrayList<String>(fsPositions.keySet());
        currentFS = new LinkedList<String>();
        possSOL = new LinkedList<FSAndLocation>();
    }
    
    public boolean testRepeatNormalPair(int fst, int snd, int length) {
        
        for(int i=0; i<possSOL.size(); i++) {
            int currFSIndex = possSOL.get(i).location;
            //Site is of no importance to pair:
            if(currFSIndex <= fst || currFSIndex >= snd + length - 1) {
                continue;
            }
            
            //We know that fst < currFSIndex < snd+length-1           
             
            //Site satisfies pair:
            if((currFSIndex >= fst + length - maxPartCommonSeq - 1) && (currFSIndex <= snd + maxPartCommonSeq - 1)) {
                return true;
            }
            
            //We know that currFSindex is inside one of the two repeats.
            
            //If this part has been reached, then the current repeat must be split.
            if(currFSIndex <= fst + length - 1) {
                return testRepeatNormalPair(currFSIndex,snd + fst + length - currFSIndex - 1, fst + length - currFSIndex);
            }
            
            if(currFSIndex >= snd) {
                return testRepeatNormalPair(fst,snd,currFSIndex-snd+1);      
            }
        }
        
        return false;
    }
    
    public boolean testRepeatType1(int fst, int snd, int length) {
        
        for(int i=0; i<possSOL.size(); i++) {
            int currFSIndex = possSOL.get(i).location;
            
            //Site is of no importance to pair:
            if(currFSIndex <= fst || currFSIndex >= snd + length - 1) {
                continue;
            }
            
            //Site satisfies pair:
            if((currFSIndex >= fst + length - maxPartCommonSeq - 1) && (currFSIndex <= snd + maxPartCommonSeq - 1)) {
                return true;
            }
            
            if(currFSIndex <= fst + length - 1) {
                return testRepeatType1(currFSIndex,snd,fst+length-currFSIndex);
            }
            
            if(currFSIndex >= snd) {
                return testRepeatType1(fst+length-(currFSIndex-snd+1),snd,currFSIndex-snd+1);      
            }
        }
        return false;
        
    }
    
    public boolean testRepeatType2(int fst, int snd, int length) {
        for(int i=0; i<possSOL.size(); i++) {
            int currFSIndex = possSOL.get(i).location;
            
            //Site is of no importance to pair:
            if(currFSIndex <= fst || currFSIndex >= snd + length - 1) {
                continue;
            }
            
            //Site satisfies pair:
            if((currFSIndex >= fst + length - maxPartCommonSeq - 1) && (currFSIndex <= snd + maxPartCommonSeq - 1)) {
                return true;
            }
            
            if(currFSIndex <= fst + length - 1) {
                return testRepeatType2(currFSIndex,snd,fst+length-currFSIndex);
            }
            
            if(currFSIndex >= snd) {
                return testRepeatType2(fst+length-(currFSIndex-snd+1),snd,currFSIndex-snd+1);      
            }
            
        }
        return false;
        
    }
    
    //Inefficient, should change RepeatPair again.
    public boolean testRepeatType3(int fst, int length) {
        for(int i=0; i<possSOL.size(); i++) {
            int currFSIndex = possSOL.get(i).location;
            
            //Site is of no importance to pair:
            if(currFSIndex <= fst || currFSIndex >= fst + length - 1) {
                continue;
            }
            
            //Site satisfies pair:
            if(length < 2 * repeatMinLen) {
                return true;
            }
            
            if(currFSIndex >= fst) {
                return testRepeatType3(currFSIndex,length-2*(currFSIndex-fst));
            }
        }
        return false;        
        
    }
    
    public boolean testPossSOL() {

        //This will loop indefinitely if there is no solution (check if size changed INSTEAD)
        //+ check if the loop must go another time too.
        labelPossSOL:
        for(int ii = 0; ii < repeats.size(); ii++) {
            
            RepeatPair currRP = repeats.get(ii);
            switch(currRP.type) {
            case 0: {
                if(testRepeatNormalPair(currRP.fst,currRP.snd,currRP.length)) {
                    continue labelPossSOL;
                }
                else {
                    return false;
                }
            }
            case 1: {
                if(testRepeatType1(currRP.fst,currRP.snd,currRP.length)) {
                    continue labelPossSOL;
                }
                else {
                    return false;
                }
            }
            case 2: {
                if(testRepeatType2(currRP.fst,currRP.snd,currRP.length)) {
                    continue labelPossSOL;
                }
                else {
                    return false;
                }
            }
            case 3: {
                if(testRepeatType3(currRP.fst,currRP.length)) {
                    continue labelPossSOL;
                }
                else {
                    return false;
                }
            }
            default: return false;
            }
        }
        
        return true;
    }
    
    //Checks if any selected fusion site is in the constraint graph of another selected fusion site.
    public boolean checkIfValidCombinationFS() {
        for(int i=0; i<currentFS.size()-1; i++) {
            for(int j=i+1; j<currentFS.size(); j++) {
                //Check if i is in the constraint graph (if not, then it works).
                if(constraintGraph.containsKey(currentFS.get(i)) && constraintGraph.containsKey(currentFS.get(j))) {
                    if((constraintGraph.get(currentFS.get(i))).contains(currentFS.get(j))) {
                        return false;
                    }
                }
                else {
                    return true;
                }

            }
        }
        return true;
    }

    
    public boolean checkAllPositionsCurrentFS(int select) {
        if(select < currentFS.size()) {
            ArrayList<Integer> auxcurrFS = fsPositions.get(currentFS.get(select));
            for(int i=0; i<auxcurrFS.size(); i++) {
                FSAndLocation auxFSA = new FSAndLocation(currentFS.get(select),auxcurrFS.get(i));
                possSOL.add(auxFSA);
                if(!checkAllPositionsCurrentFS(select+1)) {
                    possSOL.removeLast();
                }
                else {
                    return true;
                }
            }
            return false;
        }
        else {
            if(testDistanceFS() && testPossSOL()) {
                return true;
            }
            return false;
        }
    }
    
    //ASSUMPTION: A new part starts at the beginning of a fusion site and ends at the beginning of the next one.
    public boolean testDistanceFS() {
        int len = possSOL.size();
        for(int i=0;i<len-1;i++) {
            for(int j=i+1;j<len;j++) {
                int fs1 = possSOL.get(i).location, fs2 = possSOL.get(j).location;
                if((fs2 > fs1 && (fs2-fs1 < partMinLen-4 || fs2-fs1 > partMaxLen-4)) || ((fs1 >= fs2) && (fs2-fs1 < partMinLen-4 || fs2-fs1 > partMaxLen-4))) {
                    return false;
                }
            }
        }
        return true;
    }
   
    //Some sort of dynamic approach?
    //For each combination of strings, check all possible combinations of positions (of those strings). (is ok because
    //a fusion site cannot be selected 2 times in the same try).
    public boolean generateAndCheckFS(int i) {
        if(i>0) {
            for(int j=0; j<fsPositionsKeySet.size(); j++) {
                currentFS.add(fsPositionsKeySet.get(j));
                if(generateAndCheckFS(i-1)) {
                    return true;
                }
                currentFS.removeLast(); //a stack would be lighter.
            }
            return false;
        }
        else {
            
            if(System.currentTimeMillis() > endTime) {
                PrintIfTimeExceeded.printS(seq,fmxr,seqSFA);
                System.out.println("Execution time limit exceeded.");
                System.exit(1);
            }
            
            if(!checkIfValidCombinationFS()) {
                return false;
            }
            if(checkAllPositionsCurrentFS(0)) {
                return true;
            }
            return false;
        }
    }
    
    //Main function to find FusionSites.
    public LinkedList<FSAndLocation> findFusionSites() {
        //Obtain the repeats.
        this.repeats = RepeatPair.FmxrTupleListTORepeatPairList(seq.length(),fmxr,repeatMinLen,seqSFA);

        startTime = System.currentTimeMillis();
        endTime  = startTime + maxTime;
        
        int seqLen = seq.length();
        int maxFusionSites = (seqLen-partMinLen*2)/partMinLen; //if both the front and the end count as fusion sites; //int maxFusionSites = (length-40)/40; //otherwise - (only front counts as fusion site);
        
        for(int i=1; i<=maxFusionSites; i++) {
            if(generateAndCheckFS(i)) {
                return possSOL;
            }
        }
        LinkedList<FSAndLocation> LLaux = new LinkedList<>();
        return LLaux;
    }
    
    //Helper function for FusionSitesToInts.
    public static int letterToInt(char c) {
        switch(c) {
        case 'A':
            return 1;
        case 'C':
            return 2;
        case 'G':
            return 3;
        case 'T':
            return 4;
        default:
            return -1;
        }
    }
    
    //Helper function for FusionSitesToInts.
    public static int inverseLetterToInt(char c) {
        switch(c) {
        case 'A':
            return 4;
        case 'C':
            return 3;
        case 'G':
            return 2;
        case 'T':
            return 1;
        default:
            return -1;
        }
    }
    
    //Converts a list of 4-letter sections into a list of integers.
    public static int[] FusionSitesToInts(String[] p) {
        int len = p.length, aux1, aux2;
        int[] ints = new int[2*len];
        for(int i=0; i<len; i++) {
            aux1 = letterToInt(p[i].charAt(0))*1000 + 
                   letterToInt(p[i].charAt(1))*100  + 
                   letterToInt(p[i].charAt(2))*10   + 
                   letterToInt(p[i].charAt(3));
            aux2 = inverseLetterToInt(p[i].charAt(3))*1000 + 
                   inverseLetterToInt(p[i].charAt(2))*100  + 
                   inverseLetterToInt(p[i].charAt(1))*10   + 
                   inverseLetterToInt(p[i].charAt(0));
            ints[2*i] = aux1;
            ints[2*i+1] = aux2;
        }
        return ints;
    }
    
    //Returns the indices of the possible fusion sites of String s.
    public static HashMap<String,ArrayList<Integer>> generateFusionSites(String s) {
        //As there are at most 256 possible fusion sites and this is done only once,
        //we can generate them inefficiently.
        HashSet<String> hs = new HashSet<String>();
        HashMap<String,ArrayList<Integer>> hm = new HashMap<>();
        String fusion1FOR = s.substring(0,4), 
               fusion2 = s.substring(s.length()-4,s.length());
        String[] strArr = {fusion1FOR,fusion2};
        
        int[] aux = FusionSitesToInts(strArr);

        String[] stringify = {"A","C","G","T"};
        for(int i=0; i<4; i++) {
            for(int j=0; j<4; j++) {
                for(int k=0; k<4; k++) {
                    outerloop:
                    for(int l=0; l<4; l++) {
                        if((i==0 || j==0 || k==0 || l==0 || i==3 || j==3 || k==3 || l==3) &&
                           (i==1 || j==1 || k==1 || l==1 || i==2 || j==2 || k==2 || l==2) &&
                          !(i==3-l && j==3-k)){
                            //Condition for seeing which fusion sites work with the first and the last ones, which are fixed. DELETE after.
                            //GAGG = 2022 - inverse: 1131
                            //TTGA = 3320 - inverse: 3100
                            int sum;
                            for(int ii=0; ii<4; ii++) {
                                sum = 0;
                                if(i==aux[ii]/1000) {
                                    sum++;
                                }
                                if(j==(aux[ii]/100)%10) {
                                    sum++;
                                }
                                if(k==(aux[ii]/10)%10) {
                                    sum++;
                                }
                                if(l==aux[ii]%10) {
                                    sum++;
                                }
                                
                                if(sum > 2) {
                                    continue outerloop;
                                }
                            }
                            hs.add(stringify[i]+stringify[j]+stringify[k]+stringify[l]);
                        }
                    }
                }
            }
        }
        String saux;
        ArrayList<Integer> aLL = new ArrayList<>();
        for(int i=0; i<s.length()-4; i++) {
            saux = s.substring(i,i+4);
            //Check if the substring is "palindromic" with its complement.
            if(saux.equals(reverseComplement(saux))) {
                continue;
            }
            if(hs.contains(saux)) {
                if(hm.containsKey(saux)) {
                    hm.get(saux).add(i);
                }
                else {
                    aLL = new ArrayList<>();
                    aLL.add(i);
                    hm.put(saux,aLL);
                }
            }
        }
        return hm;
    }
    
    
    //Returns the complement of a char.
    public static char complement(char c) {
        switch(c) {
        case('A'): return 'T';
        case('C'): return 'G';
        case('T'): return 'A';
        case('G'): return 'C';
        default: return ' ';
        }
    }
    
    public static String reverseComplement(String s) {
        char[] revCompl = new char[s.length()];
        for(int i=s.length()-1; i>=0; i--) {
            revCompl[s.length()-1-i] = complement(s.charAt(i));
        }
        String revComp = new String(revCompl);
        return revComp;
    }
    
    public static String simpleReverse(String s) {
        int slen = s.length();
        char[] rev = new char[slen];
        for(int i=slen-1; i>=0; i--) {
            rev[slen-1-i] = s.charAt(i);
        }
        return new String(rev);
    }
    
    //Helper function for generateConstraintGraph.
    public static boolean isCombinationAllowed(String s1, String s2) {
        int sumFOR=0, sumREV=0;
        if(s1.equals(simpleReverse(s2))) {
            return false;
        }
        if(s1.equals(simpleReverse(reverseComplement(s2)))) {
            return false;
        }
        for(int i=0; i<4; i++) {
            if(s1.charAt(i)==s2.charAt(i)) {
                sumFOR++;
            }
            if(s1.charAt(i)==complement(s2.charAt(3-i))) {
                sumREV++;
            }
        }
        if(sumFOR > 2 || sumREV > 2) {
            return false;
        }
        return true;
    }
    
    
    //For a fusion site => all fusion sites it cannot be selected with.
    public static HashMap<String,HashSet<String>> generateConstraintGraph(ArrayList<String> aL) {
        int len = aL.size();
        HashMap<String,HashSet<String>> hm = new HashMap<>();
        HashSet<String> aux3;
        String aux1,aux2;
        
        for(int i=0;i<len;i++) {
            aux1 = aL.get(i);
            if(!hm.containsKey(aux1)) {
                aux3 = new HashSet<String>();
                aux3.add(aux1);
                hm.put(aux1,aux3);
            }
        }
        
        for(int i=0; i<len-1; i++) {
            for(int j=i+1; j<len; j++) {
                aux1 = aL.get(i);
                aux2 = aL.get(j);
                if(!isCombinationAllowed(aux1,aux2)) {
                    hm.get(aux1).add(aux2);
                    hm.get(aux2).add(aux1);
                }
            }
        }
        return hm;
    }
}
