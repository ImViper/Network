
package chapter01;

import javafx.stage.FileChooser;

import java.io.*;
import java.util.Scanner;

public class TextFileIO {
    private PrintWriter pw = null;
    private Scanner sc = null;
    public  TextFileIO(){

    }
//    public  void append(String msg) {
//        FileChooser fileChooser = new FileChooser();
//        File file = fileChooser.showSaveDialog(null);
//        if (file == null)
//            return;
//        try {
//            pw = new PrintWriter(
//                    new OutputStreamWriter(
//                            new FileOutputStream(file, true), "utf-8"));
//            pw.println(msg);
//        } catch (Exception e) {
//            e.printStackTrace();
//        } finally {
//            pw.close();
//        }
//    }


//    public String load(){
//            FileChooser fileChooser = new FileChooser();
//            File file = fileChooser.showSaveDialog(null);
//            if(file == null)
//                return null;
//            StringBuilder sb = new StringBuilder();
//            try {
//                sc = new Scanner(file,"utf-8");
//                while(sc.hasNext()){
//                    sb.append(sc.nextLine()+"\n");
//
//                }
//            } catch (FileNotFoundException e) {
//                e.printStackTrace();
//            }finally {
//                sc.close();
//            }
//            return sb.toString();
//
//    }

    // 改写之后，不需要GUI界面的IO

    public  void append(File file,String msg) {

        if (file == null)
            return;
        try {
            pw = new PrintWriter(
                    new OutputStreamWriter(
                            new FileOutputStream(file, true), "utf-8"));
            pw.println(msg);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            pw.close();
        }
    }
    public String load(File file){
        if(file == null)
            return null;
        StringBuilder sb = new StringBuilder();
        try {
            sc = new Scanner(file,"utf-8");
            while(sc.hasNext()){
                sb.append(sc.nextLine()+"\n");

            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }finally {
            sc.close();
        }
        return sb.toString();

    }
    }

