package net.unit8.lightningroar.boundary;

import lombok.Data;

@Data
public class FeedCreateRequest extends BoundaryBase {
    private String name;
    private String iconUrl;
}
