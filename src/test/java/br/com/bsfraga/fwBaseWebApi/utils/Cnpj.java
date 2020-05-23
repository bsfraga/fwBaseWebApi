package br.com.bsfraga.fwBaseWebApi.utils;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Cnpj {

    /**
     * This method get a random valid CNPJ from  CNPJ_Valido.csv file and verify if the value has already been used
     * based on the CNPJ_Usado.csv file.
     * @return A valid CNPJ (String)
     *
     * @author bsfraga
     */
    public static String getOneCNPJ(){
        try {
            BufferedReader csvReader = new BufferedReader(new FileReader("src\\test\\resources\\docs\\CNPJ_Validos.csv"));

            String row = "";
            List<String[]> data = new ArrayList<>();

            while ((row = csvReader.readLine()) != null) {
                data.add(row.split(","));
            }
            System.out.println("Tamanho da lista de CNPJ: "+data.size());

            Random r = new Random();
            int index = r.nextInt(data.size());
            System.out.println("Index do CNPJ que será escolhido: "+index);

            String[] choosenData = data.get(r.nextInt(index));
            System.out.println("CNPJ Escolhido: "+choosenData[0]);

            if(verifyCNPJUsage(choosenData[0])){
                System.out.println("CNPJ sem uso encontrado.");
                FileWriter fileWriter = new FileWriter("src\\test\\resources\\docs\\CNPJ_Usado.csv", true);
                fileWriter.append(String.join(",", choosenData[0]));
                fileWriter.append("\n");
                fileWriter.flush();
                fileWriter.close();
                return choosenData[0];
            }else{
                System.err.println("CNPJ já utilizado, tentando outro...");
                getOneCNPJ();
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * This method verify if a valid CNPJ had already been used by consulting CNPJ_Usado.csv file.
     * @param choosenData (String)
     * @return boolean if the String has been on CNPJ_Usado.csv file;
     */
    public static boolean verifyCNPJUsage(String choosenData){
        try {
            BufferedReader csvReader = new BufferedReader(new FileReader("src\\main\\resources\\docs\\CNPJ_Usado.csv"));
            String row = "";
            while((row = csvReader.readLine()) != null){
                if(row.equals(choosenData)){
                    return false;
                }
            }
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

}
