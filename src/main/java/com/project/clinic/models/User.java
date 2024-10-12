package com.project.clinic.models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;



@Entity
@Data
@Table(name = "app_user")
public class User {

    @Id
    private String username;
    private String password;
    private String roles; // Store roles as a comma-separated string (e.g., "ROLE_ADMIN,ROLE_RECEPTION")
}
