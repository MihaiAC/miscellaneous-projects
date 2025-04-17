package oligoDesigner;

public class Kasai {
    
    //This method constructs the LCP array using Kasai's algorithm.
    //Reference: "http://www.geeksforgeeks.org/%C2%AD%C2%ADkasais-algorithm-for-construction-of-lcp-array-from-suffix-array/".
    public static int[] buildLCP(String seq, int[] SFA, int len, int[] invSFA) {
        int[] LCP = new int[len];
        
        //Initialize length of previous LCP (largest common prefix).
        int k = 0;
        
        //Explanation: 
        //invSFA[i] = rank of suffix starting at i;
        //lcp[invSFA[i+1]] >= lcp[infSFA[i]]-1 --> this is why k->k-1 for next iteration.
        for(int i=0; i<len; i++) {
            
            if(invSFA[i] == len-1) {
                k = 0;
                continue;
            }
            
            
            int j = SFA[invSFA[i]+1]; //index of i+1 th suffix.
            
            //Count the number of letters the 2 suffixes have in common (at least k-1).
            while(i+k < len && j+k < len && seq.charAt(i+k) == seq.charAt(j+k)) {
                k++;
            }
            
            
            LCP[invSFA[i]] = k;
            
            if(k>0) {
                k--;
            }
        }
        
        return LCP;
    }
}
