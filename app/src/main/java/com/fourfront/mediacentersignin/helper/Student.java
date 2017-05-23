package com.fourfront.mediacentersignin.helper;

import android.os.Environment;
import android.os.SystemClock;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Serializable;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Scanner;

public class Student implements Serializable {

    private boolean isStudent;

    private String ID;                      // 6-digit id
    private String FULL_NAME;               // full name
    private String FIRST;                   // first name
    private String MI;                      // middle initial
    private String LAST;                    // last name

    private ArrayList<String> CRSTITLE;     // course title
    private ArrayList<String> DUR;          // duration (semesters)
    private ArrayList<String> TCHF;         // teacher first
    private ArrayList<String> TCHL;         // teacher last

    private String CNSLRF;                  // counselor first
    private String CNSLRL;                  // counselor last

    private final SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    private String path = Environment.getExternalStorageDirectory() + "/" + Environment.DIRECTORY_DOCUMENTS;

    public String getFullName() {
        return FULL_NAME;
    }

    public String[] getName() {
        return new String[]{FIRST, MI, LAST};
    }

    public String getCounselorName() {
        return CNSLRF + " " + CNSLRL;
    }

    public ArrayList<String> getCourses() {
        return CRSTITLE;
    }

    public ArrayList<String> getDurations() {
        return DUR;
    }

    public ArrayList<String> getTeacherNames() {
        ArrayList<String> fullTNs = new ArrayList<>();
        for (int i = 0; i < TCHF.size(); i ++) {
            fullTNs.add(TCHF.get(i) + " " + TCHL.get(i));
        }
        return fullTNs;
    }

    public boolean isStudent() {
        return isStudent;
    }

    public void saveToFile(String sender, String isSubstitute, String reason) {
        String p = path + "/MediaCenterSignIn/data.csv";

        Timestamp time = new Timestamp(System.currentTimeMillis());

        String content = "\"" + sdf.format(time) + "\",\"" + ID + "\",\"" + FULL_NAME + "\",\"" +
                sender + "\",\"" + isSubstitute + "\",\"" + reason + "\"" + System.lineSeparator();

        File f = new File(p);
        if(!f.exists()) {
            try (FileWriter fw = new FileWriter(p);
                 BufferedWriter bw = new BufferedWriter(fw)) {
                bw.append("\"Time\",\"ID\",\"Name\",\"Sender\",\"Substitute\",\"Reason\"" + System.lineSeparator());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        try (FileWriter fw = new FileWriter(p, true);
             BufferedWriter bw = new BufferedWriter(fw)) {
            bw.append(content);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private ArrayList<String[]> getListFromFile(String path) {
        ArrayList<String> lines = new ArrayList<>();

        Timer.tic();
        System.out.println("starting to parse file. ###########################################################################################################################");

        try (FileReader fr = new FileReader(path);
             BufferedReader br = new BufferedReader(fr)) {
            for (String line = null; (line = br.readLine()) != null;) {
                lines.add(line);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        System.out.println("done parsing file. continuing to split the entire thing. ##########################################################################################");
        System.out.println("Time:" + Timer.toc());
        Timer.tic();

        ArrayList<String[]> processed = new ArrayList<>();

        for (int i = 1; i < lines.size(); i ++) {
            String line = lines.get(i);
            processed.add(line.substring(1, line.length() - 1).split("\",\"", -1));
        }

        System.out.println("done splitting. ###################################################################################################################################");
        System.out.println("Time:" + Timer.toc());

        return processed;
    }

    private ArrayList<String[]> getStudentInfo(String id) {
        ArrayList<String[]> allStudents = getListFromFile(path + "/MediaCenterSignIn/fourfront.mer");
        ArrayList<String[]> thisStudent = new ArrayList<>();

        int loop = 0;
        boolean store = false;
        while (loop < allStudents.size() && (allStudents.get(loop)[6].equals(id) || !store)) {
            if (allStudents.get(loop)[6].equals(id)) {
                store = true;
            }
            if (store) {
                thisStudent.add(allStudents.get(loop));
            }
            loop ++;
        }
        System.out.println(loop);

        return thisStudent;
    }

    public Student(String id) {
        ID = id;
        ArrayList<String[]> info = getStudentInfo(id);

        if (info.isEmpty()) {
            isStudent = false;
        } else {
            isStudent = true;

            String[] firstLine = info.get(0);

            FIRST = firstLine[5];
            MI = firstLine[8];
            LAST = firstLine[7];

            if (MI.isEmpty()) {
                FULL_NAME = FIRST + " " + LAST;
            } else {
                FULL_NAME = FIRST + " " + MI + ". " + LAST;
            }

            CRSTITLE = new ArrayList<>();
            DUR = new ArrayList<>();
            TCHF = new ArrayList<>();
            TCHL = new ArrayList<>();

            for (String[] element: info) {
                CRSTITLE.add(element[3]);
                DUR.add(element[4]);
                TCHF.add(element[9]);
                TCHL.add(element[10]);
            }

            CNSLRF = firstLine[1];
            CNSLRL = firstLine[2];
        }
    }
}

class Timer{
    private static long start_time;

    public static double tic(){
        return start_time = System.nanoTime();
    }

    public static double toc(){
        return (System.nanoTime()-start_time)/1000000000.0;
    }

}