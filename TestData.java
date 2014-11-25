import java.util.Date;
public class TestData{
	private TestDatum testData;
	private TestDatum testKey;
	private TestDatum value1;
	private TestDatum value2;
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
		this.testData.setRecordNumber(recordNumber + 1);
	}

	public void setTestKey(String testKey, int recordNumber){
		this.testKey = new TestDatum();
		this.testKey.setTestString(testKey);
		this.testKey.setCreationDate();
		this.testKey.setRecordNumber(recordNumber + 1);
	}

	public void setValue(String testKey, int recordNumber, int valNumber){
		if(valNumber != 1 || valNumber != 2){
			System.out.println("TestData.setValue(..., valNumber) valNumber must be 1 or 2");
			return;
		}

		if(valNumber == 1){
			this.value1.setTestString(testKey);
			this.value1.setCreationDate();
			this.value1.setRecordNumber(recordNumber + 1);
		}
		else if(valNumber == 2){
			this.value2.setTestString(testKey);
			this.value2.setCreationDate();
			this.value2.setRecordNumber(recordNumber + 1);
		}
	}

	public String getVal1String(){
		return this.value1.getTestString();
	}

	public Date getVal1Date(){
		return this.value1.getCreationDate();
	}

	public int getVal1RecNo(){
		return this.value1.getRecordNumber();
	}

	public String getVal2String(){
		return this.value2.getTestString();
	}

	public Date getVal2Date(){
		return this.value2.getCreationDate();
	}

	public int getVal2RecNo(){
		return this.value2.getRecordNumber();
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
