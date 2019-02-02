package models;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

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
    private Customer customer;
    @ManyToOne
    private MovieCopy movieCopy;
}
