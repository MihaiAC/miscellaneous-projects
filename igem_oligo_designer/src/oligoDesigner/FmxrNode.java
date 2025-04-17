package oligoDesigner;
import java.util.ArrayList;

// Auxiliary structure used by Findmaxr.java.
public class FmxrNode {
    public FmxrNode leftNode = null;
    public FmxrNode rightNode = null;
    
    public FmxrNode leftChild;
    public FmxrNode rightChild;
    public FmxrNode parent;
    public int leaf; //-1 if not a leaf; 0..n+1 if it's a leaf;
    
    /*
     *  value = 0 if current node is internal and both its children are 0 or 
     *               current node is a leaf and the element it represents is not currently in the set S;
     *  value = 1 if current node is internal and at least one of its children is 1 or
     *               current node is a leaf and the element it represents is currently in the set S;
     */
    public int value;
    
    public FmxrNode(int leafValue) {
        this.leaf = leafValue;
        this.leftChild = null;
        this.rightChild = null;
        this.parent = null;
        this.value = 0;
    }
    
    public FmxrNode(FmxrNode leftChild, FmxrNode rightChild) {
        this.leftChild = leftChild;
        this.rightChild = rightChild;
        this.parent = null;
        this.leaf = -1;
        this.value = 0;
    }
    
    
    //This method should be rewritten (uses more space than necessary).
    //Also, there doesn't seem to be a logical reason for n+2 again n.
    public static ArrayList<FmxrNode> buildTree(int n) {
        ArrayList<FmxrNode> aL = new ArrayList<>();
        ArrayList<FmxrNode> aux1 = new ArrayList<>();
        ArrayList<FmxrNode> aux2 = new ArrayList<>();
        for(int i=0; i<n+1; i++) {
            FmxrNode node = new FmxrNode(i);
            aL.add(node);
            aux1.add(node);
        }
        while(aux1.size() != 1) {
            int auxLen = aux1.size();
            //improve after, just want to make it work
            for(int i=0; i<auxLen-1; i++) {
                aux1.get(i).rightNode = aux1.get(i+1);
                aux1.get(i+1).leftNode = aux1.get(i);
                
            }
            for(int i=0; i+1<auxLen; i+=2) {
                FmxrNode node = new FmxrNode(aux1.get(i),aux1.get(i+1));
                aux1.get(i).parent = node;
                aux1.get(i+1).parent = node;
                aux2.add(node);
                
            }
            if((auxLen-1)%2 == 0) {
                aux2.add(aux1.get(auxLen-1));
            }
            aux1.clear();
            aux1.addAll(aux2);
            aux2.clear();
        }
        
        return aL;
    }
    
    public static void addElem(ArrayList<FmxrNode> aL, int elem) {
        FmxrNode node = aL.get(elem);
        node.value = 1;
        node = node.parent;
        while(node != null) {
            if(node.value == 1) {break;}
            else {
                node.value = 1;
                node = node.parent;
            }
        }
    }
    
    public static int minGreaterThan(ArrayList<FmxrNode> aL, int T) {
        FmxrNode node = aL.get(T);
        do {
            if(node == node.parent.rightChild) {
                node = node.rightNode;
            }
            else {
                node = node.parent;
            }
        } while(node.value != 1);
        while(node.leaf < 0) {
            if(node.rightChild.value == 1) {
                node = node.rightChild;
            }
            else {
                node = node.leftChild;
            }
        }
        return node.leaf;
    }
    
    public static int maxLessThan(ArrayList<FmxrNode> aL, int T) {
        FmxrNode node = aL.get(T);
        do {
            //Potential problem: leftmost element of the tree.
            //For now, left unchecked.
            if(node == node.parent.leftChild) {
                node = node.leftNode;
            }
            else {
                node = node.parent;
            }
        } while(node.value != 1);
        while(node.leaf < 0) {
            if(node.rightChild.value == 1) {
                node = node.rightChild;
            }
            else {
                node = node.leftChild;
            }
        }
        return node.leaf;
    }
    
    public static void main(String[] args) {
        ArrayList<FmxrNode> aL = new ArrayList<>();
        aL = FmxrNode.buildTree(10);
        System.out.println(aL.get(1).parent.parent.rightChild.leftChild.leaf);
        System.out.println(aL.get(1) == aL.get(1).parent.rightChild);
        System.out.println(aL.get(1) == aL.get(1).parent.leftChild);
    }
}
