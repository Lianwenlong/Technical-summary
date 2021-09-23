package org.lian.arsenal.es;

import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * @author LianWL
 * @date 2021年09月23日 22:28
 */
@Data
@AllArgsConstructor
public class User {
    private String name;
    private String sex;
    private Integer age;
}
