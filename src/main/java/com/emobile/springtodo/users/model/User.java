package com.emobile.springtodo.users.model;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@Builder( toBuilder = true)
@ToString
@EqualsAndHashCode
@AllArgsConstructor
@NoArgsConstructor
public class User {

    private Long id;

    private String email;

    private String password;
}