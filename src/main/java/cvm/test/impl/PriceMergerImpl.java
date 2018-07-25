package cvm.test.impl;

import cvm.test.model.PriceInfo;

import java.util.*;

public class PriceMergerImpl implements cvm.test.PriceMerger {

    private class MapKey {
        String productCode;
        int number;
        int depart;

        MapKey(PriceInfo priceInfo) {
            this.productCode = priceInfo.getProductCode();
            this.number = priceInfo.getNumber();
            this.depart = priceInfo.getDepart();
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            MapKey mapKey = (MapKey) o;
            return number == mapKey.number &&
                    depart == mapKey.depart &&
                    Objects.equals(productCode, mapKey.productCode);
        }

        @Override
        public int hashCode() {
            return Objects.hash(productCode, number, depart);
        }
    }

    private void checkPriceInfo(PriceInfo priceInfo) {
        Objects.requireNonNull(priceInfo.getProductCode(), "Product code of price info is null");
        Objects.requireNonNull(priceInfo.getBegin(), "Begin date of price info is null");
        Objects.requireNonNull(priceInfo.getEnd(), "End date of price info is null");
        if (priceInfo.getBegin().compareTo(priceInfo.getEnd()) >= 0) {
            throw new IllegalArgumentException("Empty date segment of price info");
        }
        if (priceInfo.getValue() < 0) {
            throw new IllegalArgumentException("Value of price is less than 0");
        }
    }

    private Map<MapKey, List<PriceInfo>> separateAndCheckCorrectness(List<PriceInfo> priceInfoList, boolean old) {
        Set<Long> ids = new HashSet<>();
        Map<MapKey, List<PriceInfo>> separatedPriceInfos = new HashMap<>();
        for (int i = 0; i < priceInfoList.size(); i++) {
            PriceInfo priceInfo = priceInfoList.get(i);
            if (old) {
                if (ids.contains(Objects.requireNonNull(priceInfo.getId(), "Id of old price is null"))) {
                    throw new IllegalArgumentException("Two or more old price info have same id");
                }
                ids.add(priceInfo.getId());
            }
            checkPriceInfo(priceInfo);
            MapKey key = new MapKey(priceInfo);
            if (!separatedPriceInfos.containsKey(key)) {
                separatedPriceInfos.put(key, new ArrayList<>());
            }
            separatedPriceInfos.get(key).add(priceInfo);
        }
        for (Map.Entry<MapKey, List<PriceInfo>> entry : separatedPriceInfos.entrySet()) {
            List<PriceInfo> entryList = entry.getValue();
            Collections.sort(entryList, (p1, p2) -> {
                return p1.getBegin().compareTo(p2.getBegin());
            });
            for (int i = 0; i < entryList.size() - 1; i++) {
                if (entryList.get(i).getEnd().compareTo(entryList.get(i + 1).getBegin()) > 0) {
                    throw new IllegalArgumentException("Two or more " + (old ? "old" : "new") + " price info are intersected");
                }
            }
        }
        return separatedPriceInfos;
    }

    private Date max(Date d1, Date d2) {
        return d1.compareTo(d2) > 0 ? d1 : d2;
    }

    private Date min(Date d1, Date d2) {
        return d1.compareTo(d2) < 0 ? d1 : d2;
    }

    private boolean checkKeys(PriceInfo p1, PriceInfo p2) {
        return Objects.equals(p1.getProductCode(), p2.getProductCode()) &&
                p1.getNumber() == p2.getNumber() &&
                p1.getDepart() == p2.getDepart();
    }

    private void add(List<PriceInfo> list, PriceInfo value) {
        PriceInfo last = !list.isEmpty() ? list.get(list.size() - 1) : null;
        if (last != null && checkKeys(last, value) && last.getValue() == value.getValue() && last.getEnd().equals(value.getBegin())) {
            last.setEnd(max(last.getEnd(), value.getEnd()));
        } else {
            list.add(value);
        }
    }

    public List<PriceInfo> merge(List<PriceInfo> oldPrices, List<PriceInfo> newPrices) {
        Map<MapKey, List<PriceInfo>> separatedOldPriceInfos = separateAndCheckCorrectness(oldPrices, true);
        Map<MapKey, List<PriceInfo>> separatedNewPriceInfos = separateAndCheckCorrectness(newPrices, false);
        List<PriceInfo> result = new ArrayList<>();
        for (Map.Entry<MapKey, List<PriceInfo>> entry : separatedOldPriceInfos.entrySet()) {
            List<PriceInfo> oldList = entry.getValue();
            List<PriceInfo> newList = separatedNewPriceInfos.get(entry.getKey());
            if (newList == null) {
                result.addAll(oldList);
                continue;
            }
            int i = 0, j = 0;
            while (i < oldList.size()) {
                PriceInfo oldInfo = oldList.get(i);
                if (!result.isEmpty() && checkKeys(result.get(result.size() - 1), oldInfo)) {
                    oldInfo.setBegin(max(oldInfo.getBegin(), result.get(result.size() - 1).getEnd()));
                }
                while (j < newList.size() && oldInfo.getBegin().compareTo(newList.get(j).getEnd()) > 0) {
                    add(result, newList.get(j));
                    j++;
                }
                while (j < newList.size() && oldInfo.getEnd().compareTo(newList.get(j).getBegin()) >= 0) {
                    PriceInfo newInfo = newList.get(j);
                    if (oldInfo.getValue() == newInfo.getValue()) {
                        oldInfo.setBegin(min(oldInfo.getBegin(), newInfo.getBegin()));
                        oldInfo.setEnd(max(oldInfo.getEnd(), newInfo.getEnd()));
                        j++;
                    } else {
                        if (oldInfo.getBegin().compareTo(newInfo.getBegin()) < 0) {
                            PriceInfo priceInfo = new PriceInfo();
                            priceInfo.setProductCode(oldInfo.getProductCode());
                            priceInfo.setNumber(oldInfo.getNumber());
                            priceInfo.setDepart(oldInfo.getDepart());
                            priceInfo.setBegin(oldInfo.getBegin());
                            priceInfo.setEnd(newInfo.getBegin());
                            priceInfo.setValue(oldInfo.getValue());
                            add(result, priceInfo);
                            oldInfo.setBegin(newInfo.getEnd());
                        } else {
                            oldInfo.setBegin(newInfo.getEnd());
                        }
                        if (newInfo.getEnd().compareTo(oldInfo.getEnd()) <= 0) {
                            add(result, newInfo);
                            j++;
                        } else {
                            break;
                        }
                    }
                }
                if (oldInfo.getBegin().compareTo(oldInfo.getEnd()) < 0) {
                    add(result, oldInfo);
                }
                i++;
            }
            while (j < newList.size()) {
                add(result, newList.get(j++));
            }
        }
        for (Map.Entry<MapKey, List<PriceInfo>> entry : separatedNewPriceInfos.entrySet()) {
            if (!separatedOldPriceInfos.containsKey(entry.getKey())) {
                result.addAll(entry.getValue());
            }
        }
        return result;
    }
}
