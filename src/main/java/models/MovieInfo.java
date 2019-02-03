package models;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
public class MovieInfo {

    @Id
    @GeneratedValue
    private Long id;

    private String title;
    private Double avgScore;

    @OneToMany(orphanRemoval = true, mappedBy = "movieInfo")
    private List<MovieCopy> copies;

    @OneToMany(orphanRemoval = true, mappedBy = "movieInfo")
    private List<Rank> ranks;
}
