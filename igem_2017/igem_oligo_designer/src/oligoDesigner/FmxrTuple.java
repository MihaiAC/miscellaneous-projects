package oligoDesigner;

// Structure for memorizing the repeats found by our algorithm.
public class FmxrTuple {
    
    public Integer posInSFA;           //position of the repeat in the Suffix Array;
    public Integer nrOfOccurences;     //the number of times this repeat occurs in our sequence;
    public Integer length;             //the length of the repeat;
    
    public FmxrTuple(int p, int n, int l) {
        this.posInSFA = p;
        this.nrOfOccurences = n;
        this.length = l;
    }
    
    @Override
    public String toString() {
        return "("+posInSFA.toString()+","+nrOfOccurences.toString()+","+length.toString()+")";
    }
}
