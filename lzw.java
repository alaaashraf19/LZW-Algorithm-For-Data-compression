import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

class LZW{
    private String input;
    private int currentPosition;
//    private ArrayList<Integer> dictionary;
    private Map<Integer, String> map;

    public LZW(String input , Map<Integer, String> map){
        this.input=input;
        this.currentPosition=0;
        this.map=map;
    }

    public ArrayList<Integer> compress(){
        ArrayList<Integer> indexTag=new ArrayList<>();
        ArrayList<Character> charList = new ArrayList<>();
        int startingIndex=128;

        while(currentPosition < input.length()){

            char currentPosChar=input.charAt(currentPosition);
            charList.add(currentPosChar);
            String charListString=convertArrayToString(charList);
            int currentCharAsciiCode=currentPosChar;

            if(map.containsKey(currentCharAsciiCode)){

                if(charList.size()>=2 && map.containsValue(charListString)){
                    int keyofCharListString=getKeyByValue(map, charListString);
                    indexTag.add(keyofCharListString);
                }
                else{
                    indexTag.add(currentCharAsciiCode);

                }
//                indexTag.add(currentCharAsciiCode);
                if(!map.containsValue(charListString)){
                    map.put(startingIndex, charListString);
                    startingIndex++;
                }
                currentPosition++;
            }
            if(currentPosition==1){
                continue;
            }
            charList.removeFirst();
        }
         return indexTag;
    }

    public static Integer getKeyByValue(Map<Integer, String> map, String value) {
        for (Map.Entry<Integer, String> entry : map.entrySet()) {
            if (entry.getValue().equals(value)) {
                return entry.getKey();
            }
        }
        return null; // Return null if the value is not found
    }

    private String convertArrayToString(ArrayList<Character> charList) {
        StringBuilder sb = new StringBuilder();
        for (Character ch : charList) {
            sb.append(ch);
        }
        return sb.toString();
    }

    public static void main(String[] args) {
        Map<Integer, String> map = new HashMap<>();
        String alphabet="ABCDEFGHIJKLMNOPQRSTUVWXYZ";

        for(int i=0;i<alphabet.length();i++){
            int toAscii=alphabet.charAt(i);
            map.put(toAscii, String.valueOf(alphabet.charAt(i)));
        }

        for (Map.Entry<Integer, String> me : map.entrySet()) {
            System.out.print(me.getKey() + ":");
            System.out.println(me.getValue());
        }

        String input="ABAABABBAABAAABAAABBBBBBBBBB";
        LZW lzw=new LZW(input,map);
        ArrayList<Integer> compressedOutput=lzw.compress();
        System.out.println("Compressed Output:");
        for (int element : compressedOutput) {
            System.out.println(element);
        }
    }
}
