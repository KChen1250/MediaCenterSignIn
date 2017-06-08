package com.fourfront.mediacentersignin.helper;

import android.os.Environment;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Serializable;
import java.io.StringWriter;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;

/**
 * Helper class containing information for a student
 *
 * @author Kevin Chen
 *
 * Created May 2017, finished June 2017
 * Poolesville High School Client Project
 */
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

    // list of positions of each variable in the database
    private int CNSLRF_pos;
    private int CNSLRL_pos;
    private int CRSTITLE_pos;
    private int DUR_pos;
    private int ID_pos;
    private int FIRST_pos;
    private int MI_pos;
    private int LAST_pos;
    private int TCHF_pos;
    private int TCHL_pos;

    private final SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    private String path = Environment.getExternalStorageDirectory() + "/" + Environment.DIRECTORY_DOCUMENTS;

    public String getFullName() {
        return FULL_NAME;
    }

    public String getCounselorName() {
        return CNSLRL + ", " + CNSLRF;
    }

    public ArrayList<String> getCourses() {
        return CRSTITLE;
    }

    public ArrayList<String> getDurations() {
        return DUR;
    }

    public ArrayList<String> getTeacherNames() {
        // add last names and first names
        ArrayList<String> fullTNs = new ArrayList<>();
        for (int i = 0; i < TCHF.size(); i ++) {
            fullTNs.add(TCHL.get(i) + ", " + TCHF.get(i));
        }
        return fullTNs;
    }

    public boolean isStudent() {
        return isStudent;
    }

    /**
     * Log a student entry
     *
     * @param sender        teacher that sent student
     * @param isSubstitute  if there was / wasn't a substitute
     * @param reason        reason
     */
    public void saveToFile(String sender, String isSubstitute, String reason) {
        String p = path + "/MediaCenterSignIn/data.csv";

        Timestamp time = new Timestamp(System.currentTimeMillis());

        String content = "\"" + sdf.format(time) + "\",\"" + ID + "\",\"" + FULL_NAME + "\",\"" +
                sender + "\",\"" + isSubstitute + "\",\"" + reason + "\"" + System.lineSeparator();

        // create new file with header if it does not exist
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

        SendData.postData(ID, FULL_NAME, sender, isSubstitute, reason);
    }

    /**
     * Get the entire student database
     *
     * @param path  path of database
     * @return      array of unprocessed data
     */
    private ArrayList<String> getListFromFile(String path) {
        // read all lines from database
        StringWriter sb = new StringWriter();
        char[] buffer = new char[1024*4];
        int n;
        try (FileReader fr = new FileReader(path)) {
            while (-1 != (n = fr.read(buffer))) {
                sb.write(buffer, 0, n);
            }
            fr.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        ArrayList<String> lines = new ArrayList<>(Arrays.asList(sb.toString().split("\n")));

        // read and remove the header
        String[] header = lines.remove(0).replace("\r", "").split(",");
        CNSLRF_pos = Arrays.asList(header).indexOf("CNSLRF");
        CNSLRL_pos = Arrays.asList(header).indexOf("CNSLRL");
        CRSTITLE_pos = Arrays.asList(header).indexOf("CRSTITLE");
        DUR_pos = Arrays.asList(header).indexOf("DUR");
        ID_pos = Arrays.asList(header).indexOf("ID");
        FIRST_pos = Arrays.asList(header).indexOf("FIRST");
        MI_pos = Arrays.asList(header).indexOf("MI");
        LAST_pos = Arrays.asList(header).indexOf("LAST");
        TCHF_pos = Arrays.asList(header).indexOf("TCHF");
        TCHL_pos = Arrays.asList(header).indexOf("TCHL");

        return lines;
    }

    /**
     * Do a binary search on the database of students
     *
     * @param allStudents   database of all students
     * @return              index of specific student
     */
    private int getStudentIndex(ArrayList<String> allStudents) {
        int lo = 0;
        int hi = allStudents.size() - 1;
        while (lo <= hi) {
            int mid = lo + (hi - lo) / 2;
            if (ID.compareTo(allStudents.get(mid).split("\",\"", -1)[ID_pos]) < 0) {
                hi = mid - 1;
            } else if (ID.compareTo(allStudents.get(mid).split("\",\"", -1)[ID_pos]) > 0) {
                lo = mid + 1;
            } else {
                return mid;
            }
        }

        return -1;
    }

    /**
     * Get all the information for one student
     *
     * @return  list of info
     */
    private ArrayList<String[]> getStudentInfo() {
        ArrayList<String> allStudents = getListFromFile(path + "/MediaCenterSignIn/fourfront.mer");
        ArrayList<String[]> thisStudent = new ArrayList<>();
        int index = getStudentIndex(allStudents);

        if (index == -1) {
            return thisStudent;
        }

        // search backwards
        int i = index - 1;
        while (i >= 0 && ID.equals(allStudents.get(i).split("\",\"", -1)[ID_pos])) {
            thisStudent.add(0, allStudents.get(i).substring(1, allStudents.get(i).length() - 2).split("\",\"", -1));
            i--;
        }
        // search forwards
        while (index < allStudents.size() && ID.equals(allStudents.get(index).split("\",\"", -1)[ID_pos])) {
            thisStudent.add(allStudents.get(index).substring(1, allStudents.get(index).length() - 2).split("\",\"", -1));
            index++;
        }

        return thisStudent;
    }

    public Student(String id) {
        ID = id;
        ArrayList<String[]> info = getStudentInfo();

        if (info.isEmpty()) {
            isStudent = false;
        } else {
            isStudent = true;

            String[] firstLine = info.get(0);

            FIRST = firstLine[FIRST_pos];
            MI = firstLine[MI_pos];
            LAST = firstLine[LAST_pos];

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
                CRSTITLE.add(element[CRSTITLE_pos]);
                DUR.add(element[DUR_pos]);
                TCHF.add(element[TCHF_pos]);
                TCHL.add(element[TCHL_pos]);
            }

            CNSLRF = firstLine[CNSLRF_pos];
            CNSLRL = firstLine[CNSLRL_pos];
        }
    }
}