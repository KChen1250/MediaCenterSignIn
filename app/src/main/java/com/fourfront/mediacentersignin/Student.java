package com.fourfront.mediacentersignin;

import android.os.Environment;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
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
            fullTNs.add(TCHF + " " + TCHL);
        }
        return fullTNs;
    }

    public boolean isStudent() {
        return isStudent;
    }

    public void saveToFile(String sender, String isSubstitute, String reason) {
        String p = path +  "/MediaCenterSignIn/data.csv";

        Timestamp time = new Timestamp(System.currentTimeMillis());

        String content = "\"" + sdf.format(time) + "\",\"" + ID + "\",\"" + FULL_NAME + "\",\"" +
                sender + "\",\"" + isSubstitute + "\",\"" + reason + "\"";

        File f = new File(p);
        if(!f.exists()) {
            try (FileWriter fw = new FileWriter(p, true);
                 BufferedWriter bw = new BufferedWriter(fw);
                 PrintWriter out = new PrintWriter(bw)) {
                out.println("\"Time\",\"ID\",\"Name\",\"Sender\",\"Substitute\",\"Reason\"");
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        try (FileWriter fw = new FileWriter(p, true);
             BufferedWriter bw = new BufferedWriter(fw);
             PrintWriter out = new PrintWriter(bw)) {
            out.println(content);
        } catch (IOException e) {
            e.printStackTrace();
        }

        System.out.println("hi");
    }

    private ArrayList<String[]> getListFromFile(String path) {
        Scanner inFile = null;
        try {
            inFile = new Scanner(new File(path));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        ArrayList<String> lines = new ArrayList<>();

        while(inFile.hasNextLine()){
            String line = inFile.nextLine();
            lines.add(line);
        }
        inFile.close();

        ArrayList<String[]> processed = new ArrayList<>();

        for (int i = 1; i < lines.size(); i ++) {
            String line = lines.get(i);
            processed.add(line.substring(1, line.length() - 1).split("\",\"", -1));
        }

        return processed;
    }

    private ArrayList<String[]> getStudentInfo(String id) {
        ArrayList<String[]> allStudents = getListFromFile(path + "/MediaCenterSignIn/fourfront.mer");
        ArrayList<String[]> thisStudent = new ArrayList<>();
        System.out.println(allStudents.get(3)[6]);

        int loop = 1;
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

        return thisStudent;
    }

    public Student(String id) {
        ID = id;
        ArrayList<String[]> info = getStudentInfo(id);
        for (String[] i : info) {
            for (String j: i) {
                System.out.print(j + " | ");
            }
            System.out.println();
        }

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
