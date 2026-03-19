package com.mycompany.myapp.service.mapper;

import com.mycompany.myapp.domain.Inventario;
import com.mycompany.myapp.domain.MovimientoInventario;
import com.mycompany.myapp.service.dto.InventarioDTO;
import com.mycompany.myapp.service.dto.MovimientoInventarioDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link MovimientoInventario} and its DTO {@link MovimientoInventarioDTO}.
 */
@Mapper(componentModel = "spring")
public interface MovimientoInventarioMapper extends EntityMapper<MovimientoInventarioDTO, MovimientoInventario> {
    @Mapping(target = "inventario", source = "inventario", qualifiedByName = "inventarioNombre")
    MovimientoInventarioDTO toDto(MovimientoInventario s);

    @Named("inventarioNombre")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    @Mapping(target = "nombre", source = "nombre")
    InventarioDTO toDtoInventarioNombre(Inventario inventario);
}
