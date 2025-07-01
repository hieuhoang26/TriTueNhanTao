package org.example;


import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class MenhDe {
    private List<BieuThuc> menhDe = new ArrayList<>();

    public MenhDe() {
    }
    public MenhDe(MenhDe menhDes){
        for (int i = 0; i< menhDes.getMenhDe().size(); i++){
            menhDe.add(menhDes.getMenhDe().get(i));

        }
    }
    public MenhDe(BieuThuc bt) {
        menhDe.add(bt);
    }
    public MenhDe(List<BieuThuc> menhDe) {
        this.menhDe = menhDe;
    }
    public MenhDe(String s) {
        String[] list = s.split(" v ");
        for (int i = 0; i< list.length; i++){
            String c = list[i].substring(0, 1);
            if(c.equals("!")) {
                menhDe.add(new BieuThuc(list[i].substring(1, list[i].length()), true ));
            } else{
                menhDe.add(new BieuThuc(list[i], false));
            }
        }
    }


    public List<BieuThuc> getMenhDe() {
        return menhDe;
    }

    public void setMenhDe(List<BieuThuc> menhDe) {
        this.menhDe = menhDe;
    }

    @Override
    public String toString() {
        String s = "";
        for(int i = 0; i < menhDe.size()-1; i++){
            s += menhDe.get(i).toString() + " v ";
        }
        if(menhDe.size() > 0){
            s += menhDe.get(menhDe.size()-1);
        }

        return s;
    }
    public void deleteBT(int index){
        menhDe.remove(index);

    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        MenhDe menhDe1 = (MenhDe) o;
        return Objects.equals(menhDe, menhDe1.menhDe);
    }

    @Override
    public int hashCode() {
        return Objects.hash(menhDe);
    }
}
