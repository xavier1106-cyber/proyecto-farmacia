package com.mycompany.myapp.service.mapper;

import com.mycompany.myapp.domain.Historico;
import com.mycompany.myapp.service.dto.HistoricoDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Historico} and its DTO {@link HistoricoDTO}.
 */
@Mapper(componentModel = "spring")
public interface HistoricoMapper extends EntityMapper<HistoricoDTO, Historico> {}
