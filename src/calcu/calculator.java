package calcu;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Iterator;
import java.util.concurrent.TimeUnit;

import org.apache.commons.io.FileUtils;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.openqa.selenium.By;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import io.appium.java_client.AppiumDriver;
import io.appium.java_client.android.AndroidDriver;

public class calculator {

	public static AppiumDriver<WebElement> driver;
	public String nombre = "C:\\Users\\juan.munoz.hoyos\\eclipse-workspace\\calcu\\datos.xlsx";
	public String[] prefix;
	public String result = "";

	@BeforeMethod
	public void inicio() throws MalformedURLException {

		String packagename = "de.underflow.calc";
		String URL = "http://127.0.0.1:4723/wd/hub";
		String activityname = "de.underflow.calc.CalculatorMainActivity";

		DesiredCapabilities caps = new DesiredCapabilities();

		caps.setCapability("deviceName", "Nexus 6P");
		caps.setCapability("udid", "84B7N16315001658"); // Give Device ID of your mobile phone
		caps.setCapability("platformName", "Android");
		caps.setCapability("platformVersion", "8.0.0");
		caps.setCapability("appPackage", packagename);
		caps.setCapability("appActivity", activityname);
		caps.setCapability("noReset", "true");

		driver = new AndroidDriver<WebElement>(new URL(URL), caps);
		driver.manage().timeouts().implicitlyWait(80, TimeUnit.SECONDS);

	}

	@Test
	public void process() {

		prefix = new String[] { "Zero", "One", "Two", "Three", "Four", "Five", "Six", "Seven", "Eight", "Nine" };
		try {
			FileInputStream inputStream = new FileInputStream(new File(nombre));
			// leer archivo excel
			XSSFWorkbook workbook = new XSSFWorkbook(inputStream);
			// obtener la hoja que se va leer
			XSSFSheet sheet = workbook.getSheetAt(0);
			// obtener todas las filas de la hoja excel
			Iterator<Row> rowIterator = sheet.iterator();
		
			Row row;
			// se recorre cada fila hasta el final
			while (rowIterator.hasNext()) {
				row = rowIterator.next();
				// se obtiene las celdas por fila
				Iterator<Cell> cellIterator = row.cellIterator();
				Cell cell;
				// se recorre cada celda
				while (cellIterator.hasNext()) {
					// se obtiene la celda en especifico
					cell = cellIterator.next();
					switch (cell.getCellTypeEnum().toString()) {

						case "NUMERIC":
							int num = (int)cell.getNumericCellValue();
							String numS = String.valueOf(num);
							System.out.print(num + " ");
							if(numS.length()>0) {
								
								 for (int i = 0; i < numS.length(); i++) {
									String c = Character.toString(numS.charAt(i));
									driver.findElement(By.id("de.underflow.calc:id/" + prefix[Integer.parseInt(c)]))
									.click();
								}
							}else {
								driver.findElement(By.id("de.underflow.calc:id/" + prefix[num]));
							}
							break;
	
						case "STRING":
							System.out.print(cell.getStringCellValue() + " ");
	
							if (cell.getStringCellValue().equals("+")) {
	
								driver.findElement(By.id("de.underflow.calc:id/Plus")).click();
							}
	
							if (cell.getStringCellValue().equals("-")) {
	
								driver.findElement(By.id("de.underflow.calc:id/Minus")).click();
							}
	
							if (cell.getStringCellValue().equals("*")) {
	
								driver.findElement(By.id("de.underflow.calc:id/Multiply")).click();
							}
	
							if (cell.getStringCellValue().equals("/")) {
	
								driver.findElement(By.id("de.underflow.calc:id/Divide")).click();
							}
	
							if (cell.getStringCellValue().equals("=")) {
	
								driver.findElement(By.id("de.underflow.calc:id/Equals")).click();
								result = driver.findElement(By.id("de.underflow.calc:id/Result")).getText();
								System.out.println(result); 
								Cell newCell = row.createCell(cell.getColumnIndex()+1);
								newCell.setCellValue(result);
								
							}
	
							break;

					}
					
					takeScreenShot("juan munoz");
					
				}
				

				System.out.println("");
				driver.findElement(By.id("de.underflow.calc:id/Clear")).click();
			}
			inputStream.close();
            FileOutputStream outputStream = new FileOutputStream(nombre);
            workbook.write(outputStream);
            workbook.close();
            outputStream.close();
		}catch (Exception e) {
            e.printStackTrace();
        }
	}
	   
	private void takeScreenShot(String nameTest)  {
	    	  File scrFile = null;
	    	  File targetFile = null;
	    	  String fileName = "";
	    	  
	    	  scrFile = driver.getScreenshotAs(OutputType.FILE);
	    	  SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MMM-yyyy__hh_mm_ssaa");
	    	  fileName = nameTest+"_" + dateFormat.format(new Date()) + ".png";
	          targetFile = new File("screenshots/" + fileName);
	       
	    	  try 
	    	  {
	    	     FileUtils.copyFile(scrFile, targetFile);
	    	  } catch (IOException e) {
	    	   e.printStackTrace();
	    	  }
	  } 
	 

}
