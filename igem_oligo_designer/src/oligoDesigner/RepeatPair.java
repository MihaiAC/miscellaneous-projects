package oligoDesigner;

import java.util.ArrayList;

public class RepeatPair {
    public Integer fst;    //first position of the repeat
    public Integer snd;    //second position of the repeat
    public Integer length; //length of the repeat
    public Integer type;   
    /*
     * type := 0, if both repeats are forward facing.
     * type := 1, if forward facing + backward facing distinct, fst < snd.
     * type := 2, if forward facing + backward facing distinct, fst > snd.
     * type := 3, if forward facing + backward facing, fst = snd.
     */
    
    public RepeatPair(int fst, int snd, int length, int type) {
        this.fst = fst;
        this.snd = snd;
        this.length = length;
        this.type=type;
    }
    
    
    //Convention: for mixed repeats, firstIndex is forward, secondIndex is backward (firstIndex may be bigger than second).

    public static int deduceType(int fst, int snd, int len) {
        /*
         * type := 0, if both repeats are forward facing.
         * type := 1, if forward facing + backward facing distinct, fst < snd.
         * type := 2, if forward facing + backward facing distinct, fst > snd.
         * type := 3, if forward facing + backward facing non-distinct, fst < snd (head)
         * type := 4, if forward facing + backward facing non-distinct, fst > snd (tail)
         */
        if(fst+len-1 <= snd) {
            return 1;
        }
        
        if(snd+len-1 <= fst) {
            return 2;
        }
        
        if(fst <= snd && snd < fst+len-1) {
            return 3;
        }
        
        if(snd <= fst && fst < snd+len-1) {
            return 4;
        }
        
        //Shouldn't be reached.
        return -1;
    }
    
    public static ArrayList<RepeatPair> constructPairHelper(int fst, int snd, int len, int repeatMinLen) {
        int type = deduceType(fst,snd,len);
        ArrayList<RepeatPair> rp = new ArrayList<>();
        
        if(type == 1) {
            rp.add(new RepeatPair(fst,snd,len,type));
            return rp;
        }
        
        if(type == 2) {
            rp.add(new RepeatPair(fst,snd,len,type));
            return rp;
        }
        
        if(type == 3) {
            if(snd-fst >= repeatMinLen) {
                rp.add(new RepeatPair(fst,fst+len-1,snd-fst,1));
            }
            if(fst+len-snd >= repeatMinLen) {
                rp.add(new RepeatPair(snd,snd,fst+len-snd,3));
            }
            return rp;
        }
        
        if(type == 4) {
            if(fst-snd >= repeatMinLen) {
                rp.add(new RepeatPair(snd,snd+len-1,fst-snd,2));
            }
            if(snd+len-fst >= repeatMinLen) {
                rp.add(new RepeatPair(fst,fst,snd+len-fst,3));
            }
            return rp;
        }
        
        return rp;
        
    }

    //What do you do with repeats higher than partMaxLen? - they're handled in FusionSite.java.
    //The name of the function 
    public static ArrayList<RepeatPair> standardizeRepeatPair(int seqLen, int repeatMinLen, int firstIndex, int secondIndex,
                                                         int repeatLen) {
        
        ArrayList<RepeatPair> rp = new ArrayList<>();
        int fst,snd,len;
        
        //If both repeats are in the backward direction, discard them (they are accounted for by their 
        //forward complements).
        if(firstIndex >= seqLen && secondIndex >= seqLen) {
            return rp;
        }
        
        //If both repeats are forward-facing:
        if(firstIndex <= seqLen - repeatLen && secondIndex <= seqLen - repeatLen) {
            rp.add(new RepeatPair(firstIndex,secondIndex,repeatLen,0));
            return rp;
        }
        
        //2 cases of overlap here: the "tips" and the "backs" -> will be treated when fusion sites are checked.
        //If one repeat is in the forward direction and the other is in the backward direction:
        
        //If no repeat intersects with the boundary:
        if(firstIndex <= seqLen-repeatLen && secondIndex >= seqLen) {
            //Forward repeat is from indices[j] to indices[j]+aux.length-1.
            //Backward repeat is from i to i+repeatLen-1, which translates to:
            //(2*seqLen-1)-i-repeatLen+1 to (2*seqLen-1)-i
            int backwardSecondIndex = 2*seqLen-1-secondIndex - repeatLen + 1;
            fst = firstIndex;
            snd = backwardSecondIndex;
            len = repeatLen;
            return constructPairHelper(fst,snd,len,repeatMinLen);
            
        }
        
        //First repeat intersects with boundary:
        if(firstIndex < seqLen && firstIndex + repeatLen - 1 >= seqLen) {
            int secondPartLen = firstIndex + repeatLen - seqLen;
            if(secondPartLen >= repeatMinLen) {
                fst = (2*seqLen-1-secondPartLen+1-(secondIndex+repeatLen-secondPartLen));
                snd = (seqLen-secondPartLen);
                len = secondPartLen;
                rp.addAll(constructPairHelper(fst,snd,len,repeatMinLen));
            }
            int firstPartLen = seqLen-firstIndex;
            if(firstPartLen >= repeatMinLen) {
                fst = firstIndex;
                snd = (2*seqLen-1-firstPartLen+1-secondIndex);
                len = firstPartLen;
                rp.addAll(constructPairHelper(fst,snd,len,repeatMinLen));
            }
            return rp;
        }
        
        //Second repeat intersects with boundary:
        if(secondIndex <= seqLen-1 && secondIndex + repeatLen - 1 >= seqLen) {
            int firstPartLen = seqLen - secondIndex;
            if(firstPartLen >= repeatMinLen) {
                rp.addAll(constructPairHelper(firstIndex,secondIndex,firstPartLen,repeatMinLen));
            }
            int secondPartLen = secondIndex + repeatLen - seqLen;
            if(secondPartLen >= repeatMinLen) {
                fst = firstIndex+secondPartLen;
                snd = (2*seqLen-1)-secondPartLen+1-seqLen;
                len = secondPartLen;
                rp.addAll(constructPairHelper(fst,snd,len,repeatMinLen));
            }
            return rp;
        }
        
        //Should never be reached.
        return rp;
        
    }
    public static ArrayList<RepeatPair> FmxrTupleListTORepeatPairList(int len, ArrayList<FmxrTuple> fmxr, int repeatMinLen, 
                                                                      int[] seqSFA) {
        ArrayList<RepeatPair> repeats = new ArrayList<>();
        for(int i=0; i<fmxr.size(); i++) {
            FmxrTuple aux = fmxr.get(i);
            
            int[] indices = new int[aux.nrOfOccurences];
            
            //Save the indices of the repeats in the original string.
            for(int j=0; j<aux.nrOfOccurences; j++) {
                indices[j] = seqSFA[aux.posInSFA+j];
            }
            
            //indices[i] < indices[j], for any i<j (due to how the SFA was constructed) -> WRONG ASSUMPTION.
            java.util.Arrays.sort(indices);
            
            
            //Analyze every pair of repeats.
            for(int j=0; j<aux.nrOfOccurences-1; j++) {
                for(int k=j+1; k<aux.nrOfOccurences; k++) {
                    //If the repeats are distinct:
                    if(indices[j]+aux.length-1 <= indices[k]) {
                        repeats.addAll(standardizeRepeatPair(len,repeatMinLen,indices[j],indices[k],aux.length));
                    }   
                    //otherwise (the 2 repeats overlap):
                    else {
                        //Get the overlap length:
                        int commonLen = indices[j] + aux.length - indices[k]; //-1 + 1 - cancelled out
                        //Add 2 repeats if the lengths are right.
                        if(aux.length-commonLen >= repeatMinLen) {
                            repeats.addAll(standardizeRepeatPair(len,repeatMinLen,indices[j],indices[k],aux.length-commonLen));
                            repeats.addAll(standardizeRepeatPair(len,repeatMinLen,indices[k]+2*commonLen-aux.length-1,indices[k]+commonLen,aux.length-commonLen));
                        }
                    }
                }
            }
        }
        return repeats;
    }
    
    @Override
    public String toString() {
        return "("+fst.toString()+","+snd.toString()+","+length.toString()+","+type.toString()+")";
    }
}
