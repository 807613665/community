package com.lchcommunity.community.dto;

import lombok.Data;

import java.util.List;
@Data
public class TagDTO {
    private String title;
    private List<String> tag;
}
