package cvm.test;

import cvm.test.model.PriceInfo;

import java.util.List;

public interface PriceMerger {

    List<PriceInfo> merge(List<PriceInfo> oldPrices, List<PriceInfo> newPrices);

}
