package project.bayaraja.application.utils;

import lombok.*;

import java.util.List;

@Getter @Setter @Builder
@AllArgsConstructor @NoArgsConstructor
public class BaseResponse<T> {
    private List<String> messages;
    private T payload;
}
