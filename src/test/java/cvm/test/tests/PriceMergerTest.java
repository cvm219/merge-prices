package cvm.test.tests;

import cvm.test.PriceMerger;
import cvm.test.impl.PriceMergerImpl;
import cvm.test.model.PriceInfo;
import org.junit.Test;

import java.util.*;
import static org.junit.Assert.*;

public class PriceMergerTest {

    private PriceMerger priceMerger = new PriceMergerImpl();
    private Date now = new Date();

    Comparator<PriceInfo> comparator = (pi1, pi2) -> {
        if (!pi1.getProductCode().equals(pi2.getProductCode())) {
            return pi1.getProductCode().compareTo(pi2.getProductCode());
        }
        if (pi1.getDepart() != pi2.getDepart()) {
            return Integer.compare(pi1.getDepart(), pi2.getDepart());
        }
        if (pi1.getNumber() != pi2.getNumber()) {
            return Integer.compare(pi1.getNumber(), pi2.getNumber());
        }
        return pi1.getBegin().compareTo(pi2.getBegin());
    };

    private Date add(Date date, int years, int months, int days, int hours, int minutes, int seconds) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.add(Calendar.YEAR, years);
        calendar.add(Calendar.MONTH, months);
        calendar.add(Calendar.DATE, days);
        calendar.add(Calendar.HOUR, hours);
        calendar.add(Calendar.MINUTE, minutes);
        calendar.add(Calendar.SECOND, seconds);
        return calendar.getTime();
    }

    private Date addDay(Date date, int days) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.add(Calendar.DATE, days);
        return calendar.getTime();
    }

    private List<PriceInfo> generateOldForTestCase1(long startId) {
        int[] beginAdds = new int[]{0, 10, 40};
        int[] endAdds = new int[]{5, 20, 60};
        long[] prices = new long[]{10, 20, 30};
        List<PriceInfo> priceInfos = new ArrayList<>();
        for (int i = 0; i < beginAdds.length; i++) {
            priceInfos.add(new PriceInfo(startId + i, "123", 1, 1, addDay(now, beginAdds[i]), addDay(now, endAdds[i]), prices[i]));
        }
        return priceInfos;
    }

    private List<PriceInfo> generateNewForTestCase1() {
        int[] beginAdds = new int[]{5, 22, 61};
        int[] endAdds = new int[]{10, 29, 70};
        long[] prices = new long[]{15, 25, 35};
        List<PriceInfo> priceInfos = new ArrayList<>();
        for (int i = 0; i < beginAdds.length; i++) {
            priceInfos.add(new PriceInfo(null, "123", 1, 1, addDay(now, beginAdds[i]), addDay(now, endAdds[i]), prices[i]));
        }
        return priceInfos;
    }

    private List<PriceInfo> generateResultForTestCase1() {
        int[] beginAdds = new int[]{0, 10, 40, 5, 22, 61};
        int[] endAdds = new int[]{5, 20, 60, 10, 29, 70};
        long[] prices = new long[]{10, 20, 30, 15, 25, 35};
        List<PriceInfo> priceInfos = new ArrayList<>();
        for (int i = 0; i < beginAdds.length; i++) {
            priceInfos.add(new PriceInfo(null, "123", 1, 1, addDay(now, beginAdds[i]), addDay(now, endAdds[i]), prices[i]));
        }
        return priceInfos;
    }

    private List<PriceInfo> generateOldForTestCase2(long startId) {
        int[] beginAdds = new int[]{0, 10, 40, 70, 90};
        int[] endAdds = new int[]{5, 20, 60, 80, 100};
        long[] prices = new long[]{10, 10, 30, 30, 30};
        List<PriceInfo> priceInfos = new ArrayList<>();
        for (int i = 0; i < beginAdds.length; i++) {
            priceInfos.add(new PriceInfo(startId + i, "123", 1, 2, addDay(now, beginAdds[i]), addDay(now, endAdds[i]), prices[i]));
        }
        return priceInfos;
    }

    private List<PriceInfo> generateNewForTestCase2() {
        int[] beginAdds = new int[]{5, 20, 59, 79};
        int[] endAdds = new int[]{10, 40, 71, 90};
        long[] prices = new long[]{10, 25, 30, 30};
        List<PriceInfo> priceInfos = new ArrayList<>();
        for (int i = 0; i < beginAdds.length; i++) {
            priceInfos.add(new PriceInfo(null, "123", 1, 2, addDay(now, beginAdds[i]), addDay(now, endAdds[i]), prices[i]));
        }
        return priceInfos;
    }

    private List<PriceInfo> generateResultForTestCase2() {
        int[] beginAdds = new int[]{0, 20, 40};
        int[] endAdds = new int[]{20, 40, 100};
        long[] prices = new long[]{10, 25, 30};
        List<PriceInfo> priceInfos = new ArrayList<>();
        for (int i = 0; i < beginAdds.length; i++) {
            priceInfos.add(new PriceInfo(null, "123", 1, 2, addDay(now, beginAdds[i]), addDay(now, endAdds[i]), prices[i]));
        }
        return priceInfos;
    }

    private List<PriceInfo> generateOldForTestCase3(long startId) {
        int[] beginAdds = new int[]{-100, -90, -60, 0, 10, 40, 70, 90, 1010, 1030, 1060, 1100};
        int[] endAdds = new int[]{-95, -80, -40, 5, 20, 60, 80, 100, 1020, 1040, 1090, 1200};
        long[] prices = new long[]{10, 20, 30, 10, 10, 30, 30, 30, 100, 150, 200, 250};
        List<PriceInfo> priceInfos = new ArrayList<>();
        for (int i = 0; i < beginAdds.length; i++) {
            priceInfos.add(new PriceInfo(startId + i, "1234", 1, 2, addDay(now, beginAdds[i]), addDay(now, endAdds[i]), prices[i]));
        }
        return priceInfos;
    }

    private List<PriceInfo> generateNewForTestCase3() {
        int[] beginAdds = new int[]{-95, -78, -39, 5, 20, 59, 79, 1000};
        int[] endAdds = new int[]{-90, -71, -30, 10, 40, 71, 90, 1100};
        long[] prices = new long[]{15, 25, 35, 10, 25, 30, 30, 217};
        List<PriceInfo> priceInfos = new ArrayList<>();
        for (int i = 0; i < beginAdds.length; i++) {
            priceInfos.add(new PriceInfo(null, "1234", 1, 2, addDay(now, beginAdds[i]), addDay(now, endAdds[i]), prices[i]));
        }
        return priceInfos;
    }

    private List<PriceInfo> generateResultForTestCase3() {
        int[] beginAdds = new int[]{-100, -90, -60, -95, -78, -39, 0, 20, 40, 1000, 1100};
        int[] endAdds = new int[]{-95, -80, -40, -90, -71, -30, 20, 40, 100, 1100, 1200};
        long[] prices = new long[]{10, 20, 30, 15, 25, 35, 10, 25, 30, 217, 250};
        List<PriceInfo> priceInfos = new ArrayList<>();
        for (int i = 0; i < beginAdds.length; i++) {
            priceInfos.add(new PriceInfo(null, "1234", 1, 2, addDay(now, beginAdds[i]), addDay(now, endAdds[i]), prices[i]));
        }
        return priceInfos;
    }

    @Test
    public void test1() {
        List<PriceInfo> oldPriceInfoList = new ArrayList<>();
        List<PriceInfo> newPriceInfoList = new ArrayList<>();
        List<PriceInfo> expectedResult = new ArrayList<>();
        // test case #1 - all new prices does not cross with old ones
        oldPriceInfoList.addAll(generateOldForTestCase1(1));
        newPriceInfoList.addAll(generateNewForTestCase1());
        expectedResult.addAll(generateResultForTestCase1());
        // test case #2 - old ones merged with new ones because have same value
        oldPriceInfoList.addAll(generateOldForTestCase2(100));
        newPriceInfoList.addAll(generateNewForTestCase2());
        expectedResult.addAll(generateResultForTestCase2());
        // test case #3 - common test
        oldPriceInfoList.addAll(generateOldForTestCase3(1000));
        newPriceInfoList.addAll(generateNewForTestCase3());
        expectedResult.addAll(generateResultForTestCase3());

        List<PriceInfo> result = priceMerger.merge(oldPriceInfoList, newPriceInfoList);

        assertEquals(expectedResult.size(), result.size());
        Collections.sort(expectedResult, comparator);
        Collections.sort(result, comparator);
        for (int i = 0; i < expectedResult.size(); i++) {
            if (!expectedResult.get(i).equals(result.get(i))) {
                System.out.println(expectedResult.get(i).toMyString(now) + "\n" + result.get(i).toMyString(now));
                assertTrue(false);
            }
        }

    }

}
