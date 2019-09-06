package com.annasizova.loftcoin.util;

public interface PriceFormat extends DoubleFormat {

    String format(double value, String sign);

}
