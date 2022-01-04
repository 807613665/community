package com.lchcommunity.community.dto;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class PaginationDTO {
    private List<QuestionDTO> questionDTOList;
    private boolean showFirstPage;
    private boolean showLastPage;
    private boolean showNextPage;
    private boolean showEndPage;
    private Integer page;
    private Integer pageSum;
    private Integer size;
    private List<Integer> pages = new ArrayList<>();

    public void setPagination(Integer questionCount, Integer temPage, Integer temSize) {
        //设置当前页数
        page = temPage;
        size = temSize;
        //计算页数总和
        int temPageSum = questionCount / temSize;
        if (questionCount % size != 0)
            temPageSum++;
        pageSum = temPageSum;

        if (page < 1)
            page = 1;
        if (page > pageSum)
            page = pageSum;
        //上一页和下一页是否显示
        if (page != 1)
            showLastPage = true;
        else
            showLastPage = false;
        if (page != pageSum)
            showNextPage = true;
        else
            showNextPage = false;
        if (page - 1 >= 3)
            showFirstPage = true;
        else
            showFirstPage = false;
        if (page + 2 <= pageSum)
            showEndPage = true;
        else
            showEndPage = false;

        for (int i = Math.max(1, page - 2); i <= Math.min(page + 2, pageSum); i++)
            pages.add(i);
    }
}
