package edu.mci.snacktrack.dto;

import edu.mci.snacktrack.model.Order;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CustomerDTO {

    private Long customerId;
    private String firstName;
    private String lastName;
    private String email;
    private String address;
    private List<Order> orderHistory;
}
