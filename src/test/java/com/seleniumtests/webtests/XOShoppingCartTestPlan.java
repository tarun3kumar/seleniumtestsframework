package com.seleniumtests.webtests;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;

import org.testng.ITestContext;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;

import com.seleniumtests.controller.ContextManager;
import com.seleniumtests.controller.EasyFilter;
import com.seleniumtests.controller.TestPlan;
import com.seleniumtests.controller.TestRetryAnalyzer;
import com.seleniumtests.dataobject.User;
import com.seleniumtests.util.SpreadSheetUtil;
import com.seleniumtests.util.internal.entity.TestObject;
import com.seleniumtests.webpage.SignInPage;
import com.seleniumtests.webpage.ViewItemPage;
import com.seleniumtests.webpage.XOShoppingCartPage;

/**
 * Verifies shopping cart page use cases
 * <p/>
 * User: tbhadauria
 * Date: 4/15/13
 * Time: 12:20 PM
 */
@Test(retryAnalyzer = TestRetryAnalyzer.class, groups = {"XORevampCart"})
public class XOShoppingCartTestPlan extends TestPlan {

    private String cartServiceURI;

    @BeforeMethod(alwaysRun = true)
    @Parameters("cartServiceURI")
    public void setUp(String incentiveServiceURI) {
        this.cartServiceURI = incentiveServiceURI;
    }

    @DataProvider(name = "shopcart", parallel = true)
    public static Iterator<Object[]> getUserInfo(Method m,
                                                 ITestContext testContext) throws Exception {
        EasyFilter filter = EasyFilter.equalsIgnoreCase(TestObject.TEST_METHOD,
                m.getName());
        filter = EasyFilter.and(filter, EasyFilter.equalsIgnoreCase(
                TestObject.TEST_SITE,
                ContextManager.getTestLevelContext(testContext).getSite()));

        LinkedHashMap<String, Class<?>> classMap = new LinkedHashMap<String, Class<?>>();
        classMap.put("TestObject", TestObject.class);
        classMap.put("User", User.class);

        return SpreadSheetUtil.getEntitiesFromSpreadsheet(
                XOShoppingCartTestPlan.class, classMap, "shopcart.csv", 0,
                null, filter);
    }

    /**
     * Adds single BIN items to cart, verifies Item attributes in shopping cart page and completes checkout
     *
     * @param testObject
     * @param buyer
     * @throws Exception
     */
    @Test(groups = {"addSingleNonMSKUItemToCart"}, dataProvider = "shopcart",
            description = "Adds single BIN items to cart, verifies Item attributes in shopping cart page and completes checkout")
    public void addSingleNonMSKUItemToCart(TestObject testObject, final User buyer)
            throws Exception {

        new SignInPage(true)
                .signIn(buyer);
        XOShoppingCartPage shoppingCartPage =
                new ViewItemPage()
                        .addBINItemToCart(
                                new HashMap<String, Integer>() {{
                                    put("350840014961", 1);
                                }});

        shoppingCartPage
                .placeOrder()
                .proceedToPay();
                /*.makePNBNetBankingPayment()
                .submitBillDeskPayment();*/
    }
}