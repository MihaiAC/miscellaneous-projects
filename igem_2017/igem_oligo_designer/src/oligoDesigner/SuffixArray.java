package oligoDesigner;


public final class SuffixArray {
    private SuffixArray() {}
    
    //Construct a suffix array from the "path" sequence.
    //Adapted to Java from: http://algo2.iti.kit.edu/documents/jacm05-revised.pdf.
    public static int[] buildSuffixArray(String path, int len) {
        //For starters, let path be the sequence itself.
        //Only strings containing "ACGT" should be accepted as input.
        //A==1,C==2,G==3,T==4
        int T[] = new int[len+3];
        for(int i=0; i<len; i++) {
            switch(path.charAt(i)) {
            case 'A':
                T[i] = 1;
                break;
            case 'C':
                T[i] = 2;
                break;
            case 'G':
                T[i] = 3;
                break;
            case 'T':
                T[i] = 4;
                break;
            default:
                int[] aux = new int[1];
                return aux;
            }
        }
        
        Wrap w = new Wrap();
        w.T = T;
        w.len = len;
        w.K = 4;
        SuffixArray.buildsfA(w);
        return w.sfA;
    }
    
    public static boolean leqPair(int a1, int a2, int b1, int b2) {
        return (a1 < b1 || (a1 == b1 && a2 <= b2));
    }
    public static boolean leqTriple(int a1, int a2, int a3, int b1, int b2, int b3) {
        return (a1 < b1 || (a1 == b1) && leqPair(a2,a3,b2,b3));
    }
    
    //Sort from[0..n-1] to to[0..n-1] with keys in 0..K from r.
    //from = R, R = T.
    private static void countingSort(int[] from, int[] to, int[] R, int n, int K, int offset) {
        int[] count = new int[K+1];
        for(int i=0; i<n; i++) {
            //count[R[from[i]+offset]]++;
            int aux1 = from[i]+offset;
            int aux2 = R[aux1];
            int aux3 = count[aux2]++;
        } //count occurences.
        
        //Slightly different than the normal counting sort: count[i] will indicate the first position of i, not the last!
        //for(int i=1; i<k; i++) {count[i]+=count[i-1];}
        int aux = 0;
        for(int i=0, sum=0; i<= K; i++) {
            aux = count[i];
            count[i] = sum;
            sum += aux;
        }
        for(int i=0; i<n; i++) {
            to[count[R[from[i]+offset]]++] = from[i];
        }
    }
    
    /*
     * T = current string;
     * sfA = ??;
     * K = current range of characters.
     */
    
    private static void buildsfA(Wrap w) {
        //Define useful lengths for later.
        int len0 = (w.len+2)/3, 
            len1 = (w.len+1)/3,
            len2 = (w.len)/3,
            len02 = len0+len2;
        
        int[] R = new int[len02+3]; //ordered array of positions; character T[R[i]] will have position i.
        R[len02] = 0; R[len02+1] = 0; R[len02+2] = 0;
        int[] sfA12 = new int[len02+3]; //suffix array of positions??
        sfA12[len02] = 0; sfA12[len02+1] = 0; sfA12[len02+2] = 0;
        
        //Same, but for the elements at position%3==0:
        int[] R0 = new int[len0];
        int[] sfA0 = new int[len0];
        
        //Generate positions of mod 2 and mod 1 suffixes.
        //n0-n1 adds a dummy mod 1 suffix if n%3==1.
        // Which means that at most one position in R doesn't exist? (and is considered 0?)
        for(int i=0, j=0; i < w.len + (len0-len1); i++) {
            if(i%3!=0) {
                R[j] = i; j++;
            }
        }
        
        //Step 1: sort sample suffixes.
        countingSort(R,sfA12,w.T,len02,w.K,2);
        countingSort(sfA12,R,w.T,len02,w.K,1);
        countingSort(R,sfA12,w.T,len02,w.K,0);
        
        //Rename the triple-characters with their ranks.
        //If two of those characters are identical, they will be adjacent.
        int name = 0, c0=-1, c1=-1, c2=-1; 
        for(int i=0; i<len02; i++) {
            if(!(w.T[sfA12[i]]==c0 && w.T[sfA12[i]+1]==c1 && w.T[sfA12[i]+2]==c2)) {
                name++;
                c0 = w.T[sfA12[i]]; c1 = w.T[sfA12[i]+1]; c2 = w.T[sfA12[i]+2];
            }
            //Construct R'=R1*R2.
            if(sfA12[i] % 3 == 1) {R[sfA12[i]/3] = name;} //write to R1
            else                  {R[sfA12[i]/3 + len0] = name;} //write to R2
        }
        
        //At this point, R = R' = contains the ranking of the 3-chars: first half of the mod 1 chars;
                                                                     //second half of the mod 2 chars;
        
        //               sfA12 = contains the ordered positions...?;
        
        //recurse if names are not yet unique.
        if(name < len02) {
            Wrap newWrap = new Wrap(R,sfA12,len02,name);
            buildsfA(newWrap);
            R = newWrap.T;
            sfA12 = newWrap.sfA;
            //sfA12 now contains the order of the suffixes in R;
            //sfA12[i] = the position in R of ith rank suffix.
            //R[sfA12[i]] = no meaning?;
            //store unique names in R using the suffix array;
            for(int i=0; i<len02; i++) {R[sfA12[i]] = i+1;}
        }
        else { //generate suffix array of R directly.
            for(int i=0; i<len02; i++) {sfA12[R[i]-1] = i;}
        }
        //Now R gains relevancy again and contains the positions of the suffixes of the 3-char rankings :).
        //

        //Step 2: sort nonsample suffixes.
        //sort the mod 0 suffixes from sfA12 by their first character.
        //They are already sorted by their second "character" - this why only one countingSort is enough;
        for(int i=0, j=0; i<len02; i++) {if(sfA12[i] < len0) {R0[j++] = 3*sfA12[i];}}
        countingSort(R0,sfA0,w.T,len0,w.K,0);
        
        //Step 3: merge.
        /*
         * Contents of each array:
         * - T = current "string";
         * - R = position/ranking of 12 suffixes-ISH;
         */
        w.sfA = new int[w.len];
        for(int p=0, t=len0-len1, k=0; k<w.len; k++) {
            int i = getI(sfA12,t,len0); //pos of current offset 12 suffix
            int j = sfA0[p];            //pos of current offset 0 suffix
            if(sfA12[t] < len0 ? leqPair(w.T[i],R[sfA12[t] + len0],w.T[j],R[j/3])
                               : leqTriple(w.T[i],w.T[i+1],R[sfA12[t]-len0+1],w.T[j],w.T[j+1],R[j/3+len0])) {
                w.sfA[k] = i; t++;
                if(t == len02) { //only SA0 suffixes left;
                    for(k++; p < len0; p++, k++) {
                        w.sfA[k] = sfA0[p];
                    }
                }
            }
            else { //suffix from SA0 is smaller
                w.sfA[k] = j; p++;
                if(p == len0) { //only SA12 suffixes left
                    for(k++; t < len02; t++,k++) {
                        w.sfA[k] = getI(sfA12,t,len0);
                    }
                }
            }
        }
    }
    
    //Position of current offset 12 suffix.
    public static int getI(int[] sfA12, int t, int len0) {
        return (sfA12[t] < len0) ? sfA12[t] * 3 + 1 : (sfA12[t] - len0) * 3 + 2;
    }
}
