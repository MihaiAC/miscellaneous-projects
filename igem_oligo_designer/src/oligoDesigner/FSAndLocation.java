package oligoDesigner;

// Auxiliary data structure used by FusionSite.
public class FSAndLocation {
    String FS;           //a selected fusion site;
    Integer location;    //the location of the selected fusion site;
    
    public FSAndLocation(String fs, int location) {
        this.FS = fs;
        this.location = location;
    }
    
    @Override
    public String toString() {
        return "("+FS+","+location.toString()+")";
    }
}
