import java.util.Date;
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
	
	public void setTestData(String testData, int recordNumber){
		this.testData = new TestDatum();
		this.testData.setTestString(testData);
		this.testData.setCreationDate();
		this.testData.setRecordNumber(recordNumber);
	}

	public void setTestKey(String testKey, int recordNumber){
		this.testKey = new TestDatum();
		this.testKey.setTestString(testKey);
		this.testKey.setCreationDate();
		this.testKey.setRecordNumber(recordNumber);
	}

	public String getDataString(){
		return this.testData.getTestString();
	}

	public Date getDataDate(){
		return this.testData.getCreationDate();
	}

	public int getDataRecNo(){
		return this.testData.getRecordNumber();
	}

	public String getKeyString(){
		return this.testKey.getTestString();
	}

	public Date getKeyDate(){
		return this.testKey.getCreationDate();
	}	
	
	public int getKeyRecNo(){
		return this.testKey.getRecordNumber();
	}

	public final boolean initialized(){
		return (this.testData != null) && (this.testKey != null);
	}


	public class TestDatum{
		private String testString;
		private Date creationDate;
		private int recordNumber;

		public TestDatum(){
			this.testString = new String();
		}
		//setters
		public void setTestString(String testString){
			this.testString = testString;
		}

		public void setCreationDate(){
			this.creationDate = new Date();
		}

		public void setRecordNumber(int recordNumber){
			this.recordNumber = recordNumber;
		}
		//getters
		public String getTestString(){
			return this.testString;
		}

		public Date getCreationDate(){
			return this.creationDate;
		}

		public int getRecordNumber(){
			return this.recordNumber;
		}		
	}
}
