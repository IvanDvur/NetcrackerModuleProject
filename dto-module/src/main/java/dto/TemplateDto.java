package dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.core.io.Resource;



@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class TemplateDto {

    private Resource templatePreview;
    private String templateId;

}
