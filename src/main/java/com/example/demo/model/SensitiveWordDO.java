package com.example.demo.model;

import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.Table;

/**
 * 敏感詞信息
 *
 * @author zhou.xy
 * @date 2019/9/10
 * @since 1.0
 */
@Data
@EqualsAndHashCode(of = "id")
@Table(name = "mz_sensitive_word")
public class SensitiveWordDO {
    private Long id;
    private String word;
}
