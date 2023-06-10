package dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
public class StatusPerClientUpdateRequest {
    private String orderId;
    private List<String> clientsID;

}
