package com.example.demo.system.entity;


import lombok.Data;

/**
 * @author 高先华
 */
@Data
public class Headers {

    private String label;

    private String value;

    public Headers(String label, String value) {
        this.label = label;
        this.value = value;
    }
}
