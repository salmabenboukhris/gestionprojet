package ma.toubkalit.suiviprojet.services;

import ma.toubkalit.suiviprojet.dto.employe.EmployeRequestDto;
import ma.toubkalit.suiviprojet.dto.employe.EmployeResponseDto;
import ma.toubkalit.suiviprojet.dto.employe.EmployeSearchResponseDto;

import java.time.LocalDate;
import java.util.List;

public interface EmployeService {

    EmployeResponseDto create(EmployeRequestDto requestDto);

    EmployeResponseDto update(Long id, EmployeRequestDto requestDto);

    EmployeResponseDto getById(Long id);

    List<EmployeSearchResponseDto> search(String matricule, String login, String email, String nom);

    void delete(Long id);

    List<EmployeSearchResponseDto> getDisponibles(LocalDate dateDebut, LocalDate dateFin);

    EmployeResponseDto getCurrentUser(String login);

    void changePassword(String login, String oldPassword, String newPassword);
}