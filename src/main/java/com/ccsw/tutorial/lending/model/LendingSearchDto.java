package com.ccsw.tutorial.lending.model;

import com.ccsw.tutorial.common.pagination.PageableRequest;

import java.time.LocalDate;

public class LendingSearchDto {

    private PageableRequest pageableRequest;
    private Long customerId;
    private Long gameId;
    private LocalDate date;

    public PageableRequest getPageableRequest() {
        return pageableRequest;
    }

    public void setPageableRequest(PageableRequest pageableRequest) {
        this.pageableRequest = pageableRequest;
    }

    public Long getCustomerId() {
        return customerId;
    }

    public void setCustomerId(Long customerId) {
        this.customerId = customerId;
    }

    public Long getGameId() {
        return gameId;
    }

    public void setGameId(Long gameId) {
        this.gameId = gameId;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }
}
