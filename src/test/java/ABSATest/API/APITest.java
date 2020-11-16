package ABSATest.API;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.LinkedList;
import java.util.Properties;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
//import org.junit.Test;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.ie.InternetExplorerDriver;
import org.testng.Reporter;
import org.testng.annotations.Test;

import Automation.HybridFramework.Assertions;
import Automation.HybridFramework.Keywords;

public class APITest {
	  WebDriver driver;
	  String path = System.getProperty("user.dir");
	  String ChooseBrowser = "BrowserName";
	  String ChooseTestcaseSheet = "TestcaseSheetName";
	  //ResponseAPITest DogApiResponse = new ResponseAPITest();
	  
	  Keywords keyword = new Keywords();
	  Assertions assertion = new Assertions();
	  
	  //Driver Initialization
	  public void initiateDriver(String BrowserName) throws IOException, InterruptedException {
		    switch (BrowserName) {
		    case "Chrome":
		    System.setProperty("webdriver.chrome.driver", path+"\\Drivers\\chromedriver.exe");
		    driver = new ChromeDriver();
		    break;
		   
		    case "FireFox":
		    System.setProperty("webdriver.gecko.driver", path+"\\Drivers\\geckodriver.exe");
		    driver = new FirefoxDriver();
		    break;
		    
		    case "IE":
		    System.setProperty("webdriver.chrome.driver", path+"\\Drivers\\IEDriverServer.exe");
		    driver = new InternetExplorerDriver();
		    break;
		    }
		    
					    Reporter.log("!!!!!!!!");
					    Reporter.log("Given Two test cases passed");
					    Reporter.log("!!!!!!!!");
					    Reporter.log("\n");
		  
	  }
	

@Test
public void readExcelandexecute() throws IOException, InterruptedException {
//From excelfile
Properties prop = new Properties();
try {
  //Object Repository is opened    
  prop.load(new FileInputStream(path+"\\Externals\\Basic.properties"));
} catch(Exception e) {
  e.printStackTrace();
}

String Browser= prop.getProperty(ChooseBrowser);
String TestcaseSheet = prop.getProperty(ChooseTestcaseSheet);

System.out.println("Browser" + Browser);
String excelFilePath = path+"\\Externals\\" + TestcaseSheet;

FileInputStream fileInputStream = new FileInputStream(excelFilePath);
 
XSSFWorkbook workbook = new XSSFWorkbook(fileInputStream);

int testcasescount = workbook.getNumberOfSheets()-1;

System.out.println("Total test cases :"+testcasescount);

for (int testcase=1;testcase<=testcasescount;testcase++){
  this.initiateDriver(Browser);
  //	System.setProperty("webdriver.chrome.driver", path+"\\Drivers\\chromedriver.exe");
  //	driver = new ChromeDriver();
  XSSFSheet worksheet = workbook.getSheetAt(testcase);
	  
	System.out.println("worksheet Number "+testcase+":"+worksheet.getSheetName());
	
	int row = worksheet.getLastRowNum();
	int column = worksheet.getRow(1).getLastCellNum();
	
	for(int i=1;i<=row;i++){
		 
		LinkedList<String> Testexecution = new LinkedList<>();
		 
		System.out.println("Row value :"+i+"It has first cell value as : "+worksheet.getRow(i).getCell(0));
		
		for(int j=0;j<column-1;j++){
			System.out.println("Column index :"+j);
			Cell Criteria = worksheet.getRow(i).getCell(j);
			 
			String CriteriaText;
			if(Criteria==null){
			CriteriaText = null;
			}else{
			CriteriaText = Criteria.getStringCellValue();
			}
			Testexecution.add(CriteriaText);
			}
			System.out.println("List :"+Testexecution);
			
			String TestStep = Testexecution.get(0);
			String ObjectName = Testexecution.get(1);
			String LocatorType = Testexecution.get(2);
			String Testdata = Testexecution.get(3);
			String AssertionType = Testexecution.get(4);
			String ExpectedValue = Testexecution.get(5);
			String ActualValue = Testexecution.get(6);
			 
			perform(TestStep,ObjectName,LocatorType,Testdata,AssertionType,ExpectedValue,ActualValue);
			 
			System.out.println("Row"+i+" is read and action performed");
			
	}
	driver.close();
	workbook.close();
	System.out.println("************************TEST CASE "+worksheet.getSheetName()+" is executed*******************");
}

}

public void perform(String operation, String objectName, String locatorType, String testdata,
		  String assertionType, String expectedValue, String actualValue) throws IOException, InterruptedException {
		   
		  switch (operation) {
		  case "enter_URL":
		  //Perform click
		  keyword.enter_URL(driver,testdata);
		  break;
		   
		  case "get_currentURL":
		  //Set text on control
		  keyword.get_currentURL(driver);
		  break;
		   
		  case "type":
		  keyword.type(driver, objectName, locatorType, testdata);
		  break;
		   
		  case "click":
		  keyword.click(driver, objectName, locatorType);
		  break;
  		  
		  case "wait":
		  keyword.wait(driver, objectName, locatorType);
		  break;
		   
		  case "implicitWait":
		  Thread.sleep(5000);
		  break;
		  
		  case "select":
		  keyword.select(driver, objectName, locatorType, testdata);
		  break;
		  
		  case "clear":
		  keyword.clear(driver, objectName, locatorType);
		  break;
		  
		  case "APIRequest":
		  keyword.APIRequest(driver, operation, objectName, locatorType, testdata, false);
		  break;
		  
		  case "Verify":
		  keyword.VerifyResponse(driver, operation, objectName, locatorType, testdata, actualValue, expectedValue);
		  break;
		  
		  case "VerifyResult":
		  keyword.APIRequest(driver, operation, objectName, locatorType, testdata, true);
		  break;
		  
		  default:
		  break;
		  }
		  
		  if(operation.contains("AssertText")){
			  
			  switch(assertionType){
			   
			  case "contains":
			  assertion.AssertTextContains(driver, objectName, locatorType, expectedValue);
			  break;
			  
			  case "equals":
			  assertion.AssertURLEquals(driver, keyword.get_currentURL(driver), expectedValue);
			  break;
			  
			  }
			   
			  }
			   
			  if(operation.contains("AssertElement")){
			  
			  assertion.AssertElement(assertionType, objectName, locatorType, driver);
			   
			  }
			  
}

}