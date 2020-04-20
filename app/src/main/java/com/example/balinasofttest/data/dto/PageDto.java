package com.example.balinasofttest.data.dto;
import com.google.gson.annotations.SerializedName;
import java.util.List;

public class PageDto {

    @SerializedName("content")
    List<PhotoTypeDtoOut> list;
    @SerializedName("page")
    Integer page;
    @SerializedName("pageSize")
    Integer pageSize;
    @SerializedName("totalElements")
    Integer totalElements;
    @SerializedName("totalPages")
    Integer totalPages;

    public PageDto() {
    }

    public PageDto(List<PhotoTypeDtoOut> list, Integer page, Integer pageSize, Integer totalElements, Integer totalPages) {
        this.list = list;
        this.page = page;
        this.pageSize = pageSize;
        this.totalElements = totalElements;
        this.totalPages = totalPages;
    }

    public List<PhotoTypeDtoOut> getList() {
        return list;
    }

    public Integer getPage() {
        return page;
    }

    public Integer getPageSize() {
        return pageSize;
    }

    public Integer getTotalElements() {
        return totalElements;
    }

    public Integer getTotalPages() {
        return totalPages;
    }

    @Override
    public String toString() {
        return "PageDto{" +
                "list=" + list +
                ", page=" + page +
                ", pageSize=" + pageSize +
                ", totalElements=" + totalElements +
                ", totalPages=" + totalPages +
                '}';
    }
}
