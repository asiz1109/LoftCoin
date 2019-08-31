//package com.annasizova.loftcoin.rate;
//
//import androidx.core.util.Pair;
//
//import com.annasizova.loftcoin.data.Coin;
//import com.annasizova.loftcoin.data.Currencies;
//import com.annasizova.loftcoin.data.Quote;
//import com.annasizova.loftcoin.util.ChangeFormat;
//import com.annasizova.loftcoin.util.Function;
//import com.annasizova.loftcoin.util.ImgUrlFormat;
//import com.annasizova.loftcoin.util.PriceFormat;
//
//import java.util.ArrayList;
//import java.util.Collections;
//import java.util.Currency;
//import java.util.List;
//import java.util.Locale;
//import java.util.Objects;
//
//import javax.inject.Inject;
//
//import dagger.Reusable;
//
//@Reusable
//class RateMapper implements Function<List<Coin>, List<CoinRate>> {
//
//    private final ImgUrlFormat imgUrlFormat;
//    private final PriceFormat priceFormat;
//    private final ChangeFormat changeFormat;
//    private Currencies currencies;
//
//    @Inject
//    RateMapper(ImgUrlFormat imgUrlFormat, PriceFormat priceFormat, ChangeFormat changeFormat, Currencies currencies) {
//        this.imgUrlFormat = imgUrlFormat;
//        this.priceFormat = priceFormat;
//        this.changeFormat = changeFormat;
//        this.currencies = currencies;
//    }
//
//    @Override
//    public List<CoinRate> apply(List<Coin> coins) {
//        final Pair<Currency, Locale> pair = currencies.getCurrent();
//        final String currencyCode = Objects.requireNonNull(pair.first).getCurrencyCode();
//        final List<CoinRate> rates = new ArrayList<>(coins.size());
//        for (final Coin coin : coins) {
//            final CoinRate.Builder builder = CoinRate.builder()
//                    .id(coin.getId()).symbol(coin.getSymbol()).imageUrl(imgUrlFormat.format(coin.getId()));
//            final Quote quote = coin.getQuotes().get(currencyCode);
//            if (quote != null) {
//                builder.price(priceFormat.format(quote.getPrice()));
//                builder.change24h(changeFormat.format(quote.getChange24h()));
//                builder.isChange24hNegative(quote.getChange24h() < 0d);
//            }
//            rates.add(builder.build());
//        }
//        return Collections.unmodifiableList(rates);
//    }
//}
