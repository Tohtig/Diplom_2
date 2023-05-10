package model.orders;

import com.google.gson.annotations.SerializedName;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;

import java.util.ArrayList;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Accessors(chain = true)
public class OrdersResponse {
    private String success;
    private String name;
    @SerializedName("orders")
    private ArrayList<Orders> orders;
    private Order order;
    private Integer total;
    private Integer totalToday;
}
