# BloomFilter
Implementation of bloom filter which is easy to use, it can expend itself
automatically

### Demon
BloomFilter<Article> bloomFilter = new BloomFilter();
//BloomFilter<Article> bloomFilter = new BloomFilter(size); // default size = 100000;
//BloomFilter<Article> bloomFilter = new BloomFilter(falsePositiveRate); // default falsePositiveRate = 0.01
//BloomFilter<Article> bloomFilter = new BloomFilter(size, falsePositiveRate);
Article article = new Article();
bloomFilter.add(article);
boolean result = bloomFilter.contains(article);

##### that's all