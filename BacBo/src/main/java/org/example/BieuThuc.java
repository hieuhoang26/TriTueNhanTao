package org.example;

public class BieuThuc {
    private String bt;
    private boolean isPhu;

    public BieuThuc() {
    }

    public BieuThuc(String bt, boolean isPhu) {
        this.bt = bt;
        this.isPhu = isPhu;
    }

    public String getBt() {
        return bt;
    }

    public void setBt(String bt) {
        this.bt = bt;
    }

    public boolean isPhu() {
        return isPhu;
    }

    public void setPhu(boolean phu) {
        isPhu = phu;
    }

    @Override
    public String toString() {
        if(isPhu){
            return "!"+ bt;
        }
        return bt;
    }
    public BieuThuc btNguoc(){
        return new BieuThuc(bt, !isPhu);
    }
}
