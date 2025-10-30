package nl.overheid.rni.mapper;

import nl.overheid.rni.dto.EmployeeOutDto;
import nl.overheid.rni.entity.Employee;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper()
public interface EmployeeMapper {

    EmployeeMapper INSTANCE = Mappers.getMapper(EmployeeMapper.class);

    EmployeeOutDto employeeEntityToEmployeeDto(Employee employee);
}
