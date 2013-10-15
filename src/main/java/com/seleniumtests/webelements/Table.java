package com.seleniumtests.webelements;

import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.NotFoundException;
import org.openqa.selenium.WebElement;

public class Table extends HtmlElement {
	private List<WebElement> rows = null;
	private List<WebElement> columns = null;

	public Table(String label, By by) {
		super(label, by);
	}

	public Table(String label, String locator) {
		super(label, locator);
	}

	public void findElement() {
		super.findElement();
		try {
			rows = element.findElements(By.tagName("tr"));
		} catch (NotFoundException e) {
		}

	}

	public int getColumnCount() {
		if (rows == null)
			findElement();
		
		// Need to check whether rows is null AND whether or not the list of rows is empty
		if (rows != null && !rows.isEmpty()) {
			try {
				columns = rows.get(0).findElements(By.tagName("td"));
			} catch (NotFoundException e) {
				
			}
			if (columns == null || columns.size() == 0) {
				
				try {
					if(rows.size()>1)
						columns = rows.get(1).findElements(By.tagName("td"));
					else
						columns = rows.get(0).findElements(By.tagName("th"));
				} catch (NotFoundException e) {
				}
			}
		}
		if (columns != null)
			return columns.size();
		return 0;
	}

	public List<WebElement> getColumns() {
		return columns;
	}

	/**
	 * Get table cell content
	 * 
	 * @param row Starts from 1
	 * @param column Starts from 1
	 */
	public String getContent(int row, int column) {
		if (rows == null)
			findElement();

		if (rows != null && !rows.isEmpty()) {
			try {
				columns = rows.get(row - 1).findElements(By.tagName("td"));
			} catch (NotFoundException e) {
			}
			if (columns == null || columns.size() == 0) {
				try {
					columns = rows.get(row - 1).findElements(By.tagName("th"));
				} catch (NotFoundException e) {
				}
			}
			return columns.get(column - 1).getText();
		}
		
		return null;
	}

	// TODO MM: IN this method we're looking for /tbody/tr OR /tr -- this differs from how the rows property is being set
	// in the constructor and in the findElement() method. Why do we call findElements() immediately after the
	// findElement() call, which basically does the same thing? If we expect the answer to be different when we call /tbody/tr,
	// that means we don't trust this.findElement(), which means the getRowCount might be different from rows.size(). That could
	// lead to some very tricky debugging
	public int getRowCount() {
		if (rows == null)
			findElement();
		else
			return rows.size();
		int count = element.findElements(By.xpath("tbody/tr")).size();
		if (count == 0) {
			count = element.findElements(By.xpath("tr")).size();
		}

		return count;
	}

	public List<WebElement> getRows() {
		return rows;
	}

	public boolean isHasBody() {
		return getRows().size()>0;
	}

//	public void setColumns(List<WebElement> columns) {
//		this.columns = columns;
//	}
//
//	public void setHasBody(boolean hasBody) {
//		this.hasBody = hasBody;
//	}
//
//	public void setRows(List<WebElement> rows) {
//		this.rows = rows;
//	}
}