package ma.toubkalit.dto;

import lombok.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DashboardDTO {
    private long totalProjets;
    private long projetsEnCours;
    private double montantTotalProjets;
    private long totalEmployes;
    private long totalOrganismes;
    private double montantTotalFacture;
    private double montantTotalPaye;
}
