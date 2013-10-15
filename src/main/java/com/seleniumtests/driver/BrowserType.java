package com.seleniumtests.driver;


public enum BrowserType {
	FireFox("*firefox"),
	InternetExplore("*iexplore"),
	Chrome("*chrome"),
	HtmlUnit("*htmlunit"),
	Safari("*safari"),
	Opera("*opera"),
	Android("*android"),
	IPhone("*iphone"),
	IPad("*ipad"),
	PhantomJS("*phantomjs");
	
	
	public static BrowserType getBrowserType(String type)
	{
		if(type.equalsIgnoreCase("*firefox") || type.equalsIgnoreCase("firefox"))
			return BrowserType.FireFox;
		else if(type.equalsIgnoreCase("*iexplore") || type.equalsIgnoreCase("iexplore"))
			return BrowserType.InternetExplore;
		else if(type.equalsIgnoreCase("*chrome") || type.equalsIgnoreCase("chrome"))
			return BrowserType.Chrome;
		else if(type.equalsIgnoreCase("*htmlunit") || type.equalsIgnoreCase("htmlunit"))
			return BrowserType.HtmlUnit;
		else if(type.equalsIgnoreCase("*safari") || type.equalsIgnoreCase("safari"))
			return BrowserType.Safari;
		else if(type.equalsIgnoreCase("*android") || type.equalsIgnoreCase("android"))
			return BrowserType.Android;
		else if(type.equalsIgnoreCase("*iphone")  || type.equalsIgnoreCase("iphone"))
			return BrowserType.IPhone;
		else if(type.equalsIgnoreCase("*ipad") ||type.equalsIgnoreCase("ipad"))
			return BrowserType.IPad;
		else if(type.equalsIgnoreCase("*opera") || type.equalsIgnoreCase("opera"))
			return BrowserType.Opera;
		else if(type.equalsIgnoreCase("*phantomjs") || type.equalsIgnoreCase("phantomjs"))
			return BrowserType.PhantomJS;
		else
			return BrowserType.FireFox;
	}
	
	private String type;
	
	BrowserType(String type) {
		this.type = type;
	}
	
	public String getType(){
		return this.type;
	}

}
