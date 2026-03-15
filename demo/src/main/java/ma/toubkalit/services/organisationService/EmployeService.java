package ma.toubkalit.services.organisationService;

import ma.toubkalit.entity.organisation.Employe;

import java.util.List;

public interface EmployeService {

    Employe saveEmploye(Employe employe);

    Employe updateEmploye(Integer id, Employe employe);

    Employe getEmployeById(Integer id);

    Employe getEmployeByMatricule(String matricule);

    Employe getEmployeByLogin(String login);

    Employe getEmployeByEmail(String email);

    List<Employe> getAllEmployes();

    List<Employe> getEmployesByProfil(Integer profilId);

    void deleteEmploye(Integer id);
}