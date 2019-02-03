package models;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
public class Rent {

    @Id
    @GeneratedValue
    private Long id;
    private Boolean isOpinionProvided;

    @ManyToOne
    private Customer customer;

    @ManyToOne
    private MovieCopy movieCopy;


}
