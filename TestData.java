
public class TestData{
	private TestDatum testData;
	private TestDatum testKey;
	private boolean dataSet;
	private static TestData testData = null;	

	protected TestData(){
		testData = null;
		testKey = null;
	}

	public static TestData getInstance(){
		if(testData == null){
			testData = new TestData();
		}

		return testData;
	}
	
	public void setTestData(String testData, long creationTime, int recordNumber){
		this.testData = new TestDatum();
		this.testData.setTestString = testData;
		this.testData.setCreationTime = creationTime;
		this.testData.setRecordNumber = recordNumber;
	}

	public void setTestKey(String testKey, long creationTime, int recordNumber){
		this.testKey = new TestDatum();
		this.testKey.setTestString = testData;
		this.testKey.setCreationTime = creationTime;
		this.testKey.setRecordNumber = recordNumber;
	}

	public String getTestDataString
	
	private class TestDatum{
		private String testString;
		private long creationTime;
		private int recordNumber;

		public TestDatum(){
			this.testString = new String();
		}
		//setters
		public setTestString(String testString){
			this.testString = testString;
		}

		public setCreationTime(long creationTime){
			this.creationTime = creationTime;
		}

		public setRecordNumber(int recordNumber){
			this.recordNumber = recordNumber;
		}
		//getters
		public getTestString(){
			this.testString = testString;
		}

		public getCreationTime(){
			this.creationTime = creationTime;
		}

		public getRecordNumber(){
			this.recordNumber = recordNumber;
		}		
	}
}
