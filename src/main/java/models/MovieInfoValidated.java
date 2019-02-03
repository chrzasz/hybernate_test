package models;

import lombok.*;
import org.hibernate.validator.constraints.Length;

import javax.persistence.*;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.List;

@Data
@AllArgsConstructor(access = AccessLevel.PACKAGE)
@NoArgsConstructor
@Builder
@Entity
public class MovieInfoValidated {

    @Id
    @GeneratedValue
    private Long id;

    @NotNull
    private String title;

    @Min(0)
    @Max(10)
    private Double avgScore;

    @Length(min = 100) //uwaga domyslna wartosc kolumny 255
//    @Column(length = 2000) //default 255
    private String description;

    private LocalDate releaseDate;

    @Transient
    private Long daysSinceRelease;

    @PostLoad
    public void calculateDaysSinceRelease() {
        this.daysSinceRelease = ChronoUnit.DAYS.between(releaseDate, LocalDate.now());
    }

    @OneToMany(mappedBy = "movieInfo", orphanRemoval = true, cascade = {CascadeType.PERSIST})
    private List<MovieCopy> copies;

    @OneToMany(mappedBy = "movieInfo", orphanRemoval = true, cascade = {CascadeType.PERSIST})
    private List<Rank> ranks;
}
