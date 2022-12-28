package com.clankalliance.backbeta.entity.blog;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class BlogDetail {

    private String content;

    private List<Comment> commentList;

}
