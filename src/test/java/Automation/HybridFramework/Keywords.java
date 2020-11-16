package Automation.HybridFramework;

import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.HttpResponse;
import org.apache.http.impl.client.DefaultHttpClient;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.openqa.selenium.support.ui.Select;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Properties;

public class Keywords {
	String path = System.getProperty("user.dir");
	private final String USER_AGENT="Mozilla/5.0";
	String ResCode;
	WebDriver driver;
	 
	public void enter_URL(WebDriver driver,String TestData) throws IOException{
	driver.get(this.getData(TestData));
	driver.manage().window().maximize();
	}
	 
	public String getData(String testdata) {
      Properties prop = new Properties();
	  try {
       //Object Repository is opened    
       prop.load(new FileInputStream(path+"\\Externals\\testdata.properties"));
	   } catch(Exception e) {
	       e.printStackTrace();
	   }
	   String data = prop.getProperty(testdata);
	   return data;
	}
	public void type(WebDriver driver, String ObjectName, String locatorType, String testdata) throws IOException, InterruptedException{
		WebDriverWait wait = new WebDriverWait(driver, 30);
		wait.until(ExpectedConditions.elementToBeClickable(this.getObject(ObjectName,locatorType))).sendKeys(this.getData(testdata));
		Thread.sleep(5000);
		//element.sendKeys(this.getData(testdata));
		//wait.until(ExpectedConditions.elementToBeClickable((By.name("FirstName")))).sendKeys("FName" + i);
	//driver.findElement(this.getObject(ObjectName,locatorType)).sendKeys(this.getData(testdata));
	//driver.findElement(By.xpath("//")).sendKeys(testdata);
	 
	}
	public void wait(WebDriver driver,String ObjectName, String locatorType) throws IOException{
	WebDriverWait wait = new WebDriverWait(driver, 30);
	wait.until(ExpectedConditions.visibilityOf(driver.findElement(this.getObject(ObjectName,locatorType))));
	}
	public void click(WebDriver driver,String ObjectName, String locatorType) throws IOException{
	driver.findElement(this.getObject(ObjectName,locatorType)).click();
	}
	public void clear(WebDriver driver,String ObjectName, String locatorType) throws IOException{
		driver.findElement(this.getObject(ObjectName,locatorType)).clear();
	}
	public String get_currentURL(WebDriver driver){
	String URL = driver.getCurrentUrl();
	return URL;
	}
	public void select(WebDriver driver, String ObjectName, String locatorType, String testdata) throws IOException{
//		Select dropdown1 = new Select(driver.findElement(By.name("RoleId")));  
//   	  dropdown1.selectByVisibleText("Admin");
		Select dropdown = new Select(driver.findElement(this.getObject(ObjectName,locatorType)));  
   	    dropdown.selectByVisibleText(this.getData(testdata));
	}
	
	public String APIRequest(WebDriver driver, String operation, String ObjectName, String locatorType, String testData, boolean APIResult) throws ClientProtocolException, IOException
	{
		
		if(operation.equalsIgnoreCase("Verify")) {
			
		  ResCode = this.getResponseCode(ObjectName, locatorType);
		  
	
		}
		else {
			
		  ResCode = this.getResponseCode("APIResponse", "ResponseCode");
		 
		 
		}
		
		
		int code = Integer.parseInt(ResCode );
		StringBuffer result = new StringBuffer();
		HttpClient client = new DefaultHttpClient();
		//url for list all dog breeds
        String url = this.getAPI(testData);
		
		
		//Get request for url
		HttpGet request = new HttpGet(url);
		//Add header for request
		request.addHeader("User-Agent",USER_AGENT);
		//get response of the url request
		HttpResponse response = client.execute(request);
		//get response code
		int responseCode = response.getStatusLine().getStatusCode();
		
		try{
		if(responseCode == code)
	    {
			
			BufferedReader reader = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
			String line = "";
			while((line=reader.readLine())!=null)
			{
				result.append(line);
                
				//display results
				//if(operation.equalsIgnoreCase("APIRequest") && locatorType.equalsIgnoreCase("ResponseCode")) {
//					  System.out.println("ObjectName" + ObjectName);
//					  
//					   System.out.println("testData" + testData);
				       if(!APIResult) {
				    	   System.out.println("Get Response is Successfull");
				    	   System.out.println(result.toString());
						
					
				       	}
				if(!ObjectName.equalsIgnoreCase("RandomSubbreedgoldenImage")) {
				  Assert.assertTrue(driver.findElement(this.getObject(ObjectName,locatorType)).getText().equals(result.toString()));
			      System.out.println("API Results Verified for " + ObjectName);
				}
				else {
			      System.out.println("API Results Verified for " + ObjectName);
				}
				  
			}
		}
		
		return result.toString();
		}
		catch(Exception ex)
	    {
		result.append("Get Response Failed");
		return result.toString();
	    }
	}
	
	public void VerifyResponse(WebDriver driver, String operation, String ObjectName, String locatorType, String testData, String ActualText, String ExpectedText) throws ClientProtocolException, IOException {
	  String result = this.APIRequest(driver, operation, ObjectName, locatorType, testData, true); 
	    if(operation.equalsIgnoreCase("Verify")){
		  if(result.toString().contains(ExpectedText)) {
			Assert.assertEquals(true, result.toString().contains(ExpectedText));
			System.out.println("Verify " + result.toString());
			System.out.println("Pass  Verify - retriever is within list");
		  }
	   }
   }
	
	String getResponseCode(String ObjectName, String locatorType) throws IOException{
		
		
		Properties prop = new Properties();
		
		
	    try {
	       //Object Repository is opened    
	       prop.load(new FileInputStream(path+"\\Externals\\OR.properties"));
	    } catch(Exception e) {
	       e.printStackTrace();
	    }
	    
		//find by xpath
		if(locatorType.equalsIgnoreCase("ResponseCode")){
		  return prop.getProperty(ObjectName);
		}
		return null;
	}
	
	String getAPI(String testData) throws IOException{
		Properties prop = new Properties();
		  try {
	       //Object Repository is opened    
	       prop.load(new FileInputStream(path+"\\Externals\\testdata.properties"));
		   } catch(Exception e) {
		       e.printStackTrace();
		   }
		   String data = prop.getProperty(testData);
		   return data;
	}
	
	By getObject(String ObjectName, String locatorType) throws IOException{
	 
	 
	Properties prop = new Properties();
	
	
    try {
       //Object Repository is opened    
       prop.load(new FileInputStream(path+"\\Externals\\OR.properties"));
    } catch(Exception e) {
       e.printStackTrace();
    }
    
	//find by xpath
	if(locatorType.equalsIgnoreCase("XPATH")){
	 
	return By.xpath(prop.getProperty(ObjectName));
	}
	//find by xpath
	if(locatorType.equalsIgnoreCase("id")){
	  return By.id(prop.getProperty(ObjectName));
	}
	//find by class
	else if(locatorType.equalsIgnoreCase("CLASSNAME")){
	 
	return By.className(prop.getProperty(ObjectName));
	 
	}
	//find by name
	else if(locatorType.equalsIgnoreCase("NAME")){
	 
	return By.name(prop.getProperty(ObjectName));
	 
	}
	//Find by css
	else if(locatorType.equalsIgnoreCase("cssSelector")){
	 
	return By.cssSelector(prop.getProperty(ObjectName));
	 
	}
	//find by link
	else if(locatorType.equalsIgnoreCase("LINK")){
	 
	return By.linkText(prop.getProperty(ObjectName));
	 
	}
	//find by partial link
	else if(locatorType.equalsIgnoreCase("PARTIALLINK")){
	 
	return By.partialLinkText(prop.getProperty(ObjectName));
	 
	}
	//find by tagname
	else if(locatorType.equalsIgnoreCase("tagName")){
		 
	return By.tagName(prop.getProperty(ObjectName));
	 
	}
	return null;
	 
	}
}