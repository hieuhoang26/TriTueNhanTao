package org.example;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import javafx.util.Pair;
import java.util.*;

public class runMain {
    static Stack<MenhDe> THOA = new Stack<>();
    static List<MenhDe> listV;
    static List<MenhDe> listRes;
    static Map<MenhDe, Pair<MenhDe, MenhDe>> map = new HashMap<>();
    static boolean flag = false;
    public static List<MenhDe> readFromFile(String filename) {
        List<MenhDe> list = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader(filename))) {
            String line;
            while ((line = br.readLine()) != null) {
                list.add(new MenhDe(line));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return list;
    }

    public static int findIndexMD(MenhDe u, BieuThuc v){
        for (int i = 0; i < u.getMenhDe().size(); i++) {
            if(u.getMenhDe().get(i).getBt().equals(v.getBt()) && u.getMenhDe().get(i).isPhu()!= v.isPhu() )
                return i;
        }
        return -1;
    }
    public static MenhDe delete(MenhDe u, MenhDe v){
        flag = false;
        for (int i = 0; i< v.getMenhDe().size(); i++){
            BieuThuc b = v.getMenhDe().get(i);
            int index = findIndexMD(u,b);
            if( index > -1){
                flag = true;
                u.deleteBT(index);
            }
            else {
                u.getMenhDe().add(b);
            }

        }
        return u;
    }
    public static boolean res(MenhDe u, MenhDe v) {
        Pair<MenhDe, MenhDe> p = new Pair<>(new MenhDe(u), v);
        MenhDe kq = delete(u, v);
        if (kq.getMenhDe().size() == 0) {
            map.put(new MenhDe(), p);
            listV.add(v);
            flag = true; // Đánh dấu là hệ quả của logic
            return true;
        }
        if (flag && map.get(kq) == null) {
            map.put(kq, p);
            THOA.add(kq);
            listV.add(v);
            listRes.add(kq);
        }
        return false;
    }

    public static void main(String[] args) {

        String inputFile = "D:\\OnClass\\TTNT\\Code\\Test\\src\\main\\java\\org\\example\\input.txt";
        String outputFile = "D:\\OnClass\\TTNT\\Code\\Test\\src\\main\\java\\org\\example\\output.txt";

        List<MenhDe> listMD = readFromFile(inputFile);

        BieuThuc MDCM = new BieuThuc("P", false);

        THOA.add(new MenhDe(new BieuThuc(MDCM.getBt(), !MDCM.isPhu())));

        try (FileWriter writer = new FileWriter(outputFile)) {
            writer.write(String.format("%-15s%-30s%-40s%-50s\n", "u |", "v |", "res(u,v) |", "THOA |"));
            writer.write(String.format("%-15s%-30s%-40s%-50s\n", "|", "|", "|", new BieuThuc(MDCM.getBt(), !MDCM.isPhu()) + " |"));

            while (!THOA.isEmpty()) {
                MenhDe u = THOA.pop();
                listV = new ArrayList<>();
                listRes = new ArrayList<>();
                flag = false;

                String uStr = String.format("%-15s", u + "|");
                writer.write(uStr);

                for (MenhDe md : listMD) {
                    if (res(new MenhDe(u), md)) {
                        String outputLine = String.format("%-30s%-40s%-50s\n", printV() + "|", printRes() + "|", printTHOA() + "|");
                        System.out.print(uStr + outputLine);
                        writer.write(outputLine);
                        System.out.println("Mệnh đề " + MDCM + " là hệ quả của logic");
                        show(u);
                        show(u, writer);
                        return;
                    }
                }
                String outputLine = String.format("%-30s%-40s%-50s\n", printV() + "|", printRes() + "|", printTHOA() + "|");
                System.out.print(uStr + outputLine);
                writer.write(outputLine);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void show(MenhDe u, FileWriter writer) throws IOException {
        writer.write("(" + map.get(new MenhDe()).getKey() + ", " + map.get(new MenhDe()).getValue() + ")");
        u = map.get(new MenhDe()).getKey();
        while (map.get(u) != null) {
            writer.write(" <- ");
            writer.write("(" + map.get(u).getKey() + ", " + map.get(u).getValue() + ")");
            u = map.get(u).getKey();
        }
        writer.write("\n");
    }


    private static void show(MenhDe u) {
        System.out.print("("+ map.get(new MenhDe()).getKey() +", " + map.get(new MenhDe()).getValue() + ")");
        u = map.get(new MenhDe()).getKey();
        while (map.get(u) != null){
            System.out.print(" <- ");
            System.out.print("("+ map.get(u).getKey() +", " + map.get(u).getValue() + ")");
            u = map.get(u).getKey();
        }
        System.out.println();
    }

    private static String printV() {
        String s = "";
        for (MenhDe md : listV){
            s += md + ", ";
        }
        return s;
    }
    private static String printRes() {
        String s = "";
        for (MenhDe md : listRes){
            s += md + ", ";
        }
        return s;
    }
    private static String printTHOA(){
        Stack<MenhDe> stack = new Stack<>();
        int i = 0;
        String res = "";
       while (i < THOA.size()){
           i++;
           MenhDe bt = THOA.peek();
           stack.add(THOA.pop());
           res += bt + ", ";
       }
        THOA = stack;
       return res;
    }
}
