package word_kmeans;

import org.apache.commons.lang.ObjectUtils;

import java.util.HashSet;
import java.util.Set;

public class WordInst implements Instance {
	public String word = null;
	public Utils utils = new Utils();

	public WordInst(){};

	public WordInst(String word) {
		this.word = word;
	}

	@Override
	public double distance(Instance inst) {
		if(inst instanceof WordInst) {
			return utils.getDistance(this, ((WordInst) inst));
		}
		return -1;
	}

	@Override
	public Instance centroid(Instance... insts) {
		Set set = new HashSet();
		for (Instance inst:insts)
			set.add(inst);
		set.add(this);
		return this.centroid(set);
	}


	@Override
	public Instance centroid(Set insts) {

		double sumSimilarity = 0;
        double maxSimilarity = 0;
        String word = null;
        for (Object inst:insts) {
        	sumSimilarity = 0;
            if (inst instanceof WordInst) {
                WordInst winst = (WordInst)inst;
                for (Object yinst:insts) {
                    sumSimilarity += utils.getDistance(winst, ((WordInst)yinst));
                }
                if (maxSimilarity < sumSimilarity) {
                    maxSimilarity = sumSimilarity;
                    word = winst.word;
                }
            }
        }
        return new WordInst(word);
	}

	public String toString(){
		return String.format("Inst[word=%s;]", this.word);
	}

	public static void main(String args[]){
		//Instance Inst1 = new SimpleInst(1, 2);
		//Instance Inst2 = new SimpleInst(4, 6);
		//Instance Inst3 = new SimpleInst(4, 4);
		//System.out.printf("\t[Info] Distance between inst1 & inst2 = %g!\n", Inst1.distance(Inst2));
		//System.out.printf("\t[Info] Centroid of inst1, inst2, inst3=%s\n", Inst1.centroid(Inst2, Inst3));
	}

	@Override
	public boolean isSamePoint(Instance inst) {
		if(inst instanceof WordInst) {
			WordInst wInst = (WordInst)inst;
			if (this.word == wInst.word)
				return true;
		}
		return false;
	}
}
