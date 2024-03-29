package example.micronaut.domain;

import java.io.Serializable;

import lombok.Data;

@Data
public class FuncResponse implements Serializable {
    private static final long serialVersionUID = 1L;

    private String result;
    private String url;
}
