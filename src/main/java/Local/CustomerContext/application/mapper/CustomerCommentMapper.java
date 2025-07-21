package Local.CustomerContext.application.mapper;

import Local.CustomerContext.application.DTO.response.CustomerCommentDTO;
import Local.CustomerContext.application.DTO.response.CustomerCommentSummaryDTO;
import Local.CustomerContext.domain.model.Comment;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

import java.util.List;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface CustomerCommentMapper {

    @Mapping(source = "customer.id", target = "customerId")
    @Mapping(source = "productId", target = "productId")
    @Mapping(source = "commentText", target = "commentText")
    @Mapping(source = "rating", target = "rating")
    @Mapping(source = "visible", target = "isVisible")
    @Mapping(source = "createdAt", target = "createdAt")
    @Mapping(source = "updatedAt", target = "updatedAt")
    CustomerCommentDTO toCommentDTO(Comment comment);

    List<CustomerCommentDTO> toCommentDTOList(List<Comment> comments);

    // Дополнительный маппинг для краткой информации о комментарии
    @Mapping(source = "customer.id", target = "customerId")
    @Mapping(source = "productId", target = "productId")
    @Mapping(source = "rating", target = "rating")
    @Mapping(source = "visible", target = "isVisible")
    CustomerCommentSummaryDTO toCommentSummaryDTO(Comment comment);

    List<CustomerCommentSummaryDTO> toCommentSummaryDTOList(List<Comment> comments);
}
