package oligoDesigner;

import java.util.ArrayList;
import java.util.Arrays;

public class Findmaxr {
    // An adapted counting sort for our current problem:
    private static void countingSort(int[] LCP, int[] I, int exp) {
        int len = LCP.length, count[] = new int[10], output[] = new int[len];
        
        for(int i=0; i<len; i++) {
            count[(LCP[I[i]]/exp)%10]++;
        }
        
        for(int i=1; i<10; i++) {
            count[i] += count[i-1];
        }
        
        // From n-1 to 0 in order to preserve the order!!!.
        for(int i=len-1; i>=0; i--) {
            output[count[(LCP[I[i]]/exp)%10]-1] = I[i];
            count[(LCP[I[i]]/exp)%10]--;
        }
        
        for(int i=0; i<len; i++) {
            I[i] = output[i];
        }
    }
    
    public static void radixSort(int[] LCP, int[] I, int maxNrDigits) {
        for(int exp=1; maxNrDigits > 0; exp *=10) {
            countingSort(LCP,I,exp);
            maxNrDigits--;
        }
    }
    
    //"findmaxr" algorithm, adapted to Java from: https://arxiv.org/pdf/1304.0528.pdf
    public static ArrayList<FmxrTuple> findmaxr(String s, int len, int[] SFA, int[] LCP, int ml, int[] invSFA) {
        ArrayList<FmxrTuple> tupleArr = new ArrayList<>();
        ArrayList<FmxrNode> aL = FmxrNode.buildTree(len);
        FmxrNode.addElem(aL,0);
        FmxrNode.addElem(aL,len); //corresponds to the +1;
        //0, len+1 = fake indices => +1 when you add an element; -1 when you discard an element;
        for(int i=0; i<len; i++) {
            if(LCP[i] < ml) {
                FmxrNode.addElem(aL,i+1);
            }
        }
        int I[] = new int[len];
        for(int i=0; i<len; i++) {
            I[i] = i;
        }
        int nrOfDigits = 0, aux = len;
        while(aux > 0) {
            aux = aux/10;
            nrOfDigits++;
        }
        radixSort(LCP,I,nrOfDigits);
        int initial = 0;
        //Should check whether ml is in normal parameters.
        while(initial < len && LCP[I[initial]] < ml) {
            initial++;
        }
        //System.out.println("Initial is "+ Arrays.toString(I));
        for(int t=initial; t<len; t++) {
            int i = I[t];
            int pi = FmxrNode.maxLessThan(aL,i+1); //MAY BE +2 or +0!!!
            int ni = FmxrNode.minGreaterThan(aL,i+1)-1;
            FmxrNode.addElem(aL,i+1);
            //recheck this part.
            //auxi = LCP[ni];
            //System.out.println(ni);
            //System.out.println(pi);
            if((pi == 0 || LCP[pi-1] != LCP[i]) && (ni == len-1 || LCP[ni] != LCP[i])) {
                if(SFA[pi] == 0 || SFA[ni] == 0 || (s.charAt(SFA[pi]-1) != s.charAt(SFA[ni]-1)) || (invSFA[SFA[ni]-1] - invSFA[SFA[pi]-1] != ni - pi)) {
                    FmxrTuple FT = new FmxrTuple(pi,ni-pi+1,LCP[i]);
                    tupleArr.add(FT);
                    //System.out.println(ni + "wtf");
                    //System.out.println(pi + "wtf");
                }
            }
        }
        return tupleArr;
    }
    
    public static void main(String[] args) {
        int[] LCP = {3,10,24,50,60,12};
        int[] I = {0,1,2,3,4,5};
        radixSort(LCP,I,3);
        System.out.println(java.util.Arrays.toString(I));
    }
}
