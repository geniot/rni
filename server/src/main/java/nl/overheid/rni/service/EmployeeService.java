package nl.overheid.rni.service;

import nl.overheid.rni.dto.EmployeeInDto;
import nl.overheid.rni.dto.EmployeeOutDto;
import nl.overheid.rni.dto.FilterDto;
import nl.overheid.rni.dto.SortDto;
import nl.overheid.rni.entity.Employee;
import nl.overheid.rni.mapper.EmployeeMapper;
import nl.overheid.rni.repository.EmployeeRepository;
import nl.overheid.rni.repository.specification.EmployeeSpecification;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Service
@RequiredArgsConstructor
@Slf4j
public class EmployeeService {

    private final EmployeeRepository employeeRepository;

    public Page<EmployeeOutDto> searchEmployeeWithPaginationSortingAndFiltering(EmployeeInDto employeeInDto) {

        FilterDto filterDto = FilterDto.builder()
                .salary(employeeInDto.getSalary())
                .birthYear(employeeInDto.getBirthYear())
                .build();

        List<SortDto> sortDtos = jsonStringToSortDto(employeeInDto.getSort());
        List<Sort.Order> orders = new ArrayList<>();

        if (sortDtos != null) {
            for (SortDto sortDto : sortDtos) {
                Sort.Direction direction = Objects.equals(sortDto.getDirection(), "desc") ? Sort.Direction.DESC : Sort.Direction.ASC;
                orders.add(new Sort.Order(direction, sortDto.getField()));
            }
        }

        log.info("sort{}", sortDtos);
        log.info("filter{}", filterDto);

        PageRequest pageRequest = PageRequest.of(employeeInDto.getPage(), employeeInDto.getSize(), Sort.by(orders));

        Specification<Employee> specification = EmployeeSpecification.getSpecification(filterDto);

        Page<Employee> employees = employeeRepository.findAll(specification, pageRequest);

        return employees.map(EmployeeMapper.INSTANCE::employeeEntityToEmployeeDto);
    }

    private List<SortDto> jsonStringToSortDto(String jsonString) {
        try {
            ObjectMapper obj = new ObjectMapper();
            return obj.readValue(jsonString, new TypeReference<>() {
            });
        } catch (Exception e) {
            log.info("Exception: {}", e);
            return null;
        }
    }

    public byte[] generateFileContent() {


        List<Employee> employees = employeeRepository.fetchEmployees();
        log.info("Fetched employees: {}", employees);

        List<EmployeeOutDto> employeeDtos = employees.stream().map(EmployeeMapper.INSTANCE::employeeEntityToEmployeeDto).toList();

        return writeEmployees(employeeDtos);

    }

    private byte[] writeEmployees(List<EmployeeOutDto> employeeDtos) {

        return new byte[]{};

    }


}
