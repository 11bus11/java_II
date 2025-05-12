package library;

public class Copy {
    private String copyID;
    private String barcode;
    private final Work work;
    private boolean isReference;
    private String copyStatus;
    private String copyPlacement;

    public Copy(String copyID, String barcode, Work work,
                boolean isReference, String copyStatus, String copyPlacement) {
        this.copyID        = copyID;
        this.barcode       = barcode;
        this.work          = work;
        this.isReference   = isReference;
        this.copyStatus    = copyStatus;
        this.copyPlacement = copyPlacement;
    }

    public String getBarcode()   { return barcode; }
public String getCopyStatus(){ return copyStatus; }
public Work   getWork()      { return work; }
}
