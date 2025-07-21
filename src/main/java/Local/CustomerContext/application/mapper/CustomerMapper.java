package Local.CustomerContext.application.mapper;

import Local.CustomerContext.application.DTO.response.AddressData;
import Local.CustomerContext.application.DTO.response.CustomerDTO;
import Local.CustomerContext.domain.model.Customer;
import Local.CustomerContext.domain.model.valueObject.Address;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

import java.util.List;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface CustomerMapper {

    @Mapping(source = "customerName.firstName", target = "firstName")
    @Mapping(source = "customerName.lastName", target = "lastName")
    @Mapping(source = "email.email", target = "email")
    @Mapping(source = "phoneNumber.value", target = "phone")
    @Mapping(source = "address", target = "address")
    CustomerDTO toDTO(Customer customer);

    List<CustomerDTO> toDTOList(List<Customer> customers);

    Address toAddress(AddressData addressData);

    AddressData toAddressData(Address address);
}
