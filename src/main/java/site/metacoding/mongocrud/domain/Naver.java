package site.metacoding.mongocrud.domain;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Document(collection = "navers") // 몽고의 greendb에 연결
public class Naver {

    @Id
    private String _id; // 몽고 PK는 String 해시값
    private String company;
    private String title;
}
