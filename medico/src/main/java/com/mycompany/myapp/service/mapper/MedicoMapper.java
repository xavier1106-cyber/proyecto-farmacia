package com.mycompany.myapp.service.mapper;

import com.mycompany.myapp.domain.Medico;
import com.mycompany.myapp.service.dto.MedicoDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Medico} and its DTO {@link MedicoDTO}.
 */
@Mapper(componentModel = "spring")
public interface MedicoMapper extends EntityMapper<MedicoDTO, Medico> {}
