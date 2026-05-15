package com.ccsw.tutorial.lending;

import com.ccsw.tutorial.lending.model.Lending;
import com.ccsw.tutorial.lending.model.LendingDto;
import com.ccsw.tutorial.lending.model.LendingSearchDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.web.bind.annotation.*;

import java.util.stream.Collectors;

@RestController
@RequestMapping(value = "/lending")
@CrossOrigin(origins = "*")
@Tag(name = "Lending", description = "API of Lending")
public class LendingController {

    @Autowired
    private LendingService lendingService;

    @Autowired
    private ModelMapper mapper;

    /**
     * Método para recuperar un listado paginado de {@link Lending}
     *
     * @param searchDto dto de búsqueda
     * @return {@link Page} de {@link LendingDto}
     */
    @PostMapping(path = "")
    @Operation(summary = "Find Page", description = "Method that return a page of Lending")
    public Page<LendingDto> findPage(@RequestBody LendingSearchDto searchDto) {
        Page<Lending> page = this.lendingService.findPage(searchDto);

        return new PageImpl<>(page.getContent().stream().map(e -> mapper.map(e, LendingDto.class)).collect(Collectors.toList()), page.getPageable(), page.getTotalElements());
    }

    /**
     * Método para crear o actualizar un {@link Lending}
     *
     * @param id PK de la entidad
     * @param lendingDto datos de la entidad
     */
    @PutMapping(path = { "", "/{id}" })
    @Operation(summary = "Save or Update", description = "Method that saves or updates a Landing")
    public void save(@PathVariable(name = "id", required = false) Long id, @RequestBody LendingDto lendingDto) throws Exception {
        this.lendingService.save(id, lendingDto);
    }

    /**
     * Método para borrar un {@link Lending}
     *
     * @param id PK de la entidad
     */
    @DeleteMapping(path = "/{id}")
    @Operation(summary = "Delete", description = "Method that deletes a Landing")
    public void delete(@PathVariable(name = "id") Long id) throws Exception {
        this.lendingService.delete(id);
    }
}
