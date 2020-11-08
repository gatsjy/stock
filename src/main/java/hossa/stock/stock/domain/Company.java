package hossa.stock.stock.domain;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.springframework.stereotype.Component;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

/**
 * @author Gatsjy
 * @since 2020-11-08
 * realize dreams myself
 * Blog : https://blog.naver.com/gkswndks123
 * Github : https://github.com/gatsjy
 */
@Entity
@Getter
@Setter
@Component
@RequiredArgsConstructor
public class Company {
    @Id @GeneratedValue
    Long id;
    String name;
    String stock_id;

    public Company(String name, String stock_id) {
        this.name = name;
        this.stock_id = stock_id;
    }
}
