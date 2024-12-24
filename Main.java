import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.io.*;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;
import java.io.FileWriter;
class LZW{
    private String input;
    private int currentPosition;
    private Map<Integer, String> map;
    private Map<Integer, String> map2;
    private Integer startingIndex;
    private Integer inputCount;

    public LZW(String input , Map<Integer, String> map){
        this.input=input;
        this.map=map;
        this.map2=map;
        this.currentPosition=0;
        this.startingIndex=128;
        this.inputCount=0;
    }

    public ArrayList<Integer> compress(){

        ArrayList<Integer> indexTag=new ArrayList<>();
        char next;
        String current="";
        boolean found=false;
        while(currentPosition < input.length()) {
            if(!found){
                current= String.valueOf(input.charAt(currentPosition));
            }

            if(currentPosition+1==input.length()){
                next=' ';
            }else{
                next=input.charAt(currentPosition+1);
            }
           String combined =current + next;

            if(!map.containsValue(combined)){
                indexTag.add(getKeyByValue(map,current));
                map.put(startingIndex, combined);
                startingIndex++;
                found=false;
            }
            else{
                current=current+next;
                found=true;

            }
            currentPosition++;
        }

        return indexTag;
    }

    public String deCompress(ArrayList<Integer> compressedOutput){
        StringBuilder decompressedOutput= new StringBuilder();
        ArrayList<String> charList=new ArrayList<>();
        String old="";
        String current="";
        String combined="";
        for(int element : compressedOutput){
            if(currentPosition==0&&map2.containsKey(element)){
                old= String.valueOf(map.get(element));
                charList.add(String.valueOf(old));
                decompressedOutput.append(map2.get(element));

            }else if(map2.containsKey(element)){
                if(currentPosition>1){
                    old=current;
                }

                current= String.valueOf(map.get(element));
                charList.add(String.valueOf(old));
                if(current.length()>1){
                    charList.add(String.valueOf(current.charAt(0)));
                }else{
                    charList.add(current);
                }

                combined=convertArrayToString(charList);
                if(!map.containsValue(combined)){
                    map.put(startingIndex,combined);
                    startingIndex++;
                }


            }if(!map.containsKey(element)){
                current+=current.charAt(0);
                map.put(startingIndex,current);
                startingIndex++;
            }
            decompressedOutput.append(current);
            currentPosition++;
            charList=new ArrayList<>();

        }

        return decompressedOutput.toString();
    }
    private String convertArrayToString(ArrayList<String> charList) {
        StringBuilder sb = new StringBuilder();
        for (String ch : charList) {
            sb.append(ch);
        }
        return sb.toString();
    }
    public static Integer getKeyByValue(Map<Integer, String> map, String value) {
        for (Map.Entry<Integer, String> entry : map.entrySet()) {
            if (entry.getValue().equals(value)) {
                return entry.getKey();
            }
        }
        return null;
    }

    public static void main(String[] args) throws IOException {
        Map<Integer, String> map = new HashMap<>();
        String alphabet="ABCDEFGHIJKLMNOPQRSTUVWXYZ";

        for(int i=0;i<alphabet.length();i++){
            int toAscii=alphabet.charAt(i);
            map.put(toAscii, String.valueOf(alphabet.charAt(i)));
        }


        String filePath ="input.txt";
        String input="";
        try (Scanner scanner = new Scanner(new File(filePath))) {
            while (scanner.hasNextLine()) {
                input = scanner.nextLine();
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

//        String input="ABAABABBAABAABAAAABABBBBBBBB";
        LZW lzw=new LZW(input,map);
        ArrayList<Integer> compressedOutput=lzw.compress();
        System.out.println("Compressed Output:");
        for (int element : compressedOutput) {
            System.out.print(" < "+element+" > ");
        }

        System.out.println(" ");
        Map<Integer, String> map2 = new HashMap<>();
        for(int i=0;i<alphabet.length();i++){
            int toAscii=alphabet.charAt(i);
            map2.put(toAscii, String.valueOf(alphabet.charAt(i)));
        }
        LZW lzw2=new LZW(input,map2);
        String decompressedOutput=lzw2.deCompress(compressedOutput);
        System.out.println("Decompressed Output:");
        System.out.println(decompressedOutput);

        FileWriter myWriter = new FileWriter("output.txt");
        myWriter.write(decompressedOutput);
        myWriter.close();
        File file = new File("output.txt");
        System.out.println("File created at: " + file.getAbsolutePath());


    }
}

