package br.com.looplex.models;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
@NoArgsConstructor
public class ElasticsearchUser {

    private String username;
    private String email;
    private String full_name;

    private Object metadata;

    private String password;
    private String password_hash;

    private ArrayList<String> roles = new ArrayList<String>();

    private Boolean enabled;

}
