package Automation.HybridFramework;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Properties;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.testng.Assert;

import Automation.HybridFramework.Keywords;

public class Assertions {
  String path = System.getProperty("user.dir");
  
  Keywords keyword = new Keywords();
  
  //public WebDriver driver;
  
  public void AssertTextContains(WebDriver driver, String ObjectName, String LocatorType, String ExpectedText) throws FileNotFoundException {
    String ActualText = driver.findElement(this.getObject(ObjectName, LocatorType)).getAttribute("innerHTML");
    System.out.println("ActualText " +ActualText);
    System.out.println("ExpectedText " +ExpectedText);
    String[] ExpectedText1 = ExpectedText.split("(?<=\\D)(?=\\d)");
    String[] ActualText1 = driver.findElement(this.getObject(ObjectName, LocatorType)).getAttribute("innerHTML").split("(?<=\\D)(?=\\d)");
    Assert.assertEquals(ActualText1[0].equalsIgnoreCase(ExpectedText1[0]), ActualText1[1].equalsIgnoreCase(ExpectedText1[1]));
    System.out.println(ActualText + " Successfully Added");
  }
  
  public void AssertURLEquals(WebDriver driver, String getCurrentURL, String ExpectedValue) {
	Assert.assertEquals(true, getCurrentURL.equals(getCurrentURL));
  }
  
  public void AssertElement(String AssertionType, String ObjectName, String LocatorType, WebDriver driver) throws IOException {
    switch(AssertionType) {
    case "Displayed":
      Assert.assertEquals(true, driver.findElement(this.getObject(ObjectName, LocatorType)).isDisplayed());
      System.out.println("AssertionType " +"Displayed");
      break;
    case "Enabled":
      Assert.assertEquals(true, driver.findElement(this.getObject(ObjectName, LocatorType)).isEnabled());
      break;
    case "Selected":
    	Assert.assertEquals(true, driver.findElement(this.getObject(ObjectName, LocatorType)).isSelected());
      break;
    }
  }
  
  public By getObject(String ObjectName, String LocatorType) throws FileNotFoundException {
		
		//String path = System.getProperty("user.dir");
		
		//Object Repository is opened      
	    //File file = new File(path+"\\Externals\\OR.properties");
	    //FileInputStream fileInput = new FileInputStream(file);
		
		//Properties file is read    
		Properties prop = new Properties();
		
		try {
		  //Object Repository is opened    
	      prop.load(new FileInputStream(path+"\\Externals\\OR.properties"));
	    } catch(Exception e) {
	      e.printStackTrace();
	    }

	    //find by xpath	
		if(LocatorType.equalsIgnoreCase("xpath")) {
	      return By.xpath(prop.getProperty(ObjectName)); 
		  // ObjectName is read and its value is returned
		}
		//find by className
		else if(LocatorType.equalsIgnoreCase("classname")) {
		  return By.className(prop.getProperty(ObjectName)); 
		  // ObjectName is read and its value is returned
	    }
		//find by id
		else if(LocatorType.equalsIgnoreCase("id")) {
		  return By.id(prop.getProperty(ObjectName)); 
		  // ObjectName is read and its value is returned
	    }
		//find by name
		else if(LocatorType.equalsIgnoreCase("name")) {
		  return By.name(prop.getProperty(ObjectName)); 
		  // ObjectName is read and its value is returned
	    }
		//find by cssselector
		else if(LocatorType.equalsIgnoreCase("cssselector")) {
	      return By.cssSelector(prop.getProperty(ObjectName)); 
		  // ObjectName is read and its value is returned
	    }
		//find by linkText
		else if(LocatorType.equalsIgnoreCase("linkText")) {
		  return By.linkText(prop.getProperty(ObjectName)); 
	      // ObjectName is read and its value is returned
	    }
		//find by partiallinkText
		else if(LocatorType.equalsIgnoreCase("partiallinkText")) {
		  return By.partialLinkText(prop.getProperty(ObjectName)); 
		  // ObjectName is read and its value is returned
	    }
		return null;
	  }
}
