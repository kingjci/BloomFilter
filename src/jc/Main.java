package jc;

import jc.component.Article;
import jc.component.BloomFilter;

public class Main {

    public static void main(String[] args) {
        System.out.println("Hello World!");

        BloomFilter<Article> bloomFilter = new BloomFilter<>(100);

        Article article = new Article();
        article.setName("aaaaaa");
        article.setPage(44);
        Article test = new Article();
        test.setName("aaaaaa");
        test.setPage(44);
        Article test1 = new Article();
        test1.setName("baaaaa");
        test1.setPage(44);
        Article test2 = new Article();
        test2.setName("aaaaaa");
        test2.setPage(43);
        Article test3 = new Article();
        test2.setName("aaaaaadaf");
        test2.setPage(43);

        bloomFilter.add(article);
        bloomFilter.add(test);
        bloomFilter.add(test1);
        bloomFilter.add(test2);
        boolean result = bloomFilter.contains(test);
        result = bloomFilter.contains(test1);
        result = bloomFilter.contains(test2);
        result = bloomFilter.contains(test3);



        System.out.println(".......");

    }
}
