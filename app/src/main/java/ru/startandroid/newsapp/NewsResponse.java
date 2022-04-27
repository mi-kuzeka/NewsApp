package ru.startandroid.newsapp;

import java.util.List;

public class NewsResponse {
    // The list of the news items
    private final List<News> newsList;
    // Total count of items in the response
    private int itemsCount;
    // Count of items for one page
    private int pageSize;
    // The number of the current page
    private int currentPage;
    // Count of pages for this request
    private int pagesCount;

    /**
     * Constructs a new {@link NewsResponse} object
     *
     * @param newsList    is the list of the news items
     * @param itemsCount  is total count of items in the response
     * @param pageSize    is count of items for one page
     * @param currentPage is the number of the current page
     * @param pagesCount  is count of pages for this request
     */
    public NewsResponse(List<News> newsList, int itemsCount, int pageSize,
                        int currentPage, int pagesCount) {
        this.newsList = newsList;
        this.itemsCount = itemsCount;
        this.pageSize = pageSize;
        this.currentPage = currentPage;
        this.pagesCount = pagesCount;
    }

    public List<News> getNewsList() {
        return this.newsList;
    }

    public int getItemsCount() {
        return this.itemsCount;
    }

    public int getPageSize() {
        return this.pageSize;
    }

    public int getCurrentPage() {
        return this.currentPage;
    }

    public int getPagesCount() {
        return this.pagesCount;
    }

    public void clear() {
        this.newsList.clear();
        this.itemsCount = 0;
        this.pageSize = 0;
        this.currentPage = 0;
        this.pagesCount = 0;
    }
}
