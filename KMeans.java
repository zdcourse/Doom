package word_kmeans;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.*;

public class KMeans {
	public int k = 2;
	public List<Cluster> clusters = null;

	public KMeans(int k, Instance...insts){
		if (k != insts.length){
			System.out.printf("\t[Error] K(%s) doesn't match input instances(%d)!\n", k, insts.length);
			return;
		}
		this.k = k;
		this.clusters = new LinkedList();
		for(int i = 0; i < this.k; i ++){
			this.clusters.add(new Cluster(insts[i]));
		}
	}

	public void processUntilCentrStable(int max_loop, Set<Instance> instGrp){
		int loop = 0;
		while(true){
			loop ++;
			for(Instance inst: instGrp){
				int cp = 0;
				double dist = -1;
				for (int j = 0; j < this.k; j ++){
					Cluster c = this.clusters.get(j);

                    for(int i=0;i<this.clusters.size(); i ++) {
                        Instance ins = this.clusters.get(i).centr;
                        if (inst instanceof WordInst && ins instanceof WordInst) {
                            if (((WordInst) inst).word ==  ((WordInst) ins).word){
                                continue;
                            }
                        }
                    }
					double tdist = c.dist(inst);
					if(dist == -1) {
						dist = c.dist(inst);
						cp = j;
					} else if (tdist > dist) {
						cp = j;
						dist = tdist;
					}
				}

				this.clusters.get(cp).addInst(inst);
			}

			boolean isStable = true;
			int cnt = 0;
			for(int i=0;i < this.k; i ++){
				Cluster c = clusters.get(i);
				System.out.printf("\t[Info] Centr=%s:\n", c.centr);
				for(Instance inst:c.groups)
					System.out.printf("\t\t%s\n", inst);
				if(!c.recalcCentr(false)){
					isStable = false;
					cnt = i;
					break;
				}
			}

			if (isStable)
				break;
			else {
				for(int i=0; i < this.k; i ++) {
					if(i<=cnt) clusters.get(i).resetGroup();
					else {
						Cluster c = clusters.get(i);
						System.out.printf("\t[Info] Centr=%s :\n", c.centr);
						for(Instance inst:c.groups) System.out.printf("\t\t%s\n", inst);
						c.recalcCentr(true);
					}
				}
			}
			System.out.printf("===========================================================\n");
			if(loop==max_loop) break;
		}
	}

	public class Cluster {
		public Instance centr = null;
		public Set<Instance> groups = null;
		public Cluster(Instance initCentroid){
			this.centr = initCentroid;
			groups = new HashSet();
		}


		public double dist(Instance inst) {
			return this.centr.distance(inst);
		}

		public void resetGroup(){
			groups.clear();
		}

		public boolean recalcCentr(boolean isReset) {
			Instance uCentr = this.centr.centroid(groups);
			boolean rst = true;
			if (uCentr instanceof WordInst) {
				if(((WordInst)uCentr).word != null) {
					rst = uCentr.isSamePoint(this.centr);
					this.centr = uCentr;
				}
			}

			if(isReset)
				groups.clear();
			return rst;
		}

		public void addInst(Instance inst) {
			groups.add(inst);
		}
	}

	public static void main(String[] args) {
        File filename = new File("src/word_kmeans/word_file");
		List<String> wordArray = new ArrayList<String>();
        try (BufferedReader br = new BufferedReader(new FileReader(filename))) {
            String line;
            while ((line = br.readLine()) != null){
                String[] lineArray = line.split(" ");
				for(String str:lineArray) {
					wordArray.add(str);
				}
            }
        } catch (Exception e) {
            System.err.println(e.getMessage());
        }

		int rnd1 = new Random().nextInt(wordArray.size());
        int rnd2 = new Random().nextInt(wordArray.size());
        int rnd3 = new Random().nextInt(wordArray.size());
		int rnd4 = new Random().nextInt(wordArray.size());
		int rnd5 = new Random().nextInt(wordArray.size());
		int rnd6 = new Random().nextInt(wordArray.size());

		Instance c1 = new WordInst("工作");
		Instance c2 = new WordInst("技术");
		Instance c3 = new WordInst("范文");
		Instance c4 = new WordInst(wordArray.get(rnd4));
		Instance c5 = new WordInst(wordArray.get(rnd5));
		Instance c6 = new WordInst(wordArray.get(rnd6));


		Set InstGrp = new HashSet();
		for(String str:wordArray) {
            System.out.printf(" %s", str);
            System.out.println();
		    if(str != null) {
                InstGrp.add(new WordInst(str));
            }

		}

		KMeans km = new KMeans(3, c1, c2, c3);
		km.processUntilCentrStable(10, InstGrp);
	}
}
