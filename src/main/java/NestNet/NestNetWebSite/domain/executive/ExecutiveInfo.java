package NestNet.NestNetWebSite.domain.executive;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * 년도별 임원 정보 엔티티
 */
@Entity
@Getter
@NoArgsConstructor
public class ExecutiveInfo {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "executive_info_id")
    private Long id;                                            // PK

    private int year;                                           // 년도

    private String name;                                        // 이름

    private String studentId;                                   // 학번

    private String role;                                        // 직책

    /*
    생성자
     */
    public ExecutiveInfo(int year, String name, String studentId, String role){
        this.year = year;
        this.name = name;
        this.studentId = studentId;
        this.role = role;
    }

    //== 비지니스 로직 ==//
    /*
    임원 정보 수정
     */
    public void modifyInfo(int year, String name, String studentId, String role){
        this.year = year;
        this.name = name;
        this.studentId = studentId;
        this.role = role;
    }
}
