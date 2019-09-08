package com.annasizova.loftcoin.data;

import com.annasizova.loftcoin.db.CoinEntity;
import com.annasizova.loftcoin.util.Function;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
class CoinsMapper implements Function<Listings, List<CoinEntity>> {

    @Inject CoinsMapper() {}

    @Override
    public List<CoinEntity> apply(Listings listings) {
        if (listings != null && listings.data != null) {
            final List<CoinEntity> entities = new ArrayList<>();
            for (final Coin coin : listings.data) {
                double price = 0d;
                double change24 = 0d;
                final Iterator<Quote> quotes = coin.getQuotes().values().iterator();
                if (quotes.hasNext()) {
                    final Quote quote = quotes.next();
                    if (quote != null) {
                        price = quote.getPrice();
                        change24 = quote.getChange24h();
                    }
                }
                entities.add(CoinEntity.create(coin.getId(), coin.getName(), coin.getSymbol(), price, change24));
            }
            return Collections.unmodifiableList(entities);
        }
        return Collections.emptyList();
    }
}
