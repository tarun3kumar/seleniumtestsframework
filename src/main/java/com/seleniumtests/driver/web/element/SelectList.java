package com.seleniumtests.driver.web.element;

import java.util.ArrayList;
import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.UnexpectedTagNameException;

import com.seleniumtests.controller.Logging;
import com.seleniumtests.driver.web.WebUXDriver;

/**
 * Support both standard select tag and fake select consists of tag ul and li
 * 
 */
public class SelectList extends HtmlElement {

	protected Select select = null;
	protected List<WebElement> options = null;

	public SelectList(String text, By by) {
		super(text, by);
	}

	public SelectList(String text, String locator) {
		super(text, locator);
	}

	/**
	 * De-selects all options in a multi-select list element
	 */
	public void deselectAll() {
		Logging.logWebStep(null, "deselect all options on " + toHTML(), false);
		findElement();
		if (!isMultiple()) {
			throw new UnsupportedOperationException("You may only deselect all options of a multi-select");
		}

		for (WebElement option : options) {
			if (option.isSelected()) {
				option.click();
			}
		}
	}

	public void deselectByIndex(int index) {
		Logging.logWebStep(null, "deselect index\"" + index + "\" on " + toHTML(), false);
		findElement();
		WebElement option = options.get(index);
		if (option.isSelected()) {
			option.click();
		}
	}

	public void deselectByText(String text) {
		Logging.logWebStep(null, "deselect text\"" + text + "\" on " + toHTML(), false);
		findElement();
		for (WebElement option : options) {
			if (option.getAttribute("text").equals(text)) {
				if (option.isSelected()) {
					option.click();
				}
				break;
			}
		}
	}

	public void deselectByValue(String value) {
		Logging.logWebStep(null, "deselect value\"" + value + "\" on " + toHTML(), false);
		findElement();
		for (WebElement option : options) {
			if (option.getAttribute("value").equals(value)) {
				if (option.isSelected()) {
					option.click();
				}
				break;
			}
		}

	}

	protected void findElement() {
		driver = WebUXDriver.getWebDriver();
		element = driver.findElement(this.getBy());
		try {
			select = getNewSelectElement(element);
			options = select.getOptions();
		} catch (UnexpectedTagNameException e) {
			if (element.getTagName().equalsIgnoreCase("ul"))
				options = element.findElements(By.tagName("li"));
		}
	}

	/**
	 * Returns a new Select element (created to facilitate unit testing)
	 * 
	 * @return
	 */
	protected Select getNewSelectElement(WebElement element) {
		return new Select(element);
	}

	public List<WebElement> getOptions() {
		findElement();
		return options;
	}

	public String getSelectedText() {
		findElement();
		for (WebElement option : options) {
			if (option.isSelected()) {
				return option.getAttribute("text");
			}
		}
		return null;
	}

	public String[] getSelectedTexts() {
		findElement();
		List<String> textList = new ArrayList<String>();
		for (WebElement option : options) {
			if (option.isSelected()) {
				textList.add(option.getAttribute("text"));
			}
		}
		String[] texts = new String[textList.size()];
		return textList.toArray(texts);
	}

	public String getSelectedValue() {
		findElement();
		for (WebElement option : options) {
			if (option.isSelected()) {
				return option.getAttribute("value");
			}
		}
		return null;
	}

	public String[] getSelectedValues() {
		findElement();
		List<String> valueList = new ArrayList<String>();
		for (WebElement option : options) {
			if (option.isSelected()) {
				valueList.add(option.getAttribute("value"));
			}
		}
		String[] values = new String[valueList.size()];
		return valueList.toArray(values);
	}

	public void init() {
		super.init();
		try {
			select = getNewSelectElement(element);
			options = select.getOptions();
		} catch (UnexpectedTagNameException e) {
			if (element.getTagName().equalsIgnoreCase("ul")) {
				options = element.findElements(By.tagName("li"));
			}
		}
	}

	public boolean isMultiple() {
		findElement();
		String value = element.getAttribute("multiple");
		return value != null && !"false".equals(value);
	}

	public void selectByIndex(int index) {
		Logging.logWebStep(null, "select index\"" + index + "\" on " + toHTML(), false);
		findElement();
		WebElement option = options.get(index);
		setSelected(option);
	}

	public void selectByIndex(int[] indexs) {
		Logging.logWebStep(null, "select indexs\"" + indexs.toString() + "\" on " + toHTML(), false);
		findElement();
		for (int i = 0; i < indexs.length; i++) {
			WebElement option = options.get(indexs[i]);
			setSelected(option);
		}
	}

	/**
	 * Select standard select by attribute text, and select fake select with ul and li by attribute title
	 * @param text
	 */
	public void selectByText(String text) {
		Logging.logWebStep(null, "select text\"" + text + "\" on " + toHTML(), false);
		findElement();
		if(options==null)
		{
			driver.findElement(By.xpath("//li[text()='"+text+"']")).click();
			return;
		}
		for (WebElement option : options) {
			String selectedText = null;
			if (option.getTagName().equalsIgnoreCase("li"))
				selectedText = option.getAttribute("title");
			else
				selectedText = option.getAttribute("text");
			if (selectedText.equals(text)) {
				setSelected(option);
				break;
			}
		}
	}

	public void selectByText(String[] texts) {
		Logging.logWebStep(null, "select texts\"" + texts + "\" on " + toHTML(), false);
		findElement();
		for (int i = 0; i < texts.length; i++) {
			for (WebElement option : options) {
				if (option.getAttribute("text").equals(texts[i])) {
					setSelected(option);
					break;
				}
			}
		}
	}

	public void selectByValue(String value) {
		Logging.logWebStep(null, "select value\"" + value + "\" on " + toHTML(), false);
		findElement();
		for (WebElement option : options) {
			if (option.getAttribute("value").equals(value)) {
				setSelected(option);
				break;
			}
		}
	}

	public void selectByValue(String[] values) {
		Logging.logWebStep(null, "select values\"" + values + "\" on " + toHTML(), false);
		findElement();
		for (int i = 0; i < values.length; i++) {
			for (WebElement option : options) {
				if (option.getAttribute("value").equals(values[i])) {
					setSelected(option);
					break;
				}
			}
		}
	}

	private void setSelected(WebElement option) {
		if (!option.isSelected()) {
			option.click();
		}
		try {//Sleep 1 second to avoid StaleElementReferenceException
			Thread.sleep(1000);
		} catch (InterruptedException e) {
			//e.printStackTrace();//Ignore
		}
	}
}
