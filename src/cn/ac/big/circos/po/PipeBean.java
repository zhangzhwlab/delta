package cn.ac.big.circos.po;

public class PipeBean {

	private String organism;
	private String cellline;
	private String binsize;
	private String startPosition; // the start position of matrix
	private String enzyme;
	private String chrom;
	private int chromStart;
	private int chromEnd;
	private String readslen;
	
	private String useTest;
	
	private String hicMatrixFile; // hic matrix file
	private String matrixFile; // loop file
	private String featureFile; // model file
	private String tadFile;
	
	private String maxbin; //s
	private String gamma;//gamma
	private String tadnumber; //N
	private String pval; // -p
	private String qval;//-q
	
	private String jobid;
	
	private String numparticle;//-K
	private String numenrich;//-MP
	private String sampleiter;//-NG
	private String interval;//-NT
	private String stepsize;//-L
	
	private String maxObserv;
	private String minObserv;
	private String startbin;
	private String endbin;
	
	private String physicalModel;
	private String mogenAdjacentDist;
	private String mogenContactDist;
	private String mogenPosMinDist;
	private String mogenNegMaxDist;
	private String mogenPosMaxDistWeight;
	private String mogenPosMinDistWeight;
	
	private String mogenNegMinDistWeight;
	private String mogenNegMaxDistWeight;
	private String mogenLearnRate;
	private String mogenMaxIterator;

	private String fastpval;// peak probablity
	
	private String email; // email address
	
	
	
	
	public String getHicMatrixFile() {
		return hicMatrixFile;
	}

	public void setHicMatrixFile(String hicMatrixFile) {
		this.hicMatrixFile = hicMatrixFile;
	}

	public String getFastpval() {
		return fastpval;
	}

	public void setFastpval(String fastpval) {
		this.fastpval = fastpval;
	}

	public String getReadslen() {
		return readslen;
	}

	public void setReadslen(String readslen) {
		this.readslen = readslen;
	}

	public String getEnzyme() {
		return enzyme;
	}

	public void setEnzyme(String enzyme) {
		this.enzyme = enzyme;
	}

	public String getPhysicalModel() {
		return physicalModel;
	}

	public void setPhysicalModel(String physicalModel) {
		this.physicalModel = physicalModel;
	}

	public String getMogenAdjacentDist() {
		return mogenAdjacentDist;
	}

	public void setMogenAdjacentDist(String mogenAdjacentDist) {
		this.mogenAdjacentDist = mogenAdjacentDist;
	}

	public String getMogenContactDist() {
		return mogenContactDist;
	}

	public void setMogenContactDist(String mogenContactDist) {
		this.mogenContactDist = mogenContactDist;
	}

	public String getMogenPosMinDist() {
		return mogenPosMinDist;
	}

	public void setMogenPosMinDist(String mogenPosMinDist) {
		this.mogenPosMinDist = mogenPosMinDist;
	}

	public String getMogenNegMaxDist() {
		return mogenNegMaxDist;
	}

	public void setMogenNegMaxDist(String mogenNegMaxDist) {
		this.mogenNegMaxDist = mogenNegMaxDist;
	}

	public String getMogenPosMaxDistWeight() {
		return mogenPosMaxDistWeight;
	}

	public void setMogenPosMaxDistWeight(String mogenPosMaxDistWeight) {
		this.mogenPosMaxDistWeight = mogenPosMaxDistWeight;
	}

	public String getMogenPosMinDistWeight() {
		return mogenPosMinDistWeight;
	}

	public void setMogenPosMinDistWeight(String mogenPosMinDistWeight) {
		this.mogenPosMinDistWeight = mogenPosMinDistWeight;
	}

	public String getMogenNegMinDistWeight() {
		return mogenNegMinDistWeight;
	}

	public void setMogenNegMinDistWeight(String mogenNegMinDistWeight) {
		this.mogenNegMinDistWeight = mogenNegMinDistWeight;
	}

	public String getMogenNegMaxDistWeight() {
		return mogenNegMaxDistWeight;
	}

	public void setMogenNegMaxDistWeight(String mogenNegMaxDistWeight) {
		this.mogenNegMaxDistWeight = mogenNegMaxDistWeight;
	}

	public String getMogenLearnRate() {
		return mogenLearnRate;
	}

	public void setMogenLearnRate(String mogenLearnRate) {
		this.mogenLearnRate = mogenLearnRate;
	}

	public String getMogenMaxIterator() {
		return mogenMaxIterator;
	}

	public void setMogenMaxIterator(String mogenMaxIterator) {
		this.mogenMaxIterator = mogenMaxIterator;
	}

	public String getTadFile() {
		return tadFile;
	}

	public void setTadFile(String tadFile) {
		this.tadFile = tadFile;
	}

	public String getStartPosition() {
		return startPosition;
	}

	public void setStartPosition(String startPosition) {
		this.startPosition = startPosition;
	}

	public String getPval() {
		return pval;
	}

	public void setPval(String pval) {
		this.pval = pval;
	}

	public String getQval() {
		return qval;
	}

	public void setQval(String qval) {
		this.qval = qval;
	}

	public int getChromStart() {
		return chromStart;
	}

	public void setChromStart(int chromStart) {
		this.chromStart = chromStart;
	}

	public int getChromEnd() {
		return chromEnd;
	}

	public void setChromEnd(int chromEnd) {
		this.chromEnd = chromEnd;
	}

	public String getUseTest() {
		return useTest;
	}

	public void setUseTest(String useTest) {
		this.useTest = useTest;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getStartbin() {
		return startbin;
	}

	public void setStartbin(String startbin) {
		this.startbin = startbin;
	}

	public String getEndbin() {
		return endbin;
	}

	public void setEndbin(String endbin) {
		this.endbin = endbin;
	}

	public String getMaxObserv() {
		return maxObserv;
	}

	public void setMaxObserv(String maxObserv) {
		this.maxObserv = maxObserv;
	}

	public String getMinObserv() {
		return minObserv;
	}

	public void setMinObserv(String minObserv) {
		this.minObserv = minObserv;
	}

	public String getOrganism() {
		return organism;
	}

	public void setOrganism(String organism) {
		this.organism = organism;
	}

	public String getCellline() {
		return cellline;
	}

	public void setCellline(String cellline) {
		this.cellline = cellline;
	}

	public String getBinsize() {
		return binsize;
	}

	public void setBinsize(String binsize) {
		this.binsize = binsize;
	}

	public String getMaxbin() {
		return maxbin;
	}

	public void setMaxbin(String maxbin) {
		this.maxbin = maxbin;
	}

	
	
	public String getChrom() {
		return chrom;
	}

	public void setChrom(String chrom) {
		this.chrom = chrom;
	}

	public String getGamma() {
		return gamma;
	}

	public void setGamma(String gamma) {
		this.gamma = gamma;
	}

	public String getTadnumber() {
		return tadnumber;
	}

	public void setTadnumber(String tadnumber) {
		this.tadnumber = tadnumber;
	}

	public String getJobid() {
		return jobid;
	}

	public void setJobid(String jobid) {
		this.jobid = jobid;
	}

	public String getNumparticle() {
		return numparticle;
	}

	public void setNumparticle(String numparticle) {
		this.numparticle = numparticle;
	}

	public String getNumenrich() {
		return numenrich;
	}

	public void setNumenrich(String numenrich) {
		this.numenrich = numenrich;
	}

	public String getSampleiter() {
		return sampleiter;
	}

	public void setSampleiter(String sampleiter) {
		this.sampleiter = sampleiter;
	}

	public String getInterval() {
		return interval;
	}

	public void setInterval(String interval) {
		this.interval = interval;
	}

	public String getStepsize() {
		return stepsize;
	}

	public void setStepsize(String stepsize) {
		this.stepsize = stepsize;
	}

	public String getMatrixFile() {
		return matrixFile;
	}

	public void setMatrixFile(String matrixFile) {
		this.matrixFile = matrixFile;
	}

	public String getFeatureFile() {
		return featureFile;
	}

	public void setFeatureFile(String featureFile) {
		this.featureFile = featureFile;
	}
	
	
}
