
public class TestData{
	private TestDatum testData;
	private TestDatum testKey;
	private boolean dataSet;
	private static TestData dataPackage = null;	

	protected TestData(){
		testData = null;
		testKey = null;
	}

	public static TestData getInstance(){
		if(dataPackage == null){
			dataPackage = new TestData();
		}

		return dataPackage;
	}
	
	public void setTestData(String testData, long creationTime, int recordNumber){
		this.testData = new TestDatum();
		this.testData.setTestString(testData);
		this.testData.setCreationTime(creationTime);
		this.testData.setRecordNumber(recordNumber);
	}

	public void setTestKey(String testKey, long creationTime, int recordNumber){
		this.testKey = new TestDatum();
		this.testKey.setTestString(testKey);
		this.testKey.setCreationTime(creationTime);
		this.testKey.setRecordNumber(recordNumber);
	}

	public String getDataString(){
		return this.testData.getTestString();
	}

	public long getDataTime(){
		return this.testData.getCreationTime();
	}

	public int getDataRecNo(){
		return this.testData.getRecordNumber();
	}

	public String getKeyString(){
		return this.testKey.getTestString();
	}

	public long getKeyTime(){
		return this.testKey.getCreationTime();
	}	
	
	public int getKeyRecNo(){
		return this.testKey.getRecordNumber();
	}

	public final boolean initialized(){
		return (this.testData != null) && (this.testKey != null);
	}


	public class TestDatum{
		private String testString;
		private long creationTime;
		private int recordNumber;

		public TestDatum(){
			this.testString = new String();
		}
		//setters
		public void setTestString(String testString){
			this.testString = testString;
		}

		public void setCreationTime(long creationTime){
			this.creationTime = creationTime;
		}

		public void setRecordNumber(int recordNumber){
			this.recordNumber = recordNumber;
		}
		//getters
		public String getTestString(){
			return this.testString;
		}

		public long getCreationTime(){
			return this.creationTime;
		}

		public int getRecordNumber(){
			return this.recordNumber;
		}		
	}
}
