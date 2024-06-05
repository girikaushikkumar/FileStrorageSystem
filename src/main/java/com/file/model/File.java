package com.file.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document
@Data
@NoArgsConstructor
@AllArgsConstructor
public class File {
    @Id
    private String id;
    private String filename;
    private String fileType;
    private String fileSize;
    private byte[] file;
}
