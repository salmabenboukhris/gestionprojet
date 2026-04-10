package ma.toubkalit.suiviprojet.services.impl;

import ma.toubkalit.suiviprojet.dto.livrable.LivrableRequestDto;
import ma.toubkalit.suiviprojet.dto.livrable.LivrableResponseDto;
import ma.toubkalit.suiviprojet.entities.Livrable;
import ma.toubkalit.suiviprojet.entities.Phase;
import ma.toubkalit.suiviprojet.exceptions.ResourceNotFoundException;
import ma.toubkalit.suiviprojet.mappers.LivrableMapper;
import ma.toubkalit.suiviprojet.repositories.LivrableRepository;
import ma.toubkalit.suiviprojet.repositories.PhaseRepository;
import ma.toubkalit.suiviprojet.services.LivrableService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class LivrableServiceImpl implements LivrableService {

    private final LivrableRepository livrableRepository;
    private final PhaseRepository phaseRepository;
    private final LivrableMapper livrableMapper;

    public LivrableServiceImpl(LivrableRepository livrableRepository,
                               PhaseRepository phaseRepository,
                               LivrableMapper livrableMapper) {
        this.livrableRepository = livrableRepository;
        this.phaseRepository = phaseRepository;
        this.livrableMapper = livrableMapper;
    }

    @Override
    public LivrableResponseDto create(Long phaseId, LivrableRequestDto requestDto) {
        Phase phase = phaseRepository.findById(phaseId)
                .orElseThrow(() -> new ResourceNotFoundException("Phase introuvable avec l'id : " + phaseId));

        Livrable livrable = livrableMapper.toEntity(requestDto, phase);
        Livrable saved = livrableRepository.save(livrable);
        return livrableMapper.toResponseDto(saved);
    }

    @Override
    public List<LivrableResponseDto> getByPhaseId(Long phaseId) {
        phaseRepository.findById(phaseId)
                .orElseThrow(() -> new ResourceNotFoundException("Phase introuvable avec l'id : " + phaseId));

        return livrableRepository.findByPhaseId(phaseId).stream()
                .map(livrableMapper::toResponseDto)
                .toList();
    }

    @Override
    public LivrableResponseDto getById(Long id) {
        Livrable livrable = livrableRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Livrable introuvable avec l'id : " + id));

        return livrableMapper.toResponseDto(livrable);
    }

    @Override
    public LivrableResponseDto update(Long id, LivrableRequestDto requestDto) {
        Livrable livrable = livrableRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Livrable introuvable avec l'id : " + id));

        livrableMapper.updateEntity(livrable, requestDto);
        Livrable updated = livrableRepository.save(livrable);
        return livrableMapper.toResponseDto(updated);
    }

    @Override
    public void delete(Long id) {
        Livrable livrable = livrableRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Livrable introuvable avec l'id : " + id));

        livrableRepository.delete(livrable);
    }
}