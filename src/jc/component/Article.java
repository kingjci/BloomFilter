package jc.component;

import java.io.Serializable;

/**
 * Created by 金成 on 2016/4/12.
 * 测试类，实现Serializable接口
 */
public class Article implements Serializable{

    private String name;
    private int page;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getPage() {
        return page;
    }

    public void setPage(int page) {
        this.page = page;
    }
}
