package com.example.our_e_commerce.model;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.Type;

import java.sql.Blob;

//@Data {not preferable to use this with Entity}
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class Image {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String fileName;

    private String fileType;

    /*
    In Java Spring, Blob stands for "Binary Large Object."
     It is used to store large binary data, such as images, videos, or files, in a database.
     */
    @Lob
    private Blob image; //"Binary Large Object."
    private String downloadUrl;


    @ManyToOne
    @JoinColumn(name = "product_id")
    private Product product;
}
