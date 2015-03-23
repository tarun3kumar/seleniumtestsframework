# SeleniumTestsFramework

STF (SeleniumTestsFramework) is step by step test test reporting and automation framework for selenium.
Refer STF [How-To guide](http://www.seleniumtests.com/2013/10/announcing-selenium-tests-automation.html) to learn more about STF features 

You can generate STF jar using following command and use it in your selenium project - 

```mvn clean install```

This command should execute the UI tests in chrome browser and install STF jar in .m2 folder on successful execution of tests
In case you don't see any tests running then you may to have first clean up the project. To do this, import the projects in eclipse and click ```Project Clean``` from command menu

Depending on how you execute the projects, step by step selenium report after test execution would be available -

1. ```<your home directory>/seleniumtestsframework/test-output/SeleniumTestReport.html``` when executing tests using eclipse testng plugin, or
2. ```<your home directory>/seleniumtestsframework/target/surefire-reports/SeleniumTestReport.html``` when executing tests using maven command as stated above
