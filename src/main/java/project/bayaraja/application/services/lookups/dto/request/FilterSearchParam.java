package project.bayaraja.application.services.lookups.dto.request;

import lombok.*;

@Builder @AllArgsConstructor @NoArgsConstructor
@Getter @Setter
public class FilterSearchParam {
    private String type;
    private String key;
    private String value;
}
