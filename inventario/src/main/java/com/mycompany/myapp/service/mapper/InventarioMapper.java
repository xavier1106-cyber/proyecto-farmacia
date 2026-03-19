package com.mycompany.myapp.service.mapper;

import com.mycompany.myapp.domain.Inventario;
import com.mycompany.myapp.service.dto.InventarioDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Inventario} and its DTO {@link InventarioDTO}.
 */
@Mapper(componentModel = "spring")
public interface InventarioMapper extends EntityMapper<InventarioDTO, Inventario> {}
