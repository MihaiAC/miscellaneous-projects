package oligoDesigner;

//This Wrap object will be used to as an auxiliary structure by the sufix array constructor.
public class Wrap {
    public int[] T;
    public int[] sfA;
    public int len;
    public int K;
    public Wrap(int[] T, int[] sfA, int len, int K) {
        this.T = T;
        this.sfA = sfA;
        this.len = len;
        this.K = K;
    }
    public Wrap() {
        this.T = new int[0];
        this.sfA = new int[0];
        this.len = 0;
        this.K = 0;
    }
}
