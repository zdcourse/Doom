package word_kmeans;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.HashMap;
import java.util.Map;

public class Utils {

    public Map<TwoTuple<String, String>, Double> word_vec = new HashMap<TwoTuple<String, String>, Double>();

    public Utils(){
        File file = new File("src/word_kmeans/word_vec_file");
        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = br.readLine()) != null){
                String[] s = line.split(" ");
                TwoTuple<String, String> temp = new TwoTuple<String, String>(s[0], s[1]);
                word_vec.put(temp, Double.parseDouble(s[2]));
            }
        } catch (Exception e) {
            System.err.println(e.getMessage());
        }
    }

    public double getDistance(Instance a, Instance b) {
        String word_a;
        String word_b;
        if (a instanceof WordInst && b instanceof  WordInst) {
            word_a = ((WordInst)a).word;
            word_b = ((WordInst)b).word;
            if(word_a == word_b) {
                return 1;
            }

        } else
            return -1;

        TwoTuple<String, String> temp = new TwoTuple<String, String>(word_a, word_b);
        //System.out.println(temp);
        try {
            Double dis = word_vec.get(temp);

            if (dis == null) {
                temp = new TwoTuple<String, String>(word_b, word_a);
                dis = word_vec.get(temp);
            }
            return dis;
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        return 1;
    }



    public static void main(String[] args) {
        String a = "合肥";
        String b = "安徽";

        String c = "安徽";
        String d = "合肥";

        Utils utils = new Utils();
        //Double dis1 = utils.getDistance(a, b);
        //Double dis2 = utils.getDistance(c, d);
        //System.out.printf("dis is %f\n", dis1);
        //System.out.printf("dis is %f\n", dis2);
    }
}
