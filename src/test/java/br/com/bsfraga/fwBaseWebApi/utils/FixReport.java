package br.com.bsfraga.fwBaseWebApi.utils;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.List;

/**
 * This class contains the method to remove the external link to style file
 * that cannot be accessed by the internal network.
 */
public class FixReport {

    /**
     * This method changes the external link to style files from the report file to local files.
     */
    public static void fixReport(){
        try {

            BufferedReader bufferedReader = new BufferedReader(new FileReader("reports\\AuttarReport.html"));

            String line = "";
            String textToFind_css = "<link href='https://cdn.rawgit.com/extent-framework/extent-github-cdn/b65cd69/v3html/css/extent.css' type='text/css' rel='stylesheet' />";
            String textToFind_js = "<script src='https://cdn.rawgit.com/extent-framework/extent-github-cdn/b65cd69/v3html/js/extent.js' type='text/javascript'></script>";
            String textToChange_css = "<link href='extent.css' type='text/css' rel='stylesheet' />";
            String textToChange_js = "<script src='extent.js' type='text/javascript'></script>";

            List<String> content = new ArrayList<>();


            while((line = bufferedReader.readLine()) != null){
                if(line.contains(textToFind_css)){
                    line = textToChange_css;
                    content.add(line);
                    continue;
                }else if(line.contains(textToFind_js)){
                    line = textToChange_js;
                    content.add(line);
                    continue;
                }
                content.add(line);
            }

            BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter("reports\\AuttarReport.html"));
            for (String attr : content) {
                bufferedWriter.write(attr);
                bufferedWriter.flush();
            }
            bufferedReader.close();
            bufferedWriter.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
